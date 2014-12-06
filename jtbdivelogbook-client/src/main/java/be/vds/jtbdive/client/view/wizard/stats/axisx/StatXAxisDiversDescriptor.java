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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.stats.DiverStatQueryObject;
import be.vds.jtbdive.client.core.stats.StatQueryObject;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.comparator.DiverComparator;

public class StatXAxisDiversDescriptor extends StatXAxisWizardPanelDescriptor {

	public static final String IDENTIFIER = "STAT_X_DIVERS_PANEL";
	private StatXAxisDiversPanel statDiversPanel;

	public StatXAxisDiversDescriptor(LogBookManagerFacade logBookManagerFacade) {
		List<Diver> divers = new ArrayList<Diver>(logBookManagerFacade
				.getCurrentLogBook().getDivers());
		Collections.sort(divers, new DiverComparator());
		statDiversPanel = new StatXAxisDiversPanel(divers);
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(statDiversPanel);
	}

	@Override
	public void fillStatQueryObjectXAxisParameters(StatQueryObject statQueryObject){
		((DiverStatQueryObject)statQueryObject).setDivers(statDiversPanel.getSelectedDivers());
	}
	
	

}
