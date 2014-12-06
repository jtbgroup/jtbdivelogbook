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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import be.vds.jtbdive.client.view.components.AbstractExpendableTree;
import be.vds.jtbdive.client.view.renderer.EquipmentTreeCellRenderer;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.Equipment;
import be.vds.jtbdive.core.logging.Syslog;

public class EquipmentTree extends AbstractExpendableTree {
	private static final long serialVersionUID = -2158787204200147758L;
	private static final Syslog LOGGER = Syslog.getLogger(EquipmentTree.class);
	private DefaultTreeModel treeModel;
	private Map<Equipment, DefaultMutableTreeNode> mats = new HashMap<Equipment, DefaultMutableTreeNode>();

	public EquipmentTree() {
		super();
		init();
	}

	private void init() {
		treeModel = new DefaultTreeModel(root);
		setModel(treeModel);
		setRootVisible(false);
		putClientProperty("JTree.lineStyle", "Angled");
		setShowsRootHandles(true);

		EquipmentTreeCellRenderer renderer = new EquipmentTreeCellRenderer();
		setCellRenderer(renderer);
		// setRowHeight(renderer.getIconSize());
		setRowHeight(0);
	}

	public void removeEquipment(Equipment equipment) {
		DefaultMutableTreeNode node = getNodeForEquipment(equipment, root);
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node
				.getParent();
		treeModel.removeNodeFromParent(node);
		mats.remove(equipment);

		if (parentNode.isLeaf()) {
			treeModel.removeNodeFromParent(parentNode);
			treeModel.nodeChanged(parentNode.getParent());
		} else {
			treeModel.nodeChanged(node.getParent());
		}
	}

	private DefaultMutableTreeNode getNodeForEquipment(Equipment equipment,
			DefaultMutableTreeNode parent) {
		return mats.get(equipment);
	}

	public void clear() {
		mats.clear();
		root.removeAllChildren();
		treeModel.nodeChanged(root);
		treeModel.reload();
	}

	public void replaceEquipment(Equipment oldValue, Equipment newValue) {
		DefaultMutableTreeNode newNode = getNodeForEquipment(newValue, root);
		DefaultMutableTreeNode oldNode = getNodeForEquipment(oldValue, root);

		mats.remove(oldValue);
		mats.put(newValue, newNode);

		if (newNode != null) {
			DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) oldNode
					.getParent();
			oldNode.removeFromParent();
			treeModel.nodeStructureChanged(parentNode);
		} else {
			oldNode.setUserObject(newValue);
			treeModel.nodeStructureChanged(oldNode);
		}
	}

	public void addEquipment(Equipment equipment, boolean scrollPathToVisible) {
		DefaultMutableTreeNode matNode = mats.get(equipment);
		if (null == matNode) {
			matNode = new DefaultMutableTreeNode(equipment);
			DefaultMutableTreeNode parentNode = getMaterialNode(equipment
					.getMaterialType());
			parentNode.add(matNode);
			mats.put(equipment, matNode);
			treeModel.nodeStructureChanged(parentNode);
		}

		if (scrollPathToVisible) {
			TreePath path = new TreePath(matNode.getPath());
			scrollPathToVisible(path);
			setSelectionPath(path);
		}

	}

	private DefaultMutableTreeNode getMaterialNode(MaterialType materialType) {
		DefaultMutableTreeNode resultNode = null;

		// check if the node exists for the material type
		for (int i = 0; i < root.getChildCount(); i++) {
			DefaultMutableTreeNode matTypeNode = (DefaultMutableTreeNode) root
					.getChildAt(i);
			if (materialType.equals(matTypeNode.getUserObject())) {
				resultNode = matTypeNode;
				break;
			}
		}

		// if there's no node for this material type, create a new one
		if (null == resultNode) {
			resultNode = new DefaultMutableTreeNode(materialType);
			treeModel.insertNodeInto(resultNode, root,
					getIndexInRoot(materialType));
			treeModel.nodeStructureChanged(root);
			LOGGER.debug("created node for " + materialType);
		}
		return resultNode;
	}

	private int getIndexInRoot(MaterialType materialType) {
		int size = treeModel.getChildCount(root);
		for (int j = 0; j < size; j++) {
			MaterialType mattype = (MaterialType) ((DefaultMutableTreeNode) treeModel
					.getChild(root, j)).getUserObject();
			if (mattype.getOrder() > materialType.getOrder())
				return j;
		}
		return size;
	}

	public List<Equipment> getSelectedEquipments() {
		TreePath[] paths = getSelectionPaths();
		if (null == paths)
			return null;

		List<Equipment> result = new ArrayList<Equipment>();
		for (TreePath path : paths) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			result.addAll(getChildrenEquipmentForNode(node));
		}

		return result;
	}

	private Collection<Equipment> getChildrenEquipmentForNode(
			DefaultMutableTreeNode node) {
		List<Equipment> result = new ArrayList<Equipment>();
		if (node.isLeaf()) {
			if (node.getUserObject() instanceof Equipment) {
				result.add((Equipment) node.getUserObject());
			}
		} else {
			for (int i = 0; i < node.getChildCount(); i++) {
				result.addAll(getChildrenEquipmentForNode((DefaultMutableTreeNode) node
						.getChildAt(i)));
			}
		}
		return result;
	}
}
