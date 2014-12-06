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
package be.vds.jtbdive.client.core;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import be.vds.jtbdive.client.interfaces.SerialDataCommInterface;
import be.vds.jtbdive.client.util.LanguageHelper;
import be.vds.jtbdive.client.util.ResourceManager;
import be.vds.jtbdive.client.view.core.browser.nodeconstructor.LogBookSorting;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.catalogs.DiveComputerType;
import be.vds.jtbdive.core.core.units.LengthUnit;
import be.vds.jtbdive.core.core.units.PressureUnit;
import be.vds.jtbdive.core.core.units.TemperatureUnit;
import be.vds.jtbdive.core.core.units.WeightUnit;
import be.vds.jtbdive.core.interfaces.DataCommInterface;
import be.vds.jtbdive.core.interfaces.DataCommInterfaceType;
import be.vds.jtbdive.core.logging.Syslog;

public class UserPreferences extends Observable {

	public static final String PREFERENCES_CHANGED = "preferences.saved";

	private static final String PROPS_LOCALE = "locale";
	private static final String PROPS_UNIT_TEMPERATURE = "unit.temperature";
	private static final String PROPS_UNIT_LENGTH = "unit.length";
	private static final String PROPS_UNIT_WEIGHT = "unit.weight";
	private static final String PROPS_UNIT_PRESSURE = "unit.pressure";
	private static final String PROPS_SKIP_STARTUP_MESSAGE = "skip.startup.message";
	private static final String CHECK_UPDATES_ON_STARTUP = "check.updates";
	private static final String PROPS_LOGBOOK_DEFAULT_SORTING = "logbook.default.sorting";
	private static final String PROPS_DEFAULT_LOGGER_LEVEL = "log.level.default";
	private static final String PROPS_DIVE_COMPUTER_DATACOMM_INTERFACE = "dive.computer.datacomm.interface";
	private static final String PROPS_DIVE_COMPUTER = "dive.computer";
	private static final String PROPS_LOG_ADD_ON_TOP = "log.on.top";
	private static final String PROPS_LOG_BUFFER_SIZE = "log.buffer.size";
	private static final String PROPS_WINDOW_WIDTH = "window.width";
	private static final String PROPS_WINDOW_HEIGHT = "window.height";
	private static final String PROPS_WINDOW_TOP = "window.top";
	private static final String PROPS_WINDOW_LEFT = "window.left";
	private static final String PROPS_FORMAT_DATE_HOURS = "format.date.hours";
	private static final String PROPS_FORMAT_DATE = "format.date";
	private static final String PROPS_SKIP_DIVETIME_MODIFICATION_INFLUENCE_MESSAGE = "skip.dive.time.modification.influence.message";
	private static final String PROPS_SKIP_DIVEDEPTH_MODIFICATION_INFLUENCE_MESSAGE = "skip.dive.depth.modification.influence.message";

	private static UserPreferences instance;
	private Properties properties = new Properties();
	private static final Syslog LOGGER = Syslog
			.getLogger(UserPreferences.class);


	private UserPreferences() {
	}

	public static UserPreferences getInstance() {
		if (instance == null) {
			instance = new UserPreferences();
		}
		return instance;
	}

	public void inititalize() {
		File f = ResourceManager.getInstance().getUserPreferencesFile();
		boolean isInit = false;
		if (f != null && f.exists()) {
			try {
				properties.load(new FileInputStream(f));
				isInit = true;
			} catch (FileNotFoundException e) {
				LOGGER.error(e.getMessage());
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		}

		if (!isInit)
			loadDefaultProperties();
	}

	private void loadDefaultProperties() {
		List<Locale> locales = LanguageHelper.getKnownLocales();
		Locale l = null;
		if (locales.contains(LanguageHelper.LOCALE_ENGLISH)) {
			l = LanguageHelper.LOCALE_ENGLISH;
		} else {
			l = locales.get(0);
		}
		setPreferredLocale(l);
		setPreferredLengthUnit(LengthUnit.METER);
		setPreferredPressureUnit(PressureUnit.BAR);
		setPreferredTemperatureUnit(TemperatureUnit.CELSIUS);
		setPreferredWeightUnit(WeightUnit.KILOGRAM);
		setSkipStartupMessage(false);
		setCheckUpdatesOnStartUp(true);
		setDefaultLogbookSorting(LogBookSorting.DIVE_NUMBER);
		setLogOnTop(false);
		setLoggingBufferSize(100);
		setPreferredDiveComputerDataCommInterface(null);
		setPreferredDiveComputer(null);
		setWindowWidth(0);
		setWindowHeigth(0);
		setWindowTop(0);
		setWindowLeft(0);
	}

	public void savePreferences() {
		savePreferences(true);
	}

	public void savePreferences(boolean notifyChanges) {
		File f = ResourceManager.getInstance().getUserPreferencesFile();
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			properties.store(new FileOutputStream(f),
					"Jtb Dive logbook preferences");
			LOGGER.info("Preferences saved");
			if (notifyChanges) {
				setChanged();
				notifyObservers(PREFERENCES_CHANGED);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Locale getPreferredLocale() {
		if (properties.getProperty(PROPS_LOCALE) != null) {
			String[] s = properties.getProperty(PROPS_LOCALE).split("_");
			String country = s[1];
			String language = s[0];

			List<Locale> locales = LanguageHelper.getKnownLocales();
			for (Locale locale : locales) {
				if (locale.getCountry().equals(country)
						&& locale.getLanguage().equals(language)) {
					return locale;
				}
			}
		}
		return null;
	}

	public void setPreferredLocale(Locale locale) {
		String localeString = locale.getLanguage() + "_" + locale.getCountry();
		properties.setProperty(PROPS_LOCALE, localeString);
	}

	public void setPreferredTemperatureUnit(TemperatureUnit temperatureUnit) {
		properties.setProperty(PROPS_UNIT_TEMPERATURE,
				String.valueOf(temperatureUnit.getId()));
	}

	public TemperatureUnit getPreferredTemperatureUnit() {
		if (properties.getProperty(PROPS_UNIT_TEMPERATURE) != null) {
			return TemperatureUnit.getTemperatureUnit(Integer
					.parseInt(properties.getProperty(PROPS_UNIT_TEMPERATURE)));
		}
		return null;
	}

	public void setPreferredLengthUnit(LengthUnit lengthUnit) {
		properties.setProperty(PROPS_UNIT_LENGTH,
				String.valueOf(lengthUnit.getId()));
	}

	public LengthUnit getPreferredLengthUnit() {
		if (properties.getProperty(PROPS_UNIT_LENGTH) != null) {
			return LengthUnit.getLengthUnit(Integer.parseInt(properties
					.getProperty(PROPS_UNIT_LENGTH)));
		}
		return null;
	}

	public boolean skipStartupMessage() {
		String skip = properties.getProperty(PROPS_SKIP_STARTUP_MESSAGE);
		boolean b = skip == null ? false : Boolean.parseBoolean(skip);
		return b;
	}

	public boolean checkUpdatesOnStartup() {
		String skip = properties.getProperty(CHECK_UPDATES_ON_STARTUP);
		boolean b = skip == null ? false : Boolean.parseBoolean(skip);
		return b;
	}

	public void setSkipStartupMessage(boolean skip) {
		properties
				.setProperty(PROPS_SKIP_STARTUP_MESSAGE, String.valueOf(skip));
	}

	public void setCheckUpdatesOnStartUp(boolean skip) {
		properties.setProperty(CHECK_UPDATES_ON_STARTUP, String.valueOf(skip));
	}

	public void setPreferredWeightUnit(WeightUnit weightUnit) {
		properties.setProperty(PROPS_UNIT_WEIGHT,
				String.valueOf(weightUnit.getId()));
	}

	public void setPreferredPressureUnit(PressureUnit pressureUnit) {
		properties.setProperty(PROPS_UNIT_PRESSURE,
				String.valueOf(pressureUnit.getId()));
	}

	public WeightUnit getPreferredWeightUnit() {
		if (properties.getProperty(PROPS_UNIT_WEIGHT) != null) {
			return WeightUnit.getWeightUnit(Integer.parseInt(properties
					.getProperty(PROPS_UNIT_WEIGHT)));
		}
		return null;
	}

	public PressureUnit getPreferredPressureUnit() {
		if (properties.getProperty(PROPS_UNIT_PRESSURE) != null) {
			return PressureUnit.getPressureUnit(Integer.parseInt(properties
					.getProperty(PROPS_UNIT_PRESSURE)));
		}
		return null;
	}

	public int getDiveNumberSize() {
		return 4;
	}

	public Color getPreferredColor(String colorKey) {
		String c = (String) properties.get(colorKey);
		if (c == null)
			return null;

		Pattern p = Pattern
				.compile("rgb[(]([\\d]{1,3})[,]([\\d]{1,3})[,]([\\d]{1,3})[)]");
		Matcher m = p.matcher(c);

		if (!m.matches())
			return null;

		Color color = new Color(Integer.parseInt(m.group(1)),
				Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));

		return color;
	}

	public void setPreferredColor(String key, Color color) {
		StringBuilder sb = new StringBuilder();
		sb.append("rgb(").append(color.getRed());
		sb.append(",").append(color.getGreen());
		sb.append(",").append(color.getBlue());
		sb.append(")");
		properties.setProperty(key, sb.toString());
	}

	public void setDefaultLogbookSorting(LogBookSorting sorting) {
		if (null == sorting) {
			properties.remove(PROPS_LOGBOOK_DEFAULT_SORTING);
		} else {
			properties.setProperty(PROPS_LOGBOOK_DEFAULT_SORTING,
					sorting.getKey());
		}
	}

	public LogBookSorting getDefaultLogbookSorting() {
		return LogBookSorting.getLogBookSorting(properties
				.getProperty(PROPS_LOGBOOK_DEFAULT_SORTING));
	}

	public Integer getDefaultLoggingLevel() {
		String s = (String) properties.get(PROPS_DEFAULT_LOGGER_LEVEL);
		if (s != null)
			return Integer.parseInt(s);
		return null;
	}

	public void setDefaultLoggingLevel(Integer level) {
		properties.put(PROPS_DEFAULT_LOGGER_LEVEL, level.toString());
	}

	public void setPreferredDiveComputerDataCommInterface(
			DataCommInterface dataCommInterface) {

		if (dataCommInterface != null) {
			switch (dataCommInterface.getDataCommInterfaceType()) {
			case SERIAL:
				setPreferredDiveComputerSerialDataComInterface((SerialDataCommInterface) dataCommInterface);
				break;
			default:
				throw new IllegalArgumentException(
						"This DataComm interface can't be stored ("
								+ dataCommInterface.getDataCommInterfaceType()
								+ ")");
			}
		}

	}

	private void setPreferredDiveComputerSerialDataComInterface(
			SerialDataCommInterface dataCommInterface) {
		StringBuilder s = new StringBuilder();
		s.append(dataCommInterface.getDataCommInterfaceType().getId());
		s.append(";");
		s.append(dataCommInterface.getPort());
		s.append(";");
		s.append(dataCommInterface.getBaudRate());
		s.append(";");
		s.append(dataCommInterface.getDataBits());
		s.append(";");
		s.append(dataCommInterface.getParity());
		s.append(";");
		s.append(dataCommInterface.getStopBits());

		properties.put(PROPS_DIVE_COMPUTER_DATACOMM_INTERFACE, s.toString());
	}

	public DataCommInterface getPreferredDiveComputerDataCommInterface() {
		String s = (String) properties
				.get(PROPS_DIVE_COMPUTER_DATACOMM_INTERFACE);
		if (s == null)
			return null;

		String[] values = s.split(";");
		switch (DataCommInterfaceType.getDataCommInterfaceType(Short
				.parseShort(values[0]))) {
		case SERIAL:
			return getPreferredSerialDiveComputerDataCommInterface(values);

		}
		throw new IllegalArgumentException(
				"This DataComm interface can't be retrieved (" + s + ")");
	}

	private DataCommInterface getPreferredSerialDiveComputerDataCommInterface(
			String[] values) {
		SerialDataCommInterface sdi = new SerialDataCommInterface();
		sdi.setPort(values[1]);
		sdi.setBaudRate(Integer.valueOf(values[2]));
		sdi.setDataBits(Integer.valueOf(values[3]));
		sdi.setParity(Integer.valueOf(values[4]));
		sdi.setStopBits(Integer.valueOf(values[5]));
		return sdi;
	}

	public void setPreferredDiveComputer(DiveComputerType diveComputerType) {
		if (diveComputerType == null) {
			properties.remove(PROPS_DIVE_COMPUTER);
		} else {
			properties.put(PROPS_DIVE_COMPUTER, diveComputerType.getKey());
		}
	}

	public DiveComputerType getPreferredDiveComputer() {
		String s = (String) properties.get(PROPS_DIVE_COMPUTER);
		if (s == null)
			return null;

		return DiveComputerType.getDiveComputerType(s);
	}

	public void setLogOnTop(boolean addOnTop) {
		properties.put(PROPS_LOG_ADD_ON_TOP, String.valueOf(addOnTop));
	}

	public void setLoggingBufferSize(int bufferSize) {
		properties.put(PROPS_LOG_BUFFER_SIZE, String.valueOf(bufferSize));
	}

	public boolean getLogOnTop() {
		String s = (String) properties.get(PROPS_LOG_ADD_ON_TOP);
		if (s != null) {
			return Boolean.parseBoolean(s);
		}
		return false;
	}

	public int getLoggingBufferSize() {
		String s = (String) properties.get(PROPS_LOG_BUFFER_SIZE);
		if (s != null) {
			return Integer.parseInt(s);
		}
		return 100;
	}

	public void setWindowWidth(int i) {
		properties.put(PROPS_WINDOW_WIDTH, String.valueOf(i));
	}

	public void setWindowHeigth(int i) {
		properties.put(PROPS_WINDOW_HEIGHT, String.valueOf(i));
	}

	public void setWindowTop(int i) {
		properties.put(PROPS_WINDOW_TOP, String.valueOf(i));
	}

	public void setWindowLeft(int i) {
		properties.put(PROPS_WINDOW_LEFT, String.valueOf(i));
	}

	public int getWindowPositionLeft() {
		String s = (String) properties.get(PROPS_WINDOW_LEFT);
		if (s != null) {
			return Integer.parseInt(s);
		}
		return 0;
	}

	public int getWindowPositionTop() {
		String s = (String) properties.get(PROPS_WINDOW_TOP);
		if (s != null) {
			return Integer.parseInt(s);
		}
		return 0;
	}

	public int getWindowWidth() {
		String s = (String) properties.get(PROPS_WINDOW_WIDTH);
		if (s != null) {
			return Integer.parseInt(s);
		}
		return 0;
	}

	public int getWindowHeigth() {
		String s = (String) properties.get(PROPS_WINDOW_HEIGHT);
		if (s != null) {
			return Integer.parseInt(s);
		}
		return 0;
	}

	public SimpleDateFormat getPreferredDateHoursFormat() {
		String s = (String) properties.get(PROPS_FORMAT_DATE_HOURS);
		if (s != null) {
			return new SimpleDateFormat(s);
		}
		return UIAgent.getInstance().getExportFormatDateHoursFull();
	}

	public SimpleDateFormat getPreferredDateFormat() {
		String s = (String) properties.get(PROPS_FORMAT_DATE);
		if (s != null) {
			return new SimpleDateFormat(s);
		}
		return UIAgent.getInstance().getExportFormatDateShort();
	}
	
	public void setDateHoursFormat(SimpleDateFormat sdf) {
		properties.put(PROPS_FORMAT_DATE_HOURS, sdf.toPattern());
	}

	public void setDateFormat(SimpleDateFormat sdf) {
		properties.put(PROPS_FORMAT_DATE, sdf.toPattern());
	}
	public boolean skipDiveTimeInfluenceMessage() {
		String skip = properties
				.getProperty(PROPS_SKIP_DIVETIME_MODIFICATION_INFLUENCE_MESSAGE);
		boolean b = skip == null ? false : Boolean.parseBoolean(skip);
		return b;
	}

	public void setSkipDiveTimeInfluenceMessage(boolean skip) {
		properties.setProperty(
				PROPS_SKIP_DIVETIME_MODIFICATION_INFLUENCE_MESSAGE,
				String.valueOf(skip));
	}
	
	public boolean skipDiveDepthModificationInfluenceMessage() {
		String skip = properties
				.getProperty(PROPS_SKIP_DIVEDEPTH_MODIFICATION_INFLUENCE_MESSAGE);
		boolean b = skip == null ? false : Boolean.parseBoolean(skip);
		return b;
	}

	public void setSkipDiveDepthmodificationInfluenceMessage(boolean skip) {
		properties.setProperty(
				PROPS_SKIP_DIVEDEPTH_MODIFICATION_INFLUENCE_MESSAGE,
				String.valueOf(skip));
	}

}
