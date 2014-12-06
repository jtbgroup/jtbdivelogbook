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

import java.io.File;

import be.vds.jtb.jtbdivelogbook.persistence.xml.dao.XMLDaoFactory;
import be.vds.jtbdive.client.core.businessdelegate.DAODirectConfiguratorBusinessDelegate;
import be.vds.jtbdive.client.core.businessdelegate.DAODirectDiveSiteManagerBusinessDelegate;
import be.vds.jtbdive.client.core.businessdelegate.DAODirectDiverBusinessDelegate;
import be.vds.jtbdive.client.core.businessdelegate.DAODirectGlossaryBusinessDelegate;
import be.vds.jtbdive.client.core.businessdelegate.DAODirectLogBookBusinessDelegate;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.VersionException;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.ConfiguratorBusinessDelegate;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.DiveSiteBusinessDelegate;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.DiverBusinessDelegate;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.GlossaryBusinessDelegate;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.LogBookBusinessDelegate;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.persistence.core.dao.interfaces.DaoFactory;

public class XMLConfiguration implements Configuration {
	private static final Syslog LOGGER = Syslog
			.getLogger(XMLConfiguration.class);
	private String basePath;
	private DiverBusinessDelegate diverBusinessDelegate;
	private DiveSiteBusinessDelegate diveLocationBusinessDelegate;
	private LogBookBusinessDelegate logbookBusinessDelegate;
	private GlossaryBusinessDelegate glossaryBusinessDelegate;
	private ConfiguratorBusinessDelegate configBusinessDelegate;
	private ConfigurationType configurationType;

	public XMLConfiguration() {
		configurationType = ConfigurationType.XML_CONF;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
		if (this.basePath != null) {
			if (!this.basePath.endsWith(String.valueOf(File.separatorChar))) {
				this.basePath += File.separatorChar;
			}
		}
	}

	@Override
	public DiveSiteBusinessDelegate getDiveLocationBusinessDelegate() {
		return diveLocationBusinessDelegate;
	}

	@Override
	public DiverBusinessDelegate getDiverBusinessDelegate() {
		return diverBusinessDelegate;
	}

	@Override
	public LogBookBusinessDelegate getLogBookBusinessDelegate() {
		return logbookBusinessDelegate;
	}

	@Override
	public void initialize() throws DataStoreException, VersionException {
		configBusinessDelegate = new DAODirectConfiguratorBusinessDelegate();
		diverBusinessDelegate = new DAODirectDiverBusinessDelegate();
		diveLocationBusinessDelegate = new DAODirectDiveSiteManagerBusinessDelegate();
		logbookBusinessDelegate = new DAODirectLogBookBusinessDelegate();
		glossaryBusinessDelegate = new DAODirectGlossaryBusinessDelegate();

		XMLDaoFactory xml = new XMLDaoFactory();
		DaoFactory.setFactory(xml);

		// Initialization must be done as last action because it can throw an
		// exception
		xml.initialize(basePath);

		LOGGER.info("Configuration initialized with base path " + basePath);
	}

	public String getBasePath() {
		return basePath;
	}

	@Override
	public ConfigurationType getConfigurationType() {
		return configurationType;
	}

	@Override
	public String toString() {
		return "XML configuration (" + basePath + ")";
	}

	@Override
	public GlossaryBusinessDelegate getGlossaryBusinessDelegate() {
		return glossaryBusinessDelegate;
	}

	@Override
	public boolean upgradePersistenceVersion() throws DataStoreException {
		return configBusinessDelegate.upgradePersistenceVersion();
	}

}
