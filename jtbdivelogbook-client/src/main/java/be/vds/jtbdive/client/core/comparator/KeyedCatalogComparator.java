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
package be.vds.jtbdive.client.core.comparator;

import java.util.Comparator;
import java.util.Locale;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.core.core.catalogs.KeyedCatalog;

public class KeyedCatalogComparator implements Comparator<KeyedCatalog> {
	private static final I18nResourceManager i18n = I18nResourceManager
			.sharedInstance();
	private Locale locale;

	public KeyedCatalogComparator() {
		this(i18n.getDefaultLocale());
	}

	public KeyedCatalogComparator(Locale locale) {
		this.locale = locale;
	}

	@Override
	public int compare(KeyedCatalog o1, KeyedCatalog o2) {
		return i18n.getString(o1.getKey(), locale).compareTo(
				i18n.getString(o2.getKey(), locale));
	}

}
