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
package be.vds.jtbdive.client.view.components;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.Material;

public class MaterialComponent extends JPanel {

	private static final long serialVersionUID = -9074833352660551685L;
	public static final String MATERIAL_CHANGED = "material.changed";
	private JLabel materialLabel;
	private JButton materialChooserButton;
	private LogBookManagerFacade logBookManagerFacade;
	private JButton materialRemoverButton;
	private Material material;
	private MaterialType materialTypeRestriction;

	public MaterialComponent(LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
		init();
	}

	private void init() {
		Dimension d = new Dimension(20, 20);
		materialLabel = new JLabel();

		materialChooserButton = new JButton(
				new AbstractAction(null, UIAgent.getInstance().getIcon(
						UIAgent.ICON_SEARCH_16)) {
					private static final long serialVersionUID = -961785819082164442L;

					@Override
					public void actionPerformed(ActionEvent e) {
						Window w = WindowUtils
								.getTopLevelWindow(MaterialComponent.this);
						MaterialChooserDialog dlg = null;
						if (w instanceof Frame) {
							dlg = new MaterialChooserDialog((Frame) w);
						} else if (w instanceof Dialog) {
							dlg = new MaterialChooserDialog((Dialog) w);
						}

						List<Material> matList = null;

						if (null == materialTypeRestriction) {
							if (logBookManagerFacade.getCurrentMatCave() != null) {
								matList = logBookManagerFacade
										.getCurrentMatCave().getAllMaterials();
							}
						} else {
							if (logBookManagerFacade.getCurrentMatCave() != null) {
								matList = logBookManagerFacade
										.getCurrentMatCave().getMaterials(
												materialTypeRestriction);
							}
						}

						dlg.setMaterial(matList);

						int i = dlg.showDialog(300, 400);
						if (i == MaterialChooserDialog.OPTION_OK) {
							setMaterial(dlg.getSelectedMaterial());
						}
					}
				});

		materialChooserButton.setBorderPainted(false);
		materialChooserButton.setContentAreaFilled(false);
		materialChooserButton.setFocusable(false);
		materialChooserButton.setPreferredSize(d);

		materialRemoverButton = new JButton(
				new AbstractAction(null, UIAgent.getInstance().getIcon(
						UIAgent.ICON_CANCEL_16)) {
					private static final long serialVersionUID = 3141670066203814642L;

					@Override
					public void actionPerformed(ActionEvent e) {
						setMaterial(null);
					}
				});

		materialRemoverButton.setBorderPainted(false);
		materialRemoverButton.setContentAreaFilled(false);
		materialRemoverButton.setFocusable(false);
		materialRemoverButton.setPreferredSize(d);

		this.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		setOpaque(false);

		GridBagLayoutManager.addComponent(this, materialLabel, gc, 0, 0, 1, 1,
				1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, Box.createHorizontalStrut(5),
				gc, 1, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, materialChooserButton, gc, 2,
				0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, materialRemoverButton, gc, 3,
				0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);

	}

	public void setLogBookManagerFacade(
			LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
	}

	public void setMaterial(Material material) {
		Material oldMat = this.material;
		this.material = material;
		adaptMaterialComponents();
		notifyListeners(oldMat, this.material);
	}

	private void notifyListeners(Material oldMat, Material newMat) {
		firePropertyChange(MATERIAL_CHANGED, oldMat, newMat);
	}

	private void adaptMaterialComponents() {
		String s = this.material == null ? null : this.material
				.getShortDescription();
		materialLabel.setText(s);
		materialRemoverButton.setEnabled(this.material != null);
	}

	public void restrictToMaterialType(MaterialType materialType) {
		this.materialTypeRestriction = materialType;
	}

	public Material getMaterial() {
		return material;
	}

	public void setEditable(boolean editable) {
		materialLabel.setEnabled(editable);
		materialChooserButton.setEnabled(editable);
		
		if(editable){
			materialRemoverButton.setEnabled(material ==null?false:true);
		}else{
			materialRemoverButton.setEnabled(false);
		}
		
	}
}
