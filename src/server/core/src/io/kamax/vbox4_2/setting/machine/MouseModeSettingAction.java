/*
 * Hyperbox - Enterprise Virtualization Manager
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

package io.kamax.vbox4_2.setting.machine;

import org.altherian.hbox.constant.MachineAttribute;
import org.altherian.hbox.constant.MouseMode;
import org.altherian.setting.StringSetting;
import org.altherian.setting._Setting;
import org.altherian.vbox.settings.general.MouseModeSetting;
import io.kamax.vbox4_2.data.Mappings;
import io.kamax.vbox4_2.setting._MachineSettingAction;
import org.virtualbox_4_2.IMachine;
import org.virtualbox_4_2.LockType;
import org.virtualbox_4_2.PointingHIDType;

public class MouseModeSettingAction implements _MachineSettingAction {

   @Override
   public LockType getLockType() {
      return LockType.Write;
   }

   @Override
   public String getSettingName() {
      return MachineAttribute.MouseMode.toString();
   }

   @Override
   public void set(IMachine machine, _Setting setting) {
      PointingHIDType mouseMode = Mappings.get(MouseMode.valueOf(((StringSetting) setting).getValue()));
      machine.setPointingHIDType(mouseMode);
   }

   @Override
   public _Setting get(IMachine machine) {
      return new MouseModeSetting(Mappings.get(machine.getPointingHIDType()));
   }

}