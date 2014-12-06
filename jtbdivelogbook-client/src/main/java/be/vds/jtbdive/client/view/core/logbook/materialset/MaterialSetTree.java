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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import be.vds.jtbdive.client.core.comparator.OrderedCatalogComparator;
import be.vds.jtbdive.client.view.core.logbook.material.AbstractMaterialTree;
import be.vds.jtbdive.client.view.renderer.MatCaveTreeCellRenderer;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;
import be.vds.jtbdive.core.logging.Syslog;

public class MaterialSetTree extends AbstractMaterialTree {

	private static final long serialVersionUID = 2978609539685412034L;
	private static final Syslog LOGGER = Syslog
			.getLogger(MaterialSetTree.class);
	private DefaultTreeModel materialSetTreeModel;

	public MaterialSetTree() {
		super();
		init();
	}

	private void init() {
		materialSetTreeModel = new DefaultTreeModel(root);
		setModel(materialSetTreeModel);
		setRootVisible(false);
		putClientProperty("JTree.lineStyle", "Angled");
		setShowsRootHandles(true);

		MatCaveTreeCellRenderer renderer = new MatCaveTreeCellRenderer();
		setCellRenderer(renderer);
	}

	public void clear() {
		root.removeAllChildren();
		materialSetTreeModel.nodeChanged(root);
		materialSetTreeModel.reload();
	}

	public void addMaterialSet(MaterialSet materialSet, boolean scrollToVisible) {

		int index = getMaterialSetIndex(materialSet);

		DefaultMutableTreeNode matSetNode = new DefaultMutableTreeNode(
				materialSet);
		populateMaterialSetNode(materialSet, matSetNode);

		if (-1 == index) {
			root.add(matSetNode);
		} else {
			root.insert(matSetNode, index);
		}

		materialSetTreeModel.nodeChanged(root);
		materialSetTreeModel.reload();

		if (scrollToVisible) {
			TreePath path = new TreePath(matSetNode.getPath());
			setSelectionPath(path);
			scrollPathToVisible(path);
			expandPath(path);
		}
	}

	private void populateMaterialSetNode(MaterialSet materialSet,
			DefaultMutableTreeNode matSetNode) {
		List<MaterialType> types = new ArrayList<MaterialType>(
				materialSet.getMaterialTypes());
		Collections.sort(types, new OrderedCatalogComparator());

		DefaultMutableTreeNode materialNode = null;
		DefaultMutableTreeNode materialTypeNode = null;
		Map<MaterialType, DefaultMutableTreeNode> matTypeMap = new HashMap<MaterialType, DefaultMutableTreeNode>();

		for (MaterialType materialType : types) {
			materialTypeNode = new DefaultMutableTreeNode(materialType);
			matTypeMap.put(materialType, materialTypeNode);
			matSetNode.add(materialTypeNode);
		}

		for (Material mat : materialSet.getMaterials()) {
			materialNode = new DefaultMutableTreeNode(mat);
			matTypeMap.get(mat.getMaterialType()).add(materialNode);
		}
	}

	private int getMaterialSetIndex(MaterialSet materialSet) {
		int index = -1;

		int childCount = root.getChildCount();
		if (childCount > 0) {
			index = childCount;
			for (int i = 0; i < root.getChildCount(); i++) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) root
						.getChildAt(i);
				MaterialSet matSetAtNode = (MaterialSet) node.getUserObject();

				if (matSetAtNode.equals(materialSet)) {
					LOGGER.debug("Removing node for materialSet to replace with a new one");
					node.removeFromParent();
					index = i;
					break;
				}

				int compare = matSetAtNode.getName().compareTo(materialSet.getName());
				if (compare > 0) {
					index = i;
					break;
				}
			}
		}
		return index;
	}


	public void removeMaterialSet(MaterialSet materialSet) {
		TreePath path = getSelectionPath();
		if (null == path)
			return;

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		TreeNode parentNode = node.getParent();
		int index = parentNode.getIndex(node);
		node.removeFromParent();
		materialSetTreeModel.nodesWereRemoved(parentNode, new int[] { index },
				new Object[] { node.getUserObject() });
	}

	public void removeMaterial(Material material) {
		removeMaterial(material, root);
	}

	private void removeMaterial(Material material,
			DefaultMutableTreeNode parentNode) {
		DefaultMutableTreeNode nextNode = null;
		for (int i = 0; i < parentNode.getChildCount(); i++) {
			nextNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);

			if (!nextNode.isLeaf()) {
				removeMaterial(material, nextNode);
			} else {
				if (nextNode.getUserObject() == material) {
					int index = parentNode.getIndex(nextNode);
					parentNode.remove(nextNode);
					materialSetTreeModel.nodesWereRemoved(parentNode,
							new int[] { index },
							new Object[] { nextNode.getUserObject() });
				}
			}
		}
	}

	public void replaceMaterial(Material oldMaterial, Material newMaterial) {
		replaceMaterial(oldMaterial, newMaterial, root);
	}

	private void replaceMaterial(Material oldMaterial, Material newMaterial,
			DefaultMutableTreeNode parentNode) {
		for (int i = 0; i < parentNode.getChildCount(); i++) {
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode
					.getChildAt(i);
			if (!childNode.isLeaf()) {
				replaceMaterial(oldMaterial, newMaterial, childNode);
			} else {
				Object nodeObj = childNode.getUserObject();
				if (nodeObj.equals(oldMaterial)) {
					if (newMaterial != null) {
						DefaultMutableTreeNode nodeForNewMat = null;
						for (int j = 0; j < parentNode.getChildCount(); j++) {
							DefaultMutableTreeNode testedNode = (DefaultMutableTreeNode) parentNode
									.getChildAt(j);
							if (testedNode.getUserObject().equals(newMaterial)) {
								nodeForNewMat = testedNode;
								break;
							}
						}

						if (null != nodeForNewMat) {
							childNode.removeFromParent();
						} else {
							childNode.setUserObject(newMaterial);
						}
					} else {
						childNode.removeFromParent();
					}
					materialSetTreeModel.nodeStructureChanged(parentNode);
				}
			}
		}
	}

}
