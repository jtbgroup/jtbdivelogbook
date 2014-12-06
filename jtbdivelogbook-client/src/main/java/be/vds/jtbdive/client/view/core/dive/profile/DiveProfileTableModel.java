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
package be.vds.jtbdive.client.view.core.dive.profile;

import javax.swing.table.AbstractTableModel;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.core.utils.ObjectSerializer;
import be.vds.jtbdive.core.utils.StringManipulator;

public class DiveProfileTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 2350308467119925467L;
	private DiveProfileEditor diveProfileEditor;

	public void setDiveProfileEditor(DiveProfileEditor diveProfileEditor) {
		this.diveProfileEditor = diveProfileEditor;
	}

	public DiveProfileTableModel(DiveProfileEditor diveProfileEditor) {
		this.diveProfileEditor = diveProfileEditor;
	}

	public void reloadDiveProfile() {
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return 7;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return (I18nResourceManager.sharedInstance().getString("time") + " (m:s)");
		case 1:
			return (I18nResourceManager.sharedInstance().getString("time") + " (sec)");
		case 2:
			return (I18nResourceManager.sharedInstance().getString("depth") + " (m)");
		case 3:
			return (I18nResourceManager.sharedInstance().getString("deco.entries"));
		case 4:
			return (I18nResourceManager.sharedInstance()
					.getString("ascent.too.fast"));
		case 5:
			return (I18nResourceManager.sharedInstance()
					.getString("deco.warning.ceiling"));
		case 6:
			return (I18nResourceManager.sharedInstance()
					.getString("remaining.bottom.time.limited"));
		default:
			return "";
		}
	}

	@Override
	public int getRowCount() {
		return diveProfileEditor == null ? 0 : diveProfileEditor.getProfileEntries()
				.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ProfileEntry entry = getProfileEntry(rowIndex);
		if (entry == null)
			return null;

		switch (columnIndex) {
		case 0:
			return StringManipulator.formatSecondsInMinutes(entry.getTime(), true);
		case 1:
			return entry.getTime();
		case 2:
			return entry.getDepth();
		case 3:
			return entry.isDecoEntry();
		case 4:
			return entry.isAscentWarning();
		case 5:
			return entry.isDecoCeilingWarning();
		case 6:
			return entry.isRemainingBottomTimeWarning();
		}
		return null;
	}

	public ProfileEntry getProfileEntry(int rowIndex) {
		if (rowIndex == -1)
			return null;

		if (diveProfileEditor == null
				|| diveProfileEditor.getProfileEntries() == null
				|| rowIndex >= diveProfileEditor.getProfileEntries().size())
			return null;

		return diveProfileEditor.getProfileEntries().get(rowIndex);
	}


	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		ProfileEntry oldPe = getProfileEntry(rowIndex);
		ProfileEntry entry = (ProfileEntry) ObjectSerializer.cloneObject(oldPe);

		switch (columnIndex) {
		case 1:
			entry.setTime((Double) aValue);
			break;
		case 2:
			entry.setDepth((Double) aValue);
			break;
		case 3:
			entry.setDecoEntry((Boolean) aValue);
			break;
		case 4:
			entry.setAscentWarning((Boolean) aValue);
			break;
		case 5:
			entry.setDecoCeilingWarning((Boolean) aValue);
			break;
		case 6:
			entry.setRemainingBottomTimeWarning((Boolean) aValue);
			break;
		}

		diveProfileEditor.replaceEntry(getProfileEntry(rowIndex), entry, this);
		fireTableDataChanged();
		diveProfileEditor.entrySelected(entry, this);
	}


	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex>0;
	}


	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 1:
		case 2:
			return Double.class;
		case 3:
		case 4:
		case 5:
		case 6:
			return Boolean.class;

		default:
			return String.class;
		}
	}

	public void addEntryAfter(ProfileEntry profileEntry) {

		ProfileEntry entry = new ProfileEntry();

		diveProfileEditor.addEntry(entry, diveProfileEditor.getProfileEntries().indexOf(profileEntry)+1, this);
		fireTableDataChanged();
		diveProfileEditor.entrySelected(entry, this);
	}

	public int getRowForEntry(ProfileEntry entry) {
		return diveProfileEditor.getProfileEntries().indexOf(entry);
	}

	public void removeEntry(ProfileEntry pe) {
		diveProfileEditor.removeEntry(pe, this);
		fireTableDataChanged();
	}

}
