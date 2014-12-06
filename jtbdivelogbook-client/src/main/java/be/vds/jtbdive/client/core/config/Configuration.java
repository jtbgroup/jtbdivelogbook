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

import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.VersionException;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.DiveSiteBusinessDelegate;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.DiverBusinessDelegate;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.GlossaryBusinessDelegate;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.LogBookBusinessDelegate;

/**
 * A Configuration is responsible for determining the link between the model and
 * the persistence layer. The persistence layer is accessed by business
 * delegates that are configured here, depending on the variable it contains.
 * <p>
 * A concrete implementation of this interface MUST always have a constructor
 * with no parameter as it is instanciated by introspection.
 * 
 * @author Gautier Vanderslyen
 * 
 */

public interface Configuration {

	/**
	 * Gets the unique identifier for this specific configuration
	 */
	ConfigurationType getConfigurationType();


	/**
	 * Initializes the configuration with the provided variables.
	 * 
	 * @throws DataStoreException
	 *             thrown when an error occurs during the initialization.
	 */
	void initialize() throws DataStoreException, VersionException;

	/**
	 * Gets the business delegate for the dive locations created in the method
	 * initialize.
	 * 
	 * @return the dive location business delegate
	 */
	DiveSiteBusinessDelegate getDiveLocationBusinessDelegate();

	/**
	 * Gets the business delegate for the divers created in the method
	 * initialize.
	 * 
	 * @return the diver business delegate
	 */
	DiverBusinessDelegate getDiverBusinessDelegate();

	/**
	 * Gets the business delegate for the logbooks created in the method
	 * initialize.
	 * 
	 * @return the logbook business delegate
	 */
	LogBookBusinessDelegate getLogBookBusinessDelegate();


	GlossaryBusinessDelegate getGlossaryBusinessDelegate();


	boolean upgradePersistenceVersion() throws DataStoreException;

}
