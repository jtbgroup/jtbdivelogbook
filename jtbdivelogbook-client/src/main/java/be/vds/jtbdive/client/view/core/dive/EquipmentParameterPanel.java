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
package be.vds.jtbdive.client.view.core.dive;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.VerticalLayout;

import be.smd.i18n.swing.I18nButton;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.core.dive.equipment.DefaultEquipmentPanel;
import be.vds.jtbdive.client.view.core.dive.equipment.DiveComputerEquipmentPanel;
import be.vds.jtbdive.client.view.core.dive.equipment.EquipmentPanel;
import be.vds.jtbdive.client.view.core.dive.equipment.EquipmentSelectionDialog;
import be.vds.jtbdive.client.view.core.dive.equipment.EquipmentTreePanel;
import be.vds.jtbdive.client.view.core.dive.equipment.WeightBeltEquipmentPanel;
import be.vds.jtbdive.client.view.core.dive.equipment.divetanks.DiveTankPanel;
import be.vds.jtbdive.client.view.core.logbook.material.MaterialSelectionDialog;
import be.vds.jtbdive.client.view.events.ModificationListener;
import be.vds.jtbdive.client.view.utils.MaterialHelper;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveEquipment;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.Equipment;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.logging.Syslog;

public class EquipmentParameterPanel extends JPanel implements
		ModificationListener {

	private static final long serialVersionUID = -4745544440866177775L;
	private JPanel equipmentsPanel;
	private JScrollPane equipmentsScroll;
	private DiveEquipment diveEquipment;
	private Dive currentDive;
	private LogBookManagerFacade logBookManagerFacade;
	private List<EquipmentPanel> displayedEquipmentPanels = new ArrayList<EquipmentPanel>();
	private EquipmentTreePanel equipmentTreePanel;
	private I18nButton removeEquipmentsButton;
	private static final Syslog LOGGER = Syslog
			.getLogger(EquipmentParameterPanel.class);

	public EquipmentParameterPanel(
			LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
		init();
	}

	private void init() {

		this.setLayout(new BorderLayout());
		this.setOpaque(true);
		this.setBackground(Color.WHITE);

		this.add(createButtonsPanel(), BorderLayout.NORTH);
		this.add(createCentralPanel(), BorderLayout.CENTER);
	}

	private Component createCentralPanel() {
		JSplitPane split = new JSplitPane();
		split.setOpaque(false);
		split.setDividerLocation(200);
		split.setLeftComponent(createEquipmentTree());
		split.setRightComponent(createEquipmentScrollPanel());
		return split;
	}

	private Component createEquipmentTree() {
		equipmentTreePanel = new EquipmentTreePanel();
		equipmentTreePanel
				.addTreeSelectionListener(createTreeSelectionListener());
		return equipmentTreePanel;
	}

	private TreeSelectionListener createTreeSelectionListener() {
		return new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				if (e.getNewLeadSelectionPath() != null) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
							.getNewLeadSelectionPath().getLastPathComponent();
					showTreeSelection(node);
					removeEquipmentsButton.setEnabled(true);
				} else {
					removeEquipmentsButton.setEnabled(false);
				}
			}
		};
	}

	private void showTreeSelection(DefaultMutableTreeNode node) {
		if (null != node) {
			Object o = node.getUserObject();
			if (o instanceof Equipment) {
				displayEquipment((Equipment) o);
			} else {
				displayEquipmentList((MaterialType) o);
			}
		}
	}

	private JComponent createEquipmentScrollPanel() {
		equipmentsPanel = new JPanel();
		equipmentsPanel.setOpaque(false);
		equipmentsPanel.setLayout(new VerticalLayout(10));
		equipmentsScroll = new JScrollPane(equipmentsPanel);
		SwingComponentHelper.displayJScrollPane(equipmentsScroll);
		return equipmentsScroll;
	}

	private void displayEquipment(Equipment equipment) {
		equipmentsPanel.removeAll();
		displayedEquipmentPanels.clear();
		EquipmentPanel equipmentPanel = createEquipmentPanel(equipment);
		addEquipmentPanel(equipmentPanel, equipment);
	}

	private EquipmentPanel createEquipmentPanel(Equipment equipment) {
		switch (equipment.getMaterialType()) {
		case DIVE_COMPUTER:
			return new DiveComputerEquipmentPanel(logBookManagerFacade,
					diveEquipment);
		case DIVE_TANK:
			return new DiveTankPanel(logBookManagerFacade,
					diveEquipment, currentDive);
		case WEIGHT_BELT:
			return new WeightBeltEquipmentPanel(logBookManagerFacade,
					diveEquipment);
		default:
			return new DefaultEquipmentPanel(logBookManagerFacade,
					diveEquipment);
		}
	}

	private void displayEquipmentList(MaterialType materialType) {
		equipmentsPanel.removeAll();
		displayedEquipmentPanels.clear();
		List<Equipment> list = diveEquipment.getEquipments(materialType);
		for (Equipment equipment : list) {
			EquipmentPanel equipmentPanel = createEquipmentPanel(equipment);
			addEquipmentPanel(equipmentPanel, equipment);
		}
	}

	private void addEquipmentPanel(EquipmentPanel wmp, Equipment equipment) {
		wmp.setParamPanel(this);
		wmp.setEquipment(equipment);
		addMaterialPanel(wmp);
	}

	private Component createButtonsPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setOpaque(false);

		panel.add(createAddEquipmentButton());
		panel.add(createAddMaterialsButton());
		panel.add(createRemoveEquipmentButton());

		return panel;
	}

	private Component createRemoveEquipmentButton() {
		removeEquipmentsButton = new I18nButton(new AbstractAction() {
			private static final long serialVersionUID = -5896458776360210550L;

			@Override
			public void actionPerformed(ActionEvent e) {
				List<Equipment> equipments = equipmentTreePanel
						.getSelectedEquipment();

				removeEquipments(equipments);
			}
		});
		removeEquipmentsButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_CANCEL_16));
		removeEquipmentsButton.setTooltipTextBundleKey("delete");
		removeEquipmentsButton.setEnabled(false);
		return removeEquipmentsButton;
	}

	private Component createAddMaterialsButton() {
		I18nButton addSerieButton = new I18nButton(new AbstractAction() {

			private static final long serialVersionUID = 5952780091596730460L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Window w = WindowUtils
						.getTopLevelWindow(EquipmentParameterPanel.this);
				MaterialSelectionDialog dlg = null;
				if (w instanceof Frame) {
					dlg = new MaterialSelectionDialog((Frame) w);
				} else if (w instanceof Dialog) {
					dlg = new MaterialSelectionDialog((Dialog) w);
				}

				MatCave cave = logBookManagerFacade.getCurrentLogBook()
						.getMatCave();
				dlg.setMaterials(cave.getAllMaterials());
				dlg.setMaterialSets(cave.getMaterialSets());

				int i = dlg.showDialog();
				if (i == EquipmentSelectionDialog.OPTION_OK) {
					Collection<Material> mats = dlg.getSelectedMaterial();
					for (Material material : mats) {
						Equipment equipment = MaterialHelper
								.getEquipmentForMaterialType(material
										.getMaterialType());
						if (diveEquipment == null) {
							equipment.setOrderIndex(0);
						} else {
							equipment.setOrderIndex(diveEquipment
									.getMaxIndexForEquipment(equipment
											.getMaterialType()) + 1);
						}
						equipment.setMaterial(material);

						getDiveEquipmentSafe().addEquipment(equipment);
						equipmentTreePanel.addEquipment(equipment, true);
						logBookManagerFacade.setDiveChanged(currentDive);
					}
				}
			}
		});
		addSerieButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_MATCAVE_16));
		addSerieButton.setTooltipTextBundleKey("tooltip.equipment.add.serie");
		return addSerieButton;
	}

	private DiveEquipment getDiveEquipmentSafe() {
		if (diveEquipment == null) {
			diveEquipment = new DiveEquipment();
			currentDive.setDiveEquipment(diveEquipment);
		}
		return diveEquipment;
	}

	private Component createAddEquipmentButton() {
		I18nButton addEquipmentButton = new I18nButton(new AbstractAction() {

			private static final long serialVersionUID = 5952780091596730460L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Window w = WindowUtils
						.getTopLevelWindow(EquipmentParameterPanel.this);
				EquipmentSelectionDialog dlg = null;
				List<Material> matList = null;
				if (null != diveEquipment) {
					matList = diveEquipment.getMaterialPresent();
				}

				if (w instanceof Frame) {
					dlg = new EquipmentSelectionDialog((Frame) w, matList);
				} else if (w instanceof Dialog) {
					dlg = new EquipmentSelectionDialog((Dialog) w, matList);
				}

				dlg.setMatCave(logBookManagerFacade.getCurrentLogBook()
						.getMatCave());
				WindowUtils.centerWindow(dlg);
				int i = dlg.showDialog(true);
				if (i == EquipmentSelectionDialog.OPTION_OK) {

					Equipment equipment = dlg.getSelectedEquipment();
					if (diveEquipment == null) {
						equipment.setOrderIndex(0);
					} else {
						equipment.setOrderIndex(diveEquipment
								.getMaxIndexForEquipment(equipment
										.getMaterialType()) + 1);
					}
					getDiveEquipmentSafe().addEquipment(equipment);
					equipmentTreePanel.addEquipment(equipment, true);
					logBookManagerFacade.setDiveChanged(currentDive);
				}
			}
		});
		addEquipmentButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_ADD_16));
		addEquipmentButton.setTooltipTextBundleKey("tooltip.equipment.add");
		return addEquipmentButton;
	}

	public void clear() {
		equipmentsPanel.removeAll();
		displayedEquipmentPanels.clear();
		equipmentTreePanel.clear();
		this.repaint();
	}

	public void setDiveEquipment(DiveEquipment diveEquipment, Dive currentDive) {

		this.diveEquipment = diveEquipment;
		this.currentDive = currentDive;
		clear();

		if (null != diveEquipment) {

			for (Equipment equipment : diveEquipment.getAllEquipments()) {
				equipmentTreePanel.addEquipment(equipment, false);
			}
		}
	}

	private void addMaterialPanel(EquipmentPanel materialPanel) {
		materialPanel.addModificationListener(this);

		equipmentsPanel.add(materialPanel);
		displayedEquipmentPanels.add(materialPanel);
		equipmentsScroll.validate();
		equipmentsScroll.repaint();
	}

	@Override
	public void isModified(JComponent component, boolean isModified) {
		logBookManagerFacade.setDiveChanged(currentDive);
		LOGGER.debug("equipment modified is "
				+ ((EquipmentPanel) component).getEquipment().getClass()
						.getName());
	}

	public void removeEquipment(Equipment equipment) {
		diveEquipment.removeEquipment(equipment);
		logBookManagerFacade.setDiveChanged(currentDive);
		setDiveEquipment(diveEquipment, currentDive);
		this.repaint();
		this.revalidate();
	}

	public void removeEquipments(List<Equipment> equipments) {
		diveEquipment.removeEquipments(equipments);
		logBookManagerFacade.setDiveChanged(currentDive);
		setDiveEquipment(diveEquipment, currentDive);
		this.repaint();
		this.revalidate();
	}

	void updateUnits() {
		for (EquipmentPanel equipmentPanel : displayedEquipmentPanels) {
			equipmentPanel.updateUnits();
		}
	}

	public void decrementOrderIndex(Equipment equipment) {
		if (equipment.getOrderIndex() > 0) {
			diveEquipment.changeEquipmentOrderIndex(equipment,
					equipment.getOrderIndex() - 1);
			logBookManagerFacade.setDiveChanged(currentDive);
			showTreeSelection(equipmentTreePanel.getSelectedNode());
		}
	}

	public void incrementOrderIndex(Equipment equipment) {
		diveEquipment.changeEquipmentOrderIndex(equipment,
				equipment.getOrderIndex() + 1);
		logBookManagerFacade.setDiveChanged(currentDive);
		showTreeSelection(equipmentTreePanel.getSelectedNode());
	}

	void refreshForNewDiveTime() {
		for (EquipmentPanel equipmentPanel : displayedEquipmentPanels) {
			equipmentPanel.refreshForNewDiveTime();
		}
	}
}
