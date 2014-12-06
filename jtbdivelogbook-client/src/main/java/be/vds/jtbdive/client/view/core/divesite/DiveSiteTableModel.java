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
package be.vds.jtbdive.client.view.core.divesite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.PinableObject;

/**
 * 
 * @author Gautier Vanderslyen
 */
class DiveSiteTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -5760026432544336619L;
	private List<DiveSite> data;

	public static final int INDEX_NAME = 0;
	public static final int INDEX_DEPTH = 1;
	public static final int INDEX_PINABLE = 2;
	private static final Map<Integer, String> HEADERS = new HashMap<Integer, String>();
	static {
		HEADERS.put(INDEX_NAME, "name");
		HEADERS.put(INDEX_DEPTH, "depth");
		HEADERS.put(INDEX_PINABLE, "coordinates");
	}

	public void setData(List<DiveSite> data) {
		this.data = data;
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return (null == data ? 0 : data.size());
	}

	@Override
	public int getColumnCount() {
		return HEADERS.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		DiveSite site = getDiveSiteAt(rowIndex);
		if (site == null)
			return null;

		switch (columnIndex) {
		case INDEX_NAME:
			return site.getName();
		case INDEX_DEPTH:
			double d = UnitsAgent.getInstance().convertLengthFromModel(
					site.getDepth());
			return d;
		case INDEX_PINABLE:
			return site.hasCoordinates() ? site : null;
		}

		return null;
	}

	public DiveSite getDiveSiteAt(int row) {
		return data.get(row);
	}

	void removeDiveSites(List<DiveSite> diveLocations) {
		for (DiveSite dl : diveLocations) {
			removeDiveSiteFromModel(dl);
		}
		fireTableDataChanged();
	}

	private void removeDiveSiteFromModel(DiveSite dl) {
		data.remove(dl);
	}

	public void addDiveSites(List<DiveSite> diveSites) {
		if (null == data) {
			data = new ArrayList<DiveSite>();
		}

		for (DiveSite diveSite : diveSites) {
			replaceOrAdd(diveSite);
		}

		fireTableDataChanged();
	}

	public void addDiveSite(DiveSite diveSite) {
		if (null == data) {
			data = new ArrayList<DiveSite>();
		}

		replaceOrAdd(diveSite);

		fireTableDataChanged();
	}

	private int replaceOrAdd(DiveSite diveSite) {
		int index = data.indexOf(diveSite);
		if (index > -1) {
			removeDiveSiteFromModel(diveSite);
			data.add(index, diveSite);
		} else {
			data.add(diveSite);
		}
		return index;
	}

	public void replaceOrAddDiveSite(DiveSite diveSite) {
		int index = replaceOrAdd(diveSite);
		if (index == -1) {
			fireTableDataChanged();
		} else {
			fireTableRowsUpdated(index, index);
		}
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case INDEX_NAME:
			return I18nResourceManager.sharedInstance().getString("name");
		case INDEX_DEPTH:
			String s = I18nResourceManager.sharedInstance().getString("depth");
			s += " (" + UnitsAgent.getInstance().getLengthUnit().getSymbol()
					+ ")";
			return s;
		case INDEX_PINABLE:
			return I18nResourceManager.sharedInstance()
					.getString("coordinates");
		}
		return "";
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case INDEX_DEPTH:
			return Double.class;
		case INDEX_PINABLE:
			return PinableObject.class;
		default:
			return String.class;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == INDEX_PINABLE;
	}

	public void removeDiveSite(DiveSite diveSite) {
		removeDiveSiteFromModel(diveSite);
		fireTableDataChanged();
	}
}
