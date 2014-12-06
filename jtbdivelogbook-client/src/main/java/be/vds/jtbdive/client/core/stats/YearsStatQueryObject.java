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
package be.vds.jtbdive.client.core.stats;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.vds.jtbdive.client.core.stats.axisy.StatYAxis;
import be.vds.jtbdive.core.core.Dive;

public class YearsStatQueryObject extends StatQueryObject {
	private List<Integer> years;

	public List<Integer> getYears() {
		return years;
	}

	public void setYears(List<Integer> years) {
		this.years = years;
	}

	@Override
	public StatYAxis[] getPossibleStatYAxis() {
		return new StatYAxis[] { StatYAxis.DIVE_TIME, StatYAxis.NUMBER_OF_DIVE };
	}

	@Override
	public Collection<StatSerie> getValues() {
		Map<Integer, List<Dive>> yearsDive = new HashMap<Integer, List<Dive>>();
		for (Integer year : years) {
			 yearsDive.put(year, new ArrayList<Dive>());
		}
		
		for (Dive dive : getDives()) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dive.getDate());
			int year = gc.get(Calendar.YEAR);
			if (yearsDive.containsKey(year)) {
				yearsDive.get(year).add(dive);
			}
		}

		switch (statYAxisParams.getStatYAxis()) {
		case NUMBER_OF_DIVE:
			return getValuesForNumberOfDive(yearsDive);
		case DIVE_TIME:
			return getValuesForDiveTime(yearsDive);
		}

		return null;
	}

	private Collection<StatSerie> getValuesForDiveTime(
			Map<Integer, List<Dive>> yearsDive) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (Integer year : yearsDive.keySet()) {
			int counter = 0;
			for (Dive dive : yearsDive.get(year)) {
				counter = counter + dive.getDiveTime();
			}
			map.put(year, counter);
		}

		if (isCumulated()) {
			cumulValues(map);
		}

		return fillStatSeries(map);
	}

	private List<StatSerie> fillStatSeries(Map<Integer, Integer> map) {
		StatSerie serie = new StatSerie("years");
		StatPoint point = null;
		for (Integer year : map.keySet()) {
			point = new StatPoint(year, map.get(year));
			serie.addPoint(point);
		}

		List<StatSerie> l = new ArrayList<StatSerie>();
		l.add(serie);
		return l;
	}

	private void cumulValues(Map<Integer, Integer> map) {
		int counter = 0;
		List<Integer> yy = new ArrayList<Integer>(map.keySet());
		Collections.sort(yy);
		for (Integer year : yy) {
			counter = counter + map.get(year);
			map.put(year, counter);
		}
	}

	private Collection<StatSerie> getValuesForNumberOfDive(
			Map<Integer, List<Dive>> yearsDive) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (Integer year : yearsDive.keySet()) {
			map.put(year, yearsDive.get(year).size());
		}

		if (isCumulated()) {
			cumulValues(map);
		}

		return fillStatSeries(map);
	}

	@Override
	public boolean isCumulAllowed() {
		switch (getStatYAxisParams().getStatYAxis()) {
		case DIVE_TIME:
		case NUMBER_OF_DIVE:
			return true;
		}
		return false;
	}

	@Override
	public StatXAxis getStatXAxis() {
		return StatXAxis.YEARS;
	}
}
