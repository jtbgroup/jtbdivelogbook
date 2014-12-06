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
package be.vds.jtbdive.client.view.wizard.config;

import java.awt.BorderLayout;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JPanel;

import be.vds.jtb.swing.component.FileSelector;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.client.view.wizard.WizardPanel;

public class ConfigXMLPanel extends WizardPanel {

	private static final long serialVersionUID = 332960955002464581L;
	private FileSelector fileSelector;

	@Override
	public JComponent createContentPanel() {
		fileSelector = new FileSelector();
		fileSelector.setShowHiddenFiles(true);
		fileSelector.setButtonText(null);
		fileSelector.setButtonIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_FOLDER_OPEN_16));
		fileSelector.setButtonBorderPainted(false);
		fileSelector.setButtonContentAreaFilled(false);
		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(false);
		p.add(fileSelector, BorderLayout.NORTH);
		return p;
	}


	public String getBasePath() {
		File f = fileSelector.getSelectedFile();
		if (f != null)
			return f.getAbsolutePath();
		return null;
	}

	public void addFileSelectionListener(
			PropertyChangeListener propertyChangeListener) {
		fileSelector.addPropertyChangeListener(propertyChangeListener);
	}

	public void setBasePath(String basePath) {
		if (basePath != null) {
			fileSelector.setSelectedFile(new File(basePath));
		}
	}

	@Override
	public String getMessage() {
		return  i18n.getString("wizard.config.message.xml.folder");
	}

}
