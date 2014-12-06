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

/**
 * Represents a Gaz used in the mix in the tanks
 * 
 * @author Gautier Vanderslyen
 * 
 */
public enum Gaz implements KeyedCatalog {

	GAZ_OXYGEN(1, "O2", "oxygen"), 
	GAZ_NITROGEN(2, "N2", "nitrogen"), 
	GAZ_HELIUM(3, "He", "helium"), 
	GAZ_HYDROGEN(4, "H2", "hydrogen"), 
	GAZ_ARGON(5, "Ar", "argon"), 
	GAZ_NEON(6, "Ne", "neon"), 
	GAZ_OTHER(99, "Other", "other"), 
	;

	private String formule;
	private int gazType;
	private String key;

	private Gaz(int gazType, String formule, String key) {
		this.formule = formule;
		this.gazType = gazType;
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	public int getGazType() {
		return gazType;
	}

	/**
	 * gets the type of the Gaz
	 * 
	 * @return
	 */
	public String getFormule() {
		return formule;
	}

	/**
	 * Get a Gaz according to a type
	 * 
	 * @param gazType
	 *            the type of Gaz we want to retrieve.
	 * @return the Gaz according to the given type
	 */
	public static Gaz getGaz(int gazType) {
		for (Gaz gaz : Gaz.values()) {
			if (gaz.getGazType() == gazType){
				return gaz;
			}
		}
		return null;
	}

}
