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

import java.util.ArrayList;
import java.util.List;

public abstract class SystemProperties {

	public static final String UNKNOWN_OS_TYPE = "Unknown";
	public static final String WINDOWS_OS_TYPE = "Windows";
	public static final String LINUX_OS_TYPE = "Linux";
	private static List<String> WINDOWS_OS_NAMES;
	static {
		WINDOWS_OS_NAMES = new ArrayList<String>();
		WINDOWS_OS_NAMES.add("Windows XP");
	}
	public static List<String> getWindowsOsNames() {
		return WINDOWS_OS_NAMES;
	}
	private static List<String> LINUX_OS_NAMES;
	static {
		LINUX_OS_NAMES = new ArrayList<String>();
		LINUX_OS_NAMES.add("Linux");
	}
	public static List<String> getLinuxOsNames() {
		return LINUX_OS_NAMES;
	}
	public static List<String> getOsNames() {
		List<String> list = new ArrayList<String>();
		list.addAll(LINUX_OS_NAMES);
		list.addAll(WINDOWS_OS_NAMES);
		return list;
	}

	public static List<String> getOsTypes() {
		List<String> list = new ArrayList<String>();
		list.add(LINUX_OS_TYPE);
		list.add(WINDOWS_OS_TYPE);
		return list;
	}}
