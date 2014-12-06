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
import java.awt.FlowLayout;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import be.vds.jtbdive.client.view.utils.DiverRoleImageMapper;
import be.vds.jtbdive.core.core.PalanqueeEntry;
import be.vds.jtbdive.core.core.catalogs.DiverRole;

public class PalanqueeDiverRoleTableCellRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panel.setOpaque(true);

		PalanqueeEntry pe = (PalanqueeEntry) value;
		if (null != pe) {
			Set<DiverRole> roles = pe.getRoles();
			for (DiverRole role : roles) {
				if (role != DiverRole.ROLE_PALANQUEE_SIMPLE_DIVER)
					panel.add(new JLabel(DiverRoleImageMapper.ICON_ROLES.get(role)));
			}
		}

		if (isSelected) {
			panel.setBackground(table.getSelectionBackground());
		} else {
			panel.setBackground(table.getBackground());
		}

		return panel;
	}
}
