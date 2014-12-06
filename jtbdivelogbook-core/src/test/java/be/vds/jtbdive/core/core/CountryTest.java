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

public class CountryTest extends TestCase {

	public void testGetName() {
		Country country = new Country("Belgium", "BE");
		assertTrue("Belgium".equals(country.getName()));
		assertFalse("belgium".equals(country.getName()));
	}

	public void testGetCode() {
		Country country = new Country("Belgium", "BE");
		assertTrue("BE".equals(country.getCode()));
		assertFalse("be".equals(country.getCode()));
	}

	public void testEqualsObject() {
		Country be1 = new Country("Belgium", "BE");
		Country be2 = new Country("belgique", "BE");
		Country fr = new Country("France", "FR");
		assertTrue(be1.equals(be2));
		assertTrue(be2.equals(be1));
		assertFalse(fr.equals(be1));
	}

	public void testToString() {
		Country be1 = new Country("Belgium", "BE");
		assertTrue("BE".equals(be1.toString()));
		Country fr = new Country("France", "FR");
		assertTrue("FR".equals(fr.toString()));
		assertFalse("Fr".equals(fr.toString()));
	}

}
