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

import be.vds.jtbdive.client.view.core.LogBookApplFrame;
import be.vds.jtbdive.client.view.utils.UIAgent;

/**
 * This class will create and dispatch a WINDOW_CLOSING event to the active
 * frame. As a result a request to close the frame will be made and any
 * WindowListener that handles the windowClosing event will be executed. Since
 * clicking on the "Close" button of the frame or selecting the "Close" option
 * from the system menu also invoke the WindowListener, this will provide a
 * common exit point for the application.
 */
public class ExitAction extends AbstractAction {
	private static final long serialVersionUID = 1961905684268763009L;
	private LogBookApplFrame logbookApplFrame;

	public ExitAction(LogBookApplFrame logbookApplFrame) {
		super("exit", UIAgent.getInstance().getIcon(UIAgent.ICON_EXIT_16));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_X));
		this.logbookApplFrame = logbookApplFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (logbookApplFrame.checkIfCanBeCLosed())
			logbookApplFrame.closeWindow();
	}
}
