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

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.view.utils.MaterialHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.Equipment;

public class EquipmentTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = -7633163486465474351L;
	private static final I18nResourceManager i18n = I18nResourceManager
			.sharedInstance();
	private int size = 24;

	public EquipmentTreeCellRenderer() {
		if (size == 16) {
			setClosedIcon(UIAgent.getInstance().getIcon(
					UIAgent.ICON_FOLDER_CLOSED_16));
			setOpenIcon(UIAgent.getInstance().getIcon(
					UIAgent.ICON_FOLDER_OPEN_16));
		} else {
			setClosedIcon(UIAgent.getInstance().getIcon(
					UIAgent.ICON_FOLDER_CLOSED_24));
			setOpenIcon(UIAgent.getInstance().getIcon(
					UIAgent.ICON_FOLDER_OPEN_24));
		}

	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		 JLabel l = (JLabel) super.getTreeCellRendererComponent(tree, value,
		 selected, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		if (node.getUserObject() instanceof MaterialType) {
			MaterialType mattype = (MaterialType) node.getUserObject();
			setIconForMaterialTypeNode(mattype, l);
			l.setText(i18n.getString(mattype.getKey()));
		} else if (node.getUserObject() instanceof Equipment) {
			l.setText(getEquipmentText((Equipment) node.getUserObject()));
			l.setIcon(UIAgent.getInstance().getIcon(
					UIAgent.ICON_BULLET_16));
		}

		return l;
	}


	private String getEquipmentText(Equipment equipment) {
		return equipment.getShortDescription();
	}

	private void setIconForMaterialTypeNode(MaterialType materialType,  JLabel l) {
		if (materialType != MaterialType.OTHER)
			l.setIcon(MaterialHelper.getMaterialIcon(materialType, size));
	}

	public int getIconSize() {
		return size;
	}
}
