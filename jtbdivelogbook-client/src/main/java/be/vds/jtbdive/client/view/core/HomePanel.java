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

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.Timer;

import be.smd.i18n.swing.I18nHyperlink;
import be.vds.jtb.swing.component.waves.CurvesPanel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.actions.ActionType;
import be.vds.jtbdive.client.actions.LogBookApplActionsContoller;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.util.ResourceManager;
import be.vds.jtbdive.client.view.utils.UIAgent;

/**
 * @author Gautier Vanderslyen
 * 
 */
public class HomePanel extends CurvesPanel implements ActionListener {
	private static final long serialVersionUID = 6368238513946757935L;
	private Timer animation;
	private LogBookApplActionsContoller actions;

	public HomePanel(LogBookApplActionsContoller actions) {
		this.actions = actions;
		init();
		animation = new Timer(50, this);
	}

	private void init() {
		setGradientEnd(UIAgent.getInstance().getColorHomePanel());
		setGradientStart(UIAgent.getInstance().getColorHomePanelAlpha());

		JLabel imageLbl = new JLabel(ResourceManager.getInstance()
				.getImageIcon("logos/logo_125.png"));

		JLabel nameLbl = new JLabel(" Jt'B Dive Logbook");
		nameLbl.setForeground(Color.WHITE);
		nameLbl.setFont(new Font("Times new roman", Font.ITALIC, 30));

		int y = 0;
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);
		GridBagLayoutManager.addComponent(this, nameLbl, c, 0, y, 1, 1, 0, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(this, imageLbl, c, 1, y, 1, 1, 0, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);

		c.insets = new Insets(3, 10, 3, 10);

		GridBagLayoutManager.addComponent(this, createBeginComponent(), c, 0,
				++y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(this, Box.createVerticalGlue(), c, 0,
				++y, 1, 1, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(this, createGoToSite(), c, 1, ++y, 1,
				1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.EAST);

	}

	private Component createGoToSite() {
		Action a = new AbstractAction() {

			private static final long serialVersionUID = 8897655491855239764L;

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					Desktop.getDesktop()
							.browse(new URI(
									"http://www.sourceforge.net/projects/jtbdivelogbook"));
				} catch (Exception e1) {
					ExceptionDialog.showDialog(e1, null);
				}
			}
		};
		I18nHyperlink link = new I18nHyperlink(a);
		link.setTextBundleKey("goto.website");
		link.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_NETWORK_16));
		setLinkColor(link);
		return link;
	}

	private void setLinkColor(I18nHyperlink link) {
		link.setForeground(Color.WHITE);
		link.setActiveColor(Color.WHITE);
		link.setHoverColor(Color.WHITE);
		link.setTextColor(Color.WHITE);
		link.setFont(UIAgent.getInstance().getFontTitleDetail());
	}

	private Component createBeginComponent() {
		I18nHyperlink link = new I18nHyperlink(
				actions.getAction(ActionType.PERSPECTIVE_EDITION));
		link.setTextBundleKey("application.start");
		link.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_LOGBOOK_24));
		setLinkColor(link);
		return link;
	}

	public void enableAnimation(boolean enabled) {
		if (enabled) {
			animation.start();
		} else {
			animation.stop();
		}
	}

	public void actionPerformed(ActionEvent e) {
		this.repaint();
	}
}
