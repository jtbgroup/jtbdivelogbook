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

import java.util.Arrays;

import javax.swing.table.AbstractTableModel;

public abstract class TranslatableTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -2844674414633767719L;
	private String[] currentHeaders;

	public TranslatableTableModel(String[] currentHeaders) {
		this.currentHeaders = Arrays.copyOf(currentHeaders, currentHeaders.length);
	}


	public int getColumnCount() {
		return currentHeaders.length;
	}


	public String getColumnName(int columnIndex) {
		return currentHeaders[columnIndex];
	}

	public int getColumnIndex(String columnName) {
		for (int i = 0; i < currentHeaders.length; i++) {
			if (currentHeaders[i].equals(columnName)) {
				return i;
			}
		}
		return -1;
	}
}
