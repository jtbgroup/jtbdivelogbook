/*
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.vds.jtbdive.core.core.catalogs.Gaz;
import be.vds.jtbdive.core.core.comparator.GazesComparator;

/**
 * 
 * @author gautier
 */
public class GazMix implements Serializable {

	private static final long serialVersionUID = -6995100841239206094L;
	private static final int PERCENT_OXYGEN_NORMAL = 21;
	private static final int PERCENT_NITROGEN_NORMAL = 79;
	private static final int HUNDRED_PERCENT = 100;
	private Map<Gaz, Integer> gazMix = new HashMap<Gaz, Integer>();

	/**
	 * Adds a gaz into the mix and the percentage of this gaz in the mix
	 * 
	 * @param gaz
	 * @param percentage
	 *            between 0 and 100
	 */
	public void addGaz(Gaz gaz, int percentage) {
		gazMix.put(gaz, percentage);
	}

	public Integer getPercentage(Gaz gaz) {
		Integer inte = gazMix.get(gaz);
		return inte == null ? Integer.valueOf(0) : inte;
	}

	/**
	 * 
	 * The list is never null;
	 * 
	 * @return a list of Gazes present
	 */
	public List<Gaz> getGazes() {
		return new ArrayList<Gaz>(gazMix.keySet());
	}

	public int size() {
		return gazMix.size();
	}

	public boolean hasSameComposition(GazMix gazMix) {
		if (gazMix.size() != size()) {
			return false;
		}

		for (Gaz gaz : getGazes()) {
			if (!gazMix.contains(gaz)) {
				return false;
			} else {
				if (getPercentage(gaz) != (gazMix.getPercentage(gaz))) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean contains(Gaz gaz) {
		return gazMix.keySet().contains(gaz);

	}

	public int getPercentRest() {
		int i = HUNDRED_PERCENT;
		for (Integer integer : gazMix.values()) {
			i = i - integer;
		}
		if (i < 0){
			i = 0;
		}

		return i;
	}

	/**
	 * A default GazMix consists on normal air. 21% of oxygen, 79% of nitrogen.
	 * 
	 * @return
	 */
	public static GazMix getDefaultGazMix() {
		GazMix mix = new GazMix();
		mix.addGaz(Gaz.GAZ_OXYGEN, PERCENT_OXYGEN_NORMAL);
		mix.addGaz(Gaz.GAZ_NITROGEN, PERCENT_NITROGEN_NORMAL);
		return mix;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		List<Gaz> gazes = getGazes();
		Collections.sort(gazes, new GazesComparator());
		for (Gaz gaz : gazes) {
			sb.append(gaz.getFormule()).append("[").append(gazMix.get(gaz))
					.append("%]").append(" - ");
		}
		return sb.toString().substring(0, sb.lastIndexOf(" - "));
	}
}
