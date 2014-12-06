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
package be.vds.jtbdive.client.core;

import java.util.HashSet;
import java.util.Set;

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.logging.Syslog;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class DiveChangeInspector {

	private static final Syslog LOGGER = Syslog
			.getLogger(DiveChangeInspector.class);
	private static DiveChangeInspector instance;
	private Set<Dive> diveChanges = new HashSet<Dive>();
	private Set<DiveChangeListener> diveChangeListeners = new HashSet<DiveChangeListener>();

	private DiveChangeInspector() {
	}

	public  DiveChangeInspector getInstance() {
		if (instance == null) {
			instance = new DiveChangeInspector();
		}
		return instance;
	}

	public void addDiveChangeListener(DiveChangeListener diveChangeListener) {
		diveChangeListeners.add(diveChangeListener);
	}

	public void removeDiveChangeListener(DiveChangeListener diveChangeListener) {
		diveChangeListeners.remove(diveChangeListener);
	}

	public void notifyDiveChanged(Dive dive, boolean b) {
		for (DiveChangeListener diveChangeListener : diveChangeListeners) {
			diveChangeListener.diveChanged(dive, b);
		}
	}

	public void diveChanged(Dive dive) {
		if (!hasDiveChanged(dive)) {
			diveChanges.add(dive);
			LOGGER.debug("dive has changed : " + dive.toString());
			notifyDiveChanged(dive, true);
		}
	}

	public void removeDiveChanged(Dive dive) {
		diveChanges.remove(dive);
		LOGGER.debug("Changes canceled for dive : " + dive.toString());
		notifyDiveChanged(dive, false);
	}

	public boolean hasDiveChanged(Dive dive) {
		return diveChanges.contains(dive);
	}

	public Set<Dive> getChangedDives() {
		return diveChanges;
	}

	public void removeAllDiveChanged() {
		diveChanges.clear();
		LOGGER.debug("Changes canceled for all the dives");
		notifyClearChanged();
	}
	
	public void notifyClearChanged() {
		for (DiveChangeListener diveChangeListener : diveChangeListeners) {
			diveChangeListener.clearChangedDives();
		}
	}
}
