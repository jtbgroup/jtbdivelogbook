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

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.core.LogBookApplFrame;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class CloseLogBookAction extends AbstractAction {
	private static final long serialVersionUID = -4307397239546698654L;
	private LogBookManagerFacade logBookManagerFacade;
	private LogBookApplFrame frame;

	public CloseLogBookAction(LogBookManagerFacade logBookManagerFacade,
			LogBookApplFrame frame) {
		this.logBookManagerFacade = logBookManagerFacade;
		this.frame = frame;
		putValue(Action.NAME, "close");
		putValue(Action.SMALL_ICON,
				UIAgent.getInstance().getIcon(UIAgent.ICON_LOGBOOK_CLOSE_16));
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (frame.checkIfCanBeCLosed()) {
			logBookManagerFacade.clearCurrentLogBook();
		}
	}
}
