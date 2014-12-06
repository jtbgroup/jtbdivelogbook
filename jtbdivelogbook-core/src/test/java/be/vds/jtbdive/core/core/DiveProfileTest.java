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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

public class DiveProfileTest extends TestCase {

	public void testGetDepthEntries() {
		DiveProfile dp = new DiveProfile();
		assertNotNull(dp.getDepthEntries());
		assertEquals(0, dp.getDepthEntries().size());
		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		depthEntries.put(1d, -1d);
		depthEntries.put(2d, -2d);
		depthEntries.put(3d, -3d);
		depthEntries.put(4d, -4d);
		dp.setDepthEntries(depthEntries);
		assertEquals(depthEntries.size(), dp.getDepthEntries().size());
	}

	public void testSetDepthEntries() {
		DiveProfile dp = new DiveProfile();

		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		depthEntries.put(1d, -1d);
		depthEntries.put(2d, -2d);
		depthEntries.put(3d, -3d);
		depthEntries.put(4d, -4d);
		depthEntries.put(-5d, -5d);
		try {
			dp.setDepthEntries(depthEntries);
			fail("Not supposed to arrive here");
		} catch (InvalidParameterException e) {
			depthEntries.remove(-5d);
		}

		depthEntries.put(5d, -5d);
		try {
			dp.setDepthEntries(depthEntries);
		} catch (InvalidParameterException e) {
			fail("Not supposed to arrive here");
		}

		assertEquals(5, dp.getDepthEntries().size());
	}

	public void testSetDecoCeilingWarnings() {
		DiveProfile dp = new DiveProfile();

		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		depthEntries.put(1d, -1d);
		depthEntries.put(2d, -2d);
		dp.setDepthEntries(depthEntries);

		Set<Double> decoWarnings = new HashSet<Double>();
		decoWarnings.add(1d);
		decoWarnings.add(2d);
		decoWarnings.add(20d);
		decoWarnings.add(30d);
		decoWarnings.add(-30d);
		try {
			dp.setDecoCeilingWarnings(decoWarnings);
			fail("Not supposed to arrive here");
		} catch (InvalidParameterException e) {
			decoWarnings.remove(-30d);
		}

		decoWarnings.add(30d);
		try {
			dp.setDecoCeilingWarnings(decoWarnings);
		} catch (InvalidParameterException e) {
			fail("Not supposed to arrive here");
		}

		Set<Double> warnings = dp.getDecoCeilingWarnings();
		assertEquals(decoWarnings.size(), warnings.size());
		assertFalse(warnings.contains(-2d));
		assertTrue(warnings.contains(2d));
		assertTrue(warnings.contains(30d));
		assertFalse(warnings.contains(50d));
		assertFalse(warnings.contains(-10d));
		assertFalse(warnings.contains(10d));
		assertFalse(warnings.contains(0d));
	}

	public void testGetAscentWarnings() {
		DiveProfile dp = new DiveProfile();
		assertTrue(dp.getAscentWarnings().size() == 0);

		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		depthEntries.put(1d, -1d);
		depthEntries.put(2d, -2d);
		dp.setDepthEntries(depthEntries);

		Set<Double> ascentWarnings = new HashSet<Double>();
		ascentWarnings.add(1d);
		ascentWarnings.add(2d);
		ascentWarnings.add(20d);
		ascentWarnings.add(-30d);
		try {
			dp.setAscentWarnings(ascentWarnings);
			fail("Not supposed to arrive here");
		} catch (InvalidParameterException e) {
			ascentWarnings.remove(-30d);
		}

		ascentWarnings.add(30d);
		try {
			dp.setAscentWarnings(ascentWarnings);
		} catch (InvalidParameterException e) {
			fail("Not supposed to arrive here");
		}

		assertTrue(dp.getAscentWarnings().size() == ascentWarnings.size());
	}

	public void testSetAscentWarnings() {
		DiveProfile dp = new DiveProfile();

		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		depthEntries.put(1d, -1d);
		depthEntries.put(2d, -2d);
		dp.setDepthEntries(depthEntries);

		Set<Double> ascentWarnings = new HashSet<Double>();
		ascentWarnings.add(1d);
		ascentWarnings.add(2d);
		ascentWarnings.add(20d);
		ascentWarnings.add(30d);
		dp.setAscentWarnings(ascentWarnings);

		Set<Double> warns = dp.getAscentWarnings();
		assertEquals(ascentWarnings.size(), warns.size());
		assertTrue(warns.contains(2d));
		assertTrue(warns.contains(30d));
		assertFalse(warns.contains(50d));
		assertFalse(warns.contains(10d));
	}

	public void testGetDecoCeilingWarnings() {
		DiveProfile dp = new DiveProfile();
		assertTrue(dp.getAscentWarnings().size() == 0);

		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		depthEntries.put(1d, -1d);
		depthEntries.put(2d, -2d);
		dp.setDepthEntries(depthEntries);

		Set<Double> warnings = new HashSet<Double>();
		warnings.add(1d);
		warnings.add(2d);
		warnings.add(20d);
		warnings.add(30d);
		dp.setDecoCeilingWarnings(warnings);

		assertTrue(dp.getDecoCeilingWarnings().size() == warnings.size());
	}

	public void testGetLastTimeEntry() {
		DiveProfile dp = new DiveProfile();

		assertTrue(dp.getLastTimeEntry() == 0);
		assertFalse(dp.getLastTimeEntry() == 20);

		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		depthEntries.put(1d, -1d);
		depthEntries.put(2d, -2d);
		dp.setDepthEntries(depthEntries);

		assertFalse(dp.getLastTimeEntry() == 20);
		assertTrue(dp.getLastTimeEntry() == 2);
	}

	public void testSetRemainingBottomTimeWarnings() {
		DiveProfile dp = new DiveProfile();

		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		depthEntries.put(1d, -1d);
		depthEntries.put(2d, -2d);
		dp.setDepthEntries(depthEntries);

		Set<Double> bottomWarnings = new HashSet<Double>();
		bottomWarnings.add(1d);
		bottomWarnings.add(2d);
		bottomWarnings.add(20d);
		bottomWarnings.add(30d);
		dp.setRemainingBottomTimeWarnings(bottomWarnings);

		Set<Double> warnings = dp.getRemainingBottomTimeWarnings();
		assertEquals(bottomWarnings.size(), warnings.size());
		assertTrue(warnings.contains(2d));
		assertTrue(warnings.contains(30d));
		assertFalse(warnings.contains(50d));
		assertFalse(warnings.contains(10d));
	}

	public void testSetDecoEntries() {
		DiveProfile dp = new DiveProfile();

		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		depthEntries.put(1d, -1d);
		depthEntries.put(2d, -2d);
		dp.setDepthEntries(depthEntries);

		Set<Double> decoEntries = new HashSet<Double>();
		decoEntries.add(1d);
		decoEntries.add(2d);
		decoEntries.add(20d);
		decoEntries.add(30d);
		dp.setDecoEntries(decoEntries);

		Set<Double> warnings = dp.getDecoEntries();
		assertEquals(decoEntries.size(), warnings.size());
		assertTrue(warnings.contains(2d));
		assertTrue(warnings.contains(30d));
		assertFalse(warnings.contains(50d));
		assertFalse(warnings.contains(10d));
	}

	public void testGetRemainingBottomTimeWarnings() {
		DiveProfile dp = new DiveProfile();
		assertTrue(dp.getAscentWarnings().size() == 0);

		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		depthEntries.put(1d, 1d);
		depthEntries.put(2d, 2d);
		dp.setDepthEntries(depthEntries);

		Set<Double> warnings = new HashSet<Double>();
		warnings.add(1d);
		warnings.add(2d);
		warnings.add(20d);
		warnings.add(30d);
		dp.setRemainingBottomTimeWarnings(warnings);

		assertTrue(dp.getRemainingBottomTimeWarnings().size() == warnings
				.size());
	}

	public void testGetDecoEntries() {
		DiveProfile dp = new DiveProfile();
		assertTrue(dp.getDecoEntries().size() == 0);

		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		depthEntries.put(1d, -1d);
		depthEntries.put(2d, -2d);
		dp.setDepthEntries(depthEntries);

		Set<Double> warnings = new HashSet<Double>();
		warnings.add(1d);
		warnings.add(2d);
		warnings.add(20d);
		warnings.add(30d);
		dp.setDecoEntries(warnings);

		assertTrue(dp.getDecoEntries().size() == warnings.size());
	}

	public void testSetMaximumTime() {
		DiveProfile dp = new DiveProfile();
		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		depthEntries.put(10d, -1d);
		depthEntries.put(50d, -1d);
		depthEntries.put(100d, -1d);
		dp.setDepthEntries(depthEntries);

		assertTrue(100 == dp.getLastTimeEntry());
		assertFalse(50 == dp.getLastTimeEntry());
		assertFalse(1 == dp.getLastTimeEntry());
		dp.limitMaximumTime(50);
		assertFalse(100 == dp.getLastTimeEntry());
		assertTrue(50 == dp.getLastTimeEntry());
		assertFalse(1 == dp.getLastTimeEntry());

		dp.limitMaximumTime(100);
		assertFalse(100 == dp.getLastTimeEntry());
		assertTrue(50 == dp.getLastTimeEntry());
		assertFalse(1 == dp.getLastTimeEntry());

		dp.limitMaximumTime(49);
		assertFalse(100 == dp.getLastTimeEntry());
		assertFalse(50 == dp.getLastTimeEntry());
		assertFalse(1 == dp.getLastTimeEntry());
	}

	public void testGetMaxDepth() {
		DiveProfile dp = new DiveProfile();
		assertTrue(dp.getMaxDepth() == 0);

		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		depthEntries.put(1d, -1d);
		depthEntries.put(2d, -2d);
		dp.setDepthEntries(depthEntries);

		assertTrue(dp.getMaxDepth() == -2);
	}

	public void testSetMaximumDepth() {
		DiveProfile dp = new DiveProfile();
		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		depthEntries.put(1d, -1d);
		depthEntries.put(50d, -50d);
		depthEntries.put(100d, -100d);
		depthEntries.put(200d, -200d);
		dp.setDepthEntries(depthEntries);

		assertFalse(-500 >= dp.getMaxDepth());
		assertTrue(-200 >= dp.getMaxDepth());
		assertTrue(-100 >= dp.getMaxDepth());
		assertTrue(4 == dp.getDepthEntries().size());
		
		dp.setMaximumDepth(-150);
		assertFalse(-200 >= dp.getMaxDepth());
		assertTrue(-150 >= dp.getMaxDepth());
		assertFalse(-100 < dp.getMaxDepth());
		assertTrue(4 == dp.getDepthEntries().size());
		
		dp.setMaximumDepth(-100);
		assertFalse(-200 >= dp.getMaxDepth());
		assertFalse(-150 >= dp.getMaxDepth());
		assertTrue(-100 >= dp.getMaxDepth());
		assertTrue(4 == dp.getDepthEntries().size());
	}

}
