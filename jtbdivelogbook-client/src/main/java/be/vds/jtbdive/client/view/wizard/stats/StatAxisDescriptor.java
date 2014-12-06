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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.stats.StatQueryObject;
import be.vds.wizard.WizardPanelDescriptor;

public class StatAxisDescriptor extends WizardPanelDescriptor {

	public static final String IDENTIFIER = "STAT_AXIS_PANEL";
	private StatAxisPanel statAxisPanel;

	public StatAxisDescriptor() {
		statAxisPanel = new StatAxisPanel();
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(statAxisPanel);
		statAxisPanel.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("stat.query")) {
					getWizard()
							.setNextFinishButtonEnabled(
									statAxisPanel.getStatQueryObject() == null ? Boolean.FALSE
											: Boolean.TRUE);
				}
			}
		});
	}



	public Object getNextPanelDescriptor() {
		StatQueryObject sqo = ((StatAxisPanel) getPanelComponent())
				.getStatQueryObject();
		if (sqo != null) {
			return StatWizard.getXAxisDescriptor(sqo);
		}
		return null;
	}

	public Object getBackPanelDescriptor() {
		return null;
	}

	@Override
	public void aboutToHidePanel() {
		StatQueryObject sqo = ((StatAxisPanel) getPanelComponent())
				.getStatQueryObject();
		getWizard().getModel().getDataMap().put("stat.query", sqo);
	}

	@Override
	public String getPanelDescrition() {
		return I18nResourceManager.sharedInstance().getString(
				"axis.selection");
	}
}
