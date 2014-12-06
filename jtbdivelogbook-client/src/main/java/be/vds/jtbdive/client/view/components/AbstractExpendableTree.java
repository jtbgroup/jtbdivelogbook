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
package be.vds.jtbdive.client.view.components;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class AbstractExpendableTree extends JTree{

	private static final long serialVersionUID = -2187972634201240018L;
	protected DefaultMutableTreeNode root;
	
	
	public AbstractExpendableTree() {
		root = new DefaultMutableTreeNode("Root");
	}
	/**
	 * Expands the full tree
	 * 
	 * @author Gautier Vanderslyen
	 */
	public void expandAll() {
		int row = 0;
		while (row < super.getRowCount()) {
			super.expandRow(row);
			row++;
		}
	}

	/**
	 * Collapses the full tree
	 * 
	 * @author Gautier Vanderslyen
	 */
	public void collapseAll() {
		int row = 0;
		while (row < super.getRowCount()) {
			super.collapseRow(row);
			row++;
		}
	}
	
	public boolean hasElements() {
		return !root.isLeaf();
	}
	
}
