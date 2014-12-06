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
package be.vds.jtbdive.client.core.conversion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import be.vds.jtbdive.client.actions.ImpExDocumentHandler;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.client.util.XsdValidator;
import be.vds.jtbdive.core.core.Country;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.Version;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;
import be.vds.jtbdive.core.core.modifications.PersistenceVersion;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.XMLValidationException;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.xml.converter.XMLVersionDiveSitesConverter;
import be.vds.jtbdive.xml.converter.XMLVersionDiversConverter;
import be.vds.jtbdive.xml.converter.XMLVersionLogbookConverter;
import be.vds.jtbdive.xml.parsers.DiveSitesParser;
import be.vds.jtbdive.xml.parsers.DiversParser;
import be.vds.jtbdive.xml.parsers.LogBookParser;
import be.vds.jtbdive.xml.utils.XMLUtils;

/**
 * This parser is written to deal with UDDF standard V2.2.0. To see which tags
 * are filled, please refer to the write method.s
 * 
 * @author gautier
 */
public class ImpExJTBHandler implements ImpExDocumentHandler {

	private static final Syslog LOGGER = Syslog
			.getLogger(ImpExJTBHandler.class);
	private File file;

	public ImpExJTBHandler(File file) {
		this.file = file;
	}

	public void write(DiveSiteManagerFacade diveSiteManagerFacade,
			LogBookManagerFacade logBookManagerFacade, List<Dive> dives,
			LogBook logBook, File dest) throws IOException,
			XMLValidationException, DataStoreException {
		write(dives, logBook, dest, null, diveSiteManagerFacade,
				logBookManagerFacade);
	}

	/**
	 * * Writes the data in parameters under the JTB form (XML). Restrict the
	 * dives to the selected ones, but keeps every material and material set.
	 * 
	 * @param dives
	 *            the dives to be contained in the file.
	 * @param logBook
	 * @param dest
	 * @param xsdInputStream
	 * @param diveSiteManagerFacade
	 * @param logBookManagerFacade
	 * @throws IOException
	 * @throws XMLValidationException
	 * @throws DataStoreException
	 */
	public void write(List<Dive> dives, LogBook logBook, File dest,
			InputStream xsdInputStream,
			DiveSiteManagerFacade diveSiteManagerFacade,
			LogBookManagerFacade logBookManagerFacade) throws IOException,
			XMLValidationException, DataStoreException {
		List<DiveSite> diveSites = LogBookUtilities.getDiveSites(dives, diveSiteManagerFacade);
		List<Diver> divers = LogBookUtilities.getDivers(dives,
				logBook.getOwner());
		List<Material> materials = logBook.getMatCave().getAllMaterials();
		Set<MaterialSet> materialSets = logBook.getMatCave().getMaterialSets();

		String divesString = getLogBookXMLString(logBook, dives, diveSites,
				divers, materials, materialSets, xsdInputStream);
		String diveSitesString = getDiveSitesXMLString(logBook, dives,
				diveSites, divers, xsdInputStream);
		String diversString = getDiversXMLString(logBook, dives, diveSites,
				divers, xsdInputStream);
		String versionString = "version="
				+ Version.getCurrentVersion().toString();

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dest));
		ZipEntry ze = new ZipEntry("logbook.xml");
		zos.putNextEntry(ze);
		zos.write(divesString.getBytes("UTF-8"));
		zos.flush();

		ze = new ZipEntry("divers.xml");
		zos.putNextEntry(ze);
		zos.write(diversString.getBytes("UTF-8"));
		zos.flush();

		ze = new ZipEntry("divesites.xml");
		zos.putNextEntry(ze);
		zos.write(diveSitesString.getBytes("UTF-8"));
		zos.flush();

		ze = new ZipEntry("version.properties");
		zos.putNextEntry(ze);
		zos.write(versionString.getBytes("UTF-8"));
		zos.flush();

		writeDiveSiteDocuments(zos, diveSites, diveSiteManagerFacade);
		writeDiveDocuments(zos, dives, logBookManagerFacade);

		zos.close();

		LOGGER.info("Export done.");
	}

	private String getDiveSitesXMLString(LogBook logBook, List<Dive> dives,
			List<DiveSite> diveSites, List<Diver> divers,
			InputStream xsdInputStream) throws IOException {

		DiveSitesParser p = DiveSitesParser.buildParser();

		for (DiveSite diveSite : diveSites) {
			p.addDiveSite(diveSite);
		}
		Document doc = new Document();
		doc.setRootElement(p.getRootElement());

		return XMLUtils.getXmlDocumentAsString(doc);
	}

	private String getDiversXMLString(LogBook logBook, List<Dive> dives,
			List<DiveSite> diveSites, List<Diver> divers,
			InputStream xsdInputStream) throws IOException {
		DiversParser pa = DiversParser.buildParser();
		for (Diver diver : divers) {
			pa.addDiver(diver);
		}
		Document doc = new Document();
		doc.setRootElement(pa.getRootElement());

		return XMLUtils.getXmlDocumentAsString(doc);
	}

	private void writeDiveDocuments(ZipOutputStream zos, List<Dive> dives,
			LogBookManagerFacade logBookManagerFacade) throws IOException {
		String base = "documents/dives";

		for (Dive dive : dives) {
			if (dive.getDocuments() != null) {
				ZipEntry ze = null;
				for (be.vds.jtbdive.core.core.Document doc : dive
						.getDocuments()) {
					try {
						ze = new ZipEntry(base + "/" + doc.getId() + "."
								+ doc.getDocumentFormat().getExtension());
						zos.putNextEntry(ze);
						zos.write(logBookManagerFacade.loadDocumentContent(
								doc.getId(), doc.getDocumentFormat()));
						zos.flush();
					} catch (DataStoreException e) {
						LOGGER.error(e);
					}
				}
			}
		}
	}

	private void writeDiveSiteDocuments(ZipOutputStream zos,
			List<DiveSite> diveSites,
			DiveSiteManagerFacade diveSiteManagerFacade) throws IOException {
		String base = "documents/divesites";

		for (DiveSite diveSite : diveSites) {
			if (diveSite.getDocuments() != null) {
				ZipEntry ze = null;
				for (be.vds.jtbdive.core.core.Document doc : diveSite
						.getDocuments()) {
					try {
						ze = new ZipEntry(base + "/" + doc.getId() + "."
								+ doc.getDocumentFormat().getExtension());
						zos.putNextEntry(ze);
						zos.write(diveSiteManagerFacade.loadDocumentContent(
								doc.getId(), doc.getDocumentFormat()));
						zos.flush();
					} catch (DataStoreException e) {
						LOGGER.error(e);
					}
				}
			}
		}
	}

	private String getLogBookXMLString(LogBook logBook, List<Dive> dives,
			List<DiveSite> diveSites, List<Diver> divers,
			List<Material> materials, Set<MaterialSet> materialSets,
			InputStream xsdInputStream) throws XMLValidationException,
			IOException {
		Document doc = new Document();
		doc.setRootElement(getRootElement(logBook, dives, materials,
				materialSets));

		if (null != xsdInputStream) {
			boolean b = XsdValidator.validXML(doc, xsdInputStream);
			if (!b) {
				throw new XMLValidationException(
						"The XML generated doesn't fulfill the XSD udcf.xsd");
			}
		}

		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		XMLOutputter outputter = new XMLOutputter(format);
		return outputter.outputString(doc);
	}

	private Element getRootElement(LogBook logBook, List<Dive> dives,
			List<Material> materials, Set<MaterialSet> materialSets) {
		LogBookParser lbP = LogBookParser.buildParser(logBook, dives,
				materials, materialSets);
		return lbP.getRootElement();
	}

	public LogBook read(Map<String, Country> countries)
			throws DataStoreException {

		Version archVersion = Version.createVersion(getRealPersistenceVersion()
				.getVersionId());
		Version lastPersistenceVersion = Version
				.createVersion(PersistenceVersion.getLastPersistenceVersion()
						.getVersionId());
		boolean needsUpgrade = false;
		if (archVersion.isLowerThan(lastPersistenceVersion)) {
			needsUpgrade = true;
			LOGGER.info("Archive needs an upgrade (from "
					+ archVersion.toString() + " to "
					+ lastPersistenceVersion.toString() + ")");
		}

		LogBook logbook = null;
		try {
			SAXBuilder sb = new SAXBuilder();

			// read all the divesites
			InputStream is = getResource("divesites.xml");
			if (needsUpgrade) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				new XMLVersionDiveSitesConverter().convert(archVersion,
						lastPersistenceVersion, is, out);
				is = new ByteArrayInputStream(out.toByteArray());
			}
			Document doc = sb.build(is);
			Map<Long, DiveSite> diveSitesMap = new HashMap<Long, DiveSite>();

			DiveSitesParser dlp = DiveSitesParser.createParser(doc
					.getRootElement());
			for (DiveSite diveSite : dlp.getDiveSites(countries)) {
				diveSitesMap.put(diveSite.getId(), diveSite);
			}

			// read all the divers
			is = getResource("divers.xml");
			if (needsUpgrade) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				new XMLVersionDiversConverter().convert(archVersion,
						lastPersistenceVersion, is, out);
				is = new ByteArrayInputStream(out.toByteArray());
			}
			doc = sb.build(is);
			Map<Long, Diver> diverMap = new HashMap<Long, Diver>();

			DiversParser pa = DiversParser.createParser(doc.getRootElement());
			List<Diver> divers = pa.getDivers();

			for (Diver diver : divers) {
				diverMap.put(diver.getId(), diver);
			}

			// read the logbook
			is = getResource("logbook.xml");
			if (needsUpgrade) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				new XMLVersionLogbookConverter().convert(archVersion,
						lastPersistenceVersion, is, out);
				is = new ByteArrayInputStream(out.toByteArray());
			}
			doc = sb.build(is);
			LogBookParser p = LogBookParser.createParser(doc.getRootElement());
			logbook = p.readLogBook(diveSitesMap, diverMap);

		} catch (JDOMException e) {
			LOGGER.error("JDom Exception while reading jtb file : "
					+ e.getMessage());
			throw new DataStoreException(e);
		} catch (ZipException e) {
			LOGGER.error("ZIP Exception while reading jtb file : "
					+ e.getMessage());
			throw new DataStoreException(e);
		} catch (IOException e) {
			LOGGER.error("IO Exception while reading jtb file : "
					+ e.getMessage());
			throw new DataStoreException(e);
		}

		return logbook;
	}

	private PersistenceVersion getRealPersistenceVersion() {
		PersistenceVersion result = PersistenceVersion.V_1_0_0;

		InputStream is = null;
		try {
			is = getResource("version.properties");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return result;
		}

		if (null == is) {
			LOGGER.info("The version of the archive is not the same as the current one ("
					+ Version.getCurrentVersion().toString() + ").");
		} else {
			Properties props = new Properties();
			try {
				props.load(is);
				String s = props.getProperty("version");
				Version archVersion = Version.createVersion(s);
				result = PersistenceVersion
						.getPersistenceVersionForVersion(archVersion);
			} catch (IOException e) {
				LOGGER.error(e);
			}
		}
		return result;
	}

	private InputStream getResource(String name) throws ZipException,
			IOException {
		ZipFile zipFile = new ZipFile(file);
		ZipEntry ze = zipFile.getEntry(name);
		if (ze == null) {
			throw new ZipException("Resource " + name
					+ " not found in the zip file");
		}
		InputStream is = zipFile.getInputStream(ze);
		return is;
	}

	public byte[] getDiveSiteDocumentContent(long id,
			DocumentFormat documentFormat) throws DataStoreException {
		try {
			String fileName = "documents/divesites/" + id + "."
					+ documentFormat.getExtension();
			InputStream is = getResource(fileName);
			return readDocumentContent(is);
		} catch (ZipException e) {
			LOGGER.error("ZIP Exception while reading jtb file : "
					+ e.getMessage());
			throw new DataStoreException(e);
		} catch (IOException e) {
			LOGGER.error("ZIP Exception while reading jtb file : "
					+ e.getMessage());
			throw new DataStoreException(e);
		}
	}

	public byte[] getDiveDocumentContent(long id, DocumentFormat documentFormat)
			throws DataStoreException {
		try {
			String fileName = "documents/dives/" + id + "."
					+ documentFormat.getExtension();
			InputStream is = getResource(fileName);
			return readDocumentContent(is);
		} catch (ZipException e) {
			LOGGER.error("ZIP Exception while reading jtb file : "
					+ e.getMessage());
			throw new DataStoreException(e);
		} catch (IOException e) {
			LOGGER.error("ZIP Exception while reading jtb file : "
					+ e.getMessage());
			throw new DataStoreException(e);
		}
	}

	private byte[] readDocumentContent(InputStream is) throws IOException {
		List<Integer> contentList = new ArrayList<Integer>();
		boolean read = true;
		while (read) {
			int b = is.read();
			if (b == -1) {
				read = false;
			} else {
				contentList.add(b);
			}
		}
		byte[] content = new byte[contentList.size()];
		int i = 0;
		for (Integer integer : contentList) {
			content[i] = integer.byteValue();
			i++;
		}
		return content;
	}
}
