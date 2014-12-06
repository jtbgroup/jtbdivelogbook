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

import java.util.Map;

import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.GlossaryManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.wizard.Wizard;
import be.vds.wizard.WizardPanelDescriptor;

public class ImportWizard {

	private static final Syslog LOGGER = Syslog.getLogger(ImportWizard.class);
	public static final String IMPORT_FORMAT = "import.format";
	public static final String IMPORT_FILE = "import.file";
	public static final String IMPORT_DIVES = "import.dives";
	public static final String IMPORT_LOGBOOK = "import.logbook";
	public static final String IMPORT_DIVER_MAP = "import.diver.map";
	public static final String IMPORT_DIVESITE_MAP = "import.divesite.map";
	public static final Object IMPORT_MATERIAL_MAP = "import.material.map";
	public static final Object IMPORT_IMPEX_HANDLER = "import.parser";
	public static final Object IMPORT_NEW_LOGBOOK = "import.logbook.new";

	private LogBookManagerFacade logBookManagerFacade;
	private DiveSiteManagerFacade diveSiteManagerFacade;
	private DiverManagerFacade diverManagerFacade;
	private boolean imposeNewLogBook;
	private static Wizard wizard;
	private GlossaryManagerFacade glossaryManagerFacade;

	public ImportWizard(
			DiverManagerFacade diverManagerFacade,
			DiveSiteManagerFacade diveSiteManagerFacade,

			LogBookManagerFacade logBookManagerFacade,
			GlossaryManagerFacade glossaryManagerFacade) {
		this.diveSiteManagerFacade = diveSiteManagerFacade;
		this.logBookManagerFacade = logBookManagerFacade;
		this.diverManagerFacade = diverManagerFacade;
		this.glossaryManagerFacade = glossaryManagerFacade;
	}

	public void imposeNewLogBook(boolean imposeNewLogBook) {
		this.imposeNewLogBook = imposeNewLogBook;
	}

	public Map<Object, Object> doImport(boolean imposeNewLogBook) {
		imposeNewLogBook(imposeNewLogBook);
		return doImport();
	}

	public Map<Object, Object> doImport() {
		Map<Object, Object> map = createWizard();
		LOGGER.debug("Wizard teminated...");
		return map;
	}

	private Map<Object, Object> createWizard() {
		wizard = new Wizard();
		wizard.setWizardResizable(true);
		wizard.setWizardSize(500, 400);
		wizard.setWizardCentered(true);
		wizard.setTitle("Import Wizard");
		wizard.setDefaultIcons();

		// Choice
		ImportChoiceDescriptor importChoiceDescriptor = new ImportChoiceDescriptor(
				glossaryManagerFacade);
		wizard.registerWizardPanel(ImportChoiceDescriptor.IDENTIFIER,
				importChoiceDescriptor);

		// Dives
		WizardPanelDescriptor importDiveSelectionDescriptor = new ImportDiveSelectionDescriptor();
		wizard.registerWizardPanel(ImportDiveSelectionDescriptor.IDENTIFIER,
				importDiveSelectionDescriptor);

		// Divers
		WizardPanelDescriptor importDiverMatchDescriptor = new ImportDiverMatchDescriptor(
				diverManagerFacade);
		wizard.registerWizardPanel(ImportDiverMatchDescriptor.IDENTIFIER,
				importDiverMatchDescriptor);

		// DiveSites
		WizardPanelDescriptor importDiveSiteMatchDescriptor = new ImportDiveSiteMatchDescriptor(
				diveSiteManagerFacade);
		wizard.registerWizardPanel(ImportDiveSiteMatchDescriptor.IDENTIFIER,
				importDiveSiteMatchDescriptor);

		// Material
		WizardPanelDescriptor importMaterialMatchDescriptor = new ImportMaterialMatchDescriptor(
				logBookManagerFacade);
		wizard.registerWizardPanel(ImportMaterialMatchDescriptor.IDENTIFIER,
				importMaterialMatchDescriptor);

		// Options
		ImportOptionsDescriptor importOptionsDescriptor = new ImportOptionsDescriptor(
				diverManagerFacade);
		wizard.registerWizardPanel(ImportOptionsDescriptor.IDENTIFIER,
				importOptionsDescriptor);

		// Finish
		WizardPanelDescriptor finishDescriptor = new ImportFinishDescriptor();
		wizard.registerWizardPanel(ImportFinishDescriptor.IDENTIFIER,
				finishDescriptor);

		// Binary Import
		WizardPanelDescriptor binaryImportDescriptor = new BinaryImportDescriptor(
				logBookManagerFacade.getCurrentMatCave());
		wizard.registerWizardPanel(BinaryImportDescriptor.IDENTIFIER,
				binaryImportDescriptor);

		if (imposeNewLogBook) {
			wizard.getModel()
					.getDataMap()
					.put(ImportWizard.IMPORT_NEW_LOGBOOK,
							new LogBook(-1, "New LogBook"));
			importOptionsDescriptor.imposeNewLogBook(imposeNewLogBook);
		}

		wizard.setCurrentPanel(ImportChoiceDescriptor.IDENTIFIER);

		int ret = wizard.showModalDialog();

		if (ret == Wizard.FINISH_RETURN_CODE) {
			return wizard.getModel().getDataMap();
		} else if (ret == Wizard.CANCEL_RETURN_CODE) {
			LOGGER.warn("Wizard canceled");
		} else if (ret == Wizard.ERROR_RETURN_CODE) {
			LOGGER.error("Wizard in error!!!");
		}

		return null;
	}

}
