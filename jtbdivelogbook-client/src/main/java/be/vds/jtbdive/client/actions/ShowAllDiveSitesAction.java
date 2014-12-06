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

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.events.LogBookUiEventHandler;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.PinableObject;

public class ShowAllDiveSitesAction extends AbstractAction {
	private static final long serialVersionUID = -1841581309517915679L;
	private LogBookManagerFacade logBookManagerFacade;

	public ShowAllDiveSitesAction(LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
		putValue(Action.NAME, "divesites.show.map");
		putValue(Action.SMALL_ICON,
				UIAgent.getInstance().getIcon(UIAgent.ICON_MAP_16));
		setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<PinableObject> ds = new ArrayList<PinableObject>();
		for (DiveSite diveSite : logBookManagerFacade.getCurrentLogBook()
				.getDiveSites()) {
			if (diveSite.hasCoordinates()){
				ds.add(diveSite);
			}
		}
		LogBookUiEventHandler.getInstance().notifyPinablesSelected(ds);
	}
}
