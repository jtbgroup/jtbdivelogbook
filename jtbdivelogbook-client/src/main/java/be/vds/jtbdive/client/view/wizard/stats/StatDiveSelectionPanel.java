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
package be.vds.jtbdive.client.view.wizard.stats;

import java.util.List;

import javax.swing.JComponent;

import be.vds.jtbdive.client.view.components.DiveSelectionPanel;
import be.vds.jtbdive.client.view.wizard.WizardPanel;
import be.vds.jtbdive.core.core.Dive;

public class StatDiveSelectionPanel extends WizardPanel {

	private static final long serialVersionUID = 5627331493431516404L;
	private DiveSelectionPanel dsc;

	public StatDiveSelectionPanel(List<Dive> dives) {
		setDives(dives);
		dsc.setSelectedDives(dives);
	}

	private void setDives(List<Dive> dives) {
		dsc.setDives(dives);
	}

	@Override
	public JComponent createContentPanel() {
		dsc = new DiveSelectionPanel(null);
		return dsc;
	}

	@Override
	public String getMessage() {
		return i18n.getString("wizard.stat.messages.dives.choose");
	}

	public List<Dive> getSelectedDives() {
		return dsc.getSelectedDives();
	}

}
