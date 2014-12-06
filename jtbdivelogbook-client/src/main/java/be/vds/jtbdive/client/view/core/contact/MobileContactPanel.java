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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JTextField;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.core.core.Contact;
import be.vds.jtbdive.core.core.catalogs.ContactType;

public class MobileContactPanel extends ContactPanel {

	private JTextField mobileTf;

	public MobileContactPanel() {
		super();
		init();
	}

	private void init() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		GridBagLayoutManager
				.addComponent(this, new I18nLabel("mobile"), gc, 0, 0, 1, 1, 0,
						0, GridBagConstraints.NONE, GridBagConstraints.EAST);
		GridBagLayoutManager
				.addComponent(this, createTextField(), gc, 1, 0, 1, 1, 1, 0,
						GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(this, Box.createGlue(), gc, 1, 1, 1,
				1, 0, 1, GridBagConstraints.VERTICAL, GridBagConstraints.WEST);
	}

	private Component createTextField() {
		mobileTf = new JTextField();
		return mobileTf;
	}
	
	@Override
	public boolean isContactValid() {
		String text = mobileTf.getText();
		return text.trim().isEmpty();
	}

	public Contact getContact() {
		Contact c = new Contact(ContactType.MOBILE);
		c.setValue(mobileTf.getText());
		return c;
	}
}
