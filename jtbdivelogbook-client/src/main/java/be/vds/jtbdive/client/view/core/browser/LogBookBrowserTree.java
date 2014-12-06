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
package be.vds.jtbdive.client.view.core.browser;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.view.core.browser.nodeconstructor.DiveNumberNodeConstructor;
import be.vds.jtbdive.client.view.core.browser.nodeconstructor.LogBookBrowserNodeConstructor;
import be.vds.jtbdive.client.view.core.browser.nodeconstructor.LogBookSorting;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.LogBook;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class LogBookBrowserTree extends JTree {

	private static final long serialVersionUID = 7305586396668900326L;
	private DefaultTreeModel treeModel;
	private LogBookBrowserNodeConstructor logBookBrowserNodeConstructor;
	private LogBookBrowserController controller;
	private LogBook currentLogBook;
	private DivePopupMenu divePopupMenu;
	private LogBookBrowserAction browserActions;
	private LogBookManagerFacade logBookManagerFacade;
	private DefaultMutableTreeNode root;

	public LogBookBrowserTree(LogBookBrowserAction browserActions,
			LogBookManagerFacade logBookManagerFacade) {
		this.browserActions = browserActions;
		this.logBookManagerFacade = logBookManagerFacade;
		init();
	}

	private void init() {
		LogBookSorting sorting = UserPreferences.getInstance()
				.getDefaultLogbookSorting();
		if (sorting != null) {
			logBookBrowserNodeConstructor = LogBookSortingHelper
					.getNodeConstructor(sorting);
		} else {
			logBookBrowserNodeConstructor = new DiveNumberNodeConstructor();
		}

		initController();
		initDiveTreePopupMenu();

		putClientProperty("JTree.lineStyle", "Angled");
		setShowsRootHandles(true);

		root = new DefaultMutableTreeNode("LOGBOOK");
		treeModel = new DefaultTreeModel(root);
		this.setModel(treeModel);
		this.setRootVisible(false);
		DiveTreeCellRenderer renderer = new DiveTreeCellRenderer(
				logBookManagerFacade);
		this.setCellRenderer(renderer);
	}

	private void initDiveTreePopupMenu() {
		divePopupMenu = new DivePopupMenu(browserActions);
	}

	private void initController() {
		controller = new LogBookBrowserController(this, logBookManagerFacade);
		controller.registerListeners();
	}

	public void buildTreeModel(LogBook logbook) {
		currentLogBook = logbook;
		List<Dive> dives = new ArrayList<Dive>(currentLogBook.getDives());
		logBookBrowserNodeConstructor.clear();
		logBookBrowserNodeConstructor.construct(root, dives);
		treeModel.nodeStructureChanged(root);
	}

	public void reset() {
		root.removeAllChildren();
		treeModel.nodeStructureChanged(root);
	}

	public Dive getHighLightedDive() {
		return (Dive) ((DefaultMutableTreeNode) this
				.getLastSelectedPathComponent()).getUserObject();
	}

	public List<Dive> getHighLightedDives() {
		List<Dive> dives = new ArrayList<Dive>();
		TreePath[] paths = this.getSelectionPaths();
		for (TreePath treePath : paths) {
			Dive d = (Dive) ((DefaultMutableTreeNode) treePath
					.getLastPathComponent()).getUserObject();
			dives.add(d);
		}

		return dives;
	}

	public void changeLogBookBrowserNodeConstructor(
			LogBookBrowserNodeConstructor nodeConstructor) {
		this.logBookBrowserNodeConstructor = nodeConstructor;
		reset();
		buildTreeModel(currentLogBook);
	}

	public LogBookBrowserAction getBrowserActions() {
		return browserActions;
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

	public DivePopupMenu getDiveTreePopup() {
		return divePopupMenu;
	}

	public void refresh(final Dive dive) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				DefaultMutableTreeNode node = logBookBrowserNodeConstructor
						.getNodeForDive(dive);
				treeModel.nodeChanged(node);
			}
		});
	}

	public void replaceDiveInNode(Dive oldValue, Dive newValue) {
		logBookBrowserNodeConstructor.replaceDiveInNode(oldValue, newValue);
	}

	public void openDiveInNode(Dive dive) {
		DefaultMutableTreeNode node = logBookBrowserNodeConstructor
				.getNodeForDive(dive);
		if (null != node) {
			selectNode(node);
		}
	}

	private void selectNode(DefaultMutableTreeNode node) {
		if (node != null) {
			TreePath path = new TreePath(node.getPath());
			setSelectionPath(path);
			scrollPathToVisible(path);
			expandPath(path);
		}
	}

	public void addDive(Dive dive) {
		DefaultMutableTreeNode node = logBookBrowserNodeConstructor
				.addNodeForDive(dive, root);
		selectNode(node);
		treeModel.nodeStructureChanged(node.getParent());
	}

}
