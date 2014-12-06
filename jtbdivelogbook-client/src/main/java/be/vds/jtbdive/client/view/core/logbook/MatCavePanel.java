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
package be.vds.jtbdive.client.view.core.logbook;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.view.core.logbook.material.MaterialPanel;
import be.vds.jtbdive.client.view.core.logbook.materialset.MaterialSetPanel;

public class MatCavePanel extends JPanel implements Observer {
	private static final long serialVersionUID = 3440756169067596115L;
	private static final int INDEX_MATERIAL = 0;
	private static final int INDEX_MATERIALSET = 1;
	private LogBookManagerFacade logBookManagerFacade;
	private JTabbedPane tabbedPane;
	private MaterialSetPanel materialSetPanel;
	private MaterialPanel materialPanel;
	private static final I18nResourceManager i18Mgr = I18nResourceManager
			.sharedInstance();

	public MatCavePanel(LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
		logBookManagerFacade.addObserver(this);
		init();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		((JLabel) tabbedPane.getTabComponentAt(INDEX_MATERIAL)).setText(i18Mgr
				.getString("material"));
		((JLabel) tabbedPane.getTabComponentAt(INDEX_MATERIALSET))
				.setText(i18Mgr.getString("materialset"));
	}

	private void init() {
		this.setLayout(new BorderLayout());
		this.setOpaque(true);
		this.setBackground(Color.WHITE);

		tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		JLabel customTab = createTabLabel("material", null, null);
		tabbedPane.addTab(null, createMaterialPanel());
		tabbedPane.setTabComponentAt(INDEX_MATERIAL, customTab);

		customTab = createTabLabel("material.set", null, null);
		tabbedPane.addTab(null, createMaterialSetPanel());
		tabbedPane.setTabComponentAt(INDEX_MATERIALSET, customTab);

		this.add(tabbedPane, BorderLayout.CENTER);
		UnitsAgent.getInstance().addObserver(this);
	}

	private Component createMaterialSetPanel() {
		materialSetPanel = new MaterialSetPanel(logBookManagerFacade);
		return materialSetPanel;
	}

	private JLabel createTabLabel(String text, Icon icon, String tooltip) {
		JLabel label = new JLabel(text);
		label.setHorizontalTextPosition(SwingConstants.RIGHT);
		label.setIcon(icon);
		label.setToolTipText(tooltip);
		return label;
	}

	private Component createMaterialPanel() {
		materialPanel = new MaterialPanel(logBookManagerFacade);
		return materialPanel;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o.equals(UnitsAgent.getInstance())
				&& arg.equals(UnitsAgent.UNITS_CHANGED)) {
			reloadCurrentMaterial();
		}
	}

	private void reloadCurrentMaterial() {
		materialPanel.reloadCurrentMaterial();
	}

	public void synchronizeWithLogBook() {
		materialPanel.synchronizeWithLogBook();
		materialSetPanel.synchronizeWithLogBook();
	}

}
