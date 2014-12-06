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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import be.vds.jtbdive.client.core.stats.axisy.StatYAxis;
import be.vds.jtbdive.core.core.Dive;

public class DiveStatQueryObject extends StatQueryObject {

	@Override
	public StatYAxis[] getPossibleStatYAxis() {
		return new StatYAxis[] { StatYAxis.DEPTHS, StatYAxis.DIVE_TIME,
				StatYAxis.TEMPERATURES };
	}

	@Override
	public Collection<StatSerie> getValues() {
		StatSerie serie = new StatSerie("dives");
		StatPoint point = null;
		for (Dive dive : dives) {
			point = new StatPoint(dive.getDate(), getYValue(dive));
			serie.addPoint(point);
		}

		if (isCumulated()) {
			List<StatPoint> pointsTemp = serie.getPoints();
			Collections.sort(pointsTemp, new Comparator<StatPoint>(){
				@Override
				public int compare(StatPoint o1, StatPoint o2) {
					return ((Date)(o1).getX()).compareTo((Date)(o2).getX());
				}
			});

			Number cumulY = 0;
			for (StatPoint statPoint : pointsTemp) {
				Number y = statPoint.getY();
				cumulY = new BigDecimal(cumulY.floatValue())
						.add(new BigDecimal(y.floatValue()));
				statPoint.setY(cumulY);
			}
		}

		List<StatSerie> l = new ArrayList<StatSerie>();
		l.add(serie);
		return l;
	}

	private Number getYValue(Dive dive) {
		switch (statYAxisParams.getStatYAxis()) {
		case DEPTHS:
			return dive.getMaxDepth();
		case DIVE_TIME:
			return dive.getDiveTime();
		case TEMPERATURES:
			return dive.getWaterTemperature();
		}
		return 0;
	}

	@Override
	public boolean isCumulAllowed() {
		switch (getStatYAxisParams().getStatYAxis()) {
		case DIVE_TIME:
			return true;
		}
		return false;
	}
	@Override
	public StatXAxis getStatXAxis() {
		return StatXAxis.DIVE;
	}
}
