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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.LogBookMeta;
import be.vds.jtbdive.core.core.ModifiedDiveMemory;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.core.comparator.DiveDateComparator;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.LogBookBusinessDelegate;
import be.vds.jtbdive.core.logging.Syslog;

public class LogBookManager {

	private static final Syslog LOGGER = Syslog.getLogger(LogBookManager.class);
	private LogBook currentLogBook;
	private LogBookBusinessDelegate logBookBusinessDelegate;
	private Dive currentDive;
	private ModifiedDiveMemory modifiedDiveMemory;

	public LogBookManager(LogBookBusinessDelegate logBookBusinessDelegate) {
		this.logBookBusinessDelegate = logBookBusinessDelegate;
		modifiedDiveMemory = new ModifiedDiveMemory();
	}

	public LogBook getCurrentLogBook() {
		return currentLogBook;
	}

	public void setCurrentDive(Dive dive) {
		this.currentDive = dive;
	}

	public LogBook loadLogBook(long id) throws DataStoreException {
		LogBook lb = logBookBusinessDelegate.findLogBook(id);
		this.modifiedDiveMemory.removeAllDiveChanged();
		this.currentDive = null;
		if (lb != null) {
			currentLogBook = lb;
			LOGGER.info("Logbook " + currentLogBook.getName() + " loaded");
		}
		return lb;
	}

	public List<LogBook> getLogBookNames() throws DataStoreException {
		return logBookBusinessDelegate.findLogBookNames();
	}

	public List<Dive> deleteDives(List<Dive> dives) {
		List<Dive> deletedDive = new ArrayList<Dive>();

		for (Dive dive : dives) {
			try {
				deleteDive(dive);
				deletedDive.add(dive);
			} catch (DataStoreException e) {
				LOGGER.error(e);
			}
		}
		return deletedDive;
	}

	public Dive deleteDive(Dive dive) throws DataStoreException {
		if (dive.getId() == -1) {
			currentLogBook.removeDive(dive);
			modifiedDiveMemory.removeDiveChanged(dive);
			if (currentDive.equals(dive)){
				currentDive = null;
			}
			LOGGER.debug("dive deleted from the current logbook : " + dive);
			return dive;
		}

		Dive d = logBookBusinessDelegate.deleteDive(currentLogBook.getId(),
				dive);
		if (d != null) {
			currentLogBook.removeDive(dive);
			modifiedDiveMemory.removeDiveChanged(dive);
			if (currentDive.equals(dive)){
				currentDive = null;
			}
			LOGGER.debug("dive deleted from the current logbook : " + d);
		}

		return d;
	}

	public void setLogBookBusinessDelegate(
			LogBookBusinessDelegate logBookBusinessDelegate) {
		this.logBookBusinessDelegate = logBookBusinessDelegate;
		LOGGER.info("Business Delegate changed");
	}

	public void clearCurrentLogBook() {
		this.currentLogBook = null;
		this.currentDive = null;
		modifiedDiveMemory.removeAllDiveChanged();
	}

	public void addDive(Dive dive) {
		if (null != currentLogBook) {
			currentLogBook.addDive(dive);
			modifiedDiveMemory.diveChanged(dive);
		}
	}

	public void setCurrentLogBook(LogBook logBook) {
		currentLogBook = logBook;
	}

	public List<Dive> reunumberDives() {
		List<Dive> dives = new ArrayList<Dive>();
		Collections.sort(currentLogBook.getDives(), new DiveDateComparator());
		int i = 0;
		for (Dive dive : currentLogBook.getDives()) {
			int newNumber = ++i;
			int oldNumber = dive.getNumber();
			if (oldNumber != newNumber) {
				dive.setNumber(newNumber);
				dives.add(dive);
				modifiedDiveMemory.diveChanged(dive);
			}
		}
		return dives;
	}

	/**
	 * Saves a Dive and by default also saves the dive site and the divers if
	 * now saved yet.
	 * 
	 * @param dive
	 * @return
	 * @throws DataStoreException
	 */
	public Dive saveDive(Dive dive) throws DataStoreException {
		return saveDive(dive, true, true);
	}

	public Dive saveDive(Dive dive, boolean saveNewDiver,
			boolean saveNewDiveSite) throws DataStoreException {
		Dive result = logBookBusinessDelegate.saveDive(currentLogBook.getId(),
				dive);
		if (result != null) {
			currentLogBook.removeDive(dive);
			currentLogBook.addDive(result);
			modifiedDiveMemory.removeDiveChanged(dive);
		}
		return result;
	}

	public Dive reloadDive(Dive dive) throws DataStoreException {
		Dive result = logBookBusinessDelegate.reloadDive(
				currentLogBook.getId(), dive.getId());
		if (result != null) {
			currentLogBook.removeDive(dive);
			currentLogBook.addDive(result);
			modifiedDiveMemory.removeDiveChanged(dive);
		}
		return result;
	}

	public LogBookMeta saveLogBookMeta(LogBookMeta lb)
			throws DataStoreException {
		LogBookMeta m = logBookBusinessDelegate.saveLogBookMeta(lb);
		if (m != null) {
			currentLogBook = new LogBook();
			currentLogBook.setLogbookMeta(m);
		}
		return m;
	}

	public LogBookMeta updateLogBookMeta(LogBookMeta lb)
			throws DataStoreException {
		LogBookMeta m = logBookBusinessDelegate.saveLogBookMeta(lb);
		if (m != null) {
			currentLogBook.setLogbookMeta(m);
		}
		return m;
	}

	public List<Dive> saveDives(List<Dive> dives) throws DataStoreException {
		List<Dive> result = logBookBusinessDelegate.saveDives(dives,
				currentLogBook.getId());
		if (result != null) {
			for (Dive dive : dives) {
				currentLogBook.removeDive(dive);
			}

			for (Dive dive : result) {
				currentLogBook.addDive(dive);
			}
			modifiedDiveMemory.removeAllDiveChanged();
		}
		return result;
	}

	public boolean isModified(Dive dive) {
		return modifiedDiveMemory.hasDiveChanged(dive);
	}

	public void setDiveChanged(Dive dive) {
		modifiedDiveMemory.diveChanged(dive);
	}

	public Collection<Dive> getAllModifiedDives() {
		return modifiedDiveMemory.getChangedDives();
	}

	public byte[] loadDocumentContent(long documentId, DocumentFormat format)
			throws DataStoreException {
		return logBookBusinessDelegate.loadDocumentContent(documentId, format);
	}

	public Dive getCurrentDive() {
		return currentDive;
	}

	public Material saveMaterial(Material material) throws DataStoreException {
		Material newMat = logBookBusinessDelegate.saveMaterial(
				currentLogBook.getId(), material);
		currentLogBook.getMatCave().addMaterial(newMat);
		if (material.getId() > -1) {
			currentLogBook.replaceMaterialForEquipements(newMat, material);
		}
		return newMat;
	}

	public boolean deleteMaterial(Material material) throws DataStoreException {
		boolean b = logBookBusinessDelegate.deleteMaterial(
				currentLogBook.getId(), material);
		if (b) {
			b = currentLogBook.removeMaterial(material);
		}

		return b;
	}

	public Material mergeMaterial(Material materialToKeep,
			Material materialToDelete) throws DataStoreException {

		if (materialToDelete.getMaterialType() != materialToKeep
				.getMaterialType()) {
			throw new DataStoreException(
					"The materials are not of the same type. No merge possible.");
		}

		Material m = logBookBusinessDelegate.mergeMaterial(
				currentLogBook.getId(), materialToKeep, materialToDelete);
		if (m != null) {
			currentLogBook.mergeMaterial(materialToKeep, materialToDelete);
			return materialToKeep;
		}
		return null;
	}

	public boolean deleteLogBook(long logbookId) throws DataStoreException {
		boolean b = logBookBusinessDelegate.deleteLogBook(logbookId);
		if (currentLogBook != null && currentLogBook.getId() == logbookId) {
			currentLogBook = null;
			currentDive = null;
			modifiedDiveMemory.removeAllDiveChanged();
		}
		return b;
	}

	public MaterialSet saveMaterialSet(MaterialSet materialSet)
			throws DataStoreException {
		MaterialSet newMat = logBookBusinessDelegate.saveMaterialSet(
				currentLogBook.getId(), materialSet);
		currentLogBook.getMatCave().addMaterialSet(newMat);
		return newMat;
	}

	public MaterialSet addMaterialToMaterialSet(MaterialSet materialSet,
			Material[] materials) throws DataStoreException {
		boolean b = logBookBusinessDelegate.saveMaterialsIntoMaterialSet(
				currentLogBook.getId(), materialSet.getId(), materials);
		if (b) {
			currentLogBook.getMatCave().addMaterialToMaterialSet(materialSet,
					materials);
			MaterialSet ms = currentLogBook.getMatCave().getMaterialSet(
					materialSet.getId());
			LOGGER.info(materials.length
					+ " materials added to materialset (id: "
					+ materialSet.getId() + ")");
			return ms;
		}

		return null;
	}

	public boolean removeMaterialSet(MaterialSet materialSet)
			throws DataStoreException {
		boolean b = logBookBusinessDelegate.deleteMaterialSet(
				currentLogBook.getId(), materialSet);
		currentLogBook.getMatCave().removeMaterialSet(materialSet);
		return b;
	}

	public MaterialSet removeMaterialFromMaterialSet(MaterialSet materialSet,
			Material material) throws DataStoreException {
		boolean b = logBookBusinessDelegate.deleteMaterialFromMaterialSet(
				currentLogBook.getId(), materialSet.getId(), material);
		if (b) {
			currentLogBook.getMatCave().removeMaterialFromMaterialSet(
					materialSet, material);
			LOGGER.debug("Material " + material.getId()
					+ " removed from material set " + materialSet.getId());
			return currentLogBook.getMatCave().getMaterialSet(
					materialSet.getId());
		}
		return null;
	}

	public boolean deleteMaterials(List<Material> materials)
			throws DataStoreException {
		boolean b = logBookBusinessDelegate.deleteMaterials(
				currentLogBook.getId(), materials);
		if (b) {
			b = currentLogBook.removeMaterials(materials);
		}

		return b;
	}

}
