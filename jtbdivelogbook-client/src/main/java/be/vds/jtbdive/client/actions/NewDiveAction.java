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
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.Palanquee;
import be.vds.jtbdive.core.core.PalanqueeEntry;
import be.vds.jtbdive.core.core.catalogs.DiverRole;

public class NewDiveAction extends AbstractAction {

	private static final long serialVersionUID = -8782349976083799039L;
	private LogBookManagerFacade logBookManagerFacade;

	public NewDiveAction(LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
		setEnabled(false);
		putValue(
				Action.SMALL_ICON,
				UIAgent.getInstance().getIcon(
						UIAgent.ICON_DIVE_DOCUMENT_ADD_16));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Dive dive = new Dive();
		LogBook logBook = logBookManagerFacade.getCurrentLogBook();
		dive.setNumber(logBook.getNextDiveNumber());
		if (logBook.getOwner() != null) {
			Palanquee p = dive.getPalanquee();
			if (p == null) {
				p = new Palanquee();
				Set<DiverRole> roles = new HashSet<DiverRole>();
				roles.add(DiverRole.ROLE_PALANQUEE_SIMPLE_DIVER);
				PalanqueeEntry pa = new PalanqueeEntry(logBook.getOwner(),
						roles);
				p.addPalanqueeEntry(pa);
			}
			dive.setPalanquee(p);
		}

		logBookManagerFacade.addDive(dive);
		logBookManagerFacade.setCurrentDive(dive);
	}
}
