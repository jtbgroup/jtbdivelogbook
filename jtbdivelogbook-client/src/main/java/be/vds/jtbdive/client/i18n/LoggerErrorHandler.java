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
package be.vds.jtbdive.client.i18n;

import java.util.Locale;

import org.apache.log4j.Level;

import be.smd.i18n.ContextHandler;
import be.smd.i18n.ErrorHandler;
import be.vds.jtbdive.core.logging.Syslog;


public class LoggerErrorHandler implements ErrorHandler{

	/** The unique instance of {@link LoggerErrorHandler} */
	private static LoggerErrorHandler instance;
	
	/** The logger used to report missing values */
	private static final Syslog LOGGER = Syslog.getLogger(LoggerErrorHandler.class);

	private Level level;
	
	
	/** Creates a {@link LoggerErrorHandler} */
	private LoggerErrorHandler(){}

	/** @see ContextHandler#resourceNotFound(String, Locale) */
	public void resourceNotFound(String bundleKey, Locale locale) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("*** The resource for key \"");
		sb.append(bundleKey);
		sb.append("\" cannot be found for the locale \"");
		sb.append(locale);
		
		LOGGER.log(level, sb.toString());
	}
	
	/**
	 * Returns the unique instance of {@link LoggerErrorHandler} and creates it as needed
	 * @return {@link LoggerErrorHandler} the unique instance of the Total health context handler 
	 */
	public static LoggerErrorHandler getInstance() {
		return instance != null ? instance : (instance = new LoggerErrorHandler());
	}

	public void setLevel(Level level) {
		this.level = level;
	}
}
