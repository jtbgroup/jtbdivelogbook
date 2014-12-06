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
package be.vds.jtbdive.client.view.core.modifieddives;

import java.util.HashSet;
import java.util.Set;

import org.jdesktop.swingx.JXPanel;

import be.vds.jtbdive.client.view.events.ModificationListenable;
import be.vds.jtbdive.client.view.events.ModificationListener;


public class ModificationListenableJPanel extends JXPanel implements
		ModificationListenable {
	private static final long serialVersionUID = 2732675012372982567L;
	private Set<ModificationListener> modificationListeners = new HashSet<ModificationListener>();
	private boolean activateNotification = true;

	@Override
	public void addModificationListener(
			ModificationListener modificationListener) {
		modificationListeners.add(modificationListener);
	}

	@Override
	public void removeModificationListener(
			ModificationListener modificationListener) {
		modificationListeners.remove(modificationListener);
	}

	@Override
	public void removeAllModificationListener() {
		modificationListeners.removeAll(modificationListeners);
	}

	protected void notifyModificationListeners(boolean isModified) {
		if (activateNotification) {
			for (ModificationListener listener : modificationListeners) {
				listener.isModified(this, isModified);
			}
		}
	}

	public void activateNotification(boolean b) {
		activateNotification = b;
	}
}
