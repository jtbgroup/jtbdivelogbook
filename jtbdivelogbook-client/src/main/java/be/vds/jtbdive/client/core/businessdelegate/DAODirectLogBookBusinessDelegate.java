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

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.LogBookMeta;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.LogBookBusinessDelegate;
import be.vds.jtbdive.core.utils.ObjectSerializer;
import be.vds.jtbdive.persistence.core.dao.interfaces.DaoFactory;

public class DAODirectLogBookBusinessDelegate extends LogBookBusinessDelegate {

	public DAODirectLogBookBusinessDelegate() {
	}

	@Override
	public LogBook findLogBook(long id) throws DataStoreException {
		return DaoFactory.getFactory().createLogBookDAO().findLogBook(id);
	}

	@Override
	public List<LogBook> findLogBookNames() throws DataStoreException {
		return DaoFactory.getFactory().createLogBookDAO().findLogBookNames();
	}

	@Override
	public void initialize() {
		// DaoFactory.getFactory().createLogBookDAO().initialize();
	}

	// @Override
	// public LogBook saveLogBook(LogBook logBook) throws DataStoreException {
	// LogBook lb = (LogBook) ObjectSerializer.cloneObject(logBook);
	// return DaoFactory.getFactory().createLogBookDAO().saveLogBook(lb);
	// }

	@Override
	public Dive saveDive(long logbookId, Dive dive) throws DataStoreException {
		Dive clone = (Dive) ObjectSerializer.cloneObject(dive);
		return DaoFactory.getFactory().createLogBookDAO()
				.saveDive(logbookId, clone);
	}

	@Override
	public Dive reloadDive(long logbookId, long diveId)
			throws DataStoreException {
		Dive lb = DaoFactory.getFactory().createLogBookDAO()
				.reloadDive(logbookId, diveId);
		return (Dive) ObjectSerializer.cloneObject(lb);
	}

	@Override
	public LogBookMeta saveLogBookMeta(LogBookMeta lb)
			throws DataStoreException {
		LogBookMeta m = (LogBookMeta) ObjectSerializer.cloneObject(lb);
		return DaoFactory.getFactory().createLogBookDAO().saveLogBookMeta(m);

	}

	@Override
	public Dive deleteDive(long logbookId, Dive dive) throws DataStoreException {
		Dive clone = (Dive) ObjectSerializer.cloneObject(dive);
		return DaoFactory.getFactory().createLogBookDAO()
				.deleteDive(logbookId, clone);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Dive> saveDives(List<Dive> dives, long logbookId)
			throws DataStoreException {
		List<Dive> lb = (List<Dive>) ObjectSerializer.cloneObject(dives);
		return DaoFactory.getFactory().createLogBookDAO()
				.saveDives(lb, logbookId);
	}

	public byte[] loadDocumentContent(long documentId, DocumentFormat format)
			throws DataStoreException {
		return DaoFactory.getFactory().createLogBookDAO()
				.loadDocumentContent(documentId, format);
	}

	@Override
	public Material saveMaterial(long logbookId, Material material)
			throws DataStoreException {
		return DaoFactory.getFactory().createLogBookDAO()
				.saveMaterial(logbookId, material);
	}

	@Override
	public boolean deleteMaterial(long logbookId, Material material)
			throws DataStoreException {
		return DaoFactory.getFactory().createLogBookDAO()
				.deleteMaterial(logbookId, material);
	}
	
	@Override
	public boolean deleteMaterials(long logbookId, List<Material> materials)
			throws DataStoreException {
		return DaoFactory.getFactory().createLogBookDAO()
				.deleteMaterials(logbookId, materials);
	}

	@Override
	public Material mergeMaterial(long logbookId, Material materialToKeep,
			Material materialToDelete) throws DataStoreException {
		return DaoFactory.getFactory().createLogBookDAO()
				.mergeMaterial(logbookId, materialToKeep, materialToDelete);
	}

	@Override
	public boolean deleteLogBook(long logbookId) throws DataStoreException {
		return DaoFactory.getFactory().createLogBookDAO()
				.deleteLogBook(logbookId);
	}

	@Override
	public MaterialSet saveMaterialSet(long logBookId, MaterialSet materialSet)
			throws DataStoreException {
		return DaoFactory.getFactory().createLogBookDAO()
				.saveMaterialSet(logBookId, materialSet);
	}

	@Override
	public boolean deleteMaterialSet(long logBookId, MaterialSet materialSet)
			throws DataStoreException {
		return DaoFactory.getFactory().createLogBookDAO()
				.deleteMaterialSet(logBookId, materialSet);
	}

	@Override
	public boolean deleteMaterialFromMaterialSet(long logbookId,
			long materialSetId, Material material) throws DataStoreException {
		return DaoFactory
				.getFactory()
				.createLogBookDAO()
				.deleteMaterialFromMaterialSet(logbookId, materialSetId,
						material);
	}

	@Override
	public boolean saveMaterialsIntoMaterialSet(long logbookId,
			long materialSetId, Material[] materials) throws DataStoreException {
		return DaoFactory
				.getFactory()
				.createLogBookDAO()
				.saveMaterialsIntoMaterialSet(logbookId, materialSetId,
						materials);
	}
}
