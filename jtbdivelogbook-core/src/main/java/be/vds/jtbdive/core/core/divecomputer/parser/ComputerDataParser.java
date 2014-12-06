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
package be.vds.jtbdive.core.core.divecomputer.parser;

public enum ComputerDataParser {

	UWATEC_PARSER("UWATEC_ALADIN", UwatecDiveComputerDataParser.class);

	private Class<?> parserClass;
	private String parserName;

	private ComputerDataParser(String parserName, Class<?> parserClass) {
		this.parserName = parserName;
		this.parserClass = parserClass;
	}

	public Class<?> getParserClass() {
		return parserClass;
	}

	public String getParserName() {
		return parserName;
	}

	public static ComputerDataParser getParser(String parserName) {
		for (ComputerDataParser computerParser : values()) {
			if (computerParser.getParserName().equals(parserName)) {
				return computerParser;
			}
		}
		return null;
	}

	public static ComputerDataParser getParser(Class<?> parserClass) {
		for (ComputerDataParser computerParser : values()) {
			if (computerParser.getParserClass().equals(parserClass)) {
				return computerParser;
			}
		}
		return null;
	}
}
