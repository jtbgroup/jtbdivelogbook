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
package be.vds.jtbdive.client.view.core;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;

import be.smd.i18n.swing.I18nButton;
import be.vds.jtbdive.client.actions.ActionType;
import be.vds.jtbdive.client.actions.LogBookApplActionsContoller;

public class LogBookToolBar extends JToolBar {

	private static final long serialVersionUID = -2813336239605362535L;
	private LogBookApplActionsContoller logbookActions;

	public LogBookToolBar(LogBookApplActionsContoller logbookActions) {
		super("Drag me back...");
		this.logbookActions = logbookActions;
		init();
	}

	private void init() {
		this.add(createToolBarButton(
				logbookActions.getAction(ActionType.NEW_DIVE), "new"));
		this.add(createToolBarButton(
				logbookActions.getAction(ActionType.SAVE_DIVE), "save"));
		this.add(createToolBarButton(
				logbookActions.getAction(ActionType.SAVE_ALL_DIVE), "save.all"));
		this.add(createToolBarButton(
				logbookActions.getAction(ActionType.CANCEL_DIVE_CHANGES),
				"changes.cancel"));
		this.add(createToolBarButton(
				logbookActions.getAction(ActionType.DELETE_DIVE), "delete"));

		this.add(new JToolBar.Separator());

		this.add(createToolBarButton(
				logbookActions.getAction(ActionType.IMPORT_DIVE_COMPUTER),
				"divecomputer.download"));
	}

	private JButton createToolBarButton(Action action, String toolTipKey) {
		I18nButton b = new I18nButton(action);
		b.setTooltipTextBundleKey(toolTipKey);
		b.setToolTipText(null);
		b.setText(null);
		b.setTextBundleKey(null);
		b.setFocusable(false);
		return b;
	}
}
