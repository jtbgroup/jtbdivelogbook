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

import be.vds.jtbdive.core.core.catalogs.Gaz;
import junit.framework.TestCase;

public class GazMixTest extends TestCase {

	public void testGetPercentage() {
		GazMix mix = new GazMix();
		mix.addGaz(Gaz.GAZ_NITROGEN, 79);
		assertTrue(79 == mix.getPercentage(Gaz.GAZ_NITROGEN));
		assertTrue(0 == mix.getPercentage(Gaz.GAZ_ARGON));
		mix.addGaz(Gaz.GAZ_NITROGEN, 20);
		assertTrue(20 == mix.getPercentage(Gaz.GAZ_NITROGEN));
		mix.addGaz(Gaz.GAZ_ARGON, 80);
		assertTrue(80 == mix.getPercentage(Gaz.GAZ_ARGON));
	}

	public void testGetGazes() {
		GazMix mix = new GazMix();
		assertNotNull(mix.getGazes());
		mix.addGaz(Gaz.GAZ_NITROGEN, 79);
		mix.addGaz(Gaz.GAZ_NITROGEN, 20);
		mix.addGaz(Gaz.GAZ_ARGON, 80);
		assertNotNull(mix.getGazes());
	}

	public void testSize() {
		GazMix mix = new GazMix();
		mix.addGaz(Gaz.GAZ_NITROGEN, 79);
		assertEquals(1, mix.getGazes().size());
		mix.addGaz(Gaz.GAZ_NITROGEN, 79);
		assertEquals(1, mix.getGazes().size());
		mix.addGaz(Gaz.GAZ_ARGON, 80);
		assertEquals(2, mix.getGazes().size());
	}

	public void testHasSameComposition() {
		GazMix mix1 = new GazMix();
		mix1.addGaz(Gaz.GAZ_NITROGEN, 79);
		mix1.addGaz(Gaz.GAZ_ARGON, 80);
		
		GazMix mix2 = new GazMix();
		mix2.addGaz(Gaz.GAZ_NITROGEN, 79);
		mix2.addGaz(Gaz.GAZ_ARGON, 80);
		
		assertNotSame(mix1, mix2);
		assertTrue(mix1.hasSameComposition(mix2));
		assertTrue(mix2.hasSameComposition(mix1));
	}

	public void testGetPercentRest() {
		GazMix mix1 = new GazMix();
		mix1.addGaz(Gaz.GAZ_NITROGEN, 60);
		assertEquals(40, mix1.getPercentRest());
		mix1.addGaz(Gaz.GAZ_ARGON, 10);
		assertEquals(30, mix1.getPercentRest());
		mix1.addGaz(Gaz.GAZ_NITROGEN, 20);
		assertEquals(70, mix1.getPercentRest());
		mix1.addGaz(Gaz.GAZ_HELIUM, 90);
		assertEquals(0, mix1.getPercentRest());
	}

	public void testGetDefaultGazMix() {
		GazMix mix1 = GazMix.getDefaultGazMix();
		assertTrue(21 == mix1.getPercentage(Gaz.GAZ_OXYGEN));
		assertTrue(79 == mix1.getPercentage(Gaz.GAZ_NITROGEN));
		assertTrue(0 == mix1.getPercentage(Gaz.GAZ_HELIUM));
		assertEquals(0, mix1.getPercentRest());
	}

}
