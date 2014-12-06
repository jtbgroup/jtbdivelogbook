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
package be.vds.jtbdive.client.view.core.divesite;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.comparator.KeyedCatalogComparator;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.catalogs.DiveSiteType;

public class DiveSiteGeneralPanel extends JPanel {

	private static final long serialVersionUID = 5226692925877723038L;
	private JTextField nameTextField;
	private JSpinner depthSpinner;
	private JComboBox diveSiteTypeComboBox;
	private JTextField webSiteTextField;

	public DiveSiteGeneralPanel() {
		init();
	}

	private void init() {
		setLayout(new GridBagLayout());
		setOpaque(false);
		int y = 0;
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		GridBagLayoutManager.addComponent(this, createNameLabel(), c, 0, y, 1,
				1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.FIRST_LINE_END);
		GridBagLayoutManager.addComponent(this, createNameTextField(), c, 1, y,
				3, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.FIRST_LINE_START);

		GridBagLayoutManager.addComponent(this, createDepthLabel(), c, 0, ++y,
				1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.FIRST_LINE_END);
		GridBagLayoutManager.addComponent(this, createDepthTextField(), c, 1,
				y, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.FIRST_LINE_START);

		GridBagLayoutManager.addComponent(this, createDiveSiteTypeLabel(), c,
				0, ++y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.FIRST_LINE_END);
		GridBagLayoutManager.addComponent(this, createDiveSiteTypeComponent(),
				c, 1, y, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.FIRST_LINE_START);

		GridBagLayoutManager.addComponent(this, new I18nLabel("website"), c, 0,
				++y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.FIRST_LINE_END);
		GridBagLayoutManager.addComponent(this, createWebSiteComponent(), c, 1,
				y, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.FIRST_LINE_START);

		GridBagLayoutManager.addComponent(this, Box.createVerticalGlue(), c, 0,
				++y, 2, 1, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);

	}

	private Component createNameLabel() {
		return new I18nLabel("name");
	}

	private Component createWebSiteComponent() {
		webSiteTextField = new JTextField(20);
		return webSiteTextField;
	}

	private Component createNameTextField() {
		nameTextField = new JTextField(20);
		return nameTextField;
	}

	private Component createDepthLabel() {
		return new I18nLabel("depth");
	}

	private Component createDepthTextField() {
		SpinnerNumberModel sm = new SpinnerNumberModel(0, 0, 1000, 0.1);
		depthSpinner = new JSpinner(sm);
		return depthSpinner;
	}

	private Component createDiveSiteTypeLabel() {
		return new I18nLabel("type");
	}

	private Component createDiveSiteTypeComponent() {
		DiveSiteType[] values = DiveSiteType.values();
		Arrays.sort(values, new KeyedCatalogComparator());
		diveSiteTypeComboBox = new JComboBox(values);
		diveSiteTypeComboBox.setSelectedIndex(-1);
		diveSiteTypeComboBox.setRenderer(new KeyedCatalogRenderer());
		return diveSiteTypeComboBox;
	}

	public void reset() {
		nameTextField.setText(null);
		depthSpinner.setValue(0);
		diveSiteTypeComboBox.setSelectedIndex(-1);
		webSiteTextField.setText(null);
	}

	public void setValue(DiveSite diveSite) {
		nameTextField.setText(diveSite.getName());
		depthSpinner.setValue(diveSite.getDepth());
		diveSiteTypeComboBox.setSelectedItem(diveSite.getDiveSiteType());
		webSiteTextField.setText(diveSite.getInternetSite());
	}

	public String getDiveSiteName() {
		return nameTextField.getText();
	}

	public Double getDiveSiteDepth() {
		return (Double) depthSpinner.getValue();
	}

	public void setEditable(boolean b) {
		nameTextField.setEditable(b);
		depthSpinner.setEnabled(b);
		diveSiteTypeComboBox.setEnabled(b);
		webSiteTextField.setEditable(b);
	}

	public DiveSiteType getDiveSiteType() {
		return (DiveSiteType) diveSiteTypeComboBox.getSelectedItem();
	}

	public String getWebSite() {
		return webSiteTextField.getText();
	}
}
