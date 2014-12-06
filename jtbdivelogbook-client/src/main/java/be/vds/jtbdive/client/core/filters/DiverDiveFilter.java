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

import be.vds.jtbdive.client.core.filters.operator.DiverOperator;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.Palanquee;
import be.vds.jtbdive.core.core.catalogs.DiverRole;

public class DiverDiveFilter extends DiveFilter {

	private DiverRole role;

	public DiverDiveFilter(DiveFilterType diveFilterType) {
		super(diveFilterType);
		setOperator(DiverOperator.CONTAINS);
	}

	public void setDiverCriteria(Diver diver) {
		setCriteria(diver);
	}

	@Override
	public boolean isValid(Dive dive) {
		Diver diver = getDiverCriteria();
		if (diver == null)
			return false;

		Palanquee p = dive.getPalanquee();
		if (p == null || p.getPalanqueeEntries().size() == 0)
			return false;

		DiverOperator op = (DiverOperator) getOperator();
		switch (op) {
		case CONTAINS:
			if (role == null) {
				return p.isDiverPresent(diver);
			} else {
				return p.isDiverPresentForRole(diver, role);
			}
		}

		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Buddies ");
		DiverOperator op = (DiverOperator) getOperator();

		switch (op) {
		case CONTAINS:
			sb.append("contains ");
			break;
		}

		Diver d = getDiverCriteria();
		sb.append(d == null ? "" : d.getFullName());

		sb.append(role == null ? "" : "(" + role + ")");

		return sb.toString();
	}

	public Diver getDiverCriteria() {
		return (Diver) getCriteria();
	}

	public void setRole(DiverRole role) {
		this.role = role;
	}

	public DiverRole getRole() {
		return role;
	}

}
