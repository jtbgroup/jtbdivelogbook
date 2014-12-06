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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.core.core.Coordinates;
import be.vds.jtbdive.core.core.PinableObject;
import be.vds.jtbdive.core.utils.StringManipulator;

public class MapPanel extends JPanel {
	private static final long serialVersionUID = -7548327244783652023L;

	private static final int DEFAULT_ZOOM = 7;
	private JPanel cardPanel;
	private JLabel coordinatesLabel;
	private JMapViewer map;

	public MapPanel() {
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		this.add(createMapPanel(), BorderLayout.CENTER);
		this.add(createOptionsPanel(), BorderLayout.NORTH);
	}

	private Component createMapPanel() {
		cardPanel = new JPanel(new BorderLayout());
		cardPanel.add(createMapKit(), BorderLayout.CENTER);
		return cardPanel;
	}

	private Component createOptionsPanel() {
		coordinatesLabel = new JLabel();
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		GridBagLayoutManager.addComponent(p, coordinatesLabel, gc, 0, 0, 1, 1,
				1, 0, GridBagConstraints.NONE, GridBagConstraints.EAST);

		return p;
	}

	private Component createMapKit() {
		map = new JMapViewer();
		map.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				Coordinate coord = map.getPosition(e.getPoint());
				coordinatesLabel.setText(StringManipulator.formatCoordinates(
						coord.getLat(), coord.getLon(), 5));
			}
		});

		return map;
	}

	public void setOriginalPosition() {
		map.setDisplayPositionByLatLon(50.8467, 4.3525, DEFAULT_ZOOM);
	}

	public void centerOnCoordinates(Coordinates coordinates) {
		double lati = coordinates.getLatitude();
		double longi = coordinates.getLongitude();
		int zoom = map.getZoom();
		map.setDisplayPositionByLatLon(lati, longi, zoom);
	}

	public Collection<Coordinates> getAllPins() {
		List<Coordinates> l = new ArrayList<Coordinates>();

		for (MapMarker marker : map.getMapMarkerList()) {
			MapMarker dot = (MapMarker) marker;
			l.add(new Coordinates(dot.getLat(), dot.getLon()));
		}

		return l;
	}

	public void addMapMouseListener(MouseListener mouseListener) {
		map.addMouseListener(mouseListener);
	}

	public Coordinates getCoordinates(Point point) {
		Coordinate coord = map.getPosition(point);
		return new Coordinates(coord.getLat(), coord.getLon());
	}

	private void removeAllPins() {
		List<MapMarker> pins = new ArrayList<MapMarker>(map.getMapMarkerList());
		for (MapMarker marker : pins) {
			map.removeMapMarker(marker);
		}
	}

	public void addPin(PinableObject mapPin) {
		map.addMapMarker(new MapPin(mapPin));
	}

	public void setPins(List<PinableObject> mapPins) {
		reset();
		for (PinableObject pin : mapPins) {
			addPin(pin);
		}
		revalidate();
		repaint();
	}

	public void reset() {
		removeAllPins();
	}

	/**
	 * Sets the zoom in percentage
	 * 
	 * @param zoomPercent
	 */
	public void setZoomPercent(float zoomPercent) {
		if (zoomPercent < 0 || zoomPercent > 1)
			throw new InvalidParameterException(
					"zoompercent must be a value between 0 and 1");

		map.setZoom((int) (JMapViewer.MAX_ZOOM * zoomPercent));
	}

}
