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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import be.smd.i18n.I18nResourceManager;

public class I18nStringMapTableModel extends DefaultTableModel {

	private static final long serialVersionUID = -4235537412020157106L;
	private Map<String, Object> objects;
	private List<String> keys;

	private static final I18nResourceManager i18n = I18nResourceManager
			.sharedInstance();

	public void setData(Map<String, Object> objects) {
		this.objects = objects;
		if (objects == null) {
			this.keys = new ArrayList<String>();
		} else {
			this.keys = new ArrayList<String>(objects.keySet());
		}
		Collections.sort(this.keys);
		fireTableDataChanged();
	}

	@Override
	public Object getValueAt(int row, int column) {
		String key = getKey(row);
		if (key == null)
			return null;

		if (column == 0)
			return i18n.getString(key);
		else if (column == 1) {
			return objects.get(key).toString();
		}

		return null;
	}

	@Override
	public String getColumnName(int column) {
		if (column == 0)
			return i18n.getString("property");
		return i18n.getString("value");
	}

	private String getKey(int row) {
		return keys == null ? null : keys.get(row);
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return keys == null ? 0 : keys.size();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}
