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
import java.util.Set;

import be.vds.jtbdive.client.core.stats.axisy.StatYAxis;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.Palanquee;
import be.vds.jtbdive.core.core.PalanqueeEntry;

public class DiverStatQueryObject extends StatQueryObject {
	private List<Diver> divers;

	public List<Diver> getDivers() {
		return divers;
	}

	public void setDivers(List<Diver> divers) {
		this.divers = divers;
	}
	@Override
	public StatYAxis[] getPossibleStatYAxis() {
		return new StatYAxis[] { StatYAxis.NUMBER_OF_DIVE };
	}

	@Override
	public Collection<StatSerie> getValues() {
		StatSerie serie = new StatSerie("dives");
		StatPoint point = null;

		Map<Diver, Integer> map = new HashMap<Diver, Integer>();
		for (Diver diver : divers) {
			map.put(diver, 0);
		}

		for (Dive dive : dives) {
			Palanquee pal = dive.getPalanquee();
			if (null != pal) {
				Set<PalanqueeEntry> pe = pal.getPalanqueeEntries();
				for (PalanqueeEntry palanqueeEntry : pe) {
					Diver d = palanqueeEntry.getDiver();
					if (map.containsKey(d)) {
						map.put(d, map.get(d) + 1);
					}
				}
			}
		}

		for (Diver diver : map.keySet()) {
			point = new StatPoint(diver, map.get(diver));
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
		return StatXAxis.DIVER;
	}
}
