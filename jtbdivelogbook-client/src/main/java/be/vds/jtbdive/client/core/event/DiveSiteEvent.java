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

import be.vds.jtbdive.core.core.DiveSite;

public class DiveSiteEvent {

	public static final int SAVE = 0;
	public static final int DELETE = 1;
	public static final int UPDATE = 2;
	public static final int MERGE = 3;

	private int action;
	private DiveSite newValue;
	private DiveSite oldValue;

	public DiveSiteEvent(int action, DiveSite oldValue, DiveSite newValue) {
		this.action = action;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public DiveSite getNewValue() {
		return newValue;
	}

	public DiveSite getOldValue() {
		return oldValue;
	}

	public int getAction() {
		return action;
	}

}
