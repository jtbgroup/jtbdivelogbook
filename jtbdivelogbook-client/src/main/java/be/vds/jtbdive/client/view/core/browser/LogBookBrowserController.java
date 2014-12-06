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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.logging.Syslog;

public class LogBookBrowserController {

	private static final Syslog LOGGER = Syslog
			.getLogger(LogBookBrowserController.class);
	private LogBookBrowserTree tree;
	private boolean expanding;
	private boolean collapsing;
	private LogBookManagerFacade logBookManagerFacade;

	public LogBookBrowserController(LogBookBrowserTree tree,
			LogBookManagerFacade logBookManagerFacade) {
		this.tree = tree;
		this.logBookManagerFacade = logBookManagerFacade;
	}

	private void handleDoubleClick() {
		if (tree.getLastSelectedPathComponent() instanceof DefaultMutableTreeNode) {
			Object obj = (((DefaultMutableTreeNode) tree
					.getLastSelectedPathComponent())).getUserObject();
			if (obj instanceof Dive) {
				Dive dive = (Dive) obj;
				LOGGER.debug("double clicked for dive " + dive);
				logBookManagerFacade.setCurrentDive(dive);
			}
		}
	}

	private void handlePopupTriggered(MouseEvent e) {
		if (tree.getAnchorSelectionPath() == null) {
			return;
		}

		forceRigthClickSelection(e);

		TreePath[] selectedComponent = tree.getSelectionPaths();
		if (null != selectedComponent) {
			LOGGER.debug("Showing browser popup menu...");
			tree.getDiveTreePopup().allowMultipleSelection(
					tree.getSelectionCount() > 1);
			tree.getDiveTreePopup().show(e.getComponent(), e.getX() - 3,
					e.getY() - 5);
		}
	}

	public void registerListeners() {
		ExpansionListener expansionListener = new ExpansionListener();
		tree.addTreeWillExpandListener(expansionListener);
		tree.addTreeWillExpandListener(expansionListener);
		tree.addMouseListener(new TreeMouseListener());
		tree.addKeyListener(new TreeKeyListener());
	}

	private void forceRigthClickSelection(MouseEvent e) {
		TreePath selPath = tree.getClosestPathForLocation(e.getX(), e.getY());
		LOGGER.debug("Selection in browser forced on row " + selPath);
		if (selPath != null && e.getClickCount() == 1
				&& SwingUtilities.isRightMouseButton(e)) {
			TreePath[] selectedPaths = tree.getSelectionPaths();
			boolean pathAlreadySelected = false;
			if (selectedPaths != null) {
				for (TreePath treePath : selectedPaths) {
					if (treePath.equals(selPath)) {
						pathAlreadySelected = true;
						LOGGER.debug("Already selected path is " + treePath);
						break;
					}
				}
			}

			if (!pathAlreadySelected) {
				tree.setSelectionPath(selPath);
			}
		}
	}

	private void forceLeftClickSelection(MouseEvent e) {
		TreePath selPath = tree.getClosestPathForLocation(e.getX(), e.getY());
		LOGGER.debug("Left selection in browser forced on row " + selPath);
		if (selPath != null) {
			TreePath[] selectedPaths = tree.getSelectionPaths();
			boolean pathAlreadySelected = false;
			if (selectedPaths != null) {
				for (TreePath treePath : selectedPaths) {
					if (treePath.equals(selPath)) {
						pathAlreadySelected = true;
						LOGGER.debug("Already selected path is " + treePath);
						break;
					}
				}
			}

			if (!pathAlreadySelected) {
				tree.setSelectionPath(selPath);
			}
		}
	}

	class TreeMouseListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			LOGGER.debug("mouse pressed");
			if (tree.isEnabled()) {
				if (!(expanding || collapsing)) {
					forceRigthClickSelection(e);
				} else {
					LOGGER.debug("Expanding / collapsing tree, no need to load viewer...");
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			LOGGER.debug("mouse released");
			if (!(expanding || collapsing)) {
				if (tree.isEnabled()) {
					if (SwingUtilities.isRightMouseButton(e)) {
						handlePopupTriggered(e);
					} else if (e.getClickCount() == 2) {
						handleDoubleClick();
					} else {
						forceLeftClickSelection(e);
					}
				}
			}
			collapsing = false;
			expanding = false;
		}
	}

	class TreeKeyListener extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent arg0) {
			if (tree.isEnabled() && arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				TreePath selectedComponent = tree.getSelectionPath();
				if (null != selectedComponent) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedComponent
							.getLastPathComponent();
					if (node != null && node.isLeaf()) {
						handleDoubleClick();
					}
				}
			}
		}
	}

	class ExpansionListener implements TreeWillExpandListener {

		public void treeWillCollapse(TreeExpansionEvent arg0)
				throws ExpandVetoException {
			collapsing = true;
		}

		public void treeWillExpand(TreeExpansionEvent arg0)
				throws ExpandVetoException {
			expanding = true;
		}
	}
}
