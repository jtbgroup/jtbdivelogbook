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

import java.util.Date;

import junit.framework.TestCase;

public class DiveTest extends TestCase {

	public void testHashCode() {
		Dive d1 = new Dive();
		d1.setComment("Bla bla");
		assertTrue(d1.equals(d1));
		assertTrue(d1.hashCode() == d1.hashCode());

		Dive d2 = new Dive();
		d2.setComment("Bla bla");
		assertFalse(d2.equals(d1));
		assertFalse(d2.hashCode() == d1.hashCode());

		d1 = new Dive(20);
		d2 = new Dive(20);
		assertTrue(d2.equals(d1));
		assertTrue(d2.hashCode() == d1.hashCode());

		d2 = new Dive(30);
		assertFalse(d2.equals(d1));
		assertFalse(d2.hashCode() == d1.hashCode());
	}

	public void testGetRating() {
		Dive dive = new Dive();
		Rating rating = new Rating();
		rating.setValue((short) 5);
		dive.setRating(rating);

		assertFalse(10 == dive.getRating().getValue());
		assertTrue(5 == dive.getRating().getValue());

		rating.setValue((short) 10);
		dive.setRating(rating);
		assertFalse(5 == dive.getRating().getValue());
		assertTrue(10 == dive.getRating().getValue());

		rating.setValue((short) 20);
		dive.setRating(rating);
		assertFalse(5 == dive.getRating().getValue());
		assertTrue(10 == dive.getRating().getValue());
	}

	public void testSetRating() {
		Dive d = new Dive();
		assertNull(d.getRating());
		d.setRating(new Rating());
		assertNotNull(d.getRating());
	}

	public void testDive() {
		Dive d = new Dive();
		assertNotNull(d);
		assertEquals(0, d.getNumber());
	}

	public void testDiveInt() {
		Dive d = new Dive(9);
		assertEquals(9, d.getNumber());
		assertNull(d.getDate());
	}

	public void testDiveIntDate() {
		Dive d = new Dive(9, new Date(System.currentTimeMillis()));
		assertEquals(9, d.getNumber());
		assertNotNull(d.getDate());
	}

	public void testDiveDate() {
		Dive d = new Dive(new Date(System.currentTimeMillis()));
		assertEquals(0, d.getNumber());
		assertNotNull(d.getDate());
	}

	public void testGetDiveEquipment() {
		fail("Not yet implemented");
	}

	public void testSetDiveEquipment() {
		fail("Not yet implemented");
	}

	public void testGetDate() {
		fail("Not yet implemented");
	}

	public void testToString() {
		fail("Not yet implemented");
	}

	public void testGetNumber() {
		Dive d = new Dive(9);
		assertEquals(9, d.getNumber());
	}

	public void testSetNumber() {
		Dive d = new Dive();
		assertEquals(0, d.getNumber());
		d.setNumber(9);
		assertEquals(9, d.getNumber());
	}

	public void testGetComment() {
		fail("Not yet implemented");
	}

	public void testSetComment() {
		String comment = "a comment";
		Dive d = new Dive();
		assertNull(d.getComment());
		d.setComment(comment);
		assertEquals(comment, d.getComment());
	}

	public void testSetDate() {
		Date date = new Date(System.currentTimeMillis());
		Dive d = new Dive();
		assertNull(d.getDate());
		d.setDate(date);
		assertEquals(date, d.getDate());
	}

	public void testGetDiveTime() {
		fail("Not yet implemented");
	}

	public void testSetDiveTime() {
		fail("Not yet implemented");
	}

	public void testGetMaxDepth() {
		fail("Not yet implemented");
	}

	public void testSetMaxDepth() {
		fail("Not yet implemented");
	}

	public void testGetWaterTemperature() {
		fail("Not yet implemented");
	}

	public void testSetWaterTemperature() {
		fail("Not yet implemented");
	}

	public void testEqualsObject() {
		fail("Not yet implemented");
	}

	public void testSetDiveProfile() {
		fail("Not yet implemented");
	}

	public void testGetDiveProfile() {
		fail("Not yet implemented");
	}

	public void testSetId() {
		fail("Not yet implemented");
	}

	public void testGetId() {
		fail("Not yet implemented");
	}

	public void testSetPalanquee() {
		fail("Not yet implemented");
	}

	public void testGetPalanquee() {
		fail("Not yet implemented");
	}

	public void testGetDiveSite() {
		fail("Not yet implemented");
	}

	public void testSetDiveSite() {
		fail("Not yet implemented");
	}

	public void testSetSurfaceTime() {
		fail("Not yet implemented");
	}

	public void testGetSurfaceTime() {
		fail("Not yet implemented");
	}

	public void testSetPhysiologicalStatus() {
		fail("Not yet implemented");
	}

	public void testGetPhysiologicalStatus() {
		fail("Not yet implemented");
	}

	public void testSetDocuments() {
		fail("Not yet implemented");
	}

	public void testAddDocument() {
		fail("Not yet implemented");
	}

	public void testRemoveDocument() {
		fail("Not yet implemented");
	}

	public void testGetDocuments() {
		fail("Not yet implemented");
	}

	public void testGetDivePurposes() {
		fail("Not yet implemented");
	}

	public void testGetDiveTypes() {
		fail("Not yet implemented");
	}

	public void testSetDiveType() {
		fail("Not yet implemented");
	}

	public void testSetDivePurposes() {
		fail("Not yet implemented");
	}

	public void testGetDivePlatform() {
		fail("Not yet implemented");
	}

	public void testSetDivePlatform() {
		fail("Not yet implemented");
	}

}
