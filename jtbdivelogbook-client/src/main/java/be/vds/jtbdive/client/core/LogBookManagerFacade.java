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

import java.util.Collection;
import java.util.List;
import java.util.Observable;

import be.vds.jtbdive.client.core.event.DiveModification;
import be.vds.jtbdive.client.core.event.LogBookEvent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.LogBookMeta;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.core.manager.LogBookManager;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.LogBookBusinessDelegate;

public class LogBookManagerFacade extends Observable implements
		DocumentContentLoader {

	private LogBookManager logBookManager;

	public LogBookManagerFacade(LogBookManager logBookManager) {
		this.logBookManager = logBookManager;
	}

	public LogBook loadLogBook(long id) throws DataStoreException {
		LogBook lb = logBookManager.loadLogBook(id);
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.LOGBOOK_LOADED, null,
				logBookManager.getCurrentLogBook()));
		return lb;
	}

	public List<LogBook> getLogBookNames() throws DataStoreException {
		return logBookManager.getLogBookNames();
	}

	public LogBook getCurrentLogBook() {
		return logBookManager.getCurrentLogBook();
	}

	public Dive deleteDive(Dive dive) throws DataStoreException {
		Dive b = logBookManager.deleteDive(dive);
		if (b != null) {
			setChanged();
			notifyObservers(new LogBookEvent(LogBookEvent.DIVE_DELETED, dive,
					null));
		}
		return b;
	}

	public List<Dive> deleteDives(List<Dive> dives) {
		List<Dive> deletedDives = logBookManager.deleteDives(dives);
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.DIVES_DELETED,
				deletedDives, null));
		return deletedDives;
	}

	public void clearCurrentLogBook() {
		logBookManager.clearCurrentLogBook();
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.LOGBOOK_CLOSED, null,
				null));
	}

	public void addDive(Dive dive) {
		logBookManager.addDive(dive);
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.DIVE_ADDED, null, dive));
	}

	public void setCurrentLogBook(LogBook logBook) {
		logBookManager.setCurrentLogBook(logBook);
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.LOGBOOK_LOADED, null,
				null));
	}

	public List<Dive> reunumberDives() {
		List<Dive> result = logBookManager.reunumberDives();

		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.LOGBOOK_LOADED, null,
				null));
		return result;
	}

	public void setBusinessDelegate(
			LogBookBusinessDelegate logBookBusinessDelegate) {
		logBookManager.setLogBookBusinessDelegate(logBookBusinessDelegate);
	}

	public Dive saveDive(Dive dive) throws DataStoreException {
		Dive result = logBookManager.saveDive(dive);
		if (result != null) {
			setChanged();
			notifyObservers(new LogBookEvent(LogBookEvent.DIVE_SAVE, dive,
					result));
		}
		return result;
	}

	public Dive reloadDive(Dive dive) throws DataStoreException {
		Dive result = logBookManager.reloadDive(dive);
		if (result != null) {
			setChanged();
			notifyObservers(new LogBookEvent(LogBookEvent.DIVE_RELOAD, dive,
					result));
		}
		return result;
	}

	public LogBookMeta saveLogBookMeta(LogBookMeta lb)
			throws DataStoreException {
		LogBookMeta result = logBookManager.saveLogBookMeta(lb);
		if (null != result) {
			setChanged();
			notifyObservers(new LogBookEvent(LogBookEvent.LOGBOOK_META_SAVED,
					null, result));
		}
		return result;
	}

	public LogBookMeta updateLogBookMeta(LogBookMeta lb)
			throws DataStoreException {
		LogBookMeta result = logBookManager.updateLogBookMeta(lb);
		if (null != result) {
			setChanged();
			notifyObservers(new LogBookEvent(LogBookEvent.LOGBOOK_META_SAVED,
					null, result));
		}
		return result;
	}

	public List<Dive> saveDives(List<Dive> dives) throws DataStoreException {
		List<Dive> result = logBookManager.saveDives(dives);
		if (null != result) {
			setChanged();
			notifyObservers(new LogBookEvent(LogBookEvent.DIVES_SAVE, dives,
					result));
		}
		return result;
	}

	public void setCurrentDive(Dive dive) {
		logBookManager.setCurrentDive(dive);
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.CURRENT_DIVE_CHANGED,
				null, dive));
	}

	public void setDiveChanged(Dive dive) {
		logBookManager.setDiveChanged(dive);
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.DIVE_MODIFIED, null, dive));
	}

	public void setDiveChanged(Dive dive, DiveModification diveModification) {
		logBookManager.setDiveChanged(dive);
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.DIVE_MODIFIED, null,
				dive, diveModification));
	}

	public boolean isDiveModified(Dive dive) {
		return logBookManager.isModified(dive);
	}

	public Collection<Dive> getAllModifiedDives() {
		return logBookManager.getAllModifiedDives();
	}

	@Override
	public byte[] loadDocumentContent(long documentId, DocumentFormat format)
			throws DataStoreException {
		byte[] array = logBookManager.loadDocumentContent(documentId, format);
		return array;
	}

	public Dive getCurrentDive() {
		return logBookManager.getCurrentDive();
	}

	public Material saveMaterial(Material material) throws DataStoreException {
		Material oldMaterial = null;
		if (material.getId() > -1) {
			oldMaterial = logBookManager.getCurrentLogBook().getMatCave()
					.getMaterialForId(material.getId());
		}

		Material newMat = logBookManager.saveMaterial(material);
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.MATERIAL_SAVED,
				oldMaterial, newMat));
		return newMat;
	}

	public void deleteMaterial(Material material) throws DataStoreException {
		logBookManager.deleteMaterial(material);
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.MATERIAL_DELETED,
				material, null));
	}

	public void mergeMaterial(Material materialToKeep, Material materialToDelete)
			throws DataStoreException {
		logBookManager.mergeMaterial(materialToKeep, materialToDelete);
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.MATERIAL_MERGED,
				materialToDelete, materialToKeep));

	}

	public MatCave getCurrentMatCave() {
		LogBook lb = getCurrentLogBook();
		if (lb == null)
			return null;

		return lb.getMatCave();
	}

	public void deleteLogBook(long logbookId) throws DataStoreException {
		boolean b = logBookManager.deleteLogBook(logbookId);
		if (b) {
			setChanged();
			notifyObservers(new LogBookEvent(LogBookEvent.LOGBOOK_DELETED,
					logbookId, null));
		}
	}

	public MaterialSet saveMaterialSet(MaterialSet materialSet)
			throws DataStoreException {
		Material oldMaterialSet = null;
		if (materialSet.getId() > -1) {
			oldMaterialSet = logBookManager.getCurrentLogBook().getMatCave()
					.getMaterialForId(materialSet.getId());
		}

		MaterialSet newMaterialSet = logBookManager
				.saveMaterialSet(materialSet);
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.MATERIALSET_SAVED,
				oldMaterialSet, newMaterialSet));
		return newMaterialSet;
	}

	public MaterialSet addMaterialToMaterialSet(MaterialSet materialSet,
			Material[] materials) throws DataStoreException {
		MaterialSet result = logBookManager.addMaterialToMaterialSet(
				materialSet, materials);
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.MATERIALSET_SAVED,
				materialSet, result));
		return result;
	}

	public boolean removeMaterialSet(MaterialSet materialSet)
			throws DataStoreException {
		boolean b = logBookManager.removeMaterialSet(materialSet);
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.MATERIALSET_DELETED,
				materialSet, null));
		return b;
	}

	public MaterialSet removeMaterialFromMaterialSet(MaterialSet materialSet,
			Material material) throws DataStoreException {
		MaterialSet result = logBookManager.removeMaterialFromMaterialSet(
				materialSet, material);
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.MATERIALSET_SAVED,
				materialSet, result));
		return result;

	}

	public void deleteMaterials(List<Material> materials)
			throws DataStoreException {
		logBookManager.deleteMaterials(materials);
		setChanged();
		notifyObservers(new LogBookEvent(LogBookEvent.MATERIALS_DELETED,
				materials, null));
	}

}
