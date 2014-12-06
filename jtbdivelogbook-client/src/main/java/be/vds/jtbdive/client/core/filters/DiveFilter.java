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

import be.vds.jtbdive.client.core.filters.operator.DiveFilterOperator;
import be.vds.jtbdive.core.core.Dive;

/**
 * A dive filter determines whether a dive is valid according to the criteria
 * and the operator.
 * 
 * @author Vanderslyen.G
 * 
 */
public abstract class DiveFilter {

	protected DiveFilterType diveFilterType;
	private DiveFilterOperator operator;
	private Object criteria;

	public DiveFilter(DiveFilterType diveFilterType) {
		this.diveFilterType = diveFilterType;
	}

	public Object getCriteria() {
		return criteria;
	}

	public void setCriteria(Object criteria) {
		this.criteria = criteria;
	}

	public DiveFilterOperator getOperator() {
		return operator;
	}

	public void setOperator(DiveFilterOperator operator) {
		this.operator = operator;
	}

	public abstract boolean isValid(Dive dive);

	public DiveFilterType getDiveFilterType() {
		return diveFilterType;
	}

}
