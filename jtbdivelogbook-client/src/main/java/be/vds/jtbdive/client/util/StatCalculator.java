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
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import be.vds.jtbdive.client.core.stats.StatQueryObject;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.PalanqueeEntry;
import be.vds.jtbdive.core.core.comparator.DiveDateComparator;
import be.vds.jtbdive.core.utils.ObjectSerializer;

public class StatCalculator {

	public static Map<Diver, Integer> getNumberOfDivePerDiver(LogBook logBook,
			List<Diver> divers) {
		Map<Diver, Integer> result = new HashMap<Diver, Integer>();

		List<Dive> dives = getDivesCopy(logBook);

		for (Dive dive : dives) {
			List<Diver> dds = getDivers(dive);
			for (Diver diver : dds) {
				if (divers.contains(diver)) {
					Integer i = result.get(diver);
					if (i == null)
						i = 0;
					result.put(diver, ++i);
				}
			}
		}
		return result;
	}

	private static List<Diver> getDivers(Dive dive) {
		List<Diver> divers = new ArrayList<Diver>();
		if (null != dive.getPalanquee()) {
			for (PalanqueeEntry pa : dive.getPalanquee().getPalanqueeEntries()) {
				if (!divers.contains(pa.getDiver()))
					divers.add(pa.getDiver());
			}
		}
		return divers;
	}

	public static Map<DiveSite, Integer> getNumberOfDivePerDiveSite(
			LogBook logBook, List<DiveSite> diveLocations) {
		Map<DiveSite, Integer> result = new HashMap<DiveSite, Integer>();
		List<Dive> dives = getDivesCopy(logBook);

		for (DiveSite dl : logBook.getDiveSites()) {
			if (diveLocations.contains(dl)) {
				result.put(dl, 0);
			}
		}

		for (Dive dive : dives) {
			if (null != dive.getDiveSite()
					&& diveLocations.contains(dive.getDiveSite())) {
				Integer i = result.get(dive.getDiveSite());
				if (i == null)
					i = 0;

				result.put(dive.getDiveSite(), i + 1);
			}
		}

		return result;
	}

	public static Map<Integer, Integer> getNumberOfDivePerYear(LogBook logBook,
			List<Integer> years) {
		Map<Integer, Integer> result = new TreeMap<Integer, Integer>();
		List<Dive> dives = getDivesCopy(logBook);

		List<Integer> logYears = getDiveYears(logBook);
		Collections.sort(logYears);

		for (Integer year : logYears) {
			if (years.contains(year)) {
				result.put(year, 0);
			}
		}

		for (Dive dive : dives) {
			int yearOfDive = -1;

			if (dive.getDate() != null) {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(dive.getDate());
				yearOfDive = gc.get(GregorianCalendar.YEAR);
			}

			if (years.contains(yearOfDive)) {
				result.put(yearOfDive, result.get(yearOfDive) + 1);
			}
		}

		return result;
	}

	public static List<Integer> getDiveYears(LogBook logBook) {
		List<Integer> years = new ArrayList<Integer>();
		if (logBook == null)
			return years;
		for (Dive dive : logBook.getDives()) {
			if (dive.getDate() != null) {
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(dive.getDate());
				if (!years.contains(cal.get(Calendar.YEAR)))
					years.add(cal.get(Calendar.YEAR));
			}
		}

		return years;
	}

	public static List<Map<Integer, Map<DiveSite, Integer>>> getNumberOfDivePerDiveSitePerYear(
			LogBook logBook, List<DiveSite> diveLocations, List<Integer> years) {
		List<Map<Integer, Map<DiveSite, Integer>>> result = new ArrayList<Map<Integer, Map<DiveSite, Integer>>>();

		int refYear = 0;

		List<Dive> dives = getDivesCopy(logBook);
		Collections.sort(dives, new DiveDateComparator());

		Map<DiveSite, Integer> currentYearMap = null;
		for (Dive dive : dives) {

			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(dive.getDate());
			int currentYear = cal.get(Calendar.YEAR);
			if (years.contains(currentYear)) {
				if (currentYear > refYear) {
					if (0 != refYear) {
						Map<Integer, Map<DiveSite, Integer>> map = new HashMap<Integer, Map<DiveSite, Integer>>();
						map.put(refYear, currentYearMap);
						result.add(map);
					}
					refYear = currentYear;
					currentYearMap = new HashMap<DiveSite, Integer>();
					for (DiveSite diveLocation : logBook.getDiveSites()) {
						if (diveLocations.contains(diveLocation)) {
							currentYearMap.put(diveLocation, 0);
						}
					}
					if (null != dive.getDiveSite()) {
						DiveSite d = dive.getDiveSite();
						if (diveLocations.contains(d)) {
							currentYearMap.put(d, currentYearMap.get(d) + 1);
						}
					}

				} else {
					if (null != dive.getDiveSite()) {
						DiveSite dl = dive.getDiveSite();
						if (diveLocations.contains(dl)) {
							currentYearMap.put(dl, currentYearMap.get(dl) + 1);
						}
					}
				}
			}
		}

		Map<Integer, Map<DiveSite, Integer>> map = new HashMap<Integer, Map<DiveSite, Integer>>();
		map.put(refYear, currentYearMap);
		result.add(map);

		return result;
	}

	public static List<Map<Integer, Map<Diver, Integer>>> getNumberOfDivePerDiverPerYear(
			LogBook logBook, List<Diver> divers, List<Integer> years) {
		List<Map<Integer, Map<Diver, Integer>>> result = new ArrayList<Map<Integer, Map<Diver, Integer>>>();

		int refYear = 0;

		List<Dive> dives = getDivesCopy(logBook);
		Collections.sort(dives, new DiveDateComparator());

		Map<Diver, Integer> currentYearMap = null;
		for (Dive dive : dives) {

			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(dive.getDate());
			int currentYear = cal.get(Calendar.YEAR);

			if (years.contains(currentYear)) {
				if (currentYear > refYear) {
					if (0 != refYear) {
						Map<Integer, Map<Diver, Integer>> map = new HashMap<Integer, Map<Diver, Integer>>();
						map.put(refYear, currentYearMap);
						result.add(map);
					}
					refYear = currentYear;
					currentYearMap = new HashMap<Diver, Integer>();

					for (Diver diver : getDivers(dive)) {
						if (divers.contains(diver)) {
							Integer i = currentYearMap.get(diver);
							if (null == i)
								i = 0;
							currentYearMap.put(diver, ++i);
						}
					}

				} else {
					for (Diver diver : getDivers(dive)) {
						if (divers.contains(diver)) {
							Integer i = currentYearMap.get(diver);
							if (i == null)
								i = 0;
							currentYearMap.put(diver, ++i);
						}
					}
				}
			}
		}

		Map<Integer, Map<Diver, Integer>> map = new HashMap<Integer, Map<Diver, Integer>>();
		map.put(refYear, currentYearMap);
		result.add(map);

		return result;
	}

	private static List<Dive> getDivesCopy(LogBook logBook) {
		List<Dive> dives = new ArrayList<Dive>();
		for (Dive dive : logBook.getDives()) {
			dives.add((Dive) ObjectSerializer.cloneObject(dive));
		}
		return dives;
	}

	public static Map<Dive, Double> getDiveDepthPerDive(
			StatQueryObject statQueryObject) {
		Map<Dive, Double> m = new HashMap<Dive, Double>();
		for (Dive dive : statQueryObject.getDives()) {
			m.put(dive, dive.getMaxDepth());
		}
		return m;
	}


}
