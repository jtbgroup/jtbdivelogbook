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

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import be.vds.jtbdive.client.core.config.ConfigurationType;
import be.vds.jtbdive.client.view.wizard.WizardPanel;

public class ConfigChoicePanel extends WizardPanel {
	private static final long serialVersionUID = -3160653150799125002L;
	private ButtonGroup persistBg;
	private List<JRadioButton> radios;

	@Override
	public String getMessage() {
		return i18n.getString("configuration.selection.message");
	}

	@Override
	public JComponent createContentPanel() {
		radios = new ArrayList<JRadioButton>();

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));

		persistBg = new ButtonGroup();
		ConfigurationType[] configs = ConfigurationType.values();

		for (ConfigurationType configuration : configs) {
			JRadioButton component = createConfigComponent(configuration);
			p.add(component);
		}

		return p;

	}

	private JRadioButton createConfigComponent(ConfigurationType configuration) {
		JRadioButton radio = new JRadioButton(i18n.getString(configuration.getKey()));
		radio.setActionCommand(configuration.getKey());
		persistBg.add(radio);
		radios.add(radio);
		return radio;
	}

	public String getConfigActionCommand() {
		ButtonModel selection = persistBg.getSelection();
		if (selection == null)
			return null;
		return selection.getActionCommand();
	}

	public void addRadioActionListener(ActionListener actionListener) {
		for (JRadioButton radio : radios) {
			radio.addActionListener(actionListener);
		}

	}

	public void initValues() {
		// Auto-select the first value
		if (radios.size() > 0)
			radios.get(0).setSelected(true);
	}

	public void setSelectedConfigurationType(ConfigurationType configurationType) {
		for (JRadioButton rb : radios) {
			rb.setEnabled(rb.getActionCommand().equals(
					configurationType.getKey()));
		}
	}

}
