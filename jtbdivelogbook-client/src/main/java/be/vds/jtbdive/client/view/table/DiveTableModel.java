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
package be.vds.jtbdive.client.view.table;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.JXTable;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.view.renderer.DiveTableCellRenderer;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveSite;

public class DiveTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 2374011489641466559L;
	private List<Dive> dives;
	public static final int INDEX_NUMBER = 0;
	public static final int INDEX_DATE = 1;
	public static final int INDEX_DEPTH = 2;
	public static final int INDEX_DIVE_TIME = 3;
	public static final int INDEX_WATER_TEMPERATURE = 4;
	public static final int INDEX_DIVE_SITE = 5;
	private static final Map<Integer, String> HEADERS = new HashMap<Integer, String>();
	static {
		HEADERS.put(INDEX_NUMBER, "number");
		HEADERS.put(INDEX_DATE, "date");
		HEADERS.put(INDEX_DEPTH, "depth");
		HEADERS.put(INDEX_DIVE_TIME, "dive.time");
		HEADERS.put(INDEX_WATER_TEMPERATURE, "temperature.water");
		HEADERS.put(INDEX_DIVE_SITE, "divesite");
	}

	public void setData(List<Dive> dives) {
		this.dives = dives;
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case INDEX_NUMBER:
			return I18nResourceManager.sharedInstance().getString(
					HEADERS.get(INDEX_NUMBER));
		case INDEX_DATE:
			return I18nResourceManager.sharedInstance().getString(
					HEADERS.get(INDEX_DATE));
		case INDEX_DEPTH:
			return I18nResourceManager.sharedInstance().getString(
					HEADERS.get(INDEX_DEPTH))
					+ " ("
					+ UnitsAgent.getInstance().getLengthUnit().getSymbol()
					+ ")";
		case INDEX_DIVE_TIME:
			return I18nResourceManager.sharedInstance().getString(
					HEADERS.get(INDEX_DIVE_TIME));
		case INDEX_WATER_TEMPERATURE:
			return I18nResourceManager.sharedInstance().getString(
					HEADERS.get(INDEX_WATER_TEMPERATURE))
					+ " ("
					+ UnitsAgent.getInstance().getTemperatureUnit().getSymbol()
					+ ")";
		case INDEX_DIVE_SITE:
			return I18nResourceManager.sharedInstance().getString(
					HEADERS.get(INDEX_DIVE_SITE));
		}
		return "";
	}

	@Override
	public int getColumnCount() {
		return HEADERS.size();
	}

	@Override
	public int getRowCount() {
		if (dives == null || dives.size() == 0) {
			return 0;
		}
		return dives.size();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case INDEX_DATE:
			return Date.class;
		case INDEX_WATER_TEMPERATURE:
		case INDEX_DEPTH:
			return Double.class;
		case INDEX_NUMBER:
		case INDEX_DIVE_TIME:
			return Integer.class;
		default:
			return String.class;
		}
	}

	@Override
	public Object getValueAt(int row, int column) {
		Dive dive = getDiveAt(row);
		if (null == dive)
			return null;

		switch (column) {
		case INDEX_NUMBER:
			return dive.getNumber();
		case INDEX_DATE:
			return dive.getDate();
		case INDEX_DEPTH:
			return UnitsAgent.getInstance().convertLengthFromModel(
					dive.getMaxDepth());
		case INDEX_DIVE_TIME:
			return dive.getDiveTime();
		case INDEX_WATER_TEMPERATURE:
			return UnitsAgent.getInstance().convertTemperatureFromModel(
					dive.getWaterTemperature());
		case INDEX_DIVE_SITE:
			DiveSite ds = dive.getDiveSite();
			return ds == null ? "" : ds.getName();
		}
		return "";
	}

	public Dive getDiveAt(int row) {
		if (dives == null) {
			return null;
		}
		return dives.get(row);
	}

	public void setRenderer(JXTable table) {
		DiveTableCellRenderer r = new DiveTableCellRenderer();
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(i).setCellRenderer(r);
		}
	}
}
