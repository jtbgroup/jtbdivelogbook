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

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import be.smd.i18n.swing.I18nMenu;
import be.smd.i18n.swing.I18nMenuItem;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.actions.ActionType;
import be.vds.jtbdive.client.actions.LogBookApplActionsContoller;
import be.vds.jtbdive.client.actions.ReportBugAction;
import be.vds.jtbdive.client.actions.ShowRevisionAction;
import be.vds.jtbdive.client.actions.SwitchLanguageAction;
import be.vds.jtbdive.client.util.LanguageHelper;
import be.vds.jtbdive.client.util.LocalesComparator;
import be.vds.jtbdive.client.view.core.about.AboutWindow;
import be.vds.jtbdive.client.view.utils.UIAgent;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class LogBookApplMenuBar extends JMenuBar {

	private static final long serialVersionUID = -4517929238729326367L;
	private LogBookApplActionsContoller logBookApplActions;
	private LogBookApplFrame logBookApplicationFrame;

	public LogBookApplMenuBar(LogBookApplFrame logBookApplicationFrame,
			LogBookApplActionsContoller logBookApplActions) {
		this.logBookApplicationFrame = logBookApplicationFrame;
		this.logBookApplActions = logBookApplActions;
		init();
	}

	private void init() {
		createFileMenu();
		createDisplayMenu();
		createToolsMenu();
		createHelpMenu();
	}

	private void createToolsMenu() {
		JMenu toolsMenu = new I18nMenu("tools");
		toolsMenu.add(createLanguageMenu());
		toolsMenu.add(createPrintReportLogBookItem());
		// toolsMenu.add(createImportMenu());

		toolsMenu.addSeparator();
		toolsMenu.add(createExportItem());
		toolsMenu.add(createImportItem());

		toolsMenu.addSeparator();
		toolsMenu.add(createLogBookMenu());

		toolsMenu.addSeparator();
		I18nMenuItem item = new I18nMenuItem("dive.computer.connection");
		item.setAction(logBookApplActions
				.getAction(ActionType.IMPORT_DIVE_COMPUTER));
		toolsMenu.add(item);

		this.add(toolsMenu);
	}

	private JMenuItem createLogBookMenu() {
		JMenu menu = new I18nMenu("logbook");
		menu.add(createEditLogBookMenu());
		menu.add(createDeleteLogBookMenu());
		menu.addSeparator();
		menu.add(createRenumberDive());
		menu.add(createShowAllDiveSites());
		return menu;

	}



	private JMenuItem createChangeConfigurationMenu() {
		JMenuItem item = new I18nMenuItem(
				logBookApplActions.getAction(ActionType.CHANGE_CONFIGURATION));
		return item;
	}

	private JMenuItem createRenumberDive() {
		JMenuItem item = new I18nMenuItem(
				logBookApplActions.getAction(ActionType.RENUMBER_DIVES));
		return item;
	}
	
	private JMenuItem createShowAllDiveSites() {
		JMenuItem item = new I18nMenuItem(
				logBookApplActions.getAction(ActionType.SHOW_ALL_DIVE_SITES_ON_MAP));
		return item;
	}

	private JMenuItem createExportItem() {
		JMenuItem item = new I18nMenuItem(
				logBookApplActions.getAction(ActionType.LOGBOOK_EXPORT));
		item.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_EXPORT_16));

		return item;
	}

	private JMenuItem createImportItem() {
		JMenuItem item = new I18nMenuItem(
				logBookApplActions.getAction(ActionType.LOGBOOK_IMPORT_ANY));
		item.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_IMPORT_16));

		return item;
	}

	private void createDisplayMenu() {
		JMenu menu = new I18nMenu("display");
		menu.add(createShowMenu());
		menu.addSeparator();
		menu.add(new I18nMenuItem(logBookApplActions
				.getAction(ActionType.PERSPECTIVE_HOME)));
		menu.add(new I18nMenuItem(logBookApplActions
				.getAction(ActionType.PERSPECTIVE_EDITION)));
		this.add(menu);
	}

	private JMenu createShowMenu() {
		JMenu showMenu = new I18nMenu("show");

		I18nMenuItem item = null;

		for (Action action : logBookApplActions.getActionsShowView()) {
			item = new I18nMenuItem(action);
			showMenu.add(item);
		}

		return showMenu;
	}

	private JMenu createLanguageMenu() {
		JMenu languagesMenu = new I18nMenu("languages");
		languagesMenu.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_MOUTH_16));

		List<Locale> locales = LanguageHelper.getKnownLocales();
		Collections.sort(locales, new LocalesComparator());

		JMenuItem languageItem = null;
		for (Locale locale : locales) {
			languageItem = new I18nMenuItem("locale." + locale.getLanguage()
					+ "." + locale.getCountry());
			languageItem.setAction(new SwitchLanguageAction(
					logBookApplicationFrame, locale));
			languagesMenu.add(languageItem);
		}

		return languagesMenu;
	}

	private void createFileMenu() {
		JMenu fileMenu = new I18nMenu("file");

		fileMenu.add(createOpenLogBookMenu());
		fileMenu.add(createCloseLogBookMenu());
		fileMenu.add(createNewLogBookMenu());

		fileMenu.addSeparator();
		fileMenu.add(createPreferencesMenu());
		fileMenu.add(createChangeConfigurationMenu());
		fileMenu.addSeparator();
		fileMenu.add(new I18nMenuItem(logBookApplActions
				.getAction(ActionType.EXIT)));
		this.add(fileMenu);
	}

	private JMenuItem createPreferencesMenu() {
		JMenuItem item = new I18nMenuItem(
				logBookApplActions.getAction(ActionType.SHOW_PREFERENCES));
		return item;
	}

	private JMenuItem createPrintReportLogBookItem() {
		JMenuItem item = new I18nMenuItem(
				logBookApplActions.getAction(ActionType.PRINT_REPORT));
		return item;
	}

	private JMenuItem createCloseLogBookMenu() {
		JMenuItem closeLogBookMenuItem = new I18nMenuItem(
				logBookApplActions.getAction(ActionType.LOGBOOK_CLOSE));
		return closeLogBookMenuItem;
	}

	private JMenuItem createOpenLogBookMenu() {
		JMenuItem openLogBookMenuItem = new I18nMenuItem(
				logBookApplActions.getAction(ActionType.LOGBOOK_OPEN));
		return openLogBookMenuItem;
	}

	private JMenuItem createNewLogBookMenu() {
		JMenuItem logBookMenuItem = new I18nMenuItem(
				logBookApplActions.getAction(ActionType.LOGBOOK_NEW));
		return logBookMenuItem;
	}

	private JMenuItem createDeleteLogBookMenu() {
		JMenuItem logBookMenuItem = new I18nMenuItem(
				logBookApplActions.getAction(ActionType.LOGBOOK_DELETE));
		return logBookMenuItem;
	}

	private JMenuItem createEditLogBookMenu() {
		JMenuItem logBookMenuItem = new I18nMenuItem(
				logBookApplActions.getAction(ActionType.LOGBOOK_EDIT));
		return logBookMenuItem;
	}

	private void createHelpMenu() {
		JMenu helpMenu = new I18nMenu("help");

		// Check updates
		JMenuItem item = new I18nMenuItem(
				logBookApplActions.getAction(ActionType.CHECK_UPDATES));
		helpMenu.add(item);

		// Show revisions
		item = new I18nMenuItem(new ShowRevisionAction());
		helpMenu.add(item);

		// Report Bug
		item = new I18nMenuItem(new ReportBugAction());
		helpMenu.add(item);

		helpMenu.addSeparator();

		// About
		item = new I18nMenuItem(new AbstractAction("about", UIAgent
				.getInstance().getIcon(UIAgent.ICON_ABOUT_16)) {

			private static final long serialVersionUID = 8725466879126448028L;

			@Override
			public void actionPerformed(ActionEvent e) {
				AboutWindow about = new AboutWindow();
				about.setSize(500, 500);
				WindowUtils.centerWindow(about);
				about.setVisible(true);
			}
		});
		helpMenu.add(item);

		this.add(helpMenu);
	}
}
