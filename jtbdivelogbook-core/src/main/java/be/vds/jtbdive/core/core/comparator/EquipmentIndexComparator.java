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
package be.vds.jtbdive.core.core.comparator;

import be.vds.jtbdive.core.core.material.Equipment;

import java.util.Comparator;

/**
 * A comparator for {@link Equipment}. It compares the equipment on the index
 * for equipment of the same kind. Otherwise, the comparator is based on the
 * name of the class (the name of the class is checked before the order index).
 * 
 * @author gautier
 */
public class EquipmentIndexComparator implements Comparator<Equipment> {

	@Override
	public int compare(Equipment o1, Equipment o2) {
		int i = o1.getClass().getName().compareTo(o2.getClass().getName());
		if (i != 0){
			return i;
		}

		return Integer.valueOf(o1.getOrderIndex()).compareTo(
				Integer.valueOf(o2.getOrderIndex()));

	}
}
