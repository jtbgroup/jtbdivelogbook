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

import java.io.File;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.GlossaryManagerFacade;
import be.vds.jtbdive.client.core.conversion.ImpExBinaryHandler;
import be.vds.jtbdive.client.core.conversion.ImpExFormat;
import be.vds.jtbdive.client.core.conversion.ImpExJTBHandler;
import be.vds.jtbdive.client.core.conversion.ImpExUDCFHandler;
import be.vds.jtbdive.client.core.conversion.ImpExUDDFHandler;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.view.wizard.CompletionListener;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.XMLValidationException;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.wizard.WizardPanelDescriptor;

public class ImportChoiceDescriptor extends WizardPanelDescriptor {

	public static final Syslog LOGGER = Syslog
			.getLogger(ImportChoiceDescriptor.class);
	public static final String IDENTIFIER = "IMPORT_CHOICE_PANEL";
	private ImportChoicePanel importChoicePanel;
	private GlossaryManagerFacade glossaryManagerFacade;

	public ImportChoiceDescriptor(GlossaryManagerFacade glossaryManagerFacade) {
		this.glossaryManagerFacade = glossaryManagerFacade;
		importChoicePanel = new ImportChoicePanel();
		importChoicePanel.addCompletionListener(new CompletionListener() {

			@Override
			public void completionChanged(boolean isComplete) {
				setNextButtonAccordingToCheckBox(isComplete);
			}
		});

		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(importChoicePanel);
	}

	@Override
	public void aboutToDisplayPanel() {
		setNextButtonAccordingToCheckBox(importChoicePanel.isComplete());
	}

	private void setNextButtonAccordingToCheckBox(boolean isComplete) {
		getWizard().setNextFinishButtonEnabled(isComplete);
	}

	public Object getNextPanelDescriptor() {
		ImpExFormat format = ((ImportChoicePanel) getPanelComponent())
				.getImportFormat();

		if (format != null && format.equals(ImpExFormat.BINARIES_FORMAT))
			return BinaryImportDescriptor.IDENTIFIER;

		return ImportDiveSelectionDescriptor.IDENTIFIER;
	}

	public Object getBackPanelDescriptor() {
		return null;
	}

	@Override
	public void aboutToHidePanel() {
		File currentFile = (File) getWizard().getModel().getDataMap()
				.get(ImportWizard.IMPORT_FILE);
		ImpExFormat currentFormat = (ImpExFormat) getWizard().getModel()
				.getDataMap().get(ImportWizard.IMPORT_FORMAT);
		ImpExFormat format = ((ImportChoicePanel) getPanelComponent())
				.getImportFormat();
		File file = ((ImportChoicePanel) getPanelComponent()).getSelectedFile();

		if (currentFile == null || currentFormat == null
				|| !currentFile.equals(file) || !currentFormat.equals(format)) {
			resetAll();
		}

		getWizard().getModel().getDataMap()
				.put(ImportWizard.IMPORT_FORMAT, format);

		getWizard().getModel().getDataMap().put(ImportWizard.IMPORT_FILE, file);

		putHandler(file);
	}

	private void resetAll() {
		getWizard().getModel().getDataMap().put(ImportWizard.IMPORT_FILE, null);
		getWizard().getModel().getDataMap()
				.put(ImportWizard.IMPORT_IMPEX_HANDLER, null);
		getWizard().getModel().getDataMap()
				.put(ImportWizard.IMPORT_DIVER_MAP, null);
		getWizard().getModel().getDataMap()
				.put(ImportWizard.IMPORT_DIVES, null);
		getWizard().getModel().getDataMap()
				.put(ImportWizard.IMPORT_DIVESITE_MAP, null);
		getWizard().getModel().getDataMap()
				.put(ImportWizard.IMPORT_FORMAT, null);
		getWizard().getModel().getDataMap()
				.put(ImportWizard.IMPORT_LOGBOOK, null);
		LOGGER.debug("wizard datamap reset");
	}

	private void putHandler(File file) {
		ImpExFormat format = (ImpExFormat) getWizard().getModel().getDataMap()
				.get(ImportWizard.IMPORT_FORMAT);

		LogBook lb = null;
		switch (format) {
		case JTB_FORMAT:
			try {
				ImpExJTBHandler parser = new ImpExJTBHandler(file);
				getWizardModel().getDataMap().put(
						ImportWizard.IMPORT_IMPEX_HANDLER, parser);
				lb = parser.read(glossaryManagerFacade.getCountriesMap());
				break;
			} catch (DataStoreException e) {
				LOGGER.error(e);
				break;
			}

		case BINARIES_FORMAT:
			ImpExBinaryHandler parser = new ImpExBinaryHandler(file);
			getWizardModel().getDataMap().put(
					ImportWizard.IMPORT_IMPEX_HANDLER, parser);
			break;

		case UDCF:
			try {
				ImpExUDCFHandler udcfEx = new ImpExUDCFHandler(file);
				getWizardModel().getDataMap().put(
						ImportWizard.IMPORT_IMPEX_HANDLER, udcfEx);
				lb = udcfEx.read();
				break;
			} catch (DataStoreException e) {
				LOGGER.error(e);
				break;
			}

		case UDDF_V220:
			try {
				ImpExUDDFHandler uddfEx = new ImpExUDDFHandler(file);
				getWizardModel().getDataMap().put(
						ImportWizard.IMPORT_IMPEX_HANDLER, uddfEx);
				lb = uddfEx.read(ImpExUDDFHandler.V_2_2_0);
				break;
			} catch (DataStoreException e) {
				LOGGER.error(e);
				break;
			} catch (XMLValidationException e) {
				LOGGER.error(e.getMessage());
				ExceptionDialog.showDialog(e, null);
				break;
			}

		case UDDF_V300:
			lb = getLogBookForUddf(file, ImpExUDDFHandler.V_3_0_0);
		case UDDF_V301:
			lb = getLogBookForUddf(file, ImpExUDDFHandler.V_3_0_1);
		}
		getWizardModel().getDataMap().put(ImportWizard.IMPORT_LOGBOOK, lb);
	}

	private LogBook getLogBookForUddf(File file, String impexHandlerVersion) {
		try {
			ImpExUDDFHandler uddfEx3 = new ImpExUDDFHandler(file);
			getWizardModel().getDataMap().put(
					ImportWizard.IMPORT_IMPEX_HANDLER, uddfEx3);
			return uddfEx3.read(impexHandlerVersion);
		} catch (DataStoreException e) {
			LOGGER.error(e);
		} catch (XMLValidationException e) {
			LOGGER.error(e.getMessage());
			ExceptionDialog.showDialog(e, null);
		}
		return null;
	}

	@Override
	public String getPanelDescrition() {
		return I18nResourceManager.sharedInstance().getString(
				"format");
	}
}
