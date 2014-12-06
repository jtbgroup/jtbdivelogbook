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

public class VersionTest extends TestCase {

	public void testIsFormatValid(){
		assertTrue(Version.isFormatValid("3.4.5-SNAPSHOT"));
		assertTrue(Version.isFormatValid("3.4.5"));
		assertTrue(Version.isFormatValid("3.4"));
		assertTrue(Version.isFormatValid("3"));
		assertTrue(Version.isFormatValid("3-SNAPSHOT"));
		assertFalse(Version.isFormatValid("3.4.5.SNAPSHOT"));
	}
	
	public void testIsLowerThan(){
		Version v = Version.createVersion("3.4.5-SNAPSHOT");
		assertFalse("1", v.isLowerThan(Version.createVersion("1")));
		assertFalse("1.2", v.isLowerThan(Version.createVersion("1.2")));
		assertFalse("1.2.4", v.isLowerThan(Version.createVersion("1.2.4")));
		assertFalse("3.3.4", v.isLowerThan(Version.createVersion("3.3.4")));
		assertFalse("3.4.5-SNAPSHOT", v.isLowerThan(Version.createVersion("3.4.5-SNAPSHOT")));
		assertTrue("3.4.5", v.isLowerThan(Version.createVersion("3.4.5")));
		assertTrue("4.2.1", v.isLowerThan(Version.createVersion("4.2.1")));
		assertTrue("4.2", v.isLowerThan(Version.createVersion("4.2")));
		assertTrue("4", v.isLowerThan(Version.createVersion("4")));
		
		v = Version.createVersion("3.4.5");
		assertFalse("3.4.5-SNAPSHOT", v.isLowerThan(Version.createVersion("3.4.5-SNAPSHOT")));
		assertFalse("3.4.5", v.isLowerThan(Version.createVersion("3.4.5")));
		assertFalse("1", v.isLowerThan(Version.createVersion("1")));
		assertFalse("1.2", v.isLowerThan(Version.createVersion("1.2")));
		assertFalse("1.2.3", v.isLowerThan(Version.createVersion("1.2.3")));
		assertTrue("4", v.isLowerThan(Version.createVersion("4")));
		assertTrue("4-SNAPSHOT", v.isLowerThan(Version.createVersion("4-SNAPSHOT")));
		assertFalse("1-SNAPSHOT", v.isLowerThan(Version.createVersion("1-SNAPSHOT")));
	}
	
	public void testGetBaseVersion(){
		assertEquals("1", Version.createVersion("1").getBaseVersion());
		assertEquals("1.2", Version.createVersion("1.2").getBaseVersion());
		assertEquals("1.2.3", Version.createVersion("1.2.3").getBaseVersion());
		assertEquals("1", Version.createVersion("1-SNAPSHOT").getBaseVersion());
		assertEquals("1.2", Version.createVersion("1.2-SNAPSHOT").getBaseVersion());
		assertEquals("1.2.3", Version.createVersion("1.2.3-SNAPSHOT").getBaseVersion());
		assertEquals("1.2.3.4.5.6.7", Version.createVersion("1.2.3.4.5.6.7-SNAPSHOT").getBaseVersion());
	}
	
	public void testToString(){
		assertEquals("1", Version.createVersion("1").toString());
		assertEquals("1.2", Version.createVersion("1.2").toString());
		assertEquals("1.2.3", Version.createVersion("1.2.3").toString());
		assertEquals("1-SNAPSHOT", Version.createVersion("1-SNAPSHOT").toString());
		assertEquals("1.2-SNAPSHOT", Version.createVersion("1.2-SNAPSHOT").toString());
		assertEquals("1.2.3-SNAPSHOT", Version.createVersion("1.2.3-SNAPSHOT").toString());
		assertEquals("1.2.3.4.5.6.7-SNAPSHOT", Version.createVersion("1.2.3.4.5.6.7-SNAPSHOT").toString());
	}
	

}
