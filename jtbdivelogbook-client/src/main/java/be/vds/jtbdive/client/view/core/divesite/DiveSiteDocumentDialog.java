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
package be.vds.jtbdive.client.view.core.divesite;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import be.smd.i18n.swing.I18nButton;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.component.FileSelector;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.util.ExtensionFilter;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Document;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.core.utils.FileUtilities;

public class DiveSiteDocumentDialog extends JDialog {
	private static final long serialVersionUID = 7225411350637128250L;
	public static final int OPTION_CANCEL = 1;
	public static final int OPTION_SELECT = 2;
	public static final int OPTION_ERROR = 0;
	private static final Syslog LOGGER = Syslog
			.getLogger(DiveSiteDocumentDialog.class);
	private int returnValue;
	private I18nButton okButton;
	private JTextField titleTextField;
	private JTextArea commentTextArea;
	private FileSelector fileSelector;

	public DiveSiteDocumentDialog() {
		init();
	}

	public DiveSiteDocumentDialog(Frame frame) {
		super(frame);
		init();
	}

	public DiveSiteDocumentDialog(Dialog dialog) {
		super(dialog);
		init();
	}

	private void init() {
		this.setModal(true);
		this.getContentPane().add(createContentPanel());
	}

	private Component createContentPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.add(createMainPanel(), BorderLayout.CENTER);
		p.add(createButtonsPanel(), BorderLayout.SOUTH);
		return p;
	}

	private Component createMainPanel() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		int y = 0;
		GridBagLayoutManager.addComponent(p, createFileLabel(), c, 0, y, 1, 1,
				0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager
				.addComponent(p, createFileComponent(), c, 1, y, 1, 1, 1, 0,
						GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createTitleLabel(), c, 0, ++y, 1,
				1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createTitleComponent(), c, 1, y,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createCommentLabel(), c, 0, ++y,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createCommentComponent(), c, 1, y,
				1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);

		return p;
	}

	private Component createCommentLabel() {
		return new I18nLabel("comment");
	}

	private Component createCommentComponent() {
		commentTextArea = new JTextArea();

		JScrollPane scroll = new JScrollPane(commentTextArea);
		return scroll;
	}

	private Component createTitleLabel() {
		return new I18nLabel("title");
	}

	private Component createTitleComponent() {
		titleTextField = new JTextField();
		return titleTextField;
	}

	private Component createFileLabel() {
		return new I18nLabel("file");
	}

	private Component createFileComponent() {
		fileSelector = new FileSelector();
		fileSelector.setButtonText(null);
		fileSelector.setButtonIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_FOLDER_OPEN_16));
		fileSelector.setButtonBorderPainted(false);
		fileSelector.setButtonContentAreaFilled(false);
		fileSelector.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		List<DocumentFormat> df = DocumentFormat.getImportableExtensions();
		Set<String> knownExts = new HashSet<String>();

		for (DocumentFormat documentFormat : df) {
			String[] exts = documentFormat.getAuthorizedExtensions();
			String s = exts[0];

			if (!knownExts.contains(s)) {
				ExtensionFilter ef = new ExtensionFilter(s,
						documentFormat.getDescription());
				if (exts.length > 1) {
					for (int i = 1; i < exts.length; i++) {
						ef.addExtension(exts[i]);
					}
				}
				knownExts.add(s);
			}
		}
		ExtensionFilter allFilters = new ExtensionFilter(
				"All supported formats");
		// allFilters.setDetailedDescription(false);
		for (String string : knownExts) {
			allFilters.addExtension(string);
		}

		fileSelector.addFileFilter(allFilters);

		fileSelector.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						FileSelector.PROPERTY_FILE_CHANGED)) {
					controlFileValidity();
				}
			}
		});
		return fileSelector;
	}

	private void controlFileValidity() {
		boolean ok = false;
		File file = fileSelector.getSelectedFile();
		if (file != null) {
			DocumentFormat f = DocumentFormat
					.getAuthorizedDocumentFormatForFileName(file.getName());
			ok = (f != null);
		}
		okButton.setEnabled(ok);
	}

	private Component createButtonsPanel() {
		okButton = new I18nButton("ok");
		okButton.setAction(new AbstractAction() {

			private static final long serialVersionUID = -7494806799863283849L;

			@Override
			public void actionPerformed(ActionEvent e) {
				returnValue = OPTION_SELECT;
				dispose();
			}
		});
		okButton.setEnabled(false);

		I18nButton cancelButton = new I18nButton("cancel");
		cancelButton.setAction(new AbstractAction() {

			private static final long serialVersionUID = 2969396993651203455L;

			@Override
			public void actionPerformed(ActionEvent e) {
				returnValue = OPTION_CANCEL;
				dispose();
			}
		});

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.setOpaque(false);
		panel.add(okButton);
		panel.add(cancelButton);

		return panel;
	}

	public int showDialog() {
		setSize(300, 300);
		setLocationRelativeTo(null);
		setVisible(true);

		return returnValue;
	}

	public Document getSelectedDocument() {
		File file = fileSelector.getSelectedFile();
		Document doc = null;
		try {
			doc = new Document();
			doc.setTitle(titleTextField.getText());
			doc.setComment(commentTextArea.getText());
			doc.setDocumentFormat(DocumentFormat
					.getAuthorizedDocumentFormatForFileName(file.getName()));
			doc.setContent(FileUtilities.readFileContent(file));
			
		} catch (IOException e) {
			LOGGER.error(e);
		}
		return doc;
	}

}
