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
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtbdive.client.core.comparator.KeyedCatalogComparator;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;
import be.vds.jtbdive.core.core.Contact;
import be.vds.jtbdive.core.core.catalogs.ContactType;

public class ContactCreationPanel extends JPanel {

	private JComboBox contactTypeCb;
	private JPanel detailPanel;
	private CardLayout detailCardLayout;
	private Map<ContactType, ContactPanel> detailPanels = new HashMap<ContactType, ContactPanel>();
	private ContactPanel currentDetailPanel;

	public ContactCreationPanel() {
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.add(createChoicePanel(), BorderLayout.NORTH);
		this.add(createDetailPanel(), BorderLayout.CENTER);
	}

	private Component createChoicePanel() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		ContactType[] ct = ContactType.values();
		Arrays.sort(ct, new KeyedCatalogComparator());
		contactTypeCb = new JComboBox(ct);
		contactTypeCb.setRenderer(new KeyedCatalogRenderer());
		contactTypeCb.setSelectedItem(null);
		contactTypeCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				adaptDetailPanel();
			}
		});
		p.add(contactTypeCb);
		return p;
	}

	private void adaptDetailPanel() {
		ContactType ct = (ContactType) contactTypeCb.getSelectedItem();
		currentDetailPanel = detailPanels.get(ct);
		detailCardLayout.show(detailPanel, ct.getKey());
		currentDetailPanel.fireContactChanged();
	}

	private Component createDetailPanel() {
		detailCardLayout = new CardLayout();
		detailPanel = new JPanel(detailCardLayout);
		detailPanel.setOpaque(false);

		detailPanel.add(createDefaultPanel(), "default");
		for (ContactType ct : ContactType.values()) {
			ContactPanel p = createPanel(ct);
			detailPanel.add(p, ct.getKey());
			detailPanels.put(ct, p);
		}

		return detailPanel;
	}

	private Component createDefaultPanel() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.add(new I18nLabel("selection.empty"));
		return p;
	}

	private ContactPanel createPanel(ContactType ct) {
		ContactPanel cp = null;
		switch (ct) {
		case EMAIL:
			cp = new EmailContactPanel();
			break;
		case MOBILE:
			cp = new MobileContactPanel();
			break;
		case PHONE:
			cp = new PhoneContactPanel();
			break;
		}

		return cp;
	}

	public boolean isContactValid() {
		if (null == currentDetailPanel)
			return false;

		return currentDetailPanel.isContactValid();
	}

	public Contact getContact() {
		if (null == currentDetailPanel)
			return null;

		return currentDetailPanel.getContact();
	}

	public void addContactChangeListener(
			ContactChangeListener contactChangeListener) {
		for (ContactPanel cp : detailPanels.values()) {
			cp.addContactChangeListener(contactChangeListener);
		}
	}

}
