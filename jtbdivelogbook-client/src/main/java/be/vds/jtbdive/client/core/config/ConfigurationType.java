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
package be.vds.jtbdive.client.core.config;

import be.vds.jtbdive.core.core.catalogs.KeyedCatalog;

public enum ConfigurationType implements KeyedCatalog {
	XML_CONF( "xml"), //
//	OTHER_CONF("other", "Other configuration")//
	;

	private String key;

	
	private ConfigurationType(String key) {
		this.key = key;
	}

	/**
	 * Gets the name of the ConfigurationType. This is a short name that is enough
	 * explicit for understanding which kind for persistence layer the
	 * configuration uses.
	 * 
	 * @return a String that is the name of the configuration
	 */
	public String getKey() {
		return key;
	}

	public static ConfigurationType getConfigurationTypeForKey(
			String key) {
		for (ConfigurationType ct : values()) {
			if (ct.getKey().equals(key))
				return ct;
		}
		return null;
	}


}
