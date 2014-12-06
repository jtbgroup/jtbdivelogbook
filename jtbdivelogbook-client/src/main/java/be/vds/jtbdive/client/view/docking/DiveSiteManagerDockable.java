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
import be.vds.jtbdive.client.core.GlossaryManagerFacade;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.view.core.divesite.DiveSiteManagerPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.logging.Syslog;
import bibliothek.gui.dock.common.event.CDockableAdapter;
import bibliothek.gui.dock.common.event.CDockableStateListener;
import bibliothek.gui.dock.common.intern.CDockable;

public class DiveSiteManagerDockable extends I18nDefaultSingleCDockable {

	private static final Syslog LOGGER = Syslog
			.getLogger(DiveSiteManagerDockable.class);
	private DiveSiteManagerPanel contentPanel;

	public DiveSiteManagerDockable(DiveSiteManagerFacade diveSiteManagerFacade, GlossaryManagerFacade glossaryManagerFacade) {
		super(DockingLayoutManager.VIEW_DIVESITE_MANAGER,
				DockingLayoutManager.VIEW_DIVESITE_MANAGER, UIAgent
						.getInstance().getIcon(UIAgent.ICON_DIVE_SITE_16),
				null);
		initComponents(diveSiteManagerFacade, glossaryManagerFacade);
		initListeners();
	}

	private void initComponents(DiveSiteManagerFacade diveSiteManagerFacade, GlossaryManagerFacade glossaryManagerFacade) {
		contentPanel = new DiveSiteManagerPanel(diveSiteManagerFacade, glossaryManagerFacade);
		UnitsAgent.getInstance().addObserver(contentPanel);
		getContentPane().add(contentPanel);
	}

	private void initListeners() {
		CDockableStateListener listener = new CDockableAdapter() {

			@Override
			public void visibilityChanged(CDockable cd) {
				boolean visible = DiveSiteManagerDockable.this.isVisible();
				LOGGER.debug("visibility is now : " + visible);
				if (visible) {
					UnitsAgent.getInstance().addObserver(contentPanel);
					contentPanel.updateUnitsLabels();
				} else {
					UnitsAgent.getInstance().deleteObserver(contentPanel);
				}

			}
		};
		this.addCDockableStateListener(listener);
	}
}
