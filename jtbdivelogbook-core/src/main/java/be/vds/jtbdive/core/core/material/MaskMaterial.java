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

import be.vds.jtbdive.core.core.catalogs.MaterialType;

public class MaskMaterial extends AbstractMaterial {
	private static final long serialVersionUID = 5597127980833760755L;
	private String size;
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	private boolean glassDioptry;
	private double leftDioptry, rightDioptry;

	public boolean isGlassDioptry() {
		return glassDioptry;
	}

	public void setGlassDioptry(boolean glassDioptry) {
		this.glassDioptry = glassDioptry;
	}

	public double getLeftDioptry() {
		return leftDioptry;
	}

	public void setLeftDioptry(double leftDioptry) {
		this.leftDioptry = leftDioptry;
	}

	public double getRightDioptry() {
		return rightDioptry;
	}

	public void setRightDioptry(double rightDioptry) {
		this.rightDioptry = rightDioptry;
	}
	
	@Override
	public MaterialType getMaterialType() {
		return MaterialType.MASK;
	}
}
