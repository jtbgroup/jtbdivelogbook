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
import javax.swing.JLabel;
import javax.swing.JList;

import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.utils.StringManipulator;

public class DiveListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = -5296810300112124109L;

	@Override
	public Component getListCellRendererComponent(JList jlist, Object o, int i,
			boolean bln, boolean bln1) {
		JLabel cComponent = (JLabel) super.getListCellRendererComponent(jlist,
				o, i, bln, bln1);

		Dive dive = (Dive) o;

		StringBuilder sb = new StringBuilder();
		sb.append(StringManipulator.formatNumberWithFixedSize(dive.getNumber(), 4));
		sb.append(dive.getDate() == null ? "" : " - "
				+UIAgent.getInstance().getFormatDateHoursFull().format(dive
						.getDate()));
		cComponent.setText(sb.toString());
		cComponent.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_DIVE_DOCUMENT_16));

		return cComponent;
	}

}
