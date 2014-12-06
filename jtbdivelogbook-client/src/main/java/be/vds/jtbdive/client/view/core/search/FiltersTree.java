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
package be.vds.jtbdive.client.view.core.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import be.vds.jtbdive.client.core.filters.AggregatorDiveFilter;
import be.vds.jtbdive.client.core.filters.DiveFilter;
import be.vds.jtbdive.client.view.renderer.DiveFilterCellRenderer;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class FiltersTree extends JTree {

	public static final String SELECTION_CHANGED = "selection.changed";
	private static final long serialVersionUID = 7305586396668900326L;
	private DefaultTreeModel treeModel;
	private AggregatorDiveFilter aggregateFilter;
	private Map<DiveFilter, DefaultMutableTreeNode> mapping = new HashMap<DiveFilter, DefaultMutableTreeNode>();
	private DefaultMutableTreeNode root;

	public FiltersTree() {
		init();
	}

	private void init() {
		putClientProperty("JTree.lineStyle", "Angled");
		setShowsRootHandles(true);

		root = new DefaultMutableTreeNode("ROOT");
		treeModel = new DefaultTreeModel(root);

		this.setModel(treeModel);
		this.setRootVisible(false);
		DiveFilterCellRenderer renderer = new DiveFilterCellRenderer();
		this.setCellRenderer(renderer);

		this.addTreeSelectionListener(createTreeSelectionListener());
	}

	private TreeSelectionListener createTreeSelectionListener() {
		TreeSelectionListener tsl = new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath selectedComponent = e.getNewLeadSelectionPath();
				if (selectedComponent == null) {
					firePropertyChange(SELECTION_CHANGED, null, null);
				} else {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedComponent
							.getLastPathComponent();
					firePropertyChange(SELECTION_CHANGED, null,
							node.getUserObject());
				}
			}
		};
		return tsl;
	}

	public void setFilters(AggregatorDiveFilter aggregateFilter) {
		this.aggregateFilter = aggregateFilter;
		root.removeAllChildren();
		mapping.clear();
		root.add(buildFilterNode(aggregateFilter));
		treeModel.reload();
		treeModel.nodeStructureChanged(root);
	}

	private DefaultMutableTreeNode buildAggregateNode(
			AggregatorDiveFilter aggregateFilter) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(
				aggregateFilter);

		List<DiveFilter> allFilters = aggregateFilter.getFilters();
		if (null != allFilters) {
			for (DiveFilter filter : allFilters) {
				node.add(buildFilterNode(filter));
			}
		}
		return node;
	}

	private DefaultMutableTreeNode buildFilterNode(DiveFilter filter) {
		DefaultMutableTreeNode node = null;

		if (filter instanceof AggregatorDiveFilter) {
			node = buildAggregateNode((AggregatorDiveFilter) filter);
		} else {
			node = new DefaultMutableTreeNode(filter);
		}

		mapping.put(filter, node);
		return node;
	}

	public DiveFilter getSelectedFilter() {
		DiveFilter df = (DiveFilter) ((DefaultMutableTreeNode) getSelectionPath()
				.getLastPathComponent()).getUserObject();
		return df;
	}

	public void addFilter(DiveFilter parentFilter, DiveFilter filter) {
		if (parentFilter instanceof AggregatorDiveFilter) {
			((AggregatorDiveFilter) parentFilter).addFilter(filter);
			DefaultMutableTreeNode parentNode = mapping.get(parentFilter);
			DefaultMutableTreeNode filterNode = buildFilterNode(filter);
			parentNode.add(filterNode);
			treeModel.nodeStructureChanged(parentNode);
			TreePath path = new TreePath(filterNode.getPath());
			expandPath(path);
			scrollPathToVisible(path);
			setSelectionPath(path);
		}
	}

	public void removeFilter(DiveFilter df) {
		DefaultMutableTreeNode node = mapping.get(df);
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node
				.getParent();

		if (parentNode.isRoot()) {
			return;
		}

		DiveFilter parentFilter = (DiveFilter) parentNode.getUserObject();

		if (null != parentFilter
				&& parentFilter instanceof AggregatorDiveFilter) {
			((AggregatorDiveFilter) parentFilter).removeDiveFilter(df);
		}

		treeModel.removeNodeFromParent(node);
		treeModel.nodeStructureChanged(parentNode);
	}

	public AggregatorDiveFilter getFilters() {
		return aggregateFilter;
	}

	public void refreshLabel(DiveFilter diveFilter) {
		DefaultMutableTreeNode node = mapping.get(diveFilter);
		treeModel.nodeChanged(node);
	}

	public boolean isRootLeaf(DiveFilter diveFilter) {
		DefaultMutableTreeNode node = mapping.get(diveFilter);

		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node
				.getParent();

		return parentNode.isRoot();
	}

	public void expandAll() {
		int row = 0;
		while (row < super.getRowCount()) {
			super.expandRow(row);
			row++;
		}
	}
}
