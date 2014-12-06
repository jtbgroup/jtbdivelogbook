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

public class FinsMaterial extends AbstractMaterial {
	private static final long serialVersionUID = -2277454609140951382L;
	private String size;
	private boolean poolFins;

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public boolean isPoolFins() {
		return poolFins;
	}

	public void setPoolFins(boolean poolFins) {
		this.poolFins = poolFins;
	}
	@Override
	public MaterialType getMaterialType() {
		return MaterialType.FINS;
	}
}
