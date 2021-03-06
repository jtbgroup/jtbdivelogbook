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

public enum DiveTankCompositeMaterial  implements KeyedCatalog {
	STEEL((short) 1, "steel"), 
	CARBON((short) 2, "carbon"), 
	ALUMINIUM((short) 3, "aluminium"), 
	OTHER((short) 99, "other"), 
	;

	private short id;
	private String key;

	private DiveTankCompositeMaterial(short id, String key) {
		this.id = id;
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public short getId() {
		return id;
	}

	public static DiveTankCompositeMaterial getDiveTankCompositeMaterial(short id) {
		for (DiveTankCompositeMaterial pl : values()) {
			if (pl.getId() == id){
				return pl;
			}
		}
		return null;
	}

	public static DiveTankCompositeMaterial getDiveTankCompositeMaterialByKey(String key) {
		for (DiveTankCompositeMaterial pl : values()) {
			if (pl.getKey() == key){
				return pl;
			}
		}
		return null;
	}
}
