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

import be.vds.jtbdive.core.core.catalogs.DiveSiteType;
import junit.framework.TestCase;

public class DiveSiteTest extends TestCase {

	public void testDiveSite() {
		DiveSite ds = new DiveSite();
		assertEquals(-1, ds.getId());
		assertNull(ds.getInternetSite());
		assertNull(ds.getName());
		assertNull(ds.getAddress());
		assertEquals(0.0, ds.getAltitude());
		assertNull(ds.getCoordinates());
		assertEquals(0.0, ds.getDepth());
		assertNull(ds.getDiveSiteType());
		assertNull(ds.getDocuments());
	}

	public void testGetDiveSiteType() {
		DiveSite ds = new DiveSite();
		ds.setDiveSiteType(DiveSiteType.CAVE_CAVERN);
		assertEquals(DiveSiteType.CAVE_CAVERN, ds.getDiveSiteType());
		assertNotSame(DiveSiteType.LAKE_QUARRY, ds.getDiveSiteType());
	}

	public void testGetId() {
		DiveSite ds = new DiveSite();
		assertEquals(-1, ds.getId());
		ds.setId(9);
		assertEquals(9, ds.getId());
	}

	public void testGetDepth() {
		DiveSite ds = new DiveSite();
		assertEquals(0.0, ds.getDepth());
		ds.setDepth(9.9);
		assertEquals(9.9, ds.getDepth());
		assertNotSame(10, ds.getDepth());
	}

	public void testGetName() {
		DiveSite ds = new DiveSite();
		assertNull(ds.getName());
		ds.setName("site é&à§§--");
		assertEquals("site é&à§§--", ds.getName());
		assertNotSame("kqsjdsqdj", ds.getName());
	}

	public void testGetAddress() {
		DiveSite ds = new DiveSite();
		assertNull(ds.getAddress());
		Address address = new Address();
		address.setStreet("street");
		ds.setAddress(address);
		assertNotNull(ds.getAddress());
		assertEquals("street", ds.getAddress().getStreet());
	}

	public void testGetAltitude() {
		DiveSite ds = new DiveSite();
		assertEquals(0.0, ds.getAltitude());
		ds.setAltitude(9.9);
		assertNotSame(10, ds.getAltitude());
		assertEquals(9.9, ds.getAltitude());
	}

	public void testEqualsObject() {
		DiveSite ds1 = new DiveSite();
		DiveSite ds2 = new DiveSite();
		assertNotSame(ds1, ds2);

		ds2.setId(9);
		assertNotSame(ds1, ds2);

		ds1.setId(10);
		assertNotSame(ds1, ds2);

		ds1.setId(9);
		assertEquals(ds1, ds2);
	}

	public void testGetDocuments() {
		DiveSite ds = new DiveSite();
		assertNull(ds.getDocuments());

		Document doc = new Document();
		List<Document> docs = new ArrayList<Document>();
		docs.add(doc);
		ds.setDocuments(docs);
		assertNotNull(ds.getDocuments());
		assertEquals(docs, ds.getDocuments());
		assertEquals(1, ds.getDocuments().size());
	}

	public void testGetCoordinates() {
		DiveSite ds = new DiveSite();
		assertNull(ds.getCoordinates());
		Coordinates coordinates = new Coordinates(1.1, 2.2);
		ds.setCoordinates(coordinates);
		assertNotSame(new Coordinates(3.3, 4.4), ds.getCoordinates());
		assertEquals(coordinates, ds.getCoordinates());
	}

	public void testHasCoordinates() {
		DiveSite ds = new DiveSite();
		assertFalse(ds.hasCoordinates());
		Coordinates coordinates = new Coordinates(1.1, 2.2);
		ds.setCoordinates(coordinates);
		assertTrue(ds.hasCoordinates());
	}


	public void testGetInternetSite() {
		DiveSite ds = new DiveSite();
		assertNull(ds.getInternetSite());
		ds.setInternetSite("http://www.mysite.be");
		assertNotSame("http://www.google.com", ds.getInternetSite());
		assertEquals("http://www.mysite.be", ds.getInternetSite());
	}

}
