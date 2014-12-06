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
package be.vds.jtbdive.client.core.conversion;

import java.util.Comparator;

import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.Material;

public class UDDFMaterialComparator implements Comparator<Material> {
	@Override
	public int compare(Material m1, Material m2) {
		return convertForUDDF(m1.getMaterialType())
				.compareTo(convertForUDDF(m2.getMaterialType()));
	}

	private String convertForUDDF(MaterialType materialType) {
		switch (materialType) {
		case BOOTS:
			return "boots";
		case BUOYANCY_COMPENSATOR:
			return "buoyancycontroldevice";
		case COMPASS:
			return "compass";
		case DIVE_COMPUTER:
			return "divecomputer";
		case DIVE_TANK:
			return "tanks";
		case FINS:
			return "fins";
		case GLOVES:
			return "gloves";
		case KNIFE:
			return "knife";
		case LIGHT:
			return "light";
		case MASK:
			return "mask";
		case REBREATHER:
			return "rebreather";
		case REGULATOR:
			return "regulator";
		case SCOOTER:
			return "scooter";
		case SUIT:
			return "suit";
		case WATCH:
			return "watch";
		}
		return "variouspieces";
	}
}
