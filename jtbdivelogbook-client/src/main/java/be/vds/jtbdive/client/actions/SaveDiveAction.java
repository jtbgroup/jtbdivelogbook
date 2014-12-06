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
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.exceptions.DataStoreException;

public class SaveDiveAction extends AbstractAction {

	private static final long serialVersionUID = 5697856375375512005L;
	public static final KeyStroke SAVE_DIVE_KEYSTROKE = KeyStroke.getKeyStroke(
			KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
	private LogBookManagerFacade logBookManagerFacade;
	private LogBookApplFrame logBookApplFrame;

	public SaveDiveAction(LogBookManagerFacade logBookManagerFacade,
			LogBookApplFrame logBookApplFrame) {
		super("save.dive");
		this.logBookManagerFacade = logBookManagerFacade;
		this.logBookApplFrame = logBookApplFrame;
		setEnabled(false);
		putValue(Action.SMALL_ICON,
				UIAgent.getInstance().getIcon(UIAgent.ICON_SAVE_16));
		// putValue(Action.ACCELERATOR_KEY, SAVE_DIVE_KEYSTROKE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Dive dive = logBookApplFrame.getDisplayedDive();

		try {
			logBookManagerFacade.saveDive(dive);
		} catch (DataStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
