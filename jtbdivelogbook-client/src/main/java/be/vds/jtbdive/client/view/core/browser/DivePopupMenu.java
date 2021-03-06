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
package be.vds.jtbdive.client.view.core.browser;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import be.smd.i18n.swing.I18nMenuItem;
import be.vds.jtbdive.client.actions.browser.DuplicateDiveAction;

public class DivePopupMenu extends JPopupMenu {

	private LogBookBrowserAction browserActions;

	public DivePopupMenu(LogBookBrowserAction browserActions) {
		this.browserActions = browserActions;
		init();
	}

	private void init() {
		JMenuItem item = new I18nMenuItem(
				browserActions
						.getAction(LogBookBrowserAction.ACTION_DELETE_DIVE));
		add(item);

		item = new I18nMenuItem(
				browserActions.getAction(DuplicateDiveAction.class.getName()));
		add(item);
	}

	public void allowMultipleSelection(boolean enable) {
		browserActions.getAction(DuplicateDiveAction.class.getName())
				.setEnabled(!enable);
	}
}
