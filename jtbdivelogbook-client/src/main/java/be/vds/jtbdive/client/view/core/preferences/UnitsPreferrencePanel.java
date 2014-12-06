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
package be.vds.jtbdive.client.view.core.preferences;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;
import be.vds.jtbdive.core.core.units.LengthUnit;
import be.vds.jtbdive.core.core.units.PressureUnit;
import be.vds.jtbdive.core.core.units.TemperatureUnit;
import be.vds.jtbdive.core.core.units.WeightUnit;

public class UnitsPreferrencePanel extends AbstractPreferrencePanel {

	private static final long serialVersionUID = -4753316582141576156L;
	private JComboBox temperatureCb;
	private JComboBox lengthCb;
	private JComboBox weightCb;
	private JComboBox pressureCb;

	public UnitsPreferrencePanel() {
		super();
	}

	@Override
	protected Component createContentPanel() {
		JPanel p = new JPanel();
		p.setOpaque(false);

		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		int y = 0;

		GridBagLayoutManager.addComponent(p, createTemperatureLabel(), c, 0, y,
				1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createTemperatureComponent(), c,
				1, y++, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createLengthLabel(), c, 0, y, 1,
				1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createLengthComponent(), c, 1,
				y++, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createPressureLabel(), c, 0, y, 1,
				1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createPressureComponent(), c, 1,
				y++, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createWeightLabel(), c, 0, y, 1,
				1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createWeightComponent(), c, 1,
				y++, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, Box.createVerticalGlue(), c, 0, y,
				1, 1, 0, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

		return p;
	}

	private Component createPressureComponent() {
		pressureCb = new JComboBox(PressureUnit.values());
		pressureCb.setRenderer(new KeyedCatalogRenderer());
		pressureCb.setSelectedItem(PressureUnit.BAR);
		return pressureCb;
	}

	private Component createPressureLabel() {
		return new I18nLabel("pressure");
	}

	private Component createWeightComponent() {
		weightCb = new JComboBox(WeightUnit.values());
		weightCb.setRenderer(new KeyedCatalogRenderer());
		weightCb.setSelectedItem(WeightUnit.KILOGRAM);
		return weightCb;
	}

	private Component createWeightLabel() {
		return new I18nLabel("weight");
	}

	private Component createTemperatureComponent() {
		temperatureCb = new JComboBox(TemperatureUnit.values());
		temperatureCb.setRenderer(new KeyedCatalogRenderer());
		temperatureCb.setSelectedItem(TemperatureUnit.CELSIUS);
		return temperatureCb;
	}

	private Component createTemperatureLabel() {
		return new I18nLabel("temperature");
	}

	private Component createLengthComponent() {
		lengthCb = new JComboBox(LengthUnit.values());
		lengthCb.setRenderer(new KeyedCatalogRenderer());
		lengthCb.setSelectedItem(LengthUnit.METER);
		return lengthCb;
	}

	private Component createLengthLabel() {
		return new I18nLabel("length");
	}

	@Override
	public void adaptUserPreferences() {
		UserPreferences.getInstance().setPreferredTemperatureUnit(
				(TemperatureUnit) temperatureCb.getSelectedItem());
		UserPreferences.getInstance().setPreferredLengthUnit(
				(LengthUnit) lengthCb.getSelectedItem());
		UserPreferences.getInstance().setPreferredPressureUnit(
				(PressureUnit) pressureCb.getSelectedItem());
		UserPreferences.getInstance().setPreferredWeightUnit(
				(WeightUnit) weightCb.getSelectedItem());
	}

	@Override
	public void setUserPreferences() {
		if (null != UserPreferences.getInstance().getPreferredTemperatureUnit()) {
			temperatureCb.setSelectedItem(UserPreferences.getInstance()
					.getPreferredTemperatureUnit());
		}
		if (null != UserPreferences.getInstance().getPreferredLengthUnit()) {
			lengthCb.setSelectedItem(UserPreferences.getInstance()
					.getPreferredLengthUnit());
		}

		if (null != UserPreferences.getInstance().getPreferredWeightUnit()) {
			weightCb.setSelectedItem(UserPreferences.getInstance()
					.getPreferredWeightUnit());
		}

		if (null != UserPreferences.getInstance().getPreferredPressureUnit()) {
			pressureCb.setSelectedItem(UserPreferences.getInstance()
					.getPreferredPressureUnit());
		}
	}
}
