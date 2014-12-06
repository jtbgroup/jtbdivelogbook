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
package be.vds.jtbdive.client.view.wizard.stats.axisx;

import java.util.List;

import javax.swing.JComponent;

import be.vds.jtbdive.client.view.components.DiverSelectionPanel;
import be.vds.jtbdive.client.view.wizard.WizardPanel;
import be.vds.jtbdive.core.core.Diver;

public class StatXAxisDiversPanel extends WizardPanel {
	private static final long serialVersionUID = 3709719774991738928L;
	private DiverSelectionPanel diverSelectionPanel;


	public StatXAxisDiversPanel(List<Diver> divers) {
		super();
		init(divers);
	}

	private void init(List<Diver> divers) {
		setDivers(divers);
		diverSelectionPanel.setSelectedDivers(divers);
	}

	@Override
	public JComponent createContentPanel() {
		diverSelectionPanel = new DiverSelectionPanel(null);
		return diverSelectionPanel;
	}


	public List<Diver> getSelectedDivers() {
		return diverSelectionPanel.getSelectedDivers();
	}

	public void setDivers(List<Diver> divers) {
		diverSelectionPanel.setDivers(divers);
	}

	@Override
	public String getMessage() {
		return i18n.getString("wizard.stat.message.divers.choose");
	}
}
