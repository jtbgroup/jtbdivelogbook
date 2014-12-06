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
import be.vds.jtbdive.core.core.catalogs.SuitType;

public class SuitMaterial extends AbstractMaterial {
	private static final long serialVersionUID = 1660858869767787280L;

	private String size;
	private SuitType suitType;

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public SuitType getSuitType() {
		return suitType;
	}

	public void setSuitType(SuitType suitType) {
		this.suitType = suitType;
	}

	@Override
	public MaterialType getMaterialType() {
		return MaterialType.SUIT;
	}

}
