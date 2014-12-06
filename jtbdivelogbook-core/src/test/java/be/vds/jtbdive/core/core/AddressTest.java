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

import junit.framework.TestCase;

public class AddressTest extends TestCase {

	public void testGetCountry() {
		Address address = new Address();
		assertNull(address.getCountry());
		Country country = new Country("Belgium", "BE");
		address.setCountry(country);
		assertNotNull(address.getCountry());
		assertEquals(country, address.getCountry());
	}

	public void testGetStreet() {
		Address address = new Address();
		assertNull(address.getStreet());
		address.setStreet("testStreet");
		assertEquals("testStreet", address.getStreet());
	}

	public void testGetNumber() {
		Address address = new Address();
		assertNull(address.getNumber());
		address.setNumber("9A");
		assertEquals("9A", address.getNumber());
	}

	public void testGetZipCode() {
		Address address = new Address();
		assertNull(address.getZipCode());
		address.setZipCode("1000");
		assertEquals("1000", address.getZipCode());
	}

	public void testGetBox() {
		Address address = new Address();
		assertNull(address.getBox());
		address.setBox("B");
		assertEquals("B", address.getBox());
	}

	public void testGetCity() {
		Address address = new Address();
		assertNull(address.getCity());
		address.setCity("SuperCity");
		assertEquals("SuperCity", address.getCity());
	}

	public void testGetRegion() {
		Address address = new Address();
		assertNull(address.getRegion());
		address.setRegion("TestRegion");
		assertEquals("TestRegion", address.getRegion());
	}

}
