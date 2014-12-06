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
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.view.core.LogBookApplFrame;
import be.vds.jtbdive.client.view.core.logbook.LogBookEditionDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;

public class NewLogBookAction extends AbstractAction {
	private static final long serialVersionUID = -248634834970408829L;
	private static final Syslog LOGGER = Syslog
			.getLogger(NewLogBookAction.class);
	private DiverManagerFacade diverManagerFacade;
	private LogBookManagerFacade logBookManagerFacade;
	private LogBookApplFrame logBookApplFrame;

	public NewLogBookAction(LogBookApplFrame logBookApplFrame,
			LogBookManagerFacade logBookManagerFacade,
			DiverManagerFacade diverManagerFacade) {
		this.logBookApplFrame = logBookApplFrame;
		this.logBookManagerFacade = logBookManagerFacade;
		this.diverManagerFacade = diverManagerFacade;
		putValue(Action.NAME, "new");
		putValue(Action.SMALL_ICON,
				UIAgent.getInstance().getIcon(UIAgent.ICON_LOGBOOK_ADD_16));
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!logBookApplFrame.checkIfCanBeCLosed()) {
			return;
		}

		LogBookEditionDialog dialog = LogBookEditionDialog.createNewDialog(
				logBookApplFrame, diverManagerFacade);
		int i = dialog.showDialog();
		if (i == LogBookEditionDialog.OPTION_YES) {
			try {
				logBookManagerFacade.saveLogBookMeta(dialog
						.getDisplayedLogBookMetadata());
			} catch (DataStoreException e1) {
				ExceptionDialog.showDialog(e1, logBookApplFrame);
				LOGGER.error(e1);
			}
		}
	}

}
