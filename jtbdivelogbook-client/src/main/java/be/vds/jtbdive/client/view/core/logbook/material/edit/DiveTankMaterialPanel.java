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
package be.vds.jtbdive.client.view.core.logbook.material.edit;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;
import be.vds.jtbdive.core.core.catalogs.DiveTankCompositeMaterial;
import be.vds.jtbdive.core.core.material.DiveTankMaterial;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.logging.Syslog;

public class DiveTankMaterialPanel extends AbstractMaterialPanel {

	private static final long serialVersionUID = -663533444505662759L;
	private static final Syslog LOGGER = Syslog
			.getLogger(DiveTankMaterialPanel.class);

	public DiveTankMaterialPanel() {
		super();
	}

	private JSpinner volumeSpinner;
	private JComboBox compositeCb;
	private JFormattedTextField maxPressureTf;
	private JLabel maxPressureUnitLabel;

	@Override
	protected JComponent createSpecific() {
		JPanel content = new JPanel();
		content.setOpaque(false);
		content.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(5, 5, 5, 5);

		int y = 0;

		GridBagLayoutManager
				.addComponent(content, createVolumeLabel(), gc, 0, y, 1, 1, 0,
						0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(content, createVolumeComponent(), gc,
				1, y++, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(content, createCompositeLabel(), gc,
				0, y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(content, createCompositeComponent(),
				gc, 1, y++, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(content, createMaxPressureLabel(),
				gc, 0, y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(content,
				createMaxPressureComponent(), gc, 1, y++, 1, 1, 1, 0,
				GridBagConstraints.NONE, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(content, Box.createGlue(), gc, 0,
				++y, 2, 1, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.WEST);

		return content;
	}

	private Component createCompositeLabel() {
		return new I18nLabel("composite");
	}

	private Component createCompositeComponent() {
		compositeCb = new JComboBox(DiveTankCompositeMaterial.values());
		compositeCb.setSelectedIndex(-1);
		compositeCb.setRenderer(new KeyedCatalogRenderer());
		return compositeCb;
	}

	private Component createMaxPressureLabel() {
		return new I18nLabel("pressure.max");
	}

	private Component createMaxPressureComponent() {
		maxPressureTf = new JFormattedTextField(NumberFormat.getInstance());
		maxPressureTf.setColumns(10);

		maxPressureUnitLabel = new JLabel("("
				+ UnitsAgent.getInstance().getPressureUnit().getSymbol() + ")");

		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		p.add(maxPressureTf);
		p.add(Box.createHorizontalStrut(5));
		p.add(maxPressureUnitLabel);
		return p;
	}

	private Component createVolumeLabel() {
		return new I18nLabel("volume");
	}

	private Component createVolumeComponent() {
		SpinnerNumberModel model = new SpinnerNumberModel(0d, 0d, 30d, 1d);
		volumeSpinner = new JSpinner(model);

		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		p.add(volumeSpinner);
		p.add(Box.createHorizontalStrut(5));
		p.add(new JLabel("(L)"));
		return p;
	}

	@Override
	public Material getMaterial() {
		DiveTankMaterial mat = new DiveTankMaterial();
		setGeneralFields(mat);
		mat.setVolume((Double) volumeSpinner.getValue());
		mat.setComposite((DiveTankCompositeMaterial) compositeCb
				.getSelectedItem());

		String pressureMax = maxPressureTf.getText();
		if (null != pressureMax && pressureMax.length() > 0) {
			Number barPressure = (Number) maxPressureTf.getValue();
			mat.setMaxPressure(UnitsAgent.getInstance().convertPressureToModel(
					barPressure.doubleValue()));
		}

		return mat;
	}

	@Override
	protected void fillSpecificComponents(Material material) {
		DiveTankMaterial mat = (DiveTankMaterial) material;
		volumeSpinner.setValue(mat.getVolume());
		compositeCb.setSelectedItem(mat.getComposite());
		maxPressureTf.setValue(UnitsAgent.getInstance()
				.convertPressureFromModel(mat.getMaxPressure()));
	}

}
