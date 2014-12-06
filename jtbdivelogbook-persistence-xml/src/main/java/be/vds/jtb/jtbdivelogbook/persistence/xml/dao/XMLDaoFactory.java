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

import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.VersionException;
import be.vds.jtbdive.persistence.core.dao.interfaces.ConfigurationDAO;
import be.vds.jtbdive.persistence.core.dao.interfaces.DaoFactory;
import be.vds.jtbdive.persistence.core.dao.interfaces.DiveSiteDAO;
import be.vds.jtbdive.persistence.core.dao.interfaces.DiverDAO;
import be.vds.jtbdive.persistence.core.dao.interfaces.GlossaryDAO;
import be.vds.jtbdive.persistence.core.dao.interfaces.LogBookDAO;

public class XMLDaoFactory extends DaoFactory {

	private static XMLDaoFactory instance;

	public static XMLDaoFactory getInstance() {
		if (null == instance) {
			instance = new XMLDaoFactory();
		}
		return instance;
	}

	public void initialize(String basePath) throws DataStoreException, VersionException {
		XMLConfigurationDAO.getInstance().setBasePath(basePath);
		XMLLogBookDAO.getInstance().setBasePath(basePath);
		
		XMLConfigurationDAO.getInstance().initialize();
		
		XMLDiverDAO.getInstance().initialize(basePath);
		XMLDiveSiteDAO.getInstance().initialize(basePath);
		XMLGlossaryDAO.getInstance().initialize(basePath);
	}
	

	@Override
	public DiverDAO createDiverDAO() {
		return XMLDiverDAO.getInstance();
	}

	@Override
	public LogBookDAO createLogBookDAO() {
		return XMLLogBookDAO.getInstance();
	}

	@Override
	public DiveSiteDAO createDiveSiteDAO() {
		return XMLDiveSiteDAO.getInstance();
	}

	@Override
	public ConfigurationDAO createConfigurationDAO() {
		return XMLConfigurationDAO.getInstance();
	}

	@Override
	public GlossaryDAO createGlossaryDAO() {
		return XMLGlossaryDAO.getInstance();
	}

}
