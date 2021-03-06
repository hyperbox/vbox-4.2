/*
 * Hyperbox - Virtual Infrastructure Manager
 * Copyright (C) 2014-2015 Maxime Dor
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

package io.kamax.vbox4_2;

import org.virtualbox_4_2.ISession;
import org.virtualbox_4_2.IVirtualBox;
import org.virtualbox_4_2.VirtualBoxManager;

public class VBox {

    private static VBoxHypervisor hyp;

    public static void set(VBoxHypervisor hyp) {

        VBox.hyp = hyp;
    }

    public static void unset() {
        hyp = null;
    }

    public static VirtualBoxManager getManager() {
        return hyp.getMgr();
    }

    public static IVirtualBox get() {
        return getManager().getVBox();
    }

    public static ISession getSession() {
        return hyp.getSession();
    }

}
