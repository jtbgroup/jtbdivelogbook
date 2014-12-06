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

package be.vds.jtbdive.core.utils;

import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.DefaultEquipment;
import be.vds.jtbdive.core.core.material.DiveComputerEquipment;
import be.vds.jtbdive.core.core.material.DiveTankEquipment;
import be.vds.jtbdive.core.core.material.Equipment;
import be.vds.jtbdive.core.core.material.FinsEquipment;
import be.vds.jtbdive.core.core.material.Material;

public final class EquipmentUtilities {

	private EquipmentUtilities() {
	}

	public static Equipment createEquipmentForMaterial(Material material) {
		Equipment eq = null;
		switch (material.getMaterialType()) {
		case BOOTS:
			eq = new DefaultEquipment(MaterialType.BOOTS);
			break;
		case BUOYANCY_COMPENSATOR:
			eq = new DefaultEquipment(MaterialType.BUOYANCY_COMPENSATOR);
			break;
		case COMPASS:
			eq = new DefaultEquipment(MaterialType.COMPASS);
			break;
		case DIVE_COMPUTER:
			eq = new DiveComputerEquipment();
			break;
		case DIVE_TANK:
			eq = new DiveTankEquipment();
			break;
		case FINS:
			eq = new FinsEquipment();
			break;
		case GLOVES:
			eq = new DefaultEquipment(MaterialType.GLOVES);
			break;
		case HOOD:
			eq = new DefaultEquipment(MaterialType.HOOD);
			break;
		case KNIFE:
			eq = new DefaultEquipment(MaterialType.KNIFE);
			break;
		case LIGHT:
			eq = new DefaultEquipment(MaterialType.LIGHT);
			break;
		case MANOMETER:
			eq = new DefaultEquipment(MaterialType.MANOMETER);
			break;
		case MASK:
			eq = new DefaultEquipment(MaterialType.MASK);
			break;
		case OTHER:
			eq = new DefaultEquipment(MaterialType.OTHER);
			break;
		case REBREATHER:
			eq = new DefaultEquipment(MaterialType.REBREATHER);
			break;
		case REGULATOR:
			eq = new DefaultEquipment(MaterialType.REGULATOR);
			break;
		case SCOOTER:
			eq = new DefaultEquipment(MaterialType.SCOOTER);
			break;
		case SNORKEL:
			eq = new DefaultEquipment(MaterialType.SNORKEL);
			break;
		case SUIT:
			eq = new DefaultEquipment(MaterialType.SUIT);
			break;
		case WATCH:
			eq = new DefaultEquipment(MaterialType.WATCH);
			break;
		case WEIGHT_BELT:
			eq = new DefaultEquipment(MaterialType.WEIGHT_BELT);
			break;

		default:
			eq = new DefaultEquipment(MaterialType.OTHER);
			break;
		}

		eq.setMaterial(material);
		return eq;
	}

}
