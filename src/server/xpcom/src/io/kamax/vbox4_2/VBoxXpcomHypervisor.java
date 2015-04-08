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

import org.altherian.hbox.exception.HypervisorException;
import org.altherian.hboxd.hypervisor.Hypervisor;
import org.altherian.tool.AxStrings;
import org.altherian.tool.logging.Logger;
import io.kamax.vbox.VirtualBox;
import io.kamax.vbox4_2.VBoxHypervisor;
import java.io.File;
import org.virtualbox_4_2.ISession;
import org.virtualbox_4_2.VirtualBoxManager;

@Hypervisor(
      id = VirtualBox.ID.XPCOM_4_2,
      typeId = VirtualBox.Type.XPCOM,
      vendor = VirtualBox.VENDOR,
      product = VirtualBox.PRODUCT,
      schemes = { VirtualBox.ID.XPCOM_4_2 })
public final class VBoxXpcomHypervisor extends VBoxHypervisor {

   private final String defaultHome = "/usr/lib/virtualbox";

   @Override
   public String getId() {
      return this.getClass().getAnnotation(Hypervisor.class).id();
   }

   @Override
   public String getTypeId() {
      return this.getClass().getAnnotation(Hypervisor.class).typeId();
   }

   @Override
   protected VirtualBoxManager connect(String options) {
      if (AxStrings.isEmpty(options)) {
         options = defaultHome;
      }

      Logger.debug("Options - " + options);
      Logger.debug("vbox.home - " + System.getProperty("vbox.home"));
      Logger.debug("java.library.path - " + System.getProperty("java.library.path"));
      File libxpcom = new File(options + "/libvboxjxpcom.so");
      Logger.debug("Lib exists - " + libxpcom.getAbsolutePath() + " - " + libxpcom.isFile());

      VirtualBoxManager mgr = VirtualBoxManager.createInstance(options);
      if (mgr.getVBox().getVersion().contains("OSE") && (mgr.getVBox().getRevision() < 50393)) {
         throw new HypervisorException(
               "XPCOM is only available on OSE from revision 50393 or greater. See https://www.virtualbox.org/ticket/11232 for more information.");
      } else if (mgr.getVBox().getRevision() < 92790) {
         throw new HypervisorException(
               "XPCOM is only available from revision 92790 or greater. See https://www.virtualbox.org/ticket/11232 for more information.");
      } else {
         return mgr;
      }
   }

   @Override
   protected void disconnect() {
      System.gc();
   }

   @Override
   protected ISession getSession() {
      return getMgr().getSessionObject();
   }

   @Override
   public void importAppliance(String applianceFile) {
      // TODO Auto-generated method stub

   }

}
