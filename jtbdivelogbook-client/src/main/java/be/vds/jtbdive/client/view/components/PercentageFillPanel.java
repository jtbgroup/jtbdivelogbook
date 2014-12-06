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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PercentageFillPanel extends JPanel {

	private static final long serialVersionUID = -946377782478809680L;
	private double percentage;
	private int alpha = 125;
	private JLabel textLabel;
	private Color color = Color.BLUE;
	private Color background = Color.WHITE;
	private Color borderColor = Color.BLACK;

	@Override
	protected void paintComponent(Graphics grphcs) {
		super.paintComponent(grphcs);
		double width = getWidth() * percentage;
		Graphics2D g2 = (Graphics2D) grphcs;

		Color c = color;

		int red = c.getRed();
		int green = c.getGreen();
		int blue = c.getBlue();
		Color c2 = new Color(red, green, blue, alpha);

		int height = getHeight();

		g2.setPaint(background);
		g2.fillRoundRect(0, 0, getWidth(), height, 10, 10);

		g2.setPaint(new GradientPaint(0, height / 2, c, (float) width,
				height / 2, c2));
		g2.fillRoundRect(0, 0, (int) width, height, 10, 10);

		g2.setPaint(borderColor);
		g2.drawRoundRect(0, 0, getWidth() - 1, height - 1, 10, 10);
	}

	public PercentageFillPanel(String text) {
		init(text);
	}

	public PercentageFillPanel(String text, Color color, int alpha) {
		this.alpha = alpha;
		this.color = color;
		init(text);
	}

	private void init(String text) {
		this.setLayout(new BorderLayout());
		setOpaque(false);
		setBackground(background);
		textLabel = new JLabel(text);
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add(textLabel, BorderLayout.CENTER);
	}

	public void setText(String text) {
		textLabel.setText(text);
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
		this.repaint();
		this.validate();
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
		this.repaint();
		this.validate();
	}
}
