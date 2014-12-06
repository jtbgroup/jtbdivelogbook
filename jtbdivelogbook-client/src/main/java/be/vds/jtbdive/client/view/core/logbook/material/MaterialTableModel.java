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
package be.vds.jtbdive.client.view.core.logbook.material;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.view.utils.MaterialHelper;
import be.vds.jtbdive.core.core.material.Material;

public class MaterialTableModel extends DefaultTableModel {

	private static final long serialVersionUID = -2271602785218800307L;
	private List<Material> data;

	public void setData(List<Material> data) {
		this.data = data;
		fireTableDataChanged();
	}

	@Override
	public Object getValueAt(int row, int column) {
		Material mat = getMaterialAt(row);
		if (null == mat) {
			return null;
		}

		switch (column) {
		case 0:
			return MaterialHelper.getMaterialIcon(mat.getMaterialType(), 16);
		case 1:
			return mat.getManufacturer();
		case 2:
			return mat.getModel();
		case 3:
			return mat.getPurchaseDate();
		}
		return null;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return I18nResourceManager.sharedInstance().getString("type");
		case 1:
			return I18nResourceManager.sharedInstance().getString(
					"manufacturer");
		case 2:
			return I18nResourceManager.sharedInstance().getString("model");
		case 3:
			return I18nResourceManager.sharedInstance().getString(
					"purchase.date");
		}
		return null;
	}

	@Override
	public int getRowCount() {
		return data == null ? 0 : data.size();
	}

	private Material getMaterialAt(int row) {
		return data == null ? null : data.get(row);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}
