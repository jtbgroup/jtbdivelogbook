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
package be.vds.jtbdive.client.view.wizard.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.config.Configuration;
import be.vds.jtbdive.client.core.config.ConfigurationType;
import be.vds.jtbdive.client.core.config.XMLConfiguration;
import be.vds.wizard.WizardPanelDescriptor;

public class ConfigChoiceDescriptor extends WizardPanelDescriptor {

	public static final String IDENTIFIER = "CONFIG_CHOICE_PANEL";
	private ConfigChoicePanel configChoicePanel;

	public ConfigChoiceDescriptor() {
		configChoicePanel = new ConfigChoicePanel();
		configChoicePanel.addRadioActionListener(createActionListener());
		configChoicePanel.initValues();
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(configChoicePanel);
	}

	@Override
	public void aboutToDisplayPanel() {
		Configuration c = (Configuration) getWizard().getModel().getDataMap()
				.get(ConfigurationWizard.DATA_CONFIGURATION);
		if (c != null) {
			configChoicePanel.setSelectedConfigurationType(c
					.getConfigurationType());
		}
		setNextButtonAccordingToCheckBox();
	}

	private ActionListener createActionListener() {
		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setNextButtonAccordingToCheckBox();
			}
		};
		return al;
	}

	private void setNextButtonAccordingToCheckBox() {
		if (configChoicePanel.getConfigActionCommand() != null)
			getWizard().setNextFinishButtonEnabled(true);
		else
			getWizard().setNextFinishButtonEnabled(false);

	}

	public Object getNextPanelDescriptor() {
		String s = configChoicePanel.getConfigActionCommand();
		return ConfigurationWizard
				.getDescriptorIdentifierForConfigTypeIdentifier(s);
	}

	public Object getBackPanelDescriptor() {
		return null;
	}

	@Override
	public void aboutToHidePanel() {
		Configuration c = null;
		String actionCommand = ((ConfigChoicePanel) getPanelComponent())
				.getConfigActionCommand();

		Configuration currentConfig = (Configuration) getWizard().getModel()
				.getDataMap().get(ConfigurationWizard.DATA_CONFIGURATION);
		if (null == currentConfig || !currentConfig.getConfigurationType().getKey()
				.equals(actionCommand)) {

			if (actionCommand
					.equals(ConfigurationType.XML_CONF.getKey())) {
				c = new XMLConfiguration();
			}

			getWizard().getModel().getDataMap()
					.put(ConfigurationWizard.DATA_CONFIGURATION, c);
		}
	}

	@Override
	public String getPanelDescrition() {
		return I18nResourceManager.sharedInstance().getString(
				"choice");
	}
}
