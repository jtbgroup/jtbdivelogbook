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
package be.vds.jtbdive.core.core.manager;

import java.io.Serializable;
import java.util.List;

import be.vds.jtbdive.core.core.Dive;

public class DiveManager implements Serializable {

	private static final long serialVersionUID = 6065427902918045635L;
	private List<Dive> dives;

	public List<Dive> getDives() {
		return dives;
	}

	public void setDives(List<Dive> dives) {
		this.dives = dives;
	}

	public boolean addDive(Dive dive) {
		return dives.add(dive);
	}

	public void removeDive(Dive dive) {
		dives.remove(dive);
	}

	public Dive getDiveByNumber(int i) {
		for (Dive dive : dives) {
			if(i == dive.getNumber()){
				return dive;
			}
		}
		return null;
	}
	



}
