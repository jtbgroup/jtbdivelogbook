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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nMenuItem;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.actions.ActionType;
import be.vds.jtbdive.client.actions.LogBookApplActionsContoller;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.event.LogBookEvent;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.core.browser.nodeconstructor.LogBookBrowserNodeConstructor;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.LogBook;

public class LogBookBrowserPanel extends DetailPanel implements Observer {
	private static final long serialVersionUID = -8948230778385503997L;
	private LogBookBrowserTree tree;
	private JPopupMenu sortPopupMenu;
	private JButton sortButton;
	private int buttonHeight = 18;
	private int gap = 2;
	private LogBookManagerFacade logBookManagerFacade;
	private JLabel logBookNameLabel;
	private LogBookBrowserAction browserActions;
	private JButton collapseButton;
	private JButton expandButton;
	private JPanel buttonHeader;
	private JScrollPane scrollTree;
	private JLabel numberOfDiveLabel;
	private JPanel bottomPanel;

	public LogBookBrowserPanel(LogBookManagerFacade logBookManagerFacade,
			LogBookApplActionsContoller actions) {
		this.logBookManagerFacade = logBookManagerFacade;

		createActions();
		init(logBookManagerFacade, actions);

		if (null != logBookManagerFacade) {
			logBookManagerFacade.addObserver(this);
		}
	}

	private void createActions() {
		browserActions = new LogBookBrowserAction(this, logBookManagerFacade);
	}

	private void init(LogBookManagerFacade logBookManagerFacade,
			LogBookApplActionsContoller actions) {
		this.setLayout(new BorderLayout());

		scrollTree = new JScrollPane(createTree());
		scrollTree.getVerticalScrollBar().setUnitIncrement(
				UIAgent.VERTICAL_UNIT_SCROLL);

		this.add(createHeaderPanel(), BorderLayout.NORTH);
		this.add(scrollTree, BorderLayout.CENTER);
		this.add(createBottomPanel(actions), BorderLayout.SOUTH);
	}

	private Component createBottomPanel(LogBookApplActionsContoller actions) {
		numberOfDiveLabel = new JLabel();
		
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
		bottomPanel.setOpaque(false);
		bottomPanel.add(numberOfDiveLabel);
		bottomPanel.add(Box.createHorizontalGlue());
		bottomPanel.add(createInformationButton(actions));
		bottomPanel.add(Box.createHorizontalStrut(3));
		bottomPanel.add(createMapButton(actions));
		return bottomPanel;
	}

	private Component createMapButton(LogBookApplActionsContoller actions) {
		JButton mapButton = new JButton(
				actions.getAction(ActionType.SHOW_ALL_DIVE_SITES_ON_MAP));
		mapButton.setText(null);
		mapButton.setBorderPainted(false);
		mapButton.setContentAreaFilled(false);
		mapButton.setFocusable(false);
		mapButton.setPreferredSize(new Dimension(20, 20));
		SwingComponentHelper.addI18nToolTip(mapButton, "tooltip.divesites.show.map");
		
		return mapButton;
	}
	
	private Component createInformationButton(LogBookApplActionsContoller actions) {
		JButton informationButton = new JButton(
				actions.getAction(ActionType.VIEW_LOGBOOK_INFORMATION));
		informationButton.setText(null);
		informationButton.setBorderPainted(false);
		informationButton.setContentAreaFilled(false);
		informationButton.setFocusable(false);
		informationButton.setPreferredSize(new Dimension(20, 20));
		SwingComponentHelper.addI18nToolTip(informationButton, "tooltip.logbook.information");
		
		return informationButton;
	}

	private Component createHeaderPanel() {
		JPanel headerPanel = new JPanel();
		headerPanel.setOpaque(false);

		headerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		GridBagLayoutManager.addComponent(headerPanel,
				createHeaderButtonsPanel(), c, 0, 0, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.EAST);
		c.insets = new Insets(5, 0, 5, 0);
		GridBagLayoutManager.addComponent(headerPanel, createNameLabel(), c, 0,
				1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		return headerPanel;
	}

	private Component createHeaderButtonsPanel() {

		buttonHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, gap, gap));
		buttonHeader.setOpaque(false);

		createSortPopup();

		// JButton button = null;
		Dimension d = new Dimension(18, buttonHeight);

		// // COLLAPSE
		collapseButton = new JButton(
				browserActions.getAction(LogBookBrowserAction.ACTION_COLLAPSE));
		collapseButton.setBorderPainted(false);
		collapseButton.setContentAreaFilled(false);
		collapseButton.setText(null);
		collapseButton.setPreferredSize(d);
		collapseButton.setEnabled(false);
		buttonHeader.add(collapseButton);

		// // EXPAND
		expandButton = new JButton(
				browserActions.getAction(LogBookBrowserAction.ACTION_EXPAND));
		expandButton.setBorderPainted(false);
		expandButton.setContentAreaFilled(false);
		expandButton.setText(null);
		expandButton.setPreferredSize(d);
		expandButton.setEnabled(false);
		buttonHeader.add(expandButton);

		// // SORT
		sortButton = new JButton(new AbstractAction("", UIAgent
				.getInstance().getIcon(UIAgent.ICON_SORT_16)) {

			private static final long serialVersionUID = 7486659902387687488L;

			public void actionPerformed(ActionEvent arg0) {
				sortPopupMenu.show(buttonHeader, sortButton.getX(),
						sortButton.getY() + sortButton.getHeight());
			}
		});
		sortButton.setBorderPainted(false);
		sortButton.setContentAreaFilled(false);
		sortButton.setText(null);
		sortButton.setPreferredSize(d);
		sortButton.setEnabled(false);
		buttonHeader.add(sortButton);

		return buttonHeader;
	}

	private Component createNameLabel() {
		logBookNameLabel = new JLabel();
		logBookNameLabel.setFont(UIAgent.getInstance()
				.getFontNormalItalic());
		return logBookNameLabel;
	}

	private void createSortPopup() {
		sortPopupMenu = new JPopupMenu();

		JMenuItem item = null;

		for (Action action : browserActions.getSortActions()) {
			item = new I18nMenuItem(action);
			sortPopupMenu.add(item);
		}
	}

	private Component createTree() {
		tree = new LogBookBrowserTree(browserActions, logBookManagerFacade);
		return tree;
	}

	public Dive getHighLightedDive() {
		return tree.getHighLightedDive();
	}

	public List<Dive> getHighLightedDives() {
		return tree.getHighLightedDives();
	}

	public void synchronizeLogBook() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				reset();
				LogBook lb = logBookManagerFacade.getCurrentLogBook();
				if (null != lb) {
					logBookNameLabel.setText(lb.getName());
					logBookNameLabel.setToolTipText(lb.getName());
					tree.buildTreeModel(lb);
					boolean activateButton = lb.getDives().size() > 0;
					sortButton.setEnabled(activateButton);
					collapseButton.setEnabled(activateButton);
					expandButton.setEnabled(activateButton);
					displayNumberOfDive();
				}
				revalidate();
				repaint();
			}
		});
	}

	public void reset() {
		logBookNameLabel.setText(null);
		logBookNameLabel.setToolTipText(null);
		sortButton.setEnabled(false);
		collapseButton.setEnabled(false);
		expandButton.setEnabled(false);
		
		bottomPanel.setVisible(false);
		
		tree.reset();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof LogBookEvent) {
			LogBookEvent event = (LogBookEvent) arg;
			if (event.getType().equals(LogBookEvent.LOGBOOK_LOADED)
					|| event.getType().equals(LogBookEvent.LOGBOOK_CLOSED)
					|| event.getType().equals(LogBookEvent.LOGBOOK_META_SAVED)
					|| event.getType().equals(LogBookEvent.DIVE_DELETED)
					|| event.getType().equals(LogBookEvent.DIVES_DELETED)
					|| event.getType().equals(LogBookEvent.DIVES_SAVE)) {
				synchronizeLogBook();
			} else if (event.getType().equals(LogBookEvent.DIVE_SAVE)
					|| event.getType().equals(LogBookEvent.DIVE_RELOAD)) {
				// synchronizeLogBook();
				tree.replaceDiveInNode((Dive) event.getOldValue(),
						(Dive) event.getNewValue());
				tree.refresh((Dive) event.getNewValue());
			} else if (event.getType().equals(LogBookEvent.LOGBOOK_DELETED)) {
				if (logBookManagerFacade.getCurrentLogBook() == null) {
					synchronizeLogBook();
				}
			} else if (event.getType().equals(LogBookEvent.DIVE_ADDED)) {
				tree.addDive((Dive) event.getNewValue());
				displayNumberOfDive();
			} else if (event.getType().equals(LogBookEvent.DIVE_MODIFIED)) {
				tree.refresh((Dive) event.getNewValue());
			} else if (event.getType()
					.equals(LogBookEvent.CURRENT_DIVE_CHANGED)) {
				tree.openDiveInNode((Dive) event.getNewValue());
			}
		}
	}

	public void changeLogBookBrowserNodeConstructor(
			LogBookBrowserNodeConstructor nodeConstructor) {
		tree.changeLogBookBrowserNodeConstructor(nodeConstructor);
	}

	public void expandTree() {
		tree.expandAll();
	}

	public void collapseTree() {
		tree.collapseAll();
	}

	private void displayNumberOfDive() {
		LogBook lb = logBookManagerFacade.getCurrentLogBook();
//		informationButton.setVisible(lb != null);
		bottomPanel.setVisible(lb != null);
		
		if (lb == null) {
			numberOfDiveLabel.setText(null);
			return;
		}

		String text = new String();
		if (null != lb) {
			text = String.valueOf(lb.getDives().size());
		}

		String message = I18nResourceManager.sharedInstance().getMessage(
				"dives.numberof.param", new Object[] { text });
		numberOfDiveLabel.setText(message);
	}

}
