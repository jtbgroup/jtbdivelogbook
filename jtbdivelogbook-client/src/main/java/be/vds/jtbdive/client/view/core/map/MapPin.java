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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.PinableObject;

public class MapPin implements MapMarker {
	private static final Image PIN_IMAGE = UIAgent.getInstance()
			.getBufferedImage(UIAgent.ICON_PIN_BLACK_16);
	private static int PIN_WIDTH = PIN_IMAGE.getWidth(null);
	private static int PIN_HEIGHT = PIN_IMAGE.getHeight(null);
	private double lat;
	private double lon;
	private PinableObject pinableObject;

	public MapPin(PinableObject pinableObject) {
		this.pinableObject = pinableObject;
		this.lat = pinableObject.getCoordinates().getLatitude();
		this.lon = pinableObject.getCoordinates().getLongitude();
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public void paint(Graphics g, Point position) {
		double horTr = PIN_WIDTH / 2;
		double vertTr = PIN_HEIGHT;
		
		int x = (int) (position.x - horTr);
		int y = (int) (position.y - vertTr);
		g.drawImage(getPinImage(), x,
				y, null);
		g.setFont(UIAgent.getInstance().getFontMapPin());
		g.drawString(pinableObject.getShortDescription(), x, y-3);
	}

	private Image getPinImage() {
		return PIN_IMAGE;
	}
}
