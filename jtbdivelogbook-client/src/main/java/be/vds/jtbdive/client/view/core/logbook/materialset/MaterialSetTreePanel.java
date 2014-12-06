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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.core.logbook.material.MaterialSelectable;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;

public class MaterialSetTreePanel extends DetailPanel implements
		MaterialSelectable {
	private static final long serialVersionUID = 8864491041385141786L;
	private MaterialSetTree tree;
	private JButton collapseButton;
	private JButton expandButton;
	

	public MaterialSetTreePanel() {
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		this.add(createNorthPanel(), BorderLayout.NORTH);
		this.add(createTreePanel(), BorderLayout.CENTER);
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

	public void expandTree() {
		tree.expandAll();
	}

	public void collapseTree() {
		tree.collapseAll();
	}

	private Component createTreePanel() {
		tree = new MaterialSetTree();
		JScrollPane scroll = new JScrollPane(tree);
		scroll.setPreferredSize(new Dimension(60, 100));
		SwingComponentHelper.displayJScrollPane(scroll);
		return scroll;
	}

	public void addMaterialSet(MaterialSet materialSet, boolean scrollToVisible) {
		tree.addMaterialSet(materialSet, scrollToVisible);
		adaptExpandButtons(tree.hasElements());
	}

	private void adaptExpandButtons(boolean b) {
		expandButton.setEnabled(b);
		collapseButton.setEnabled(b);
	}

	public void clear() {
		tree.clear();
		adaptExpandButtons(tree.hasElements());
	}

	public JTree getTree() {
		return tree;
	}

	public void addTransfer(TransferHandler treeTransferHandler) {
		tree.setTransferHandler(treeTransferHandler);
	}

	public void addTreeSelectionListener(
			TreeSelectionListener treeSelectionListener) {
		tree.addTreeSelectionListener(treeSelectionListener);
	}

	public void addTreeKeyListener(KeyAdapter keyAdapter) {
		tree.addKeyListener(keyAdapter);
	}

	public DefaultMutableTreeNode getSelectedNode() {
		TreePath path = tree.getSelectionPath();
		if (null == path)
			return null;

		return (DefaultMutableTreeNode) path.getLastPathComponent();

	}

	public void removeMaterialSet(MaterialSet materialSet) {
		tree.removeMaterialSet(materialSet);
		adaptExpandButtons(tree.hasElements());
	}

	public void removeMaterial(Material material) {
		tree.removeMaterial(material);
	}

	public void replaceMaterial(Material oldMaterial, Material newMaterial) {
		tree.replaceMaterial(oldMaterial, newMaterial);
	}

	@Override
	public List<Material> getSelectedMaterials() {
		return tree.getSelectedMaterials();
	}

	public void setMaterialSet(Collection<MaterialSet> materialSets) {
		tree.clear();
		if (materialSets != null) {
			for (MaterialSet materialSet : materialSets) {
				addMaterialSet(materialSet, false);
			}
		}
	}

	public MaterialSet getSelectedMaterialSet() {
		TreePath path = tree.getSelectionPath();
		if (null == path)
			return null;

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		MaterialSet result = null;
		boolean search = true;
		while(search){
			if(node.getUserObject() instanceof MaterialSet){
				result = (MaterialSet) node.getUserObject();
				search = false;
			}else{
				node = (DefaultMutableTreeNode) node.getParent();
			}
		}

		return result;
	}

	public void setSelectionMode(int selectionMode) {
		tree.getSelectionModel().setSelectionMode(selectionMode);
	}


}
