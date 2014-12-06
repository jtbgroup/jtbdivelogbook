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
package be.vds.jtbdive.client.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import be.vds.jtbdive.core.logging.Syslog;

public class LanguageHelper {
	private static final Syslog LOGGER = Syslog.getLogger(LanguageHelper.class);
	public static final Locale LOCALE_ENGLISH = new Locale("en", "EN");
	private static List<Locale> knownLocales;

	public static List<Locale> getKnownLocales() {
		if (knownLocales != null) {
			return knownLocales;
		}

		List<Locale> testedLocales = null;
		try {
			URL url = LanguageHelper.class.getClassLoader()
					.getSystemClassLoader().getResource("resources/bundles");
			File file = new File(url.getFile());
			LOGGER.debug("Path for bundle files is : " + url);
			String fileName = URLDecoder
					.decode(file.getAbsolutePath(), "UTF-8");
			LOGGER.debug("FileName is : " + fileName);

			if (url.getProtocol().startsWith("jar")) {
				testedLocales = getLocalesFromJar(url);
			} else if (url.getProtocol().equals("file")) {
				testedLocales = getLocalesFromFile(new File(fileName));
			}
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Encoding problem in the file name of the bundles", e);
		}

		knownLocales = testedLocales;
		return knownLocales;
	}

	public static String getLocaleIconName16(Locale locale) {
		return locale.getCountry() + "16.png";
	}

	public static String getLocaleIconName24(Locale locale) {
		return locale.getCountry() + "24.png";
	}

	public static void main(String[] args) {
		LanguageHelper.getKnownLocales();
	}

	@SuppressWarnings("unchecked")
	private static List<Locale> getLocalesFromJar(URL url) {
		List<Locale> locs = new ArrayList<Locale>();
		String s = url.getFile().substring(url.getFile().indexOf(":") + 1,
				url.getFile().lastIndexOf("!"));
		try {
			s = URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e);
		}

		try {
			ZipFile file = new ZipFile(s);
			Enumeration<ZipEntry> enumEntries = (Enumeration<ZipEntry>) file
					.entries();
			Pattern pat = Pattern
					.compile("Bundle(_(..)_(..)(\\.properties)){1}");
			while (enumEntries.hasMoreElements()) {
				ZipEntry zipEntry = enumEntries.nextElement();
				if (zipEntry.getName().startsWith("resources/bundles/")
						&& !zipEntry.isDirectory()) {
					String name = zipEntry.getName().substring(
							zipEntry.getName().lastIndexOf("/") + 1);
					Matcher mat = pat.matcher(name);
					if (mat.matches()) {
						locs.add(new Locale(mat.group(2), mat.group(3)));
					}
				}
			}

		} catch (IOException ex) {
			Logger.getLogger(LanguageHelper.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		return locs;
	}

	private static List<Locale> getLocalesFromFile(File f) {
		List<Locale> locs = new ArrayList<Locale>();
		if (f.exists() && f.isDirectory()) {
			String[] files = f.list();
			Pattern pat = Pattern
					.compile("Bundle(_(..)_(..)(\\.properties)){1}");
			for (String file : files) {
				Matcher mat = pat.matcher(file);
				if (mat.matches()) {
					locs.add(new Locale(mat.group(2), mat.group(3)));
				}
			}
		}
		return locs;
	}
}
