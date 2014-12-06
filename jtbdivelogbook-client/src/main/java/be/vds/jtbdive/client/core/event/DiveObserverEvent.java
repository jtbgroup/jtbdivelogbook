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
package be.vds.jtbdive.client.core.event;

import be.vds.jtbdive.core.core.Dive;

public class DiveObserverEvent {
	public static final int SAVE = 0;
	public static final int UPDATE = 1;
	public static final int DELETE = 2;
	private Dive newValue;
	private Dive oldValue;
	private int type;

	public DiveObserverEvent(int type, Dive oldValue, Dive newValue) {
		this.type = type;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public Dive getNewValue() {
		return newValue;
	}

	public void setNewValue(Dive newValue) {
		this.newValue = newValue;
	}

	public Dive getOldValue() {
		return oldValue;
	}

	public void setOldValue(Dive oldValue) {
		this.oldValue = oldValue;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
