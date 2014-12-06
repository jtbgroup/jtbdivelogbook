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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import be.vds.jtbdive.client.util.ResourceManager;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.XMLValidationException;

public class ImpExUDDFHandler {

	private File file;
	public static final String V_2_2_0 = "2.2.0";
	public static final String V_3_0_0 = "3.0.0";
	public static final String V_3_0_1 = "3.0.1";

	public ImpExUDDFHandler(File file) {
		this.file = file;
	}

	public LogBook read(String version) throws DataStoreException,
			XMLValidationException {
		if (version.equals(V_2_2_0)) {
			return read220();
		} else if (version.equals(V_3_0_0)) {
			return read3x("uddf_3.0.0.xsd");
		} else if (version.equals(V_3_0_1)) {
			return read3x("uddf_3.0.1.xsd");
		}
		return null;
	}

	private LogBook read3x(String xsdFileName) throws DataStoreException,
			XMLValidationException {
		try {
			UDDFV300Parser parser = new UDDFV300Parser();
			InputStream xsd = ResourceManager.getInstance()
					.getResourceAsInputStream("resources/xsd/" + xsdFileName);
			FileInputStream fis = new FileInputStream(file);
			LogBook lb = parser.read(fis, xsd);
			fis.close();
			return lb;
		} catch (FileNotFoundException e) {
			throw new DataStoreException(e);
		} catch (IOException e) {
			throw new DataStoreException(e);
		}
	}

	public void write(List<Dive> dives, Diver owner, String version)
			throws IOException, XMLValidationException {
		if (version.equals(V_2_2_0)) {
			write220(dives, owner);
		} else if (version.equals(V_3_0_0)) {
			write300(dives, owner);
		} else if (version.equals(V_3_0_1)) {
			write301(dives, owner);
		}
	}

	private LogBook read220() throws DataStoreException {
		UDDFV220Parser parser = new UDDFV220Parser();
		try {
			return parser.read(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new DataStoreException(e);
		}
	}

	private void write220(List<Dive> dives, Diver owner) throws IOException,
			XMLValidationException {
		UDDFV220Parser parser = new UDDFV220Parser();
		parser.write(dives, owner, new FileOutputStream(file), null);
	}

	private void write300(List<Dive> dives, Diver owner) throws IOException,
			XMLValidationException {

		InputStream s = ResourceManager.getInstance().getResourceAsInputStream(
				"resources/xsd/uddf_3.0.0.xsd");
		// Currently don't use xsd because it gives problems

		UDDFV300Parser parser = new UDDFV300Parser();
		parser.write(dives, owner, new FileOutputStream(file), s);
	}

	private void write301(List<Dive> dives, Diver owner) throws IOException,
			XMLValidationException {

		InputStream s = ResourceManager.getInstance().getResourceAsInputStream(
				"resources/xsd/uddf_3.0.1.xsd");
		// Currently don't use xsd because it gives problems

		UDDFV301Parser parser = new UDDFV301Parser();
		parser.write(dives, owner, new FileOutputStream(file), s);
	}

	public static String getUDDFVersion(File file) {
		try {
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(new FileInputStream(file));
			String v = doc.getRootElement().getAttributeValue("version");
			return v;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
