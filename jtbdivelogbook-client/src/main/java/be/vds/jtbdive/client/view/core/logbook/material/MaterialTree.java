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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import be.vds.jtbdive.client.view.renderer.MatCaveTreeCellRenderer;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.logging.Syslog;

public class MaterialTree extends AbstractMaterialTree {
	private static final long serialVersionUID = -2158787204200147758L;
	private static final Syslog LOGGER = Syslog.getLogger(MaterialTree.class);
	private DefaultTreeModel materialTreeModel;
	private Map<Material, DefaultMutableTreeNode> mats = new HashMap<Material, DefaultMutableTreeNode>();

	public MaterialTree() {
		super();
		init();
	}

	private void init() {
		materialTreeModel = new DefaultTreeModel(root);
		setModel(materialTreeModel);
		setRootVisible(false);
		putClientProperty("JTree.lineStyle", "Angled");
		setShowsRootHandles(true);

		MatCaveTreeCellRenderer renderer = new MatCaveTreeCellRenderer();
		setCellRenderer(renderer);
		// setRowHeight(renderer.getIconSize());
		setRowHeight(0);
	}

	public void removeMaterial(Material material) {
		DefaultMutableTreeNode node = getNodeForMaterial(material, root);
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node
				.getParent();
		materialTreeModel.removeNodeFromParent(node);
		mats.remove(material);

		if (parentNode.isLeaf()) {
			DefaultMutableTreeNode parentNode2 = (DefaultMutableTreeNode) parentNode.getParent();
			materialTreeModel.removeNodeFromParent(parentNode);
			materialTreeModel.nodeStructureChanged(parentNode2);
		} else {
			materialTreeModel.nodeStructureChanged(node.getParent());
		}
	}

	private DefaultMutableTreeNode getNodeForMaterial(Material material,
			DefaultMutableTreeNode parent) {
//		for (int i = 0; i < parent.getChildCount(); i++) {
//			TreeNode child = parent.getChildAt(i);
//			if (child instanceof DefaultMutableTreeNode) {
//				DefaultMutableTreeNode newNode = (DefaultMutableTreeNode) child;
//				if (((DefaultMutableTreeNode) child).getUserObject() instanceof Material) {
//					Material mat = (Material) newNode.getUserObject();
//					if (material.getId() == mat.getId()) {
//						return newNode;
//					}
//				} else {
//					if (!newNode.isLeaf()) {
//						DefaultMutableTreeNode node = getNodeForMaterial(
//								material, newNode);
//						if (node != null)
//							return node;
//					}
//				}
//			}
//		}
//		return null;

		return mats.get(material);
	}

	public void clear() {
		mats.clear();
		root.removeAllChildren();
		materialTreeModel.nodeChanged(root);
		materialTreeModel.reload();
	}

	public void replaceMaterial(Material oldValue, Material newValue) {
//		DefaultMutableTreeNode newNode = getNodeForMaterial(newValue, root);
//		DefaultMutableTreeNode oldNode = getNodeForMaterial(oldValue, root);
//
//		mats.remove(oldValue);
//		mats.put(newValue, newNode);
//
//		if (newNode != null) {
//			DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) oldNode
//					.getParent();
//			oldNode.removeFromParent();
//			materialTreeModel.nodeStructureChanged(parentNode);
//		} else {
//			oldNode.setUserObject(newValue);
//			materialTreeModel.nodeStructureChanged(oldNode);
//		}
		removeMaterial(oldValue);
		addMaterial(newValue, true);
	}

	public void addMaterial(Material material, boolean scrollPathToVisible) {

		DefaultMutableTreeNode matNode = mats.get(material);
		if (null == matNode) {
			matNode = new DefaultMutableTreeNode(material);
			DefaultMutableTreeNode parentNode = getMaterialNode(material
					.getMaterialType());
			parentNode.add(matNode);
			mats.put(material, matNode);
			materialTreeModel.nodeStructureChanged(parentNode);
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
			materialTreeModel.insertNodeInto(resultNode, root,
					getIndexInRoot(materialType));
			materialTreeModel.nodeStructureChanged(root);
			LOGGER.debug("created node for " + materialType);
		}
		return resultNode;
	}

	private int getIndexInRoot(MaterialType materialType) {
		int size = materialTreeModel.getChildCount(root);
		for (int j = 0; j < size; j++) {
			MaterialType mattype = (MaterialType) ((DefaultMutableTreeNode) materialTreeModel
					.getChild(root, j)).getUserObject();
			if (mattype.getOrder() > materialType.getOrder())
				return j;
		}
		return size;
	}

	public Collection<Material> getMaterials() {
		return mats.keySet();
	}

}
