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
package be.vds.jtbdive.client.view.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;

import be.vds.jtbdive.client.view.utils.MaterialHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.MaterialTypable;

public class MaterialListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1620331570677453671L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		JLabel l = (JLabel) super.getListCellRendererComponent(list, value,
				index, isSelected, cellHasFocus);
		MaterialTypable mt = (MaterialTypable) value;
		l.setText(getMaterialText(mt));
		l.setIcon(getIconForMaterialType(mt.getMaterialType()));
		return this;
	}

	private String getMaterialText(MaterialTypable userObject) {
		return userObject.getShortDescription();
	}

	private Icon getIconForMaterialType(MaterialType materialType) {
		if (materialType != MaterialType.OTHER)
			return MaterialHelper.getMaterialIcon(materialType, 16);
		return UIAgent.getInstance().getIcon(UIAgent.ICON_BULLET_16);
	}
}
