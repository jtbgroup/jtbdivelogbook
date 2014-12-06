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
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import be.vds.jtbdive.core.core.catalogs.ContactType;

public class DiverTest extends TestCase {

	public void testDiver() {
		Diver d = new Diver();
		assertNotNull(d);
		assertEquals(-1, d.getId());
		assertNull(d.getFirstName());
		assertNull(d.getLastName());
		assertNull(d.getBirthDate());
		assertNull(d.getContacts());
	}

	public void testDiverLong() {
		Diver d = new Diver(9);
		assertNotNull(d);
		assertEquals(9, d.getId());
		assertNull(d.getFirstName());
		assertNull(d.getLastName());
		assertNull(d.getBirthDate());
		assertNull(d.getContacts());
	}

	public void testGetLastName() {
		Diver d = new Diver();
		d.setLastName("Lastname");
		assertEquals("Lastname", d.getLastName());
	}

	public void testGetFirstName() {
		Diver d = new Diver();
		d.setFirstName("Firstname");
		assertEquals("Firstname", d.getFirstName());
	}

	public void testGetId() {
		Diver d = new Diver(9);
		assertEquals(9, d.getId());
	}

	public void testSetId() {
		Diver d = new Diver(9);
		assertEquals(9, d.getId());
		d.setId(10);
		assertEquals(10, d.getId());
	}

	public void testGetFullName() {
		Diver d = new Diver();
		d.setLastName("lastname");
		d.setFirstName("firstname");
		assertEquals("firstname lastname", d.getFullName());
	}

	public void testEqualsObject() {
		Diver d1 = new Diver();
		Diver d2 = new Diver();
		assertNotSame(d1, d2);

		d1.setId(9);
		assertNotSame(d1, d2);

		d2.setId(10);
		assertNotSame(d1, d2);

		d2.setId(9);
		assertEquals(d1, d2);
	}

	public void testGetBirthDate() {
		Diver d = new Diver();
		Date birthDate = new Date();
		d.setBirthDate(birthDate);
		assertEquals(birthDate, d.getBirthDate());
	}

	public void testGetContacts() {
		Diver d = new Diver();
		Contact c = new Contact(ContactType.PHONE);
		c.setValue("123456");
		List<Contact> contacts = new ArrayList<Contact>();
		contacts.add(c);
		d.setContacts(contacts);
		assertEquals(1, d.getContacts().size());
		assertEquals("123456", d.getContacts().get(0).getValue());
	}

}
