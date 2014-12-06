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
package be.vds.jtbdive.client.view.core.diver;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JTabbedPane;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.core.contact.ContactsPanel;
import be.vds.jtbdive.core.core.Diver;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class DiverPanel extends DetailPanel {

	private static final long serialVersionUID = -5693639254664280518L;
	private Diver currentDiver;
	private DiverGeneralPanel diverGeneralPanel;
	private ContactsPanel contactsPanel;

	public DiverPanel() {
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.add(createDetailPanel(), BorderLayout.CENTER);
		setEditable(false);


	}

	private Component createDetailPanel() {
		I18nResourceManager i18n = I18nResourceManager.sharedInstance();
		JTabbedPane diveSiteTabbedPane = new JTabbedPane();
		diveSiteTabbedPane
				.addTab(i18n.getString("general"), createGeneralTab());
		diveSiteTabbedPane.addTab(i18n.getString("contacts"),
				createContactsTab());
		return diveSiteTabbedPane;
	}

	private Component createContactsTab() {
		contactsPanel = new ContactsPanel();
		return contactsPanel;
	}

	private Component createGeneralTab() {
		diverGeneralPanel = new DiverGeneralPanel();
		return diverGeneralPanel;
	}



	public void reset() {
		diverGeneralPanel.reset();
	
	}

	public Diver getDiver() {
		Diver newDiver = new Diver();
		if (currentDiver != null) {
			newDiver.setId(currentDiver.getId());
		}
		diverGeneralPanel.fillDiverWithValues(newDiver);
		contactsPanel.fillDiverWithValues(newDiver);
		return newDiver;
	}

	public void setEditable(boolean b) {
		diverGeneralPanel.setEditable(b);
		contactsPanel.setEditable(b);
	}

	public void setDiver(Diver diver) {
		if (null == diver) {
			reset();
		} else {
			this.currentDiver = diver;
			diverGeneralPanel.setDiver(diver);
			contactsPanel.setContacts(diver.getContacts());
		}
	}
}
