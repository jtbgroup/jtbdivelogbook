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
import java.util.Date;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import be.vds.jtbdive.client.view.table.DiveTableModel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.utils.StringManipulator;

//TODO: this is not a very beautiful class... too limited... only treats special cases.
public class DiveTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 7579834519949922538L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		setHorizontalTextPosition(SwingConstants.RIGHT);
		
		if (value == null) {
			setText(null);
		} else {
			Class c = value.getClass();
			if (c.equals(Date.class)) {
				setText(UIAgent.getInstance().getFormatDateHoursFull()
						.format((Date) value));
			} else {
				if (column == DiveTableModel.INDEX_DIVE_TIME) {
					setText(StringManipulator.formatTimeInHour(((Integer)value)*1000));
				} else {
					setText(value.toString());
				}
			}
		}
		
		return this;
	}
}
