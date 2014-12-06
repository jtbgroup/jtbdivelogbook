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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.view.core.LogBookApplFrame;
import be.vds.jtbdive.client.view.docking.DockingLayoutManager;

public class ChangePerspectiveAction extends AbstractAction {

	private static final long serialVersionUID = -7272974985894023121L;
	private LogBookApplFrame logBookApplFrame;
	private int perspective;

	public ChangePerspectiveAction(LogBookApplFrame logBookApplFrame,
			int perspective, String i18nName) {
		putValue(Action.NAME, i18nName);
		this.logBookApplFrame = logBookApplFrame;
		this.perspective = perspective;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (canPerformChangePerspective()) {
			logBookApplFrame.changePerspective(perspective);
		}
	}

	private boolean canPerformChangePerspective() {
		if (perspective == DockingLayoutManager.PERSPECTIVE_HOME
				&& logBookApplFrame.getLogBookManagerFacade()
						.getAllModifiedDives() != null
				&& logBookApplFrame.getLogBookManagerFacade()
						.getAllModifiedDives().size() > 0) {
			I18nResourceManager res = I18nResourceManager.sharedInstance();
			int i = JOptionPane.showConfirmDialog(logBookApplFrame,
					res.getString("modified.dives.exist.continue"),
					res.getString("confirm"), JOptionPane.YES_NO_OPTION);
			return i == JOptionPane.YES_OPTION;
		}
		return true;
	}

}
