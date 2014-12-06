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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.comparator.PalanqueeEntriesComparator;
import be.vds.jtbdive.client.view.renderer.PalanqueeDiverRoleTableCellRenderer;
import be.vds.jtbdive.core.core.Palanquee;
import be.vds.jtbdive.core.core.PalanqueeEntry;

public class PalanqueeTableModel extends DefaultTableModel {

	private static final long serialVersionUID = -6194580431085123445L;
	private List<PalanqueeEntry> entries = new ArrayList<PalanqueeEntry>();
	public static final int INDEX_DIVER = 0;
	public static final int INDEX_ROLE = 1;
	private static final Map<Integer, String> HEADERS = new HashMap<Integer, String>();
	static {
		HEADERS.put(INDEX_DIVER, "diver");
		HEADERS.put(INDEX_ROLE, "role");
	}

	public int getRowCount() {
		if (null == entries) {
			return 0;
		}
		return entries.size();
	}

	public PalanqueeEntry getPalanqueeEntryAt(int row) {
		int i = 0;
		for (PalanqueeEntry palanqueeEntry : entries) {
			if (i == row) {
				return palanqueeEntry;
			}
			i++;
		}
		return null;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (entries == null || rowIndex > entries.size()) {
			return null;
		}

		PalanqueeEntry palanqueeEntry = getPalanqueeEntryAt(rowIndex);
		switch (columnIndex) {
		case INDEX_DIVER:
			if (palanqueeEntry != null && null != palanqueeEntry.getDiver()) {
				return palanqueeEntry.getDiver().getFullName();
			}
			return "";
		case INDEX_ROLE:
			return palanqueeEntry;
		default:
			return null;
		}
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void setPalanquee(Palanquee palanquee) {
		entries.clear();
		if (palanquee != null) {
			entries.addAll(palanquee.getPalanqueeEntries());
			Collections.sort(entries, new PalanqueeEntriesComparator());
		}
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return HEADERS.size();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case INDEX_DIVER:
			return String.class;
		case INDEX_ROLE:
			return PalanqueeEntry.class;
		default:
			return String.class;
		}
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case INDEX_DIVER:
			return I18nResourceManager.sharedInstance().getString(
					HEADERS.get(INDEX_DIVER));
		case INDEX_ROLE:
			return I18nResourceManager.sharedInstance().getString(
					HEADERS.get(INDEX_ROLE));
		}
		return null;
	}

	public void setRenderer(JXTable table) {
		PalanqueeDiverRoleTableCellRenderer r = new PalanqueeDiverRoleTableCellRenderer();
		TableColumnExt tableCol = table.getColumnExt(INDEX_ROLE);
		tableCol.setCellRenderer(r);
		tableCol.setComparator(new PalanqueeEntriesComparator());
		tableCol.setPreferredWidth(20);
	}
}
