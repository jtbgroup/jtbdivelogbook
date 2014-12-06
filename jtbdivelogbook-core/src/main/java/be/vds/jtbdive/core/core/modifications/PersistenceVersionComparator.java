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
package be.vds.jtbdive.core.core.modifications;

import java.util.Comparator;

import be.vds.jtbdive.core.core.Version;

public class PersistenceVersionComparator implements Comparator<PersistenceVersion> {
	@Override
	public int compare(PersistenceVersion p1, PersistenceVersion p2) {
		return Version.createVersion(p1.getVersionId()).compareTo(
				Version.createVersion(p2.getVersionId()));
	}

}
