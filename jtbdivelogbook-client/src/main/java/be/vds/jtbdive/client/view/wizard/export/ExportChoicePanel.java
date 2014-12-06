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
package be.vds.jtbdive.client.view.wizard.export;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import be.vds.jtbdive.client.core.conversion.ImpExFormat;
import be.vds.jtbdive.client.view.wizard.WizardPanel;

public class ExportChoicePanel extends WizardPanel {
	private static final long serialVersionUID = -549159795745275L;
	private ButtonGroup persistBg;
	private List<JRadioButton> radios;

	public String getMessage() {
		return i18n.getString("wizard.export.message.choice");
	}

	@Override
	public JComponent createContentPanel() {
		radios = new ArrayList<JRadioButton>();
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));

		persistBg = new ButtonGroup();

		for (ImpExFormat format : ImpExFormat.getExportFormats()) {
			JRadioButton component = createConfigComponent(format);
			p.add(component);
		}

		return p;

	}

	private JRadioButton createConfigComponent(ImpExFormat format) {
		JRadioButton radio = new JRadioButton(format.getName());
		radio.setActionCommand(format.getName());
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
		if (radios.size() > 0)
			radios.get(0).setSelected(true);
	}

	public ImpExFormat getIOFormat() {
		ImpExFormat f = null;
		for (JRadioButton r : radios) {
			if (r.isSelected()) {
				f = ImpExFormat.getFormat(r.getActionCommand());
				break;
			}
		}
		return f;
	}

}
