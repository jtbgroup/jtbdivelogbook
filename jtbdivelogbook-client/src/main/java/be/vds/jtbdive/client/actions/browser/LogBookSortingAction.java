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
package be.vds.jtbdive.client.actions.browser;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import be.vds.jtbdive.client.view.core.browser.LogBookBrowserPanel;
import be.vds.jtbdive.client.view.core.browser.LogBookSortingHelper;
import be.vds.jtbdive.client.view.core.browser.nodeconstructor.LogBookSorting;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class LogBookSortingAction extends AbstractAction {

	private static final long serialVersionUID = -940969799842253170L;
	private LogBookSorting logbookSorting;
	private LogBookBrowserPanel logBookBrowserPanel;

	public LogBookSortingAction(LogBookSorting logbookSorting,
			LogBookBrowserPanel logBookBrowserPanel) {
		this.logbookSorting = logbookSorting;
		this.logBookBrowserPanel = logBookBrowserPanel;
		setAttributes();
	}

	private void setAttributes() {
		switch (logbookSorting) {
		case DIVE_DATE:
			putValue(Action.SMALL_ICON, UIAgent.getInstance().getIcon(
					UIAgent.ICON_SORT_DAY_16));
			putValue(Action.NAME, "by.date");
			break;
		case DIVE_NUMBER:
			putValue(Action.NAME, "by.number");
			putValue(Action.SMALL_ICON, UIAgent.getInstance().getIcon(
					UIAgent.ICON_SORT_NUMBER_16));
			break;
		case DIVE_SITE:
			putValue(Action.SMALL_ICON, UIAgent.getInstance().getIcon(
					UIAgent.ICON_SORT_DIVE_SITE_16));
			putValue(Action.NAME, "by.divesite");
			break;
		case DIVE_YEAR:
			putValue(Action.SMALL_ICON, UIAgent.getInstance().getIcon(
					UIAgent.ICON_SORT_YEAR_16));
			putValue(Action.NAME, "by.year");
			break;
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
			logBookBrowserPanel
					.changeLogBookBrowserNodeConstructor(LogBookSortingHelper.getNodeConstructor(logbookSorting));
	}
}
