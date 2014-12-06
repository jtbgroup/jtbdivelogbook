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

import java.util.Date;
import java.util.List;

import be.vds.jtbdive.core.core.catalogs.DiveTankCompositeMaterial;
import be.vds.jtbdive.core.core.catalogs.MaterialType;

public class DiveTankMaterial extends AbstractMaterial {

	private static final long serialVersionUID = 43017403901517667L;
	/**
	 * In liter
	 */
	private double volume;
	private DiveTankCompositeMaterial composite = DiveTankCompositeMaterial.STEEL;
	/**
	 * In bar
	 */
	private double maxPressure;
	private List<Date> opticalControls;
	private List<Date> hydrolicControls;

	public List<Date> getOpticalControls() {
		return opticalControls;
	}

	public void setOpticalControls(List<Date> opticalControls) {
		this.opticalControls = opticalControls;
	}

	public List<Date> getHydrolicControls() {
		return hydrolicControls;
	}

	public void setHydrolicControls(List<Date> hydrolicControls) {
		this.hydrolicControls = hydrolicControls;
	}

	public double getMaxPressure() {
		return maxPressure;
	}

	public void setMaxPressure(double maxPressure) {
		this.maxPressure = maxPressure;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public DiveTankCompositeMaterial getComposite() {
		return composite;
	}

	public void setComposite(DiveTankCompositeMaterial composite) {
		this.composite = composite;
	}

	@Override
	public MaterialType getMaterialType() {
		return MaterialType.DIVE_TANK;
	}
}
