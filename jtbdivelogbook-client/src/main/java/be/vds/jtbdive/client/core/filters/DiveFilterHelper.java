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

import java.util.Date;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.filters.inspector.DoubleFilterInspector;
import be.vds.jtbdive.client.core.filters.inspector.IntegerFilterInspector;
import be.vds.jtbdive.client.core.filters.operator.DateOperator;
import be.vds.jtbdive.client.core.filters.operator.DiveSiteOperator;
import be.vds.jtbdive.client.core.filters.operator.DiverOperator;
import be.vds.jtbdive.client.core.filters.operator.KeyedCatalogOperator;
import be.vds.jtbdive.client.core.filters.operator.NumericOperator;
import be.vds.jtbdive.client.core.filters.operator.StringOperator;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.catalogs.DiverRole;
import be.vds.jtbdive.core.core.catalogs.KeyedCatalog;

public class DiveFilterHelper {
	private static final I18nResourceManager i18n = I18nResourceManager
			.sharedInstance();

	public static DiveFilter createDiveFilter(DiveFilterType filterType) {
		switch (filterType.getDiveFilterNature()) {
		case AGGREGATOR:
			return new AggregatorDiveFilter(filterType);
		case STRING:
			return new StringDiveFilter(filterType);
		case DOUBLE:
			return new DoubleDiveFilter(filterType);
		case INTEGER:
			return new IntegerDiveFilter(filterType);
		case DIVER:
			return new DiverDiveFilter(filterType);
		case DIVESITE:
			return new DiveSiteDiveFilter(filterType);
		case KEYED_CATALOG:
			return new KeyedCatalogDiveFilter(filterType);
		case DATE:
			return new DateDiveFilter(filterType);
		}

		return null;
	}

	public static String getTextForFilter(DiveFilter filter) {
		switch (filter.getDiveFilterType().getDiveFilterNature()) {
		case AGGREGATOR:
			return getLabelForAggregator((AggregatorDiveFilter) filter);
		case STRING:
			return getLabelForStringFilter((StringDiveFilter) filter);
		case DOUBLE:
			return getLabelForDouble((DoubleDiveFilter) filter);
		case INTEGER:
			return getLabelForInteger((IntegerDiveFilter) filter);
		case DIVER:
			return getLabelForDiver((DiverDiveFilter) filter);
		case KEYED_CATALOG:
			return getLabelForKeyedCatalog((KeyedCatalogDiveFilter) filter);
		case DIVESITE:
			return getLabelForDiveSite((DiveSiteDiveFilter) filter);
		case DATE:
			return getLabelForDate((DateDiveFilter) filter);
		default:
			return filter.toString();
		}
	}

	private static String getLabelForDiver(DiverDiveFilter filter) {
		StringBuilder sb = new StringBuilder();
		sb.append(i18n.getString("diver")).append(" ");
		DiverOperator op = (DiverOperator) filter.getOperator();

		switch (op) {
		case CONTAINS:
			sb.append(i18n.getString("contains").toLowerCase()).append(" ");
			break;
		}

		Diver d = filter.getDiverCriteria();
		sb.append(d == null ? "" : d.getFullName());
		
		DiverRole role = filter.getRole();
		sb.append(role == null ? "" : "(" + i18n.getString(role.getKey()) + ")");

		return sb.toString();
	}

	private static String getLabelForDate(DateDiveFilter filter) {
		StringBuilder sb = new StringBuilder();
		sb.append(i18n.getString(filter.getDiveFilterType().getKey())).append(
				" ");
		DateOperator op = (DateOperator) filter.getOperator();

		switch (op) {
		case EQUALS:
			sb.append(" = ");
			break;
		case AFTER_OR_EQUALS:
			sb.append(" >= ");
			break;
		case AFTER:
			sb.append(" > ");
			break;
		case BEFORE:
			sb.append(" < ");
			break;
		case BEFORE_OR_EQUALS:
			sb.append(" <= ");
			break;
		}

		Date date = (Date) filter.getCriteria();

		if (date != null) {
			sb.append(UIAgent.getInstance().getFormatDateHoursFull().format(date));
		}

		return sb.toString();
	}

	private static String getLabelForKeyedCatalog(KeyedCatalogDiveFilter filter) {
		StringBuilder sb = new StringBuilder();
		sb.append(i18n.getString(filter.getDiveFilterType().getKey())).append(
				" ");
		KeyedCatalogOperator op = (KeyedCatalogOperator) filter.getOperator();

		switch (op) {
		case EQUALS:
			sb.append(" is ");
			break;
		}

		KeyedCatalog key = (KeyedCatalog) filter.getCriteria();
		if (key != null)
			sb.append(i18n.getString(key.getKey()));

		return sb.toString();
	}

	private static String getLabelForDiveSite(DiveSiteDiveFilter filter) {
		StringBuilder sb = new StringBuilder();
		sb.append(i18n.getString(filter.getDiveFilterType().getKey())).append(
				" ");
		DiveSiteOperator op = (DiveSiteOperator) filter.getOperator();

		switch (op) {
		case EQUALS:
			sb.append(" is ");
			break;
		}

		DiveSite site = (DiveSite) filter.getCriteria();
		if (site != null)
			sb.append(site.getName());

		return sb.toString();
	}

	private static String getLabelForDouble(DoubleDiveFilter filter) {
		StringBuilder sb = new StringBuilder();
		sb.append(i18n.getString(filter.getDiveFilterType().getKey())).append(
				" ");
		NumericOperator op = (NumericOperator) filter.getOperator();

		switch (op) {
		case EQUALS:
			sb.append(" = ");
			break;
		case IS_HIGER_OR_EQUALS:
			sb.append(" >= ");
			break;
		case IS_HIGHER:
			sb.append(" > ");
			break;
		case IS_LOWER:
			sb.append(" < ");
			break;
		case IS_LOWER_OR_EQUALS:
			sb.append(" <= ");
			break;
		}

		DoubleFilterInspector inspector = ((DoubleFilterInspector) filter
				.getDiveFilterType().getInspector());
		sb.append(inspector.convertFromModel(filter.getFirstCriteria()));

		if (inspector.getUnitSymbol() != null)
			sb.append(inspector.getUnitSymbol());

		return sb.toString();
	}

	private static String getLabelForInteger(IntegerDiveFilter filter) {
		StringBuilder sb = new StringBuilder();
		sb.append(i18n.getString(filter.getDiveFilterType().getKey())).append(
				" ");
		NumericOperator op = (NumericOperator) filter.getOperator();

		switch (op) {
		case EQUALS:
			sb.append(" = ");
			break;
		case IS_HIGER_OR_EQUALS:
			sb.append(" >= ");
			break;
		case IS_HIGHER:
			sb.append(" > ");
			break;
		case IS_LOWER:
			sb.append(" < ");
			break;
		case IS_LOWER_OR_EQUALS:
			sb.append(" <= ");
			break;
		}

		IntegerFilterInspector inspector = ((IntegerFilterInspector) filter
				.getDiveFilterType().getInspector());

		sb.append(filter.getFirstCriteria());

		if (inspector.getUnitSymbol() != null)
			sb.append(inspector.getUnitSymbol());

		return sb.toString();
	}

	private static String getLabelForStringFilter(StringDiveFilter filter) {
		StringBuilder sb = new StringBuilder();
		sb.append(i18n.getString(filter.getDiveFilterType().getKey())).append(
				" ");
		StringOperator op = (StringOperator) filter.getOperator();

		switch (op) {
		case CONTAINS:
			sb.append(i18n.getString("contains").toLowerCase()).append(" ");
			break;
		case EQUALS:
			sb.append(i18n.getString("equals").toLowerCase()).append(" ");
			break;
		}

		sb.append(filter.getTextCriteria());

		return sb.toString();
	}

	private static String getLabelForAggregator(AggregatorDiveFilter filter) {
		StringBuilder sb = new StringBuilder();
		sb.append(i18n.getString("aggregator")).append(": ");

		if (filter.getOperator() != null) {
			sb.append(i18n.getString(filter.getOperator().getKey())
					.toLowerCase());
		}

		return sb.toString();
	}

}
