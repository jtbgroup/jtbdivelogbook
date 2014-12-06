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

import java.util.ArrayList;
import java.util.List;

public enum ActionType {
	SHOW_PREFERENCES(false), //
	CHANGE_CONFIGURATION(false), //
	PRINT_REPORT(false), //
	RENUMBER_DIVES(false), //
	IMPORT_VERSION_2(false), //
	EXIT(false), //
	// IMPORT_DIVE_BINARY(false), //
	IMPORT_DIVE_COMPUTER(false), //
	CANCEL_DIVE_CHANGES(false), //
	SAVE_DIVE(false), //
	DELETE_DIVE(false), //
	SAVE_ALL_DIVE(false), //
	NEW_DIVE(false), //
	LOGBOOK_CLOSE(false), //
	LOGBOOK_EDIT(false), //
	LOGBOOK_DELETE(false), //
	LOGBOOK_NEW(false), //
	LOGBOOK_OPEN(false), //
	LOGBOOK_EXPORT(false), //
	LOGBOOK_IMPORT_ANY(false), //
	PERSPECTIVE_HOME(false), //
	PERSPECTIVE_EDITION(false), //
	CHECK_UPDATES(false), //
	REPORT_BUG(false),//
	SHOW_ALL_DIVE_SITES_ON_MAP(false), //
	// VIEWS
	VIEW_BROWSER(true), //
	VIEW_DIVE_EDITOR(true), //
	VIEW_DIVER_MANAGER(true), //
	VIEW_DIVESITE_MANAGER(true), //
	VIEW_MODIFIED_DIVES(true), //
	VIEW_CONSOLE(true), //
	VIEW_TASKS(true), //
	VIEW_DIVER_DETAIL(true), //
	VIEW_DIVESITE_DETAIL(true), //
	VIEW_STATISTICS(true), //
	VIEW_MAT_CAVE(true), //
	VIEW_LOGBOOK_INFORMATION(true), //
	VIEW_SEARCH(true), // 
	VIEW_MAP(true), // 
	;

	private boolean isShowPanel;

	private ActionType(boolean isShowPanel) {
		this.isShowPanel = isShowPanel;
	}

	public static List<ActionType> getShowViewActionTypes() {
		List<ActionType> types = new ArrayList<ActionType>();
		for (ActionType actionType : values()) {
			if (actionType.isShowPanel)
				types.add(actionType);
		}
		return types;
	}

}
