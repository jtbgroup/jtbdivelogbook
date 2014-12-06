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
package be.vds.jtbdive.client.view.core.logbook.material;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.material.Material;

public class MaterialTreePanel extends DetailPanel implements
		MaterialSelectable {
	private static final long serialVersionUID = -1272164829412136704L;
	private MaterialTree materialTree;
	private JButton collapseButton;
	private JButton expandButton;

	public MaterialTreePanel() {
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

		// COLLAPSE
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

		// EXPAND
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
		materialTree = new MaterialTree();
		JScrollPane scroll = new JScrollPane(materialTree);
		scroll.setPreferredSize(new Dimension(60, 100));
		SwingComponentHelper.displayJScrollPane(scroll);
		return scroll;
	}

	public void addTreeSelectionListener(
			TreeSelectionListener treeSelectionListener) {
		materialTree.addTreeSelectionListener(treeSelectionListener);
	}

	public Material getSelectedMaterial() {
		TreePath path = materialTree.getSelectionPath();
		if (null == path)
			return null;

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		if (node != null && node.getUserObject() instanceof Material) {
			return (Material) node.getUserObject();
		}
		return null;
	}

	public void removeMaterial(Material material) {
		materialTree.removeMaterial(material);
		adaptExpandButtons(materialTree.hasElements());
	}

	public void replaceMaterial(Material oldValue, Material newValue) {
		materialTree.replaceMaterial(oldValue, newValue);
	}

	public void clear() {
		materialTree.clear();
		adaptExpandButtons(materialTree.hasElements());
	}

	private void adaptExpandButtons(boolean b) {
		expandButton.setEnabled(b);
		collapseButton.setEnabled(b);
	}

	public void addMaterial(Material material, boolean scrollPathToVisible) {
		materialTree.addMaterial(material, scrollPathToVisible);
		adaptExpandButtons(materialTree.hasElements());
	}

	public void expandTree() {
		materialTree.expandAll();
	}

	public void collapseTree() {
		materialTree.collapseAll();
	}

	public DefaultMutableTreeNode getSelectedNode() {
		return (DefaultMutableTreeNode) materialTree.getLeadSelectionPath()
				.getLastPathComponent();
	}

	public void addTransfer(TransferHandler treeTransferHandler) {
		materialTree.setTransferHandler(treeTransferHandler);
	}

	public void enableDrag(boolean b) {
		materialTree.setDragEnabled(b);
	}

	public void setMaterials(Collection<Material> materials) {
		materialTree.clear();
		if (null != materials) {
			for (Material material : materials) {
				addMaterial(material, false);
			}
		}
	}

	@Override
	public List<Material> getSelectedMaterials() {
		return materialTree.getSelectedMaterials();
	}

	public void addMaterials(List<Material> materials) {
		for (Material material : materials) {
			addMaterial(material, false);
		}
	}

	public void removeMaterials(List<Material> materials) {
		for (Material material : materials) {
			removeMaterial(material);
		}
	}

	public Collection<Material> getMaterials() {
		return materialTree.getMaterials();
	}

	/**
	 * This method returns a boolean value to say whether only {@link Material}
	 * objects are selected or if other types of objects are selected.
	 * 
	 * @return true if only {@link Material} objects are selected, false if
	 *         there is no object selected or if at least one object isn't an
	 *         instance of {@link Material}
	 */
	public boolean hasOnlyMaterialsSelected() {
		TreePath[] paths = materialTree.getSelectionPaths();
		if (paths == null)
			return false;
		for (TreePath treePath : paths) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath
					.getLastPathComponent();
			if (!(node.getUserObject() instanceof Material)) {
				return false;
			}
		}
		return true;
	}

}
