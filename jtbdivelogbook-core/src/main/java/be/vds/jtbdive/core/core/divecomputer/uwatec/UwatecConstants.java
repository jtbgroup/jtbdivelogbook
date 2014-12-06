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
package be.vds.jtbdive.core.core.divecomputer.uwatec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class contains all the constants for the application
 * 
 * @author Gautier Vanderslyen
 * 
 */
public final class UwatecConstants {

	private UwatecConstants() {
	}

	/**
	 * variables utilises temporairement pour copier le logbook localement et
	 * travailler dessus
	 */

	/**
	 * Different Stat modules
	 */
	public static final int STAT_NONE = 0;

	public static final int STAT_DIVER = 1;

	public static final int STAT_DIVELOCATION = 2;

	public static final int STAT_DIVE = 3;

	public static final int STAT_DIVE_TYPE = 4;

	/*
	 * Computer Types
	 */
	public static final int MARES_GENIUS = 0x40;
	public static final int ALADIN_AIR_Z = 0x34;
	public static final int ALADIN_AIR_Z2 = 0x44;
	public static final int ALADIN_AIR_Z_O2 = 0xa4;
	public static final int ALADIN_AIR_Z_NITROX = 0xf4;
	public static final int SPIRO_MONITOR_3_AIR = 0x48;
	public static final int ALADIN_AIR_TWIN = 0x1c;
	public static final int SPIRO_MONITOR_2_PLUS = 0x1d;
	public static final int SPIRO_MONITOR_2_PLUS2 = 0x3d;
	public static final int ALADIN_SPORT_PLUS = 0x1e;
	public static final int ALADIN_SPORT_PLUS2 = 0x3e;
	public static final int ALADIN_PRO = 0x1f;
	public static final int ALADIN_PRO2 = 0x3f;
	public static final int ALADIN_PRO_ULTRA = 0xff;
	public static final int AIRE = 0x1b;

	/*
	 * Warnings
	 */
	public static final Integer WARN_DECO_STOP = 1;

	public static final Integer WARN_REMAINING_BOTTOM_TIME_TOO_SHORT = 2;

	public static final Integer WARN_ASCENT_TOO_FAST = 4;

	public static final Integer WARN_CEILING_VIOLATION = 8;

	public static final Integer WARN_WORK_TOO_HARD = 16;

	public static final Integer WARN_TRANSMIT_ERROR = 32;

	private static final Map<Integer, String> WARNINGS;
	static {
		WARNINGS = new HashMap<Integer, String>();
		WARNINGS.put(WARN_DECO_STOP, "deco stop");
		WARNINGS.put(WARN_REMAINING_BOTTOM_TIME_TOO_SHORT,
				"remaining bottom time too short; 5 min to reserved bar");
		WARNINGS.put(WARN_ASCENT_TOO_FAST, "ascent too fast");
		WARNINGS.put(WARN_CEILING_VIOLATION, "ceiling violation of deco stop");
		WARNINGS.put(WARN_WORK_TOO_HARD, "work too hard");
		WARNINGS.put(WARN_TRANSMIT_ERROR, "transmit error of air pressure");
	}

	public static String getWarning(Integer key) {
		return WARNINGS.get(key);
	}

	/*
	 * Alarms
	 */
	public static final Integer ASCENT_TOO_LONG = 0;

	public static final Integer REPEATED_DIVE = 1;

	public static final Integer HUNDREDS_OF_BOTTOM_TIME = 2;

	public static final Integer DECO_VIOLATION = 3;

	public static final Integer WORK_TOO_HARD = 4;

	public static final Integer SOS_MODE = 5;

	private static final Map<Integer, String> ALARMS;
	static {
		ALARMS = new HashMap<Integer, String>();
		ALARMS.put(ASCENT_TOO_LONG, "ascent warning too long");
		ALARMS.put(REPEATED_DIVE, "repeated diving");
		ALARMS.put(HUNDREDS_OF_BOTTOM_TIME, "figure of hundreds of bottom time");
		ALARMS.put(DECO_VIOLATION, "decompression violation");
		ALARMS.put(WORK_TOO_HARD, "work too hard");
		ALARMS.put(SOS_MODE, "SOS mode");
	}

	public static String getAlarms(Integer key) {
		return ALARMS.get(key);
	}

	/*
	 * Altitude
	 */
	public static final Integer ALTITUDE_UNKNOWN = 0;
	public static final Integer ALTITUDE_0_900 = 1;
	public static final Integer ALTITUDE_900_1750 = 2;
	public static final Integer ALTITUDE_1750_2700 = 3;
	public static final Integer ALTITUDE_2700_4000 = 4;
	public static final Map<Integer, String> ALTITUDES;
	static {
		ALTITUDES = new HashMap<Integer, String>();
		ALTITUDES.put(ALTITUDE_UNKNOWN, "unknown altitude");
		ALTITUDES.put(ALTITUDE_0_900, "0m - 900m");
		ALTITUDES.put(ALTITUDE_900_1750, "900m - 1750m");
		ALTITUDES.put(ALTITUDE_1750_2700, "1750m - 2700m");
		ALTITUDES.put(ALTITUDE_2700_4000, "2700m - 4000m");
	}
	public static final Integer ALTITUDE_REAL_0 = 0;
	public static final Integer ALTITUDE_REAL_1 = 1;
	public static final Integer ALTITUDE_REAL_2 = 2;
	public static final Integer ALTITUDE_REAL_3 = 3;
	public static final Integer ALTITUDE_REAL_4 = 4;
	public static final Map<Integer, Integer> ALTITUDES_REAL;
	static {
		ALTITUDES_REAL = new HashMap<Integer, Integer>();
		ALTITUDES_REAL.put(ALTITUDE_REAL_0, 0);
		ALTITUDES_REAL.put(ALTITUDE_REAL_1, 0);
		ALTITUDES_REAL.put(ALTITUDE_REAL_2, 900);
		ALTITUDES_REAL.put(ALTITUDE_REAL_3, 1750);
		ALTITUDES_REAL.put(ALTITUDE_REAL_4, 2700);
	}

	public static String getAltitude(Integer key) {
		return ALTITUDES.get(key);
	}

	public static List<Integer> getAltitudes() {
		List<Integer> alt = new ArrayList<Integer>();
		for (int i = 0; i < ALTITUDES.size(); i++) {
			alt.add(i);
		}
		Collections.sort(alt);
		return alt;
	}

	// Tissues
	public static final Integer TISSUES_KIDNEY = 0;

	public static final Integer TISSUES_STOMACH = 1;

	public static final Integer TISSUES_CNS = 2;

	public static final Integer TISSUES_SKIN = 3;

	public static final Integer TISSUES_HEART = 4;

	public static final Integer TISSUES_MUSCLES = 5;

	public static final Integer TISSUES_JOINTS = 6;

	public static final Integer TISSUES_FAT = 7;

	public static final Map<Integer, String> TISSUES;
	static {
		TISSUES = new HashMap<Integer, String>();
		TISSUES.put(TISSUES_KIDNEY, "kidney");
		TISSUES.put(TISSUES_STOMACH,
				"stomach, bowels, liver, central nervous system");
		TISSUES.put(TISSUES_CNS,
				"central nervous system, liver, stomach, bowels");
		TISSUES.put(TISSUES_SKIN, "skin");
		TISSUES.put(TISSUES_HEART, "skin, muscles, heart");
		TISSUES.put(TISSUES_MUSCLES, "muscles");
		TISSUES.put(TISSUES_JOINTS, "muscles, joints, bones, fat");
		TISSUES.put(TISSUES_FAT, "fat, joints, bones, rest");
	}

	public static String getTissues(Integer key) {
		return TISSUES.get(key);
	}

	// Tissues
	public static final Integer ARTERIAL_MICROBUBBLE_LEVEL_UNKNOWN = 0;

	public static final Integer ARTERIAL_MICROBUBBLE_LEVEL0 = 1;

	public static final Integer ARTERIAL_MICROBUBBLE_LEVEL1 = 2;

	public static final Integer ARTERIAL_MICROBUBBLE_LEVEL2 = 3;

	public static final Integer ARTERIAL_MICROBUBBLE_LEVEL3 = 4;

	public static final Integer ARTERIAL_MICROBUBBLE_LEVEL4 = 5;

	public static final Integer ARTERIAL_MICROBUBBLE_LEVEL5 = 6;

	public static final Integer ARTERIAL_MICROBUBBLE_LEVEL6 = 7;

	public static final Integer ARTERIAL_MICROBUBBLE_LEVEL7 = 8;

	public static final Map<Integer, String> ARTERIAL_MICROBUBBLE_LEVEL;
	static {
		ARTERIAL_MICROBUBBLE_LEVEL = new HashMap<Integer, String>();
		ARTERIAL_MICROBUBBLE_LEVEL.put(ARTERIAL_MICROBUBBLE_LEVEL0, "level 0");
		ARTERIAL_MICROBUBBLE_LEVEL.put(ARTERIAL_MICROBUBBLE_LEVEL1, "level 1");
		ARTERIAL_MICROBUBBLE_LEVEL.put(ARTERIAL_MICROBUBBLE_LEVEL2, "level 2");
		ARTERIAL_MICROBUBBLE_LEVEL.put(ARTERIAL_MICROBUBBLE_LEVEL3, "level 3");
		ARTERIAL_MICROBUBBLE_LEVEL.put(ARTERIAL_MICROBUBBLE_LEVEL4, "level 4");
		ARTERIAL_MICROBUBBLE_LEVEL.put(ARTERIAL_MICROBUBBLE_LEVEL5, "level 5");
		ARTERIAL_MICROBUBBLE_LEVEL.put(ARTERIAL_MICROBUBBLE_LEVEL6, "level 6");
		ARTERIAL_MICROBUBBLE_LEVEL.put(ARTERIAL_MICROBUBBLE_LEVEL7, "level 7");
		ARTERIAL_MICROBUBBLE_LEVEL.put(ARTERIAL_MICROBUBBLE_LEVEL_UNKNOWN,
				"undefined level");
	}

	public static String getArterialMicroBubbleLevel(Integer key) {
		return ARTERIAL_MICROBUBBLE_LEVEL.get(key);
	}

	public static final Integer SKIN_COOL_LEVEL_UNKNOWN = 0;

	public static final Integer SKIN_COOL_LEVEL0 = 1;

	public static final Integer SKIN_COOL_LEVEL1 = 2;

	public static final Integer SKIN_COOL_LEVEL2 = 3;

	public static final Integer SKIN_COOL_LEVEL3 = 4;

	public static final Integer SKIN_COOL_LEVEL4 = 5;

	public static final Integer SKIN_COOL_LEVEL5 = 6;

	public static final Integer SKIN_COOL_LEVEL6 = 7;

	public static final Integer SKIN_COOL_LEVEL7 = 8;

	public static final Map<Integer, String> SKIN_COOL_LEVEL;
	static {
		SKIN_COOL_LEVEL = new HashMap<Integer, String>();
		SKIN_COOL_LEVEL.put(SKIN_COOL_LEVEL0, "level 0");
		SKIN_COOL_LEVEL.put(SKIN_COOL_LEVEL1, "level 1");
		SKIN_COOL_LEVEL.put(SKIN_COOL_LEVEL2, "level 2");
		SKIN_COOL_LEVEL.put(SKIN_COOL_LEVEL3, "level 3");
		SKIN_COOL_LEVEL.put(SKIN_COOL_LEVEL4, "level 4");
		SKIN_COOL_LEVEL.put(SKIN_COOL_LEVEL5, "level 5");
		SKIN_COOL_LEVEL.put(SKIN_COOL_LEVEL6, "level 6");
		SKIN_COOL_LEVEL.put(SKIN_COOL_LEVEL7, "level 7");
		SKIN_COOL_LEVEL.put(SKIN_COOL_LEVEL_UNKNOWN, "undefined level");
	}

	public static String getSkinCoolLevel(Integer key) {
		return SKIN_COOL_LEVEL.get(key);
	}

	public static final Integer NITROX_MIX_21 = 0;
	public static final Integer NITROX_MIX_22 = 1;
	public static final Integer NITROX_MIX_24 = 2;
	public static final Integer NITROX_MIX_26 = 3;
	public static final Integer NITROX_MIX_28 = 4;
	public static final Integer NITROX_MIX_30 = 5;
	public static final Integer NITROX_MIX_32 = 6;
	public static final Integer NITROX_MIX_34 = 7;
	public static final Integer NITROX_MIX_36 = 8;
	public static final Integer NITROX_MIX_38 = 9;
	public static final Integer NITROX_MIX_40 = 10;
	public static final Integer NITROX_MIX_42 = 11;
	public static final Integer NITROX_MIX_44 = 12;
	public static final Integer NITROX_MIX_46 = 13;
	public static final Integer NITROX_MIX_48 = 14;
	public static final Integer NITROX_MIX_50 = 15;

	public static final Map<Integer, String> NITROX_MIX;
	static {
		NITROX_MIX = new HashMap<Integer, String>();
		NITROX_MIX.put(NITROX_MIX_21, "21");
		NITROX_MIX.put(NITROX_MIX_22, "22");
		NITROX_MIX.put(NITROX_MIX_24, "24");
		NITROX_MIX.put(NITROX_MIX_26, "26");
		NITROX_MIX.put(NITROX_MIX_28, "28");
		NITROX_MIX.put(NITROX_MIX_30, "30");
		NITROX_MIX.put(NITROX_MIX_32, "32");
		NITROX_MIX.put(NITROX_MIX_34, "34");
		NITROX_MIX.put(NITROX_MIX_36, "36");
		NITROX_MIX.put(NITROX_MIX_38, "38");
		NITROX_MIX.put(NITROX_MIX_40, "40");
		NITROX_MIX.put(NITROX_MIX_42, "42");
		NITROX_MIX.put(NITROX_MIX_44, "44");
		NITROX_MIX.put(NITROX_MIX_46, "46");
		NITROX_MIX.put(NITROX_MIX_48, "48");
		NITROX_MIX.put(NITROX_MIX_50, "50");
	}

	public static String getNitroxMix(Integer key) {
		return NITROX_MIX.get(key);
	}

	public static Set<Integer> getNitroxMixes() {
		return NITROX_MIX.keySet();
	}

	public static final Integer PPO2_UNDEFINED = 16;

	public static final Integer PPO2_1_20 = 0;

	public static final Integer PPO2_1_25 = 1;

	public static final Integer PPO2_1_30 = 2;

	public static final Integer PPO2_1_35 = 3;

	public static final Integer PPO2_1_40 = 4;

	public static final Integer PPO2_1_45 = 5;

	public static final Integer PPO2_1_50 = 6;

	public static final Integer PPO2_1_55 = 7;

	public static final Integer PPO2_1_60 = 8;

	public static final Integer PPO2_1_65 = 9;

	public static final Integer PPO2_1_70 = 10;

	public static final Integer PPO2_1_75 = 11;

	public static final Integer PPO2_1_80 = 12;

	public static final Integer PPO2_1_85 = 13;

	public static final Integer PPO2_1_90 = 14;

	public static final Integer PPO2_1_95 = 15;

	public static final Map<Integer, String> PPO2;
	static {
		PPO2 = new HashMap<Integer, String>();
		PPO2.put(PPO2_1_20, "1,20");
		PPO2.put(PPO2_1_25, "1,25");
		PPO2.put(PPO2_1_30, "1,30");
		PPO2.put(PPO2_1_35, "1,35");
		PPO2.put(PPO2_1_40, "1,40");
		PPO2.put(PPO2_1_45, "1,45");
		PPO2.put(PPO2_1_50, "1,50");
		PPO2.put(PPO2_1_55, "1,55");
		PPO2.put(PPO2_1_60, "1,60");
		PPO2.put(PPO2_1_65, "1,65");
		PPO2.put(PPO2_1_70, "1,70");
		PPO2.put(PPO2_1_75, "1,75");
		PPO2.put(PPO2_1_80, "1,80");
		PPO2.put(PPO2_1_85, "1,85");
		PPO2.put(PPO2_1_90, "1,90");
		PPO2.put(PPO2_1_95, "1,95");
		PPO2.put(PPO2_UNDEFINED, "unknown PPO2");
	}

	public static String getPPO2(Integer key) {
		return PPO2.get(key);
	}


	public static final int LOGBOOK_NEW = 0;

	public static final int LOGBOOK_EDIT_NAME = 1;

	private static final Map<String, String> COUNTRIES;

	public static final String COUNTRY_BELGIUM = "BE";

	public static final String COUNTRY_FRANCE = "FR";

	public static final String COUNTRY_US = "US";

	public static final String COUNTRY_ENGLAND = "UK";

	public static final String COUNTRY_NETHERLANDS = "NL";

	static {
		COUNTRIES = new HashMap<String, String>();
		COUNTRIES.put(COUNTRY_BELGIUM, "Belgium");
		COUNTRIES.put(COUNTRY_FRANCE, "France");
		COUNTRIES.put(COUNTRY_US, "United States");
		COUNTRIES.put(COUNTRY_ENGLAND, "England");
		COUNTRIES.put(COUNTRY_NETHERLANDS, "Nederland");
	}

	public static String getCountry(String countryCode) {
		return COUNTRIES.get(countryCode);
	}

	public static Map<String, String> getCountries() {
		return COUNTRIES;
	}

	private static final Map<Integer, String> PORT_TYPE;

	public static final int PORT_SERIAL = 1;

	public static final int PORT_USB = 2;

	static {
		PORT_TYPE = new HashMap<Integer, String>();
		PORT_TYPE.put(PORT_SERIAL, "portserial");
		PORT_TYPE.put(PORT_USB, "portusb");
	}

	public static Map<Integer, String> getPortTypes() {
		return PORT_TYPE;
	}

	public static String getPortType(Integer portType) {
		return PORT_TYPE.get(portType);
	}


	public static final List<String> DATE_LIST;
	static {
		DATE_LIST = new ArrayList<String>();
		DATE_LIST.add("yyyy-MM-dd");
		DATE_LIST.add("yyyy-MM-dd HH:mm");
		DATE_LIST.add("dd-MM-yyyy");
		DATE_LIST.add("dd-MM-yyyy (HH:mm)");
	}

	public static final String UNIT_TYPE_METRIC = "1";

	public static final String UNIT_TYPE_ENGLISH = "2";

	private static final Map<String, String> UNITS_TYPE;
	static {
		UNITS_TYPE = new HashMap<String, String>();
		UNITS_TYPE.put(UNIT_TYPE_METRIC, "metric");
		UNITS_TYPE.put(UNIT_TYPE_ENGLISH, "english");
	}

	/*
	 * Units
	 */
	public static final int UNIT_METER = 1;

	public static final int UNIT_CENTIMETER = 2;

	public static final int UNIT_FEET = 3;

	public static final int UNIT_CELSIUS = 4;

	public static final int UNIT_KELVIN = 5;

	public static final int UNIT_FARENHEIT = 6;

	private static final Map<Integer, String> UNITS;
	static {
		UNITS = new HashMap<Integer, String>();
		UNITS.put(UNIT_METER, "meter");
		UNITS.put(UNIT_CENTIMETER, "centimeter");
		UNITS.put(UNIT_FEET, "feet");
		UNITS.put(UNIT_CELSIUS, "celsius");
		UNITS.put(UNIT_KELVIN, "kelvin");
		UNITS.put(UNIT_FARENHEIT, "farenheit");
	}

	public static String getUnit(int key) {
		return UNITS.get(key);
	}

	public static final Map<Integer, String> UNITS_SYMBOL;
	static {
		UNITS_SYMBOL = new HashMap<Integer, String>();
		UNITS_SYMBOL.put(UNIT_METER, "m");
		UNITS_SYMBOL.put(UNIT_CENTIMETER, "cm");
		UNITS_SYMBOL.put(UNIT_FEET, "ft");
		UNITS_SYMBOL.put(UNIT_CELSIUS, "C");
		UNITS_SYMBOL.put(UNIT_KELVIN, "K");
		UNITS_SYMBOL.put(UNIT_FARENHEIT, "F");
	}

	public static String getUnitSymbol(int key) {
		return UNITS_SYMBOL.get(key);
	}

	public static final Set<Integer> UNITS_LENGTH;
	static {
		UNITS_LENGTH = new HashSet<Integer>();
		UNITS_LENGTH.add(UNIT_METER);
		UNITS_LENGTH.add(UNIT_CENTIMETER);
		UNITS_LENGTH.add(UNIT_FEET);
	}

	public static final Set<Integer> UNITS_TEMPERATURES;
	static {
		UNITS_TEMPERATURES = new HashSet<Integer>();
		UNITS_TEMPERATURES.add(UNIT_CELSIUS);
		UNITS_TEMPERATURES.add(UNIT_KELVIN);
		UNITS_TEMPERATURES.add(UNIT_FARENHEIT);
	}

	public static final int DIVE_TYPE_UNKNOWN = 0;

	public static final int DIVE_TYPE_SEA_BOARD = 1;

	public static final int DIVE_TYPE_SEA_BOAT = 2;

	public static final int DIVE_TYPE_CAVE = 3;

	public static final int DIVE_TYPE_CARRER = 4;

	public static final int DIVE_TYPE_LAKE = 5;

	public static final Map<Integer, String> DIVE_TYPES;
	static {
		DIVE_TYPES = new HashMap<Integer, String>();
		DIVE_TYPES.put(DIVE_TYPE_UNKNOWN, "dive.type.unknown");
		DIVE_TYPES.put(DIVE_TYPE_SEA_BOARD, "dive.type.sea.board");
		DIVE_TYPES.put(DIVE_TYPE_SEA_BOAT, "dive.type.sea.boat");
		DIVE_TYPES.put(DIVE_TYPE_CAVE, "dive.type.cave");
		DIVE_TYPES.put(DIVE_TYPE_CARRER, "dive.type.carrer");
		DIVE_TYPES.put(DIVE_TYPE_LAKE, "dive.type.lake");
	}

	public static Map<Integer, String> getDiveTypes() {
		return DIVE_TYPES;
	}

	public static String getDiveType(int key) {
		return DIVE_TYPES.get(key);
	}

}
