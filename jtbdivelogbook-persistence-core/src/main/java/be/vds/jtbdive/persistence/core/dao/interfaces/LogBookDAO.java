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

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.LogBookMeta;
import java.util.List;

import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;
import be.vds.jtbdive.core.exceptions.DataStoreException;

public interface LogBookDAO {

	List<LogBook> findLogBookNames() throws DataStoreException;

	LogBook findLogBook(long id) throws DataStoreException;

	// LogBook saveLogBook(LogBook logBook) throws DataStoreException;

	void mergeDiveLocations(DiveSite diveLocationToKeep,
			DiveSite diveLocationToDelete) throws DataStoreException;

	boolean isDiveSiteUsed(DiveSite diveLocation) throws DataStoreException;

	boolean isDiverUsed(Diver diver) throws DataStoreException;

	void mergeDivers(Diver diverToKeep, Diver diverToDelete)
			throws DataStoreException;

	Dive saveDive(long logbookId, Dive dive) throws DataStoreException;

	Dive reloadDive(long logbookId, long diveId) throws DataStoreException;

	LogBookMeta saveLogBookMeta(LogBookMeta m) throws DataStoreException;

	Dive deleteDive(long logbookId, Dive dive) throws DataStoreException;

	List<Dive> saveDives(List<Dive> lb, long logbookId)
			throws DataStoreException;

	byte[] loadDocumentContent(long documentId, DocumentFormat format)
			throws DataStoreException;

	Material saveMaterial(long logbookId, Material material)
			throws DataStoreException;

	boolean deleteMaterial(long logbookId, Material material)
			throws DataStoreException;

	boolean deleteMaterials(long logbookId, List<Material> materials)
			throws DataStoreException;

	Material mergeMaterial(long logbookId, Material materialToKeep,
			Material materialToDelete) throws DataStoreException;

	boolean deleteLogBook(long logbookId) throws DataStoreException;

	MaterialSet saveMaterialSet(long logbookId, MaterialSet materialset)
			throws DataStoreException;

	boolean deleteMaterialSet(long logBookId, MaterialSet materialSet)
			throws DataStoreException;

	boolean deleteMaterialFromMaterialSet(long logbookId, long materialSetId,
			Material material) throws DataStoreException;

	boolean saveMaterialsIntoMaterialSet(long logbookId, long materialSetId,
			Material[] materials) throws DataStoreException;

	long getMaxDocumentId(long id) throws DataStoreException;
}
