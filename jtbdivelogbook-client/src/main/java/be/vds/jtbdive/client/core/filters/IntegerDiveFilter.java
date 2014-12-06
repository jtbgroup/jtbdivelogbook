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

import be.vds.jtbdive.client.core.filters.operator.NumericOperator;
import be.vds.jtbdive.core.core.Dive;

public class IntegerDiveFilter extends DiveFilter {

	public IntegerDiveFilter(DiveFilterType filterType) {
		super(filterType);
		setOperator(NumericOperator.EQUALS);
	}

	@Override
	public boolean isValid(Dive dive) {
		NumericOperator op = (NumericOperator) getOperator();

		switch (op) {
		case EQUALS:
			return checkEquals(dive);
		case IS_HIGER_OR_EQUALS:
			return checkHigherOrEquals(dive);
		case IS_HIGHER:
			return checkHigher(dive);
		case IS_LOWER:
			return checkLower(dive);
		case IS_LOWER_OR_EQUALS:
			return checkLowerOrEquals(dive);

		}

		return false;
	}

	public int getDiveCriteria(Dive dive) {
		return (Integer) diveFilterType.getInspector().getFilterParameter(dive);
	}

	private boolean checkHigherOrEquals(Dive dive) {
		return (getDiveCriteria(dive) >= getFirstCriteria());
	}

	private boolean checkHigher(Dive dive) {
		return (getDiveCriteria(dive) > getFirstCriteria());
	}

	private boolean checkLower(Dive dive) {
		return (getDiveCriteria(dive) < getFirstCriteria());
	}

	private boolean checkLowerOrEquals(Dive dive) {
		return (getDiveCriteria(dive) <= getFirstCriteria());
	}

	private boolean checkEquals(Dive dive) {
		return (getDiveCriteria(dive) == getFirstCriteria());
	}

	public int getFirstCriteria() {
		Object o = getCriteria();
		if (o == null)
			return 0;

		return (Integer) o;

	}

	public void setCriteriaValue(Object values) {
		setCriteria(values);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getDiveFilterType().getKey());
		NumericOperator op = (NumericOperator) getOperator();
		boolean doubleCriteria = false;

		switch (op) {
		case EQUALS:
			sb.append(" = ");
			break;
		case IS_HIGER_OR_EQUALS:
			sb.append(" >= ");
			break;
		case IS_HIGHER:
			sb.append(" > ");
			break;
		case IS_LOWER:
			sb.append(" < ");
			break;
		case IS_LOWER_OR_EQUALS:
			sb.append(" <= ");
			break;
		}

		if (!doubleCriteria) {
			sb.append(getFirstCriteria());
		}

		return sb.toString();
	}
}
