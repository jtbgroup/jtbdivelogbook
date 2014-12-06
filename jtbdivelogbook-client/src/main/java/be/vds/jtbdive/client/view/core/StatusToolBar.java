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
package be.vds.jtbdive.client.view.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import be.vds.jtbdive.client.swing.component.HeapMonitorGraphPanel;
import be.vds.jtbdive.client.swing.component.HeapMonitorShortPanel;
import be.vds.jtbdive.core.logging.Syslog;

public class StatusToolBar extends JPanel {

	private static final long serialVersionUID = 7357929282634762652L;
	private HeapMonitorGraphPanel monitorPanel;
	private static final Syslog LOGGER = Syslog.getLogger(StatusToolBar.class);
	private HeapMonitorShortPanel heapPanel;

	public StatusToolBar() {
		init();
	}

	private void init() {
		this.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		this.add(createHeapShortPanel());
	}

	private JComponent createHeapShortPanel() {
		heapPanel = new HeapMonitorShortPanel();
		Dimension dim = new Dimension(100, 21);
		heapPanel.setPreferredSize(dim);
		heapPanel.setMaximumSize(dim);
		heapPanel.setMinimumSize(dim);
		heapPanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				System.gc();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				displayMonitor();
				LOGGER.debug("display monitor window");
			}

			@Override
			public void mouseExited(MouseEvent e) {
				hideMonitor();
				LOGGER.debug("hide monitor window");
			}
		});

		JPanel memoryPanel = new JPanel();
		memoryPanel.setLayout(new BorderLayout(0, 0));
		memoryPanel.add(heapPanel, BorderLayout.CENTER);
		return memoryPanel;
	}

	private void hideMonitor() {
		if (null != monitorPanel) {
			monitorPanel.stop();
			monitorPanel.getParentWindow().dispose();
		}
	}

	private void displayMonitor() {
		monitorPanel = new HeapMonitorGraphPanel();
		monitorPanel.start();
		Window w = monitorPanel.putOnWindow(150, 80);

		Point p = heapPanel.getLocation();
		SwingUtilities.convertPointToScreen(p, heapPanel);
		// p.setLocation(p.x - monitorPanel.getWidth(), p.y);
		int x = p.x + heapPanel.getWidth() - w.getWidth();
		int y = p.y - 5 - w.getHeight();
		w.setLocation(x, y);
		w.setVisible(true);
	}

}
