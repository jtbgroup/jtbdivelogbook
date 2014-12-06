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
package be.vds.jtbdive.client.view.core.diver;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Diver;

/**
 * 
 * @author Gautier Vanderslyen
 */
class DiverTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -949272799709169168L;
	private List<Diver> data;
	public static final int INDEX_FIRSTNAME = 0;
	public static final int INDEX_LASTNAME = 1;
	public static final int INDEX_BIRTHDATE = 2;
	public static final int INDEX_AGE = 3;
	private static final Map<Integer, String> HEADERS = new HashMap<Integer, String>();
	static {
		HEADERS.put(INDEX_FIRSTNAME, "firstname");
		HEADERS.put(INDEX_LASTNAME, "lastname");
		HEADERS.put(INDEX_BIRTHDATE, "birthdate");
		HEADERS.put(INDEX_AGE, "age");
	}

	public void setData(List<Diver> data) {
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

	public Diver getDiverAt(int row) {
		if (data == null) {
			return null;
		}
		return data.get(row);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Diver diver = getDiverAt(rowIndex);
		if (null == diver)
			return null;

		switch (columnIndex) {
		case INDEX_FIRSTNAME:
			return diver.getFirstName();
		case INDEX_LASTNAME:
			return diver.getLastName();
		case INDEX_BIRTHDATE:
			return diver.getBirthDate();
		case INDEX_AGE:
			int age = LogBookUtilities.getDiversAge(diver);
			return age == -1?null:age;
		default:
			return diver;
		}
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case INDEX_FIRSTNAME:
			return I18nResourceManager.sharedInstance().getString(
					HEADERS.get(INDEX_FIRSTNAME));
		case INDEX_LASTNAME:
			return I18nResourceManager.sharedInstance().getString(
					HEADERS.get(INDEX_LASTNAME));
		case INDEX_BIRTHDATE:
			return I18nResourceManager.sharedInstance().getString(
					HEADERS.get(INDEX_BIRTHDATE));
		case INDEX_AGE:
			return I18nResourceManager.sharedInstance().getString(
					HEADERS.get(INDEX_AGE));
		default:
			return "";
		}
	}

	void removeDivers(List<Diver> divers) {
		for (Diver dl : divers) {
			data.remove(dl);
		}
		fireTableDataChanged();
	}

	public void removeDiver(Diver diver) {
		int index = data.indexOf(diver);
		if (index > -1) {
			data.remove(diver);
			fireTableRowsDeleted(index, index);
		}
	}

	public void addDiver(List<Diver> divers) {
		if (null == data) {
			data = divers;
		} else {
			for (Diver diver : divers) {
				replaceOrAdd(diver);
			}
		}
		fireTableDataChanged();
	}

	private int replaceOrAdd(Diver diver) {
		if (data == null)
			data = new ArrayList<Diver>();
		int index = data.indexOf(diver);
		if (index > -1) {
			data.remove(diver);
			data.add(index, diver);
		} else {
			data.add(diver);
		}
		return index;
	}

	public void replaceOrAddDiver(Diver diver) {
		int index = replaceOrAdd(diver);
		if (index == -1) {
			fireTableDataChanged();
		} else {
			fireTableRowsUpdated(index, index);
		}
	}

	public void setRenderer(JXTable table) {
		TableColumnExt tableCol = table.getColumnExt(INDEX_BIRTHDATE);
		tableCol.setCellRenderer(new DefaultTableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel l = (JLabel) super.getTableCellRendererComponent(table,
						value, isSelected, hasFocus, row, column);
				if (null == value) {
					l.setText(null);
				} else {
					l.setText(UIAgent.getInstance().getFormatDateShort().format((Date)value));
				}
				return l;
			}
		});
	
	}

}
