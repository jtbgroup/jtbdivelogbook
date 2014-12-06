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

import java.util.List;
import java.util.Map;

import be.vds.jtbdive.core.core.Country;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.persistence.core.dao.interfaces.GlossaryDAO;

public class XMLGlossaryDAO implements GlossaryDAO {

    private static XMLGlossaryDAO instance;

    public static XMLGlossaryDAO getInstance() {
        if (instance == null) {
            instance = new XMLGlossaryDAO();
        }
        return instance;
    }

    public List<Country> getCountries() throws DataStoreException {
        return XMLCountryDAO.getInstance().getCountries();
    }

    public void initialize(String basePath) {
    }

    @Override
    public Map<String, Country> getCountriesMap() throws DataStoreException {
        return XMLCountryDAO.getInstance().getCountriesMap();
    }
}
