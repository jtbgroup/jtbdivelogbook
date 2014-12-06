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
package be.vds.jtbdive.xml.converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import be.vds.jtbdive.core.core.Version;
import be.vds.jtbdive.core.core.modifications.PersistenceVersion;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.xml.utils.XMLUtils;

public abstract class XMLVersionConverter {
	private static final Syslog LOGGER = Syslog
			.getLogger(XMLVersionConverter.class);

	/**
	 * Converts an InputStream according to the target version
	 * 
	 * @param sourceVersion
	 * @param targetVersion
	 * @param is
	 * @return the well formed String according to the desired version or null
	 *         if there is no change.
	 * @throws DataStoreException
	 * @throws IOException
	 */
	public void convert(Version sourceVersion, Version targetVersion,
			InputStream is, OutputStream out) throws DataStoreException,
			IOException {
		boolean upgrade = true;
		Document document = XMLUtils.readDocument(is);
		if (document != null) {

			while (upgrade) {
				if (sourceVersion.isLowerThan(targetVersion)) {
					PersistenceVersion source = PersistenceVersion
							.getPersistenceVersionForVersion(sourceVersion);
					PersistenceVersion target = PersistenceVersion
							.getNextPersistenceVersion(source);
					convertTo(target, document);
					sourceVersion = Version
							.createVersion(target.getVersionId());
				} else {
					upgrade = false;
				}
			}
			
			
			updateVersionTag(targetVersion, document.getRootElement());
			new XMLOutputter().output(document, out);
		}
	}

	private void convertTo(PersistenceVersion targetVersion, Document document) throws DataStoreException, IOException {
		LOGGER.info("converting to " + targetVersion.getVersionId());
		switch (targetVersion) {
		case V_1_0_0:
			LOGGER.info("This is the basic version... No conversion required");
			break;
		case V_2_6_0:
			convertToVersion_2_6_0(targetVersion, document);
			break;
		case V_2_7_0:
			convertToVersion_2_7_0(targetVersion, document);
			break;
		}
	}

	private void updateVersionTag(Version targetVersion,
			Element rootElement) {
		Element el = rootElement.getChild("version");
		if (el == null) {
			el = new Element("version");
			rootElement.addContent(el);
		}
		el.setText(targetVersion.toString());
		LOGGER.debug("Adding the version in the logbook file");
	}

	/**
	 * We suppose we come from the previous persistence Version
	 * 
	 * @param targetVersion
	 * @param is
	 * @param out
	 * @throws IOException
	 */
	public abstract void convertToVersion_2_6_0(
			PersistenceVersion targetVersion, Document document)
			throws IOException;

	public abstract void convertToVersion_2_7_0(
			PersistenceVersion targetVersion, Document document)
			throws IOException;
}
