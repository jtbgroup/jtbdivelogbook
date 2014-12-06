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
package be.vds.jtbdive.client.view.core.preferences;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import be.vds.jtbdive.client.view.utils.UIAgent;

public abstract class AbstractPreferrencePanel extends JPanel {

	private static final long serialVersionUID = -4341302100637415201L;

	public AbstractPreferrencePanel() {
		init(true);
	}

	public AbstractPreferrencePanel(boolean insertIntoScroll) {
		init(insertIntoScroll);
	}

	private void init(boolean insertIntoScroll) {
		this.setLayout(new BorderLayout());
		this.setOpaque(false);

		Component comp = createContentPanel();
		if (insertIntoScroll) {
			JScrollPane scroll = new JScrollPane(comp);
			scroll.getVerticalScrollBar().setUnitIncrement(
					UIAgent.VERTICAL_UNIT_SCROLL);
			this.add(scroll, BorderLayout.CENTER);
		} else {
			this.add(comp, BorderLayout.CENTER);
		}

	}

	protected abstract Component createContentPanel();

	public abstract void adaptUserPreferences();

	public abstract void setUserPreferences();
	
}
