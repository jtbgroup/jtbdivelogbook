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
package be.vds.jtbdive.core.core.catalogs;

public enum DiveType implements KeyedCatalog {
	NORMAL((short) 0, "normal"), UNDER_ICE((short) 1, "under.ice"), WRECK(
			(short) 2, "wreck"),
	// dive with stream
	CURRENT((short) 3, "stream"), SPELEO((short) 4, "speleo"), TECK((short) 5,
			"teck"), SWIMMINGPOOL((short) 6, "swimmingpool"), OTHER((short) 99,
			"other"), ;

	private short id;
	private String key;

	private DiveType(short id, String key) {
		this.id = id;
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public short getId() {
		return id;
	}

	public static DiveType getDiveType(short id) {
		for (DiveType dt : values()) {
			if (dt.getId() == id) {
				return dt;
			}
		}
		return null;
	}

}
