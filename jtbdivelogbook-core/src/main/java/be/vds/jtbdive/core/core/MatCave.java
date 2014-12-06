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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.DiveComputerMaterial;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;

/**
 * A Mat Cave is the shop of material that can be used inside a logbook. This
 * should be the material of the logbook's owner. <br>
 * The name is a funny reference to the Bat Cave... :-)
 * 
 * @author Vanderslyen.G
 * 
 */
public class MatCave implements Serializable {

	private static final long serialVersionUID = -8213939967721137938L;
	private Map<MaterialType, List<Material>> materialMap = new HashMap<MaterialType, List<Material>>();
	private Set<MaterialSet> materialSets = new HashSet<MaterialSet>();

	public Set<MaterialSet> getMaterialSets() {
		return materialSets;
	}

	/**
	 * Be careful using this setter: if a null is set as value, it can have a
	 * negative influence on the rest of the MatCave object.
	 * 
	 * @param materialSets
	 */
	public void setMaterialSets(Set<MaterialSet> materialSets) {
		this.materialSets = materialSets;
	}

	public void addMaterialSet(MaterialSet materialSet) {
		materialSets.add(materialSet);
	}

	public void removeMaterialSet(MaterialSet materialSet) {
		materialSets.remove(materialSet);
	}

	public void addMaterial(Material material) {
		List<Material> matList = materialMap.get(material.getMaterialType());
		int index = -1;
		if (matList == null) {
			matList = new ArrayList<Material>();
			materialMap.put(material.getMaterialType(), matList);
		} else {
			index = matList.indexOf(material);
			matList.remove(material);
		}

		if (index == -1) {
			matList.add(material);
		} else {
			matList.add(index, material);
		}
	}

	public List<Material> getAllMaterials() {
		return getAllMaterials(false);
	}

	public List<Material> getAllMaterials(boolean includeInactive) {
		List<Material> materials = new ArrayList<Material>();
		for (List<Material> list : materialMap.values()) {
			for (Material material : list) {
				if ((material.isActive())
						|| (!material.isActive() && includeInactive)) {
					materials.add(material);
				}
			}
		}
		return materials;
	}

	public void setMaterials(List<Material> materials) {
		clear();
		if (null != materials) {
			for (Material material : materials) {
				addMaterial(material);
			}
		}
	}

	private void clear() {
		materialMap.clear();
	}

	public int getSizeOfMaterial(MaterialType materialType) {
		List<Material> mat = materialMap.get(materialType);
		if (null == mat){
			return 0;
		}
		return mat.size();
	}

	public List<Material> getMaterials(MaterialType materialType) {
		return getMaterials(materialType, false);
	}

	public List<Material> getMaterials(MaterialType materialType,
			boolean includeInactive) {
		if (materialMap == null){
			return null;
		}

		Set<Material> mat = new HashSet<Material>();
		List<Material> all = materialMap.get(materialType);
		if (all == null) {
			return null;
		}

		for (Material material : all) {
			if ((material.isActive())
					|| ((!material.isActive()) && includeInactive)) {
				mat.add(material);
			}
		}
		return new ArrayList<Material>(mat);
	}

	/**
	 * Gets the material (active or not) according to the given ID
	 * 
	 * @param id
	 *            the ID of the desired material
	 * @return the desired material if found or null
	 */
	public Material getMaterialForId(long id) {
		for (Material mat : getAllMaterials(true)) {
			if (mat.getId() == id){
				return mat;
			}
		}
		return null;
	}

	public boolean removeMaterial(Material material) {
		boolean b = true;
		List<Material> mat = getMaterials(material.getMaterialType());
		if (mat != null) {
			b = mat.remove(material);
			for (MaterialSet ms : getMaterialSets()) {
				b = b & ms.removeMaterial(material);
			}

		}
		return b;
	}

	public List<MaterialType> getMaterialTypes() {
		List<MaterialType> types = new ArrayList<MaterialType>();
		types.addAll(materialMap.keySet());
		return types;
	}

	public Material getDiveComputerForSerialNumber(String serialNumber) {
		List<Material> computers = getMaterials(MaterialType.DIVE_COMPUTER);
		if (null == computers){
			return null;
		}

		for (Material mat : computers) {
			DiveComputerMaterial dc = (DiveComputerMaterial) mat;
			if (dc.getSerialNumber().equals(serialNumber)){
				return dc;
			}
		}
		return null;
	}

	public int size() {
		int counter = 0;
		for (List<Material> mats : materialMap.values()) {
			counter += mats.size();
		}
		return counter;
	}

	public boolean removeMaterialFromMaterialSet(MaterialSet materialSet,
			Material material) {
		boolean b = false;
		for (MaterialSet ms : materialSets) {
			if (ms.equals(materialSet)) {
				b = ms.removeMaterial(material);
				break;
			}
		}
		return b;
	}

	public MaterialSet getMaterialSet(long materialSetId) {
		for (MaterialSet ms : materialSets) {
			if (ms.getId() == materialSetId) {
				return ms;
			}
		}
		return null;
	}

	public boolean addMaterialToMaterialSet(MaterialSet materialSet,
			Material[] materials) {
		MaterialSet ms = getMaterialSet(materialSet.getId());
		if (ms == null){
			return false;
		}

		for (Material material : materials) {
			ms.addMaterial(material);
		}

		return true;
	}

	public void mergeMaterlial(Material materialToKeep,
			Material materialToDelete) {
		// adapt material sets
		if (null != materialSets) {
			for (MaterialSet ms : materialSets) {
				ms.mergeMaterial(materialToKeep, materialToDelete);
			}
		}

		// replace material
		List<Material> mats = materialMap.get(materialToDelete
				.getMaterialType());
		if (null != mats && mats.contains(materialToDelete)) {
			int index = mats.indexOf(materialToDelete);
			mats.remove(materialToDelete);
			if (!mats.contains(materialToKeep)) {
				mats.add(index, materialToKeep);
			}
		}
	}
}
