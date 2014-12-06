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
package be.vds.jtbdive.xml.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.core.utils.FileUtilities;

public class XMLUtils {

	private static final Syslog LOGGER = Syslog.getLogger(XMLUtils.class);

	public static void writeDocument(File file, Document document)
			throws DataStoreException {
		File tempFile = new File(file.getAbsolutePath() + "~");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			FileUtilities.copy(file, tempFile);
			LOGGER.debug("Backing up file " + file.getAbsolutePath());
			writeDocument(fos, document);
			tempFile.delete();
			LOGGER.debug("Temp file deleted");
			fos = null;
		} catch (IOException e) {
			LOGGER.error(
					"Error while saving document in the file. Rolling back.", e);
			try {
				FileUtilities.copy(tempFile, file);
				tempFile.delete();
				LOGGER.debug("Temp file deleted");
				throw new DataStoreException(e);
			} catch (IOException e1) {
				LOGGER.error("Temp File couldn't be restored... BIG TROUBLE!!! Temp file hasn't been deleted ("
						+ tempFile.getAbsolutePath() + ")");
				throw new DataStoreException(e1);
			}
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					LOGGER.error("Error while closing the ouptputstream for the file "
							+ file.getAbsolutePath());
				}
			}
		}
	}

	public static void writeDocument(OutputStream os, Document document)
			throws IOException {
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		XMLOutputter outputter = new XMLOutputter(format);
		outputter.output(document, os);
		os.close();
		LOGGER.debug("XML Document saved");
	}

	public static void writeXmlAsString(OutputStream os, String s)
			throws IOException, DataStoreException {
		Document document = readDocument(new ByteArrayInputStream(
				s.getBytes("UTF-8")));
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		XMLOutputter outputter = new XMLOutputter(format);
		outputter.output(document, os);
		os.close();
	}

	public static String getXmlDocumentAsString(Document document) {
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		XMLOutputter outputter = new XMLOutputter(format);
		return outputter.outputString(document);
	}

	/**
	 * Reads an inputstream using JDom and return a JDOM {@link Document}
	 * 
	 * @param is
	 *            the Inputstream to be read.
	 * @return the read {@link Document}
	 * @throws DataStoreException
	 */
	public static Document readDocument(InputStream is)
			throws DataStoreException {
		Document document = null;
		SAXBuilder sb = new SAXBuilder();
		try {
			document = sb.build(is);
		} catch (Exception e) {
			LOGGER.error("Problem while reading the inputstream", e);
			throw new DataStoreException(e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				LOGGER.error("Error while closing the inputstream");
			}
		}

		return document;
	}

	/**
	 * Same as readDocument(InputStream), but bypasses the IOException.
	 * 
	 * @param file
	 * @return
	 * @throws DataStoreException
	 */
	public static Document readDocument(File file) throws DataStoreException {
		try {
			return readDocument(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new DataStoreException(e);
		}
	}
}
