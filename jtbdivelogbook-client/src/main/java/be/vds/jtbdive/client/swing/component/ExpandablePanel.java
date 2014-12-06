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
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.view.utils.UIAgent;

public abstract class ExpandablePanel extends JPanel {

	private static final long serialVersionUID = 8242314371273137811L;
	private Component centralPanel;
	private JButton hideButton;
	private Color lightColor, darkColor;

	public ExpandablePanel(Color lightColor, Color darkColor) {
		this.lightColor = lightColor;
		this.darkColor = darkColor;
		init();
	}

	public ExpandablePanel() {
		this(UIAgent.getInstance().getColorTitlePanelAlpha(), UIAgent
				.getInstance().getColorTitlePanel());
	}

	private void init() {

		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		JPanel centralPanel = new JPanel(new BorderLayout());
		centralPanel.setOpaque(false);
		centralPanel.setBorder(new LineBorder(darkColor, UIAgent
				.getInstance().getLineThickness()));
		centralPanel.add(createInnerCentralPanel(), BorderLayout.CENTER);

		GridBagConstraints gc = new GridBagConstraints();
		GridBagLayoutManager.addComponent(this, createFullHeaderPanel(), gc, 0,
				0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, centralPanel, gc, 0, 1, 1, 1,
				1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

	}

	private Component createInnerCentralPanel() {
		centralPanel = createGlobalCentralPanel();
		return centralPanel;
	}

	protected abstract Component createGlobalCentralPanel();

	private Component createFullHeaderPanel() {
		hideButton = new JButton(new AbstractAction() {

			private static final long serialVersionUID = -2303054022340645502L;

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean visible = centralPanel.isVisible();
				centralPanel.setVisible(!visible);
				hideButton.setIcon(visible ? UIAgent.getInstance()
						.getIcon(UIAgent.ICON_BTN_EXPAND_ALL_16)
						: UIAgent.getInstance().getIcon(
								UIAgent.ICON_BTN_COLLAPSE_ALL_16));

			}
		});
		hideButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_BTN_COLLAPSE_ALL_16));
		hideButton.setBorderPainted(false);
		hideButton.setContentAreaFilled(false);

		JPanel p = new JPanel() {
			private static final long serialVersionUID = 3228892549138466801L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

				g2d.setPaint(darkColor);
				g2d.fillRect(0, getHeight() - 9, getWidth(), getHeight());

				g2d.setPaint(new GradientPaint(getWidth() / 2, 0, lightColor,
						getWidth() / 2, getHeight(), darkColor));
				g2d.fillRoundRect(0, 1, getWidth(), getHeight() - 3, 9, 9);

			}
		};
		p.setOpaque(false);
		p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
		p.add(hideButton);
		p.add(Box.createHorizontalStrut(10));
		p.add(createHeaderPanel());
		return p;
	}

	protected abstract Component createHeaderPanel();

}
