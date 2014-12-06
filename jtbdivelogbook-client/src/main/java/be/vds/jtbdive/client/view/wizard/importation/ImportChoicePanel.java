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

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtb.swing.component.FileSelector;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.conversion.ImpExFormat;
import be.vds.jtbdive.client.core.conversion.ImpExUDDFHandler;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.client.view.wizard.WizardPanel;
import be.vds.jtbdive.core.utils.FileUtilities;

public class ImportChoicePanel extends WizardPanel {
	private static final long serialVersionUID = -1865521622205726436L;
	private JComboBox importFormatCb;
	private FileSelector fileSelector;
	private JTextArea descriptionTf;

	@Override
	public String getMessage() {
		return "Choose the import format.";
	}

	@Override
	public JComponent createContentPanel() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();
		int y = 0;

		GridBagLayoutManager.addComponent(p, createFileSelector(), gc, 0, y++,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, Box.createVerticalStrut(5), gc, 0,
				y++, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, createImportFormatComponent(), gc,
				0, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, Box.createVerticalStrut(20), gc,
				0, y++, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, createDescriptionComponent(), gc,
				0, y++, 1, 1, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);

		return p;
	}

	private Component createDescriptionComponent() {
		descriptionTf = new JTextArea();
		descriptionTf.setWrapStyleWord(true);
		descriptionTf.setLineWrap(true);
		descriptionTf.setFont(new Font("Arial", Font.ITALIC, 12));
		descriptionTf.setOpaque(false);
		JScrollPane scroll = new JScrollPane(descriptionTf);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		return scroll;
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
					adaptFormatChooser();
					adaptFormatDescription();
					fireCompletionChanged(isComplete());
				}
			}

		});
		return fileSelector;
	}

	private void adaptFormatDescription() {
		ImpExFormat format = (ImpExFormat) importFormatCb.getSelectedItem();
		String s = null;
		if (format != null) {
			s = I18nResourceManager.sharedInstance().getString(
					"impot.wizard.format.desription." + format.getKey());
		}
		descriptionTf.setText(s);
	}

	private void adaptFormatChooser() {
		File file = fileSelector.getSelectedFile();
		if (file != null) {
			ImpExFormat f = (ImpExFormat) importFormatCb.getSelectedItem();
			String selectedExt = "";
			if (null != f) {
				selectedExt = f.getExtension();
			}

			String fileExt = FileUtilities.getExtension(file);
			if (!selectedExt.equals(fileExt)) {
				ImpExFormat format = ImpExFormat.getFormatForExtension(fileExt);

				if (fileExt.equals("uddf")) {
					String version = ImpExUDDFHandler
							.getUDDFVersion(getSelectedFile());
					if (version == null) {
						format = ImpExFormat.getFormatForExtension(fileExt);
					} else if (version.equals(ImpExUDDFHandler.V_2_2_0)) {
						format = ImpExFormat.UDDF_V220;
					} else if (version.equals(ImpExUDDFHandler.V_3_0_0)) {
						format = ImpExFormat.UDDF_V300;
					} else if (version.equals(ImpExUDDFHandler.V_3_0_1)) {
						format = ImpExFormat.UDDF_V301;
					}
				}
				importFormatCb.setSelectedItem(format);
			}
		}
	}

	private Component createImportFormatComponent() {
		importFormatCb = new JComboBox(ImpExFormat.values());
		importFormatCb.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 4812197426775355370L;

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel l = (JLabel) super.getListCellRendererComponent(list,
						value, index, isSelected, cellHasFocus);
				if (null != value) {
					l.setText(((ImpExFormat) value).getName());
				}
				return this;
			}
		});
		importFormatCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				adaptFormatDescription();
				fireCompletionChanged(isComplete());
			}
		});
		return importFormatCb;
	}


	public ImpExFormat getImportFormat() {
		ImpExFormat f = (ImpExFormat) importFormatCb.getSelectedItem();
		return f;
	}

	public File getSelectedFile() {
		return fileSelector.getSelectedFile();
	}

	public boolean isComplete() {
		return fileSelector.getSelectedFile() != null
				&& importFormatCb.getSelectedItem() != null;
	}

}
