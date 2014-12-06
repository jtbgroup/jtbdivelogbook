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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import be.smd.i18n.swing.I18nCheckBox;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.view.core.diver.DiverChooser;
import be.vds.jtbdive.client.view.wizard.WizardPanel;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.LogBook;

public class ImportOptionsPanel extends WizardPanel {

	private static final long serialVersionUID = 2613297905861700345L;
	private JCheckBox newLogBookCb;
	private JTextField logBookNameTf;
	private String originalLogBookName;
	private DiverManagerFacade diverManagerFacade;
	private DiverChooser ownerComponent;
	private Component ownerLabel;

	public ImportOptionsPanel(DiverManagerFacade diverManagerFacade) {
		super();
		setDiverManagerFacade(diverManagerFacade);
	}

	private void setDiverManagerFacade(DiverManagerFacade diverManagerFacade) {
		this.diverManagerFacade = diverManagerFacade;
		ownerComponent.setDiveManagerFacade(this.diverManagerFacade);
	}

	@Override
	public String getMessage() {
		return "Import Options";
	}

	@Override
	public JComponent createContentPanel() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();
		int y = 0;

		GridBagLayoutManager.addComponent(p, createNewLogbookCb(), gc, 0, y++,
				2, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, createLogBookNameTf(), gc, 0, y++,
				2, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager
				.addComponent(p, createLogBookOwnerLabel(), gc, 0, y, 1, 1, 1,
						0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createLogBookOwnerComponent(), gc,
				1, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, Box.createVerticalGlue(), gc, 0,
				y, 2, 1, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);

		return p;
	}

	private Component createLogBookOwnerLabel() {
		ownerLabel = new I18nLabel("owner");
		ownerLabel.setEnabled(false);
		return ownerLabel;
	}

	private Component createLogBookOwnerComponent() {
		ownerComponent = new DiverChooser(diverManagerFacade);
		ownerComponent.setEditable(false);
		return ownerComponent;
	}

	private JComponent createNewLogbookCb() {
		newLogBookCb = new I18nCheckBox("logbook.import.create.new");
		newLogBookCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				adaptLogBookNameTf();
			}
		});
		return newLogBookCb;
	}

	private void adaptLogBookNameTf() {
		boolean b = newLogBookCb.isSelected();
		if (b) {
			logBookNameTf.setText(originalLogBookName);
		} else {
			logBookNameTf.setText(null);
		}

		ownerLabel.setEnabled(b);
		ownerComponent.setEditable(b);
		logBookNameTf.setEnabled(b);
	}

	private Component createLogBookNameTf() {
		logBookNameTf = new JTextField();
		logBookNameTf.setEnabled(false);
		return logBookNameTf;
	}


	public void setOriginalLogBookName(String originalLogBookName) {
		this.originalLogBookName = originalLogBookName;
	}

	@Override
	public boolean isComplete() {
		return true;
	}

	public void setImportInNewLogBook(LogBook logBook) {
		newLogBookCb.setSelected(null != logBook);
		logBookNameTf.setEnabled(null != logBook);
		if (null != logBook) {
			logBookNameTf.setText(logBook.getName());
			ownerComponent.setDiver(logBook.getOwner());
		}
	}

	public boolean getImportInNewLogBook() {
		return newLogBookCb.isSelected();
	}

	public String getNewLogBookName() {
		return logBookNameTf.getText();
	}

	public void imposeNewLogBook(boolean imposeNewLogBook) {
		if (imposeNewLogBook) {
			setImportInNewLogBook(null);
			newLogBookCb.setEnabled(false);
			logBookNameTf.setEnabled(true);
			adaptLogBookNameTf();
			ownerComponent.setEditable(true);
		}
	}

	public Diver getOwner() {
		return ownerComponent.getDiver();
	}

}
