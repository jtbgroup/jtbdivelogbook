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

import java.util.ArrayList;
import java.util.List;

import be.vds.jtbdive.client.core.filters.operator.AggregatorOperator;
import be.vds.jtbdive.core.core.Dive;

public class AggregatorDiveFilter extends DiveFilter {

	public AggregatorDiveFilter(DiveFilterType diveFilterType) {
		super(diveFilterType);
	}

	public void setFilters(List<DiveFilter> filters) {
		setCriteria(filters);
	}

	@Override
	public boolean isValid(Dive dive) {

		if (AggregatorOperator.OR.equals(getOperator())) {
			return isValidoperatorOr(dive);
		}

		return isValidoperatorAnd(dive);
	}

	private boolean isValidoperatorAnd(Dive dive) {
		List<DiveFilter> filters = getFilters();
		for (DiveFilter df : filters) {
			if (!df.isValid(dive))
				return false;
		}

		return true;
	}

	private boolean isValidoperatorOr(Dive dive) {
		List<DiveFilter> filters = getFilters();
		for (DiveFilter df : filters) {
			if (df.isValid(dive))
				return true;
		}

		return false;
	}

	@Override
	public DiveFilterType getDiveFilterType() {
		return DiveFilterType.AGGREGATOR;
	}

	@SuppressWarnings("unchecked")
	public List<DiveFilter> getFilters() {
		return (List<DiveFilter>) getCriteria();
	}

	public void addFilter(DiveFilter filter) {
		List<DiveFilter> filters = getFilters();
		if (filters == null) {
			filters = new ArrayList<DiveFilter>();
			setFilters(filters);
		}
		filters.add(filter);
	}

	public void removeDiveFilter(DiveFilter diveFilter) {
		List<DiveFilter> filters = getFilters();
		if (null != filters)
			filters.remove(diveFilter);
	}

	@Override
	public String toString() {
		return "Aggregator "
				+ (getOperator() != null ? getOperator().getKey() : "");
	}
}
