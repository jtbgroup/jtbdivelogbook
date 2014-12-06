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
package be.vds.jtbdive.core.core.comparator;

import java.util.Comparator;

import be.vds.jtbdive.core.core.Diver;

public class DiverComparator implements Comparator<Diver> {

	private static final int SORT_LAST_NAME = 0;
	private static final int SORT_FIRST_NAME = 1;
	private int sortType;

	public DiverComparator() {
		this(SORT_FIRST_NAME);
	}

	public DiverComparator(int sortType) {
		this.sortType = sortType;
	}

	@Override
	public int compare(Diver o1, Diver o2) {
		if (sortType == SORT_LAST_NAME) {
			return sortLastNameFirst(o1, o2);
		} else {
			return sortFirstNameFirst(o1, o2);
		}

	}

	private int sortFirstNameFirst(Diver o1, Diver o2) {
		if (o1.getFirstName() == null) {
			return -1;
		}

		if (o2.getFirstName() == null) {
			return 1;
		}

		int i = o1.getFirstName().compareTo(o2.getFirstName());
		if (i != 0) {
			return i;
		}

		if (o1.getLastName() == null) {
			return -1;
		}

		if (o2.getLastName() == null){
			return 1;
		}

		return o1.getLastName().compareTo(o2.getLastName());
	}

	private int sortLastNameFirst(Diver o1, Diver o2) {
		if (o1.getLastName() == null){
			return -1;
		}

		if (o2.getLastName() == null){
			return 1;
		}

		int i = o1.getLastName().compareTo(o2.getLastName());
		if (i != 0){
			return i;
		}

		if (o1.getFirstName() == null){
			return -1;
		}

		if (o2.getFirstName() == null){
			return 1;
		}

		return o1.getFirstName().compareTo(o2.getFirstName());
	}
}
