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

import java.security.InvalidParameterException;

import junit.framework.TestCase;

public class CoordinatesTest extends TestCase {

	public void testGetLatitude() {
		Coordinates c = new Coordinates(9, 9);
		assertTrue(9 == c.getLatitude());
		
		try{
			c.setLatitude(9000);
			fail("Not supposed to arrive here");
		}catch (InvalidParameterException e) {
			assertTrue(9 == c.getLatitude());
		}
		
		try{
			c.setLatitude(-9000);
			fail("Not supposed to arrive here");
		}catch (InvalidParameterException e) {
			assertTrue(9 == c.getLatitude());
		}
		
		c.setLatitude(-9);
		assertTrue(-9 == c.getLatitude());
		c.setLatitude(9);
		assertTrue(9 == c.getLatitude());
		
		c.setLatitude(Coordinates.MAX_LATITUDE);
		assertTrue(Coordinates.MAX_LATITUDE == c.getLatitude());
		c.setLatitude(-Coordinates.MAX_LATITUDE);
		assertTrue(-Coordinates.MAX_LATITUDE == c.getLatitude());
	}

	public void testGetLongitude() {
		Coordinates c = new Coordinates(9, 9);
		assertTrue(9 == c.getLongitude());
		
		try{
			c.setLongitude(9000);
			fail("Not supposed to arrive here");
		}catch (InvalidParameterException e) {
			assertTrue(9 == c.getLongitude());
		}
		
		try{
			c.setLongitude(-9000);
			fail("Not supposed to arrive here");
		}catch (InvalidParameterException e) {
			assertTrue(9 == c.getLongitude());
		}
		
		c.setLongitude(-9);
		assertTrue(-9 == c.getLongitude());
		c.setLongitude(9);
		assertTrue(9 == c.getLongitude());
		
		c.setLongitude(Coordinates.MAX_LONGITUDE);
		assertTrue(Coordinates.MAX_LONGITUDE == c.getLongitude());
		c.setLongitude(-Coordinates.MAX_LONGITUDE);
		assertTrue(-Coordinates.MAX_LONGITUDE == c.getLongitude());
	}

}
