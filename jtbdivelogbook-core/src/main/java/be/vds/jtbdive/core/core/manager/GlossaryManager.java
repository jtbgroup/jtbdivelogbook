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
package be.vds.jtbdive.core.core.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import be.vds.jtbdive.core.core.Country;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.GlossaryBusinessDelegate;
import be.vds.jtbdive.core.logging.Syslog;

/**
 * This class manages all the Glossaries, catalogs and other static listings
 * coming from the persistence layer. Caches can be implemented for performance
 * reasons.
 * 
 * @author gautier
 * 
 */
public class GlossaryManager {

	private static final Syslog LOGGER = Syslog
			.getLogger(GlossaryManager.class);
	private GlossaryBusinessDelegate glossaryBusinessDelegate;
	private Map<String, Country> countries;

	public GlossaryManager(GlossaryBusinessDelegate glossaryBusinessDelegate) {
		this.glossaryBusinessDelegate = glossaryBusinessDelegate;
	}

	public List<Country> getCountries() {
		if (countries == null) {
			getCountriesMap();
		}
		
		return new ArrayList<Country>(countries.values());
	}

	public Map<String, Country> getCountriesMap() {
		if (countries == null) {
			try {
				countries = glossaryBusinessDelegate.getCountriesMap();
			} catch (DataStoreException e) {
				LOGGER.error(e);
			}
		}
		return countries;
	}

}
