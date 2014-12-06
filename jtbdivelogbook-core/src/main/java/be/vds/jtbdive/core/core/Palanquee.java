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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import be.vds.jtbdive.core.core.catalogs.DiverRole;

/**
 * A group of divers that dive together. Each diver has a role in the palanquee.
 * This Role is descibed int the {@link PalanqueeEntry}
 * 
 * @author Gautier Vanderslyen
 * 
 */
public class Palanquee implements Serializable {

	private static final long serialVersionUID = -922453509479936836L;
	private Set<PalanqueeEntry> palanqueeEntries = new HashSet<PalanqueeEntry>();

	public Set<PalanqueeEntry> getPalanqueeEntries() {
		return palanqueeEntries;
	}

	/**
	 * Adds a {@link PalanqueeEntry} in the palanquee
	 * 
	 * @param palanqueeEntry
	 */
	public void addPalanqueeEntry(PalanqueeEntry palanqueeEntry) {
		palanqueeEntries.add(palanqueeEntry);
	}

	/**
	 * Returns the size of the palanquee.
	 * 
	 * @return the number of diver in the palanquee
	 */
	public int size() {
		return palanqueeEntries.size();
	}

	public void removePalanqueeEntry(PalanqueeEntry palanqueeEntry) {
		palanqueeEntries.remove(palanqueeEntry);
	}

	/**
	 * Tells if a specific diver is present in the palanquee.
	 * 
	 * @param diver
	 * @return
	 */
	public boolean isDiverPresent(Diver diver) {
		for (PalanqueeEntry palanqueeEntry : palanqueeEntries) {
			if (palanqueeEntry.getDiver().equals(diver)) {
				return true;
			}
		}
		return false;
	}

	public void setPalanqueeEntries(Set<PalanqueeEntry> palanqueeEntries) {
		this.palanqueeEntries = palanqueeEntries;
	}

	public boolean isDiverPresentForRole(Diver diver, DiverRole role) {
		for (PalanqueeEntry palanqueeEntry : palanqueeEntries) {
			if (palanqueeEntry.getDiver().equals(diver)) {
				return palanqueeEntry.getRoles().contains(role);
			}
		}
		return false;
	}

}
