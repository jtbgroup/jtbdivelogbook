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

/**
 * 
 * @author vanderslyen.g
 */
public final class UnitsUtilities {

	private static final short MINUTES_IN_HOUR = 60;
	private static final double KELVIN_CELSIUS_DIFFERENCE = 273.15;
	private static final double INCH_METER_FACTOR = 39.3700787;
	private static final double METER_FEET_FACTOR = 3.28083989501;
	private static final double BAR_PSI_FACTOR = 14.5037738007;

	private UnitsUtilities() {
	}

	public static double convertMinutesToSeconds(double minutes) {
		return minutes * MINUTES_IN_HOUR;
	}

	/**
	 * f = ((9 * c) / 5) + 32
	 * 
	 * @param temperature
	 * @return the fahrenheit value of a celsius value
	 */
	public static double convertCelsiusToFharenheit(double temperature) {
		return ((9 * temperature) / 5) + 32;
	}

	/**
	 * f = ((9 * c) / 5) + 32
	 * 
	 * @param temperature
	 * @return the celsius value of a fahrenheit value
	 */
	public static double convertFharenheitToCelsius(double temperature) {
		return ((temperature - 32) * 5) / 9;
	}

	/**
	 * K = °C + 273,15
	 */
	public static double convertKelvinToCelsius(double temperature) {
		return temperature - KELVIN_CELSIUS_DIFFERENCE;
	}

	/**
	 * K = °C + 273,15
	 */
	public static double convertCelsiusToKelvin(double temperature) {
		return temperature + KELVIN_CELSIUS_DIFFERENCE;
	}

	public static double convertCentimeterToMeter(double length) {
		return length / 100;
	}

	public static double convertMeterToCentimeter(double length) {
		return length * 100;
	}

	public static double convertMeterToInch(double length) {
		return length * INCH_METER_FACTOR;
	}

	public static double convertInchToMeter(double length) {
		return length / INCH_METER_FACTOR;
	}

	/**
	 * 
	 * 1 meter = 3.28083989501 ft
	 */
	public static double convertMeterToFeet(double length) {
		return length * METER_FEET_FACTOR;
	}

	public static double convertFeetToMeter(double length) {
		return length / METER_FEET_FACTOR;
	}

	public static double convertMillibarToBar(double pressure) {
		return pressure / 1000;
	}

	public static double convertBarToMillibar(double pressure) {
		return pressure * 1000;
	}

	/**
	 * 1 bar = 100000 Pa
	 * 
	 * @param pressure
	 * @return
	 */
	public static double convertBarToPascal(double pressure) {
		return pressure * 100000;
	}

	public static double convertPascalToBar(double pressure) {
		return pressure / 100000;
	}

	/**
	 * 1 bar = 14.5037738007 psi
	 */
	public static double convertBarToPsi(double pressure) {
		return pressure * BAR_PSI_FACTOR;
	}

	public static double convertPsiToBar(double pressure) {
		return pressure / BAR_PSI_FACTOR;
	}

	public static double convertCubicMeterToLiter(double volume) {
		return volume * 1000d;
	}

	public static double convertLiterToCubicMeter(double volume) {
		return volume / 1000d;
	}

	public static double convertSecondsToMinutes(double seconds) {
		return seconds / 60;
	}

	public static double convertSecondsToMiliseconds(double seconds) {
		return seconds * 1000;
	}

	public static double convertMilisecondsToSeconds(double miliseconds) {
		return miliseconds / 1000;
	}

}
