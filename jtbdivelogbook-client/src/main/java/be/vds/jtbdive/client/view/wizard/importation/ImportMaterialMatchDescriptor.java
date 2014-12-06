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
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.wizard.WizardPanelDescriptor;

public class ImportMaterialMatchDescriptor extends WizardPanelDescriptor {

	public static final String IDENTIFIER = "IMPORT_MATERIAL_MATCH_PANEL";
	private ImportMaterialMatchPanel importMaterialMatchPanel;
	private boolean firstRun = true;
	private LogBookManagerFacade logbookManagerFacade;

	public ImportMaterialMatchDescriptor(
			LogBookManagerFacade logbookManagerFacade) {
		this.logbookManagerFacade = logbookManagerFacade;
		importMaterialMatchPanel = new ImportMaterialMatchPanel(
				logbookManagerFacade);
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(importMaterialMatchPanel);
	}

	public Object getNextPanelDescriptor() {
		return ImportFinishDescriptor.IDENTIFIER;
	}

	public Object getBackPanelDescriptor() {
		return ImportOptionsDescriptor.IDENTIFIER;
	}

	@Override
	public void aboutToHidePanel() {
		ImportFinishDescriptor.setBackIdentifier(IDENTIFIER);

		Map<Material, Material> matchMap = importMaterialMatchPanel
				.getMaterialMatching();
		getWizardModel().getDataMap().put(ImportWizard.IMPORT_MATERIAL_MAP,
				matchMap);
	}

	@Override
	public String getPanelDescrition() {
		return I18nResourceManager.sharedInstance().getString(
				"material");
	}

	@Override
	public void aboutToDisplayPanel() {
		LogBook lb = (LogBook) getWizardModel().getDataMap().get(
				ImportWizard.IMPORT_NEW_LOGBOOK);
		Object materials = getWizardModel().getDataMap().get(
				ImportWizard.IMPORT_MATERIAL_MAP);

		boolean allowMatch = false;
		if (lb == null && logbookManagerFacade.getCurrentMatCave() != null
				&& logbookManagerFacade.getCurrentMatCave().size() > 0) {
			allowMatch = true;
		}

		if (allowMatch != importMaterialMatchPanel.allowMatch()) {
			importMaterialMatchPanel.setAllowMatch(allowMatch);
		}

		if (firstRun || materials == null) {
			@SuppressWarnings("unchecked")
			List<Dive> dives = (List<Dive>) getWizardModel().getDataMap().get(
					ImportWizard.IMPORT_DIVES);
			importMaterialMatchPanel.setMaterials(LogBookUtilities
					.getMaterials(dives));
			firstRun = false;
		}

	}

}
