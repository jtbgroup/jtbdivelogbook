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

import java.util.Date;

import be.vds.jtbdive.client.core.filters.operator.DateOperator;
import be.vds.jtbdive.core.core.Dive;

public class DateDiveFilter extends DiveFilter {

	public DateDiveFilter(DiveFilterType filterType) {
		super(filterType);
		setOperator(DateOperator.EQUALS);
	}

	@Override
	public boolean isValid(Dive dive) {
		DateOperator op = (DateOperator) getOperator();

		switch (op) {
		case EQUALS:
			return checkEquals(dive);
		case BEFORE:
			return checkBefore(dive);
		case BEFORE_OR_EQUALS:
			return checkBeforeOrEquals(dive);
		case AFTER:
			return checkAfter(dive);
		case AFTER_OR_EQUALS:
			return checkAfterOrEquals(dive);

		}

		return false;
	}

	public Date getDiveCriteria(Dive dive) {
		return (Date) diveFilterType.getInspector().getFilterParameter(dive);
	}

	private boolean checkBefore(Dive dive) {
		return (getDiveCriteria(dive).getTime() < getFirstCriteria().getTime());
	}

	private boolean checkBeforeOrEquals(Dive dive) {
		return (getDiveCriteria(dive).getTime() <= getFirstCriteria().getTime());
	}

	private boolean checkAfter(Dive dive) {
		return (getDiveCriteria(dive).getTime() > getFirstCriteria().getTime());
	}

	private boolean checkAfterOrEquals(Dive dive) {
		return (getDiveCriteria(dive).getTime() >= getFirstCriteria().getTime());
	}

	private boolean checkEquals(Dive dive) {
		return (getDiveCriteria(dive).getTime() == getFirstCriteria().getTime());
	}

	public Date getFirstCriteria() {
		Object o = getCriteria();
		if (o == null)
			return null;

		return (Date) o;

	}

	public void setCriteriaValue(Object values) {
		setCriteria(values);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getDiveFilterType().getKey());
		DateOperator op = (DateOperator) getOperator();
		boolean doubleCriteria = false;

		switch (op) {
		case EQUALS:
			sb.append(" = ");
			break;
		case AFTER_OR_EQUALS:
			sb.append(" >= ");
			break;
		case AFTER:
			sb.append(" > ");
			break;
		case BEFORE:
			sb.append(" < ");
			break;
		case BEFORE_OR_EQUALS:
			sb.append(" <= ");
			break;
		}

		if (!doubleCriteria) {
			sb.append(getFirstCriteria());
		}

		return sb.toString();
	}
}
