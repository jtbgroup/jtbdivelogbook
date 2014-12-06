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

import be.vds.jtbdive.persistence.core.dao.interfaces.AddressDAO;
import be.vds.jtbdive.persistence.core.util.JdbcConnectionCreator;

public class XMLAddressDAO extends JdbcConnectionCreator implements
		AddressDAO {

	private static XMLAddressDAO instance;

	public static XMLAddressDAO getInstance() {
		if (null == instance) {
			instance = new XMLAddressDAO();
		}
		return instance;
	}

	
}
