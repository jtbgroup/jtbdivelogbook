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

import java.awt.Component;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.actions.LogBookApplActionsContoller;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.GlossaryManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.core.event.LogBookEvent;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.swing.component.glass.AlphaGlassPane;
import be.vds.jtbdive.client.util.ResourceManager;
import be.vds.jtbdive.client.view.components.SaveModifiedDivesDialog;
import be.vds.jtbdive.client.view.components.StartupMessageDialog;
import be.vds.jtbdive.client.view.components.UpdateAvailableDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.Version;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.TransferException;

public class LogBookApplFrame extends JFrame implements Observer {

	private static final long serialVersionUID = -6897808431393389767L;
	private Logger LOGGER = Logger.getLogger(LogBookApplFrame.class);
	private LogBookPanel logbookDockPanel;
	private DiveSiteManagerFacade diveLocationManagerFacade;
	private DiverManagerFacade diverManagerFacade;
	private LogBookManagerFacade logBookManagerFacade;
	private LogBookApplActionsContoller actions;
	private AlphaGlassPane glassPane;
	private GlossaryManagerFacade glossaryManagerFacade;

	public LogBookApplFrame(DiveSiteManagerFacade diveLocationManagerFacade,
			DiverManagerFacade diverManagerFacade,
			LogBookManagerFacade logBookManagerFacade,
			GlossaryManagerFacade glossaryManagerFacade) {
		this.diveLocationManagerFacade = diveLocationManagerFacade;
		this.diverManagerFacade = diverManagerFacade;
		this.logBookManagerFacade = logBookManagerFacade;
		this.glossaryManagerFacade = glossaryManagerFacade;
		logBookManagerFacade.addObserver(this);
		init();
	}

	public LogBookPanel getLogbookDockPanel() {
		return logbookDockPanel;
	}

	private void init() {
		actions = new LogBookApplActionsContoller(this, logBookManagerFacade,
				diverManagerFacade, diveLocationManagerFacade,
				glossaryManagerFacade);

		adaptTitle();
		this.setIconImage(UIAgent.getInstance().getBufferedImage(
				UIAgent.ICON_DIVER_16));
		this.getContentPane().add(createContentPanel());
		this.setGlassPane(createGlasspane());
		this.setJMenuBar(createJMenuBar());
	}

	private Component createGlasspane() {
		glassPane = new AlphaGlassPane(0.65f);
		return glassPane;
	}

	private JMenuBar createJMenuBar() {
		LogBookApplMenuBar bar = new LogBookApplMenuBar(this, actions);
		return bar;
	}

	private Component createContentPanel() {
		logbookDockPanel = new LogBookPanel(logBookManagerFacade,
				diverManagerFacade, diveLocationManagerFacade,
				glossaryManagerFacade, actions);
		return logbookDockPanel;
	}

	/**
	 * Closes the window and remove itself from the Application.
	 */
	public void closeWindow() {
		logbookDockPanel.saveLayout();
		saveFramePosition();
		this.dispose();
		LOGGER.info("Window Closed");
		firePropertyChange("windowClosed", this, null);
	}

	private void saveFramePosition() {
		UserPreferences up = UserPreferences.getInstance();
		if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
			up.setWindowWidth(0);
			up.setWindowHeigth(0);
			up.setWindowTop(0);
			up.setWindowLeft(0);
			LOGGER.debug("Cancel Windows Position as it is maximized");
		} else {
			Point p = getLocationOnScreen();
			int w = getWidth();
			int h = getHeight();
			int l = (int) p.getX();
			if (l < 0)
				l = 0;
			int t = (int) p.getY();
			if (t < 0)
				t = 0;
			up.setWindowWidth(w);
			up.setWindowHeigth(h);
			up.setWindowLeft(l);
			up.setWindowTop(t);
			LOGGER.debug("Set Windows param to " + w + "*" + h + " / x:" + l
					+ " y:" + t);
		}
		up.savePreferences(false);
	}

	// Window closing management
	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			LOGGER.debug("Closing the frame using the OS window close button");
			if (checkIfCanBeCLosed()) {
				closeWindow();
			}
		} else {
			super.processWindowEvent(e);
		}
	}

	public boolean checkIfCanBeCLosed() {
		I18nResourceManager i18n = I18nResourceManager.sharedInstance();
		int count = logBookManagerFacade.getAllModifiedDives().size();
		if (count == 0)
			return true;

		SaveModifiedDivesDialog dlg = new SaveModifiedDivesDialog(
				logBookManagerFacade, this, i18n.getString("confirmation"),
				i18n.getString("close.confirm.modified.dives"));
		int i = dlg.showDialog(300, 200);
		if (i == SaveModifiedDivesDialog.OPTION_CANCEL) {
			return false;
		} else if (i == SaveModifiedDivesDialog.OPTION_NO) {
			return true;
		} else if (i == SaveModifiedDivesDialog.OPTION_YES) {
			saveModifiedDives(dlg.getSelectedDives());

			return true;
		}

		return false;
	}

	private void saveModifiedDives(List<Dive> dives) {

		try {
			logBookManagerFacade.saveDives(dives);
		} catch (DataStoreException e) {
			LOGGER.error(e);
		}

	}

	public Dive getDisplayedDive() {
		return logbookDockPanel.getDisplayedDive();
	}

	public LogBookManagerFacade getLogBookManagerFacade() {
		return logBookManagerFacade;
	}

	public void adaptLanguage() {
		logbookDockPanel.adaptLanguage();

	}

	// private void initDiveChangeListener() {
	// DiveChangeInspector.getInstance().addDiveChangeListener(
	// new DiveChangeListener() {
	//
	// @Override
	// public void diveChanged(Dive dive, boolean b) {
	// Dive d = getDisplayedDive();
	// if (d != null && d.equals(dive)) {
	// actions.getAction(SaveDiveAction.class.getName())
	// .setEnabled(b);
	// }
	// }
	//
	// @Override
	// public void clearChangedDives() {
	// }
	// });
	// }

	public void showStartupMessage(boolean forceDisplay) {
		if (!UserPreferences.getInstance().skipStartupMessage() || forceDisplay) {
			StartupMessageDialog d = new StartupMessageDialog(this);
			d.setAlwaysOnTop(false);
			WindowUtils.centerWindow(d);
			d.requestFocus();
			d.setVisible(true);
		}
	}

	// public void addWorkingProcess(WorkingProcess wp) {
	// logbookDockPanel.addWorkingProcess(wp);
	// }

	public void showView(String viewKey) {
		logbookDockPanel.showView(viewKey);
	}

	public void changePerspective(int perspective) {
		logbookDockPanel.changePerspective(perspective);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof LogBookEvent) {
			LogBookEvent event = (LogBookEvent) arg;
			if (event.getType().equals(LogBookEvent.LOGBOOK_LOADED)
					|| event.getType().equals(LogBookEvent.LOGBOOK_META_SAVED)
					|| event.getType().equals(LogBookEvent.LOGBOOK_CLOSED)) {
				adaptTitle();
			}
		}
	}

	private void adaptTitle() {
		String s = "Jt'B Dive Logbook";
		LogBook lb = logBookManagerFacade.getCurrentLogBook();
		if (lb != null)
			s += " - " + lb.getName();

		this.setTitle(s);
	}

	public void checkForUpdate() {
		checkForUpdate(false, false, true, true);
	}

	/**
	 * Checks for updates and display a dialog if needed.
	 * 
	 * @param forceDisplay
	 *            forces the dialog to prompt
	 * @param notifyNoUpdate
	 *            notify when no updates are available
	 * @param checkBefore
	 *            Checks the version before the instantiation of the dialog. if
	 *            false, it's the dialog itself that has to check the version
	 *            number
	 * @param canSkip
	 *            add the possibility to use a "Skip next time" chanckBox
	 */
	public void checkForUpdate(final boolean forceDisplay,
			final boolean notifyNoUpdate, final boolean checkBefore,
			final boolean canSkip) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				if (UserPreferences.getInstance().checkUpdatesOnStartup()
						|| forceDisplay) {
					String lastVersion = null;
					if (checkBefore) {
						try {
							lastVersion = ResourceManager.getInstance()
									.getLastReleaseVersion();
							if (lastVersion == null)
								lastVersion = Version.getCurrentVersion()
										.toString();
						} catch (TransferException e) {
							LOGGER.error(e);
							if (forceDisplay) {
								ExceptionDialog.showDialog(e, null);
							}
							return;
						}
					}

					if (null == lastVersion) {
						LOGGER.debug("check after display dialog");
						displayCheckUpdate(lastVersion, canSkip);
					} else if (null != lastVersion
							&& Version.getCurrentVersion().isLowerThan(
									lastVersion)) {
						LOGGER.warn("There is a new version of the software available");
						displayCheckUpdate(lastVersion, canSkip);
					} else if (notifyNoUpdate) {
						displayCheckUpdate(lastVersion, canSkip);
					} else {
						LOGGER.info("No new version available");
					}
				}
			}
		}).start();
	}

	private void displayCheckUpdate(String lastVersion, boolean canSkip) {
		UpdateAvailableDialog d = new UpdateAvailableDialog(this, lastVersion,
				canSkip);
		d.setAlwaysOnTop(false);
		WindowUtils.centerWindow(d);
		d.requestFocus();
		d.setVisible(true);
	}

	public void setGlassPaneActive(final boolean b) {
		if (b) {
			this.setGlassPane(glassPane);
		}

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				glassPane.setVisible(b);
			}
		});
	}
}
