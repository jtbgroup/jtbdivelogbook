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
package be.vds.jtbdive.client.view.core.browser.nodeconstructor;

import be.vds.jtbdive.core.core.catalogs.KeyedCatalog;

public enum LogBookSorting implements KeyedCatalog{

	DIVE_DATE("by.date"), //
	DIVE_NUMBER("by.number"), //
	DIVE_SITE("by.divesite"), //
	DIVE_YEAR("by.year"), //
	;
	private String key;

	private LogBookSorting(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public static LogBookSorting getLogBookSorting(String key) {
		for (LogBookSorting s : values()) {
			if (s.getKey().equals(key))
				return s;
		}
		return null;
	}
}
