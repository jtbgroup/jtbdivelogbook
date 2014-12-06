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
package be.vds.jtbdive.client.view.core.dive.equipment;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.comparator.OrderedCatalogComparator;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;
import be.vds.jtbdive.client.view.utils.MaterialHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.Equipment;
import be.vds.jtbdive.core.core.material.Material;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class EquipmentSelectionDialog extends PromptDialog {

	private static final long serialVersionUID = -1131689189872863790L;
	private JComboBox materialTypeCb;
	private JComboBox materialCb;
	private DefaultComboBoxModel materialListModel;
	private MatCave matCave;
	private List<Material> materialAlreadyPresent;

	public EquipmentSelectionDialog(Frame frame,
			List<Material> materialAlreadyPresent) {
		super(frame, i18n.getString("equipment"), i18n
				.getString("equipment.select.message"));
		this.materialAlreadyPresent = materialAlreadyPresent;
	}

	public EquipmentSelectionDialog(Dialog dialog,
			List<Material> materialAlreadyPresent) {
		super(dialog, "Equipment", "Select an equipment");
		this.materialAlreadyPresent = materialAlreadyPresent;
	}

	public void setMatCave(MatCave matCave) {
		this.matCave = matCave;
	}

	@Override
	protected Component createContentPanel() {
		JPanel p = new DetailPanel(new GridBagLayout());
		p.setOpaque(false);
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(5, 5, 5, 5);
		GridBagLayoutManager
				.addComponent(p, createMaterialTypeLabel(), gc, 0, 0, 1, 1, 0,
						0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createMaterialTypeComponent(), gc,
				1, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createMaterialReferenceLabel(),
				gc, 0, 1, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p,
				createMaterialReferenceComponent(), gc, 1, 1, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, Box.createGlue(), gc, 0, 2, 2, 1,
				1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);

		return p;
	}

	private Component createMaterialTypeLabel() {
		return new I18nLabel("material.type");
	}

	private Component createMaterialTypeComponent() {
		MaterialType[] values = MaterialType.values();
		Arrays.sort(values, new OrderedCatalogComparator());
		materialTypeCb = new JComboBox(values);
		materialTypeCb.setSelectedIndex(-1);
		materialTypeCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setOkButtonEnabled(materialTypeCb.getSelectedIndex() > -1);
				adaptMaterialList();
			}

		});

		materialTypeCb.setRenderer(new KeyedCatalogRenderer());
		return materialTypeCb;
	}

	private void adaptMaterialList() {
		materialListModel.removeAllElements();

		MaterialType matType = (MaterialType) materialTypeCb.getSelectedItem();
		List<Material> materials = matCave.getMaterials(matType);
		if (null != materials) {
			for (Material material : materials) {
				if (materialAlreadyPresent == null
						|| !materialAlreadyPresent.contains(material)) {
					materialListModel.addElement(material);
				}

			}
		}

		materialCb.setSelectedIndex(-1);
	}

	private Component createMaterialReferenceComponent() {
		materialListModel = new DefaultComboBoxModel();
		materialCb = new JComboBox(materialListModel);

		JButton clearMaterialButton = new JButton(
				new AbstractAction(null, UIAgent.getInstance().getIcon(
						UIAgent.ICON_CANCEL_16)) {

					private static final long serialVersionUID = -3696800469303919581L;

					@Override
					public void actionPerformed(ActionEvent e) {
						materialCb.setSelectedIndex(-1);
					}
				});
		clearMaterialButton.setBorderPainted(false);
		clearMaterialButton.setContentAreaFilled(false);
		clearMaterialButton.setPreferredSize(new Dimension(16, 16));

		materialCb.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 8614898889916069912L;

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				if (value != null) {
					setText(((Material) value).getShortDescription());
				} else {
					setText(null);
				}
				return this;
			}
		});

		JPanel p = new JPanel(new BorderLayout(3, 0));
		p.setOpaque(false);
		p.add(materialCb, BorderLayout.CENTER);
		p.add(clearMaterialButton, BorderLayout.EAST);

		return p;
	}

	private Component createMaterialReferenceLabel() {
		return new I18nLabel("material");
	}

	public Equipment getSelectedEquipment() {
		MaterialType mt = (MaterialType) materialTypeCb.getSelectedItem();
		Equipment equipment = MaterialHelper.getEquipmentForMaterialType(mt);

		Material mat = (Material) materialCb.getSelectedItem();
		equipment.setMaterial(mat);

		return equipment;
	}

}
