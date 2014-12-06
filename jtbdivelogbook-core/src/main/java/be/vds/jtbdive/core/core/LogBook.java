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
package be.vds.jtbdive.core.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import be.vds.jtbdive.core.core.material.Material;


public class LogBook implements Serializable {

	private static final long serialVersionUID = -2413265031166763945L;
	private LogBookMeta logbookMeta;
	private List<Dive> dives = new ArrayList<Dive>();
	private MatCave matCave = new MatCave();

	public LogBook() {
		logbookMeta = new LogBookMeta();
	}

	public LogBook(long id, String name) {
		logbookMeta = new LogBookMeta(id, name);
	}

	public LogBookMeta getLogbookMeta() {
		return logbookMeta;
	}

	public void setLogbookMeta(LogBookMeta logbookMeta) {
		this.logbookMeta = logbookMeta;
	}

	public void setName(String name) {
		logbookMeta.setName(name);
	}

	public Diver getOwner() {
		return logbookMeta.getOwner();
	}

	public long getId() {
		return logbookMeta.getId();
	}

	public List<Dive> getDives() {
		return dives;
	}

	public void setDives(List<Dive> dives) {
		this.dives = dives;
	}

	public void addDive(Dive dive) {
		this.dives.add(dive);
	}

	public Dive getDiveByNumber(int i) {
		for (Dive dive : dives) {
			if (i == dive.getNumber()) {
				return dive;
			}
		}
		return null;
	}

	public String getName() {
		return logbookMeta.getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (obj instanceof LogBook) {
			LogBook logBook = (LogBook) obj;
			return logBook.getId() == getId();
		}
		return false;
	}

	public void setId(long id) {
		this.logbookMeta.setId(id);
	}

	@Override
	public String toString() {
		return getName() + " (" + getId() + ")";
	}

	public int getNextDiveNumber() {
		int i = 0;
		for (Dive dive : dives) {
			if (i < dive.getNumber()) {
				i = dive.getNumber();
			}
		}
		return i + 1;
	}

	public Set<DiveSite> getDiveSites() {
		Set<DiveSite> dls = new HashSet<DiveSite>();
		for (Dive dive : dives) {
			if (dive.getDiveSite() != null) {
				dls.add(dive.getDiveSite());
			}
		}
		return dls;
	}

	public Set<Diver> getDivers() {
		Set<Diver> divers = new HashSet<Diver>();
		for (Dive dive : dives) {
			if (dive.getPalanquee() != null) {
				for (PalanqueeEntry entry : dive.getPalanquee()
						.getPalanqueeEntries()) {
					divers.add(entry.getDiver());
				}
			}
		}
		return divers;
	}

	public void setOwner(Diver owner) {
		this.logbookMeta.setOwner(owner);
	}

	public boolean removeDive(Dive dive) {
		return dives.remove(dive);
	}

	public MatCave getMatCave() {
		return matCave;
	}

	public boolean removeMaterial(Material material) {
		boolean b = matCave.removeMaterial(material);
		for (Dive dive : dives) {
			if (dive.getDiveEquipment() != null){
				dive.getDiveEquipment().removeMaterial(material);
			}
		}
		return b;
	}

	public void replaceMaterialForEquipements(Material newMaterial, Material oldMaterial) {
		for (Dive dive : getDives()) {
			if (dive.getDiveEquipment() != null) {
				dive.getDiveEquipment().replaceMaterialForEquipements(newMaterial, oldMaterial);
			}
		}
	}

	public void mergeMaterial(Material materialToKeep, Material materialToDelete) {
		replaceMaterialForEquipements(materialToKeep, materialToDelete);
		matCave.mergeMaterlial(materialToKeep, materialToDelete);
	}

	public boolean removeMaterials(List<Material> materials) {
		boolean b = false;
		for (Material material : materials) {
			b = b &removeMaterial(material);
		}
		return b;
	}

	public int hashCode() {
		long id = getId();
		if (-1 == id) {
			return (int) id;
		} else {
			return logbookMeta.getName().hashCode() + logbookMeta.getOwner().hashCode();
		}
	};
}
