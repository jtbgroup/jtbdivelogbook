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
package be.vds.jtbdive.client.swing.component.glass;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;

public class AlphaGlassPane extends JComponent {
	private float alphaComposite;

	public AlphaGlassPane(float alphaComposite) {
		this.alphaComposite = alphaComposite;
		initializeListeners();
	}

	public AlphaGlassPane() {
		this(0.65f);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Rectangle clip = g.getClipBounds();
		Color alphaWhite = new Color(1.0f, 1.0f, 1.0f, alphaComposite);
		g.setColor(alphaWhite);
		g.fillRect(clip.x, clip.y, clip.width, clip.height);
	}

	private void initializeListeners() {
		addMouseListener(new MouseAdapter() {
		});
		addKeyListener(new KeyAdapter() {
		});
		addMouseMotionListener(new MouseMotionAdapter() {
		});
	}
}
