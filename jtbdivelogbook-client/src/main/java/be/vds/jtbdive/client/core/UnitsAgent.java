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
package be.vds.jtbdive.client.core;

import java.util.Observable;
import java.util.Observer;

import be.vds.jtbdive.core.core.units.LengthUnit;
import be.vds.jtbdive.core.core.units.PressureUnit;
import be.vds.jtbdive.core.core.units.TemperatureUnit;
import be.vds.jtbdive.core.core.units.VolumeUnit;
import be.vds.jtbdive.core.core.units.WeightUnit;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.core.utils.UnitsUtilities;

public class UnitsAgent extends Observable implements Observer {

	private static final Syslog LOGGER = Syslog.getLogger(UnitsAgent.class);
	private static UnitsAgent instance;

	private TemperatureUnit temperatureUnit = TemperatureUnit.CELSIUS;
	private LengthUnit lengthUnit = LengthUnit.METER;
	private WeightUnit weightUnit = WeightUnit.KILOGRAM;
	private PressureUnit pressureUnit = PressureUnit.BAR;
	public static final String UNITS_CHANGED = "units.changed";

	private UnitsAgent() {
		UserPreferences.getInstance().addObserver(this);
	}

	public static UnitsAgent getInstance() {
		if (null == instance) {
			instance = new UnitsAgent();
		}
		return instance;
	}

	// //////////////////////// TEMPERATURE /////////////////////////////
	public void setTemperatureUnit(TemperatureUnit temperatureUnit) {
		this.temperatureUnit = temperatureUnit;
		LOGGER.info("temperature unit set to " + this.temperatureUnit.getKey());
	}

	public TemperatureUnit getTemperatureUnit() {
		return temperatureUnit;
	}

	public double convertTemperatureToModel(double temperature) {
		switch (temperatureUnit) {
		case CELSIUS:
			return temperature;
		case KELVIN:
			return UnitsUtilities.convertKelvinToCelsius(temperature);
		case FAHRENHEIT:
			return UnitsUtilities.convertFharenheitToCelsius(temperature);
		}
		return temperature;
	}

	public double convertTemperatureFromModel(double temperature,
			TemperatureUnit temperatureUnit) {
		switch (temperatureUnit) {
		case CELSIUS:
			return temperature;
		case KELVIN:
			return UnitsUtilities.convertCelsiusToKelvin(temperature);
		case FAHRENHEIT:
			return UnitsUtilities.convertCelsiusToFharenheit(temperature);

		}
		return temperature;
	}

	public double convertTemperatureFromModel(double temperature) {
		return convertTemperatureFromModel(temperature, temperatureUnit);
	}

	// //////////////////////// LENGTH /////////////////////////////
	public void setLengthUnit(LengthUnit lengthUnit) {
		this.lengthUnit = lengthUnit;
		LOGGER.info("length unit set to " + this.lengthUnit.getKey());
	}

	public double convertLengthToModel(double length) {
		switch (lengthUnit) {
		case METER:
			return length;
		case CENTIMETER:
			return UnitsUtilities.convertCentimeterToMeter(length);
		case INCH:
			return UnitsUtilities.convertInchToMeter(length);
		case FEET:
			return UnitsUtilities.convertFeetToMeter(length);
		}
		return length;
	}

	public double convertLengthFromModel(double length, LengthUnit lengthUnit) {
		switch (lengthUnit) {
		case METER:
			return length;
		case CENTIMETER:
			return UnitsUtilities.convertMeterToCentimeter(length);
		case INCH:
			return UnitsUtilities.convertMeterToInch(length);
		case FEET:
			return UnitsUtilities.convertMeterToFeet(length);
		}
		return length;
	}

	public double convertLengthFromModel(double length) {
		return convertLengthFromModel(length, lengthUnit);
	}

	public LengthUnit getLengthUnit() {
		return lengthUnit;
	}

	// //////////////////////// WEIGHT /////////////////////////////
	public WeightUnit getWeightUnit() {
		return weightUnit;
	}

	public double convertWeightToModel(double weight) {
		switch (weightUnit) {
		case KILOGRAM:
			return weight;
		}
		return weight;
	}

	public double convertWeightFromModel(double weight, WeightUnit weightUnit) {
		switch (weightUnit) {
		case KILOGRAM:
			return weight;
		}
		return weight;
	}

	// //////////////////////// PRESSURE /////////////////////////////
	/**
	 * Sets the pressure units that will be used by the Unit Agent to interact
	 * with the model. The model only uses BAR as pressure unit.
	 * 
	 * @param pressureUnit
	 *            The unit to be used by the agent
	 */
	public void setPressureUnit(PressureUnit pressureUnit) {
		this.pressureUnit = pressureUnit;
		LOGGER.info("pressure unit set to " + this.pressureUnit.getKey());
	}

	public PressureUnit getPressureUnit() {
		return pressureUnit;
	}

	/**
	 * Converts a pressure into the reference unit for the model. The pressure
	 * given is assumed to be expressed in the unit used by the agent.
	 * 
	 * @param pressure
	 * @return
	 */
	public double convertPressureToModel(double pressure) {
		switch (pressureUnit) {
		case BAR:
			return pressure;
		case MILLIBAR:
			return UnitsUtilities.convertMillibarToBar(pressure);
		case PASCAL:
			return UnitsUtilities.convertPascalToBar(pressure);
		case PSI:
			return UnitsUtilities.convertPsiToBar(pressure);
		}
		return pressure;
	}

	/**
	 * Converts a pressure from a unit used in the model into the unit used by
	 * the agent
	 * 
	 * @param pressure
	 * @return
	 */
	public double convertPressureFromModel(double pressure) {
		return convertPressureFromModel(pressure, pressureUnit);
	}

	/**
	 * Volume is supposed to be in Liter
	 * 
	 * @param volume
	 * @return
	 */
	public double convertVolumeFromModel(double volume, VolumeUnit volumeUnit) {
		switch (volumeUnit) {
		case LITER:
			return volume;
		case CUBIC_METER:
			return UnitsUtilities.convertLiterToCubicMeter(volume);
		}
		return volume;
	}

	/**
	 * 
	 * @param volume
	 *            the value to be converted
	 * @param volumeUnit
	 *            the unit from which we want to convert the value
	 * @return a value converted from the specified volumeUnit into LITER
	 */
	public double convertVolumeToModel(double volume, VolumeUnit volumeUnit) {
		switch (volumeUnit) {
		case LITER:
			return volume;
		case CUBIC_METER:
			return UnitsUtilities.convertCubicMeterToLiter(volume);
		}
		return volume;
	}

	/**
	 * Converts a pressure from a unit used in the model into a given unit.
	 * 
	 * @param pressure
	 * @param pressureUnit
	 * @return
	 */
	public double convertPressureFromModel(double pressure,
			PressureUnit pressureUnit) {
		switch (pressureUnit) {
		case BAR:
			return pressure;
		case MILLIBAR:
			return UnitsUtilities.convertBarToMillibar(pressure);
		case PASCAL:
			return UnitsUtilities.convertBarToPascal(pressure);
		case PSI:
			return UnitsUtilities.convertBarToPsi(pressure);
		}
		return pressure;
	}

	// ////////////////// OTHER /////////////////
	@Override
	public void update(Observable o, Object arg) {
		if (o.equals(UserPreferences.getInstance())) {
			if (arg.equals(UserPreferences.PREFERENCES_CHANGED)) {
				updatePreferences();
			}
		}
	}

	private void updatePreferences() {
		setTemperatureUnit(UserPreferences.getInstance()
				.getPreferredTemperatureUnit());
		setLengthUnit(UserPreferences.getInstance().getPreferredLengthUnit());
		setPressureUnit(UserPreferences.getInstance()
				.getPreferredPressureUnit());
		setChanged();
		notifyObservers(UNITS_CHANGED);
	}

}
