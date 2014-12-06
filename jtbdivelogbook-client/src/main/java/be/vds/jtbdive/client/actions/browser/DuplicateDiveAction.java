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
package be.vds.jtbdive.client.actions.browser;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.core.browser.LogBookBrowserPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.Palanquee;
import be.vds.jtbdive.core.core.PalanqueeEntry;
import be.vds.jtbdive.core.core.catalogs.DiverRole;

public class DuplicateDiveAction extends AbstractAction {
	private static final long serialVersionUID = 6084390425591813376L;
	private LogBookBrowserPanel logBookBrowserPanel;
	private LogBookManagerFacade logBookManagerFacade;

	public DuplicateDiveAction(LogBookBrowserPanel logBookBrowserPanel,
			LogBookManagerFacade logBookManagerFacade) {
		super("dive.duplicate", UIAgent.getInstance().getIcon(
				UIAgent.ICON_DIVE_DOCUMENT_16));
		this.logBookBrowserPanel = logBookBrowserPanel;
		this.logBookManagerFacade = logBookManagerFacade;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Dive originDive = logBookBrowserPanel.getHighLightedDive();
		Dive newDive = new Dive();
		newDive.setNumber(logBookManagerFacade.getCurrentLogBook()
				.getNextDiveNumber());
		newDive.setDate(originDive.getDate());
		newDive.setDiveTime(originDive.getDiveTime());
		newDive.setMaxDepth(originDive.getMaxDepth());
		newDive.setWaterTemperature(originDive.getWaterTemperature());

		if (originDive.getPalanquee() != null) {
			Palanquee p = new Palanquee();
			for (PalanqueeEntry pe : originDive.getPalanquee()
					.getPalanqueeEntries()) {
				Set<DiverRole> drs = new HashSet<DiverRole>(pe.getRoles());
				p.addPalanqueeEntry(new PalanqueeEntry(pe.getDiver(), drs));
			}
			newDive.setPalanquee(p);
		}

		newDive.setDiveSite(originDive.getDiveSite());

		logBookManagerFacade.addDive(newDive);
		// DiveChangeInspector.getInstance().diveChanged(newDive);
	}
}
