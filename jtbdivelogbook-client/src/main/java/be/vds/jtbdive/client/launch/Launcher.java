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
package be.vds.jtbdive.client.launch;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.GlossaryManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.core.config.Configuration;
import be.vds.jtbdive.client.core.config.ConfigurationHandler;
import be.vds.jtbdive.client.i18n.LoggerErrorHandler;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.swing.component.VersionUpgraderDialog;
import be.vds.jtbdive.client.util.LanguageHelper;
import be.vds.jtbdive.client.util.ResourceManager;
import be.vds.jtbdive.client.view.components.ChangeConfigDialog;
import be.vds.jtbdive.client.view.components.SplashScreen;
import be.vds.jtbdive.client.view.core.LogBookApplFrame;
import be.vds.jtbdive.client.view.events.LogBookUiEventHandler;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.client.view.wizard.config.ConfigurationWizard;
import be.vds.jtbdive.core.core.Version;
import be.vds.jtbdive.core.core.manager.DiveSiteManager;
import be.vds.jtbdive.core.core.manager.DiverManager;
import be.vds.jtbdive.core.core.manager.GlossaryManager;
import be.vds.jtbdive.core.core.manager.LogBookManager;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.VersionException;
import be.vds.jtbdive.core.logging.Syslog;

public class Launcher {

	private static final Syslog LOGGER = Syslog.getLogger(Launcher.class);

	public static void main(String[] args) {
		Launcher launcher = new Launcher();
		launcher.initialize(args);
	}

	public static String APPENDER_NAME = "listenableappender";
	private LogBookApplFrame frame;
	private Window windowSplash;
	private SplashScreen splash;
	private LogBookManagerFacade logBookManagerFacade;
	private DiverManagerFacade diverManagerFacade;
	private DiveSiteManagerFacade diveSiteManagerFacade;
	private GlossaryManagerFacade glossaryManagerFacade;
	private boolean demoOption;

	private void showSplashScreen() {
		if (splash == null) {
			splash = new SplashScreen();
			splash.setText("Initializing...");
			windowSplash = splash.putOnWindow();
			windowSplash.setVisible(true);
		}
	}

	private void closeSplashScreen() {
		LOGGER.debug("Close Splash Screen");
		windowSplash.dispose();
	}

	private void initialize(String[] args) {
		// This must be done first as it configures the path to the local
		// resources
		showSplashScreen();
		splash.setText("Initializing Proxy...");
		initProxySettings();
		splash.setText("Initializing Resources...");
		initResourceManager();
		splash.setText("Initializing Logger...");
		initLogger();
		splash.setText("Initializing Preferences...");
		initPreferences();
		splash.setText("Initializing i18n...");
		initi18nManager();
		splash.setText("Initializing Default UI settings...");
		initUIController();
		splash.setText("Initializing Units...");
		initUnits();
		splash.setText("Initializing LAF...");
		initLAF();
		splash.setText("Initializing Startup arguments...");
		processCommandLine(args);
		splash.setText("Initializing Configuration...");
		Configuration conf = initConfiguration();
		splash.setText("Initializing Managers...");
		initGlossaryManagerFacade(conf);
		initDiveLocationManagerFacade(conf);
		initDiverManagerFacade(conf);
		initLogBookManagerFacade(conf);
		initEventHandler();
		if (demoOption) {
			JOptionPane.showMessageDialog(null,
					"Demo option not supported yet...");
			// splash.setText("Filling demo data...");

		}
		splash.setText("Initializing the user interface...");

		startApplication();
		closeSplashScreen();
		showApplication();

	}

	private void initProxySettings() {
		Properties props = System.getProperties();
		props.put("java.net.useSystemProxies", "true");
		System.setProperties(props);
	}

	private void initUIController() {
		UIAgent.getInstance().setFormatDateHoursFull(
				UserPreferences.getInstance().getPreferredDateHoursFormat());
		UIAgent.getInstance().setFormatDateShort(
				UserPreferences.getInstance().getPreferredDateFormat());
	}

	private void initEventHandler() {
		LogBookUiEventHandler.getInstance().initialize();
		LogBookUiEventHandler.getInstance().registerLogBookManagerFacade(
				logBookManagerFacade);
	}

	private void initUnits() {
		UnitsAgent ua = UnitsAgent.getInstance();
		if (UserPreferences.getInstance().getPreferredTemperatureUnit() != null) {
			ua.setTemperatureUnit(UserPreferences.getInstance()
					.getPreferredTemperatureUnit());
		}

		if (UserPreferences.getInstance().getPreferredLengthUnit() != null) {
			ua.setLengthUnit(UserPreferences.getInstance()
					.getPreferredLengthUnit());
		}

		if (UserPreferences.getInstance().getPreferredPressureUnit() != null) {
			ua.setPressureUnit(UserPreferences.getInstance()
					.getPreferredPressureUnit());
		}
	}

	private void initPreferences() {
		UserPreferences.getInstance().inititalize();
		if (ResourceManager.getInstance().getLocalLog4jFile() != null
				&& UserPreferences.getInstance().getDefaultLoggingLevel() != null) {
			Syslog.getRootLogger().setLevel(
					UserPreferences.getInstance().getDefaultLoggingLevel());
		}
	}

	private void initResourceManager() {
		ResourceManager.getInstance().setDefaultApplication("logbook");
	}

	private void initLAF() {
		// Sets the antialiasing on for the whole system
		System.setProperty("swing.aatext", "true");

		try {
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			LOGGER.debug("LaF set");
		} catch (Exception e) {
			LOGGER.warn("Unable to load system LaF");
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e1) {
				LOGGER.error(e1.getMessage());
			}
		}
	}

	private void processCommandLine(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-demo")) {
				demoOption = true;
			} else if (args[i].equals("-debug")) {
				enableDebugMode(true);
			}
		}
	}

	private void enableDebugMode(boolean b) {
		Syslog.getRootLogger().setLevel(Syslog.INT_DEBUG);
		LOGGER.debug("Debug mode enabled");
	}

	private Configuration initConfiguration() {
		Configuration configuration = null;
		boolean showWizard = true;
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
				configuration = ConfigurationHandler.getInstance()
						.loadConfiguration(configFile);
				showWizard = configuration == null;
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		} else {
			LOGGER.debug("Configuration file not found");
		}

		if (showWizard) {
			configuration = showConfigurationWizard();
		}

		if (configuration == null) {
			JOptionPane
					.showMessageDialog(
							null,
							"An error occured while creating the configuration. \r\nApplication will close.");
			endApplication(1);
		}

		boolean mustEnd = false;
		try {
			mustEnd = !initializeConfig(configuration);
		} catch (DataStoreException e) {
			LOGGER.fatal("DataStore Exception while initializing the config: "
					+ e.getMessage());
			mustEnd = true;
		}

		if (mustEnd) {
			endApplication(1);
		}

		return configuration;
	}

	private Configuration showConfigurationWizard() {
		return showConfigurationWizard(null);
	}

	private Configuration showConfigurationWizard(
			Configuration configurationToDisplay) {
		Configuration configuration;
		File configFile;
		LOGGER.info("Configuration wizard will start.");
		if (null == configurationToDisplay) {
			configuration = ConfigurationWizard.createConfiguration();
		} else {
			configuration = ConfigurationWizard
					.createConfiguration(configurationToDisplay);
		}
		if (configuration != null) {
			try {
				configFile = ResourceManager.getInstance().getConfigFile(true);

				ConfigurationHandler.getInstance().persistConfiguration(
						configuration, configFile);
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
				ExceptionDialog.showDialog(e, null);
			}
		}
		return configuration;
	}

	private void endApplication(int stopCode) {
		switch (stopCode) {
		case 0:
			LOGGER.info("Exit application ... Bye!");
			break;
		case 1:
			LOGGER.fatal("Application ends now due to an error. Bye!");
			break;
		}
		System.exit(stopCode);
	}

	/**
	 * initializes the {@link Configuration}. In case of upgrade of the
	 * persistence layer, the initialization is complete when the upgrade has
	 * been accepted by the user (prompt dialog) and when the upgrade has been
	 * successfully done.
	 * 
	 * @param configuration
	 * @return true if the initialization is complete, false if not.
	 * @throws DataStoreException
	 */
	private boolean initializeConfig(Configuration configuration)
			throws DataStoreException {
		// splash.setText("Initializing Configuration...");
		LOGGER.debug("initializing config");

		try {
			configuration.initialize();
		} catch (VersionException e1) {
			if (e1.isCanBeUpgraded()) {
				boolean b = proposeUpgrade(e1.getVersion());
				if (b) {
					configuration.upgradePersistenceVersion();
					initializeConfig(configuration);
				} else {
					LOGGER.warn("Upgrade has not been allowed by the user.");
					return false;
				}
			} else {
				ChangeConfigDialog dlg = new ChangeConfigDialog(
						I18nResourceManager.sharedInstance()
								.getMessage(
										"configuration.newer.message.params",
										new Object[] {
												e1.getVersion().toString(),
												Version.getCurrentVersion()
														.toString() }));
				int i = dlg.showDialog(600, 500);
				if (i == ChangeConfigDialog.OPTION_YES) {
					Configuration conf = showConfigurationWizard(configuration);
					if (null == conf) {
						endApplication(0);
					}
					initializeConfig(conf);
				} else {
					endApplication(0);
				}
			}
		}

		return true;
	}

	private boolean proposeUpgrade(Version oldVersion) {
		VersionUpgraderDialog dlg = new VersionUpgraderDialog(oldVersion, Version.getCurrentVersion());
		return dlg.showDialog(400, 350) == VersionUpgraderDialog.OPTION_YES;
	}

	private void initi18nManager() {
		List<Locale> locales = LanguageHelper.getKnownLocales();
		LOGGER.info("Found " + locales.size() + " languages...");

		I18nResourceManager i18nManager = I18nResourceManager.sharedInstance();
		Locale l = UserPreferences.getInstance().getPreferredLocale();
		if (null == l) {
			if (locales.size() == 0
					|| locales.contains(LanguageHelper.LOCALE_ENGLISH)) {
				l = LanguageHelper.LOCALE_ENGLISH;
			} else {
				l = locales.get(0);
			}
		}
		i18nManager.setDefaultLocale(l);
		Locale[] locs = new Locale[locales.size()];
		for (int i = 0; i < locs.length; i++) {
			locs[i] = locales.get(i);
		}

		LoggerErrorHandler errorHandler = LoggerErrorHandler.getInstance();
		errorHandler.setLevel(Level.DEBUG);
		i18nManager.setErrorHandler(errorHandler);

		i18nManager.addBundle("resources/bundles/Bundle", locs);
		i18nManager.addBundle("resources/bundles/Countries", locs);
		LOGGER.debug("I18n Manager initialized");
	}

	private void showApplication() {
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);

		LOGGER.debug("Display Application");
		frame.setVisible(true);

		setPositionFrame();

		frame.showStartupMessage(false);
		frame.checkForUpdate();
	}

	private void setPositionFrame() {
		int width = UserPreferences.getInstance().getWindowWidth();
		int height = UserPreferences.getInstance().getWindowHeigth();
		if (height > 0 && width > 0) {
			frame.setSize(width, height);
			int top = UserPreferences.getInstance().getWindowPositionTop();
			int left = UserPreferences.getInstance().getWindowPositionLeft();

			if (top < 0)
				top = 0;
			if (left < 0)
				left = 0;

			frame.setLocation(left, top);
		} else {
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
	}

	private void startApplication() {
		LOGGER.debug("Initialization of the LogBookApplicationPanel");
		frame = new LogBookApplFrame(diveSiteManagerFacade,
				diverManagerFacade, logBookManagerFacade, glossaryManagerFacade);
		frame.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("windowClosed")) {
					endApplication(0);
				}
			}
		});
		((LogBookApplFrame) frame).adaptLanguage();
	}

	private void initDiveLocationManagerFacade(Configuration configuration) {
		DiveSiteManager diveLocationManager = new DiveSiteManager(
				configuration.getDiveLocationBusinessDelegate());
		diveSiteManagerFacade = new DiveSiteManagerFacade(
				diveLocationManager);
	}

	private void initGlossaryManagerFacade(Configuration configuration) {
		GlossaryManager glossaryManager = new GlossaryManager(
				configuration.getGlossaryBusinessDelegate());
		glossaryManagerFacade = new GlossaryManagerFacade(glossaryManager);

	}

	private void initDiverManagerFacade(Configuration configuration) {
		DiverManager diverManager = new DiverManager(
				configuration.getDiverBusinessDelegate());
		diverManagerFacade = new DiverManagerFacade(diverManager);
	}

	private void initLogBookManagerFacade(Configuration configuration) {
		LogBookManager logBookManager = new LogBookManager(
				configuration.getLogBookBusinessDelegate());
		logBookManagerFacade = new LogBookManagerFacade(logBookManager);
	}

	private void initLogger() {
		BasicConfigurator.configure();
		URL logConfig = ResourceManager.getInstance().getLog4jFile();
		if (null != logConfig) {
			PropertyConfigurator.configure(logConfig);
		}
		Syslog.getListenerAppender();

		LOGGER.info("**********************************");
		LOGGER.info("**  Jt'B dive logbook starting  **");
		LOGGER.info("**********************************");

		LOGGER.info("Operating system: " + System.getProperty("os.name"));
		LOGGER.info("Architecture: " + System.getProperty("os.arch"));

		LOGGER.info("Java vendor: " + System.getProperty("java.vendor"));
		LOGGER.info("Java runtime: " + System.getProperty("java.runtime.name"));
		LOGGER.info("Java version: " + System.getProperty("java.version"));
		LOGGER.info("Jt'B Dive Logbook version: "
				+ Version.getCurrentVersion().toString());
	}
}
