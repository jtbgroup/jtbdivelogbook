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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.conversion.ImpExFormat;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.utils.FileUtilities;
import be.vds.wizard.WizardPanelDescriptor;

public class ExportOptionDescriptor extends WizardPanelDescriptor {

	public static final String IDENTIFIER = "EXPORT_OPTION_PANEL";
	private ExportOptionPanel exportChoicePanel;

	public ExportOptionDescriptor(List<Dive> dives) {
		exportChoicePanel = new ExportOptionPanel(dives);
		exportChoicePanel
				.addPropertyChangeListener(createPropertyChangeListener());
		exportChoicePanel.initValues();
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(exportChoicePanel);
	}

	@Override
	public void aboutToDisplayPanel() {
		setNextButtonAccordingToData();
		adaptFileSeletor();
	}

	private void adaptFileSeletor() {
		ImpExFormat format = (ImpExFormat) getWizard().getModel().getDataMap()
				.get(ExportWizard.EXPORT_FORMAT);
		if (null != format) {
			exportChoicePanel.limitFileToFormat(format);
			exportChoicePanel.setExtension(format.getExtension());
		}

		File file = (File) getWizard().getModel().getDataMap()
				.get(ExportWizard.EXPORT_FILE);
		if (null != file) {
			String ext = FileUtilities.getExtension(file);
			if (ext != null && !(ext.equals(format.getExtension()))) {
				String path = file.getAbsolutePath();
				path = path.substring(0, path.lastIndexOf(ext));
				File f = new File(path + format.getExtension());

				getWizard().getModel().getDataMap()
						.put(ExportWizard.EXPORT_FILE, f);
				exportChoicePanel.setSelectedFile(f);
			}
		}
	}

	private PropertyChangeListener createPropertyChangeListener() {
		PropertyChangeListener al = new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("modification")) {
					setNextButtonAccordingToData();
				}
			}
		};
		return al;
	}

	private void setNextButtonAccordingToData() {
		if (exportChoicePanel.getSelectedFile() != null)
			getWizard().setNextFinishButtonEnabled(true);
		else
			getWizard().setNextFinishButtonEnabled(false);

	}

	public Object getNextPanelDescriptor() {
		return ExportFinishDescriptor.IDENTIFIER;
	}

	public Object getBackPanelDescriptor() {
		return ExportChoiceDescriptor.IDENTIFIER;
	}

	@Override
	public void aboutToHidePanel() {
		File file = ((ExportOptionPanel) getPanelComponent()).getSelectedFile();
		getWizard().getModel().getDataMap().put(ExportWizard.EXPORT_FILE, file);

		List<Dive> dives = ((ExportOptionPanel) getPanelComponent())
				.getSelectedDives();
		getWizard().getModel().getDataMap()
				.put(ExportWizard.EXPORT_DIVES, dives);

		ExportFinishDescriptor.setBackIdentifier(IDENTIFIER);
	}

	@Override
	public String getPanelDescrition() {
		return I18nResourceManager.sharedInstance().getString(
				"options");
	}
}
