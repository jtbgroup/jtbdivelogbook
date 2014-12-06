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
package be.vds.jtbdive.client.view.core;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import be.vds.jtbdive.client.actions.LogBookApplActionsContoller;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.GlossaryManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.docking.DockingLayoutManager;
import be.vds.jtbdive.core.core.Dive;

public class LogBookPanel extends JPanel {

	private static final long serialVersionUID = -2492634965506786337L;
	/**
	 * Contains all the static views
	 */
	private DiveSiteManagerFacade diveSiteManagerFacade;
	private DiverManagerFacade diverManagerFacade;
	private LogBookManagerFacade logBookManagerFacade;
	private GlossaryManagerFacade glossaryManagerFacade;
	private LogBookApplActionsContoller logbookActions;
	private DockingLayoutManager dockingLayoutManager;
	private StatusToolBar statusToolBar;

	public LogBookPanel(LogBookManagerFacade logBookManagerFacade,
			DiverManagerFacade diverManagerFacade,
			DiveSiteManagerFacade diveSiteManagerFacade,
			GlossaryManagerFacade glossaryManagerFacade,
			LogBookApplActionsContoller logbookActions) {
		this.diveSiteManagerFacade = diveSiteManagerFacade;
		this.diverManagerFacade = diverManagerFacade;
		this.logBookManagerFacade = logBookManagerFacade;
		this.glossaryManagerFacade = glossaryManagerFacade;
		this.logbookActions = logbookActions;

		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		dockingLayoutManager = new DockingLayoutManager(this,
				logBookManagerFacade, diverManagerFacade,
				diveSiteManagerFacade, glossaryManagerFacade, logbookActions);

		this.add(createToolBar(), BorderLayout.NORTH);
		this.add(dockingLayoutManager.getContentArea(), BorderLayout.CENTER);
		this.add(createStatusToolBar(), BorderLayout.SOUTH);

		dockingLayoutManager.layoutComponents();

	}

	private Component createStatusToolBar() {
		statusToolBar = new StatusToolBar();
		return statusToolBar;
	}

	private Component createToolBar() {
		LogBookToolBar toolBar = new LogBookToolBar(logbookActions);
		return toolBar;
	}

	public void saveLayout() {
		dockingLayoutManager.saveLayout();
	}

	public void adaptLanguage() {
		dockingLayoutManager.adaptLanguage();
	}

	public void showView(String viewKey) {
		dockingLayoutManager.showDockable(viewKey);
	}

	public Dive getDisplayedDive() {
		return dockingLayoutManager.getDisplayedDive();
	}

	public void changePerspective(int perspective) {
		dockingLayoutManager.setPerspective(perspective);
	}

}
