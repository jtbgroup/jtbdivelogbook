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
package be.vds.jtbdive.client.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
/**
 * Manages the user properties for the application
 * @author Gautier Vanderslyen
 *
 */
public class PropertiesManager {

	private Properties props;

	public static final File propertyPath = new File(System
			.getProperty("user.home")
			+ File.separator + "jtb_logbook" + File.separator);
	private static final File propertyFile = new File(propertyPath
			+ File.separator + "logbookapp.properties");

	private static File defaultPropertyFile;
	private static final Logger LOGGER = Logger
			.getLogger(PropertiesManager.class);

	public PropertiesManager() {
		props = new Properties();
		init();
	}

	private void init() {
		if (propertyFile.exists() && propertyFile.length() != 0) {
			loadProperties();
		} else {
			LOGGER.info("Properties file not found, creating one");
			createPropertiesFile();
		}
	}

	private void createPropertiesFile() {
		try {
			propertyPath.mkdir();
			propertyFile.createNewFile();
			Properties p = new Properties();
			FileInputStream fis = new FileInputStream(defaultPropertyFile);
			FileOutputStream fos = new FileOutputStream(propertyFile);
			p.load(fis);
			p.store(fos, "J t'B LogBook Properties");

			fis.close();
			fos.close();
			LOGGER.info("Properties file created in " + propertyFile);
			init();
		} catch (IOException e) {
			LOGGER.fatal("IO exception while creating the properties : "
					+ e.getMessage());
		}
	}

	public void saveProperties() {
		try {
			props.store(new FileOutputStream(propertyFile),
					"JtB LogBook Properties");
		} catch (FileNotFoundException e) {
			LOGGER
					.warn("Couldn't save the properties because file hasn't been found");
		} catch (IOException e) {
			LOGGER.warn("Couldn't save the properties because of IO Exception");
		}
	}

	public void loadProperties() {
		try {
			props.load(new FileInputStream(propertyFile));
		} catch (FileNotFoundException e) {
			LOGGER
					.warn("Couldn't load the properties because file hasn't been found");
		} catch (IOException e) {
			LOGGER.warn("Couldn't load the properties because of IO Exception");
		}
	}

	public String getProperty(String key) {
		return props.getProperty(key);
	}

	public void setProperty(String key, String value) {
		props.setProperty(key, value);
	}

	public void removeProperty(String key) {
		props.remove(key);
	}

	public Properties getProperties() {
		return props;
	}

	/**
	 * This refers to {@link SystemProperties}
	 * @return
	 */
	public static String getOSType() {
		String os = System.getProperty("os.name");
		if (SystemProperties.getWindowsOsNames().contains(os)) {
			return SystemProperties.WINDOWS_OS_TYPE;
		} else if (SystemProperties.getLinuxOsNames().contains(os)) {
			return SystemProperties.LINUX_OS_TYPE;
		}
		return SystemProperties.UNKNOWN_OS_TYPE;
	}

	public static String getJREUsed() {
		String s = System.getProperty("java.home");
		return s;
	}
}
