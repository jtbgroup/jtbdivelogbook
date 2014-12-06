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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.swing.component.ExpandablePanel;
import be.vds.jtbdive.client.view.components.MaterialComponent;
import be.vds.jtbdive.client.view.core.dive.EquipmentParameterPanel;
import be.vds.jtbdive.client.view.events.ModificationListenable;
import be.vds.jtbdive.client.view.events.ModificationListener;
import be.vds.jtbdive.client.view.utils.MaterialHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.DiveEquipment;
import be.vds.jtbdive.core.core.material.Equipment;
import be.vds.jtbdive.core.core.material.Material;

public abstract class EquipmentPanel extends ExpandablePanel implements
		ModificationListenable {

	private static final long serialVersionUID = 4141943449195347840L;
	protected Equipment equipment;
	private EquipmentParameterPanel equipmentParameterPanel;
	private JLabel orderIndexLabel;
	private JButton indexDownButton;
	protected LogBookManagerFacade logBookManagerFacade;
	private JLabel materialTypeIcon;
	private MaterialComponent materialComponent;

	public EquipmentPanel(LogBookManagerFacade logBookManagerFacade,
			DiveEquipment diveEquipment) {
		super();
		initComponents(logBookManagerFacade, diveEquipment);
	}

	private void initComponents(LogBookManagerFacade logBookManagerFacade,
			DiveEquipment diveEquipment) {
		this.logBookManagerFacade = logBookManagerFacade;
		materialComponent.setLogBookManagerFacade(logBookManagerFacade);
	}

	protected Component createHeaderPanel() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));

		materialTypeIcon = new JLabel();
		p.add(materialTypeIcon);
		p.add(Box.createHorizontalGlue());
		p.add(createOrderIndexPanel());
		p.add(Box.createHorizontalStrut(10));
		p.add(createRemoveButton());

		// JPanel bp = new JPanel(new BorderLayout());
		// bp.setOpaque(false);
		// bp.add(p, BorderLayout.NORTH);
		// bp.add(createMaterialPanel(), BorderLayout.SOUTH);
		return p;
	}

	private Component createMaterialPanel() {
		materialComponent = new MaterialComponent(logBookManagerFacade);
		materialComponent
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals(
								MaterialComponent.MATERIAL_CHANGED)) {
							equipment.setMaterial((Material) evt.getNewValue());
							notifyModificationListeners(true);
						}
					}
				});
		
		JPanel p = new DetailPanel(new BorderLayout());
		p.add(materialComponent, BorderLayout.CENTER);

		return p;
	}

	private Component createOrderIndexPanel() {
		JButton indexUpButton = new JButton(new AbstractAction() {

			private static final long serialVersionUID = -8428002660450007749L;

			@Override
			public void actionPerformed(ActionEvent e) {
				equipmentParameterPanel.incrementOrderIndex(equipment);
			}
		});
		indexUpButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_TRIANGLE_UP_16));

		indexDownButton = new JButton(new AbstractAction() {

			private static final long serialVersionUID = -8428002660450007749L;

			@Override
			public void actionPerformed(ActionEvent e) {
				equipmentParameterPanel.decrementOrderIndex(equipment);
			}
		});
		indexDownButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_TRIANGLE_DOWN_16));

		Dimension d = new Dimension(20, 20);
		indexUpButton.setPreferredSize(d);
		indexUpButton.setMaximumSize(d);
		indexDownButton.setPreferredSize(d);
		indexDownButton.setMaximumSize(d);

		indexUpButton.setBorderPainted(false);
		indexUpButton.setContentAreaFilled(false);
		indexUpButton.setFocusPainted(false);

		indexDownButton.setBorderPainted(false);
		indexDownButton.setContentAreaFilled(false);
		indexDownButton.setFocusPainted(false);

		orderIndexLabel = new JLabel();

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
		p.setOpaque(false);
		p.add(indexDownButton);
		p.add(Box.createHorizontalStrut(5));
		p.add(orderIndexLabel);
		p.add(Box.createHorizontalStrut(5));
		p.add(indexUpButton);

		return p;
	}

	private Component createRemoveButton() {
		JButton removeButton = new JButton(new AbstractAction() {

			private static final long serialVersionUID = -8428002660450007749L;

			@Override
			public void actionPerformed(ActionEvent e) {
				equipmentParameterPanel.removeEquipment(equipment);
			}
		});
		removeButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_CANCEL_16));
		removeButton.setBorderPainted(false);
		removeButton.setContentAreaFilled(false);
		return removeButton;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
		orderIndexLabel.setText("# " + equipment.getOrderIndex());
		indexDownButton.setEnabled(equipment.getOrderIndex() > 0);
		commentTa.setText(equipment.getComment());
		adaptMaterialTypeIcon();
		materialComponent.setMaterial(equipment.getMaterial());
		if (equipment != null) {
			materialComponent.restrictToMaterialType(equipment
					.getMaterialType());
		}
		setEquipmentComponents();
	}

	private void adaptMaterialTypeIcon() {
		materialTypeIcon.setIcon(MaterialHelper.getMaterialIcon(getEquipment()
				.getMaterialType(), 16));
	}

	protected abstract void setEquipmentComponents();

	public void setParamPanel(EquipmentParameterPanel equipmentParameterPanel) {
		this.equipmentParameterPanel = equipmentParameterPanel;
	}

	// ////////////////Modification listener part
	private Set<ModificationListener> modificationListeners = new HashSet<ModificationListener>();
	private boolean activateNotification = true;
	private JTextArea commentTa;

	public void addModificationListener(
			ModificationListener modificationListener) {
		modificationListeners.add(modificationListener);
	}

	public void removeModificationListener(
			ModificationListener modificationListener) {
		modificationListeners.remove(modificationListener);
	}

	public void removeAllModificationListener() {
		modificationListeners.removeAll(modificationListeners);
	}

	protected void notifyModificationListeners(boolean isModified) {
		if (activateNotification) {
			for (ModificationListener listener : modificationListeners) {
				listener.isModified(this, isModified);
			}
		}
	}

	public void activateNotification(boolean b) {
		activateNotification = b;
	}

	public void updateUnits() {
	}

	@Override
	protected Component createGlobalCentralPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(false);

		p.add(createCommonPanel(), BorderLayout.NORTH);
		p.add(createCentralPanel(), BorderLayout.CENTER);
		// p.add(createComment(), BorderLayout.SOUTH);

		// GridBagConstraints gc = new GridBagConstraints();
		// gc.insets = new Insets(5, 5, 5, 10);
		// GridBagLayoutManager.addComponent(p, createCentralPanel(), gc, 0, 0,
		// 2, 1, 1, 0, GridBagConstraints.HORIZONTAL,
		// GridBagConstraints.CENTER);
		//
		// GridBagLayoutManager.addComponent(p, new I18nLabel("comment"), gc, 0,
		// 1, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		// GridBagLayoutManager.addComponent(p, scroll, gc, 1, 1, 1, 1, 1, 0,
		// GridBagConstraints.NONE, GridBagConstraints.WEST);
		return p;
	}

	private Component createCommonPanel() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setOpaque(false);
		GridBagConstraints gc = new GridBagConstraints();
		// gc.insets = new Insets(5, 5, 5, 10);
		GridBagLayoutManager.addComponent(p, new I18nLabel("material"), gc, 0,
				0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.NORTHEAST);

		GridBagLayoutManager.addComponent(p, createMaterialPanel(), gc, 1, 0,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.NORTHWEST);


		GridBagLayoutManager.addComponent(p, new I18nLabel("comment"), gc, 0,
				1, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.NORTHEAST);

		GridBagLayoutManager.addComponent(p, createComment(), gc, 1, 1, 1, 1,
				1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
		return p;
	}

	protected abstract Component createCentralPanel();

	private Component createComment() {
		commentTa = new JTextArea();
		commentTa.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String s = commentTa.getText();
				equipment.setComment(s);
				notifyModificationListeners(true);
			}
		});
		commentTa.setLineWrap(false);
		commentTa.setWrapStyleWord(true);
		commentTa.setRows(2);

		JScrollPane scroll = new JScrollPane(commentTa);
//		scroll.setPreferredSize(new Dimension(250, 100));

		return scroll;
	}

	public void refreshForNewDiveTime() {
	}
}
