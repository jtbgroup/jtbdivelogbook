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

import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import be.vds.jtbdive.core.core.Contact;

public abstract class ContactPanel extends JPanel {

	public static final String PROPERTY_VALUE_CHANGED = "value.changed";
	private Set<ContactChangeListener> contactChangeListeners = new HashSet<ContactChangeListener>();

	public ContactPanel() {
		doFirst();
	}

	private void doFirst() {
		setOpaque(false);
	}

	public abstract Contact getContact();

	public abstract boolean isContactValid();

	public void addContactChangeListener(
			ContactChangeListener contactChangeListener) {
		contactChangeListeners.add(contactChangeListener);
	}

	protected void fireContactChanged() {
		for (ContactChangeListener c : contactChangeListeners) {
			c.contactChange();
		}
	}
}
