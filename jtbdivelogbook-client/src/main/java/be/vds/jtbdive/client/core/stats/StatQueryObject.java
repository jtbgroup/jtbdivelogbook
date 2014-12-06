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

import java.util.Collection;
import java.util.List;

import be.vds.jtbdive.client.core.stats.axisy.StatYAxis;
import be.vds.jtbdive.client.core.stats.axisy.StatYAxisDiveDepthParams;
import be.vds.jtbdive.client.core.stats.axisy.StatYAxisDiveTemperatureParams;
import be.vds.jtbdive.client.core.stats.axisy.StatYAxisDiveTimeParams;
import be.vds.jtbdive.client.core.stats.axisy.StatYAxisNumberOfDiveParams;
import be.vds.jtbdive.client.core.stats.axisy.StatYAxisParams;
import be.vds.jtbdive.core.core.Dive;

public abstract class StatQueryObject {

	protected StatYAxisParams statYAxisParams;

	protected List<Dive> dives;

	protected boolean cumulated;

	public boolean isCumulated() {
		return cumulated;
	}

	public void setCumulated(boolean cumulated) {
		this.cumulated = cumulated;
	}

	public List<Dive> getDives() {
		return dives;
	}

	public void setDives(List<Dive> dives) {
		this.dives = dives;
	}

	public StatYAxisParams getStatYAxisParams() {
		return statYAxisParams;
	}

	public void setStatYAxisParams(StatYAxisParams statYAxisParams) {
		this.statYAxisParams = statYAxisParams;
	}



	public static StatYAxisParams createStatYAxisParam(StatYAxis statYAxis) {
		switch (statYAxis) {
		case DEPTHS:
			return new StatYAxisDiveDepthParams();
		case DIVE_TIME:
			return new StatYAxisDiveTimeParams();
		case NUMBER_OF_DIVE:
			return new StatYAxisNumberOfDiveParams();
		case TEMPERATURES:
			return new StatYAxisDiveTemperatureParams();

		default:
			break;
		}
		return null;
	}
	
	public static StatQueryObject createStatQueryObject(StatXAxis statXAxis) {
		switch (statXAxis) {
		case DIVE:
			return new DiveStatQueryObject();
		case YEARS:
			return new YearsStatQueryObject();
		case DIVER:
			return new DiverStatQueryObject();
		case DIVESITE:
			return new DiveSiteStatQueryObject();
		case DEPTHS:
			return new DiveDepthsStatQueryObject();
		}

		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getStatXAxis() == null ? "" : getStatXAxis().getKey());
		sb.append(" - ");
		sb.append(statYAxisParams == null ? "" : statYAxisParams.getStatYAxis()
				.getKey());
		return sb.toString();
	}

	public abstract StatXAxis getStatXAxis();

	public abstract Collection<StatSerie> getValues();

	public abstract StatYAxis[] getPossibleStatYAxis();

	public abstract boolean isCumulAllowed();


}
