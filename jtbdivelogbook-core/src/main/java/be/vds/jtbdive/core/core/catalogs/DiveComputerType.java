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

public enum DiveComputerType {
	AIRE("aladin.aire", "aladin_"+0x1b, 1), 
	ALADIN_AIR_Z("aladin.air.z", "aladin_"+0x34, 1), 
	ALADIN_AIR_Z2("aladin.air.z2","aladin_"+0x44, 1), 
	ALADIN_AIR_Z_O2("aladin.air.z.o2","aladin_"+0xa4, 1), 
	ALADIN_AIR_Z_NITROX("aladin.air.z.nitrox","aladin_"+0xf4, 1), 
	ALADIN_AIR_TWIN("aladin.air.twin","aladin_"+0x1c, 1), 
	ALADIN_SPORT_PLUS("aladin.sport.plus","aladin_"+0x1e, 1), 
	ALADIN_SPORT_PLUS2("aladin.sport.plus.2","aladin_"+0x3e, 1), 
	ALADIN_PRO("aladin.pro","aladin_"+0x1f, 1), 
	ALADIN_PRO2("aladin.pro.2","aladin_"+0x3f, 1), 
	ALADIN_PRO_ULTRA("aladin.pro.ultra","aladin_"+0xff, 1), 
	MARES_GENIUS("mares.genius","mares_"+0x44, 1), 
	SPIRO_MONITOR_2_PLUS("spiro.monitor.2.plus","spiro_"+0x1d, 1), 
	SPIRO_MONITOR_2_PLUS2("spiro.monitor.2.plus.2","spiro_"+0x1d, 1), 
	SPIRO_MONITOR_3_AIR("spiro.monitor.3.air", "spiro_"+0x48, 1);
	;
	
	private String key;
	private String computerIdentifier;
	private int parserType;

	private DiveComputerType(String key, String computerIdentifier, int parserType) {
		this.key = key;
		this.computerIdentifier = computerIdentifier;
		this.parserType = parserType;
	}

	public int getParserType() {
		return parserType;
	}

	public static DiveComputerType getDiveComputerType(String key) {
		for (DiveComputerType type : values()) {
			if (type.key.equals(key)){
				return type;
			}
		}
		return null;
	}

	public String getKey() {
		return key;
	}
	
	public String getComputerIdentifier() {
		return computerIdentifier;
	}
}
