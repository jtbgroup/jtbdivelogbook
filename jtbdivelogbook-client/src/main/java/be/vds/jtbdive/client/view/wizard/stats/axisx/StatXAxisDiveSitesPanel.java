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

import be.vds.jtbdive.client.view.components.DiveSiteSelectionPanel;
import be.vds.jtbdive.client.view.wizard.WizardPanel;
import be.vds.jtbdive.core.core.DiveSite;

public class StatXAxisDiveSitesPanel extends WizardPanel {
	private static final long serialVersionUID = -1352765417759643713L;
	private DiveSiteSelectionPanel diveSiteSelectionpanel;

	public StatXAxisDiveSitesPanel(List<DiveSite> diveSites) {
		init(diveSites);
	}
	
	private void init(List<DiveSite> diveSites) {
		setDiveSites(diveSites);
		diveSiteSelectionpanel.setSelectedDiveSites(diveSites);
	}

	public JComponent createContentPanel() {
		diveSiteSelectionpanel = new DiveSiteSelectionPanel(null);
		return diveSiteSelectionpanel;
	}

	public List<DiveSite> getSelectedDiveSites() {
		return diveSiteSelectionpanel.getSelectedDiveSites();
	}

	public void setDiveSites(List<DiveSite> diveSites) {
		diveSiteSelectionpanel.setSelectedDiveSites(diveSites);
	}

	@Override
	public String getMessage() {
		return i18n.getString("wizard.stat.message.divesites.choose");
	}
}
