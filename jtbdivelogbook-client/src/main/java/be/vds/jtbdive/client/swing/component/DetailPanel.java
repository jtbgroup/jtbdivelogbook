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

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import be.vds.jtbdive.client.view.utils.UIAgent;

public class DetailPanel extends JPanel {

	private static final long serialVersionUID = -4258317740216167127L;
	private boolean selected = false;


	public DetailPanel() {
		initDetailPanel();
	}
	
	public DetailPanel(LayoutManager layoutManager) {
		super(layoutManager);
		initDetailPanel();
	}

	private void initDetailPanel() {
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		setOpaque(false);
	}


	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	@Override
	protected void paintBorder(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setPaint(selected ? UIAgent.getInstance().getColorPanelSelected()
				: UIAgent.getInstance().getColorPanelUnselected());
		if (selected) {
			g2d.setStroke(new BasicStroke(2));
		} else {

			g2d.setStroke(new BasicStroke(1));
		}
		g2d
				.drawRoundRect(1, 1, this.getWidth() - 3, this.getHeight() - 3,
						9, 9);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setPaint(new GradientPaint(getWidth()/2, 0, UIAgent.getInstance().getColorDetailPanelTop(),
				getWidth()/2, getHeight(), UIAgent.getInstance().getColorDetailPanelBottom()));
		g2d.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 9, 9);

	}
}
