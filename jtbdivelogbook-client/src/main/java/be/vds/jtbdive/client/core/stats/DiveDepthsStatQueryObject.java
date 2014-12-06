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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.client.core.stats.axisy.StatYAxis;
import be.vds.jtbdive.core.core.Dive;

public class DiveDepthsStatQueryObject extends StatQueryObject {

	/**
	 * The pitch in the model unit.
	 */
	private double pitch = 5;

	public double getPitch() {
		return pitch;
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
	}

	@Override
	public StatYAxis[] getPossibleStatYAxis() {
		return new StatYAxis[] { StatYAxis.NUMBER_OF_DIVE };
	}

	@Override
	public Collection<StatSerie> getValues() {
		boolean maxReached = false;
		double maxDepth = LogBookUtilities.getMaxDepth(dives);
		Map<Double, List<Dive>> categories = new HashMap<Double, List<Dive>>();
		double currentDepth = 0;

		while (!maxReached) {
			categories.put(currentDepth, new ArrayList<Dive>());
			currentDepth -= pitch;
			maxReached = maxDepth > currentDepth;
		}

		for (Dive dive : getDives()) {
			double depth = dive.getMaxDepth();
			double key = depth - (depth % pitch);
			categories.get(key).add(dive);
		}

		StatSerie serie = new StatSerie("depths");
		StatPoint point = null;
		for (Double depth : categories.keySet()) {
			int size = categories.get(depth).size();
			point = new StatPoint(depth, size);
			serie.addPoint(point);
		}

		if (isCumulated()) {
			cumulValues(serie);
		}

		List<StatSerie> l = new ArrayList<StatSerie>();
		l.add(serie);
		return l;
	}

	private void cumulValues(StatSerie statSerie) {
		int counter = 0;
		List<StatPoint> yy = new ArrayList<StatPoint>(statSerie.getPoints());
		Collections.sort(yy, new Comparator<StatPoint>() {
			@Override
			public int compare(StatPoint o1, StatPoint o2) {
				return -((Double) o1.getX()).compareTo((Double) o2.getX());
			}
		});
		
		for (StatPoint sp : yy) {
			counter = counter + sp.getY().intValue();
			sp.setY(counter);
		}
	}

	@Override
	public boolean isCumulAllowed() {
		return true;
	}

	@Override
	public StatXAxis getStatXAxis() {
		return StatXAxis.DEPTHS;
	}
}
