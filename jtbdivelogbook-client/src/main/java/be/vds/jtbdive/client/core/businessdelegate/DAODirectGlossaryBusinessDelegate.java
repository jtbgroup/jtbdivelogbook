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
package be.vds.jtbdive.client.core.businessdelegate;

import java.util.Map;

import be.vds.jtbdive.core.core.Country;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.GlossaryBusinessDelegate;
import be.vds.jtbdive.persistence.core.dao.interfaces.DaoFactory;

/**
 * This business delegate is used by the model to interact with persistence
 * layer. The business delegate cares for any connection to an application
 * server, a database, a file system, ...
 * 
 * @author gautier
 * 
 */
public class DAODirectGlossaryBusinessDelegate extends GlossaryBusinessDelegate {

	public DAODirectGlossaryBusinessDelegate() {
	}

	@Override
	public Map<String , Country> getCountriesMap() throws DataStoreException {
		return DaoFactory.getFactory().createGlossaryDAO().getCountriesMap();
	}

}
