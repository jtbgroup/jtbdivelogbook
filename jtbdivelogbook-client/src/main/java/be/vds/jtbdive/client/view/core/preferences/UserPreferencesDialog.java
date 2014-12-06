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
package be.vds.jtbdive.client.view.core.preferences;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class UserPreferencesDialog extends JDialog {

	private static final long serialVersionUID = -3177059905769723022L;
	private static final String NODE_DEFAULT = "node.default";
	private static final String NODE_GENERAL = "node.general";
	private static final String NODE_UNITS = "node.units";
	private static final String NODE_FORMATTING = "node.formatting";
	private static final String NODE_CONSOLE = "node.console";
	private static final String NODE_GENERAL_CONFIG = "node.general.config";
	private static final String NODE_DIVE_COMPUTER = "node.dive.computer";

	private JTree preferenceTree;
	private JPanel preferencePanel;
	private CardLayout preferenceLayout;

	private Map<DefaultMutableTreeNode, String> nodes = new HashMap<DefaultMutableTreeNode, String>();
	private Map<String, JComponent> components = new HashMap<String, JComponent>();

	public UserPreferencesDialog(JFrame parentFrame) {
		super(parentFrame, I18nResourceManager.sharedInstance().getString(
				"preferences"), true);
		this.setIconImage(UIAgent.getInstance().getBufferedImage(UIAgent.ICON_PREFERENCES_16));
		init();
	}

	private void init() {
		this.getContentPane().add(createContentPane());
		setUserPreferences();
	}

	private Component createContentPane() {
		JPanel p = new JPanel(new BorderLayout());
		p.add(createMainPanel(), BorderLayout.CENTER);
		p.add(createButtonsPanel(), BorderLayout.SOUTH);
		return p;
	}

	private Component createMainPanel() {
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		Component right = createPreferencePanel();
		Component left = createPreferenceTree();
		split.setLeftComponent(left);
		split.setRightComponent(right);
		split.setDividerLocation(170);
		// split.setResizeWeight(0.3d);
		split.setOneTouchExpandable(true);
		return split;
	}

	private Component createPreferenceTree() {
		DefaultMutableTreeNode root = getRootNode();
		preferenceTree = new JTree(root);
		preferenceTree.setRootVisible(false);
		preferenceTree.setRowHeight(0);
		preferenceTree.setShowsRootHandles(true);

		preferenceTree.setCellRenderer(new DefaultTreeCellRenderer() {

			private static final long serialVersionUID = -5044280620721167226L;

			@Override
			public Component getTreeCellRendererComponent(JTree tree,
					Object value, boolean sel, boolean expanded, boolean leaf,
					int row, boolean hasFocus) {
				JLabel l = (JLabel) super.getTreeCellRendererComponent(tree,
						value, sel, expanded, leaf, row, hasFocus);
				l.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
				l.setIcon(getIconForNode((DefaultMutableTreeNode) value));
				return this;
			}

			private Icon getIconForNode(DefaultMutableTreeNode value) {
				String s = nodes.get(value);
				if (null == s) {
					return UIAgent.getInstance().getIcon(
							UIAgent.ICON_BULLET_16);
				}

				if (s.equals(NODE_GENERAL)) {
					return UIAgent.getInstance().getIcon(
							UIAgent.ICON_TOOLS_24);
				} else if (s.equals(NODE_UNITS)) {
					return UIAgent.getInstance().getIcon(
							UIAgent.ICON_PREFERENCE_UNIT_24);
				} else if (s.equals(NODE_GENERAL_CONFIG)) {
					return UIAgent.getInstance().getIcon(
							UIAgent.ICON_PREFERENCE_GENERAL_24);
				} else if (s.equals(NODE_DIVE_COMPUTER)) {
					return UIAgent.getInstance().getIcon(
							UIAgent.ICON_DIVECOMPUTER_24);
				} else if (s.equals(NODE_CONSOLE)) {
					return UIAgent.getInstance().getIcon(
							UIAgent.ICON_CONSOLE_24);
				} else if (s.equals(NODE_FORMATTING)) {
					return UIAgent.getInstance().getIcon(
							UIAgent.ICON_EDIT_24);
				}

				return UIAgent.getInstance().getIcon(
						UIAgent.ICON_BULLET_16);
			}
		});

		preferenceTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				displayPreferencePanel();
			}
		});

		JScrollPane scroll = new JScrollPane(preferenceTree);
		return scroll;
	}

	private DefaultMutableTreeNode getRootNode() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Preferences");

		// general
		DefaultMutableTreeNode generalNode = new DefaultMutableTreeNode(
				I18nResourceManager.sharedInstance().getString("general"));
//		JComponent c = getDefaultPanel();
//		registerNode(generalNode, c, NODE_GENERAL);
		GeneralPreferrencePanel generalPreferrencePanel = new GeneralPreferrencePanel();
		registerNode(generalNode, generalPreferrencePanel,
				NODE_GENERAL_CONFIG);
		root.add(generalNode);

//		// general > config
//		DefaultMutableTreeNode generalConfigNode = new DefaultMutableTreeNode(
//				I18nResourceManager.sharedInstance()
//						.getString("config.general"));
//		generalNode.add(generalConfigNode);

		// general > units
		DefaultMutableTreeNode unitsNode = new DefaultMutableTreeNode(
				I18nResourceManager.sharedInstance().getString("units"));
		generalNode.add(unitsNode);
		UnitsPreferrencePanel unitsPreferrencePanel = new UnitsPreferrencePanel();
		registerNode(unitsNode, unitsPreferrencePanel, NODE_UNITS);
		
		// general > console
		DefaultMutableTreeNode consoleNode = new DefaultMutableTreeNode(
				I18nResourceManager.sharedInstance().getString("console"));
		generalNode.add(consoleNode);
		ConsolePreferrencePanel cpp = new ConsolePreferrencePanel();
		registerNode(consoleNode, cpp, NODE_CONSOLE);
		
		// general > formatting
		DefaultMutableTreeNode formatNode = new DefaultMutableTreeNode(
				I18nResourceManager.sharedInstance().getString("formatting"));
		generalNode.add(formatNode);
		FormattingPreferrencePanel fpp = new FormattingPreferrencePanel();
		registerNode(formatNode, fpp, NODE_FORMATTING);

		// diveComputer
		DefaultMutableTreeNode diveComputerNode = new DefaultMutableTreeNode(
				I18nResourceManager.sharedInstance().getString("dive.computer"));
		DiveComputerPreferrencePanel dcpp = new DiveComputerPreferrencePanel();
		registerNode(diveComputerNode, dcpp, NODE_DIVE_COMPUTER);
		root.add(diveComputerNode);

		return root;
	}

//	private JComponent getDefaultPanel() {
//		JPanel p = new JPanel();
//		p.setOpaque(false);
//		p.add(new JLabel("No option here"));
//		return p;
//	}

	private void registerNode(DefaultMutableTreeNode node,
			JComponent component, String key) {
		nodes.put(node, key);
		components.put(key, component);
		preferencePanel.add(component, key);
	}

	private Component createPreferencePanel() {
		preferenceLayout = new CardLayout();
		preferencePanel = new JPanel(preferenceLayout);
		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(false);
		p.add(new JLabel(UIAgent.getInstance().getIcon(
				UIAgent.ICON_PREFERENCE_GENERAL_24)));
		preferencePanel.add(p, NODE_DEFAULT);

		// JScrollPane scroll = new JScrollPane(preferencePanel);
		// scroll.getVerticalScrollBar().setUnitIncrement(
		// UIController.VERTICAL_UNIT_SCROLL);
		// return scroll;

		return preferencePanel;
	}

	private Component createButtonsPanel() {
		JButton okButton = new I18nButton(new AbstractAction("ok") {

			private static final long serialVersionUID = -2596434498855622434L;

			@Override
			public void actionPerformed(ActionEvent e) {
				savePreferences();
				SwingUtilities.updateComponentTreeUI(UserPreferencesDialog.this
						.getOwner());
				dispose();
			}
		});

		JButton cancelButton = new I18nButton(new AbstractAction("cancel") {

			private static final long serialVersionUID = 8206528186016769065L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p.add(okButton);
		p.add(cancelButton);
		return p;
	}

	private void displayPreferencePanel() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) preferenceTree
				.getLastSelectedPathComponent();
		String key = nodes.get(node);
		preferenceLayout.show(preferencePanel, key);
	}

	private void setUserPreferences() {
		for (JComponent comp : components.values()) {
			if (comp instanceof AbstractPreferrencePanel) {
				((AbstractPreferrencePanel) comp).setUserPreferences();
			}
		}
	}

	private void savePreferences() {
		for (JComponent comp : components.values()) {
			if (comp instanceof AbstractPreferrencePanel) {
				((AbstractPreferrencePanel) comp).adaptUserPreferences();
			}
		}
		UserPreferences.getInstance().savePreferences();
	}

}
