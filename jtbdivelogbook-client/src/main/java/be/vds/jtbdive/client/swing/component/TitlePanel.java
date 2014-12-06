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
package be.vds.jtbdive.client.swing.component;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import be.vds.jtbdive.client.view.utils.UIAgent;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class TitlePanel extends JPanel {

	private static final long serialVersionUID = -2948286199701098643L;
	protected JLabel label;

	public TitlePanel() {
		this(null);
	}

	public TitlePanel(String title) {
		init(title);
	}

	private void init(String title) {
		label = createJLabel();
		label.setForeground(UIAgent.getInstance()
				.getColorTitlePanelForeground());
		label.setFont(new Font("Arial", Font.PLAIN + Font.BOLD, 16));
		setLayout(new BorderLayout());
		add(label, BorderLayout.CENTER);
		add(Box.createHorizontalStrut(20), BorderLayout.WEST);
		add(Box.createHorizontalStrut(20), BorderLayout.EAST);

		if (null != title) {
			setTitle(title);
		}
	}

	protected JLabel createJLabel() {
		return new JLabel();
	}

	@Override
	public void paintComponent(Graphics grphcs) {
		Graphics2D g2 = (Graphics2D) grphcs;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		int i = getWidth() / 2;
		int h = getHeight();

		g2.setPaint(new GradientPaint(i, 0, UIAgent.getInstance()
				.getColorTitlePanelAlpha(), i, h / 2, UIAgent.getInstance()
				.getColorTitlePanel()));
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), h / 2, h / 2);
	}

	public void setTitle(String title) {
		label.setText(title);
	}

}
