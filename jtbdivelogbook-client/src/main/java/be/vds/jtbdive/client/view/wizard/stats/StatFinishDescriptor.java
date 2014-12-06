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

import be.smd.i18n.I18nResourceManager;
import be.vds.wizard.WizardPanelDescriptor;

public class StatFinishDescriptor extends WizardPanelDescriptor {

	public static final String IDENTIFIER = "STAT_FINISH_PANEL";
	private static String backIdentifier;
	private StatFinishPanel panel;

	public StatFinishDescriptor() {
		panel = new StatFinishPanel();
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(panel);
	}

	public Object getNextPanelDescriptor() {
		return FINISH;
	}

	public Object getBackPanelDescriptor() {
		return StatGraphOptionsDescriptor.IDENTIFIER;
	}

	@Override
	public void aboutToDisplayPanel() {
		panel.adaptText(getWizardModel().getDataMap());
	}

	@Override
	public String getPanelDescrition() {
		return I18nResourceManager.sharedInstance().getString("finish");
	}
}
