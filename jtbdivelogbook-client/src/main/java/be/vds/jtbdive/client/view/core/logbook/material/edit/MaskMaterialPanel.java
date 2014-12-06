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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.core.core.material.MaskMaterial;
import be.vds.jtbdive.core.core.material.Material;

public class MaskMaterialPanel extends AbstractMaterialPanel {

	private static final long serialVersionUID = 1224388751828189368L;

	public MaskMaterialPanel() {
		super();
	}

	private JTextField sizeTf;

	@Override
	protected JComponent createSpecific() {
		JPanel content = new JPanel();
		content.setOpaque(false);
		content.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(5, 5, 5, 5);

		int y = 0;
		GridBagLayoutManager.addComponent(content, createSizeLabel(), gc, 0, y,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(content, createSizeComponent(), gc,
				1, y, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(content, Box.createGlue(), gc, 0,
				++y, 2, 1, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.WEST);

		return content;
	}

	private Component createSizeLabel() {
		return new I18nLabel("size");
	}

	private Component createSizeComponent() {
		sizeTf = new JTextField(20);
		return sizeTf;
	}

	@Override
	public Material getMaterial() {
		MaskMaterial mat = new MaskMaterial();
		setGeneralFields(mat);
		mat.setSize(sizeTf.getText());
		return mat;
	}

	@Override
	protected void fillSpecificComponents(Material material) {
		MaskMaterial mat = (MaskMaterial) material;
		sizeTf.setText(mat.getSize());
	}

}
