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

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.utils.MaterialHelper;
import be.vds.jtbdive.core.core.material.Material;

public class EditMaterialDialog extends PromptDialog {

	private static final long serialVersionUID = -1274174535888622989L;
	private JPanel contentPanel;
	private AbstractMaterialPanel currentMatPanel;

	public EditMaterialDialog(Material material) {
		super(i18n.getString("edit"), null);
		setMaterial(material);
	}

	private void setMaterial(Material material) {
		if (null != currentMatPanel)
			contentPanel.remove(currentMatPanel);

		currentMatPanel = MaterialHelper
				.createPanel(material.getMaterialType());
		currentMatPanel.setMaterial(material);
		currentMatPanel.setEnabled(true);

		contentPanel.add(currentMatPanel, BorderLayout.CENTER);
	}

	@Override
	protected Component createContentPanel() {
		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setOpaque(false);
		return contentPanel;
	}

	public Material getEditedMaterial() {
		if (currentMatPanel == null)
			return null;

		return currentMatPanel.getMaterial();
	}


}
