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
package be.vds.jtbdive.client.view.wizard.importation;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.conversion.ImpExBinaryHandler;
import be.vds.jtbdive.client.view.wizard.CompletionListener;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.wizard.WizardPanelDescriptor;

public class BinaryImportDescriptor extends WizardPanelDescriptor {

	public static final Syslog LOGGER = Syslog
			.getLogger(BinaryImportDescriptor.class);
	public static final String IDENTIFIER = "IMPORT_BINARY_PANEL";
	private BinaryImportPanel innerPanel;
	private MatCave matCave;

	public BinaryImportDescriptor(MatCave matCave) {
		this.matCave = matCave;
		innerPanel = new BinaryImportPanel();
		innerPanel.addCompletionListener(new CompletionListener() {
			
			@Override
			public void completionChanged(boolean isComplete) {
				setNextButtonActive();
			}
		});
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(innerPanel);
	}

	private void setNextButtonActive() {
		getWizard().setNextFinishButtonEnabled(innerPanel.isComplete());
	}

	@Override
	public void aboutToDisplayPanel() {
		ImpExBinaryHandler handler = (ImpExBinaryHandler) getWizardModel()
				.getDataMap().get(ImportWizard.IMPORT_IMPEX_HANDLER);
		innerPanel.setBinaryValues(handler.getBinaryLogBooks());
		setNextButtonActive();
	}

	public Object getNextPanelDescriptor() {
		return ImportDiveSelectionDescriptor.IDENTIFIER;
	}

	public Object getBackPanelDescriptor() {
		return ImportChoiceDescriptor.IDENTIFIER;
	}

	@Override
	public void aboutToHidePanel() {
		ImpExBinaryHandler handler = (ImpExBinaryHandler) getWizardModel()
				.getDataMap().get(ImportWizard.IMPORT_IMPEX_HANDLER);

		LogBook lb = handler.convertToLogBook(innerPanel
				.getSelectedBinaryLogBook(), matCave);
		getWizardModel().getDataMap().put(ImportWizard.IMPORT_LOGBOOK, lb);
	}

	@Override
	public String getPanelDescrition() {
		return I18nResourceManager.sharedInstance().getString(
				"binary.format");
	}

}
