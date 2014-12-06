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
package be.vds.jtbdive.client.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;

import be.vds.jtbdive.client.actions.download.DownloadDiveFromDiveComputerAction;
import be.vds.jtbdive.client.actions.download.ImportVersion2Action;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.GlossaryManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.event.LogBookEvent;
import be.vds.jtbdive.client.view.core.LogBookApplFrame;
import be.vds.jtbdive.client.view.docking.DockingLayoutManager;
import be.vds.jtbdive.core.core.Dive;

public class LogBookApplActionsContoller implements Observer {

	private Map<ActionType, Action> actions = new HashMap<ActionType, Action>();
	private LogBookManagerFacade logBookManagerFacade;
	private DiverManagerFacade diverManagerFacade;
	private DiveSiteManagerFacade diveSiteManagerFacade;
	private GlossaryManagerFacade glossaryManagerFacade;
	private LogBookApplFrame logBookApplFrame;

	public LogBookApplActionsContoller(LogBookApplFrame logBookApplFrame,
			LogBookManagerFacade logBookManagerFacade,
			DiverManagerFacade diverManagerFacade,
			DiveSiteManagerFacade diveLocationManagerFacade,
			GlossaryManagerFacade glossaryManagerFacade) {
		this.logBookApplFrame = logBookApplFrame;
		this.logBookManagerFacade = logBookManagerFacade;
		this.diverManagerFacade = diverManagerFacade;
		this.diveSiteManagerFacade = diveLocationManagerFacade;
		this.glossaryManagerFacade = glossaryManagerFacade;

		this.logBookManagerFacade.addObserver(this);
		createActions();
	}

	private void createActions() {
		createPrintReportLogBookAction();
		createExitAction();

		createOpenLogBookAction();
		createCloseLogBookAction();
		createNewLogBookAction();
		createEditLogBookAction();
		createDeleteLogBookAction();
		createRenumerDiveAction();
		createShowAllDiveSitesAction();

		createNewDiveAction();
		createSaveDiveAction();
		createSaveAllDiveAction();
		createCancelEditDiveAction();
		createDeleteDiveAction();

		createImportDiveFromDiveComputerAction();

		createExportLogBookAction();
		createImportAnyLogBookAction();

		createShowLogBookBrowser();
		createShowDiverManager();
		createShowDiveSiteManager();
		createShowConsole();
		createShowDiveChanges();
		createShowProcesses();
		createShowEditor();
		createShowDiveSiteDetail();
		createShowDiverDetail();
		createShowStatsDock();
		createShowMatCaveDock();
		createShowLogBookInformationDock();
		createShowSearchDock();
		createShowMapDock();

		createOpenPerspectiveHome();
		createOpenPerspectiveEdition();

		createChangeConfiguration();

		createImportVersion2();
		createShowPreferencesAction();

		createCheckUpdateAction();
	}

	private void createCheckUpdateAction() {
		Action a = new CheckUpdateAction(logBookApplFrame);
		actions.put(ActionType.CHECK_UPDATES, a);
	}

	private void createShowPreferencesAction() {
		Action a = new ShowPreferencesAction(logBookApplFrame);
		actions.put(ActionType.SHOW_PREFERENCES, a);

	}

	private void createChangeConfiguration() {
		Action action = new ChangeConfigurationAction(logBookManagerFacade,
				diverManagerFacade, diveSiteManagerFacade);
		actions.put(ActionType.CHANGE_CONFIGURATION, action);
	}

	private void createImportVersion2() {
		Action action = new ImportVersion2Action(logBookManagerFacade,
				diverManagerFacade, diveSiteManagerFacade);
		actions.put(ActionType.IMPORT_VERSION_2, action);
	}

	private void createShowAllDiveSitesAction() {
		Action action = new ShowAllDiveSitesAction(logBookManagerFacade);
		action.setEnabled(false);
		actions.put(ActionType.SHOW_ALL_DIVE_SITES_ON_MAP, action);
	}

	private void createRenumerDiveAction() {
		Action action = new RenumberDivesAction(logBookManagerFacade);
		action.setEnabled(false);
		actions.put(ActionType.RENUMBER_DIVES, action);
	}

	private void createImportAnyLogBookAction() {
		Action action = new ImportDivesAction(logBookManagerFacade,
				diveSiteManagerFacade, diverManagerFacade,
				glossaryManagerFacade);
		actions.put(ActionType.LOGBOOK_IMPORT_ANY, action);
	}

	private void createExportLogBookAction() {
		Action action = new ExportDivesAction(logBookApplFrame,
				logBookManagerFacade, diveSiteManagerFacade);
		action.setEnabled(false);
		actions.put(ActionType.LOGBOOK_EXPORT, action);
	}

	private void createPrintReportLogBookAction() {
		Action action = new PrintReportLogBookAction(logBookManagerFacade,
				diveSiteManagerFacade);
		actions.put(ActionType.PRINT_REPORT, action);
	}

	private void createExitAction() {
		Action action = new ExitAction(logBookApplFrame);
		actions.put(ActionType.EXIT, action);
	}

	private void createShowProcesses() {
		ShowStaticViewAction action = new ShowStaticViewAction(
				logBookApplFrame, DockingLayoutManager.VIEW_TASKS);
		actions.put(ActionType.VIEW_TASKS, action);
	}

	private void createShowEditor() {
		ShowStaticViewAction action = new ShowStaticViewAction(
				logBookApplFrame, DockingLayoutManager.VIEW_DIVE_EDITOR);
		actions.put(ActionType.VIEW_DIVE_EDITOR, action);
	}

	private void createShowDiveSiteDetail() {
		ShowStaticViewAction action = new ShowStaticViewAction(
				logBookApplFrame, DockingLayoutManager.VIEW_DIVESITE_DETAIL);
		actions.put(ActionType.VIEW_DIVESITE_DETAIL, action);
	}

	private void createShowDiverDetail() {
		ShowStaticViewAction action = new ShowStaticViewAction(
				logBookApplFrame, DockingLayoutManager.VIEW_DIVER_DETAIL);
		actions.put(ActionType.VIEW_DIVER_DETAIL, action);
	}

	private void createOpenPerspectiveHome() {
		Action action = new ChangePerspectiveAction(logBookApplFrame,
				DockingLayoutManager.PERSPECTIVE_HOME, "welcome.panel");
		actions.put(ActionType.PERSPECTIVE_HOME, action);
	}

	private void createOpenPerspectiveEdition() {
		Action action = new ChangePerspectiveAction(logBookApplFrame,
				DockingLayoutManager.PERSPECTIVE_EDITION, "layout.default");
		actions.put(ActionType.PERSPECTIVE_EDITION, action);
	}

	private void createShowLogBookBrowser() {
		ShowStaticViewAction action = new ShowStaticViewAction(
				logBookApplFrame, DockingLayoutManager.VIEW_BROWSER);
		actions.put(ActionType.VIEW_BROWSER, action);
	}

	private void createShowDiverManager() {
		ShowStaticViewAction action = new ShowStaticViewAction(
				logBookApplFrame, DockingLayoutManager.VIEW_DIVER_MANAGER);
		actions.put(ActionType.VIEW_DIVER_MANAGER, action);
	}

	private void createShowDiveSiteManager() {
		ShowStaticViewAction action = new ShowStaticViewAction(
				logBookApplFrame, DockingLayoutManager.VIEW_DIVESITE_MANAGER);
		actions.put(ActionType.VIEW_DIVESITE_MANAGER, action);
	}

	private void createShowConsole() {
		ShowStaticViewAction action = new ShowStaticViewAction(
				logBookApplFrame, DockingLayoutManager.VIEW_CONSOLE);
		actions.put(ActionType.VIEW_CONSOLE, action);
	}

	private void createShowDiveChanges() {
		ShowStaticViewAction action = new ShowStaticViewAction(
				logBookApplFrame, DockingLayoutManager.VIEW_MODIFIED_DIVES);
		actions.put(ActionType.VIEW_MODIFIED_DIVES, action);
	}

	private void createShowStatsDock() {
		ShowStaticViewAction action = new ShowStaticViewAction(
				logBookApplFrame, DockingLayoutManager.VIEW_STATISTICS);
		actions.put(ActionType.VIEW_STATISTICS, action);
	}

	private void createShowLogBookInformationDock() {
		ShowStaticViewAction action = new ShowStaticViewAction(
				logBookApplFrame, DockingLayoutManager.VIEW_LOGBOOK_INFORMATION);
		actions.put(ActionType.VIEW_LOGBOOK_INFORMATION, action);
	}

	private void createShowSearchDock() {
		ShowStaticViewAction action = new ShowStaticViewAction(
				logBookApplFrame, DockingLayoutManager.VIEW_SEARCH);
		actions.put(ActionType.VIEW_SEARCH, action);
	}

	private void createShowMapDock() {
		ShowStaticViewAction action = new ShowStaticViewAction(
				logBookApplFrame, DockingLayoutManager.VIEW_MAP);
		actions.put(ActionType.VIEW_MAP, action);
	}

	private void createShowMatCaveDock() {
		ShowStaticViewAction action = new ShowStaticViewAction(
				logBookApplFrame, DockingLayoutManager.VIEW_MAT_CAVE);
		actions.put(ActionType.VIEW_MAT_CAVE, action);
	}


	private void createImportDiveFromDiveComputerAction() {
		Action action = new DownloadDiveFromDiveComputerAction(logBookApplFrame);
		actions.put(ActionType.IMPORT_DIVE_COMPUTER, action);
	}

	private void createSaveDiveAction() {
		SaveDiveAction action = new SaveDiveAction(logBookManagerFacade,
				logBookApplFrame);
		actions.put(ActionType.SAVE_DIVE, action);
	}

	private void createSaveAllDiveAction() {
		Action action = new SaveAllDivesAction(
				logBookApplFrame.getLogBookManagerFacade());
		actions.put(ActionType.SAVE_ALL_DIVE, action);
	}

	private void createDeleteDiveAction() {
		DeleteDiveAction action = new DeleteDiveAction(logBookManagerFacade);
		actions.put(ActionType.DELETE_DIVE, action);
	}

	private void createCancelEditDiveAction() {
		CancelEditionDiveAction action = new CancelEditionDiveAction(
				logBookManagerFacade, logBookApplFrame);
		actions.put(ActionType.CANCEL_DIVE_CHANGES, action);
	}

	private void createNewDiveAction() {
		NewDiveAction action = new NewDiveAction(logBookManagerFacade);
		actions.put(ActionType.NEW_DIVE, action);
	}

	private void createCloseLogBookAction() {
		Action action = new CloseLogBookAction(logBookManagerFacade,
				logBookApplFrame);
		action.setEnabled(false);
		actions.put(ActionType.LOGBOOK_CLOSE, action);
	}

	private void createEditLogBookAction() {
		Action action = new EditLogBookAction(logBookApplFrame,
				logBookManagerFacade, diverManagerFacade);
		actions.put(ActionType.LOGBOOK_EDIT, action);
	}

	private void createDeleteLogBookAction() {
		Action action = new DeleteLogBookAction(logBookApplFrame,
				logBookManagerFacade);
		actions.put(ActionType.LOGBOOK_DELETE, action);
	}

	private void createNewLogBookAction() {
		Action action = new NewLogBookAction(logBookApplFrame,
				logBookManagerFacade, diverManagerFacade);
		actions.put(ActionType.LOGBOOK_NEW, action);
	}

	private void createOpenLogBookAction() {
		Action action = new OpenLogBookAction(logBookApplFrame,
				logBookManagerFacade);
		actions.put(ActionType.LOGBOOK_OPEN, action);
	}

	public Action getAction(ActionType key) {
		return actions.get(key);
	}

	public List<Action> getActionsShowView() {
		List<ActionType> at = ActionType.getShowViewActionTypes();
		List<Action> actions = new ArrayList<Action>();
		for (ActionType a : at) {
			actions.add(getAction(a));
		}
		return actions;
	}

	public void adaptActions() {
		boolean isLogbookSelected = logBookManagerFacade.getCurrentLogBook() != null;

		getAction(ActionType.LOGBOOK_CLOSE).setEnabled(isLogbookSelected);
		getAction(ActionType.LOGBOOK_EDIT).setEnabled(isLogbookSelected);

		getAction(ActionType.NEW_DIVE).setEnabled(isLogbookSelected);

		getAction(ActionType.IMPORT_DIVE_COMPUTER)
				.setEnabled(isLogbookSelected);
		getAction(ActionType.LOGBOOK_EXPORT).setEnabled(isLogbookSelected);

		getAction(ActionType.PRINT_REPORT).setEnabled(isLogbookSelected);
		getAction(ActionType.SHOW_ALL_DIVE_SITES_ON_MAP).setEnabled(
				isLogbookSelected);
		getAction(ActionType.RENUMBER_DIVES).setEnabled(isLogbookSelected);

		Dive displayedDive = logBookApplFrame.getDisplayedDive();
		if (displayedDive == null) {
			getAction(ActionType.SAVE_DIVE).setEnabled(false);
		} else {
			getAction(ActionType.SAVE_DIVE).setEnabled(
					logBookManagerFacade.isDiveModified(displayedDive));
		}

		getAction(ActionType.SAVE_ALL_DIVE).setEnabled(
				logBookManagerFacade.getAllModifiedDives().size() > 0);

		adaptCancelActions();
	}

	private void adaptCancelActions() {
		Dive displayedDive = logBookManagerFacade.getCurrentDive();
		boolean isDiveDisplayed = displayedDive != null;
		boolean isDisplayedDiveModified = isDiveDisplayed
				&& logBookManagerFacade.getAllModifiedDives().contains(
						displayedDive);
		getAction(ActionType.DELETE_DIVE).setEnabled(isDiveDisplayed);
		getAction(ActionType.CANCEL_DIVE_CHANGES).setEnabled(
				isDisplayedDiveModified);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o.equals(logBookManagerFacade)) {
			if (arg instanceof LogBookEvent) {
				LogBookEvent event = (LogBookEvent) arg;
				if (event.getType().equals(LogBookEvent.LOGBOOK_LOADED)) {
					adaptActions();
				} else if (event.getType().equals(
						LogBookEvent.LOGBOOK_META_SAVED)) {
					adaptActions();
				} else if (event.getType().equals(LogBookEvent.LOGBOOK_CLOSED)) {
					adaptActions();
				} else if (event.getType().equals(LogBookEvent.DIVES_SAVE)) {
					adaptActions();
				} else if (event.getType().equals(
						LogBookEvent.CURRENT_DIVE_CHANGED)) {
					adaptActions();
				} else if (event.getType().equals(LogBookEvent.DIVE_ADDED)) {
					getAction(ActionType.SAVE_ALL_DIVE).setEnabled(true);
				} else if (event.getType().equals(LogBookEvent.DIVE_MODIFIED)) {
					adaptActions();
				} else if (event.getType().equals(LogBookEvent.DIVE_RELOAD)) {
					adaptActions();
				}
			}
		}
	}
}
