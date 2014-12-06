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
package be.vds.jtbdive.core.core;

import java.util.ArrayList;
import java.util.List;

import be.vds.jtbdive.core.logging.Syslog;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class ModifiedDiveMemory {

	private static final Syslog LOGGER = Syslog
			.getLogger(ModifiedDiveMemory.class);
	private List<Dive> diveChanges = new ArrayList<Dive>();

	public void diveChanged(Dive dive) {
		if (!hasDiveChanged(dive)) {
			diveChanges.add(dive);
			LOGGER.debug("dive has changed : " + dive.toString());
		}
	}

	public void removeDiveChanged(Dive dive) {
		diveChanges.remove(dive);
		LOGGER.debug("Changes canceled for dive : " + dive.toString());
	}

	public boolean hasDiveChanged(Dive dive) {
		return diveChanges.contains(dive);
	}

	public List<Dive> getChangedDives() {
		return diveChanges;
	}

	public void removeAllDiveChanged() {
		diveChanges.removeAll(diveChanges);
		LOGGER.debug("Changes canceled for all the dives");
	}
	
}
