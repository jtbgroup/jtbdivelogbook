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
package be.vds.jtbdive.client.view.wizard.stats;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.comparator.KeyedCatalogComparator;
import be.vds.jtbdive.client.core.stats.StatQueryObject;
import be.vds.jtbdive.client.core.stats.StatXAxis;
import be.vds.jtbdive.client.core.stats.axisy.StatYAxis;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;
import be.vds.jtbdive.client.view.wizard.WizardPanel;

public class StatAxisPanel extends WizardPanel {
	private static final long serialVersionUID = -3160653150799125002L;
	private JComboBox xAxisCb;
	private JComboBox yAxisCb;
	private StatQueryObject currentStatQueryObject;
	private DefaultComboBoxModel yAxisCbModel;

	@Override
	public String getMessage() {
		return i18n.getString("wizard.stat.message.variables.choose");
	}

	@Override
	public JComponent createContentPanel() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		GridBagLayoutManager.addComponent(p, new I18nLabel("axis.x"), gc, 0, 0,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.EAST);
		GridBagLayoutManager.addComponent(p, createXAxisCb(), gc, 1, 0, 1, 1,
				1, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, new I18nLabel("axis.y"), gc, 0, 1,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.EAST);
		GridBagLayoutManager.addComponent(p, createYAxisCb(), gc, 1, 1, 1, 1,
				1, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, Box.createGlue(), gc, 0, 2, 2, 1,
				1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);

		return p;

	}

	private Component createYAxisCb() {
		yAxisCbModel = new DefaultComboBoxModel();
		yAxisCb = new JComboBox(yAxisCbModel);
		yAxisCb.setRenderer(new KeyedCatalogRenderer());
		yAxisCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				StatYAxis axis = (StatYAxis) yAxisCb.getSelectedItem();
				if (null == axis) {
					currentStatQueryObject.setStatYAxisParams(null);
				} else {
					currentStatQueryObject.setStatYAxisParams(StatQueryObject
							.createStatYAxisParam(axis));
				}
			}
		});
		return yAxisCb;
	}

	private Component createXAxisCb() {
		StatXAxis[] objs = StatXAxis.values();
		Arrays.sort(objs, new KeyedCatalogComparator());
		xAxisCb = new JComboBox(objs);
		xAxisCb.setSelectedItem(null);
		xAxisCb.setRenderer(new KeyedCatalogRenderer());
		xAxisCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				currentStatQueryObject = StatQueryObject
						.createStatQueryObject((StatXAxis) xAxisCb
								.getSelectedItem());
				refillYAxisCb();
				StatAxisPanel.this.firePropertyChange("stat.query", null, null);
			}
		});
		return xAxisCb;
	}

	private void refillYAxisCb() {
		yAxisCbModel.removeAllElements();
		if (null != currentStatQueryObject) {
			StatYAxis[] objs = currentStatQueryObject.getPossibleStatYAxis();
			Arrays.sort(objs, new KeyedCatalogComparator());
			for (StatYAxis statYAxis : objs) {
				yAxisCbModel.addElement(statYAxis);
			}
		}
	}


	public StatQueryObject getStatQueryObject() {
		return currentStatQueryObject;
	}
}
