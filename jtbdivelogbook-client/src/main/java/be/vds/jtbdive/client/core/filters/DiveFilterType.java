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
package be.vds.jtbdive.client.core.filters;

import be.vds.jtbdive.client.core.filters.inspector.CommentInspector;
import be.vds.jtbdive.client.core.filters.inspector.DepthInspector;
import be.vds.jtbdive.client.core.filters.inspector.DiveDateInspector;
import be.vds.jtbdive.client.core.filters.inspector.DiveNumberInspector;
import be.vds.jtbdive.client.core.filters.inspector.DivePlatformInspector;
import be.vds.jtbdive.client.core.filters.inspector.DivePurposeInspector;
import be.vds.jtbdive.client.core.filters.inspector.DiveRatingInspector;
import be.vds.jtbdive.client.core.filters.inspector.DiveSiteInspector;
import be.vds.jtbdive.client.core.filters.inspector.DiveTimeInspector;
import be.vds.jtbdive.client.core.filters.inspector.DiveTypeInspector;
import be.vds.jtbdive.client.core.filters.inspector.DiverInspector;
import be.vds.jtbdive.client.core.filters.inspector.FilterInspector;
import be.vds.jtbdive.client.core.filters.inspector.WaterTemperatureInspector;
import be.vds.jtbdive.core.core.catalogs.KeyedCatalog;

public enum DiveFilterType implements KeyedCatalog {

	AGGREGATOR("aggregator", DiveFilterNature.AGGREGATOR, null), //
	COMMENT("comment", DiveFilterNature.STRING, new CommentInspector()), //
	DEPTH("depth", DiveFilterNature.DOUBLE, new DepthInspector()), //
	DIVE_TIME("dive.time", DiveFilterNature.DOUBLE, new DiveTimeInspector()), //
	WATER_TEMPERATURE("temperature.water", DiveFilterNature.DOUBLE,
			new WaterTemperatureInspector()), //
	DIVESITE("divesite", DiveFilterNature.DIVESITE, new DiveSiteInspector()), //
	DIVE_PURPOSE("dive.purpose", DiveFilterNature.KEYED_CATALOG,
			new DivePurposeInspector()), //
	DIVE_PLATFORM("dive.platform", DiveFilterNature.KEYED_CATALOG,
			new DivePlatformInspector()), //
	DIVE_TYPE("dive.type", DiveFilterNature.KEYED_CATALOG,
			new DiveTypeInspector()), //
	NUMBER("number", DiveFilterNature.INTEGER, new DiveNumberInspector()), //
	RATING("rating", DiveFilterNature.INTEGER, new DiveRatingInspector()), //
	DATE("date", DiveFilterNature.DATE, new DiveDateInspector()),//
	DIVER("diver", DiveFilterNature.DIVER, new DiverInspector()),//
	;

	private String key;

	public String getKey() {
		return key;
	}

	private DiveFilterNature diveFilterNature;
	private FilterInspector inspector;

	private DiveFilterType(String key, DiveFilterNature diveFilterNature,
			FilterInspector inspector) {
		this.key = key;
		this.diveFilterNature = diveFilterNature;
		this.inspector = inspector;
	}

	public static DiveFilterType getFilterType(String key) {
		for (DiveFilterType ft : values()) {
			if (ft.key.equals(key))
				return ft;
		}
		return null;
	}

	public FilterInspector getInspector() {
		return inspector;
	}

	public DiveFilterNature getDiveFilterNature() {
		return diveFilterNature;
	}
}
