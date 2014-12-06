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

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.LogBookMeta;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;
import be.vds.jtbdive.core.exceptions.DataStoreException;

/**
 * This business delegate is used by the model to interact with persistence
 * layer. The business delegate cares for any connection to an application
 * server, a database, a file system, ...
 * 
 * @author gautier
 * 
 */
public abstract class LogBookBusinessDelegate {

	public abstract void initialize();

	public abstract List<LogBook> findLogBookNames() throws DataStoreException;

	public abstract LogBook findLogBook(long logBookId)
			throws DataStoreException;

	public abstract Dive saveDive(long loBookId, Dive dive)
			throws DataStoreException;

	public abstract Dive reloadDive(long logBookId, long diveId)
			throws DataStoreException;

	public abstract LogBookMeta saveLogBookMeta(LogBookMeta lb)
			throws DataStoreException;

	public abstract Dive deleteDive(long logbookId, Dive dive)
			throws DataStoreException;

	public abstract List<Dive> saveDives(List<Dive> dives, long id)
			throws DataStoreException;

	public abstract byte[] loadDocumentContent(long documentId,
			DocumentFormat format) throws DataStoreException;

	public abstract Material saveMaterial(long loBookId, Material material)
			throws DataStoreException;

	public abstract boolean deleteMaterial(long loBookId, Material material)
			throws DataStoreException;

	public abstract Material mergeMaterial(long loBookId,
			Material materialToKeep, Material materialToDelete)
			throws DataStoreException;

	public abstract boolean deleteLogBook(long loBookId)
			throws DataStoreException;

	public abstract MaterialSet saveMaterialSet(long loBookId,
			MaterialSet materialSet) throws DataStoreException;

	public abstract boolean deleteMaterialSet(long loBookId,
			MaterialSet materialSet) throws DataStoreException;

	public abstract boolean deleteMaterialFromMaterialSet(long loBookId,
			long materialSetId, Material material) throws DataStoreException;

	public abstract boolean saveMaterialsIntoMaterialSet(long loBookId,
			long materialSetId, Material[] materials) throws DataStoreException;

	public abstract boolean deleteMaterials(long id, List<Material> materials)
			throws DataStoreException;
}
