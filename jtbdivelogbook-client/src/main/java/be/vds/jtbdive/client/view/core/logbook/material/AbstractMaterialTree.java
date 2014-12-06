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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import be.vds.jtbdive.client.view.components.AbstractExpendableTree;
import be.vds.jtbdive.core.core.material.Material;

public abstract class AbstractMaterialTree extends AbstractExpendableTree {
	private static final long serialVersionUID = 1777976027006201390L;

	public AbstractMaterialTree() {
		super();
	}

	public List<Material> getSelectedMaterials() {
		TreePath[] paths = getSelectionPaths();
		if (null == paths)
			return null;

		List<Material> result = new ArrayList<Material>();
		for (TreePath path : paths) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			result.addAll(getChildrenMaterialForNode(node));
		}

		return result;
	}

	private Collection<Material> getChildrenMaterialForNode(
			DefaultMutableTreeNode node) {
		Set<Material> result = new HashSet<Material>();
		if (node.isLeaf()) {
			if (node.getUserObject() instanceof Material) {
				result.add((Material) node.getUserObject());
			}
		} else {
			for (int i = 0; i < node.getChildCount(); i++) {
				result.addAll(getChildrenMaterialForNode((DefaultMutableTreeNode) node
						.getChildAt(i)));
			}
		}
		return result;
	}

}
