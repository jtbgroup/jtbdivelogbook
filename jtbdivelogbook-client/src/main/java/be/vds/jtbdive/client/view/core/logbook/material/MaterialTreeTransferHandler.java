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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.logging.Syslog;

public class MaterialTreeTransferHandler extends TransferHandler {
	private static final Syslog LOGGER = Syslog
			.getLogger(MaterialTreeTransferHandler.class);
	private DataFlavor nodesFlavor;
	private DataFlavor[] flavors = new DataFlavor[1];

	public MaterialTreeTransferHandler() {
		try {
			String mimeType = DataFlavor.javaJVMLocalObjectMimeType
					+ ";class=\"" + Material[].class.getName() + "\"";
			nodesFlavor = new DataFlavor(mimeType);
			flavors[0] = nodesFlavor;
		} catch (ClassNotFoundException e) {
			LOGGER.error("ClassNotFound: " + e.getMessage());
		}
	}

	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

	/**
	 * @inherited <p>
	 */
	@Override
	protected Transferable createTransferable(JComponent c) {
		JTree tree = (JTree) c;
		TreePath[] paths = tree.getSelectionPaths();
		Set<Material> mats = new HashSet<Material>();

		for (int i = 0; i < paths.length; i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i]
					.getLastPathComponent();
			if (node.isLeaf()) {
				Material material = (Material) node.getUserObject();
				mats.add(material);
			} else {
				for (int j = 0; j < node.getChildCount(); j++) {
					DefaultMutableTreeNode lnode = (DefaultMutableTreeNode) node
							.getChildAt(j);
					Material mat = (Material) lnode.getUserObject();
					mats.add(mat);
				}
			}
		}

		Material[] arr = new Material[mats.size()];
		int i = 0;
		for (Material material : mats) {
			arr[i] = material;
			i++;
		}
		return new NodesTransferable(arr);
	}

	public class NodesTransferable implements Transferable {
		Material[] materials;

		public NodesTransferable(Material[] nodes) {
			this.materials = Arrays.copyOf(nodes, nodes.length);
		}

		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor))
				throw new UnsupportedFlavorException(flavor);
			return materials;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return nodesFlavor.equals(flavor);
		}
	}
}