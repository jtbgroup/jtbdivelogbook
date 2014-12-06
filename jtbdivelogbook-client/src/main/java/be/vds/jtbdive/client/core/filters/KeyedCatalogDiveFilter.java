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

import java.util.List;

import be.vds.jtbdive.client.core.filters.operator.KeyedCatalogOperator;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.catalogs.KeyedCatalog;

public class KeyedCatalogDiveFilter extends DiveFilter {

	public KeyedCatalogDiveFilter(DiveFilterType diveFilterType) {
		super(diveFilterType);
		setOperator(KeyedCatalogOperator.EQUALS);
	}

	public void setKeyedCriteria(KeyedCatalog catalog) {
		setCriteria(catalog);
	}

	public KeyedCatalog getKeyedCriteria() {
		return (KeyedCatalog) getCriteria();
	}

	@Override
	public boolean isValid(Dive dive) {
		KeyedCatalog catalog = getKeyedCriteria();

		KeyedCatalogOperator op = (KeyedCatalogOperator) getOperator();
		switch (op) {
		case EQUALS:
			return checkIs(catalog, (List<KeyedCatalog>) diveFilterType
					.getInspector().getFilterParameter(dive));
		}

		return false;
	}

	private boolean checkIs(KeyedCatalog catalog, List<KeyedCatalog> registered) {
		if (catalog == null && (registered == null || registered.size() == 0))
			return true;

		if (registered == null) {
			return false;
		}

		if(registered.contains(catalog)){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(diveFilterType.getKey());
		KeyedCatalogOperator op = (KeyedCatalogOperator) getOperator();

		switch (op) {
		case EQUALS:
			sb.append(" is ");
			break;
		}

		KeyedCatalog d = getKeyedCriteria();
		sb.append(d == null ? "" : d.getKey());

		return sb.toString();
	}



}
