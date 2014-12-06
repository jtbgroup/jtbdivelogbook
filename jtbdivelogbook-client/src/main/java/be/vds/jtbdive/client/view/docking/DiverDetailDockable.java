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

import be.vds.jtbdive.client.view.core.diver.DiverDetailPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.logging.Syslog;
import bibliothek.gui.dock.common.event.CDockableAdapter;
import bibliothek.gui.dock.common.event.CDockableStateListener;
import bibliothek.gui.dock.common.intern.CDockable;

public class DiverDetailDockable extends I18nDefaultSingleCDockable {
    private static final Syslog LOGGER = Syslog.getLogger(DiverDetailDockable.class);
    private DiverDetailPanel contentPanel;

    public DiverDetailDockable() {
        super(DockingLayoutManager.VIEW_DIVER_DETAIL,
				DockingLayoutManager.VIEW_DIVER_DETAIL, UIAgent.getInstance()
						.getIcon(UIAgent.ICON_DIVER_BLACK_DETAIL_16), null);
        initListeners();
        initComponents();

    }

    private void initComponents() {
         contentPanel = new DiverDetailPanel();
        getContentPane().add(contentPanel);
    }

    private void initListeners() {
        CDockableStateListener listener = new CDockableAdapter() {

            @Override
            public void visibilityChanged(CDockable cd) {
                boolean visible =  DiverDetailDockable.this.isVisible();
                LOGGER.debug("visibility is now : " +visible);

            }
        };
        this.addCDockableStateListener(listener);
    }
}
