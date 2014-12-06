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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.conversion.ImpExFormat;
import be.vds.wizard.WizardPanelDescriptor;

public class ExportChoiceDescriptor extends WizardPanelDescriptor {

	public static final String IDENTIFIER = "EXPORT_CHOICE_PANEL";
	private ExportChoicePanel exportChoicePanel;

	public ExportChoiceDescriptor() {
		exportChoicePanel = new ExportChoicePanel();
		exportChoicePanel.addRadioActionListener(createActionListener());
		exportChoicePanel.initValues();
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(exportChoicePanel);
	}

	@Override
	public void aboutToDisplayPanel() {
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
		if (exportChoicePanel.getConfigActionCommand() != null)
			getWizard().setNextFinishButtonEnabled(true);
		else
			getWizard().setNextFinishButtonEnabled(false);

	}

	public Object getNextPanelDescriptor() {
		return ExportOptionDescriptor.IDENTIFIER;
	}

	public Object getBackPanelDescriptor() {
		return null;
	}

	@Override
	public void aboutToHidePanel() {
		ImpExFormat format = ((ExportChoicePanel) getPanelComponent()).getIOFormat();
		getWizard().getModel().getDataMap().put(ExportWizard.EXPORT_FORMAT, format);
		
//		ExportFinishDescriptor.setBackIdentifier(IDENTIFIER);
	}

	@Override
	public String getPanelDescrition() {
		return I18nResourceManager.sharedInstance().getString(
				"format");
	}
}
