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
package be.vds.jtbdive.persistence.core.dao.interfaces;

import java.util.List;

import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.DiveLocationUsedException;

public interface DiveSiteDAO {

	public List<DiveSite> findAllDiveSites() throws DataStoreException;

	public List<DiveSite> findDiveSitesByName(String name)
			throws DataStoreException;

	public boolean deleteDiveLocation(DiveSite diveSite)
			throws DataStoreException, DiveLocationUsedException;

	public DiveSite saveDiveSite(DiveSite diveSite)
			throws DataStoreException;

	public DiveSite updateDiveSite(DiveSite diveSite)
			throws DataStoreException;

	public boolean mergeDiveSites(DiveSite diveSiteToKeep,
			DiveSite diveSiteToDelete) throws DataStoreException;

	public DiveSite findDiveSiteById(long id, boolean includeDocumentsContent)
			throws DataStoreException;
	
	public DiveSite findDiveSiteById(long id)
			throws DataStoreException;

	public byte[] loadDocumentContent(long documentId, DocumentFormat format)throws DataStoreException;

}
