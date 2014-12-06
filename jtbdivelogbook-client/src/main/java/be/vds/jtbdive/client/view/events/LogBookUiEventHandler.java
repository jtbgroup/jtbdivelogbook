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
package be.vds.jtbdive.client.view.events;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.event.LogBookEvent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.PinableObject;

public class LogBookUiEventHandler {

	private static LogBookUiEventHandler instance;
	private List<LogBookEventListener> eventListeners;
	private LogBookManagerFacade logBookManagerFacade;

	public static LogBookUiEventHandler getInstance() {
		if (instance == null)
			instance = new LogBookUiEventHandler();
		return instance;
	}

	public void initialize() {
		eventListeners = new ArrayList<LogBookEventListener>();
	}

	public void addEventListener(LogBookEventListener logBookEventListener) {
		eventListeners.add(logBookEventListener);
	}

	public void removeEventListener(LogBookEventListener logBookEventListener) {
		eventListeners.remove(logBookEventListener);
	}

	public void registerLogBookManagerFacade(
			LogBookManagerFacade lbManagerFacade) {
		this.logBookManagerFacade = lbManagerFacade;
		this.logBookManagerFacade.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				if (arg instanceof LogBookEvent) {
					LogBookEvent event = (LogBookEvent) arg;
					if (event.getType().equals(
							LogBookEvent.CURRENT_DIVE_CHANGED)) {
						notifyDiveSiteSelected(null,
								((Dive) event.getNewValue()).getDiveSite());
					} else if (event.getType()
							.equals(LogBookEvent.DIVE_DELETED)
							|| event.getType().equals(
									LogBookEvent.DIVES_DELETED)) {
						if (logBookManagerFacade.getCurrentDive() == null) {
							notifyDiverSelected(null, null);
							notifyDiveSiteSelected(null, null);
						}
					} else if (event.getType().equals(
							LogBookEvent.DIVE_MODIFIED)) {
						notifyDiveSiteSelected(null,
								((Dive) event.getNewValue()).getDiveSite());
					}
				}
			}
		});
	}

	// public void notifyCurrentDiveChanged(Component source, Dive dive) {
	// for (LogBookEventListener listener : eventListeners) {
	// listener.currentDiveChanged(source, dive);
	// }
	// }

	public void notifyDiverSelected(Component source, Diver diver) {
		for (LogBookEventListener listener : eventListeners) {
			listener.diverSelected(source, diver);
		}
	}

	public void notifyDiveSiteSelected(Component source, DiveSite diveSite) {
		for (LogBookEventListener listener : eventListeners) {
			listener.diveSiteSelected(source, diveSite);
		}
	}

	public void notifyPinablesSelected(List<PinableObject> pins) {
		for (LogBookEventListener listener : eventListeners) {
			listener.pinablesSelected(pins);
		}
	}
	
	public void notifyPinableSelected(PinableObject pin) {
		for (LogBookEventListener listener : eventListeners) {
			listener.pinableSelected(pin);
		}
	}
}
