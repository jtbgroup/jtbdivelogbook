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

import be.vds.jtbdive.core.core.Dive;

public class DiveDiveSiteComparator implements Comparator<Dive> {

	public int compare(Dive d1, Dive d2) {
		if (d1.getDiveSite() == null && d2.getDiveSite() == null) {
			return 0;
		} else if (d1.getDiveSite() != null && d2.getDiveSite() == null) {
			return -1;
		} else if (d1.getDiveSite() == null && d2.getDiveSite() != null) {
			return 1;
		} else {
			return d1.getDiveSite().getName().compareTo(
					d2.getDiveSite().getName());
		}
	}

}
