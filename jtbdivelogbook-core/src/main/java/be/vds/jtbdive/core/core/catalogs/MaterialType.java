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

public enum MaterialType implements KeyedCatalog, OrderedCatalog {
	DIVE_TANK((short) 1, "dive.tank"), 
	FINS((short) 2, "fins"), 
	MASK((short) 3, "mask"), 
	WEIGHT_BELT((short) 4, "weight.belt"), 
	DIVE_COMPUTER((short) 5, "dive.computer"), 
	KNIFE((short) 6, "knife"), 
	BOOTS((short) 7, "boots"), 
	GLOVES((short) 8, "gloves"), 
	BUOYANCY_COMPENSATOR((short) 9, "buoyancy.compensator"), 
	COMPASS((short) 10, "compass"), 
	LIGHT((short) 11, "light"), 
	MANOMETER((short) 12, "manometer"), 
	REGULATOR((short) 13, "regulator"), 
	SNORKEL((short) 14, "snorkel"), 
	SUIT((short) 15, "suit"), 
	SCOOTER((short) 16, "scooter"), 
	WATCH((short) 17, "watch"), 
	HOOD((short) 18, "hood"), 
	REBREATHER((short) 19, "rebreather"), 
	CAMERA((short) 20, "camera"), 
	VIDEOCAMERA((short) 21, "videocamera"), 
	LEAD((short) 22, "lead"), 
	OTHER((short) 99, "other"), 
	;

	private String key;

	public String getKey() {
		return key;
	}

	public short getId() {
		return id;
	}

	private short id;

	private MaterialType(short id, String key) {
		this.id = id;
		this.key = key;
	}

	public static MaterialType getMaterialType(short id) {
		for (MaterialType mt : values()) {
			if (mt.getId() == id){
				return mt;
			}
		}
		return null;
	}

	@Override
	public int getOrder() {
		return id;
	}
}
