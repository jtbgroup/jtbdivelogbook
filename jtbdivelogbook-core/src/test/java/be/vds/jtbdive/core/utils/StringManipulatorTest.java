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
package be.vds.jtbdive.core.utils;

import be.vds.jtbdive.core.utils.StringManipulator;
import junit.framework.TestCase;

/**
 * 
 * @author vanderslyen.g
 */
public class StringManipulatorTest extends TestCase {

	public void testFormatNumberWithFixedSize() {
		assertEquals("12", StringManipulator.formatNumberWithFixedSize(12, 2));
		assertEquals("012", StringManipulator.formatNumberWithFixedSize(12, 3));
		assertEquals("1234",
				StringManipulator.formatNumberWithFixedSize(1234, 3));
	}

	public void testFormatFixedDecimalNumber1() {
		assertEquals("test 1", "12.00",
				StringManipulator.formatFixedDecimalNumber(12, 2));
		assertEquals("test 2", "12.00",
				StringManipulator.formatFixedDecimalNumber(12.0003, 2));
		assertEquals("test 3", "12.10",
				StringManipulator.formatFixedDecimalNumber(12.104, 2));
		assertEquals("test 4", "12.1",
				StringManipulator.formatFixedDecimalNumber(12.123, 1));
		assertEquals("test 5", "12.1200",
				StringManipulator.formatFixedDecimalNumber(12.12, 4));
	}

	public void testFormatFixedDecimalNumber2() {
		assertEquals("22,000000000",
				StringManipulator.formatFixedDecimalNumber(22, (short) 9, ','));
		assertEquals("22", StringManipulator.formatFixedDecimalNumber(22,
				(short) 9, ',', false));
		assertEquals("22,000", StringManipulator.formatFixedDecimalNumber(22,
				(short) 3, ',', true));
		assertEquals("232,123456000",
				StringManipulator.formatFixedDecimalNumber(232.123456,
						(short) 9, ','));
		assertEquals("22,12", StringManipulator.formatFixedDecimalNumber(
				22.123456, (short) 2, ','));
		assertEquals("-22,12", StringManipulator.formatFixedDecimalNumber(
				-22.123456, (short) 2, ','));
		assertEquals("-22.1234", StringManipulator.formatFixedDecimalNumber(
				-22.123456, (short) 4, '.'));
		assertEquals("-229 876.1234",
				StringManipulator.formatFixedDecimalNumber(-229876.123456,
						(short) 4, '.', " ", false));
		assertEquals("-229 876.12", StringManipulator.formatFixedDecimalNumber(
				-229876.12, (short) 4, '.', " ", false));

	}

	public void testRemoveDiacriticalMarks() {
		assertEquals("hello world",
				StringManipulator.removeDiacriticalMarks("hello world"));
		assertEquals("Hello World",
				StringManipulator.removeDiacriticalMarks("Hello World"));
		assertEquals("HELLO WORLD",
				StringManipulator.removeDiacriticalMarks("HELLO WORLD"));
		assertEquals("Hello World!",
				StringManipulator.removeDiacriticalMarks("Hello World!"));
		assertEquals("Hello", StringManipulator.removeDiacriticalMarks("Héllo"));
		assertEquals("Hello", StringManipulator.removeDiacriticalMarks("Hèllo"));
		assertEquals("Hello", StringManipulator.removeDiacriticalMarks("Hêllo"));
		assertEquals("Hello", StringManipulator.removeDiacriticalMarks("Hëllo"));
		assertEquals("Hallo", StringManipulator.removeDiacriticalMarks("Hàllo"));
		assertEquals("Hallo", StringManipulator.removeDiacriticalMarks("Hâllo"));
		assertEquals("Hallo", StringManipulator.removeDiacriticalMarks("Hällo"));
		assertEquals("coke", StringManipulator.removeDiacriticalMarks("çoke"));

	}
}
