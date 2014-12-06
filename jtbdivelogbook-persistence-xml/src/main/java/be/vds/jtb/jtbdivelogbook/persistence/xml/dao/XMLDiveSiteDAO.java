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

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import be.vds.jtb.jtbdivelogbook.persistence.xml.handler.DiveSiteDocumentHandler;
import be.vds.jtb.jtbdivelogbook.persistence.xml.util.IdCounter;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.DiveLocationUsedException;
import be.vds.jtbdive.core.utils.StringManipulator;
import be.vds.jtbdive.persistence.core.dao.interfaces.DaoFactory;
import be.vds.jtbdive.persistence.core.dao.interfaces.DiveSiteDAO;
import be.vds.jtbdive.persistence.core.util.SurrogateKeys;
import be.vds.jtbdive.xml.parsers.DiveSitesParser;
import be.vds.jtbdive.xml.parsers.DiversParser;
import be.vds.jtbdive.xml.parsers.DocumentParser;
import be.vds.jtbdive.xml.utils.XMLUtils;

public class XMLDiveSiteDAO implements DiveSiteDAO {

	private static final Logger LOGGER = Logger.getLogger(XMLDiveSiteDAO.class);
	private static XMLDiveSiteDAO instance;
	private String basePath;
	private DiveSitesParser diveSitesParser;
	private DiveSiteDocumentHandler diveSiteDocumentHandler;

	private XMLDiveSiteDAO() {
	}

	public static XMLDiveSiteDAO getInstance() {
		if (null == instance) {
			instance = new XMLDiveSiteDAO();
		}
		return instance;
	}

	public void initialize(String basePath) throws DataStoreException {
		this.basePath = basePath;
		initializeDiveSiteParser(basePath);
		initializeDiveSiteDocumentHandler(basePath);
	}

	private void initializeDiveSiteDocumentHandler(String basePath) {
		diveSiteDocumentHandler = new DiveSiteDocumentHandler(basePath);
	}

	private void initializeDiveSiteParser(String basePath)
			throws DataStoreException {
		File f = new File(basePath + File.separatorChar + "divesites.xml");
		if (f.length() == 0) {
			diveSitesParser = DiveSitesParser.buildParser();
		} else {
			Document doc = XMLUtils.readDocument(f);
			diveSitesParser = DiveSitesParser
					.createParser(doc.getRootElement());
		}
	}

	private void writeXMLDocument() throws IOException {
		OutputStream os = new FileOutputStream(
				(basePath + File.separatorChar + "divesites.xml"));
		Document document = new Document((Element) diveSitesParser
				.getRootElement().clone());
		XMLUtils.writeDocument(os, document);
	}

	public boolean deleteDiveLocation(DiveSite diveLocation)
			throws DataStoreException, DiveLocationUsedException {
		return deleteDiveSite(diveLocation, true);
	}

	private boolean deleteDiveSite(DiveSite diveSite, boolean deleteDocuments)
			throws DataStoreException, DiveLocationUsedException {

		if (DaoFactory.getFactory().createLogBookDAO().isDiveSiteUsed(diveSite)) {
			throw new DiveLocationUsedException("Dive location "
					+ diveSite.toString()
					+ " is used at least by one logbook and can't be deleted.");
		}

		boolean result = false;

		List<String> documentIds = diveSitesParser.getDocumentNames(diveSite
				.getId());
		if (deleteDocuments && documentIds != null) {
			diveSiteDocumentHandler.deleteDocuments(documentIds);
		}
		diveSitesParser.removeDiveSite(diveSite.getId());

		return result;
	}

	public List<DiveSite> findAllDiveSites() throws DataStoreException {
		List<DiveSite> diveSites = diveSitesParser.getDiveSites(DaoFactory
				.getFactory().createGlossaryDAO().getCountriesMap());

		return diveSites;
	}

	public List<DiveSite> findDiveSitesByName(String name)
			throws DataStoreException {
		if (null == name || name.length() == 0) {
			return new ArrayList<DiveSite>();
		}

		List<DiveSite> diveSites = diveSitesParser.findDiveSiteByName(name,
				DaoFactory.getFactory().createGlossaryDAO().getCountriesMap());
		return diveSites;
	}

	public DiveSite saveDiveSite(DiveSite diveSite) throws DataStoreException {
		if (diveSite.getId() > -1) {
			throw new DataStoreException(
					"Impossible to save a dive site with id > -1");
		}

		try {
			diveSite.setId(IdCounter.getInstance().getNextId(
					SurrogateKeys.DIVESITE));

			if (diveSite.getDocuments() != null) {
				attributeDocumentIds(diveSite.getDocuments());
			}

			diveSitesParser.addDiveSite(diveSite);

			saveDocuments(diveSite);
			writeXMLDocument();
			return diveSite;
		} catch (IOException e) {
			throw new DataStoreException("IO exception : " + e.getMessage());
		}
	}

	private void saveDocuments(DiveSite diveSite) throws DataStoreException {
		if (diveSite.getDocuments() == null) {
			return;
		}

		// List<be.vds.jtbdive.core.core.Document> docs =
		// attributeDocumentIds(diveSite
		// .getDocuments());

		diveSiteDocumentHandler.saveDocuments(diveSite.getDocuments());
	}

	private void attributeDocumentIds(
			List<be.vds.jtbdive.core.core.Document> docs)
			throws DataStoreException {
		for (be.vds.jtbdive.core.core.Document document : docs) {
			if (document.getId() == -1) {
				document.setId(IdCounter.getInstance().getNextId(
						SurrogateKeys.DOCUMENT_DIVESITE));
			}
		}
	}

	public DiveSite updateDiveSite(DiveSite diveSite) throws DataStoreException {
		if (diveSite.getId() <= -1) {
			throw new DataStoreException(
					"Impossible to update a dive location with an ID <= -1");
		}
		try {
			List<be.vds.jtbdive.core.core.Document> currentDocuments = diveSitesParser
					.getDocuments(diveSite.getId());

			List<be.vds.jtbdive.core.core.Document> docs = diveSite
					.getDocuments();
			if (null != docs) {
				attributeDocumentIds(docs);
			}
			diveSiteDocumentHandler
					.synchronizeDocuments(currentDocuments, docs);

			diveSitesParser.removeDiveSite(diveSite.getId());
			diveSitesParser.addDiveSite(diveSite);
			writeXMLDocument();
			LOGGER.info("Dive Site (id: " + diveSite.getId() + ") updated");
			return diveSite;
		} catch (IOException e) {
			throw new DataStoreException("IO exception : " + e.getMessage());
		}
	}

	public DiveSite findDiveSiteById(long diveSiteId) throws DataStoreException {
		return findDiveSiteById(diveSiteId, false);
	}

	public DiveSite findDiveSiteById(long diveSiteId,
			boolean includeDocumentsContent) throws DataStoreException {
		DiveSite diveSite = diveSitesParser.findDiveSiteById(diveSiteId,
				DaoFactory.getFactory().createGlossaryDAO().getCountriesMap());
		if (includeDocumentsContent) {
			diveSiteDocumentHandler.fillDocumentContent(diveSite);
		}
		return diveSite;
	}

	public boolean mergeDiveSites(DiveSite diveLocationToKeep,
			DiveSite diveLocationToDelete) throws DataStoreException {
		DaoFactory.getFactory().createLogBookDAO()
				.mergeDiveLocations(diveLocationToKeep, diveLocationToDelete);

		diveLocationToKeep = findDiveSiteById(diveLocationToKeep.getId());
		diveLocationToDelete = findDiveSiteById(diveLocationToDelete.getId());

		if (diveLocationToDelete.getDocuments() != null
				&& diveLocationToDelete.getDocuments().size() > 0) {

			if (diveLocationToKeep.getDocuments() == null) {
				diveLocationToKeep
						.setDocuments(new ArrayList<be.vds.jtbdive.core.core.Document>());
			}

			diveLocationToKeep.getDocuments().addAll(
					diveLocationToDelete.getDocuments());
			updateDiveSite(diveLocationToKeep);
		}

		try {
			deleteDiveSite(diveLocationToDelete, false);
		} catch (DiveLocationUsedException e) {
			return false;
		}
		return true;
	}

	@Override
	public byte[] loadDocumentContent(long documentId, DocumentFormat format)
			throws DataStoreException {
		String name = documentId + "." + format.getExtension();
		String path = basePath + "documents" + File.separatorChar
				+ File.separatorChar + "divesites" + File.separatorChar + name;
		File f = new File(path);
		byte[] content = new byte[(int) f.length()];
		try {
			new FileInputStream(f).read(content);
		} catch (IOException e) {
			LOGGER.error("Can't load document content : " + e.getMessage());
			throw new DataStoreException("Can't load document content", e);
		}
		return content;
	}
}
