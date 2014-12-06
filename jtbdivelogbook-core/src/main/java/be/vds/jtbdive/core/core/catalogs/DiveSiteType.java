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

public enum DiveSiteType implements KeyedCatalog{
	UNKNOWN((short) 0, "unknown"), 
	OCEAN_SEA((short) 1, "ocean.sea"), 
	LAKE_QUARRY((short) 2, "lake.quarry"), 
	RIVER_SPRING((short) 3, "river.spring"), 
	CAVE_CAVERN((short) 4, "cave.cavern"), 
	POOL((short) 5, "pool"), 
	HYPERBARIC_CHAMBER((short) 6, "hyperbaric.chamber"), 
	OTHER((short) 99, "other");

	private String key;
	private short id;

	private DiveSiteType(short id, String key) {
		this.id = id;
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public short getId() {
		return id;
	}

	public static DiveSiteType getDiveSiteType(short id) {
		for (DiveSiteType type : values()) {
			if (type.getId() == id) {
				return type;
			}
		}
		return null;
	}

}
