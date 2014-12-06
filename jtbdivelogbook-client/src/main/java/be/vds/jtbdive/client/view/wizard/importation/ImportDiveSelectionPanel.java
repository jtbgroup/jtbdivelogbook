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
package be.vds.jtbdive.client.view.wizard.importation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.components.DiveSelectionPanel;
import be.vds.jtbdive.client.view.wizard.WizardPanel;
import be.vds.jtbdive.core.core.Dive;

public class ImportDiveSelectionPanel extends WizardPanel {

	private static final long serialVersionUID = -3251508424210932800L;
	private DiveSelectionPanel diveSelectionComponent;

	@Override
	public String getMessage() {
		return "Select Dives.";
	}

	@Override
	public JComponent createContentPanel() {

		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();
		GridBagLayoutManager.addComponent(p, createDiveSelectionPanel(), gc, 0,
				0, 1, 1, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);

		return p;

	}

	private Component createDiveSelectionPanel() {
		DetailPanel p = new DetailPanel(new BorderLayout());
		diveSelectionComponent = new DiveSelectionPanel(null);
		p.add(diveSelectionComponent, BorderLayout.CENTER);
		return p;
	}

	@Override
	public boolean isComplete() {
		return true;
	}

	public void setDives(List<Dive> dives) {
		diveSelectionComponent.setDives(dives);
	}
	
	public void setSelectedDives(List<Dive> dives) {
		diveSelectionComponent.setSelectedDives(dives);
	}

	public List<Dive>  getSelectedDives() {
		return diveSelectionComponent.getSelectedDives();
	}

	public List<Dive>  getOriginalDives() {
		return diveSelectionComponent.getOriginalDives();
	}

}
