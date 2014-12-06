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
package be.vds.jtbdive.core.integration.businessdelegate.interfaces;

import java.util.List;

import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.DiveLocationUsedException;

/**
 * This business delegate is used by the model to interact with persistence
 * layer. The business delegate cares for any connection to an application
 * server, a database, a file system, ...
 * 
 * @author gautier
 * 
 */

public interface DiveSiteBusinessDelegate {

	List<DiveSite> findAllDiveSites() throws DataStoreException;

	List<DiveSite> findDiveSitesByName(String name) throws DataStoreException;

	boolean deleteDiveLocation(DiveSite diveSite) throws DataStoreException,
			DiveLocationUsedException;

	DiveSite saveDiveLocation(DiveSite diveSite) throws DataStoreException;

	DiveSite updateDiveSite(DiveSite diveSite) throws DataStoreException;

	void initialize();

	boolean mergeDiveSites(DiveSite diveSiteToKeep, DiveSite diveSiteToDelete)
			throws DataStoreException;

	DiveSite findDiveSiteById(long id, boolean includeDocumentsContent)
			throws DataStoreException;

	byte[] loadDocumentContent(long documentId, DocumentFormat format)
			throws DataStoreException;
}
