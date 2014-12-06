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
package be.vds.jtbdive.client.core.filters.inspector;

import be.vds.jtbdive.core.core.Dive;

public class DiveTimeInspector implements DoubleFilterInspector {

	public Object getFilterParameter(Dive dive) {
		return new Double(dive.getDiveTime());
	}

	@Override
	public Double getMinimumValue() {
		return new Double(0);
	}

	@Override
	public Double getMaximumValue() {
		return new Double(86400);
	}

	@Override
	public Double getInitialValue() {
		return new Double(0);
	}

	@Override
	public Double getStepValue() {
		return new Double(60);
	}

	public Double convertToModel(double d) {
		return d;
	}

	public Double convertFromModel(double d) {
		return d;
	}

	public String getUnitSymbol() {
		return "sec";
	}

}