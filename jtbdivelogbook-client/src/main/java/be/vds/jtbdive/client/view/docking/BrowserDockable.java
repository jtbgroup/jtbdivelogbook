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

import be.vds.jtbdive.client.actions.LogBookApplActionsContoller;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.core.browser.LogBookBrowserPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class BrowserDockable extends I18nDefaultSingleCDockable {

	private LogBookBrowserPanel browserPanel;

	public BrowserDockable(LogBookManagerFacade logBookManagerFacade, LogBookApplActionsContoller actions) {
		super(DockingLayoutManager.VIEW_BROWSER,
				DockingLayoutManager.VIEW_BROWSER,UIAgent.getInstance().getIcon(UIAgent.ICON_HIERARCHY_16), null);
		add(createContent(logBookManagerFacade, actions));
		setCloseable(true);
	}

	private Component createContent(
			LogBookManagerFacade logBookManagerFacade, LogBookApplActionsContoller actions) {
		browserPanel = new LogBookBrowserPanel(logBookManagerFacade, actions);
		browserPanel.synchronizeLogBook();
		return browserPanel;
	}

}
