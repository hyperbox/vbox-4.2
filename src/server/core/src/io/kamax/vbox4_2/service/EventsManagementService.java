/*
 * Hyperbox - Virtual Infrastructure Manager
 * Copyright (C) 2013-2015 Maxime Dor
 * 
 * http://kamax.io/hbox/
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package io.kamax.vbox4_2.service;

import io.kamax.hbox.event._Event;
import io.kamax.hbox.exception.HyperboxException;
import io.kamax.hboxd.event._EventManager;
import io.kamax.hboxd.service.SimpleLoopService;
import io.kamax.tool.logging.Logger;
import io.kamax.vbox4_2.ErrorInterpreter;
import io.kamax.vbox4_2.VBox;
import io.kamax.vbox4_2.factory.EventBusFactory;
import io.kamax.vbox4_2.factory.EventFactory;
import java.util.Arrays;
import org.virtualbox_4_2.IEvent;
import org.virtualbox_4_2.IEventListener;
import org.virtualbox_4_2.VBoxEventType;
import org.virtualbox_4_2.VBoxException;

/**
 * Recommended way to handle events is the pasive implementation, which required polling to get new events.<br/>
 * This service will keep polling, transform events and feed them into hyperbox.
 *
 * @author max
 */
public final class EventsManagementService extends SimpleLoopService {

    private _EventManager evMgr;
    private IEventListener el;

    public EventsManagementService(_EventManager evMgr) {
        this.evMgr = evMgr;
    }

    @Override
    protected void beforeLooping() {
        setSleepingTime(0);

        el = VBox.get().getEventSource().createListener();
        VBox.get().getEventSource().registerListener(el, Arrays.asList(VBoxEventType.Any), false);
        Logger.debug("Virtualbox Event Manager Server started.");
    }

    @Override
    protected void afterLooping() throws HyperboxException {
        if (el != null) {
            VBox.get().getEventSource().unregisterListener(el);
            el = null;
        }
        Logger.debug("Virtualbox Event Manager Server stopped.");
    }

    @Override
    protected void doLoop() {
        try {
            VBox.getManager().waitForEvents(0); // Needed to clear the internal event queue, see https://www.virtualbox.org/ticket/13647
            IEvent rawEvent = VBox.get().getEventSource().getEvent(el, 1000);
            if (rawEvent != null) {
                Logger.debug("Got an event from Virtualbox: " + rawEvent.getClass().getName() + " - " + rawEvent.getType() + " - " + rawEvent);
                IEvent preciseRawEvent = EventFactory.getRaw(rawEvent);
                if (preciseRawEvent != null) {
                    Logger.debug("Event was processed to " + preciseRawEvent.getClass().getName() + " - " + preciseRawEvent.getType() + " - "
                            + preciseRawEvent);
                    EventBusFactory.post(preciseRawEvent);
                    _Event ev = EventFactory.get(preciseRawEvent);
                    if (ev != null) {
                        evMgr.post(ev);
                    }
                }
                VBox.get().getEventSource().eventProcessed(el, rawEvent);
            }
        } catch (VBoxException e) {
            throw ErrorInterpreter.transform(e);
        } catch (RuntimeException r) {
            if ((r.getMessage() != null) && r.getMessage().contains("Connection refused")) {
                Logger.error("Virtualbox broke the connection with us");
                Logger.exception(r);
                stop();
            } else {
                throw r;
            }
        } catch (Throwable t) {
            Logger.error("Unexpected error occured in the VBox Event Manager - " + t.getMessage());
            Logger.exception(t);
            stop();
        }
    }

    @Override
    public String getId() {
        return "vbox-4.2-EventMgmtSvc";
    }

}
