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

package io.kamax.vbox4_2.setting.storage;

import io.kamax.hbox.constant.MediumAttribute;
import io.kamax.hbox.exception.HyperboxException;
import io.kamax.setting.BooleanSetting;
import io.kamax.setting._Setting;
import io.kamax.vbox4_2.setting._MediumSettingAction;
import org.virtualbox_4_2.IMedium;

public class MediumReadOnlySettingAction implements _MediumSettingAction {

   @Override
   public String getSettingName() {
      return MediumAttribute.ReadOnly.toString();
   }

   @Override
   public void set(IMedium medium, _Setting setting) {
      throw new HyperboxException("Read-only setting");
   }

   @Override
   public _Setting get(IMedium medium) {
      return new BooleanSetting(MediumAttribute.ReadOnly, medium.getReadOnly());
   }

}
