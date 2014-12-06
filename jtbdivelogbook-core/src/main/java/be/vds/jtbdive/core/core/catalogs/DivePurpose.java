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

public enum DivePurpose  implements KeyedCatalog {
	SIGHTSEEING((short) 1, "sightseeing"), 
	PHOTOGRAPHY_VIDEO((short) 2, "photo.video"), 
	LEARNING((short) 3, "learning"), 
	TEACHING((short) 4, "teaching"), 
	RESEARCH((short) 5, "research"), 
	SPEARFISHING((short) 6, "spearfishing"), 
	PROFICIENCY((short) 7, "proficiency"), 
	WORK((short) 8, "work"), 
	OTHER((short) 99, "other"), 
	;

	private short id;
	private String key;

	private DivePurpose(short id, String key) {
		this.id = id;
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public short getId() {
		return id;
	}

	public static DivePurpose getDivePurpose(short id) {
		for (DivePurpose dp : values()) {
			if (dp.getId() == id){
				return dp;
			}
		}
		return null;
	}

}
