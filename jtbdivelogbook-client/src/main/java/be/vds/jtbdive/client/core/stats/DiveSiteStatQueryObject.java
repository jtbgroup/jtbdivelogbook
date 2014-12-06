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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.vds.jtbdive.client.core.stats.axisy.StatYAxis;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveSite;

public class DiveSiteStatQueryObject extends StatQueryObject {
	private List<DiveSite> diveSites;

	public List<DiveSite> getDiveSites() {
		return diveSites;
	}

	public void setDiveSites(List<DiveSite> diveSites) {
		this.diveSites = diveSites;
	}

	@Override
	public StatYAxis[] getPossibleStatYAxis() {
		return new StatYAxis[] { StatYAxis.NUMBER_OF_DIVE };
	}

	@Override
	public Collection<StatSerie> getValues() {
		StatSerie serie = new StatSerie("divesites");
		StatPoint point = null;

		Map<DiveSite, Integer> map = new HashMap<DiveSite, Integer>();
		for (DiveSite diveSite : diveSites) {
			map.put(diveSite, 0);
		}

		for (Dive dive : dives) {
			DiveSite ds = dive.getDiveSite();
			if (null != ds) {
				if (map.containsKey(ds)) {
					map.put(ds, map.get(ds) + 1);
				}
			}
		}

		for (DiveSite diveSite : map.keySet()) {
			point = new StatPoint(diveSite, map.get(diveSite));
			serie.addPoint(point);
		}

		List<StatSerie> l = new ArrayList<StatSerie>();
		l.add(serie);
		return l;
	}

	@Override
	public boolean isCumulAllowed() {
		return false;
	}

	@Override
	public StatXAxis getStatXAxis() {
		return StatXAxis.DIVESITE;
	}
}
