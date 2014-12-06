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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.client.core.conversion.ImpExFormat;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.comparator.DiveNumberComparator;
import be.vds.wizard.WizardPanelDescriptor;

public class ImportDiveSelectionDescriptor extends WizardPanelDescriptor {

	public static final String IDENTIFIER = "IMPORT_DIVE_SELECTION_PANEL";
	private ImportDiveSelectionPanel importDiveSelectionPanel;
	private boolean firstRun = true;

	public ImportDiveSelectionDescriptor() {
		importDiveSelectionPanel = new ImportDiveSelectionPanel();
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(importDiveSelectionPanel);
	}

	public Object getNextPanelDescriptor() {
		return ImportDiverMatchDescriptor.IDENTIFIER;
	}

	public Object getBackPanelDescriptor() {
		ImpExFormat format = (ImpExFormat) getWizard().getModel().getDataMap()
				.get(ImportWizard.IMPORT_FORMAT);

		if (format.equals(ImpExFormat.BINARIES_FORMAT))
			return BinaryImportDescriptor.IDENTIFIER;

		return ImportChoiceDescriptor.IDENTIFIER;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void aboutToHidePanel() {
		List<Dive> importDives = (List<Dive>) getWizardModel().getDataMap()
				.get(ImportWizard.IMPORT_DIVES);

		List<Dive> selectedDives = importDiveSelectionPanel.getSelectedDives();
		getWizardModel().getDataMap().put(ImportWizard.IMPORT_DIVES,
				selectedDives);

		if (!LogBookUtilities.areDiveListEquals(importDives, selectedDives)) {
			getWizardModel().getDataMap().put(ImportWizard.IMPORT_DIVESITE_MAP,
					null);
			getWizardModel().getDataMap().put(ImportWizard.IMPORT_DIVER_MAP,
					null);
		}

	}

	@Override
	public String getPanelDescrition() {
		return I18nResourceManager.sharedInstance().getString(
				"dives");
	}

	@Override
	@SuppressWarnings("unchecked")
	public void aboutToDisplayPanel() {
		List<Dive> dives = (List<Dive>) getWizardModel().getDataMap().get(
				ImportWizard.IMPORT_DIVES);

		if (firstRun || dives == null) {
			LogBook lb = (LogBook) getWizardModel().getDataMap().get(
					ImportWizard.IMPORT_LOGBOOK);
			if (lb != null) {
				List<Dive> logBookDives = new ArrayList<Dive>(lb.getDives());
				Collections.sort(logBookDives, new DiveNumberComparator());
				importDiveSelectionPanel.setDives(logBookDives);
			} else {
				importDiveSelectionPanel.setDives(null);
			}
			firstRun = false;
		}
	}

}
