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

import org.apache.log4j.Logger;

import bibliothek.gui.dock.common.SingleCDockable;
import bibliothek.gui.dock.common.SingleCDockableFactory;

public class DockingBackUpFactory implements SingleCDockableFactory {
	private Logger LOGGER = Logger.getLogger(DockingBackUpFactory.class);
	private String key;
	private DockingLayoutManager dockingLayoutManager;

	public DockingBackUpFactory(String key,
			DockingLayoutManager dockingLayoutManager) {
		this.key = key;
		this.dockingLayoutManager = dockingLayoutManager;
	}

	@Override
	public SingleCDockable createBackup(String arg0) {
		SingleCDockable d = null;
		boolean ok = true;

		if (key == DockingLayoutManager.VIEW_BROWSER) {
			d = dockingLayoutManager.createBrowserDockable();
		} else if (key == DockingLayoutManager.VIEW_CONSOLE) {
			d = dockingLayoutManager.createConsoleDockable();
		} else if (key == DockingLayoutManager.VIEW_DIVER_MANAGER) {
			d = dockingLayoutManager.createDiverManagerDockable();
		} else if (key == DockingLayoutManager.VIEW_DIVESITE_MANAGER) {
			d = dockingLayoutManager.createDiveSiteManagerDockable();
		} else if (key == DockingLayoutManager.VIEW_DIVE_EDITOR) {
			d = dockingLayoutManager.createDiveEditorDockable();
		} else if (key == DockingLayoutManager.VIEW_TASKS) {
			d = dockingLayoutManager.createTaskPanelDockable();
		} else if (key == DockingLayoutManager.VIEW_DIVER_DETAIL) {
			d = dockingLayoutManager.createDiverDetailDockable();
		} else if (key == DockingLayoutManager.VIEW_DIVESITE_DETAIL) {
			d = dockingLayoutManager.createDiveSiteDetailDockable();
		} else if (key == DockingLayoutManager.VIEW_MODIFIED_DIVES) {
			d = dockingLayoutManager.createDiveModifiedDockable();
		} else if (key == DockingLayoutManager.VIEW_STATISTICS) {
			d = dockingLayoutManager.createStatisticsDockable();
		} else if (key == DockingLayoutManager.VIEW_MAT_CAVE) {
			d = dockingLayoutManager.createMatCaveDockable();
		} else if (key == DockingLayoutManager.VIEW_LOGBOOK_INFORMATION) {
			d = dockingLayoutManager.createLogBookInformationDockable();
		} else if (key == DockingLayoutManager.VIEW_SEARCH) {
			d = dockingLayoutManager.createSearchDockable();
		} else if (key == DockingLayoutManager.VIEW_MAP) {
			d = dockingLayoutManager.createMapDockable();
		} else if (key == DockingLayoutManager.VIEW_HOME) {
			d = dockingLayoutManager.createHomeDockable();
		} else {
			ok = false;
		}

		if (!ok) {
			LOGGER.warn("Couldn't create dock for key : " + key);
		} else {
			LOGGER.debug("creating dock for key : " + key);
		}

		return d;
	}
}
