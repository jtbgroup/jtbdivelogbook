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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Manometer2 extends JComponent {
	private static final long serialVersionUID = 3407393375566013691L;
	private List<Float> arrows = new ArrayList<Float>();
	// private Graphics2D g2;
	private String unit;
	private int minPressure = 0;
	private int maxPressure = 400;
	private int pressureIntermediateStep = 10;
	private int pressureStep = 50;

	private static final List<Color> COLORS;
	static {
		COLORS = new ArrayList<Color>();
		COLORS.add(Color.BLUE);
		COLORS.add(Color.GREEN);
	}

	public Manometer2(String unit) {
		this.unit = unit;
	}

	public void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		int centerX = width / 2;
		int centerY = height / 2;

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.black);
		g2.fillOval(0, 0, width, height);

		// white oval
		g2.setPaint(new Color(245, 245, 245));
		double w = (width > 100 ? width / 20 : 2);
		double h = (height > 100 ? height / 20 : 2);
		g2.fillOval((int) (w / 2), (int) (h / 2), (int) (width - w),
				(int) (height - h));

		// central point
		g2.setColor(Color.black);
		g2.fillOval(centerX - 2, centerY - 2, 5, 5);

		g2.setFont(new Font("Arial", Font.PLAIN, width / 20));

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0.8f));
		drawColorZone(g2, width, height, centerX, centerY, Color.RED,
				Color.RED, 0, 50);
		drawColorZone(g2, width, height, centerX, centerY, Color.YELLOW,
				Color.WHITE, 50, 100);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1f));

		g2.setColor(Color.BLACK);
		for (int i = minPressure; i <= maxPressure; i += pressureIntermediateStep) {
			if (i % pressureStep == 0) {
				drawAngle(g2, width, height, centerX, centerY, i, true);
			} else {
				drawAngle(g2, width, height, centerX, centerY, i, false);
			}
		}

		drawPressureIndicator(g2, width, height, centerX, centerY);

		int i = 0;
		for (Float arrow : arrows) {
			drawArrow(g2, width, height, centerX, centerY, arrow, i);
			i++;
		}
		g2.dispose();

	}

	private void drawPressureIndicator(Graphics2D g2, int width, int height,
			int centerX, int centerY) {
		int i = 1;
		String indicator = null;
		int fontSize = height / 20;
		Font currentFont = g2.getFont();
		g2.setFont(new Font(currentFont.getFontName(), currentFont.getStyle(),
				fontSize));
		int length = 0;
		for (Float arrow : arrows) {
			indicator = String.valueOf(arrow.intValue()) + " " + unit;
			length = indicator.length() * fontSize / 4;
			g2.setColor(COLORS.get(i - 1));
			g2.drawString(indicator, centerX - (length), centerY
					+ ((width / 7) + (i * fontSize) + 5));
			i++;
		}
	}

	private void drawColorZone(Graphics2D g2, int width, int height,
			int centerX, int centerY, Color color, Color color2, int start,
			int stop) {
		g2.setColor(color);

		int[] angles = new int[stop + 1 - start];
		for (int i = 0; i < angles.length; i++) {
			angles[i] = start + i;
		}

		int[] xArray = new int[angles.length + 1];
		int[] yArray = new int[angles.length + 1];
		xArray[0] = centerX;
		yArray[0] = centerY;
		for (int i = 0; i < angles.length; i++) {
			float angle = calculateAngleInDegree(angles[i]);
			double pointX = (width / 2)
					- ((Math.cos(Math.toRadians(angle))) * (width * 0.75 / 2));
			double pointY = (height / 2)
					- ((Math.sin(Math.toRadians(angle))) * (height * 0.75 / 2));
			xArray[i + 1] = (int) pointX;
			yArray[i + 1] = (int) pointY;
		}

		Polygon polygon = new Polygon(xArray, yArray, angles.length + 1);

		int gradient1x = (int) ((width / 2) - ((Math.cos(Math.toRadians(50))) * (width * 0.10 / 2)));
		int gradient1y = (int) ((height / 2) - ((Math.sin(Math.toRadians(50))) * (height * 0.10 / 2)));
		int gradient2x = (int) ((width / 2) - ((Math.cos(Math.toRadians(55))) * (width * 0.70 / 2)));
		int gradient2y = (int) ((height / 2) - ((Math.sin(Math.toRadians(55))) * (height * 0.70 / 2)));

		GradientPaint gp = new GradientPaint(gradient1x, gradient1y, color,
				gradient2x, gradient2y, color2, false);
		// Fill with a gradient.
		g2.setPaint(gp);
		g2.fill(polygon);

		g2.fillPolygon(polygon);
	}

	/**
	 * We only add an arrow if the number of arrows is < than the number of
	 * colors
	 * 
	 * @param pressure
	 * @return
	 */
	public boolean addArrow(float pressure) {
		if (arrows.size() < COLORS.size()) {
			return arrows.add(pressure);
		}
		return false;
	}

	public void deleteArrow(int index) {
		arrows.remove(index);
	}

	public boolean deleteAllArrows() {
		return arrows.removeAll(arrows);
	}

	private void drawArrow(Graphics2D g2, int width, int height, int centerX,
			int centerY, Float arrow, int color) {
		float angle = calculateAngleInDegree(arrow);
		double pointY = 0;
		double pointX = 0;
		pointY = (height / 2)
				- ((Math.sin(Math.toRadians(angle))) * (height * 0.65 / 2));
		pointX = (width / 2)
				- ((Math.cos(Math.toRadians(angle))) * (width * 0.65 / 2));

		g2.setColor(COLORS.get(color));
		g2.drawLine(centerX, centerY, (int) pointX, (int) pointY);
	}

	private void drawAngle(Graphics2D g2, int width, int height, int centerX,
			int centerY, float pressure, boolean b) {
		float angle = calculateAngleInDegree(pressure);

		// calculate & print point
		double pointY = 0;
		double pointX = 0;
		double w = (width > 100 ? width / 40 : 2);
		double h = (height > 100 ? height / 40 : 2);
		pointY = (height / 2)
				- ((Math.sin(Math.toRadians(angle))) * (height * 0.80 / 2));
		pointX = (width / 2)
				- ((Math.cos(Math.toRadians(angle))) * (width * 0.80 / 2));
		if (b) {
			g2.fillOval((int) pointX - 2, (int) pointY - 2, (int) w, (int) h);
		} else {
			g2.fillOval((int) pointX - 1, (int) pointY - 1, (int) (w / 2),
					(int) (h / 2));
		}

	}

	private float calculateAngleInDegree(float pressure) {
		// 27 car 270�
		// 40 car 400 bar
		// -45 car d�callage de 45�
		float angle = (((float) 27 / 40) * pressure) - 45;
		return angle;
	}

	public static void main(String[] args) {
		Manometer2 mano = new Manometer2("bar");
		mano.addArrow((float) 200.8);
		mano.addArrow((float) 10);
		mano.setPreferredSize(new Dimension(200, 200));

		JFrame frame = new JFrame();
		frame.getContentPane().add(mano);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		try {
			Thread.sleep(500);
			mano.changeArrow(180, 0);
			mano.changeArrow(20, 1);

			Thread.sleep(500);
			mano.changeArrow(170, 0);
			mano.changeArrow(30, 1);

			Thread.sleep(500);
			mano.changeArrow(160, 0);
			mano.changeArrow(40, 1);

			Thread.sleep(500);
			mano.changeArrow(150, 0);
			mano.changeArrow(50, 1);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void changeArrow(float value, int index) {
		if (null == arrows.get(index)) {
			arrows.add(index, value);
		} else {
			List<Float> newArrows = new ArrayList<Float>();
			int i = 0;
			for (Float arrow : arrows) {
				if (index == i) {
					newArrows.add(value);
				} else {
					newArrows.add(arrow);
				}
				i++;
			}
			arrows = newArrows;
			repaint();
		}
	}

	public void setUnit(String unit) {
		this.unit = unit;
		repaint();
	}

}
