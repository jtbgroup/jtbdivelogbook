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
package be.vds.jtb.jtbdivelogbook.persistence.xml.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import be.vds.jtb.jtbdivelogbook.persistence.xml.util.IdCounter;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.DiverUsedException;
import be.vds.jtbdive.core.utils.StringManipulator;
import be.vds.jtbdive.persistence.core.dao.interfaces.DaoFactory;
import be.vds.jtbdive.persistence.core.dao.interfaces.DiverDAO;
import be.vds.jtbdive.persistence.core.util.SurrogateKeys;
import be.vds.jtbdive.xml.parsers.DiversParser;
import be.vds.jtbdive.xml.utils.XMLUtils;

public class XMLDiverDAO implements DiverDAO {

	// private static final Logger logger = Logger.getLogger(XMLDiverDAO.class);
	private static XMLDiverDAO instance;
	private DiversParser diverParser;

	public static XMLDiverDAO getInstance() {
		if (null == instance) {
			instance = new XMLDiverDAO();
		}
		return instance;
	}

	private String basePath;

	// private Document diverdocument;

	public void initialize(String basePath) throws DataStoreException {
		this.basePath = basePath;
		readDocument();
	}

	private void readDocument() throws DataStoreException {
		try {
			File f = new File(basePath + File.separatorChar + "divers.xml");
			FileInputStream fis = new FileInputStream(f);

			if (f.length() == 0) {
				// diverdocument = new Document();
				diverParser = DiversParser.buildParser();
				// diverdocument.setRootElement(diverParser.getRootElement());
			} else {
				// SAXBuilder sb = new SAXBuilder();
				// diverdocument = sb.build(fis);
				Document doc = XMLUtils.readDocument(f);
				diverParser = DiversParser.createParser(doc.getRootElement());
			}
			// } catch (JDOMException e) {

		} catch (IOException e) {
			throw new DataStoreException(
					"Error occured during the initialization of the configuration",
					e);
		}
	}

	public static void main(String[] args) {
		try {
			SAXBuilder sb = new SAXBuilder();
			Document diverdocument = sb
					.build(new FileInputStream("divers.xml"));
			DocType doctype = new DocType("monDoctype", "divers.dtd");
			diverdocument.setDocType(doctype);
			System.out.println(diverdocument.getRootElement().getName());

		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeXMLDocument() throws IOException {
		OutputStream os = new FileOutputStream(
				(basePath + File.separatorChar + "divers.xml"));
		XMLUtils.writeDocument(os, new Document((Element) diverParser.getRootElement().clone()));
	}

	//TODO: check whether the return value is required
	public boolean deleteDiver(Diver diver) throws DataStoreException,
			DiverUsedException {
		if (DaoFactory.getFactory().createLogBookDAO().isDiverUsed(diver)) {
			throw new DiverUsedException("Diver " + diver.toString()
					+ " is used at least by one logbook and can't be deleted.");
		}

		boolean result = true;

		diverParser.removeDiver(diver.getId());

		// Element root = diverdocument.getRootElement();
		// for (Iterator iterator = root.getChildren("diver").iterator();
		// iterator
		// .hasNext();) {
		// Element diverElement = (Element) iterator.next();
		// if (diver.getId() == Long.parseLong(diverElement
		// .getAttributeValue("id"))) {
		// diverElement.detach();
		// try {
		// writeXMLDocument();
		// } catch (IOException e) {
		// throw new DataStoreException("IO Exception : "
		// + e.getMessage());
		// }
		// result = true;
		// break;
		// }
		// }

		try {
			writeXMLDocument();
		} catch (IOException e) {
			throw new DataStoreException("IO Exception : " + e.getMessage());
		}
		return result;
	}

	public List<Diver> findAllDivers() throws DataStoreException {
		return diverParser.getDivers();

		// Element root = diverdocument.getRootElement();
		// List<Diver> divers = new ArrayList<Diver>();
		// for (Iterator iterator = root.getChildren("diver").iterator();
		// iterator
		// .hasNext();) {
		// Element diverElement = (Element) iterator.next();
		// divers.add(readDiver(diverElement));
		// }
		//
		// return divers;
	}

	// private Diver readDiver(Element diverElement) {
	// return diverParser.readDiver(diverElement);
	// }

	@Override
	public List<Diver> findDiversByFirstOrLastName(String name)
			throws DataStoreException {
		if (null == name || name.length() == 0)
			return new ArrayList<Diver>();

		return diverParser.findDiver(name);
	}

	public List<Diver> findDiversByName(String firstName, String lastName)
			throws DataStoreException {
		if ((null == firstName || firstName.length() == 0)
				&& (null == lastName || lastName.length() == 0)) {
			return null;
		}

		return diverParser.findDiver(firstName, lastName);
	}

	public Diver saveDiver(Diver diver) throws DataStoreException {
		try {
			if (diver.getId() <= -1) {
				diver.setId(IdCounter.getInstance().getNextId(
						SurrogateKeys.DIVER));
			} else {
				diverParser.removeDiver(diver.getId());
			}
			// Element root = diverdocument.getRootElement();
			// root.addContent(createDiverElement(diver));
			diverParser.addDiver(diver);
			writeXMLDocument();
			return diver;
		} catch (IOException e) {
			throw new DataStoreException("IO exception : " + e.getMessage());
		}
	}

	// private Element createDiverElement(Diver diver) {
	// return diverParser.createDiverElement(diver);
	// }

	public Diver updateDiver(Diver diver) throws DataStoreException {
		if (diver.getId() > -1) {
			try {
				// Element root = diverdocument.getRootElement();
				//
				// for (Iterator iterator =
				// root.getChildren("diver").iterator(); iterator
				// .hasNext();) {
				// Element diverElement = (Element) iterator.next();
				// if (diver.getId() == Long.parseLong(diverElement
				// .getAttributeValue("id"))) {
				// diverElement.detach();
				// break;
				// }
				// }
				//
				// root.addContent(createDiverElement(diver));
				diverParser.removeDiver(diver.getId());
				diverParser.addDiver(diver);
				writeXMLDocument();
				return diver;
			} catch (IOException e) {
				throw new DataStoreException("IO exception : " + e.getMessage());
			}
		}
		return null;
	}

	public Diver findDiver(long diverId) {
		// Element root = diverdocument.getRootElement();
		// for (Iterator iterator = root.getChildren("diver").iterator();
		// iterator
		// .hasNext();) {
		// Element diverElement = (Element) iterator.next();
		// if (Long.parseLong(diverElement.getAttributeValue("id")) == id) {
		// return readDiver(diverElement);
		// }
		// }
		//
		// return null;
		return diverParser.findDiver(diverId);
	}

	public boolean mergeDivers(Diver diverToKeep, Diver diverToDelete)
			throws DataStoreException {
		DaoFactory.getFactory().createLogBookDAO()
				.mergeDivers(diverToKeep, diverToDelete);
		try {
			deleteDiver(diverToDelete);
		} catch (DiverUsedException e) {
			return false;
		}
		return true;
	}
}
