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

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.jdesktop.swingx.renderer.DefaultTableRenderer;

public class DiveProfileEntryCellRenderer extends DefaultTableRenderer {
	private static final long serialVersionUID = -2588425424446811462L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setText(String.valueOf((Double) value));

        if(isSelected){
          label.setBackground(table.getSelectionBackground());
        }else{
          label.setBackground(table.getBackground());
        }

		return label;
	}
}
