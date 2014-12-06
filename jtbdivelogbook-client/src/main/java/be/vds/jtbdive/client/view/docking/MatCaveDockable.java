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

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.core.logbook.MatCavePanel;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class MatCaveDockable extends I18nDefaultSingleCDockable {

	private MatCavePanel contentPanel;

	public MatCaveDockable(LogBookManagerFacade logBookManagerFacade) {
		super(DockingLayoutManager.VIEW_MAT_CAVE,
				DockingLayoutManager.VIEW_MAT_CAVE, UIAgent.getInstance()
						.getIcon(UIAgent.ICON_MATCAVE_16), null);
		initContentPane(logBookManagerFacade);
		setCloseable(true);

	}

	private void initContentPane(LogBookManagerFacade logBookManagerFacade) {
		contentPanel = new MatCavePanel(logBookManagerFacade);
		getContentPane().add(contentPanel);
		contentPanel.synchronizeWithLogBook();
	}

}
