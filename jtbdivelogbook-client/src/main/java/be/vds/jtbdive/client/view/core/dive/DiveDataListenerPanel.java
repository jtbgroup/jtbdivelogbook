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
package be.vds.jtbdive.client.view.core.dive;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import be.vds.jtbdive.client.view.events.ValuesModifiedListener;
import be.vds.jtbdive.core.core.Dive;

public abstract class DiveDataListenerPanel extends JPanel {

	private static final long serialVersionUID = 293134953350130189L;

	private List<ValuesModifiedListener> valuesModifiedListeners = new ArrayList<ValuesModifiedListener>();

	private boolean activateNotification;

	public void setActivateNotification(boolean activateNotification) {
		this.activateNotification = activateNotification;
	}

	public void notifyModifiedValues(boolean modified) {
		if (activateNotification) {
			for (ValuesModifiedListener valuesModifiedListener : valuesModifiedListeners) {
				valuesModifiedListener.valuesModified(this, modified);
			}
		}
	}

	public void addValuesModifiedListeners(
			ValuesModifiedListener valuesModifiedListener) {
		valuesModifiedListeners.add(valuesModifiedListener);
	}

	public void removeValuesModifiedListeners(
			ValuesModifiedListener valuesModifiedListener) {
		valuesModifiedListeners.remove(valuesModifiedListener);
	}

	public abstract void fillDive(Dive diveToFill);
}
