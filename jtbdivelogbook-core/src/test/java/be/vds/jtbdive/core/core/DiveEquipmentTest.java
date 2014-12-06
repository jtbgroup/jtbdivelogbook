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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import be.vds.jtbdive.core.core.material.DiveTankEquipment;
import be.vds.jtbdive.core.core.material.Equipment;

public class DiveEquipmentTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testChangeEquipmentOrderIndexEquipmentInt() {
		DiveEquipment de = createDiveEquipmpent();

		DiveTankEquipment dte = de.getDiveTankForOrderIndex(0);
		assertEquals(0d, dte.getBeginPressure());
		dte = de.getDiveTankForOrderIndex(6);
		assertEquals(6d, dte.getBeginPressure());
		dte = de.getDiveTankForOrderIndex(3);
		assertEquals(3d, dte.getBeginPressure());

		DiveTankEquipment b = de.getDiveTankForOrderIndex(2);
		de.changeEquipmentOrderIndex(b, b.getOrderIndex() + 1);

		dte = de.getDiveTankForOrderIndex(0);
		assertEquals(0d, dte.getBeginPressure());
		dte = de.getDiveTankForOrderIndex(2);
		assertEquals(3d, dte.getBeginPressure());
		dte = de.getDiveTankForOrderIndex(3);
		assertEquals(2d, dte.getBeginPressure());
		dte = de.getDiveTankForOrderIndex(4);
		assertEquals(4d, dte.getBeginPressure());
		dte = de.getDiveTankForOrderIndex(5);
		assertEquals(4d, dte.getBeginPressure());
		dte = de.getDiveTankForOrderIndex(6);
		assertEquals(6d, dte.getBeginPressure());
		dte = de.getDiveTankForOrderIndex(7);
		assertEquals(7d, dte.getBeginPressure());

		b = de.getDiveTankForOrderIndex(7);
		de.changeEquipmentOrderIndex(b, 10);
		dte = de.getDiveTankForOrderIndex(7);
		assertNull(dte);
		dte = de.getDiveTankForOrderIndex(10);
		assertEquals(7d, dte.getBeginPressure());
	}

	private DiveEquipment createDiveEquipmpent() {
		List<DiveTankEquipment> tanks = new ArrayList<DiveTankEquipment>();

		fillDiveTank(tanks, 0);
		fillDiveTank(tanks, 2);
		fillDiveTank(tanks, 3);
		fillDiveTank(tanks, 4);
		fillDiveTank(tanks, 6);
		fillDiveTank(tanks, 7);

		DiveEquipment de = new DiveEquipment();
		de.setDiveTanks(tanks);
		return de;
	}

	private void fillDiveTank(List<DiveTankEquipment> tanks, int i) {
		DiveTankEquipment b = new DiveTankEquipment();
		b.setBeginPressure(i);
		b.setOrderIndex(i);
		tanks.add(b);
	}

}
