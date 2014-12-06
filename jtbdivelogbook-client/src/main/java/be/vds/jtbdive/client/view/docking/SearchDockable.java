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

import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.core.search.SearchPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class SearchDockable extends I18nDefaultSingleCDockable {
	private SearchPanel contentPanel;

	public SearchDockable(LogBookManagerFacade logBookManagerFacade,
			DiverManagerFacade diverManagerFacade,
			DiveSiteManagerFacade diveSiteManagerFacade) {
		super(DockingLayoutManager.VIEW_SEARCH,
				DockingLayoutManager.VIEW_SEARCH, UIAgent.getInstance()
						.getIcon(UIAgent.ICON_SEARCH_16), null);
		initComponents(logBookManagerFacade, diverManagerFacade,
				diveSiteManagerFacade);
	}

	private void initComponents(LogBookManagerFacade logBookManagerFacade,
			DiverManagerFacade diverManagerFacade,
			DiveSiteManagerFacade diveSiteManagerFacade) {
		contentPanel = new SearchPanel(logBookManagerFacade,
				diverManagerFacade, diveSiteManagerFacade);

		if (logBookManagerFacade.getCurrentLogBook() != null)
			contentPanel.enableCriteriaButton(true);

		getContentPane().add(contentPanel);
	}

}
