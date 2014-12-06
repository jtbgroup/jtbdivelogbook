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
package be.vds.jtbdive.client.view.core.logbook.material;

import java.util.Observable;
import java.util.Observer;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.event.LogBookEvent;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.core.material.Material;

public class MaterialTreeObserverPanel extends MaterialTreePanel implements Observer {
	private LogBookManagerFacade logBookManagerFacade;


	public MaterialTreeObserverPanel(LogBookManagerFacade logBookManagerFacade) {
		super();
		this.logBookManagerFacade = logBookManagerFacade;
		if (null != logBookManagerFacade) {
			logBookManagerFacade.addObserver(this);
			loadAllMaterial();
		}
	}



	@Override
	public void update(Observable o, Object arg) {
		if (o.equals(logBookManagerFacade) && arg instanceof LogBookEvent) {
			LogBookEvent evt = (LogBookEvent) arg;
			if (evt.getType().equals(LogBookEvent.MATERIAL_SAVED)) {
				Material newMat = (Material) evt.getNewValue();
				addMaterial(newMat, false);
			} else if (evt.getType().equals(LogBookEvent.MATERIAL_DELETED)) {
				Material oldMat = (Material) evt.getOldValue();
				removeMaterial(oldMat);
			} else if (evt.getType().equals(LogBookEvent.MATERIAL_MERGED)) {
				replaceMaterial((Material) evt.getOldValue(),
						(Material) evt.getNewValue());
			} else if (evt.getType().equals(LogBookEvent.LOGBOOK_LOADED)) {
				clear();
				loadAllMaterial();
			} else if (evt.getType().equals(LogBookEvent.LOGBOOK_DELETED)
					|| evt.getType().equals(LogBookEvent.LOGBOOK_CLOSED)) {
				clear();
			}
		}
	}

	private void loadAllMaterial() {
		MatCave mc = logBookManagerFacade.getCurrentMatCave();
		if (null != mc) {
			for (Material mat : mc.getAllMaterials()) {
				addMaterial(mat, false);
			}
		}
	}
}
