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
package be.vds.jtbdive.client.view.docking;

import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.core.event.DiveModification;
import be.vds.jtbdive.client.core.event.LogBookEvent;
import be.vds.jtbdive.client.view.core.dive.DivePanel;
import be.vds.jtbdive.client.view.events.DiveSiteSelectionListener;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.logging.Syslog;
import bibliothek.gui.dock.common.event.CDockableAdapter;
import bibliothek.gui.dock.common.event.CDockableStateListener;
import bibliothek.gui.dock.common.intern.CDockable;

public class DiveContainerDockable extends I18nDefaultSingleCDockable implements
		Observer {

	private static I18nResourceManager res = I18nResourceManager
			.sharedInstance();
	private static final Syslog LOGGER = Syslog
			.getLogger(DiveContainerDockable.class);
	private DivePanel divePanel;
	private JPanel defaultPanel;
	private Set<DiveSiteSelectionListener> diveSiteSelectionListeners = new HashSet <DiveSiteSelectionListener>();

	public DiveContainerDockable(LogBookManagerFacade logBookManagerFacade,
			DiverManagerFacade diverManagerFacade,
			DiveSiteManagerFacade diveSiteManagerFacade) {
		super(DockingLayoutManager.VIEW_DIVE_EDITOR,
				DockingLayoutManager.VIEW_DIVE_EDITOR, UIAgent.getInstance()
						.getIcon(UIAgent.ICON_DOCUMENT_16), null);
		initListeners();
		initComponents(logBookManagerFacade, diverManagerFacade,
				diveSiteManagerFacade);
		logBookManagerFacade.addObserver(this);
	}

	private void initComponents(LogBookManagerFacade logBookManagerFacade,
			DiverManagerFacade diverManagerFacade,
			DiveSiteManagerFacade diveSiteManagerFacade) {
		divePanel = new DivePanel(null, logBookManagerFacade,
				diverManagerFacade, diveSiteManagerFacade);
		getContentPane().add(createDefaultPanel());

		logBookManagerFacade.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				if (arg instanceof LogBookEvent) {
					LogBookEvent event = (LogBookEvent) arg;
					if (event.getType().equals(LogBookEvent.LOGBOOK_CLOSED)
							|| event.getType().equals(
									LogBookEvent.LOGBOOK_LOADED)) {
						releaseDiveEditor();
						getContentPane().add(defaultPanel);
						getContentPane().repaint();
						getContentPane().validate();
					}
				}
			}
		});
	}

	// private LogBookEventListener createEventListener() {
	// LogBookEventAdapter logBookEventAdapter = new LogBookEventAdapter(){
	// @Override
	// public void currentDiveChanged(Component source, Dive dive) {
	// displayDive(dive);
	//
	// }
	// };
	// return logBookEventAdapter;
	// }
	private void displayDive(Dive dive) {
		if (dive == null) {
			releaseDiveEditor();
		} else {
			if (getContentPane().getComponent(0).equals(defaultPanel)) {
				getContentPane().remove(defaultPanel);
				initDivePanel();
				getContentPane().add(divePanel);
				getContentPane().repaint();
				getContentPane().validate();
			}
			divePanel.setCurrentDive(dive);
			this.toFront();
		}
		updateLanguage();
		// toFront();
	}

	private JComponent createDefaultPanel() {
		defaultPanel = new JPanel();
		defaultPanel.setBackground(UIAgent.getInstance()
				.getColorBaseBackground());
		defaultPanel.setOpaque(true);
		return defaultPanel;
	}

	@Override
	public void updateLanguage() {
		setTitleText(generateTitle());
	}

	private String generateTitle() {
		if (null == divePanel || divePanel.getCurrentDive() == null) {
			return res.getString("dive.editor");
		}

		String title = "";
		title += res.getString("dive.editor") + " - ";
		title += res.getString("dive") + " "
				+ divePanel.getCurrentDive().getNumber();
		return title;
	}

	public void releaseDiveEditor() {
		if (divePanel != null) {
			diveSiteSelectionListeners.addAll(divePanel
					.getDiveSiteSelectionListeners());
		}

		divePanel.removeAllListeners();
		divePanel.setCurrentDive(null);

		getContentPane().removeAll();
		getContentPane().add(defaultPanel);
	}

	public void addDiveSiteSelectionListener(
			DiveSiteSelectionListener diveSiteSelectionListener) {
		if (divePanel == null) {
			diveSiteSelectionListeners.add(diveSiteSelectionListener);
		} else {
			divePanel.addDiveSiteSelectionListener(diveSiteSelectionListener);
		}
	}

	private void initDivePanel() {
		// divePanel = new DivePanel(null, logBookManagerFacade,
		// diverManagerFacade, diveLocationManagerFacade);

		for (DiveSiteSelectionListener l : diveSiteSelectionListeners) {
			divePanel.addDiveSiteSelectionListener(l);
		}
		diveSiteSelectionListeners.clear();
	}

	public Dive getDisplayDive() {
		if (getContentPane().getComponentCount() == 0
				|| getContentPane().getComponent(0).equals(defaultPanel)) {
			return null;
		} else {
			return divePanel.getCurrentDive();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {
		if (arg instanceof LogBookEvent) {
			LogBookEvent event = (LogBookEvent) arg;
			if (event.getType().equals(LogBookEvent.CURRENT_DIVE_CHANGED)
					|| event.getType().equals(LogBookEvent.DIVE_RELOAD)) {
				displayDive((Dive) event.getNewValue());
			} else if (event.getType().equals(LogBookEvent.LOGBOOK_CLOSED)
					|| event.getType().equals(LogBookEvent.LOGBOOK_LOADED)
					|| event.getType().equals(LogBookEvent.LOGBOOK_META_SAVED)) {
				displayDive(null);
			} else if (event.getType().equals(LogBookEvent.DIVE_DELETED)) {
				handleDiveDeleted((Dive) event.getOldValue());
			} else if (event.getType().equals(LogBookEvent.DIVES_DELETED)) {
				handleDivesDeleted((List<Dive>) event.getOldValue());
			} else if (event.getType().equals(LogBookEvent.DIVE_MODIFIED)) {
				DiveModification mod = event.getDiveModification();
				if (mod != null
						&& (DiveModification.DIVE_TIME.equals(mod) || DiveModification.DIVE_DEPTH
								.equals(mod))) {
					refreshGraph();
				}
			}
		}
	}

	private void handleDiveDeleted(Dive deletedDive) {
		Dive current = getDisplayDive();
		if (null != current && deletedDive.equals(current)) {
			displayDive(null);
		}
	}

	private void handleDivesDeleted(List<Dive> deletedDives) {
		Dive current = getDisplayDive();
		if (null != current && deletedDives.contains(current)) {
			displayDive(null);
		}
	}

	private void refreshGraph() {
		divePanel.refreshProfileGraph();
	}

	private void initListeners() {
		CDockableStateListener listener = new CDockableAdapter() {

			@Override
			public void visibilityChanged(CDockable cd) {
				boolean visible = DiveContainerDockable.this.isVisible();
				LOGGER.debug("visibility is now : " + visible);
				if (visible) {
					UnitsAgent.getInstance().addObserver(divePanel);
					divePanel.updateUnitsLabels();
				} else {
					UnitsAgent.getInstance().deleteObserver(divePanel);
				}

			}
		};
		this.addCDockableStateListener(listener);
	}

}
