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
package be.vds.jtbdive.client.view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.util.ResourceManager;
import be.vds.jtbdive.core.core.Version;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class SplashScreen extends JPanel {

	private static final long serialVersionUID = 1012690482435697901L;
	private Image bkg;
	private JLabel label;

	public SplashScreen() {
		init();
	}

	private void init() {
		ImageIcon bkgIcon = ResourceManager.getInstance().getImageIcon(
				"splash.png");
		if (bkgIcon != null) {
			bkg = bkgIcon.getImage();
			Dimension size = new Dimension(bkg.getWidth(null),
					bkg.getHeight(null));
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
			setSize(size);
		}
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

		this.setLayout(new BorderLayout());

		Font font = new Font("Arial", Font.ITALIC, 10);

		label = new JLabel();
		label.setForeground(Color.WHITE);
		label.setFont(font);

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p.setOpaque(false);
		JLabel l = new JLabel(Version.getCurrentVersion().toString());
		l.setFont(font);
		p.add(l);

		this.add(p, BorderLayout.NORTH);
		this.add(label, BorderLayout.SOUTH);
		this.setOpaque(false);
	}

	public Window putOnWindow() {
		JWindow window = new JWindow();
		window.getContentPane().add(this);
		window.pack();
		WindowUtils.centerWindow(window);
		return window;
	}

	@Override
	public void paintComponent(Graphics g) {
		if (null != bkg) {
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(bkg, 0, 0, null);
		}
	}

	public void setText(String text) {
		label.setText(text);
	}

}
