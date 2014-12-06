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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;

import be.vds.jtb.jtbdivelogbook.persistence.xml.util.IdCounter;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.Version;
import be.vds.jtbdive.core.core.modifications.PersistenceVersion;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.VersionException;
import be.vds.jtbdive.core.utils.FileUtilities;
import be.vds.jtbdive.persistence.core.dao.interfaces.ConfigurationDAO;
import be.vds.jtbdive.persistence.core.dao.interfaces.DaoFactory;
import be.vds.jtbdive.persistence.core.dao.interfaces.LogBookDAO;
import be.vds.jtbdive.persistence.core.util.SurrogateKeys;
import be.vds.jtbdive.xml.converter.XMLVersionDiveSitesConverter;
import be.vds.jtbdive.xml.converter.XMLVersionDiversConverter;
import be.vds.jtbdive.xml.converter.XMLVersionLogbookConverter;
import be.vds.jtbdive.xml.converter.XMLVersionSurrogatesConverter;
import be.vds.jtbdive.xml.parsers.DiveSitesParser;
import be.vds.jtbdive.xml.parsers.DiversParser;
import be.vds.jtbdive.xml.parsers.LogBookParser;
import be.vds.jtbdive.xml.utils.XMLUtils;

public class XMLConfigurationDAO implements ConfigurationDAO {

	private static final Logger LOGGER = Logger
			.getLogger(XMLConfigurationDAO.class);
	private static XMLConfigurationDAO instance;
	private String basePath;

	public static XMLConfigurationDAO getInstance() {
		if (null == instance) {
			instance = new XMLConfigurationDAO();
		}
		return instance;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public boolean initialize() throws DataStoreException, VersionException {
		if (basePath == null)
			throw new DataStoreException(
					"Your XML Base Path is null. Nothing can be initialized...");
		return initialize(basePath);
	}

	private boolean initialize(String basePath) throws DataStoreException,
			VersionException {
		try {
			boolean b = createStructure(basePath);
			// Do when the structure isn't new
			if (!b) {
				checkPersistenceVersion();
			}
		} catch (IOException e) {
			throw new DataStoreException(
					"Error while initializing the XML configuration", e);
		}
		return true;
	}

	private void checkPersistenceVersion() throws DataStoreException,
			VersionException {
		Version currentPersistenceVersion = getCurrentPersistenceVersion();
		LOGGER.debug("File version of the persistence is : "
				+ currentPersistenceVersion.toString());
		Version lastPersistenceVersion = Version
				.createVersion(PersistenceVersion.getLastPersistenceVersion()
						.getVersionId());
		LOGGER.debug("Application last version of the persistence is : "
				+ lastPersistenceVersion.toString());

		if (Version.getCurrentVersion().isLowerThan(currentPersistenceVersion)) {
			LOGGER.debug("Current version of the persistence "
					+ currentPersistenceVersion.toString()
					+ " requires a more recent version of the application.");
			throw new VersionException(
					false,
					"Current version of the persistence "
							+ currentPersistenceVersion.toString()
							+ " requires a more recent version of the application. Please use the last version.");
		} else if (currentPersistenceVersion.equals(lastPersistenceVersion)
				|| currentPersistenceVersion
						.isHigerOrEqualsThan(lastPersistenceVersion)) {
			LOGGER.debug("current version of the persistence still good");
		} else {
			LOGGER.warn("current version of the persistence not good anymore !!!!");
			throw new VersionException(true,
					"Current version of the persistence "
							+ currentPersistenceVersion.toString()
							+ " not good anymore.", currentPersistenceVersion);
		}
	}

	private Version getCurrentPersistenceVersion() throws DataStoreException {
		String s = IdCounter.getInstance().getCurrentId(SurrogateKeys.VERSION);
		if (s == null) {
			return Version.createVersion(PersistenceVersion.V_1_0_0
					.getVersionId());
		}
		return Version.createVersion(s);
	}

	/**
	 * 
	 * @param basePath
	 * @return a boolean to know wheter the file "surrogates" has been created;
	 *         meaning the installation is new.
	 * @throws IOException
	 * @throws DataStoreException
	 */
	private boolean createStructure(String basePath) throws IOException,
			DataStoreException {
		boolean isNew = false;
		boolean isDiversNew = false;
		boolean isDiveSitesNew = false;
		File f = new File(basePath);
		if (!f.exists()) {
			f.mkdirs();
		}

		IdCounter.getInstance().setBasePath(basePath);

		isNew = createSurrogateFile(basePath);
		isDiversNew = createDiversFile();
		isDiveSitesNew = createDiveSitesFile();

		if (isNew && !isDiversNew) {
			adaptSurrogatesForDivers();
		}

		if (isNew && !isDiveSitesNew) {
			adaptSurrogatesForDiveSites();
		}

		if (isNew) {
			adaptSurrogatesForLogBook();
		}

		createDir(basePath + File.separatorChar + "documents");
		createDir(basePath + File.separatorChar + "documents"
				+ File.separatorChar + "divesites");
		createDir(basePath + File.separatorChar + "documents"
				+ File.separatorChar + "dives");

		return isNew;
	}

	private void adaptSurrogatesForLogBook() {
		try {
			List<LogBook> lbs = DaoFactory.getFactory().createLogBookDAO()
					.findLogBookNames();
			long id = -1;
			for (LogBook logBook : lbs) {
				id = Math.max(id, logBook.getId());
			}

			if (id > -1) {
				IdCounter.getInstance().setCurrentId(SurrogateKeys.LOGBOOK,
						id + 1);
			}

			adaptSurrogatesForLogbookDocuments();

		} catch (DataStoreException e) {
			LOGGER.error("Error while getting surrogates for logbooks");
		}
	}

	private void adaptSurrogatesForLogbookDocuments() {
		try {
			long id = -1;
			LogBookDAO dao = DaoFactory.getFactory().createLogBookDAO();
			List<LogBook> lbs = dao.findLogBookNames();
			for (LogBook logBook : lbs) {
				id = Math.max(id, dao.getMaxDocumentId(logBook.getId()));
			}
			
			if (id > -1) {
				IdCounter.getInstance().setCurrentId(SurrogateKeys.DOCUMENT_DIVE,
						id + 1);
			}
		} catch (DataStoreException e) {
			LOGGER.error("Error while getting documents surrogates for logbooks");
		}
	}

	private void adaptSurrogatesForDivers() throws DataStoreException {
		File f = getDiversFile();
		Document d = XMLUtils.readDocument(f);
		DiversParser p = DiversParser.createParser(d.getRootElement());
		long maxId = p.getMaxDiverId() + 1;

		IdCounter.getInstance().setCurrentId(SurrogateKeys.DIVER, maxId);
	}

	private void adaptSurrogatesForDiveSites() throws DataStoreException {
		File f = getDiveSitesFile();
		Document d = XMLUtils.readDocument(f);
		DiveSitesParser p = DiveSitesParser.createParser(d.getRootElement());
		long maxId = p.getMaxDiveSiteId() + 1;

		IdCounter.getInstance().setCurrentId(SurrogateKeys.DIVESITE, maxId);
		
		maxId = p.getMaxDiveSiteDocumentId() + 1;
		IdCounter.getInstance().setCurrentId(SurrogateKeys.DOCUMENT_DIVESITE, maxId);
	}

	
	private boolean createDiveSitesFile() throws IOException,
			FileNotFoundException {
		boolean b = false;
		File f = getDiveSitesFile();
		if (!f.exists()) {
			f.createNewFile();
			FileOutputStream fis = new FileOutputStream(f);
			fis.write(("<divesites><" + SurrogateKeys.VERSION + ">"
					+ Version.getCurrentVersion().toString() + "</"
					+ SurrogateKeys.VERSION + "></divesites>").getBytes());
			fis.close();
			b = true;
		}
		return b;
	}

	private boolean createDiversFile() throws IOException,
			FileNotFoundException {
		boolean b = false;
		File f = getDiversFile();
		if (!f.exists()) {
			f.createNewFile();
			FileOutputStream fis = new FileOutputStream(f);
			fis.write(("<divers><" + SurrogateKeys.VERSION + ">"
					+ Version.getCurrentVersion().toString() + "</"
					+ SurrogateKeys.VERSION + "></divers>").getBytes());
			fis.close();
			b = true;
		}
		return b;
	}

	private boolean createSurrogateFile(String basePath) throws IOException,
			FileNotFoundException {
		boolean isNew = false;
		File f = new File(basePath + File.separatorChar + "surrogates.xml");
		if (!f.exists()) {
			f.createNewFile();
			FileOutputStream fis = new FileOutputStream(f);
			fis.write(("<surrogates><" + SurrogateKeys.VERSION + ">"
					+ Version.getCurrentVersion().toString() + "</"
					+ SurrogateKeys.VERSION + "></surrogates>").getBytes());
			isNew = true;
			fis.close();
		}
		return isNew;
	}

	// private void replaceDiveSitesTag(File f) throws IOException {
	// // FileUtils.replaceAllInFile(f, "<divelocations>", "<divesites>");
	// // FileUtils.replaceAllInFile(f, "</divelocations>", "</divesites>");
	// // FileUtils.replaceAllInFile(f, "<divelocation>", "<divesite>");
	// // FileUtils.replaceAllInFile(f, "</divelocation>", "</divesite>");
	// String[] oldValues = { "<divelocations>", "</divelocations>",
	// "<divelocation", "</divelocation>" };
	// String[] newValues = { "<divesites>", "</divesites>", "<divesite",
	// "</divesite>" };
	// FileUtils.replaceAllInFile(f, oldValues, newValues);
	//
	// LOGGER.info("replaced dive location tags in file " + f);
	// }

	private void createDir(String path) {
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	private List<File> findLogBookFiles(String basePath) {
		File base = new File(basePath);
		List<File> result = new ArrayList<File>();
		for (String fileName : base.list()) {
			if (fileName.startsWith("logbook_") && fileName.endsWith(".xml")) {
				result.add(new File(basePath + File.separatorChar + fileName));
			}
		}
		return result;
	}

	@Override
	public boolean upgradePersistenceVersion() throws DataStoreException {
		Version lastPersistenceVersion = Version
				.createVersion(PersistenceVersion.getLastPersistenceVersion()
						.getVersionId());
		Version currentPeristenceVersion = getCurrentPersistenceVersion();

		boolean avoidrollback = true;

		List<File> originalFiles = new ArrayList<File>();
		originalFiles.add(getDiversFile());
		originalFiles.add(getDiveSitesFile());
		originalFiles.add(getSurrogatesFile());
		for (File file : findLogBookFiles(basePath)) {
			originalFiles.add(file);
		}
		Map<File, File> backupFiles = null;
		try {
			backupFiles = backUpFiles(originalFiles);
		} catch (IOException e) {
			LOGGER.warn("Can't create backup files...");
		}

		try {
			if (avoidrollback) {
				avoidrollback = avoidrollback
						& convertDivers(currentPeristenceVersion,
								lastPersistenceVersion);
			}

			if (avoidrollback) {
				avoidrollback = avoidrollback
						& convertDiveSites(currentPeristenceVersion,
								lastPersistenceVersion);
			}

			if (avoidrollback) {
				avoidrollback = avoidrollback
						& convertLogBooks(currentPeristenceVersion,
								lastPersistenceVersion);
			}

			if (avoidrollback) {
				avoidrollback = avoidrollback
						& convertSurrogates(currentPeristenceVersion,
								lastPersistenceVersion);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e);
			avoidrollback = false;
		}

		if (avoidrollback == false) {
			rollBackFiles(backupFiles);
		} else {
			for (File file : backupFiles.values()) {
				file.delete();
				LOGGER.debug("Backup file deleted : " + file.getAbsolutePath());
			}
		}

		return avoidrollback;
	}

	private boolean convertLogBooks(Version currentPeristenceVersion,
			Version lastPersistenceVersion) throws DataStoreException {
		try {
			for (File f : findLogBookFiles(basePath)) {
				InputStream is = new FileInputStream(f.getAbsoluteFile() + "~");
				new XMLVersionLogbookConverter().convert(
						currentPeristenceVersion, lastPersistenceVersion, is,
						new FileOutputStream(f));
			}
		} catch (IOException e) {
			LOGGER.warn(e);
			return false;
		}
		return true;
	}

	private boolean convertSurrogates(Version currentPeristenceVersion,
			Version lastPersistenceVersion) throws DataStoreException {
		try {
			File f = getSurrogatesFile();
			InputStream is = new FileInputStream(f.getAbsoluteFile() + "~");
			new XMLVersionSurrogatesConverter().convert(
					currentPeristenceVersion, lastPersistenceVersion, is,
					new FileOutputStream(f));
		} catch (IOException e) {
			LOGGER.warn(e);
			return false;
		}
		return true;
	}

	private boolean convertDiveSites(Version currentPeristenceVersion,
			Version lastPersistenceVersion) throws DataStoreException {
		try {
			InputStream is = new FileInputStream(getDiveSitesFile()
					.getAbsolutePath() + "~");
			new XMLVersionDiveSitesConverter().convert(
					currentPeristenceVersion, lastPersistenceVersion, is,
					new FileOutputStream(getDiveSitesFile()));
		} catch (IOException e) {
			LOGGER.warn(e);
			return false;
		}
		return true;
	}

	private boolean convertDivers(Version currentPeristenceVersion,
			Version lastPersistenceVersion) throws DataStoreException {
		try {

			InputStream is = new FileInputStream(getDiversFile()
					.getAbsolutePath() + "~");
			new XMLVersionDiversConverter().convert(currentPeristenceVersion,
					lastPersistenceVersion, is, new FileOutputStream(
							getDiversFile()));
		} catch (IOException e) {
			LOGGER.warn(e);
			return false;
		}
		return true;
	}

	private Map<File, File> backUpFiles(List<File> originalFiles)
			throws IOException {
		Map<File, File> backUpFiles = new HashMap<File, File>();

		for (File file : originalFiles) {
			File f = new File(file.getAbsoluteFile() + "~");
			FileUtilities.copy(file, f);
			backUpFiles.put(file, f);
		}
		return backUpFiles;
	}

	private void rollBackFiles(Map<File, File> backupFiles) {
		if (null == backupFiles) {
			LOGGER.info("No file to roll back");
			return;
		}
		for (File file : backupFiles.keySet()) {
			file.delete();
			try {
				FileUtilities.renameFile(backupFiles.get(file), file);
				LOGGER.debug("Recovered File " + file.getAbsolutePath());
			} catch (IOException e) {
				LOGGER.error("Couldn't recover file " + file.getAbsolutePath());
			}
		}
	}

	private File getDiversFile() {
		File f = new File(basePath + File.separatorChar + "divers.xml");
		return f;
	}

	private File getSurrogatesFile() {
		return new File(basePath + File.separatorChar + "surrogates.xml");
	}

	private File getDiveSitesFile() {
		return new File(basePath + File.separatorChar + "divesites.xml");
	}
}
