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
package be.vds.jtbdive.client.view.core.map;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.core.core.Coordinates;
import be.vds.jtbdive.core.core.PinableObject;
import be.vds.jtbdive.core.core.SimplePinableObject;

public class MapPinDialog extends PromptDialog {

	private MapPanel mapPanel;

	public MapPinDialog() {
		super(i18n.getString("map.pin"), null);
		setOkButtonEnabled(false);
	}

	@Override
	protected Component createContentPanel() {
		mapPanel = new MapPanel();
		mapPanel.setOriginalPosition();
		mapPanel.addMapMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Coordinates coordinates = mapPanel.getCoordinates(e.getPoint());
				mapPanel.reset();
				if (null != coordinates) {
					mapPanel.addPin(new SimplePinableObject(coordinates));
					setOkButtonEnabled(true);
				} else {
					setOkButtonEnabled(false);
				}
				mapPanel.repaint();

			}
		});

		return mapPanel;
	}

	public Collection<Coordinates> getAllPins() {
		return mapPanel.getAllPins();
	}

	public void addPin(PinableObject pinableObject) {
		mapPanel.addPin(pinableObject);
	}

	public void centerOnCoordinates(Coordinates coordinates) {
		mapPanel.centerOnCoordinates(coordinates);
	}

	public void setZoomPercent(float zoom) {
		mapPanel.setZoomPercent(zoom);
	}
}
