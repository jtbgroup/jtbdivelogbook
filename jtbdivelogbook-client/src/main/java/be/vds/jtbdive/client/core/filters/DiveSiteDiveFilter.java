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
package be.vds.jtbdive.client.core.filters;

import be.vds.jtbdive.client.core.filters.operator.DiveSiteOperator;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveSite;

public class DiveSiteDiveFilter extends DiveFilter {

	public DiveSiteDiveFilter(DiveFilterType diveFilterType) {
		super(diveFilterType);
		setOperator(DiveSiteOperator.EQUALS);
	}

	public void setDiveSiteCriteria(DiveSite diveSite) {
		setCriteria(diveSite);
	}

	@Override
	public boolean isValid(Dive dive) {
		DiveSite diveSite = getDiveSiteCriteria();
		

		DiveSiteOperator op = (DiveSiteOperator) getOperator();
		switch (op) {
		case EQUALS:
			return checkEquals(diveSite, dive);
		}

		return false;
	}

	private boolean checkEquals(DiveSite diveSite, Dive dive) {
		if (diveSite == null && dive.getDiveSite() == null)
			return true;
		
		if(diveSite == null){
			return false;
		}
		
		return (diveSite.equals(dive.getDiveSite()));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Dive site ");
		DiveSiteOperator op = (DiveSiteOperator) getOperator();

		switch (op) {
		case EQUALS:
			sb.append("equals ");
			break;
		}

		DiveSite d = getDiveSiteCriteria();
		sb.append(d == null ? "" : d.getName());

		return sb.toString();
	}

	public DiveSite getDiveSiteCriteria() {
		return (DiveSite) getCriteria();
	}



}
