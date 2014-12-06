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

import java.util.HashMap;
import java.util.Map;

import be.vds.jtbdive.client.core.config.Configuration;
import be.vds.jtbdive.client.core.config.ConfigurationType;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.wizard.Wizard;
import be.vds.wizard.WizardPanelDescriptor;

public class ConfigurationWizard {
	private static final Syslog LOGGER = Syslog
			.getLogger(ConfigurationWizard.class);
	public static final String DATA_CONFIGURATION = "configuration";
	private static Map<String, String> configDescriptorMap = new HashMap<String, String>();

	public static Configuration createConfiguration() {
		return createConfiguration(null);
	}

	public static Configuration createConfiguration(Configuration configuration) {
		Map<Object, Object> map = createWizard(configuration);
		LOGGER.debug("Wizard ended, configuration must be returned");

		if (map == null) {
			LOGGER.info("the map is null and no configuration has been created");
			return null;
		} else {
			Configuration c = (Configuration) map.get("configuration");
			LOGGER.info("the configuration has been created");
			return c;
		}
	}

	private static Map<Object, Object> createWizard(Configuration configuration) {
		Wizard wizard = new Wizard();
		if (null != configuration) {
			fillInitialConfig(configuration, wizard);
		}

		SwingComponentHelper.displayWizard("wizard.configuration", wizard);

		WizardPanelDescriptor choiceConfigDescriptor = new ConfigChoiceDescriptor();
		wizard.registerWizardPanel(ConfigChoiceDescriptor.IDENTIFIER,
				choiceConfigDescriptor);

		WizardPanelDescriptor xmlConfigDescriptor = new ConfigXMLDescriptor();
		wizard.registerWizardPanel(ConfigXMLDescriptor.IDENTIFIER,
				xmlConfigDescriptor);
		configDescriptorMap.put(ConfigurationType.XML_CONF.getKey(),
				ConfigXMLDescriptor.IDENTIFIER);

		WizardPanelDescriptor finishDescriptor = new ConfigFinishDescriptor();
		wizard.registerWizardPanel(ConfigFinishDescriptor.IDENTIFIER,
				finishDescriptor);

		wizard.setCurrentPanel(ConfigChoiceDescriptor.IDENTIFIER);

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

	private static void fillInitialConfig(Configuration configuration, Wizard wizard) {
		Map<Object, Object> map = wizard.getModel().getDataMap();
		map.put(DATA_CONFIGURATION, configuration);		
	}

	public static Object getDescriptorIdentifierForConfigTypeIdentifier(
			String configTypeIdentifier) {
		return configDescriptorMap.get(configTypeIdentifier);
	}

}
