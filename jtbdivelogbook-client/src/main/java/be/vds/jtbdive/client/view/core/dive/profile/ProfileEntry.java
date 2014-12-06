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

import java.io.Serializable;

public class ProfileEntry implements Serializable {

	private static final long serialVersionUID = -4235362791048496584L;
	private double time;
	private double depth;
	private boolean decoEntry;
	
	private boolean ascentWarning;
	private boolean decoCeilingWarning;
	private boolean remainingBottomTimeWarning;
	
	public ProfileEntry() {
	}
	
	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getDepth() {
		return depth;
	}

	public void setDepth(double depth) {
		this.depth = depth;
	}

	public boolean isDecoEntry() {
		return decoEntry;
	}

	public void setDecoEntry(boolean decoEntry) {
		this.decoEntry = decoEntry;
	}

	public boolean isAscentWarning() {
		return ascentWarning;
	}

	public void setAscentWarning(boolean ascentWarning) {
		this.ascentWarning = ascentWarning;
	}

	public boolean isDecoCeilingWarning() {
		return decoCeilingWarning;
	}

	public void setDecoCeilingWarning(boolean decoCeilingWarning) {
		this.decoCeilingWarning = decoCeilingWarning;
	}

	public boolean isRemainingBottomTimeWarning() {
		return remainingBottomTimeWarning;
	}

	public void setRemainingBottomTimeWarning(boolean remainingBottomTimeWarning) {
		this.remainingBottomTimeWarning = remainingBottomTimeWarning;
	}

	
	@Override
	public String toString() {
		return time + "s - " + depth + "m";
	}
	
}
