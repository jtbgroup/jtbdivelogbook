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

import java.util.List;
import java.util.Observable;

import be.vds.jtbdive.client.core.event.DiverEvent;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.manager.DiverManager;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.DiverUsedException;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.DiverBusinessDelegate;
import be.vds.jtbdive.core.logging.Syslog;

public class DiverManagerFacade extends Observable {
	private static final Syslog LOGGER = Syslog
			.getLogger(DiverManagerFacade.class);
	private DiverManager diverManager;

	public DiverManagerFacade(DiverManager diverManager) {
		this.diverManager = diverManager;
	}

	public List<Diver> findDivers() throws DataStoreException {
		return diverManager.findAllDivers();
	}

	public Diver saveDiver(Diver diver) throws DataStoreException {
		Diver b = diverManager.saveDiver(diver);
		setChanged();
		notifyObservers(new DiverEvent(DiverEvent.SAVE, diver, b));
		return b;
	}

	public boolean deleteDiver(Diver diver) throws DataStoreException,
			DiverUsedException {
		boolean b = diverManager.deleteDiver(diver);
		if (b) {
			setChanged();
			notifyObservers(new DiverEvent(DiverEvent.DELETE, diver, null));
		}
		return b;
	}

	public Diver updateDiver(Diver diver) throws DataStoreException {
		Diver b = diverManager.updateDiver(diver);
		setChanged();
		notifyObservers(new DiverEvent(DiverEvent.UPDATE, diver, b));
		return b;
	}

	public List<Diver> findDiversByName(String firstName, String lastName)
			throws DataStoreException {
		return diverManager.findDiversByName(firstName, lastName);
	}

	public List<Diver> findDiversByFirstOrLastName(String name)
			throws DataStoreException {
		return diverManager.findDiversByFirstOrLastName(name);
	}

	public boolean mergeDivers(Diver diverToKeep, Diver diverToDelete)
			throws DataStoreException, DiverUsedException {
		boolean b = diverManager.mergeDivers(diverToKeep, diverToDelete);
		setChanged();
		notifyObservers(new DiverEvent(DiverEvent.MERGE, diverToDelete, diverToKeep));
		return b;
	}

	public void setBusinessDelegate(DiverBusinessDelegate diverBusinessDelegate) {
		diverManager.setBusinessDelegate(diverBusinessDelegate);
		LOGGER.info("Business Delegate changed");
	}
}
