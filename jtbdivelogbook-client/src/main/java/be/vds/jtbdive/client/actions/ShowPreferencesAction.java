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

import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.view.core.LogBookApplFrame;
import be.vds.jtbdive.client.view.core.preferences.UserPreferencesDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class ShowPreferencesAction extends AbstractAction {

	private static final long serialVersionUID = -7039567231698213404L;
	private LogBookApplFrame logBookApplFrame;

	public ShowPreferencesAction(LogBookApplFrame logBookApplFrame) {
		super("preferences");
		this.logBookApplFrame = logBookApplFrame;
		putValue(Action.SMALL_ICON,
				UIAgent.getInstance().getIcon(UIAgent.ICON_PREFERENCES_16));
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		UserPreferencesDialog sd = new UserPreferencesDialog(logBookApplFrame);
		sd.setSize(500, 500);
		WindowUtils.centerWindow(sd);
		sd.setVisible(true);
	}
}
