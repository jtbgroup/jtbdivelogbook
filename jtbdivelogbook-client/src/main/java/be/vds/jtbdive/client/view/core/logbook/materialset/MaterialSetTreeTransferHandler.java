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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;

public class MaterialSetTreeTransferHandler extends TransferHandler {
	private static final long serialVersionUID = -2846088207510315450L;
	private static final Syslog LOGGER = Syslog
			.getLogger(MaterialSetTreeTransferHandler.class);
	private DataFlavor materialsFlavor;
	private LogBookManagerFacade logBookManagerFacade;

	public MaterialSetTreeTransferHandler(
			LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;

		try {
			String mimeType = DataFlavor.javaJVMLocalObjectMimeType
					+ ";class=\"" + Material[].class.getName() + "\"";
			materialsFlavor = new DataFlavor(mimeType);
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFound: " + e.getMessage());
		}
	}

	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

	public boolean canImport(TransferHandler.TransferSupport support) {
		if (!support.isDrop()) {
			return false;
		}
		support.setShowDropLocation(true);
		if (!support.isDataFlavorSupported(materialsFlavor)) {
			LOGGER.error("can't import because dataflavor unspupported");
			return false;
		}

		JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
		TreePath dest = dl.getPath();
		return null != dest;
	}

	@Override
	public boolean importData(TransferHandler.TransferSupport support) {
		if (!canImport(support)) {
			return false;
		}

		// Extract transfer data.
		try {
			Transferable t = support.getTransferable();
			t.getTransferDataFlavors();

			Material[] materials = (Material[]) t
					.getTransferData(materialsFlavor);

			JTree.DropLocation dl = (javax.swing.JTree.DropLocation) support
					.getDropLocation();
			TreePath path = dl.getPath();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			MaterialSet materialSet = getMaterialSetParentNode(node);

			try {
				logBookManagerFacade.addMaterialToMaterialSet(materialSet,
						materials);
			} catch (DataStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;

		} catch (UnsupportedFlavorException ufe) {
			System.out.println("UnsupportedFlavor: " + ufe.getMessage());
		} catch (java.io.IOException ioe) {
			System.out.println("I/O error: " + ioe.getMessage());
		}
		return false;

	}

	private MaterialSet getMaterialSetParentNode(DefaultMutableTreeNode node) {
		Object userObj = node.getUserObject();
		if (node.isRoot()) {
			return null;
		} else if (userObj instanceof MaterialSet) {
			return (MaterialSet) userObj;
		} else {
			return getMaterialSetParentNode((DefaultMutableTreeNode) node
					.getParent());
		}
	}

}