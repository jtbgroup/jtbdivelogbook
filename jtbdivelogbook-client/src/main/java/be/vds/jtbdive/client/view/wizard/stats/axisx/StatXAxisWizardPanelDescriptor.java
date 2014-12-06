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

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.stats.StatQueryObject;
import be.vds.jtbdive.client.view.wizard.stats.StatAxisDescriptor;
import be.vds.jtbdive.client.view.wizard.stats.StatWizard;
import be.vds.wizard.WizardPanelDescriptor;

public abstract class StatXAxisWizardPanelDescriptor extends WizardPanelDescriptor{

	public Object getNextPanelDescriptor() {
		return StatWizard.getYAxisDescriptor((StatQueryObject) getWizard()
				.getModel().getDataMap().get(StatWizard.KEY_STAT_QUERY));
	}
	
	public Object getBackPanelDescriptor() {
		return StatAxisDescriptor.IDENTIFIER;
	}
	
	@Override
	public String getPanelDescrition() {
		return I18nResourceManager.sharedInstance().getString("axis.x");
	}
	
	@Override
	public void aboutToHidePanel() {
		StatQueryObject sq = (StatQueryObject) getWizard().getModel()
				.getDataMap().get(StatWizard.KEY_STAT_QUERY);
//		sq.setStatXAxisParams(getXAxisParams());
		fillStatQueryObjectXAxisParameters(sq);
	}
	
	public abstract void fillStatQueryObjectXAxisParameters(StatQueryObject sq);
}
