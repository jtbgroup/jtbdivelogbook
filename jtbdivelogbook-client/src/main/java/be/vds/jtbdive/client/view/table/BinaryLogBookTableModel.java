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

import java.util.List;

import javax.swing.table.AbstractTableModel;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.core.core.divecomputer.binary.BinaryLogBook;

public class BinaryLogBookTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -6837631387949799529L;
	private List<BinaryLogBook> data;

	public void setData(List<BinaryLogBook> data) {
		this.data = data;
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return I18nResourceManager.sharedInstance().getString("date");
		case 1:
			return I18nResourceManager.sharedInstance().getString("parser");
	
		}
		return "";
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		if (data == null) {
			return 0;
		}
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		BinaryLogBook bl = getBinaryLogBookAt(row);
		if (null == bl)
			return null;

		switch (column) {
		case 0:
			return bl.getDate();
		case 1:
			return bl.getParser().getParserName();
		}
		return "";

	}

	public BinaryLogBook getBinaryLogBookAt(int row) {
		if (data == null) {
			return null;
		}
		return data.get(row);
	}

}
