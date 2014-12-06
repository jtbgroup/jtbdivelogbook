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
package be.vds.jtbdive.core.core.manager;

import java.io.Serializable;
import java.util.List;

import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.DiveLocationUsedException;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.DiveSiteBusinessDelegate;
import be.vds.jtbdive.core.logging.Syslog;

public class DiveSiteManager implements Serializable {

	private static final long serialVersionUID = -2798933353089695559L;
	private static final Syslog LOGGER = Syslog
			.getLogger(DiveSiteManager.class);
	private DiveSiteBusinessDelegate diveSiteManagerBusinessDelegate;

	public DiveSiteManager(
			DiveSiteBusinessDelegate diveSiteManagerBusinessDelegate) {
		this.diveSiteManagerBusinessDelegate = diveSiteManagerBusinessDelegate;
	}

	public List<DiveSite> findAllDiveSites() throws DataStoreException {
		return diveSiteManagerBusinessDelegate.findAllDiveSites();
	}

	public List<DiveSite> findDiveLocationsByName(String name)
			throws DataStoreException {
		return diveSiteManagerBusinessDelegate.findDiveSitesByName(name);
	}

	public boolean deleteDiveLocation(DiveSite diveSite)
			throws DataStoreException, DiveLocationUsedException {
		return diveSiteManagerBusinessDelegate.deleteDiveLocation(diveSite);
	}

	public DiveSite saveDiveSite(DiveSite diveSite)
			throws DataStoreException {
		return diveSiteManagerBusinessDelegate.saveDiveLocation(diveSite);
	}

	public DiveSite updateDiveSite(DiveSite diveSite)
			throws DataStoreException {
		return diveSiteManagerBusinessDelegate.updateDiveSite(diveSite);
	}

	public void setBusinessDelegate(
			DiveSiteBusinessDelegate diveSiteManagerBusinessDelegate) {
		this.diveSiteManagerBusinessDelegate = diveSiteManagerBusinessDelegate;
		LOGGER.info("Business Delegate changed");
	}

	public boolean mergeDiveSite(DiveSite diveLocationToKeep,
			DiveSite diveLocationToDelete) throws DataStoreException {
		return diveSiteManagerBusinessDelegate.mergeDiveSites(
				diveLocationToKeep, diveLocationToDelete);
	}

	public DiveSite findDiveSiteById(long id) throws DataStoreException {
		return findDiveSiteById(id, false);
	}

	public DiveSite findDiveSiteById(long id, boolean includeDocumentsContent)
			throws DataStoreException {
		return diveSiteManagerBusinessDelegate.findDiveSiteById(id,
				includeDocumentsContent);
	}

	public byte[] loadDocumentContent(long documentId, DocumentFormat format)
			throws DataStoreException {
		return diveSiteManagerBusinessDelegate.loadDocumentContent(documentId,
				format);
	}

}
