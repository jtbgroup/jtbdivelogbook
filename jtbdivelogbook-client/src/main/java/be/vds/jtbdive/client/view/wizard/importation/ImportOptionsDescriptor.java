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
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.wizard.WizardPanelDescriptor;

public class ImportOptionsDescriptor extends WizardPanelDescriptor {

	public static final Syslog LOGGER = Syslog
			.getLogger(ImportOptionsDescriptor.class);
	public static final String IDENTIFIER = "IMPORT_OPTION_PANEL";
	private ImportOptionsPanel importOptionPanel;

	public ImportOptionsDescriptor(DiverManagerFacade diverManagerFacade) {
		importOptionPanel = new ImportOptionsPanel(diverManagerFacade);
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(importOptionPanel);
	}

	@Override
	public void aboutToDisplayPanel() {
		LogBook newLogBookB = (LogBook) getWizard().getModel().getDataMap()
				.get(ImportWizard.IMPORT_NEW_LOGBOOK);
		importOptionPanel.setImportInNewLogBook(newLogBookB);
	}

	public Object getNextPanelDescriptor() {
		return ImportMaterialMatchDescriptor.IDENTIFIER;
	}

	public Object getBackPanelDescriptor() {
		return ImportDiveSiteMatchDescriptor.IDENTIFIER;
	}

	@Override
	public void aboutToHidePanel() {
		boolean newLogBookB = importOptionPanel.getImportInNewLogBook();
		if (newLogBookB) {
			LogBook lb = new LogBook();
			lb.setName(importOptionPanel.getNewLogBookName());
			lb.setOwner(importOptionPanel.getOwner());
			getWizard().getModel().getDataMap()
					.put(ImportWizard.IMPORT_NEW_LOGBOOK, lb);
		}else{
			getWizard().getModel().getDataMap()
			.put(ImportWizard.IMPORT_NEW_LOGBOOK, null);
		}

	}

	@Override
	public String getPanelDescrition() {
		return I18nResourceManager.sharedInstance().getString(
				"options");
	}

	public void imposeNewLogBook(boolean imposeNewLogBook) {
		importOptionPanel.imposeNewLogBook(imposeNewLogBook);
	}
}
