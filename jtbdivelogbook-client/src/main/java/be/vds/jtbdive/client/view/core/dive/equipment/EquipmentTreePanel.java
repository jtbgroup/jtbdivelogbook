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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.material.Equipment;

public class EquipmentTreePanel extends DetailPanel {
	private static final long serialVersionUID = -1272164829412136704L;
	private EquipmentTree tree;
	private JButton collapseButton;
	private JButton expandButton;

	public EquipmentTreePanel() {
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.add(createNorthPanel(), BorderLayout.NORTH);
		this.add(createTree(), BorderLayout.CENTER);
	}

	private Component createNorthPanel() {
		JPanel buttonHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
		buttonHeader.setOpaque(false);

		Dimension d = new Dimension(18, 18);

		// // COLLAPSE
		collapseButton = new JButton(new AbstractAction(null, UIAgent
				.getInstance().getIcon(UIAgent.ICON_BTN_COLLAPSE_ALL_16)) {

			private static final long serialVersionUID = 7592442717029155431L;

			@Override
			public void actionPerformed(ActionEvent e) {
				collapseTree();
			}
		});
		collapseButton.setBorderPainted(false);
		collapseButton.setContentAreaFilled(false);
		collapseButton.setText(null);
		collapseButton.setPreferredSize(d);
		collapseButton.setEnabled(false);
		buttonHeader.add(collapseButton);

		// // EXPAND
		expandButton = new JButton(new AbstractAction(null, UIAgent
				.getInstance().getIcon(UIAgent.ICON_BTN_EXPAND_ALL_16)) {

			private static final long serialVersionUID = 1990893338485891500L;

			@Override
			public void actionPerformed(ActionEvent e) {
				expandTree();
			}
		});
		expandButton.setBorderPainted(false);
		expandButton.setContentAreaFilled(false);
		expandButton.setText(null);
		expandButton.setPreferredSize(d);
		expandButton.setEnabled(false);
		buttonHeader.add(expandButton);
		return buttonHeader;
	}

	private Component createTree() {
		tree = new EquipmentTree();
		JScrollPane scroll = new JScrollPane(tree);
		return scroll;
	}

	public void addTreeSelectionListener(
			TreeSelectionListener treeSelectionListener) {
		tree.addTreeSelectionListener(treeSelectionListener);
	}

	public List<Equipment> getSelectedEquipment() {
		return tree.getSelectedEquipments();
	}

	public void removeEquipment(Equipment equipment) {
		tree.removeEquipment(equipment);
		adaptExpandButtons(tree.hasElements());
	}

	public void clear() {
		tree.clear();
		adaptExpandButtons(tree.hasElements());
	}

	private void adaptExpandButtons(boolean b) {
		expandButton.setEnabled(b);
		collapseButton.setEnabled(b);
	}

	public void addEquipment(Equipment equipment, boolean scrollPathToVisible) {
		tree.addEquipment(equipment, scrollPathToVisible);
		adaptExpandButtons(tree.hasElements());
	}

	public void expandTree() {
		tree.expandAll();
	}

	public void collapseTree() {
		tree.collapseAll();
	}

	public DefaultMutableTreeNode getSelectedNode() {
		return (DefaultMutableTreeNode) tree.getLeadSelectionPath()
				.getLastPathComponent();
	}


	public void addEquipments(List<Equipment> equipments) {
		for (Equipment equipment : equipments) {
			addEquipment(equipment, false);
		}
	}

	public void removeEquipments(List<Equipment> equipments) {
		for (Equipment equipment : equipments) {
			removeEquipment(equipment);
		}
	}

}
