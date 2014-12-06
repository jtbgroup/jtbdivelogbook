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

import java.util.Collections;
import java.util.List;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.stats.StatQueryObject;
import be.vds.jtbdive.client.core.stats.YearsStatQueryObject;
import be.vds.jtbdive.client.util.StatCalculator;

public class StatXAxisYearsDescriptor extends StatXAxisWizardPanelDescriptor {

	public static final String IDENTIFIER = "STAT_X_YEARS_PANEL";
	private StatXAxisYearsPanel statYearsPanel;

	public StatXAxisYearsDescriptor(LogBookManagerFacade logBookManagerFacade) {
		List<Integer> years = StatCalculator.getDiveYears(logBookManagerFacade
				.getCurrentLogBook());
		Collections.sort(years);
		statYearsPanel = new StatXAxisYearsPanel(years);
		// statScopePanel.addRadioActionListener(createActionListener());
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(statYearsPanel);
	}

	@Override
	public void fillStatQueryObjectXAxisParameters(StatQueryObject statQueryObject){
		((YearsStatQueryObject)statQueryObject).setYears(statYearsPanel.getSelectedYears());
	}

}
