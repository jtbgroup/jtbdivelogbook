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
package be.vds.jtbdive.client.view.docking;

import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.view.core.console.JLoggingPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.logging.ListenerAppender;
import be.vds.jtbdive.core.logging.LoggingListener;
import be.vds.jtbdive.core.logging.Syslog;

public class ConsoleDockable extends I18nDefaultSingleCDockable {
	private JLoggingPanel loggingPanel;

	public ConsoleDockable() {
		super(DockingLayoutManager.VIEW_CONSOLE,
				DockingLayoutManager.VIEW_CONSOLE, 
				UIAgent.getInstance().getIcon(UIAgent.ICON_CONSOLE_16), null);
		add(createContent());
		setCloseable(true);
	}

	private Component createContent() {
		loggingPanel = new JLoggingPanel(100, UserPreferences.getInstance().getLogOnTop());
		loggingPanel.setLevel(getCurrentLevel());
		loggingPanel.setConsoleBackgroundColor(UIAgent.getInstance()
				.getColorConsoleBkg());
		Map<Integer, Color> colorMap = new HashMap<Integer, Color>();
		colorMap.put(JLoggingPanel.LogEntry.LVL_INFO, Color.LIGHT_GRAY);
		loggingPanel.setConsoleForegroundColors(colorMap);

		loggingPanel.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {

				if (evt.getPropertyName().equals(JLoggingPanel.LEVEL_CHANGED)) {

					int lev = (Integer) evt.getNewValue();
					int level = Syslog.INT_INFO;
					switch (lev) {
					case JLoggingPanel.LogEntry.LVL_DEBUG:
						level = Syslog.INT_DEBUG;
						break;
					case JLoggingPanel.LogEntry.LVL_INFO:
						level = Syslog.INT_INFO;
						break;
					case JLoggingPanel.LogEntry.LVL_WARN:
						level = Syslog.INT_WARN;
						break;
					case JLoggingPanel.LogEntry.LVL_ERROR:
						level = Syslog.INT_ERROR;
						break;
					case JLoggingPanel.LogEntry.LVL_FATAL:
						level = Syslog.INT_FATAL;
						break;
					}

					Syslog.getRootLogger().setLevel(level);
				}
			}
		});

		final ListenerAppender appender = Syslog.getListenerAppender();
		appender.addLoggerListener(new LoggingListener() {

			@Override
			public void log(LoggingEvent event) {
				int level = getLevelForLogEvent(event);

				loggingPanel.log(appender.getLayout().format(event), level);
			}
		});

		if (null != appender && appender.getCache() != null) {
			Queue<LoggingEvent> queue = appender.getCache();
			for (LoggingEvent event : queue) {
				loggingPanel.log(appender.getLayout().format(event),
						getLevelForLogEvent(event));
			}
		}
		return loggingPanel;
	}

	private int getCurrentLevel() {
		int lvl = Syslog.getRootLogger().getLevel().toInt();
		return convertLogLevelToLogEntryLevel(lvl);
	}

	private int getLevelForLogEvent(LoggingEvent event) {
		int lvl = event.getLevel().toInt();
		return convertLogLevelToLogEntryLevel(lvl);
	}

	private int convertLogLevelToLogEntryLevel(int lvl) {
		int level = JLoggingPanel.LogEntry.LVL_INFO;
		switch (lvl) {
		case Level.DEBUG_INT:
			level = JLoggingPanel.LogEntry.LVL_DEBUG;
			break;
		case Level.INFO_INT:
			level = JLoggingPanel.LogEntry.LVL_INFO;
			break;
		case Level.WARN_INT:
			level = JLoggingPanel.LogEntry.LVL_WARN;
			break;
		case Level.ERROR_INT:
			level = JLoggingPanel.LogEntry.LVL_ERROR;
			break;
		case Level.FATAL_INT:
			level = JLoggingPanel.LogEntry.LVL_FATAL;
			break;

		default:
			level = JLoggingPanel.LogEntry.LVL_INFO;
			break;
		}
		return level;
	}

	public void setBufferSize(int loggingBufferSize) {
		loggingPanel.setBufferMaxSize(loggingBufferSize);
	}

	public void setLogOnTop(boolean logOnTop) {
		if(loggingPanel.isLogOnTop() != logOnTop){
			loggingPanel.switchSorting();
		}
	}

}
