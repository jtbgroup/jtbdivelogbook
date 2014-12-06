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
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.config.Configuration;
import be.vds.jtbdive.client.core.config.ConfigurationHandler;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.swing.component.VersionUpgraderDialog;
import be.vds.jtbdive.client.util.ResourceManager;
import be.vds.jtbdive.client.view.components.ChangeConfigDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.client.view.wizard.config.ConfigurationWizard;
import be.vds.jtbdive.core.core.Version;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.VersionException;
import be.vds.jtbdive.core.logging.Syslog;

public class ChangeConfigurationAction extends AbstractAction {

	private static final long serialVersionUID = 8020318505144124702L;
	private static final Syslog LOGGER = Syslog
			.getLogger(ChangeConfigurationAction.class);
	private LogBookManagerFacade logBookManagerFacade;
	private DiveSiteManagerFacade diveSiteManagerFacade;
	private DiverManagerFacade diverManagerFacade;

	public ChangeConfigurationAction(LogBookManagerFacade logBookManagerFacade,
			DiverManagerFacade diverManagerFacade,
			DiveSiteManagerFacade diveLocationManagerFacade) {
		super("configuration.change", UIAgent.getInstance().getIcon(
				UIAgent.ICON_CONNECT_16));
		this.logBookManagerFacade = logBookManagerFacade;
		this.diverManagerFacade = diverManagerFacade;
		this.diveSiteManagerFacade = diveLocationManagerFacade;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Configuration currentConfiguration = getConfig();
		Configuration configuration = ConfigurationWizard
				.createConfiguration(currentConfiguration);
		if (configuration != null) {
			try {
				File file = ResourceManager.getInstance().getConfigFile(true);
				ConfigurationHandler.getInstance().persistConfiguration(
						configuration, file);
				boolean allowedToChangeConfig = true;
				boolean hasBeenUpgraded = false;
				try {
					configuration.initialize();
				} catch (VersionException e1) {
					if (e1.isCanBeUpgraded()) {

						boolean b = proposeUpgrade(e1.getVersion());
						if (b) {
							allowedToChangeConfig = configuration
									.upgradePersistenceVersion();
							hasBeenUpgraded = true;
						} else {
							allowedToChangeConfig = false;
						}
					} else {
						ChangeConfigDialog dlg = new ChangeConfigDialog(
								I18nResourceManager
										.sharedInstance()
										.getMessage(
												"configuration.newer.message.params",
												new Object[] {
														e1.getVersion()
																.toString(),
														Version.getCurrentVersion()
																.toString() })

						);
						int i = dlg.showDialog();
						if (i == ChangeConfigDialog.OPTION_YES) {
							actionPerformed(e);
						}
					}
				}

				if (hasBeenUpgraded) {
					try {
						configuration.initialize();
					} catch (VersionException e1) {
						LOGGER.error("Impossible to get here !!!!! arrrrgggggghhhhh");
					}
				}

				if (allowedToChangeConfig) {
					logBookManagerFacade.setBusinessDelegate(configuration
							.getLogBookBusinessDelegate());
					diverManagerFacade.setBusinessDelegate(configuration
							.getDiverBusinessDelegate());
					diveSiteManagerFacade.setBusinessDelegate(configuration
							.getDiveLocationBusinessDelegate());
				}
			} catch (IOException ex) {
				LOGGER.error(ex.getMessage());
				ExceptionDialog.showDialog(ex, null);
			} catch (DataStoreException ex) {
				LOGGER.error(ex.getMessage());
				ExceptionDialog.showDialog(ex, null);
			}
		}
	}

	private Configuration getConfig() {
		File configFile = null;
		LOGGER.debug("Reading ConfigFile");
		try {
			configFile = ResourceManager.getInstance().getConfigFile(false);
		} catch (IOException e1) {
			LOGGER.error(e1.getMessage());
		}

		if (configFile != null && configFile.exists()) {
			LOGGER.info("Reading configuration from file "
					+ configFile.getAbsolutePath());
			try {
				return ConfigurationHandler.getInstance().loadConfiguration(
						configFile);
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		} else {
			LOGGER.debug("Configuration file not found");
		}
		return null;
	}

	private boolean proposeUpgrade(Version oldVersion) {
		VersionUpgraderDialog dlg = new VersionUpgraderDialog(oldVersion, Version.getCurrentVersion());
		return dlg.showDialog(400, 250) == VersionUpgraderDialog.OPTION_YES;
	}
}
