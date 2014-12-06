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
package be.vds.jtbdive.client.view.core.dive.profile;

public class DiveProfileEditionEvent {

	public static final int ENTRY_SELECTED = 1;
	public static final int ENTRY_ADDED = 2;
	public static final int ENTRY_REMOVED = 3;
	public static final int DIVE_PROFILE_SET = 4;
	public static final int ENTRY_MODIFIED = 5;
	
	private Object originator;
	private int type;
	private Object oldValue, newValue;

	
	public DiveProfileEditionEvent(int type, Object originator) {
		this(type, originator, null, null);
	}
	
	public DiveProfileEditionEvent(int type, Object originator, Object oldValue, Object newValue) {
		this.type = type;
		this.originator = originator;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	

	public Object getOriginator() {
		return originator;
	}

	public int getType() {
		return type;
	}
}
