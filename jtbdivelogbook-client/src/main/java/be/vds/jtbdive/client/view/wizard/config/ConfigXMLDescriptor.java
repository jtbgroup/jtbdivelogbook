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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtb.swing.component.FileSelector;
import be.vds.jtbdive.client.core.config.Configuration;
import be.vds.jtbdive.client.core.config.XMLConfiguration;
import be.vds.wizard.WizardPanelDescriptor;

public class ConfigXMLDescriptor extends WizardPanelDescriptor implements
		PropertyChangeListener {

	public static final String IDENTIFIER = "CONFIG_XML_PANEL";
	private ConfigXMLPanel configXmlPanel;

	public ConfigXMLDescriptor() {
		configXmlPanel = new ConfigXMLPanel();
		configXmlPanel.addFileSelectionListener(this);
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(configXmlPanel);
	}

	public Object getNextPanelDescriptor() {
		return ConfigFinishDescriptor.IDENTIFIER;
	}

	@Override
	public void aboutToDisplayPanel() {
		Configuration c = (Configuration) getWizard().getModel().getDataMap()
				.get(ConfigurationWizard.DATA_CONFIGURATION);
		if (c != null && c instanceof XMLConfiguration) {
			configXmlPanel.setBasePath(((XMLConfiguration) c).getBasePath());
		}
		setNextButton();
	}

	private void setNextButton() {
		boolean b = configXmlPanel.getBasePath() != null;

		if (b != getWizard().getNextFinishButtonEnabled())
			getWizard().setNextFinishButtonEnabled(b);
	}

	// public Object getNextPanelDescriptor() {
	// String s = configXmlPanel.getConfigActionCommand();
	// return ConfigurationWizard.getDescriptorForIdentifier(s);
	// }

	public Object getBackPanelDescriptor() {
		return ConfigChoiceDescriptor.IDENTIFIER;
	}

	@Override
	public void aboutToHidePanel() {
		XMLConfiguration c = (XMLConfiguration) getWizard().getModel()
				.getDataMap().get(ConfigurationWizard.DATA_CONFIGURATION);
		c.setBasePath(configXmlPanel.getBasePath());

		ConfigFinishDescriptor.setBackIdentifier(IDENTIFIER);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(FileSelector.PROPERTY_FILE_CHANGED)) {
			setNextButton();
		}
	}

	@Override
	public String getPanelDescrition() {
		return I18nResourceManager.sharedInstance().getString(
				"xml");
	}
}
