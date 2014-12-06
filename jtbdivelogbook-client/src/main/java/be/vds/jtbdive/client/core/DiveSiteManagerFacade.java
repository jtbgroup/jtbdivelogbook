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
package be.vds.jtbdive.client.core;

import java.util.List;
import java.util.Observable;

import be.vds.jtbdive.client.core.event.DiveSiteEvent;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.core.manager.DiveSiteManager;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.DiveLocationUsedException;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.DiveSiteBusinessDelegate;

public class DiveSiteManagerFacade extends Observable implements DocumentContentLoader{

	private DiveSiteManager diveSiteManager;

	public DiveSiteManagerFacade(DiveSiteManager diveSiteManager) {
		this.diveSiteManager = diveSiteManager;
	}

	public List<DiveSite> findDiveSitesByName(String name)
			throws DataStoreException {
		return diveSiteManager.findDiveLocationsByName(name);
	}

	public boolean deleteDiveSite(DiveSite diveSite)
			throws DiveLocationUsedException, DataStoreException {
		boolean b = diveSiteManager.deleteDiveLocation(diveSite);
		if (b) {
			setChanged();
			notifyObservers(new DiveSiteEvent(
					DiveSiteEvent.DELETE, diveSite, null));
		}
		return b;
	}

	public DiveSite saveDiveSite(DiveSite diveSite)
			throws DataStoreException {
		DiveSite newDiveSite = diveSiteManager.saveDiveSite(diveSite);
		if (null != newDiveSite) {
			setChanged();
			notifyObservers(new DiveSiteEvent(
					DiveSiteEvent.SAVE, null, newDiveSite));
		}
		return newDiveSite;
	}

	public DiveSite updateDiveSite(DiveSite diveSite)
			throws DataStoreException {
		DiveSite b = diveSiteManager.updateDiveSite(diveSite);
		if (null != b) {
			setChanged();
			notifyObservers(new DiveSiteEvent(
					DiveSiteEvent.UPDATE, diveSite, b));
		}
		return b;
	}

	public void mergeDiveSite(DiveSite diveSiteToKeep,
			DiveSite diveSiteToDelete) throws DataStoreException {
		boolean b = diveSiteManager.mergeDiveSite(diveSiteToKeep,
				diveSiteToDelete);
		if(b){
			setChanged();
			notifyObservers(new DiveSiteEvent(
					DiveSiteEvent.MERGE, diveSiteToDelete, diveSiteToKeep));
		}
	}

	public void setBusinessDelegate(
			DiveSiteBusinessDelegate diveLocationBusinessDelegate) {
		diveSiteManager.setBusinessDelegate(diveLocationBusinessDelegate);
	}

	public DiveSite findDiveSiteById(long id) throws DataStoreException {
		return findDiveSiteById(id, false);
	}
	
	public DiveSite findDiveSiteById(long id, boolean includeDocumentsContent) throws DataStoreException {
		return diveSiteManager.findDiveSiteById(id, includeDocumentsContent);
	}
	
	@Override
	public byte[] loadDocumentContent(long documentId, DocumentFormat format) throws DataStoreException {
		return diveSiteManager.loadDocumentContent(documentId, format);
	}
}
