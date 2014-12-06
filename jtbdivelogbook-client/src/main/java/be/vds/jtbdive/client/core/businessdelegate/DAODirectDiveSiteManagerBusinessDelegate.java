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

import java.util.List;

import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.DiveLocationUsedException;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.DiveSiteBusinessDelegate;
import be.vds.jtbdive.core.utils.ObjectSerializer;
import be.vds.jtbdive.persistence.core.dao.interfaces.DaoFactory;

public class DAODirectDiveSiteManagerBusinessDelegate implements
		DiveSiteBusinessDelegate {

	public DAODirectDiveSiteManagerBusinessDelegate() {
	}
	

	@Override
	public List<DiveSite> findAllDiveSites() throws DataStoreException {
		return DaoFactory.getFactory().createDiveSiteDAO().findAllDiveSites();
	}

	@Override
	public List<DiveSite> findDiveSitesByName(String name)
			throws DataStoreException {
		return DaoFactory.getFactory().createDiveSiteDAO()
				.findDiveSitesByName(name);
	}

	@Override
	public boolean deleteDiveLocation(DiveSite diveLocation)
			throws DataStoreException, DiveLocationUsedException {
		return DaoFactory.getFactory().createDiveSiteDAO()
				.deleteDiveLocation(diveLocation);
	}

	@Override
	public DiveSite saveDiveLocation(DiveSite diveLocation)
			throws DataStoreException {
		DiveSite d = (DiveSite) ObjectSerializer.cloneObject(diveLocation);
		return DaoFactory.getFactory().createDiveSiteDAO().saveDiveSite(d);
	}

	@Override
	public DiveSite updateDiveSite(DiveSite diveLocation)
			throws DataStoreException {
		DiveSite d = (DiveSite) ObjectSerializer.cloneObject(diveLocation);
		return DaoFactory.getFactory().createDiveSiteDAO().updateDiveSite(d);
	}

	@Override
	public void initialize() {
		// DaoFactory.getFactory().createDiveLocationDAO().initialize();
	}

	@Override
	public boolean mergeDiveSites(DiveSite diveLocationToKeep,
			DiveSite diveLocationToDelete) throws DataStoreException {
		return DaoFactory.getFactory().createDiveSiteDAO()
				.mergeDiveSites(diveLocationToKeep, diveLocationToDelete);
	}

	@Override
	public DiveSite findDiveSiteById(long id, boolean includeDocumentsContent)
			throws DataStoreException {
		return DaoFactory.getFactory().createDiveSiteDAO()
				.findDiveSiteById(id, includeDocumentsContent);
	}

	@Override
	public byte[] loadDocumentContent(long documentId, DocumentFormat format)
			throws DataStoreException {
		return DaoFactory.getFactory().createDiveSiteDAO()
				.loadDocumentContent(documentId, format);
	}
}
