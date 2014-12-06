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
import be.vds.jtbdive.core.core.Rating;

public class DiveRatingInspector extends IntegerFilterInspector {

	public Object getFilterParameter(Dive dive) {
		Rating rating = dive.getRating();
		return rating == null ? new Integer(0) : new Integer(rating.getValue());
	}

	@Override
	public Integer getMinimumValue() {
		return 0;
	}

	@Override
	public Integer getMaximumValue() {
		return 10;
	}

	@Override
	public Integer getInitialValue() {
		return 0;
	}

	@Override
	public Integer getStepValue() {
		return 1;
	}

}