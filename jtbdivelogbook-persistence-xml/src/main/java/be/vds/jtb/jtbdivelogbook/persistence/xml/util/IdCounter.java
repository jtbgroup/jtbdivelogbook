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
package be.vds.jtb.jtbdivelogbook.persistence.xml.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.xml.utils.XMLUtils;

/**
 * Counter in the keys container is the next ID available.
 * 
 * @author Vanderslyen.G
 * 
 */
public class IdCounter {

	private static final Syslog LOGGER = Syslog.getLogger(IdCounter.class);
	private static IdCounter instance;
	private String basePath;

	// private Document surrogatesdocument;

	private IdCounter() {
	}

	public static IdCounter getInstance() {
		if (null == instance)
			instance = new IdCounter();
		return instance;
	}

	public void setBasePath(String basePath) throws DataStoreException {
		this.basePath = basePath;
		// initialize();
	}

	// private void initialize() throws DataStoreException {
	//
	// File f = new File(basePath + File.separatorChar + "surrogates.xml");
	// if (!f.exists()) {
	// try {
	// f.createNewFile();
	// } catch (IOException e1) {
	// LOGGER.error(e1);
	// }
	// }
	//
	// try {
	// SAXBuilder sb = new SAXBuilder();
	// surrogatesdocument = sb.build(new FileInputStream(f));
	// } catch (JDOMException e) {
	// surrogatesdocument = new Document();
	// LOGGER.error("Error while loading the surrogate keys. Creating a new XML document.",
	// e);
	// throw new
	// DataStoreException("Error while loading the surrogate keys. Creating a new XML document.",
	// e);
	// } catch (IOException e) {
	// LOGGER.error(e);
	// throw new DataStoreException(e);
	// }
	// }

	private Element getRootElement() throws DataStoreException {
		File f = new File(basePath + File.separatorChar + "surrogates.xml");
		try {
			SAXBuilder sb = new SAXBuilder();
			return sb.build(new FileInputStream(f)).getRootElement();
		} catch (JDOMException e) {
			LOGGER.error(
					"Error while loading the surrogate keys. Creating a new XML document.",
					e);
			throw new DataStoreException(
					"Error while loading the surrogate keys. Creating a new XML document.",
					e);
		} catch (IOException e) {
			LOGGER.error(e);
			throw new DataStoreException(e);
		}
	}

	public String getCurrentId(String key) throws DataStoreException {
		Element element = getRootElement().getChild(key);
		if (null == element) {
			return null;
		} else {
			return element.getText();
		}

	}

	public void setCurrentId(String key, long id) throws DataStoreException {
		Element root = getRootElement();
		Element element = root.getChild(key);
		if (null == element) {
			element = new Element(key);
			root.addContent(element);
		}
		element.setText(String.valueOf(id));

		try {
			writeDocument(root);
		} catch (IOException e) {
			throw new DataStoreException(e);
		}
	}

	/**
	 * Gets the ID stored in the surrogates container and increments the key in
	 * the container.
	 * 
	 * @param key
	 * @return
	 * @throws DataStoreException
	 */
	public long getNextId(String key) throws DataStoreException {
		Element root = getRootElement();
		Element element = root.getChild(key);

		long id = -1;
		if (null == element) {
			element = new Element(key);
			element.setText("1");
			root.addContent(element);
		}

		id = Long.parseLong(element.getText());
		element.setText(String.valueOf(id + 1));

		try {
			writeDocument(root);
		} catch (IOException e) {
			throw new DataStoreException(e);
		}
		return id;
	}

	private void writeDocument(Element root) throws IOException {
		OutputStream os = new FileOutputStream(
				(basePath + File.separatorChar + "surrogates.xml"));
		// os.close();
		Document document = new Document((Element) root.clone());
		XMLUtils.writeDocument(os, document);
	}

}
