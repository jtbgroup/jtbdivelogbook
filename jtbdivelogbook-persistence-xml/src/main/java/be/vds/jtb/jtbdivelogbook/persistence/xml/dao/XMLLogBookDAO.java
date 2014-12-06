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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import be.vds.jtb.jtbdivelogbook.persistence.xml.util.IdCounter;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.LogBookMeta;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.persistence.core.dao.interfaces.DaoFactory;
import be.vds.jtbdive.persistence.core.dao.interfaces.LogBookDAO;
import be.vds.jtbdive.persistence.core.util.SurrogateKeys;
import be.vds.jtbdive.xml.parsers.LogBookParser;
import be.vds.jtbdive.xml.parsers.MatCaveParser;
import be.vds.jtbdive.xml.utils.XMLUtils;

public class XMLLogBookDAO implements LogBookDAO {

	private static final Logger LOGGER = Logger.getLogger(XMLLogBookDAO.class);
	private static XMLLogBookDAO instance;
	private String basePath;

	public static XMLLogBookDAO getInstance() {
		if (null == instance) {
			instance = new XMLLogBookDAO();
		}
		return instance;
	}

	public LogBook findLogBook(long id) throws DataStoreException {
		LogBook result = readLogBook(basePath + File.separatorChar + "logbook_"
				+ id + ".xml");
		return result;
	}

	/**
	 * This method reads a logbook based on the given filename.
	 * 
	 * @todo The algorithm could be enhanced by getting only the divers and the
	 *       dive locations of the concerned logbook.
	 * @param fileName
	 * @return
	 * @throws DataStoreException
	 */
	private LogBook readLogBook(String fileName) throws DataStoreException {
		LogBook lb = null;
		Document doc = XMLUtils.readDocument(new File(fileName));
		LogBookParser logBookParser = LogBookParser.createParser(doc
				.getRootElement());
		lb = logBookParser.readLogBook(getDiveSitesMap(), getDiversMap());
		return lb;
	}

	public Map<Long, DiveSite> getDiveSitesMap() {
		Map<Long, DiveSite> diveLocations = new HashMap<Long, DiveSite>();
		try {
			List<DiveSite> dls = DaoFactory.getFactory().createDiveSiteDAO()
					.findAllDiveSites();
			diveLocations = new HashMap<Long, DiveSite>();
			for (DiveSite diveloc : dls) {
				diveLocations.put(diveloc.getId(), diveloc);
			}
		} catch (DataStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return diveLocations;
	}

	public Map<Long, Diver> getDiversMap() {
		Map<Long, Diver> divers = new HashMap<Long, Diver>();
		try {
			List<Diver> ds = DaoFactory.getFactory().createDiverDAO()
					.findAllDivers();
			divers = new HashMap<Long, Diver>();
			for (Diver diver : ds) {
				divers.put(diver.getId(), diver);
			}
		} catch (DataStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return divers;
	}

	public List<LogBook> findLogBookNames() throws DataStoreException {
		File base = new File(basePath);
		List<LogBook> result = new ArrayList<LogBook>();
		for (String fileName : base.list()) {
			if (fileName.startsWith("logbook_") && fileName.endsWith(".xml")) {
				result.add(getLogBookLight(basePath + File.separatorChar
						+ fileName));
			}
		}
		return result;
	}

	private LogBook getLogBookLight(String fileName) throws DataStoreException {
		Document doc = XMLUtils.readDocument(new File(fileName));
		LogBookParser logBookParser = LogBookParser.createParser(doc
				.getRootElement());
		LogBook lb = new LogBook();
		lb.setId(logBookParser.getLogBookId());
		lb.setName(logBookParser.getLogBookName());
		return lb;
	}

	// public LogBook saveLogBook(LogBook logBook) throws DataStoreException {
	// try {
	// if (logBook.getId() <= -1) {
	// logBook.setId(IdCounter.getInstance().getNextId(
	// SurrogateKeys.LOGBOOK));
	// }
	//
	// for (Dive dive : logBook.getDives()) {
	// if (dive.getId() <= -1) {
	// dive.setId(IdCounter.getInstance().getNextId(
	// SurrogateKeys.DIVE));
	// }
	// }
	//
	// Element rootElement = logbookParser
	// .createLogBookRootElement(logBook);
	//
	// Document doc = new Document(rootElement);
	// File f = getFilePathForLogBook(logBook.getId());
	// if (!f.exists()) {
	// f.createNewFile();
	// }
	//
	// OutputStream os = new FileOutputStream(f);
	// XMLOutputter outputter = new XMLOutputter();
	// outputter.setFormat(Format.getPrettyFormat());
	// outputter.output(doc, os);
	// os.close();
	// } catch (IOException e) {
	// throw new DataStoreException(
	// "IO Exception when saving the logbook : " + e.getMessage());
	// }
	//
	// return logBook;
	// }

	public void mergeDiveLocations(DiveSite diveSiteToKeep,
			DiveSite diveSiteToDelete) throws DataStoreException {
		List<LogBook> lbs = findLogBookNames();
		for (LogBook logBook : lbs) {

			File file = getFilePathForLogBook(logBook.getId());
			Document doc = XMLUtils.readDocument(file);
			LogBookParser logBookParser = LogBookParser.createParser(doc
					.getRootElement());
			logBookParser.replaceDiveSite(diveSiteToKeep.getId(),
					diveSiteToDelete.getId());

			XMLUtils.writeDocument(file, doc);

		}
	}

	public boolean isDiveSiteUsed(DiveSite diveSite) throws DataStoreException {
		List<LogBook> lbs = findLogBookNames();
		for (LogBook logBook : lbs) {
			Document doc = getLogBookRootDocument(logBook.getId());
			LogBookParser logBookParser = LogBookParser.createParser(doc
					.getRootElement());
			boolean b = logBookParser.isDiveSiteUsed(diveSite.getId());
			if (b)
				return true;
		}
		return false;
	}

	public boolean isDiverUsed(Diver diver) throws DataStoreException {
		List<LogBook> lbs = findLogBookNames();
		for (LogBook logBook : lbs) {
			Document doc = getLogBookRootDocument(logBook.getId());
			LogBookParser logBookParser = LogBookParser.createParser(doc
					.getRootElement());

			if (logBookParser.getLogBookOwnerId() == diver.getId()) {
				return true;
			}

			boolean b = logBookParser.isDiverUsed(diver.getId());
			if (b)
				return true;
		}
		return false;
	}

	public void mergeDivers(Diver diverToKeep, Diver diverToDelete)
			throws DataStoreException {
		List<LogBook> lbs = findLogBookNames();
		for (LogBook logBook : lbs) {

			File f = getFilePathForLogBook(logBook.getId());
			Document doc = XMLUtils.readDocument(f);
			LogBookParser logBookParser = LogBookParser.createParser(doc
					.getRootElement());
			logBookParser.replaceDiver(diverToKeep.getId(),
					diverToDelete.getId());
			XMLUtils.writeDocument(f, doc);
		}
	}

	@Override
	public Dive saveDive(long logbookId, Dive dive) throws DataStoreException {
		Document doc = getLogBookRootDocument(logbookId);
		LogBookParser logBookParser = LogBookParser.createParser(doc
				.getRootElement());

		saveDive(logBookParser, dive);

		XMLUtils.writeDocument(getFilePathForLogBook(logbookId), doc);
		LOGGER.info("Dive " + dive.getId() + " saved in logbook " + logbookId);

		return dive;
	}

	private void persistDocument(be.vds.jtbdive.core.core.Document document) {
		File f = new File(basePath + "documents" + File.separatorChar
				+ File.separatorChar + "dives" + File.separatorChar
				+ document.getId() + "."
				+ document.getDocumentFormat().getExtension());
		try {
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(document.getContent());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	@Override
	public Dive reloadDive(long logbookId, long diveId)
			throws DataStoreException {
		if (diveId <= -1)
			return null;
		Document doc = getLogBookRootDocument(logbookId);
		LogBookParser logBookParser = LogBookParser.createParser(doc
				.getRootElement());
		Dive d = logBookParser.readDiveElement(diveId, getDiveSitesMap(),
				getDiversMap(), null);
		return d;
	}

	@Override
	public LogBookMeta saveLogBookMeta(LogBookMeta m) throws DataStoreException {
		if (m.getId() == -1) {
			return createLogBookMeta(m);
		} else {
			return updateMeta(m);
		}
	}

	private LogBookMeta createLogBookMeta(LogBookMeta m)
			throws DataStoreException {
		try {
			m.setId(IdCounter.getInstance().getNextId(SurrogateKeys.LOGBOOK));
			File f = getFilePathForLogBook(m.getId());
			if (!f.exists()) {
				f.createNewFile();
			}

			LogBook logBook = new LogBook();
			logBook.setLogbookMeta(m);
			LogBookParser parser = LogBookParser.buildParser(logBook);
			Document doc = new Document(parser.getRootElement());

			XMLUtils.writeDocument(f, doc);
			LOGGER.info("Metadata created for logbook " + m.getId());

			return m;
		} catch (IOException ex) {
			LOGGER.error(ex);
			throw new DataStoreException(ex);
		}
	}

	private LogBookMeta updateMeta(LogBookMeta m) throws DataStoreException {
		File f = getFilePathForLogBook(m.getId());
		Document doc = XMLUtils.readDocument(f);
		LogBookParser logBookParser = LogBookParser.createParser(doc
				.getRootElement());
		logBookParser.setLogBookName(m.getName());
		logBookParser.setOwner(m.getOwner().getId());

		XMLUtils.writeDocument(f, doc);
		LOGGER.info("Metadata updated for logbook " + m.getId());

		return m;

	}

	@Override
	public Dive deleteDive(long logbookId, Dive dive) throws DataStoreException {
		long diveId = dive.getId();
		Document doc = getLogBookRootDocument(logbookId);
		LogBookParser logBookParser = LogBookParser.createParser(doc
				.getRootElement());
		List<String> documentNames = logBookParser
				.findDiveDocumentNames(diveId);
		boolean b = logBookParser.removeDive(diveId);
		deleteDiveDocuments(documentNames);

		if (b) {
			XMLUtils.writeDocument(getFilePathForLogBook(logbookId), doc);
			LOGGER.info("Dive " + dive.getId() + " removed from logbook "
					+ logbookId);
			return dive;
		}
		LOGGER.info("No dive with id " + diveId + " found.");
		return null;
	}

	@Override
	public List<Dive> saveDives(List<Dive> dives, long logbookId)
			throws DataStoreException {
		Document doc = getLogBookRootDocument(logbookId);
		LogBookParser logBookParser = LogBookParser.createParser(doc
				.getRootElement());

		StringBuffer buffer = new StringBuffer();
		buffer.append("Logbook").append(logbookId).append(" - Dives saved : ");
		for (Dive dive : dives) {
			saveDive(logBookParser, dive);
			buffer.append(dive.getId()).append(", ");
		}

		XMLUtils.writeDocument(getFilePathForLogBook(logbookId), doc);
		LOGGER.info(buffer.toString());

		return dives;
	}

	private Document getLogBookRootDocument(long logbookId)
			throws DataStoreException {
		File f = getFilePathForLogBook(logbookId);
		SAXBuilder sb = new SAXBuilder();
		try {
			return sb.build(new FileInputStream(f));
		} catch (Exception e) {
			throw new DataStoreException(e);
		}
	}

	private File getFilePathForLogBook(long logbookId) {
		return new File(basePath + File.separatorChar + "logbook_" + logbookId
				+ ".xml");
	}

	private void saveDive(LogBookParser logBookParser, Dive dive)
			throws DataStoreException {

		List<String> documentNamesToDelete = null;
		if (dive.getId() != -1) {
			documentNamesToDelete = logBookParser.findDiveDocumentNames(dive
					.getId());
			logBookParser.removeDive(dive.getId());
		} else {
			dive.setId(IdCounter.getInstance().getNextId(SurrogateKeys.DIVE));
		}

		if (dive.getDocuments() != null && dive.getDocuments().size() > 0) {
			for (be.vds.jtbdive.core.core.Document doc : dive.getDocuments()) {
				if (doc.getId() > -1) {
					String name = doc.getId() + "."
							+ doc.getDocumentFormat().getExtension();
					documentNamesToDelete.remove(name);
				} else {
					doc.setId(IdCounter.getInstance().getNextId(
							SurrogateKeys.DOCUMENT_DIVE));
				}
			}
		}

		logBookParser.addDive(dive);

		if (documentNamesToDelete != null && documentNamesToDelete.size() > 0)
			deleteDiveDocuments(documentNamesToDelete);

		if (dive.getDocuments() != null && dive.getDocuments().size() > 0)
			persistDocuments(dive.getDocuments());

	}

	private void persistDocuments(
			List<be.vds.jtbdive.core.core.Document> documents) {
		for (be.vds.jtbdive.core.core.Document document : documents) {
			if (document.getContent() != null)
				persistDocument(document);
		}
	}

	private void deleteDiveDocuments(List<String> documentNamesToDelete) {
		if (null != documentNamesToDelete) {
			for (String name : documentNamesToDelete) {
				String path = basePath + "documents" + File.separatorChar
						+ File.separatorChar + "dives" + File.separatorChar
						+ name;
				new File(path).delete();
				LOGGER.info("Dive document " + name + " deleted");
			}
		}
	}

	@Override
	public byte[] loadDocumentContent(long documentId, DocumentFormat format)
			throws DataStoreException {
		String name = documentId + "." + format.getExtension();
		String path = basePath + "documents" + File.separatorChar
				+ File.separatorChar + "dives" + File.separatorChar + name;
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

	@Override
	public Material saveMaterial(long logbookId, Material material)
			throws DataStoreException {
		Document doc = getLogBookRootDocument(logbookId);
		LogBookParser logBookParser = LogBookParser.createParser(doc
				.getRootElement());

		if (material.getId() == -1) {
			material.setId(IdCounter.getInstance().getNextId(
					SurrogateKeys.MATERIAL));
		}
		logBookParser.addMaterial(material);

		XMLUtils.writeDocument(getFilePathForLogBook(logbookId), doc);
		LOGGER.info("Material " + material.getId() + " saved in logbook "
				+ logbookId);

		return material;
	}

	@Override
	public MaterialSet saveMaterialSet(long logBookId, MaterialSet materialset)
			throws DataStoreException {
		Document doc = getLogBookRootDocument(logBookId);
		LogBookParser logBookParser = LogBookParser.createParser(doc
				.getRootElement());
		if (materialset.getId() == -1) {
			materialset.setId(IdCounter.getInstance().getNextId(
					SurrogateKeys.MATERIALSET));
		}
		logBookParser.addMaterialSet(materialset);

		XMLUtils.writeDocument(getFilePathForLogBook(logBookId), doc);
		LOGGER.info("Materialset " + materialset.getName()
				+ " saved in logbook " + logBookId);

		return materialset;
	}

	@Override
	public boolean deleteMaterial(long logbookId, Material material)
			throws DataStoreException {
		List<Material> m = new ArrayList<Material>();
		m.add(material);
		return deleteMaterials(logbookId, m);
	}
	
	@Override
	public boolean deleteMaterials(long logbookId, List<Material> materials)
			throws DataStoreException {
		Document doc = getLogBookRootDocument(logbookId);
		LogBookParser logBookParser = LogBookParser.createParser(doc
				.getRootElement());
		for (Material material : materials) {
			logBookParser.removeMaterial(material.getId());
			LOGGER.info("Material " + material.getId() + " deleted");
		}
		XMLUtils.writeDocument(getFilePathForLogBook(logbookId), doc);
		return true;
	}

	@Override
	public Material mergeMaterial(long logbookId, Material materialToKeep,
			Material materialToDelete) throws DataStoreException {
		Document doc = getLogBookRootDocument(logbookId);
		LogBookParser logBookParser = LogBookParser.createParser(doc
				.getRootElement());
		logBookParser.replaceMaterialReference(materialToKeep.getId(),
				materialToDelete.getId());

		XMLUtils.writeDocument(getFilePathForLogBook(logbookId), doc);
		LOGGER.info("Material " + materialToDelete.getId() + " replaced by "
				+ materialToKeep.getId());
		return materialToKeep;
	}

	@Override
	public boolean deleteLogBook(long logbookId) throws DataStoreException {
		Document doc = getLogBookRootDocument(logbookId);
		LogBookParser parser = LogBookParser.createParser(doc.getRootElement());
		List<String> l = parser.findAllDivesDocumentNames();
		deleteDiveDocuments(l);
		File f = getFilePathForLogBook(logbookId);
		f.delete();
		LOGGER.info("Logbook " + logbookId + " deleted");
		return true;
	}

	@Override
	public boolean deleteMaterialSet(long logbookId, MaterialSet materialSet)
			throws DataStoreException {
		Document doc = getLogBookRootDocument(logbookId);
		LogBookParser parser = LogBookParser.createParser(doc.getRootElement());
		parser.removeMaterialSet(materialSet.getId());

		XMLUtils.writeDocument(getFilePathForLogBook(logbookId), doc);
		LOGGER.info("Material set " + materialSet.getId() + " deleted");
		return true;
	}

	@Override
	public boolean deleteMaterialFromMaterialSet(long logbookId,
			long materialSetId, Material material) throws DataStoreException {
		Document doc = getLogBookRootDocument(logbookId);
		LogBookParser logBookParser = LogBookParser.createParser(doc
				.getRootElement());
		logBookParser.removeMaterialFromMaterialSet(materialSetId,
				material.getId());
		XMLUtils.writeDocument(getFilePathForLogBook(logbookId), doc);
		LOGGER.info("Material " + material.getId()
				+ " deleted from material set " + materialSetId);
		return true;
	}

	@Override
	public boolean saveMaterialsIntoMaterialSet(long logbookId,
			long materialSetId, Material[] materials) throws DataStoreException {
		Document doc = getLogBookRootDocument(logbookId);
		LogBookParser logBookParser = LogBookParser.createParser(doc
				.getRootElement());
		logBookParser.addMaterialsIntoMaterialSet(materialSetId, materials);
		XMLUtils.writeDocument(getFilePathForLogBook(logbookId), doc);
		LOGGER.info(materials.length + " materials saved into material set "
				+ materialSetId);
		return true;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	@Override
	public long getMaxDocumentId(long logbookId) throws DataStoreException {
		Document doc = getLogBookRootDocument(logbookId);
		LogBookParser logBookParser = LogBookParser.createParser(doc
				.getRootElement());
		return logBookParser.getMaxDocumentId();
		
	}
}
