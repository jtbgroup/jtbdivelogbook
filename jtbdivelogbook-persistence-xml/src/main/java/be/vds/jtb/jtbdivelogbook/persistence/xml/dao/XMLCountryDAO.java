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
package be.vds.jtb.jtbdivelogbook.persistence.xml.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import be.vds.jtbdive.core.core.Country;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.xml.parsers.CountriesParser;

public class XMLCountryDAO {
	private static final Syslog LOGGER = Syslog.getLogger(XMLCountryDAO.class);

	private static XMLCountryDAO instance;
	private List<Country> countries;
	private CountriesParser parser = new CountriesParser();

	private XMLCountryDAO() {
	
	}

	private void loadCountries() throws DataStoreException{
		try {
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(getClass().getClassLoader()
					.getResourceAsStream(
							"resources/misc/iso_3166-1_list_en.xml"));
			List<Country> newList = parser.readCountriesList(doc.getRootElement());
			countries = newList;
			LOGGER.debug("countries list loaded");
		} catch (JDOMException e) {
			throw new DataStoreException(e);
		} catch (IOException e) {
			throw new DataStoreException(e);
		}
	}

	public List<Country> getCountries() throws DataStoreException{
		if (null == countries) {
			loadCountries();
		}
		return countries;
	}


	protected static XMLCountryDAO getInstance() {
		if (instance == null) {
			instance = new XMLCountryDAO();
		}
		return instance;
	}

    public Map<String, Country> getCountriesMap() throws DataStoreException {
        List<Country> c = getCountries();
        Map<String, Country> m = new HashMap<String, Country>();
        for (Country country : c) {
            m.put(country.getCode(), country);
        }
        return m;
    }

}
