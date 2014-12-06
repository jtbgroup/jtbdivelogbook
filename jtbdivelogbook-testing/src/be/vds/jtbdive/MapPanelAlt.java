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
package be.vds.jtbdive;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.w3c.dom.events.MouseEvent;

import com.roots.map.MapPanel;



public class MapPanelAlt {

	private MapPanel mapPanel1;

	public void run() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(createCentralPanel());
		f.setSize(600, 600);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	private JComponent createCentralPanel() {
		JSplitPane split = new JSplitPane();
		split.setLeftComponent(createMapPanel1());
		split.setRightComponent(createMapPanel2());
		return split;
	}

	private JComponent createMapPanel2() {
		JMapViewer viewer = new JMapViewer();
		return viewer;
	}

	private JComponent createMapPanel1() {
		mapPanel1 = new MapPanel();
		// p.getOverlayPanel().setVisible(false);
		mapPanel1.addMouseListener(createMouseListener());
		return mapPanel1;
	}

	private MouseListener createMouseListener() {
		MouseAdapter adapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				double longitude = mapPanel1.position2lon(
						mapPanel1.getCursorPosition().x, mapPanel1.getZoom());
				double latitude = mapPanel1.position2lat(
						mapPanel1.getCursorPosition().y, mapPanel1.getZoom());

				System.out.println(longitude + " - " + latitude);
			}
		};
		return adapter;
	}
}


