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
package be.vds.jtbdive.core.core.material;

import java.util.List;

import be.vds.jtbdive.core.core.GazMix;
import be.vds.jtbdive.core.core.catalogs.MaterialType;

/**
 * A Tank used during the dive. The tanks has a pressure at the beginning and a
 * pressure at the end. The volume of the dive is also stored. The gaz mix is a
 * also stored.
 * 
 * @author Gautier Vanderslyen
 * 
 */
public class DiveTankEquipment extends AbstractEquipment {

	private static final long serialVersionUID = -5193712683105570928L;
	private double beginPressure;
	private double endPressure;
	private GazMix gazMix;
	/**
	 * The switch time is the time at which the dive tank is being used. The
	 * switch time is expressed in seconds. The switch time must be smaller than
	 * the dive time, but no control is done on it.
	 */
	private double switchTime;

	public double getSwitchTime() {
		return switchTime;
	}

	public void setSwitchTime(double switchTime) {
		this.switchTime = switchTime;
	}

	/**
	 * Gets the pressure at the beginning. The pressure is expressed in Bar
	 * 
	 * @return
	 */
	public double getBeginPressure() {
		return beginPressure;
	}

	/**
	 * Sets the pressure at the beginning. The pressure is expressed in Bar
	 */
	public void setBeginPressure(double beginPressure) {
		this.beginPressure = beginPressure;
	}

	/**
	 * Gets the pressure at the end. The pressure is expressed in Bar
	 * 
	 * @return
	 */
	public double getEndPressure() {
		return endPressure;
	}

	/**
	 * Sets the pressure at the end. The pressure is expressed in Bar
	 */
	public void setEndPressure(double endPressure) {
		this.endPressure = endPressure;
	}

	/**
	 * gets the whole gaz mix containing the different gazes
	 * 
	 * @return
	 */
	public GazMix getGazMix() {
		return gazMix;
	}

	/**
	 * Sets the gaz mix of the tank
	 * 
	 * @param gazMix
	 */
	public void setGazMix(GazMix gazMix) {
		this.gazMix = gazMix;
	}

	@Override
	public MaterialType getMaterialType() {
		return MaterialType.DIVE_TANK;
	}

}
