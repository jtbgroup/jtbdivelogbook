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

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.core.stats.StatPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class StatisticsDockable extends I18nDefaultSingleCDockable  {

	private static StatPanel panel;

	public StatisticsDockable(LogBookManagerFacade logBookManagerFacade) {
		super(DockingLayoutManager.VIEW_STATISTICS,
				DockingLayoutManager.VIEW_STATISTICS,
				UIAgent.getInstance().getIcon(
						UIAgent.ICON_STATISTICS_16),
				createContent(logBookManagerFacade));
		setCloseable(true);
	}

	private static Component createContent(
			LogBookManagerFacade logBookManagerFacade) {
		panel = new StatPanel(logBookManagerFacade);
		return panel;
	}
	
	@Override
	public void updateLanguage() {
		super.updateLanguage();
		panel.updateLanguage();
	}


}
