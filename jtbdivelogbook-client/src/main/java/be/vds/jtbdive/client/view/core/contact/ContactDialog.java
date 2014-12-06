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
package be.vds.jtbdive.client.view.core.contact;

import java.awt.BorderLayout;
import java.awt.Component;

import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.core.core.Contact;

public class ContactDialog extends PromptDialog {
	private ContactCreationPanel contactCreationPanel;

	public ContactDialog() {
		super("contact", null);
		setOkButtonEnabled(false);
	}

	@Override
	protected Component createContentPanel() {
		contactCreationPanel = new ContactCreationPanel();
		contactCreationPanel
				.addContactChangeListener(new ContactChangeListener() {

					@Override
					public void contactChange() {
							setOkButtonEnabled(contactCreationPanel
									.isContactValid());
						}
				});

		DetailPanel dp = new DetailPanel(new BorderLayout());
		dp.add(contactCreationPanel, BorderLayout.CENTER);
		return dp;
	}

	public Contact getContact() {
		return contactCreationPanel.getContact();
	}

}
