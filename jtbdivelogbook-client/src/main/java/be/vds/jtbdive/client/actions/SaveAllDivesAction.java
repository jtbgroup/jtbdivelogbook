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

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class SaveAllDivesAction extends AbstractAction {

	private static final long serialVersionUID = 4004939352872402357L;
	private final LogBookManagerFacade logBookManagerFacade;
	private static final Syslog LOGGER = Syslog
			.getLogger(SaveAllDivesAction.class);

	public SaveAllDivesAction(LogBookManagerFacade logBookManagerFacade) {
		super("save.all",
				UIAgent.getInstance().getIcon(UIAgent.ICON_SAVE_ALL_16));
		super.setEnabled(false);
		this.logBookManagerFacade = logBookManagerFacade;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		List<Dive> dives = new ArrayList<Dive>(
				logBookManagerFacade.getAllModifiedDives());
		try {
			logBookManagerFacade.saveDives(dives);
		} catch (DataStoreException ex) {
			LOGGER.error(ex);
			ExceptionDialog.showDialog(ex, null);
		}

	}
}
