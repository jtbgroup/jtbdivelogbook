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
package be.vds.jtbdive.client.view.docking;

import java.awt.Component;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.event.LogBookEvent;
import be.vds.jtbdive.client.view.core.map.MapPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Coordinates;
import be.vds.jtbdive.core.core.PinableObject;

public class MapDockable extends I18nDefaultSingleCDockable implements Observer {
	private MapPanel mapPanel;

	public MapDockable(LogBookManagerFacade logBookManagerFacade) {
		super(DockingLayoutManager.VIEW_MAP, DockingLayoutManager.VIEW_MAP,
				UIAgent.getInstance().getIcon(UIAgent.ICON_MAP_16), null);
		add(createContent());
		setCloseable(true);
		logBookManagerFacade.addObserver(this);
	}

	private Component createContent() {
		mapPanel = new MapPanel();
		mapPanel.setOriginalPosition();
		return mapPanel;
	}

	public void centerOnCoordinates(Coordinates coordinates) {
		mapPanel.centerOnCoordinates(coordinates);
	}

	public void keepOnlyThisPinAndCenter(PinableObject pin) {
		mapPanel.reset();
		mapPanel.addPin(pin);
		mapPanel.centerOnCoordinates(pin.getCoordinates());
		mapPanel.setZoomPercent(0.5f);
	}

	public void setPins(List<PinableObject> pins) {
		mapPanel.setPins(pins);
	}
	
	public void reset() {
		mapPanel.reset();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof LogBookEvent) {
			LogBookEvent event = (LogBookEvent) arg;
			if (event.getType().equals(LogBookEvent.LOGBOOK_CLOSED)
					|| event.getType().equals(LogBookEvent.LOGBOOK_LOADED)
					|| event.getType().equals(LogBookEvent.LOGBOOK_DELETED)) {
				reset();
			}
		}

	}
}
