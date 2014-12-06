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
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.view.core.logbook.LogBookEditionDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;

public class EditLogBookAction extends AbstractAction {
	private static final long serialVersionUID = -248634834970408829L;
	private static final Syslog LOGGER = Syslog
			.getLogger(EditLogBookAction.class);
	private DiverManagerFacade diverManagerFacade;
	private LogBookManagerFacade logBookManagerFacade;
	private JFrame parentFrame;

	public EditLogBookAction(JFrame parentFrame,
			LogBookManagerFacade logBookManagerFacade,
			DiverManagerFacade diverManagerFacade) {
		this.parentFrame = parentFrame;
		this.logBookManagerFacade = logBookManagerFacade;
		this.diverManagerFacade = diverManagerFacade;
		putValue(Action.NAME, "edit");
		putValue(Action.SMALL_ICON,
				UIAgent.getInstance().getIcon(UIAgent.ICON_LOGBOOK_EDIT_16));
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK));
		setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LogBookEditionDialog dialog = LogBookEditionDialog.createEditDialog(
				parentFrame, diverManagerFacade);
		dialog.setLogBookMetadata(logBookManagerFacade.getCurrentLogBook()
				.getLogbookMeta());
		int i = dialog.showDialog();
		if (i == LogBookEditionDialog.OPTION_YES) {
			try {
				logBookManagerFacade.updateLogBookMeta(dialog
						.getDisplayedLogBookMetadata());
			} catch (DataStoreException e1) {
				ExceptionDialog.showDialog(e1, parentFrame);
				LOGGER.error(e1);
			}
		}
	}

}
