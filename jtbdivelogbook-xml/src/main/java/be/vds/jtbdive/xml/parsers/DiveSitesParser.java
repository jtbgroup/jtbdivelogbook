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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import be.vds.jtbdive.core.core.Address;
import be.vds.jtbdive.core.core.Coordinates;
import be.vds.jtbdive.core.core.Country;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Document;
import be.vds.jtbdive.core.core.Version;
import be.vds.jtbdive.core.core.catalogs.DiveSiteType;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.core.utils.StringManipulator;

public class DiveSitesParser extends AbstractXMLParser {

	private static final Syslog LOGGER = Syslog
			.getLogger(DiveSitesParser.class);
	private static final String TAG_DIVESITES = "divesites";
	private static final String TAG_DIVESITE = "divesite";
	private static final String TAG_DOCUMENTS = "documents";
	private static final String TAG_DOCUMENT = "document";
	private Element rootElement;

	private DiveSitesParser(Element rootElement) {
		this.rootElement = rootElement;
	}

	public static DiveSitesParser createParser(Element rootElement) {
		rootElement.detach();
		return new DiveSitesParser(rootElement);
	}

	public static DiveSitesParser buildParser() {
		DiveSitesParser parser = new DiveSitesParser(null);
		Element root = parser.createDiveSitesRootElement();
		parser.setRootElement(root);
		return parser;
	}

	private Element createDiveSitesRootElement() {
		Element root = new Element(TAG_DIVESITES);

		Element version = new Element("version");
		version.setText(Version.getCurrentVersion().toString());
		root.addContent(version);

		return root;
	}

	private void setRootElement(Element root) {
		rootElement = root;
	}

	public Element getRootElement() {
		return rootElement;
	}

	private Element createDiveSiteElement(DiveSite diveSite) {
		Element el = new Element(TAG_DIVESITE);
		el.setAttribute(ATTRIBUTE_ID, String.valueOf(diveSite.getId()));
		if (null != diveSite.getName()) {
			Element fnEl = new Element("name");
			fnEl.setText(diveSite.getName());
			el.addContent(fnEl);
		}

		if (diveSite.getDepth() != 0) {
			Element depthEl = new Element("depth");
			depthEl.setText(String.valueOf(diveSite.getDepth()));
			el.addContent(depthEl);
		}

		if (diveSite.getAltitude() != 0) {
			Element altitudeEl = new Element("altitude");
			altitudeEl.setText(String.valueOf(diveSite.getAltitude()));
			el.addContent(altitudeEl);
		}

		if (diveSite.hasCoordinates()) {
			el.addContent(createCoordinationElement(diveSite.getCoordinates()));
		}

		if (diveSite.getAddress() != null) {
			el.addContent(createAddressElement(diveSite.getAddress()));
		}

		if (diveSite.getInternetSite() != null) {
			el.addContent(createInternetSiteElement(diveSite.getInternetSite()));
		}

		if (diveSite.getDocuments() != null
				&& diveSite.getDocuments().size() > 0) {
			Element docsEl = new Element("documents");
			// Element docEl = null;
			// Element elt = null;
			DocumentParser parser = new DocumentParser();
			for (Document doc : diveSite.getDocuments()) {
				docsEl.addContent(parser.createDocumentElement(doc));
			}
			el.addContent(docsEl);

		}

		if (null != diveSite.getDiveSiteType()) {
			Element typeEl = new Element("type");
			typeEl.addContent(String
					.valueOf(diveSite.getDiveSiteType().getId()));
			el.addContent(typeEl);
		}

		return el;
	}

	private Element createInternetSiteElement(String internetSite) {
		Element el = new Element("internetsite");
		el.setText(internetSite);
		return el;
	}

	private Element createAddressElement(Address address) {
		Element addressEl = new Element("address");

		String street = address.getNumber();
		if (null != street && street.length() > 0) {
			Element streetEl = new Element("street");
			streetEl.setText(street);
			addressEl.addContent(streetEl);
		}

		String nbr = address.getNumber();
		if (null != nbr && nbr.length() > 0) {
			Element el = new Element("number");
			el.setText(nbr);
			addressEl.addContent(el);
		}

		String box = address.getBox();
		if (null != box && box.length() > 0) {
			Element el = new Element("box");
			el.setText(box);
			addressEl.addContent(el);
		}

		String zip = address.getZipCode();
		if (null != zip && zip.length() > 0) {
			Element el = new Element("zip");
			el.setText(zip);
			addressEl.addContent(el);
		}

		String city = address.getCity();
		if (null != city && city.length() > 0) {
			Element el = new Element("city");
			el.setText(city);
			addressEl.addContent(el);
		}

		String region = address.getRegion();
		if (null != region && region.length() > 0) {
			Element el = new Element("region");
			el.setText(address.getRegion());
			addressEl.addContent(el);
		}

		if (null != address.getCountry()) {
			Element el = new Element("country");
			el.setText(address.getCountry().getCode());
			addressEl.addContent(el);
		}

		return addressEl;
	}

	private Element createCoordinationElement(Coordinates coordinates) {
		Element latEl = new Element("latitude");
		latEl.setText(String.valueOf(coordinates.getLatitude()));

		Element longEl = new Element("longitude");
		longEl.setText(String.valueOf(coordinates.getLongitude()));

		Element coordEl = new Element("coordinates");
		coordEl.addContent(latEl);
		coordEl.addContent(longEl);
		return coordEl;
	}

	private DiveSite readDiveSiteElement(Element diveSiteElement,
			Map<String, Country> countries) {
		return readDiveSiteElement(diveSiteElement, 
				 countries, false);
	}

	private DiveSite readDiveSiteElement(Element diveSiteElement,
			 Map<String, Country> countries, boolean includeDocumentsContent) {
		return readDiveSiteElement(diveSiteElement, null, countries, includeDocumentsContent);
	}

	/**
	 * 
	 * @param diveSiteElement
	 * @param documentFolderPath
	 *            can't be null if includeDocumentsContent is true
	 * @param countries
	 * @return
	 */
	private DiveSite readDiveSiteElement(Element diveSiteElement,
			 String documentFolderPath,
			Map<String, Country> countries, boolean includeDocumentsContent) {
		DiveSite d = new DiveSite();
//		d.setLoadType(loadType);
		d.setId(Long.parseLong(diveSiteElement.getAttributeValue("id")));

		// NAME
		d.setName(diveSiteElement.getChildText("name"));

		// DEPTH
		String depth = diveSiteElement.getChildText("depth");
		if (null != depth)
			d.setDepth(Double.parseDouble(depth));

		// ALTITUDE
		String altitude = diveSiteElement.getChildText("altitude");
		if (null != altitude)
			d.setAltitude(Double.parseDouble(altitude));

		// COORDINATES
		Element coordEl = diveSiteElement.getChild("coordinates");
		if (coordEl != null) {
			double latitude = Double.parseDouble(coordEl
					.getChildText("latitude"));
			double longitude = Double.parseDouble(coordEl
					.getChildText("longitude"));
			Coordinates coordinates = new Coordinates(latitude, longitude);
			d.setCoordinates(coordinates);
		}

		// ADDRESS
		Element addressEl = diveSiteElement.getChild("address");
		if (addressEl != null) {
			d.setAddress(readAddress(addressEl, countries));
		}

		// Internet site
		Element internetSiteEl = diveSiteElement.getChild("internetsite");
		if (internetSiteEl != null) {
			d.setInternetSite(internetSiteEl.getText());
		}

		// Dive Site type
		d.setDiveSiteType(loadDiveType(diveSiteElement));

		// Documents references
		d.setDocuments(readDiveSiteDocuments(diveSiteElement));

		// if (loadType >= DiveSite.LOAD_MEDIUM && documentFolderPath != null) {
		// loadDiveSiteDocuments(diveSiteElement, loadType,
		// documentFolderPath, d);

		// }

		return d;
	}

	private List<Document> readDiveSiteDocuments(Element diveSiteElement) {
		if (diveSiteElement.getChild(TAG_DOCUMENTS) == null) {
			return null;
		} else {
			List<Document> l = new ArrayList<Document>();
			for (Iterator iterator = diveSiteElement.getChild(TAG_DOCUMENTS)
					.getChildren(TAG_DOCUMENT).iterator(); iterator.hasNext();) {
				Element documentEl = (Element) iterator.next();
				Document document = DocumentParser.readDocument(documentEl);
				l.add(document);
			}
			return l;
		}
	}

	private Address readAddress(Element addressEl,
			Map<String, Country> countries) {
		Address address = new Address();
		Element el = addressEl.getChild("street");
		if (el != null) {
			address.setStreet(el.getText());
		}

		el = addressEl.getChild("number");
		if (el != null)
			address.setNumber(el.getText());

		el = addressEl.getChild("box");
		if (el != null)
			address.setBox(el.getText());

		el = addressEl.getChild("zip");
		if (el != null)
			address.setZipCode(el.getText());

		el = addressEl.getChild("city");
		if (el != null)
			address.setCity(el.getText());

		el = addressEl.getChild("region");
		if (el != null)
			address.setRegion(el.getText());

		el = addressEl.getChild("country");
		if (el != null)
			address.setCountry(countries.get(el.getText()));

		return address;
	}

	private DiveSiteType loadDiveType(Element diveSiteElement) {
		Element el = diveSiteElement.getChild("type");
		if (el != null) {
			return DiveSiteType.getDiveSiteType(Short.parseShort(el.getText()));
		}
		return null;
	}

	// private void loadDiveSiteDocuments(Element diveSiteElement, short
	// loadType,
	// String documentFolderPath, DiveSite d) {
	// if (diveSiteElement.getChild("documents") == null) {
	// d.setDocuments(null);
	// } else {
	// List<Document> l = new ArrayList<Document>();
	// for (Iterator iterator = diveSiteElement.getChild("documents")
	// .getChildren("document").iterator(); iterator.hasNext();) {
	// Element documentEl = (Element) iterator.next();
	// Document document = readDocument(documentEl,
	// documentFolderPath, loadType);
	// l.add(document);
	// }
	// d.setDocuments(l);
	// }
	// }

	// private Document readDocument(Element documentEl,
	// String documentFolderPath, short loadType) {
	// Document document = DocumentParser.readDocument(documentEl);
	//
	// if (loadType >= DiveSite.LOAD_FULL) {
	// String path = documentFolderPath + File.separatorChar
	// + document.getId() + "."
	// + document.getDocumentFormat().getExtension();
	// try {
	// File file = new File(path);
	// byte[] content = new byte[(int) file.length()];
	// new FileInputStream(file).read(content);
	// document.setContent(content);
	// } catch (FileNotFoundException e) {
	// LOGGER.error("Problem loading the image " + path + ": "
	// + e.getMessage());
	// } catch (IOException e) {
	// LOGGER.error("Problem loading the image " + path + ": "
	// + e.getMessage());
	// }
	// }
	//
	// return document;
	// }

	public long getMaxDiveSiteId() {
		long maxId = 0;
		for (Iterator iterator = rootElement.getChildren(TAG_DIVESITE)
				.iterator(); iterator.hasNext();) {
			Element diverElement = (Element) iterator.next();

			long id = Long
					.valueOf(diverElement.getAttributeValue(ATTRIBUTE_ID));
			if (id > maxId) {
				maxId = id;
			}
		}
		return maxId;
	}

	public void removeDiveSite(long diveSiteId) {
		if (null != rootElement) {
			detachElWithIdFromParent(rootElement, diveSiteId);
		}
	}

	public List<DiveSite> getDiveSites(Map<String, Country> countries) {
		List<DiveSite> result = new ArrayList<DiveSite>();
		for (Iterator iterator = rootElement.getChildren(TAG_DIVESITE)
				.iterator(); iterator.hasNext();) {
			Element subEl = (Element) iterator.next();
			result.add(readDiveSiteElement(subEl, countries));
		}

		return result;
	}

	public List<String> getDocumentNames(long diveSiteId) {
		Element diveSiteElement = getDiveSiteElement(diveSiteId);
		if (null == diveSiteElement) {
			Element docs = diveSiteElement.getChild("documents");
			if (docs != null) {
				return DocumentParser.getDocumentFileNames(docs);
			}
		}
		return null;
	}

	private Element getDiveSiteElement(long diveSiteId) {
		for (Iterator iterator = rootElement.getChildren(TAG_DIVESITE)
				.iterator(); iterator.hasNext();) {
			Element subEl = (Element) iterator.next();
			if (Long.valueOf(subEl.getAttributeValue(ATTRIBUTE_ID)) == diveSiteId) {
				return subEl;
			}
		}

		return null;
	}

	public void addDiveSite(DiveSite diveSite) {
		rootElement.addContent(createDiveSiteElement(diveSite));
	}

	public DiveSite findDiveSiteById(long diveSiteId, 
			Map<String, Country> countries) {
		return findDiveSiteById(diveSiteId, countries, false);
	}
	
	public DiveSite findDiveSiteById(long diveSiteId, 
			Map<String, Country> countries, boolean includeDocumentsContent) {
		for (Iterator iterator = rootElement.getChildren(TAG_DIVESITE)
				.iterator(); iterator.hasNext();) {
			Element subElement = (Element) iterator.next();
			if (Long.parseLong(subElement.getAttributeValue(ATTRIBUTE_ID)) == diveSiteId) {
				return readDiveSiteElement(subElement, countries, includeDocumentsContent);
			}
		}
		return null;
	}

	public List<DiveSite> findDiveSiteByName(String name,
			Map<String, Country> countriesMap) {
		List<DiveSite> result = new ArrayList<DiveSite>();
		for (Iterator iterator = rootElement.getChildren(TAG_DIVESITE)
				.iterator(); iterator.hasNext();) {
			Element subElement = (Element) iterator.next();
			String dlName = subElement.getChildText("name");

			boolean nameFit = (null != dlName && StringManipulator
					.removeDiacriticalMarks(dlName.toLowerCase()).contains(
							StringManipulator.removeDiacriticalMarks(name
									.toLowerCase())));
			if (nameFit) {
				result.add(readDiveSiteElement(subElement, countriesMap));
			}
		}
		return result;
	}

	public List<Document> getDocuments(long diveSiteId) {
		List<Document> documents = new ArrayList<Document>();
		Element diveSiteElement = getDiveSiteElement(diveSiteId);
		if (null != diveSiteElement) {
			Element docs = diveSiteElement.getChild(TAG_DOCUMENTS);
			if (docs != null) {
				for (Iterator iterator = docs.getChildren(TAG_DOCUMENT)
						.iterator(); iterator.hasNext();) {
					Element docEl = (Element) iterator.next();
					documents.add(DocumentParser.readDocument(docEl));
				}
			}
		}
		return documents;
	}

	public long getMaxDiveSiteDocumentId() {
		long maxId = -1;
		for (Iterator iterator = rootElement.getChildren(TAG_DIVESITE)
				.iterator(); iterator.hasNext();) {
			Element diveSiteElement = (Element) iterator.next();
			Element docs = diveSiteElement.getChild(TAG_DOCUMENTS);
			if (docs != null) {
				for (Iterator iterator2 = docs.getChildren(TAG_DOCUMENT)
						.iterator(); iterator2.hasNext();) {
					Element docEl = (Element) iterator2.next();
					maxId = Math.max(maxId, Long.parseLong(docEl
							.getAttributeValue(ATTRIBUTE_ID)));
				}
			}
		}
		return maxId;
	}

}
