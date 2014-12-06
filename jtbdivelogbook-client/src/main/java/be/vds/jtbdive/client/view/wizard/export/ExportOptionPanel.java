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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.component.FileSelector;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.conversion.ImpExFormat;
import be.vds.jtbdive.client.view.components.DiveSelectionComponent;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.client.view.wizard.WizardPanel;
import be.vds.jtbdive.core.core.Dive;

public class ExportOptionPanel extends WizardPanel {

	private static final long serialVersionUID = 65719533712159514L;
	private FileSelector fileSelector;
	private DiveSelectionComponent diveSelectionComponent;
	private String extension;

	public ExportOptionPanel(List<Dive> dives) {
		super();
		loadDives(dives);
	}

	private void loadDives(List<Dive> dives) {
		diveSelectionComponent.setDives(dives);
	}

	@Override
	public String getMessage() {
		return i18n.getString("wizard.export.message.options");
	}

	@Override
	public JComponent createContentPanel() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();
		int y = 0;

		GridBagLayoutManager.addComponent(p, createFileDescription(), gc, 0,
				y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(p, createFileSelector(), gc, 0, y++,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, Box.createVerticalStrut(20), gc,
				0, y++, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, createDiveSeletorDescription(),
				gc, 0, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(p, createDiveSeletorComponent(), gc,
				0, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, Box.createVerticalGlue(), gc, 0,
				y, 1, 1, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		return p;

	}

	private Component createDiveSeletorDescription() {
		return new I18nLabel("dives.choose");
	}

	private Component createFileDescription() {
		return new I18nLabel("file.choose");
	}

	private Component createDiveSeletorComponent() {
		diveSelectionComponent = new DiveSelectionComponent(null);
		diveSelectionComponent.setPreferredSize(new Dimension(0, 20));
		return diveSelectionComponent;
	}

	private JComponent createFileSelector() {
		fileSelector = new FileSelector();
		fileSelector.setButtonText(null);
		fileSelector.setButtonIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_FOLDER_OPEN_16));
		fileSelector.setButtonBorderPainted(false);
		fileSelector.setButtonContentAreaFilled(false);
		fileSelector.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileSelector.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						FileSelector.PROPERTY_FILE_CHANGED)) {

					File f = fileSelector.getSelectedFile();
					if (f != null) {
						String path = f.getAbsolutePath();
						if (extension != null && !path.endsWith(extension)) {
							fileSelector.setSelectedFile(new File(path + "."
									+ extension));
						}
					}

					firePropertyChange("modification", null, null);
				}
			}
		});
		return fileSelector;
	}

	public void initValues() {
		// if (radios.size() > 0)
		// radios.get(0).setSelected(true);
	}

	public File getSelectedFile() {
		return fileSelector.getSelectedFile();
	}

	public List<Dive> getSelectedDives() {
		return diveSelectionComponent.getSelectedDives();
	}

	public void limitFileToFormat(ImpExFormat format) {
		String ext = format.getExtension();
		fileSelector.addFileFilter(new FileNameExtensionFilter("." + ext, ext));
	}

	public void setSelectedFile(File file) {
		fileSelector.setSelectedFile(file);
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}
