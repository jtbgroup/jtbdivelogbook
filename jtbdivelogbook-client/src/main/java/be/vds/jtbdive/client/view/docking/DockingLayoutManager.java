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

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import be.vds.jtbdive.client.actions.ActionType;
import be.vds.jtbdive.client.actions.LogBookApplActionsContoller;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.GlossaryManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.util.ResourceManager;
import be.vds.jtbdive.client.view.core.HomePanel;
import be.vds.jtbdive.client.view.core.TasksPanel;
import be.vds.jtbdive.client.view.core.diver.DiverManagerPanel;
import be.vds.jtbdive.client.view.core.modifieddives.DivesModifiedPanel;
import be.vds.jtbdive.client.view.events.LogBookEventAdapter;
import be.vds.jtbdive.client.view.events.LogBookEventListener;
import be.vds.jtbdive.client.view.events.LogBookUiEventHandler;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.PinableObject;
import be.vds.jtbdive.core.logging.Syslog;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import bibliothek.gui.dock.common.event.CDockableStateListener;
import bibliothek.gui.dock.common.intern.AbstractCDockable;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.gui.dock.common.mode.ExtendedMode;
import bibliothek.gui.dock.util.ComponentWindowProvider;

public class DockingLayoutManager {
	private static final Syslog LOGGER = Syslog
			.getLogger(DockingLayoutManager.class);
	private CControl control;
	private LogBookManagerFacade logBookManagerFacade;
	private DiveSiteManagerFacade diveSiteManagerFacade;
	private GlossaryManagerFacade glossaryManagerFacade;
	private DiverManagerFacade diverManagerFacade;
	private LogBookApplActionsContoller logbookActions;
	public static final String VIEW_BROWSER = "browser";
	public static final String VIEW_CONSOLE = "console";
	public static final String VIEW_DIVER_MANAGER = "diver.manager";
	public static final String VIEW_DIVESITE_MANAGER = "divesite.manager";
	public static final String VIEW_DIVE_EDITOR = "dive.editor";
	public static final String VIEW_TASKS = "tasks";
	public static final String VIEW_DIVER_DETAIL = "diver.detail";
	public static final String VIEW_DIVESITE_DETAIL = "divesite.detail";
	public static final String VIEW_MODIFIED_DIVES = "modifications";
	public static final String VIEW_STATISTICS = "statistics";
	public static final String VIEW_MAT_CAVE = "matcave";
	public static final String VIEW_HOME = "home";
	public static final String VIEW_LOGBOOK_INFORMATION = "logbook";
	public static final String VIEW_SEARCH = "search";
	public static final String VIEW_MAP = "map";

	public static final int PERSPECTIVE_HOME = 1;
	public static final int PERSPECTIVE_EDITION = 2;

	private static final List<String> possibleViews;

	static {
		possibleViews = new ArrayList<String>();
		possibleViews.add(VIEW_BROWSER);
		possibleViews.add(VIEW_CONSOLE);
		possibleViews.add(VIEW_DIVER_MANAGER);
		possibleViews.add(VIEW_DIVESITE_MANAGER);
		possibleViews.add(VIEW_DIVE_EDITOR);
		possibleViews.add(VIEW_TASKS);
		possibleViews.add(VIEW_MODIFIED_DIVES);
		possibleViews.add(VIEW_DIVER_DETAIL);
		possibleViews.add(VIEW_DIVESITE_DETAIL);
		possibleViews.add(VIEW_HOME);
		possibleViews.add(VIEW_STATISTICS);
		possibleViews.add(VIEW_MAT_CAVE);
		possibleViews.add(VIEW_LOGBOOK_INFORMATION);
		possibleViews.add(VIEW_SEARCH);
		possibleViews.add(VIEW_MAP);
	}

	public DockingLayoutManager(JComponent component,
			LogBookManagerFacade logBookManagerFacade,
			DiverManagerFacade diverManagerFacade,
			DiveSiteManagerFacade diveSiteManagerFacade,
			GlossaryManagerFacade glossaryManagerFacade,
			LogBookApplActionsContoller logbookActions) {
		this.logBookManagerFacade = logBookManagerFacade;
		this.diverManagerFacade = diverManagerFacade;
		this.diveSiteManagerFacade = diveSiteManagerFacade;
		this.glossaryManagerFacade = glossaryManagerFacade;
		this.logbookActions = logbookActions;
		init(component);
	}

	private void init(JComponent component) {
		control = new CControl(new ComponentWindowProvider(component));
		control.setTheme("eclipse");
		initializeBackupFactories();
		initializeListeners();
	}

	private void initializeListeners() {
		LogBookUiEventHandler.getInstance().addEventListener(
				createLogBookEventListener());
	}

	private LogBookEventListener createLogBookEventListener() {
		LogBookEventAdapter adapter = new LogBookEventAdapter() {

			@Override
			public void pinablesSelected(List<PinableObject> pins) {
				showDockable(VIEW_MAP);
				MapDockable d = (MapDockable) control
						.getSingleDockable(VIEW_MAP);
				d.setPins(pins);
			}

			@Override
			public void pinableSelected(PinableObject pin) {
				showDockable(VIEW_MAP);
				MapDockable d = (MapDockable) control
						.getSingleDockable(VIEW_MAP);
				d.keepOnlyThisPinAndCenter(pin);
			}
		};
		return adapter;
	}

	public void resetDefaultLayout() {
		removeAllDockables();
		layoutHomePerspective();
	}

	private void layoutEditorPerspective() {
		DefaultSingleCDockable browser = createBrowserDockable();
		DefaultSingleCDockable diveEditor = createDiveEditorDockable();

		DefaultSingleCDockable diverManager = createDiverManagerDockable();
		DefaultSingleCDockable diveSiteManager = createDiveSiteManagerDockable();

		logbookActions.adaptActions();

		control.addDockable(diveEditor);
		control.addDockable(browser);
		control.addDockable(diverManager);
		control.addDockable(diveSiteManager);

		diveEditor.setVisible(true);

		browser.setLocation(CLocation.base().normalWest(0.20));
		browser.setVisible(true);

		diveSiteManager.setLocation(CLocation.base().normalSouth(0.30));
		diveSiteManager.setVisible(true);

		diverManager.setLocation(CLocation.base().normalSouth(0.30));
		diverManager.setVisible(true);
	}

	private void layoutHomePerspective() {
		DefaultSingleCDockable welcome = createHomeDockable();
		control.addDockable(welcome);
		welcome.setVisible(true);
		welcome.toFront();
	}

	private void adaptActionsForHome() {
		logbookActions.getAction(ActionType.LOGBOOK_NEW).setEnabled(false);
		logbookActions.getAction(ActionType.LOGBOOK_EDIT).setEnabled(false);
		logbookActions.getAction(ActionType.LOGBOOK_CLOSE).setEnabled(false);
		logbookActions.getAction(ActionType.LOGBOOK_OPEN).setEnabled(false);
		// logbookActions.getAction(ActionType.LOGBOOK_EXPORT_UDDF).setEnabled(
		// false);
		logbookActions.getAction(ActionType.LOGBOOK_EXPORT).setEnabled(false);
		enableViewAction(false);
	}

	private void enableViewAction(boolean enable) {
		logbookActions.getAction(ActionType.VIEW_BROWSER).setEnabled(enable);
		logbookActions.getAction(ActionType.VIEW_CONSOLE).setEnabled(enable);
		logbookActions.getAction(ActionType.VIEW_DIVE_EDITOR)
				.setEnabled(enable);
		logbookActions.getAction(ActionType.VIEW_DIVER_DETAIL).setEnabled(
				enable);
		logbookActions.getAction(ActionType.VIEW_DIVER_MANAGER).setEnabled(
				enable);
		logbookActions.getAction(ActionType.VIEW_DIVESITE_DETAIL).setEnabled(
				enable);
		logbookActions.getAction(ActionType.VIEW_DIVESITE_MANAGER).setEnabled(
				enable);
		logbookActions.getAction(ActionType.VIEW_MODIFIED_DIVES).setEnabled(
				enable);
		logbookActions.getAction(ActionType.VIEW_TASKS).setEnabled(enable);
		logbookActions.getAction(ActionType.VIEW_MAT_CAVE).setEnabled(enable);
		logbookActions.getAction(ActionType.VIEW_LOGBOOK_INFORMATION)
				.setEnabled(enable);
	}

	private void adaptActionsForEdit() {
		enableViewAction(true);
		logbookActions.getAction(ActionType.LOGBOOK_NEW).setEnabled(true);
		logbookActions.getAction(ActionType.LOGBOOK_OPEN).setEnabled(true);
		logbookActions.adaptActions();
	}

	private void removeAllDockables() {
		for (String key : possibleViews) {
			if (null != control.getSingleDockable(key))
				// control.remove(control.getSingleDockable(key));
				control.removeSingleDockable(key);
		}
	}

	public Component getContentArea() {
		return control.getContentArea();
	}

	public void saveLayout() {
		try {
			control.writeXML(ResourceManager.getInstance().getLayoutFile(true));
			LOGGER.debug("Layout saved");
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e);
		}
	}

	public void layoutComponents() {
		boolean resetBaseLayout = true;
		File layoutFile = null;
		try {
			layoutFile = ResourceManager.getInstance().getLayoutFile(false);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

		if (layoutFile != null && layoutFile.exists()) {
			try {
				control.readXML(layoutFile);
				resetBaseLayout = false;
			} catch (Exception e1) {
				LOGGER.error(e1.getMessage());
			}
		}

		if (resetBaseLayout) {
			removeAllDockables();
			layoutHomePerspective();
		}
	}

	private void initializeBackupFactories() {
		for (String key : possibleViews) {
			control.addSingleDockableFactory(key, new DockingBackUpFactory(key,
					this));
		}
	}

	public DefaultSingleCDockable createHomeDockable() {
		final HomePanel welcomePane = new HomePanel(logbookActions);
		welcomePane.enableAnimation(false);
		DefaultSingleCDockable d = new I18nDefaultSingleCDockable(VIEW_HOME,
				VIEW_HOME, UIAgent.getInstance().getIcon(UIAgent.ICON_HOME_16),
				welcomePane);
		d.setCloseable(false);
		d.setExternalizable(false);
		d.setMinimizable(false);
		d.setMaximizable(true);
		d.setResizeLocked(false);
		// d.setMinimizedHold(true);
		d.setMinimizable(true);
		d.setStackable(true);
		d.setSingleTabShown(true);

		d.addCDockableStateListener(new CDockableStateListener() {

			@Override
			public void visibilityChanged(CDockable arg0) {
				welcomePane.enableAnimation(arg0.isVisible());

				if (arg0.isVisible()) {
					adaptActionsForHome();
				} else {
					adaptActionsForEdit();
				}
			}

			@Override
			public void extendedModeChanged(CDockable arg0, ExtendedMode arg1) {
				// TODO Auto-generated method stub

			}
		});

		return d;
	}

	public DefaultSingleCDockable createDiveEditorDockable() {
		DefaultSingleCDockable d = new DiveContainerDockable(
				logBookManagerFacade, diverManagerFacade, diveSiteManagerFacade);
		d.setCloseable(false);
		return d;
	}

	public DefaultSingleCDockable createBrowserDockable() {
		BrowserDockable d = new BrowserDockable(logBookManagerFacade,
				logbookActions);
		return d;
	}

	public DefaultSingleCDockable createDiverManagerDockable() {
		DiverManagerPanel p = new DiverManagerPanel(diverManagerFacade);
		DefaultSingleCDockable d = new I18nDefaultSingleCDockable(
				VIEW_DIVER_MANAGER, VIEW_DIVER_MANAGER, UIAgent.getInstance()
						.getIcon(UIAgent.ICON_DIVER_16), p);
		d.setCloseable(true);
		return d;
	}

	public DefaultSingleCDockable createDiveSiteManagerDockable() {
		DiveSiteManagerDockable d = new DiveSiteManagerDockable(
				diveSiteManagerFacade, glossaryManagerFacade);
		d.setCloseable(true);
		return d;
	}

	public DefaultSingleCDockable createTaskPanelDockable() {
		TasksPanel p = new TasksPanel();
		DefaultSingleCDockable d = new I18nDefaultSingleCDockable(VIEW_TASKS,
				VIEW_TASKS,
				UIAgent.getInstance().getIcon(UIAgent.ICON_TASK_16), p);
		d.setCloseable(true);
		return d;
	}

	public DefaultSingleCDockable createDiveSiteDetailDockable() {
		DiveSiteDetailDockable d = new DiveSiteDetailDockable(
				diveSiteManagerFacade);
		d.setCloseable(true);
		return d;
	}

	public DefaultSingleCDockable createLogBookInformationDockable() {
		LogBookInformationDockable d = new LogBookInformationDockable(
				logBookManagerFacade);
		d.setCloseable(true);
		return d;
	}

	public DefaultSingleCDockable createSearchDockable() {
		SearchDockable d = new SearchDockable(logBookManagerFacade,
				diverManagerFacade, diveSiteManagerFacade);
		d.setCloseable(true);
		return d;
	}

	public DefaultSingleCDockable createMapDockable() {
		MapDockable d = new MapDockable(logBookManagerFacade);
		d.setCloseable(true);
		return d;
	}

	public DefaultSingleCDockable createStatisticsDockable() {
		StatisticsDockable d = new StatisticsDockable(logBookManagerFacade);
		d.setCloseable(true);
		return d;
	}

	public DefaultSingleCDockable createMatCaveDockable() {
		MatCaveDockable d = new MatCaveDockable(logBookManagerFacade);
		d.setCloseable(true);
		return d;
	}

	public DefaultSingleCDockable createDiverDetailDockable() {
		DiverDetailDockable d = new DiverDetailDockable();
		d.setCloseable(true);
		return d;
	}

	public DefaultSingleCDockable createDiveModifiedDockable() {
		DivesModifiedPanel p = new DivesModifiedPanel(logbookActions,
				logBookManagerFacade);

		if (logBookManagerFacade.getAllModifiedDives() != null) {
			p.reloadDiveChanged();
		}
		DefaultSingleCDockable d = new I18nDefaultSingleCDockable(
				VIEW_MODIFIED_DIVES, VIEW_MODIFIED_DIVES, UIAgent.getInstance()
						.getIcon(UIAgent.ICON_ATTENTION_16), p);
		d.setCloseable(true);
		return d;
	}

	public DefaultSingleCDockable createConsoleDockable() {
		ConsoleDockable d = new ConsoleDockable();
		d.setBufferSize(UserPreferences.getInstance().getLoggingBufferSize());
		d.setLogOnTop(UserPreferences.getInstance().getLogOnTop());
		return d;
	}

	public void showDockable(String key) {
		if (control.getSingleDockable(key) == null) {
			createAndDisposeDockable(key);
		}
		control.getSingleDockable(key).setVisible(true);
		if (control.getSingleDockable(key) instanceof AbstractCDockable) {
			((AbstractCDockable) control.getSingleDockable(key)).toFront();
		}
	}

	private DefaultSingleCDockable createAndDisposeDockable(String key) {
		CLocation location = CLocation.base().normalSouth(0.2);
		DefaultSingleCDockable d = null;
		if (key.equals(VIEW_BROWSER)) {
			d = createBrowserDockable();
		} else if (key.equals(VIEW_DIVE_EDITOR)) {
			d = createDiveEditorDockable();
		} else if (key.equals(VIEW_CONSOLE)) {
			d = createConsoleDockable();
		} else if (key.equals(VIEW_DIVER_MANAGER)) {
			d = createDiverManagerDockable();
		} else if (key.equals(VIEW_DIVESITE_MANAGER)) {
			d = createDiveSiteManagerDockable();
		} else if (key.equals(VIEW_DIVESITE_DETAIL)) {
			d = createDiveSiteDetailDockable();
		} else if (key.equals(VIEW_DIVER_DETAIL)) {
			d = createDiverDetailDockable();
		} else if (key.equals(VIEW_TASKS)) {
			d = createTaskPanelDockable();
		} else if (key.equals(VIEW_MODIFIED_DIVES)) {
			d = createDiveModifiedDockable();
		} else if (key.equals(VIEW_LOGBOOK_INFORMATION)) {
			d = createLogBookInformationDockable();
		} else if (key.equals(VIEW_SEARCH)) {
			d = createSearchDockable();
		} else if (key.equals(VIEW_MAP)) {
			d = createMapDockable();
			location = control.getSingleDockable(VIEW_DIVE_EDITOR)
					.getBaseLocation().aside();
		} else if (key.equals(VIEW_STATISTICS)) {
			d = createStatisticsDockable();
			location = control.getSingleDockable(VIEW_DIVE_EDITOR)
					.getBaseLocation().aside();
		} else if (key.equals(VIEW_MAT_CAVE)) {
			d = createMatCaveDockable();
			location = control.getSingleDockable(VIEW_DIVE_EDITOR)
					.getBaseLocation().aside();
		} else if (key.equals(VIEW_HOME)) {
			d = createHomeDockable();
			location = control.getSingleDockable(VIEW_DIVE_EDITOR)
					.getBaseLocation().aside();
		}

		control.addDockable(d);
		d.setLocation(location);
		return d;

	}

	public void adaptLanguage() {
		for (String key : possibleViews) {
			SingleCDockable dock = control.getSingleDockable(key);
			if (null != dock && dock instanceof I18nDefaultSingleCDockable) {
				((I18nDefaultSingleCDockable) dock).updateLanguage();
			}
		}
	}

	public Dive getDisplayedDive() {
		if ((DiveContainerDockable) control.getSingleDockable(VIEW_DIVE_EDITOR) == null)
			return null;

		return ((DiveContainerDockable) control
				.getSingleDockable(VIEW_DIVE_EDITOR)).getDisplayDive();
	}

	public void setPerspective(int perspective) {
		removeAllDockables();
		switch (perspective) {
		case PERSPECTIVE_EDITION:
			layoutEditorPerspective();
			break;
		default:
			layoutHomePerspective();
			break;
		}
	}

}
