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

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringManipulator {
	private StringManipulator() {
	}

	/**
	 * Formats a Long with a fixed size. The number is filled with preceding "0"
	 * if the number's length is greater than the desired size, otherwhise the
	 * full number is returned.
	 * 
	 * @param l
	 * @param size
	 * @return
	 */
	public static String formatNumberWithFixedSize(long l, int size) {
		if (String.valueOf(l).length() > size) {
			return String.valueOf(l);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(l);
		while (sb.length() < size) {
			sb.insert(0, "0");
		}
		return sb.toString();
	}

	/**
	 * Returns a String fomatted according to the parameters. Default decimal
	 * char is a '.'. No evaluation of the number is done, the string is just
	 * cut according to the desireed size.
	 * 
	 * @param number
	 *            the number to be rendered as String
	 * @param decimalSize
	 *            the size of the decimal (number of chars afer the separator)
	 * @return the formatted String
	 */
	public static String formatFixedDecimalNumber(double number, int decimalSize) {
		return formatFixedDecimalNumber(number, decimalSize, '.');
	}

	public static String formatFixedDecimalNumber(double number,
			int decimalSize, char decimalChar) {
		return formatFixedDecimalNumber(number, decimalSize, decimalChar, true);
	}

	public static String formatFixedDecimalNumber(double number,
			int decimalSize, char decimalChar, boolean fillWithZeros) {
		return formatFixedDecimalNumber(number, decimalSize, decimalChar, null,
				fillWithZeros);
	}

	/**
	 * Formats a double number using the desired parameters. The rules used to
	 * format are the following:
	 * <ul>
	 * <li>an entire number is filled with "0" only if the fillWithZero
	 * parameter is true.</li>
	 * <li>decimals that are cutted off are not rounded</li>
	 * <li>the only negative sign possible is "-"</li>
	 * </ul>
	 * 
	 * @param number
	 *            The number to be returned
	 * @param decimalSize
	 * @param decimalChar
	 * @param integerSeparator
	 * @param fillWithZeros
	 * @return
	 */
	public static String formatFixedDecimalNumber(double number,
			int decimalSize, char decimalChar, String integerSeparator,
			boolean fillWithZeros) {

		Pattern p = Pattern
				.compile("([-+]?)([0-9]*)\\.?([0-9]+([eE][-+]?[0-9]+)?)");
		String numberAsString = String.valueOf(number);
		Matcher m = p.matcher(numberAsString);
		String groupSign = null;
		String groupInteger = null;
		String groupDecimal = null;
		if (m.matches()) {
			groupSign = m.group(1);
			groupInteger = m.group(2);
			groupDecimal = m.group(3);
		}

		if (number % 1 == 0) {
			groupDecimal = "";
		}

		String decimalPart = "";
		String integerPart = groupInteger;

		if (groupDecimal.length() > decimalSize) {
			decimalPart = groupDecimal.substring(0, decimalSize);
		} else {
			decimalPart = groupDecimal;
			if (fillWithZeros) {
				for (int i = groupDecimal.length(); i < decimalSize; i++) {
					decimalPart += "0";
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		if (null == integerSeparator) {
			sb.append(integerPart);
		} else {
			int l = integerPart.length();

			for (int i = 0; i < l; i++) {
				sb.insert(0, integerPart.charAt(l - 1 - i));
				if (i > 0 && i < l - 1 && (i + 1) % 3 == 0) {
					sb.insert(0, integerSeparator);
				}
			}
		}

		sb.insert(0, groupSign);

		if (decimalPart.length() > 0) {
			sb.append(decimalChar);
			sb.append(decimalPart);
		}

		return sb.toString();

	}

	public static String formatSecondsInMinutes(double seconds,
			boolean twoDigits) {
		StringBuilder sb = new StringBuilder();
		int min = (int) (seconds / 60);
		int sec = (int) (seconds % 60);

		if (twoDigits && min < 10) {
			sb.append("0");
		}
		sb.append(min);

		sb.append(":");

		if (twoDigits && sec < 10) {
			sb.append("0");
		}
		sb.append(sec);

		return sb.toString();
	}

	public static String removeDiacriticalMarks(String text) {
		return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll(
				"\\p{InCombiningDiacriticalMarks}+", "");
	}

	public static String formatTimeInHour(double miliseconds) {
		long hrs = (long) (miliseconds / 3600000);
		long min = (long) ((miliseconds - hrs * 3600000) / 60000);
		long sec = (long) (miliseconds - (hrs * 3600000) - (min * 60000)) / 1000;

		String s = formatInMinTwoDigits(hrs) + ":" + formatInMinTwoDigits(min)
				+ ":" + formatInMinTwoDigits(sec);
		return s;
	}

	private static String formatInMinTwoDigits(long digit) {
		String s = String.valueOf(digit);
		if (s.length() < 2) {
			return "0" + s;
		}
		return s;
	}

	public static String formatCoordinates(double latitude, double longitude,
			int decimalSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(StringManipulator.formatFixedDecimalNumber(
				Math.abs(latitude), decimalSize, ','));
		sb.append(" ");
		sb.append(latitude > 0 ? "N" : "S");

		sb.append(" - ");

		sb.append(StringManipulator.formatFixedDecimalNumber(
				Math.abs(longitude), decimalSize, ','));
		sb.append(" ");
		sb.append(longitude > 0 ? "W" : "E");
		return sb.toString();
	}
}
