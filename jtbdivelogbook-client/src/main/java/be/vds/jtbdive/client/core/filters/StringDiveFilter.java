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

import be.vds.jtbdive.client.core.filters.operator.StringOperator;
import be.vds.jtbdive.core.core.Dive;

public class StringDiveFilter extends DiveFilter {


	public StringDiveFilter(DiveFilterType diveFilterType) {
		super(diveFilterType);
		setOperator(StringOperator.CONTAINS);
	}
	
	public void setTextCriteria(String textCriteria) {
		setCriteria(textCriteria);
	}

	@Override
	public boolean isValid(Dive dive) {
		String textCriteria = getTextCriteria();
//		String c = dive.getComment();
		String c = (String) (getDiveFilterType().getInspector().getFilterParameter(dive));
		if (textCriteria == null || textCriteria.length() == 0)
			return (c == null || c.length() == 0);

		if (c == null) {
			return false;
		}

		StringOperator op = (StringOperator) getOperator();
		switch (op) {
		case CONTAINS:
			return c.contains(textCriteria);
		case EQUALS:
			return c.equals(textCriteria);
		}

		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Comment ");
		StringOperator op = (StringOperator) getOperator();

		switch (op) {
		case CONTAINS:
			sb.append("contains ");
			break;
		case EQUALS:
			sb.append("equals ");
			break;
		}

		sb.append(getTextCriteria());

		return sb.toString();
	}

	public String getTextCriteria() {
		return (String) getCriteria();
	}


}
