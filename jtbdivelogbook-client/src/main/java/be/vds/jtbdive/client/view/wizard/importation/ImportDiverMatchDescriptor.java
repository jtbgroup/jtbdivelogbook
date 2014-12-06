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

import java.util.List;
import java.util.Map;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.wizard.WizardPanelDescriptor;

public class ImportDiverMatchDescriptor extends WizardPanelDescriptor {

	public static final String IDENTIFIER = "IMPORT_DIVER_MATCH_PANEL";
	private ImportDiverMatchPanel importDiverMatchPanel;
	private boolean firstRun = true;

	public ImportDiverMatchDescriptor(DiverManagerFacade diverManagerFacade) {
		importDiverMatchPanel = new ImportDiverMatchPanel(diverManagerFacade);
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(importDiverMatchPanel);
	}

	public Object getNextPanelDescriptor() {
		return ImportDiveSiteMatchDescriptor.IDENTIFIER;
	}

	public Object getBackPanelDescriptor() {
		return ImportDiveSelectionDescriptor.IDENTIFIER;
	}

	@Override
	public void aboutToHidePanel() {
		Map<Diver, Diver> diverMap = importDiverMatchPanel.getDiversMatching();
		getWizardModel().getDataMap().put(ImportWizard.IMPORT_DIVER_MAP,
				diverMap);
	}

	@Override
	public String getPanelDescrition() {
		return I18nResourceManager.sharedInstance().getString(
				"divers");
	}

	@Override
	public void aboutToDisplayPanel() {
		LogBook lb = (LogBook) getWizardModel().getDataMap().get(
				ImportWizard.IMPORT_LOGBOOK);
		Object divers = getWizardModel().getDataMap().get(
				ImportWizard.IMPORT_DIVER_MAP);
		if (firstRun || divers == null) {
			@SuppressWarnings("unchecked")
			List<Dive> dives = (List<Dive>) getWizardModel().getDataMap().get(
					ImportWizard.IMPORT_DIVES);
			importDiverMatchPanel.setDivers(LogBookUtilities.getDivers(dives,
					lb.getOwner()));
			firstRun = false;
		}

	}

}
