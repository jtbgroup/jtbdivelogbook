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
package be.vds.jtbdive.client.view.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtb.swing.component.FileSelector;
import be.vds.jtb.swing.layout.GridBagLayoutManager;

public class ExportLogBookDialog extends JDialog {

	private static final long serialVersionUID = -1466486055013832529L;
	public static int OPTION_ERROR = 0;
	public static int OPTION_OK = 1;
	public static int OPTION_CANCEL = 2;
	private int option;
	private FileSelector fs;

	public ExportLogBookDialog() {
		init();
	}

	public ExportLogBookDialog(JFrame frame) {
		super(frame, I18nResourceManager.sharedInstance().getString("export"), true);
		init();
	}

	private void init() {
		this.getContentPane().add(createContentPane());
		this.setSize(new Dimension(200, 120));
		this.setResizable(false);
	}

	private Component createContentPane() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createMainPanel(), BorderLayout.CENTER);
		panel.add(createButtonsPanel(), BorderLayout.SOUTH);
		return panel;
	}

	private Component createMainPanel() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		GridBagLayoutManager.addComponent(p, createFileLabel(), c, 0, 0, 1, 1,
				1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(p, createFileComponent(), c, 0, 1, 1,
				1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		return p;

	}

	private Component createFileComponent() {
		fs = new FileSelector();
		fs.addFileFilter(new FileNameExtensionFilter(".xml", "xml"));
		return fs;
	}

	private Component createFileLabel() {
		JTextArea t = new JTextArea("Choose a File...");
		t.setEditable(false);
		t.setEnabled(false);
		t.setBorder(null);
		t.setOpaque(false);
		return t;
	}

	private Component createButtonsPanel() {
		I18nButton cancelButton = new I18nButton(new AbstractAction("cancel") {
			private static final long serialVersionUID = 8601084693288617216L;

			@Override
			public void actionPerformed(ActionEvent e) {
				option = OPTION_CANCEL;
				dispose();
			}
		});

		I18nButton okButton = new I18nButton(new AbstractAction("ok") {
			private static final long serialVersionUID = -3347091692073001035L;

			@Override
			public void actionPerformed(ActionEvent e) {
				option = OPTION_OK;
				dispose();
			}
		});

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(okButton);
		panel.add(cancelButton);
		return panel;
	}

	public int showDialog() {
		this.setVisible(true);
		return option;
	}

	public File getFile() {
		return fs.getSelectedFile();
	}

}
