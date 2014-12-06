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
package be.vds.jtbdive.client.view.core.contact;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.client.core.comparator.OrderedCatalogComparator;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Contact;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.catalogs.ContactType;

public class ContactsPanel extends JPanel {
	// private JList contactsList;
	private JButton removeButton;
	// private DefaultListModel contactsListModel;
	private I18nButton addButton;
	private DefaultMutableTreeNode root;
	private JTree contactTree;

	public ContactsPanel() {
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		this.add(createContactList(), BorderLayout.CENTER);
		this.add(createButtonsPanel(), BorderLayout.EAST);
	}

	private Component createContactList() {
		root = new DefaultMutableTreeNode();
		contactTree = new JTree(root);
		contactTree.setRootVisible(false);
		contactTree.getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {

					@Override
					public void valueChanged(TreeSelectionEvent e) {
						removeButton.setEnabled(e.getNewLeadSelectionPath() != null);
					}
				});

		contactTree.setCellRenderer(new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree,
					Object value, boolean sel, boolean expanded, boolean leaf,
					int row, boolean hasFocus) {
				JLabel l = (JLabel) super.getTreeCellRendererComponent(tree,
						value, sel, expanded, leaf, row, hasFocus);
				Object myObj = ((DefaultMutableTreeNode) value).getUserObject();
				if (myObj instanceof ContactType) {
					displayContactType((ContactType) myObj, l);
				} else if (myObj instanceof Contact) {
					displayContact((Contact) myObj, l);
				} else {
					l.setText(null);
				}

				return l;
			}

			private void displayContactType(ContactType value, JLabel l) {
				l.setText(I18nResourceManager.sharedInstance().getString(
						value.getKey()));

				switch (value) {
				case EMAIL:
					l.setIcon(UIAgent.getInstance().getIcon(
							UIAgent.ICON_EMAIL_16));
					break;
				case MOBILE:
					l.setIcon(UIAgent.getInstance().getIcon(
							UIAgent.ICON_MOBILE_16));
					break;
				case PHONE:
					l.setIcon(UIAgent.getInstance().getIcon(
							UIAgent.ICON_PHONE_16));
					break;
				}
			}

			private void displayContact(Contact value, JLabel l) {
				l.setText(value.getValue());
				l.setIcon(null);
			}
		});

		JScrollPane scroll = new JScrollPane(contactTree);
		return scroll;
	}

	private Component createButtonsPanel() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		GridBagLayoutManager.addComponent(p, createAddButton(), gc, 0, 0, 1, 1,
				0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(p, createRemoveButton(), gc, 0, 1, 1,
				1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		// GridBagLayoutManager.addComponent(p, createAddButton(), gc, 0, 0, 1,
		// 1, 0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, Box.createVerticalGlue(), gc, 0,
				2, 1, 1, 0, 1, GridBagConstraints.VERTICAL,
				GridBagConstraints.CENTER);

		return p;
	}

	private Component createAddButton() {
		addButton = new I18nButton(new AbstractAction("add") {

			@Override
			public void actionPerformed(ActionEvent e) {
				ContactDialog dlg = new ContactDialog();
				int i = dlg.showDialog();
				if (i == ContactDialog.OPTION_OK) {
					Contact contact = dlg.getContact();
					// contactsListModel.addElement(contact);
					addContact(contact);
				}
			}
		});
		return addButton;
	}

	private void addContact(Contact contact) {
		DefaultMutableTreeNode targetNode = null;
		DefaultMutableTreeNode modifiedNode = null;
		for (int i = 0; i < root.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root
					.getChildAt(i);
			if (node.getUserObject().equals(contact.getContactType())) {
				targetNode = node;
				modifiedNode = targetNode;
				break;
			}
		}

		if (null == targetNode) {
			targetNode = new DefaultMutableTreeNode(contact.getContactType());
			root.add(targetNode);
			modifiedNode = root;
		}

		DefaultMutableTreeNode insertedNode = new DefaultMutableTreeNode(
				contact);
		targetNode.add(insertedNode);
		((DefaultTreeModel) contactTree.getModel()).nodeStructureChanged(modifiedNode);
		contactTree.expandPath(new TreePath(modifiedNode.getPath()));
		contactTree.setSelectionPath(new TreePath(insertedNode.getPath()));
	}

	private Component createRemoveButton() {
		removeButton = new I18nButton(new AbstractAction("remove") {

			@Override
			public void actionPerformed(ActionEvent e) {
				TreePath[] paths = contactTree.getSelectionPaths();
				for (TreePath treePath : paths) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath
							.getLastPathComponent();
					DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node
							.getParent();
					node.removeFromParent();
					((DefaultTreeModel) contactTree.getModel())
							.nodeStructureChanged(parentNode);
					if (node.isLeaf() && parentNode.getChildCount() == 0) {
						parentNode.removeFromParent();
						((DefaultTreeModel) contactTree.getModel())
								.nodeStructureChanged(root);
					}
				}
			}
		});
		return removeButton;
	}

	public void setContacts(List<Contact> contacts) {
		clear();
		if (null != contacts) {
			Map<ContactType, List<Contact>> cont = LogBookUtilities
					.getContactMap(contacts);
			List<ContactType> types = new ArrayList(cont.keySet());
			Collections.sort(types, new OrderedCatalogComparator());
			for (ContactType contactType : types) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(
						contactType);
				root.add(node);
				for (Contact contact : cont.get(contactType)) {
					DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(
							contact);
					node.add(leaf);
				}
			}
		}
		((DefaultTreeModel) contactTree.getModel()).nodeStructureChanged(root);
	}

	private void clear() {
		removeButton.setEnabled(false);
		root.removeAllChildren();
	}

	public void setEditable(boolean b) {
		removeButton.setEnabled(b);
		addButton.setEnabled(b);
		// contactsList.setEnabled(b);
		contactTree.setEnabled(b);
	}

	public void fillDiverWithValues(Diver diver) {
		if (root.getChildCount() == 0)
			return;

		List<Contact> cs = new ArrayList<Contact>();
		// for (int i = 0; i < contactsListModel.size(); i++) {
		// Contact contact = (Contact) contactsListModel.get(i);
		// cs.add(contact);
		// }
		for (int i = 0; i < root.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root
					.getChildAt(i);
			for (int j = 0; j < node.getChildCount(); j++) {
				cs.add((Contact) ((DefaultMutableTreeNode) node.getChildAt(j))
						.getUserObject());
			}
		}
		diver.setContacts(cs);
	}
}
