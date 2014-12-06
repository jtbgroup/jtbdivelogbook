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
package be.vds.jtbdive.xml.parsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom.Element;

import be.vds.jtbdive.core.core.Contact;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.Version;
import be.vds.jtbdive.core.core.catalogs.ContactType;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.core.utils.StringManipulator;

public class DiversParser extends AbstractXMLParser {
	private static final Syslog LOGGER = Syslog.getLogger(DiversParser.class);
	private static final SimpleDateFormat FORMATTER_DATE = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final String TAG_DIVER = "diver";
	private static final String TAG_LASTNAME = "lastname";
	private static final String TAG_FIRSTNAME = "firstname";

	private Element rootElement;

	private DiversParser(Element rootElement) {
		this.rootElement = rootElement;
	}

	public static DiversParser createParser(Element rootElement) {
		return new DiversParser(rootElement);
	}

	public static DiversParser buildParser() {
		DiversParser parser = new DiversParser(null);
		Element root = parser.createDiversRootElement();
		parser.setRootElement(root);
		return parser;
	}

	private void setRootElement(Element root) {
		rootElement = root;
	}

	public Element getRootElement() {
		return rootElement;
	}

	private Element createDiversRootElement() {
		Element root = new Element("divers");
		Element version = new Element("version");
		version.setText(Version.getCurrentVersion().toString());
		root.addContent(version);
		return root;
	}

	private Element createDiverElement(Diver diver) {
		Element el = new Element("diver");
		el.setAttribute("id", String.valueOf(diver.getId()));
		if (null != diver.getFirstName()) {
			Element fnEl = new Element("firstname");
			fnEl.setText(diver.getFirstName());
			el.addContent(fnEl);
		}

		if (null != diver.getLastName()) {
			Element fnEl = new Element("lastname");
			fnEl.setText(diver.getLastName());
			el.addContent(fnEl);
		}

		if (null != diver.getBirthDate()) {
			Element fnEl = new Element("birthdate");
			fnEl.setText(FORMATTER_DATE.format(diver.getBirthDate()));
			el.addContent(fnEl);
		}

		if (null != diver.getContacts()) {
			el.addContent(createContactsEl(diver.getContacts()));
		}

		return el;
	}

	private Element createContactsEl(List<Contact> contacts) {
		Element contactsEl = new Element("contacts");
		Element contactEl = null;
		for (Contact contact : contacts) {
			contactEl = new Element("contact");
			contactEl.setAttribute("type",
					String.valueOf(contact.getContactType().getId()));
			contactEl.setText(contact.getValue());
			contactsEl.addContent(contactEl);
		}
		return contactsEl;
	}

	private Diver readDiver(Element diverElement) {
		Diver diver = new Diver(Long.parseLong(diverElement
				.getAttributeValue("id")));
		diver.setFirstName(diverElement.getChildText("firstname"));
		diver.setLastName(diverElement.getChildText("lastname"));

		Element bdEl = diverElement.getChild("birthdate");
		if (bdEl != null) {
			try {
				diver.setBirthDate(FORMATTER_DATE.parse(bdEl.getText()));
			} catch (ParseException e) {
				LOGGER.error("Error with birthdate format for diver "
						+ diver.getId() + " : " + e.getMessage());
			}
		}

		Element contactsEl = diverElement.getChild("contacts");
		if (contactsEl != null) {
			diver.setContacts(readContacts(contactsEl));
		}

		return diver;
	}

	private List<Contact> readContacts(Element contactsEl) {
		List<Contact> contacts = new ArrayList<Contact>();
		Contact contact = null;
		for (Iterator iterator = contactsEl.getChildren("contact").iterator(); iterator
				.hasNext();) {
			Element el = (Element) iterator.next();
			contact = new Contact(ContactType.getContactTypeForId(Integer
					.parseInt(el.getAttributeValue("type"))));
			contact.setValue(el.getText());
			contacts.add(contact);
		}
		return contacts;
	}

	// public Element createRootElement() {
	// return new Element("divers");
	// }

	public void addDiver(Diver diver) {
		rootElement.addContent(createDiverElement(diver));
	}

	public List<Diver> getDivers() {
		List<Diver> result = new ArrayList<Diver>();
		if (rootElement != null) {
			for (Iterator iterator = rootElement.getChildren("diver")
					.iterator(); iterator.hasNext();) {
				Element dEl = (Element) iterator.next();
				Diver d = readDiver(dEl);
				result.add(d);
			}
		}
		return result;
	}

	public void removeDiver(long diverId) {
		if (null != rootElement) {
			detachElWithIdFromParent(rootElement, diverId);
		}
	}

	public Diver findDiver(long diverId) {
		for (Iterator iterator = rootElement.getChildren(TAG_DIVER).iterator(); iterator
				.hasNext();) {
			Element diverElement = (Element) iterator.next();
			if (Long.parseLong(diverElement.getAttributeValue(ATTRIBUTE_ID)) == diverId) {
				return readDiver(diverElement);
			}
		}
		return null;
	}

	public List<Diver> findDiver(String firstName, String lastName) {
		List<Diver> divers = new ArrayList<Diver>();
		for (Iterator iterator = rootElement.getChildren(TAG_DIVER).iterator(); iterator
				.hasNext();) {
			Element diverElement = (Element) iterator.next();

			String fn = diverElement.getChildText(TAG_FIRSTNAME);
			String ln = diverElement.getChildText(TAG_LASTNAME);

			boolean firstNameFit = (null != fn && StringManipulator
					.removeDiacriticalMarks(fn.toLowerCase()).contains(
							StringManipulator.removeDiacriticalMarks(firstName
									.toLowerCase())));
			boolean lastNameFit = (null != ln && StringManipulator
					.removeDiacriticalMarks(ln.toLowerCase()).contains(
							StringManipulator.removeDiacriticalMarks(lastName
									.toLowerCase())));

			if (firstNameFit && lastNameFit) {
				divers.add(readDiver(diverElement));
			}
		}

		return divers;
	}

	public List<Diver> findDiver(String name) {
		List<Diver> divers = new ArrayList<Diver>();
		for (Iterator iterator = rootElement.getChildren(TAG_DIVER).iterator(); iterator
				.hasNext();) {
			Element diverElement = (Element) iterator.next();

			String firstName = diverElement.getChildText(TAG_FIRSTNAME);
			String lastName = diverElement.getChildText(TAG_LASTNAME);

			boolean firstNameFit = (null != firstName && StringManipulator
					.removeDiacriticalMarks(firstName.toLowerCase()).contains(
							StringManipulator.removeDiacriticalMarks(name
									.toLowerCase())));
			boolean lastNameFit = (null != lastName && StringManipulator
					.removeDiacriticalMarks(lastName.toLowerCase()).contains(
							StringManipulator.removeDiacriticalMarks(name
									.toLowerCase())));

			if (firstNameFit || lastNameFit) {
				divers.add(readDiver(diverElement));
			}
		}

		return divers;
	}

	public long getMaxDiverId() {
		long maxId = 0;
		for (Iterator iterator = rootElement.getChildren(TAG_DIVER).iterator(); iterator
				.hasNext();) {
			Element diverElement = (Element) iterator.next();

			long id = Long
					.valueOf(diverElement.getAttributeValue(ATTRIBUTE_ID));
			if (id > maxId) {
				maxId = id;
			}
		}
		return maxId;
	}

}
