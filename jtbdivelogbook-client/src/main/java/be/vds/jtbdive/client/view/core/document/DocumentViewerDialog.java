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
package be.vds.jtbdive.client.view.core.document;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nButton;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.DocumentContentLoader;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Document;

public class DocumentViewerDialog extends JDialog {

	private static final long serialVersionUID = 5728450857419833843L;
	public static final int OPTION_CANCEL = 1;
	public static final int OPTION_SELECT = 2;
	public static final int OPTION_ERROR = 0;
	private int returnValue;
	private DocumentViewerPanel dvp;
	private I18nButton selectButton;

	public DocumentViewerDialog() {
		init();
	}

	public DocumentViewerDialog(Frame frame) {
		super(frame);
		init();
	}

	public DocumentViewerDialog(Dialog dialog) {
		super(dialog);
		init();
	}

	private void init() {
		this.setModal(true);
		dvp = new DocumentViewerPanel();
		this.getContentPane().add(createContentPane());
	}

	private JComponent createContentPane() {
		dvp = new DocumentViewerPanel();

		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(true);
		p.setBackground(UIAgent.getInstance().getColorBaseBackground());
		p.setLayout(new BorderLayout());
		p.add(dvp, BorderLayout.CENTER);
		p.add(createButtons(), BorderLayout.SOUTH);
		return p;

	}

	private JComponent createButtons() {
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.setOpaque(false);
		selectButton = new I18nButton("ok");
		selectButton.setAction(new AbstractAction() {
			private static final long serialVersionUID = -2944575476009921924L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				returnValue = OPTION_SELECT;
			}
		});

		JButton cancelButton = new I18nButton("cancel");
		cancelButton.setAction(new AbstractAction() {
			private static final long serialVersionUID = 8976684888299128777L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				returnValue = OPTION_CANCEL;
			}
		});

		buttonsPanel.add(selectButton);
		buttonsPanel.add(cancelButton);
		return buttonsPanel;
	}

	public void setDocument(Document document, DocumentContentLoader loader) {
		dvp.setDocument(document, loader);
	}

	public int showDocument() {
		setSize(500, 500);
		WindowUtils.centerWindow(this);
		setVisible(true);
		return returnValue;
	}

	public String getDocumentTitle() {
		return dvp.getDocumentTitle();
	}

	public String getDocumentComment() {
		return dvp.getDocumentComment();
	}
}
