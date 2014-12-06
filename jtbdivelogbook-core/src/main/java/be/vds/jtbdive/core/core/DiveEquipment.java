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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.comparator.DiveTankSwitchTimeComparator;
import be.vds.jtbdive.core.core.comparator.EquipmentIndexComparator;
import be.vds.jtbdive.core.core.material.AbstractEquipment;
import be.vds.jtbdive.core.core.material.DiveTankEquipment;
import be.vds.jtbdive.core.core.material.Equipment;
import be.vds.jtbdive.core.core.material.Material;

public class DiveEquipment implements Serializable {

	private static final long serialVersionUID = -3961601316722436485L;
	private Map<MaterialType, List<Equipment>> equipmentMap = new HashMap<MaterialType, List<Equipment>>();

	public List<Equipment> getAllEquipments() {
		List<Equipment> equipments = new ArrayList<Equipment>();
		for (List<Equipment> list : equipmentMap.values()) {
			equipments.addAll(list);
		}
		return equipments;
	}

	public List<Equipment> getEquipments(MaterialType materialType) {
		return equipmentMap.get(materialType);
	}

	public void setEquipments(List<Equipment> equipments) {
		for (Equipment equipment : equipments) {
			addEquipment(equipment);
		}

		EquipmentIndexComparator comp = new EquipmentIndexComparator();
		for (List<Equipment> list : equipmentMap.values()) {
			Collections.sort(list, comp);
		}
	}

	public void changeEquipmentOrderIndex(Equipment equipment, int newIndex) {
		List<Equipment> list = changeEquipmentOrderIndex(equipment, newIndex,
				equipmentMap.get(equipment.getMaterialType()));
		if (null != list) {
			equipmentMap.put(equipment.getMaterialType(), list);
		}
	}

	private List<Equipment> changeEquipmentOrderIndex(Equipment equipment,
			int newIndex, List<Equipment> equipmentList) {

		boolean isHigerThanBefore = newIndex > equipment.getOrderIndex();
		List<Equipment> l = new ArrayList<Equipment>(equipmentList);
		l.remove(equipment);
		orderEquipments(l);
		int position = 0;
		for (Equipment equ : l) {
			if (equ.getOrderIndex() == newIndex) {
				if (isHigerThanBefore) {
					position = l.indexOf(equ) + 1;
				}
				break;
			} else if (equ.getOrderIndex() > newIndex) {
				break;
			}
			position = l.indexOf(equ) + 1;
		}
		equipment.setOrderIndex(newIndex);
		l.add(position, equipment);

		if (position + 1 < l.size()) {
			for (int i = position + 1; i < l.size(); i++) {
				if (l.get(i).getOrderIndex() <= l.get(i - 1).getOrderIndex()) {
					l.get(i).setOrderIndex(l.get(i - 1).getOrderIndex() + 1);
				}
			}
		}

		if (position - 1 >= 0) {
			for (int i = position - 1; i >= 0; i--) {
				if (l.get(i).getOrderIndex() >= l.get(i + 1).getOrderIndex()) {
					l.get(i).setOrderIndex(l.get(i + 1).getOrderIndex() - 1);
				}
			}
		}

		orderEquipments(l);
		return l;
	}

	private void orderEquipments(List<Equipment> l) {
		EquipmentIndexComparator comp = new EquipmentIndexComparator();
		Collections.sort(l, comp);
	}

	/**
	 * Returns the list of {@link DiveTankEquipment} known in the
	 * {@link DiveEquipment} or null if no {@link DiveTankEquipment} has been
	 * registered.
	 * 
	 * @return
	 */
	public List<DiveTankEquipment> getDiveTanks() {
		List<Equipment> equ = equipmentMap.get(MaterialType.DIVE_TANK);
		if (equ == null) {
			return null;
		}

		List<DiveTankEquipment> list = new ArrayList<DiveTankEquipment>();
		for (Equipment e : equ) {
			if (e instanceof DiveTankEquipment) {
				list.add((DiveTankEquipment) e);
			}
		}
		return list;
	}

	public void setDiveTanks(List<DiveTankEquipment> diveTanks) {
		for (DiveTankEquipment diveTankEquipment : diveTanks) {
			addEquipment(diveTankEquipment);
		}
		orderEquipments(equipmentMap.get(MaterialType.DIVE_TANK));
	}

	public boolean removeEquipment(Equipment equipment) {
		List<Equipment> mat = getEquipments(equipment.getMaterialType());
		if (mat != null) {
			return mat.remove(equipment);
		}
		return true;
	}

	public void addEquipment(Equipment equipment) {
		if (equipmentMap.get(equipment.getMaterialType()) == null) {
			equipmentMap.put(equipment.getMaterialType(),
					new ArrayList<Equipment>());
		}

		equipmentMap.get(equipment.getMaterialType()).add(equipment);
	}

	/**
	 * Returns the max order index for a material type.
	 * 
	 * @param materialType
	 *            the material type for which we want the max order index
	 * @return -1 if there is no equipment of this type, otherwise the maximum
	 *         index found for the type of material
	 */
	public int getMaxIndexForEquipment(MaterialType materialType) {
		List<Equipment> list = equipmentMap.get(materialType);
		if (list == null) {
			return -1;
		}
		int max = -1;
		for (Equipment equipment : list) {
			if (equipment.getOrderIndex() > max) {
				max = equipment.getOrderIndex();
			}
		}
		return max;
	}

	public void removeMaterial(Material material) {
		for (Equipment equipment : getAllEquipments()) {
			if (equipment.getMaterial() != null
					&& equipment.getMaterial().equals(material)) {
				equipment.setMaterial(null);
			}
		}
	}

	public List<Material> getMaterialPresent() {
		List<Material> m = new ArrayList<Material>();
		List<Equipment> equs = getAllEquipments();
		for (Equipment equipment : equs) {
			if (equipment.getMaterial() != null) {
				m.add(equipment.getMaterial());
			}
		}
		return m;
	}

	public Collection<Material> getMaterialPresent(MaterialType materialType) {
		List<Material> m = new ArrayList<Material>();
		List<Equipment> equs = getEquipments(materialType);
		for (Equipment equipment : equs) {
			if (equipment.getMaterial() != null) {
				m.add(equipment.getMaterial());
			}
		}
		return m;
	}

	public void replaceMaterialForEquipements(Material newMaterial,
			Material oldMaterial) {
		List<Equipment> equs = getEquipments(oldMaterial.getMaterialType());
		if (null != equs) {
			for (Equipment eq : equs) {
				if (eq.getMaterial() != null
						&& eq.getMaterial().equals(oldMaterial)) {
					eq.setMaterial(newMaterial);
				}
			}
		}
	}

	public Equipment getEquipmentForMaterial(Material material) {
		for (Equipment eq : equipmentMap.get(material.getMaterialType())) {
			if (eq.getMaterial() != null && eq.getMaterial().equals(material)) {
				return eq;
			}
		}
		return null;
	}

	public double getLastDiveTankSwitchTime() {
		List<DiveTankEquipment> tanks = getDiveTanks();
		if (null == tanks || tanks.isEmpty()) {
			return 0;
		}
		Collections.sort(tanks, new DiveTankSwitchTimeComparator());
		Collections.reverse(tanks);
		return tanks.get(0).getSwitchTime();
	}

	/**
	 * sets the maximum time for the equipments. This methods has an impact on
	 * the dive tanks' switch times.
	 * 
	 * @param seconds
	 *            the maximum seconds that can be used in the equipments. the
	 *            seconds equals to the provided param will be kept.
	 */
	public void setMaximumTime(double seconds) {
		List<DiveTankEquipment> tanks = getDiveTanks();
		if (null == tanks || tanks.isEmpty()) {
			return;
		}
		for (DiveTankEquipment diveTankEquipment : tanks) {
			if (diveTankEquipment.getSwitchTime() > seconds) {
				diveTankEquipment.setSwitchTime(seconds);
			}
		}
	}

	public boolean containsDiveTanks() {
		List<DiveTankEquipment> tanks = getDiveTanks();
		return tanks != null && !tanks.isEmpty();
	}

	public void removeEquipments(List<Equipment> equipments) {
		for (Equipment equipment : equipments) {
			removeEquipment(equipment);
		}
	}

	public DiveTankEquipment getDiveTankForOrderIndex(int i) {
		for (DiveTankEquipment equipment : getDiveTanks()) {
			if (equipment.getOrderIndex() == i) {
				return equipment;
			}
		}
		return null;
	}

}
