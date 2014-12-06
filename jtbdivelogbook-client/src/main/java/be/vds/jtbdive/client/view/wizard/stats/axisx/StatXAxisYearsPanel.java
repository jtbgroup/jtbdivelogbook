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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;

import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.view.wizard.WizardPanel;
import be.vds.jtbdive.client.view.wizard.stats.YearsPanel;

public class StatXAxisYearsPanel extends WizardPanel {
	private static final long serialVersionUID = 4950917591255846562L;
	private YearsPanel yearsPanel;

	public StatXAxisYearsPanel(List<Integer> years) {
		super();
		init(years);
	}

	@Override
	public String getMessage() {
		return i18n.getString("wizard.stat.message.years.choose");
	}

	@Override
	public JComponent createContentPanel() {
		yearsPanel = new YearsPanel();

		JPanel p = new JPanel(new GridBagLayout());
		p.setOpaque(false);
		GridBagConstraints gc = new GridBagConstraints();

		GridBagLayoutManager.addComponent(p, yearsPanel, gc, 0, 0, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, Box.createGlue(), gc, 0, 1, 1, 1,
				1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

		return p;
	}

	private void init(List<Integer> years) {
		yearsPanel.setYears(years);
	}

	public List<Integer> getSelectedYears() {
		return yearsPanel.getSelectedYears();
	}

	public void setYears(List<Integer> years) {
		yearsPanel.setYears(years);
	}
}
