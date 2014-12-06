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
package be.vds.jtbdive.core.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * A version must be formatted like 'x[.y[.z[-SNAPSHOT]]]'
 * 
 * @author Gautier Vanderslyen
 */
public final class Version implements Comparable<Version> {

	private int[] versionNumbers;

	/**
	 * The extra field is used to indicate a build date, or a "snapshot"
	 * indication. It means that a version containing an extra field is always
	 * lower than the same version without extra field.
	 */
	private String extra;
	private static final Logger LOGGER = Logger.getLogger(Version.class);
	private static final String VERSION_PATTERN = "(\\d+[\\.\\d+]*)(-(SNAPSHOT))?";
	private static Version currentVersion;

	private Version(int[] versionNumbers, String extra) {
		this.versionNumbers = Arrays.copyOf(versionNumbers, versionNumbers.length);
		this.extra = extra;
	}
	
	public static Version createVersion(String version) {
		int[] versionNumbers = getVersionNumbers(version);
		String extra = getVersionExtra(version);
		return new Version(versionNumbers, extra);
	}

	private static String getCurrentVersionString() {
		Properties props = new Properties();
		try {
			props.load(Version.class.getClassLoader().getResourceAsStream(
					"resources/version.properties"));
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
		}

		return props.getProperty("version");
	}

	public static boolean isFormatValid(String version) {
		Pattern p = Pattern.compile(VERSION_PATTERN);
		Matcher matcher = p.matcher(version);
		return matcher.matches();
	}

	private static int[] getVersionNumbers(String version) {
		Pattern p = Pattern.compile(VERSION_PATTERN);
		Matcher matcher = p.matcher(version);
		if (matcher.matches()) {
			String[] numbersString = matcher.group(1).split("\\.");
			int[] numbers = new int[numbersString.length];
			for (int i = 0; i < numbersString.length; i++) {
				numbers[i] = Integer.parseInt(numbersString[i]);
			}
			return numbers;
		}
		return null;
	}
	
	private static String getVersionExtra(String version) {
		Pattern p = Pattern.compile(VERSION_PATTERN);
		Matcher matcher = p.matcher(version);
		if (matcher.matches()) {
			return matcher.group(3);
		}
		return null;
	}

	/**
	 * Gets the version of the currently running application.
	 * 
	 * @return
	 */
	public static Version getCurrentVersion() {
		if (null == currentVersion) {
			String versionString = getCurrentVersionString();
			currentVersion = Version.createVersion(versionString);
		}
		return currentVersion;
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getBaseVersion());

		if (extra != null) {
			sb.append("-").append(extra);
		}
		return sb.toString();
	}

	/**
	 * This method returns a String containing only the version, revision and
	 * modification
	 * 
	 * @return the formatter String v.r.m
	 */
	public String getBaseVersion() {
		StringBuilder sb = new StringBuilder();
		for (int number : versionNumbers) {
			sb.append(number).append(".");
		}
		return sb.toString().substring(0, sb.length() - 1);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof Version) {
			Version version = (Version) obj;
			if(Arrays.equals(versionNumbers, version.getVersionNumbers())){
				if(extra == null && version.getExtra() == null){
					return true;
				}else if (extra != null && version.getExtra() != null){
					return extra.equals(version.getExtra());
				}
			}
		}
		return false;
	}


	public int hashCode() {
		int i = 0;
		for (Integer number : versionNumbers) {
			i += number;
		}
		return i + extra.hashCode();
	};

	public boolean isHigerOrEqualsThan(Version version) {
		return !isLowerThan(version);
	}

	public boolean isLowerThan(String version) {
		Version vers = createVersion(version);
		return isLowerThan(vers);
	}

	/**
	 * This method determines whether the current (this) version is lower than
	 * the provided one.
	 * <p>
	 * Attention, the extra field is used to indicate a build date, or a
	 * "snapshot" indication. It means that a version containing an extra field
	 * is always lower than the same version without extra field.
	 * <p>
	 * <u>Example :</u><br/>
	 * - 2.1.0 > 2.0.0<br/>
	 * - 2.1.0 < 2.5.0<br/>
	 * - 2.1.0 > 2.1.0-SNAPSHOT<br/>
	 * - 2.1.0 < 2.1.0<br/>
	 * 
	 * @param version
	 *            the provided version relative to which we want to compare the
	 *            current (this) version.
	 * @return <b>true</b> if the current version (this) is lower than the
	 *         provided one, <b>false</b> if the current version (this) is
	 *         higher or equals.
	 */
	public boolean isLowerThan(Version version) {
		int[] params1 = this.getVersionNumbers();
		int[] params2 = version.getVersionNumbers();
		for (int i = 0; i < params1.length - 1; i++) {
			if (params1[i] != params2[i]) {
				return params1[i] < params2[i];
			}
		}

		if (extra == null) {
			return false;
		} else if (version.getExtra() == null) {
			return extra != null;
		}
		return version.getExtra().compareTo(extra) == 1;
	}

	/**
	 * Gets the extra field of the version
	 * 
	 * @return
	 */
	private String getExtra() {
		return extra;
	}

	/**
	 * Compares the current (this) version to the given one.
	 * 
	 * @return 0 if the versions are equals (from the equals() method), -1 if
	 *         the current (this) version is lower than the provided one and 1
	 *         in other cases.
	 */
	public int compareTo(Version version) {
		if (this.equals(version)) {
			return 0;
		}

		if (this.isLowerThan(version)) {
			return -1;
		}
		return 1;
	}
	
	private int[] getVersionNumbers() {
		return versionNumbers;
	}

}
