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

import java.io.Serializable;

public class PhysiologicalStatus implements Serializable{
	
	private static final long serialVersionUID = -4946612187784175539L;
	private int arterialMicrobubbleLevel;
	private int cnsBeforeDive;
	private char saturationIndexBeforeDive;
	private char saturationIndexAfterDive;
	private double maxPPO2;
	private double skinCoolTemperature;
	private double interPulmonaryShunt;

	public char getSaturationIndexBeforeDive() {
		return saturationIndexBeforeDive;
	}

	public void setSaturationIndexBeforeDive(char index) {
		this.saturationIndexBeforeDive = index;
	}
	
	public char getSaturationIndexAfterDive() {
		return saturationIndexAfterDive;
	}

	public void setSaturationIndexAfterDive(char index) {
		this.saturationIndexAfterDive = index;
	}

	public int getArterialMicrobubbleLevel() {
		return arterialMicrobubbleLevel;
	}

	public void setArterialMicrobubbleLevel(int arterialMicrobubbleLevel) {
		this.arterialMicrobubbleLevel = arterialMicrobubbleLevel;
	}

	public double getSkinCoolTemperature() {
		return skinCoolTemperature;
	}

	public void setSkinCoolTemperature(double skinCoolTemperature) {
		this.skinCoolTemperature = skinCoolTemperature;
	}

	public double getMaxPPO2() {
		return maxPPO2;
	}

	public void setMaxPPO2(double maxPPO2) {
		this.maxPPO2 = maxPPO2;
	}

	public void setCnsBeforeDive(int cnsBeforeDive) {
		this.cnsBeforeDive = cnsBeforeDive;
	}

	public int getCnsBeforeDive() {
		return cnsBeforeDive;
	}

	public void setInterPulmonaryShunt(double interPulmonaryShunt) {
		this.interPulmonaryShunt = interPulmonaryShunt;
	}

	public double getInterPulmonaryShunt() {
		return interPulmonaryShunt;
	}

}
