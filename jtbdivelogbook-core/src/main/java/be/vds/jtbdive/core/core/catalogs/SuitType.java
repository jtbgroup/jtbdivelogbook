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

public enum SuitType implements KeyedCatalog {

	// dive-skin, wet-suit (also a "semi-dry suit" is classified here under),
	// dry-suit, hot-water-suit, other.
	WET((short) 0, "wet"), HALF_DRY((short) 1, "half.dry"), DRY((short) 2,
			"dry"), ;

	private short id;
	private String key;

	private SuitType(short id, String key) {
		this.id = id;
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}

	public short getId() {
		return id;
	}

	public static SuitType getSuitType(Short id) {
		for (SuitType st : values()) {
			if (st.getId() == id) {
				return st;
			}
		}
		return null;
	}

}
