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

import be.vds.jtbdive.core.core.catalogs.ContactType;
import junit.framework.TestCase;

public class ContactTest extends TestCase {

	public void testContact() {
		Contact c = new Contact(ContactType.EMAIL);
		assertNotNull(c.getContactType());
		assertNull(c.getValue());
	}

	public void testGetValue() {
		Contact c = new Contact(ContactType.EMAIL);
		c.setValue("abc@mail.com");
		assertNotNull(c.getValue());
		assertEquals("abc@mail.com", c.getValue());
	}

	public void testGetContactType() {
		Contact c = new Contact(ContactType.EMAIL);
		assertNotSame(ContactType.MOBILE, c.getContactType());
		assertEquals(ContactType.EMAIL, c.getContactType());
		
		c = new Contact(ContactType.MOBILE);
		assertNotSame(ContactType.PHONE, c.getContactType());
		assertEquals(ContactType.MOBILE, c.getContactType());
	}

}
