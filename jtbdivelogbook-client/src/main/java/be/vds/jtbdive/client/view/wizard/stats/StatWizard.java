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

import java.util.Map;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.stats.StatQueryObject;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.wizard.stats.axisx.StatXAxisDefaultDescriptor;
import be.vds.jtbdive.client.view.wizard.stats.axisx.StatXAxisDiveDepthDescriptor;
import be.vds.jtbdive.client.view.wizard.stats.axisx.StatXAxisDiveSitesDescriptor;
import be.vds.jtbdive.client.view.wizard.stats.axisx.StatXAxisDiversDescriptor;
import be.vds.jtbdive.client.view.wizard.stats.axisx.StatXAxisYearsDescriptor;
import be.vds.jtbdive.client.view.wizard.stats.axisy.StatYAxisDiveDepthsDescriptor;
import be.vds.jtbdive.client.view.wizard.stats.axisy.StatYAxisDiveTemperatureDescriptor;
import be.vds.jtbdive.client.view.wizard.stats.axisy.StatYAxisDiveTimeDescriptor;
import be.vds.jtbdive.client.view.wizard.stats.axisy.StatYAxisNumberOfDiveDescriptor;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.wizard.Wizard;
import be.vds.wizard.WizardPanelDescriptor;

public class StatWizard {
	private static final Syslog LOGGER = Syslog.getLogger(StatWizard.class);
	public static final Object KEY_STAT_QUERY = "stat.query";
	private LogBookManagerFacade logBookManagerFacade;

	public StatWizard(LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
	}

	public StatQueryObject launchWizard() {
		Map<Object, Object> map = createWizard();
		LOGGER.debug("Wizard ended, StatQueryObject must be returned");

		if (map == null) {
			LOGGER.info("the map is null and no StatQuery has been created");
			return null;
		} else {
			LOGGER.info("the wizard ended succesfully");
			StatQueryObject q = (StatQueryObject) map.get("stat.query");
			return q;
		}
	}

	private Map<Object, Object> createWizard() {
		Wizard wizard = new Wizard();
		SwingComponentHelper.displayWizard("wizard.stat", wizard);

		WizardPanelDescriptor statAxisDescriptor = new StatAxisDescriptor();
		wizard.registerWizardPanel(StatAxisDescriptor.IDENTIFIER,
				statAxisDescriptor);

		// X AXIS
		WizardPanelDescriptor statYearsDescriptor = new StatXAxisYearsDescriptor(
				logBookManagerFacade);
		wizard.registerWizardPanel(StatXAxisYearsDescriptor.IDENTIFIER,
				statYearsDescriptor);

		WizardPanelDescriptor statDateDescriptor = new StatXAxisDefaultDescriptor();
		wizard.registerWizardPanel(StatXAxisDefaultDescriptor.IDENTIFIER,
				statDateDescriptor);

		WizardPanelDescriptor statDiverDescriptor = new StatXAxisDiversDescriptor(
				logBookManagerFacade);
		wizard.registerWizardPanel(StatXAxisDiversDescriptor.IDENTIFIER,
				statDiverDescriptor);

		WizardPanelDescriptor statDiveSiteDescriptor = new StatXAxisDiveSitesDescriptor(
				logBookManagerFacade);
		wizard.registerWizardPanel(StatXAxisDiveSitesDescriptor.IDENTIFIER,
				statDiveSiteDescriptor);

		WizardPanelDescriptor statXDiveDepthDescriptor = new StatXAxisDiveDepthDescriptor();
		wizard.registerWizardPanel(StatXAxisDiveDepthDescriptor.IDENTIFIER,
				statXDiveDepthDescriptor);

		// Y AXIS
		WizardPanelDescriptor statDiveTimeDescriptor = new StatYAxisDiveTimeDescriptor();
		wizard.registerWizardPanel(StatYAxisDiveTimeDescriptor.IDENTIFIER,
				statDiveTimeDescriptor);

		WizardPanelDescriptor statDiveDepthDescriptor = new StatYAxisDiveDepthsDescriptor();
		wizard.registerWizardPanel(StatYAxisDiveDepthsDescriptor.IDENTIFIER,
				statDiveDepthDescriptor);

		WizardPanelDescriptor statNumberOfDiveDepthDescriptor = new StatYAxisNumberOfDiveDescriptor();
		wizard.registerWizardPanel(StatYAxisNumberOfDiveDescriptor.IDENTIFIER,
				statNumberOfDiveDepthDescriptor);

		WizardPanelDescriptor statDiveTemperatureDescriptor = new StatYAxisDiveTemperatureDescriptor();
		wizard.registerWizardPanel(
				StatYAxisDiveTemperatureDescriptor.IDENTIFIER,
				statDiveTemperatureDescriptor);

		// Dive selection
		WizardPanelDescriptor statDiveSelectionDescriptor = new StatDiveSelectionDescriptor(
				logBookManagerFacade);
		wizard.registerWizardPanel(StatDiveSelectionDescriptor.IDENTIFIER,
				statDiveSelectionDescriptor);

		// Graph Options
		WizardPanelDescriptor statOptionsDescriptor = new StatGraphOptionsDescriptor();
		wizard.registerWizardPanel(StatGraphOptionsDescriptor.IDENTIFIER,
				statOptionsDescriptor);

		// FINISH
		WizardPanelDescriptor finishDescriptor = new StatFinishDescriptor();
		wizard.registerWizardPanel(StatFinishDescriptor.IDENTIFIER,
				finishDescriptor);

		wizard.setCurrentPanel(StatAxisDescriptor.IDENTIFIER);

		int ret = wizard.showModalDialog();

		if (ret == 0) {
			return wizard.getModel().getDataMap();
		} else if (ret == 1) {
			LOGGER.warn("Wizard canceled");
		} else if (ret == 2) {
			LOGGER.error("Wizard in error!!!");
		}

		return null;
	}

	public static String getXAxisDescriptor(StatQueryObject statQueryObject) {
		switch (statQueryObject.getStatXAxis()) {
		case YEARS:
			return StatXAxisYearsDescriptor.IDENTIFIER;
		case DIVER:
			return StatXAxisDiversDescriptor.IDENTIFIER;
		case DIVESITE:
			return StatXAxisDiveSitesDescriptor.IDENTIFIER;
		case DEPTHS:
			return StatXAxisDiveDepthDescriptor.IDENTIFIER;
		default:
			return StatXAxisDefaultDescriptor.IDENTIFIER;
		}
	}

	public static String getYAxisDescriptor(StatQueryObject statQueryObject) {
		switch (statQueryObject.getStatYAxisParams().getStatYAxis()) {
		case DIVE_TIME:
			return StatYAxisDiveTimeDescriptor.IDENTIFIER;
		case DEPTHS:
			return StatYAxisDiveDepthsDescriptor.IDENTIFIER;
		case NUMBER_OF_DIVE:
			return StatYAxisNumberOfDiveDescriptor.IDENTIFIER;
		case TEMPERATURES:
			return StatYAxisDiveTemperatureDescriptor.IDENTIFIER;
		}
		return null;
	}
}
