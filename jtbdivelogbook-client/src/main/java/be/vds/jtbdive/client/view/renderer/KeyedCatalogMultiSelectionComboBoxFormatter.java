/*
* Jt'B Dive Logbook - Electronic dive logbook.
* 
* Copyright (C) 2010  Gautier Vanderslyen
* 
* Jt'B Dive Logbook is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package be.vds.jtbdive.client.view.renderer;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtb.swing.component.MultiSelectionComboBoxFormatter;
import be.vds.jtbdive.core.core.catalogs.KeyedCatalog;

public class KeyedCatalogMultiSelectionComboBoxFormatter implements
		MultiSelectionComboBoxFormatter {
	private static final I18nResourceManager i18n = I18nResourceManager
			.sharedInstance();

	@Override
	public String format(Object object) {
		KeyedCatalog dp = (KeyedCatalog) object;
		return i18n.getString(dp.getKey());
	}

}
