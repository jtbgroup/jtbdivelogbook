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

public enum DivePlatform implements KeyedCatalog {
	BEACH_SHORE((short) 1, "beach.shore"), 
	PIER((short) 2, "pier"), 
	SMALL_BOAT((short) 3, "boat.small"), 
	CHARTER_BOAT((short) 4, "boat.charter"), 
	LIVE_ABOARD((short) 5, "live.aboard"), 
	BARGE((short) 6, "barge"), 
	LANDSIDE((short) 7, "landside"), 
	HYPERBARIC_FACILITY((short) 8, "hyperbaric.facility"), 
	OTHER((short) 99, "other"), 
	;

	private short id;
	private String key;

	private DivePlatform(short id, String key) {
		this.id = id;
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public short getId() {
		return id;
	}

	public static DivePlatform getDivePlatform(short id) {
		for (DivePlatform pl : values()) {
			if (pl.getId() == id) {
				return pl;
			}
		}
		return null;
	}
}
