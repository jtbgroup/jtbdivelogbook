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

import be.vds.jtbdive.client.view.core.LogBookApplFrame;
import be.vds.jtbdive.client.view.docking.DockingLayoutManager;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class ShowStaticViewAction extends AbstractAction {
	private static final long serialVersionUID = 789920529018609182L;
	private LogBookApplFrame logBookApplFrame;
	private String viewKey;
	private static final int ACCELERATOR_KEY = KeyEvent.CTRL_DOWN_MASK
			+ KeyEvent.SHIFT_DOWN_MASK;

	public ShowStaticViewAction(LogBookApplFrame logBookApplFrame,
			String viewKey) {
		this.viewKey = viewKey;
		this.logBookApplFrame = logBookApplFrame;
		setActionsParams();
	}

	private void setActionsParams() {
		if (DockingLayoutManager.VIEW_BROWSER.equals(viewKey)) {
			putValue(Action.NAME, "browser");
			putValue(Action.SMALL_ICON,
					UIAgent.getInstance().getIcon(UIAgent.ICON_HIERARCHY_16));
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_B, ACCELERATOR_KEY));
		} else if (DockingLayoutManager.VIEW_CONSOLE.equals(viewKey)) {
			putValue(Action.NAME, "console");
			putValue(Action.SMALL_ICON,
					UIAgent.getInstance().getIcon(UIAgent.ICON_CONSOLE_16));
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_C, ACCELERATOR_KEY));
		} else if (DockingLayoutManager.VIEW_DIVESITE_MANAGER.equals(viewKey)) {
			putValue(Action.NAME, "divesite.manager");
			putValue(Action.SMALL_ICON,
					UIAgent.getInstance().getIcon(UIAgent.ICON_DIVE_SITE_16));
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_L, ACCELERATOR_KEY));
		} else if (DockingLayoutManager.VIEW_DIVER_MANAGER.equals(viewKey)) {
			putValue(Action.NAME, "diver.manager");
			putValue(Action.SMALL_ICON,
					UIAgent.getInstance().getIcon(UIAgent.ICON_DIVER_16));
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_D, ACCELERATOR_KEY));
		} else if (DockingLayoutManager.VIEW_DIVE_EDITOR.equals(viewKey)) {
			putValue(Action.NAME, "editor");
			putValue(Action.SMALL_ICON,
					UIAgent.getInstance().getIcon(UIAgent.ICON_DIVE_EDITOR_16));
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_E, ACCELERATOR_KEY));
		} else if (DockingLayoutManager.VIEW_TASKS.equals(viewKey)) {
			putValue(Action.NAME, "tasks");
			putValue(Action.SMALL_ICON,
					UIAgent.getInstance().getIcon(UIAgent.ICON_TASK_16));
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_T, ACCELERATOR_KEY));
		} else if (DockingLayoutManager.VIEW_MODIFIED_DIVES.equals(viewKey)) {
			putValue(Action.NAME, "dives.modified");
			putValue(Action.SMALL_ICON,
					UIAgent.getInstance().getIcon(UIAgent.ICON_ATTENTION_16));
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_Q, ACCELERATOR_KEY));
		} else if (DockingLayoutManager.VIEW_DIVER_DETAIL.equals(viewKey)) {
			putValue(Action.NAME, "diver.detail");
			putValue(Action.SMALL_ICON,
					UIAgent.getInstance().getIcon(UIAgent.ICON_DIVER_BLACK_DETAIL_16));
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_K, ACCELERATOR_KEY));
		} else if (DockingLayoutManager.VIEW_DIVESITE_DETAIL.equals(viewKey)) {
			putValue(Action.NAME, "divesite.detail");
			putValue(Action.SMALL_ICON,
					UIAgent.getInstance().getIcon(UIAgent.ICON_DIVE_SITE_DETAIL_16));
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_F, ACCELERATOR_KEY));
		} else if (DockingLayoutManager.VIEW_HOME.equals(viewKey)) {
			putValue(Action.NAME, "home");
			putValue(Action.SMALL_ICON,
					UIAgent.getInstance().getIcon(UIAgent.ICON_HOME_16));
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_H, ACCELERATOR_KEY));
		} else if (DockingLayoutManager.VIEW_STATISTICS.equals(viewKey)) {
			putValue(Action.NAME, "statistics");
			putValue(Action.SMALL_ICON,
					UIAgent.getInstance().getIcon(UIAgent.ICON_STATISTICS_16));
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_A, ACCELERATOR_KEY));
		} else if (DockingLayoutManager.VIEW_MAT_CAVE.equals(viewKey)) {
			putValue(Action.NAME, "matcave");
			putValue(Action.SMALL_ICON,
					UIAgent.getInstance().getIcon(UIAgent.ICON_MATCAVE_16));
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_U, ACCELERATOR_KEY));
		} else if (DockingLayoutManager.VIEW_LOGBOOK_INFORMATION
				.equals(viewKey)) {
			putValue(Action.NAME, "logbook.information");
			putValue(Action.SMALL_ICON,
					UIAgent.getInstance().getIcon(UIAgent.ICON_INFO_16));
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_I, ACCELERATOR_KEY));
		} else if (DockingLayoutManager.VIEW_SEARCH.equals(viewKey)) {
			putValue(Action.NAME, "search");
			putValue(Action.SMALL_ICON,
					UIAgent.getInstance().getIcon(UIAgent.ICON_SEARCH_16));
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_S, ACCELERATOR_KEY));
		} else if (DockingLayoutManager.VIEW_MAP.equals(viewKey)) {
			putValue(Action.NAME, "map");
			putValue(Action.SMALL_ICON,
					UIAgent.getInstance().getIcon(UIAgent.ICON_MAP_16));
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_M, ACCELERATOR_KEY));
		} else {
			putValue(Action.NAME, "show action");
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		logBookApplFrame.showView(viewKey);
	}

	public String getViewKey() {
		return viewKey;
	}

}
