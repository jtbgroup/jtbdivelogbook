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
package be.vds.jtbdive.core.core.material;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import be.vds.jtbdive.core.core.catalogs.MaterialType;

public class MaterialSet {

	private long id = -1;
	private Set<Material> materials = new HashSet<Material>();
	private String name;

	public MaterialSet(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public boolean addMaterial(Material material) {
		return this.materials.add(material);
	}

	public boolean removeMaterial(Material material) {
		return this.materials.remove(material);
	}

	public void setMaterials(Collection<Material> materials) {
		this.materials.clear();
		this.materials.addAll(materials);
	}

	public Set<Material> getMaterials() {
		return this.materials;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null){
			return false;
		}
		if (obj instanceof MaterialSet) {
			MaterialSet ms = (MaterialSet) obj;
			if (ms.getId() == -1 && this.id == -1) {
				return super.equals(ms);
			} else {
				return ms.getId() == this.id;
			}
		}
		return false;
	}

	public void mergeMaterial(Material materialToKeep, Material materialToDelete) {
		if (materials.contains(materialToDelete)) {
			materials.remove(materialToDelete);
			materials.add(materialToKeep);
		}
	}

	public Set<MaterialType> getMaterialTypes() {
		Set<MaterialType> types = new HashSet<MaterialType>();
		for (Material m : materials) {
			types.add(m.getMaterialType());
		}
		return types;
	}

	public int hashCode() {
		if (-1 == id) {
			return (int) id;
		} else {
			return name.hashCode();
		}
	};
}
