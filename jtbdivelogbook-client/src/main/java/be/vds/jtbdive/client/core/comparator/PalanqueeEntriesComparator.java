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
package be.vds.jtbdive.client.core.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import be.vds.jtbdive.core.core.PalanqueeEntry;
import be.vds.jtbdive.core.core.catalogs.DiverRole;

public class PalanqueeEntriesComparator implements Comparator<PalanqueeEntry> {

	@Override
	public int compare(PalanqueeEntry o1, PalanqueeEntry o2) {
		List<Integer> dr1 = new ArrayList<Integer>();
		for (DiverRole role : o1.getRoles()) {
			dr1.add(role.getOrder());
		}
		List<Integer> dr2 = new ArrayList<Integer>();
		for (DiverRole role : o2.getRoles()) {
			dr2.add(role.getOrder());
		}

		if (dr1.size() == 0 && dr2.size() == 0) {
			return 0;
		} else if (dr1.size() == 0) {
			return 1;
		} else if (dr2.size() == 0)
			return -1;

		Collections.sort(dr1);
		Collections.sort(dr2);

		for (int i = 0; i < dr1.size(); i++) {
			Integer r1 = dr1.get(i);
			Integer r2 = dr2.get(i);
			if (r1 != r2) {
				return r1.compareTo(r2);
			}
		}

		return 0;
	}

}
