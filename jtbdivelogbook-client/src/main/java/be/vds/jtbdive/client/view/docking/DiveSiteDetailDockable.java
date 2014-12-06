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
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.view.core.divesite.DiveSiteDetailPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.logging.Syslog;
import bibliothek.gui.dock.common.event.CDockableAdapter;
import bibliothek.gui.dock.common.event.CDockableStateListener;
import bibliothek.gui.dock.common.intern.CDockable;

public class DiveSiteDetailDockable extends I18nDefaultSingleCDockable {

	private static final Syslog LOGGER = Syslog
			.getLogger(DiveSiteDetailDockable.class);
	private DiveSiteDetailPanel contentPanel;

	public DiveSiteDetailDockable(DiveSiteManagerFacade diveSiteManagerFacade) {
		super(DockingLayoutManager.VIEW_DIVESITE_DETAIL,
				DockingLayoutManager.VIEW_DIVESITE_DETAIL, UIAgent
						.getInstance().getIcon(UIAgent.ICON_DIVE_SITE_DETAIL_16),
				null);
		initComponents(diveSiteManagerFacade);
		initListeners();
	}

	private void initComponents(DiveSiteManagerFacade diveSiteManagerFacade) {
		contentPanel = new DiveSiteDetailPanel(diveSiteManagerFacade);
		UnitsAgent.getInstance().addObserver(contentPanel);
		getContentPane().add(contentPanel);
	}

	private void initListeners() {
		CDockableStateListener listener = new CDockableAdapter() {

			@Override
			public void visibilityChanged(CDockable cd) {
				boolean visible = DiveSiteDetailDockable.this.isVisible();
				LOGGER.debug("visibility is now : " + visible);
				if (visible) {
					UnitsAgent.getInstance().addObserver(contentPanel);
					contentPanel.updateUnits();
				} else {
					UnitsAgent.getInstance().deleteObserver(contentPanel);
				}

			}
		};
		this.addCDockableStateListener(listener);
	}
}
