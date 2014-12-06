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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import be.vds.jtbdive.core.logging.Syslog;

public class ConfigurationHandler {
	private static final Syslog LOGGER = Syslog
			.getLogger(ConfigurationHandler.class);
	private List<Configuration> knownConfigs;
	private static ConfigurationHandler instance;

	public void setKnownConfigurations(List<Configuration> knownConfigs) {
		this.knownConfigs = knownConfigs;
		LOGGER.info("known configurations set (" + this.knownConfigs.toString()
				+ ")");
	}

	public void persistConfiguration(Configuration config, File file)
			throws IOException {
		ConfigurationParser parser = new ConfigurationParser();
		parser.write(config, new FileOutputStream(file));
	}

	public Configuration loadConfiguration(File file) throws IOException {
		Configuration c = null;
		ConfigurationParser p = new ConfigurationParser();
		c = p.read(new FileInputStream(file));
		LOGGER.debug("Configuration identifier : "
				+ c.getConfigurationType().getKey());

		return c;
	}

	private ConfigurationHandler() {
	}

	public static ConfigurationHandler getInstance() {
		if (null == instance)
			instance = new ConfigurationHandler();
		return instance;
	}

}
