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
package be.vds.jtbdive.client.view.core.logbook.materialset;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.core.dive.equipment.EquipmentSelectionDialog;
import be.vds.jtbdive.client.view.core.logbook.material.MaterialSelectionDialog;
import be.vds.jtbdive.client.view.core.logbook.material.MaterialTreePanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;

public class MaterialSetEditionPanel extends DetailPanel {

	private static final long serialVersionUID = 433457606918244721L;
	private JTextField nameTf;
	private MaterialSet materialSet;
	private MaterialTreePanel materialTreePanel;
	private LogBookManagerFacade logBookManagerFacade;

	public MaterialSetEditionPanel() {
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		this.add(createNorthPanel(), BorderLayout.NORTH);
		this.add(createCenterPanel(), BorderLayout.CENTER);
	}

	private Component createCenterPanel() {
		materialTreePanel = new MaterialTreePanel();

		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		p.setOpaque(false);
		GridBagLayoutManager.addComponent(p, materialTreePanel, gc, 0, 0, 1, 1,
				1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(p, createEditButton(), gc, 1, 0, 1,
				1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.NORTH);
		return p;
	}

	private Component createEditButton() {
		JButton b = new JButton(new AbstractAction() {

			private static final long serialVersionUID = 4242695041552671992L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Window w = WindowUtils
						.getTopLevelWindow(MaterialSetEditionPanel.this);
				MaterialSelectionDialog dlg = null;
				if (w instanceof Frame) {
					dlg = new MaterialSelectionDialog((Frame) w);
				} else if (w instanceof Dialog) {
					dlg = new MaterialSelectionDialog((Dialog) w);
				}

				dlg.setMaterials(logBookManagerFacade.getCurrentMatCave().getAllMaterials());
				dlg.setSelectedMaterials(materialTreePanel.getMaterials());
				int i = dlg.showDialog();
				if (i == EquipmentSelectionDialog.OPTION_OK) {
					Collection<Material> mats = dlg.getSelectedMaterial();
					materialTreePanel.setMaterials(mats);
				}
			}
		});

		b.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_EDIT_16));
		return b;
	}

	private Component createNorthPanel() {
		nameTf = new JTextField(15);

		DetailPanel dp = new DetailPanel();
		dp.setLayout(new FlowLayout(FlowLayout.LEFT));
		dp.add(new I18nLabel("name"));
		dp.add(nameTf);

		return dp;
	}

	public void setMaterialSet(MaterialSet materialSet) {
		this.materialSet = materialSet;
		if (materialSet != null) {
			fillMaterialSet(materialSet);
		} else {
			reset();
		}
	}

	private void fillMaterialSet(MaterialSet materialSet) {
		nameTf.setText(materialSet.getName());
		materialTreePanel.setMaterials(materialSet.getMaterials());
	}

	private void reset() {
		nameTf.setText(null);
		materialTreePanel.setMaterials(null);
	}

	public MaterialSet getMaterialSet() {
		return materialSet;
	}

	public MaterialSet getDisplayedMaterialSet() {
		MaterialSet ms = new MaterialSet(nameTf.getText());
		if (materialSet != null) {
			ms.setId(materialSet.getId());
		}
		ms.setMaterials(materialTreePanel.getMaterials());
		return ms;
	}

	public void setLogBookManagerFacade(
			LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
	}
}
