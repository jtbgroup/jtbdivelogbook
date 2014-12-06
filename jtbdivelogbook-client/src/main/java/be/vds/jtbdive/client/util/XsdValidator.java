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
package be.vds.jtbdive.client.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import be.vds.jtbdive.core.logging.Syslog;

public class XsdValidator {

	protected static final Syslog LOGGER = Syslog.getLogger(XsdValidator.class);

	public static boolean validate(InputStream xmlIs, File xsdSchema)
			throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			factory.setValidating(true);
			factory.setAttribute(
					"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
					"http://www.w3.org/2001/XMLSchema");
			factory.setAttribute(
					"http://java.sun.com/xml/jaxp/properties/schemaSource",
					xsdSchema.getAbsolutePath());
			factory.setNamespaceAware(true);
			factory.setValidating(true);
			
			DocumentBuilder parser = factory.newDocumentBuilder();
			parser.setErrorHandler(new DefaultHandler() {
				public void error(SAXParseException e) throws SAXException {
					System.out.println("error: " + e.getMessage());
				}
			});
			parser.parse(xmlIs);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean validate(Document doc, URL xsdSchema)
			throws SAXException, IOException, ParserConfigurationException {

		SchemaFactory schemaFactory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(xsdSchema);
		Validator validator = getValidator(schema);

		LOGGER.debug(" dom validation...");
		validator.validate(new DOMSource(doc));
		LOGGER.debug(" ...passed");

		return true;
	}

	private static Validator getValidator(Schema schema) {
		Validator validator = schema.newValidator();
		validator.setErrorHandler(new DefaultHandler() {
			public void error(SAXParseException e) throws SAXException {
				System.out.println("error: " + e.getMessage());
				throw e;
			}
		});
		return validator;
	}

	public static void main(String[] args) {
		try {
//			FileInputStream xml = new FileInputStream(
//					new File(
//							"/home/gautier/workspace/WS-JTBDiveLogBook/jtbdivelogbook-client/src/main/resources/resources/xsd/aaa.udcf"));
//			File xsd = new File(
//					"/home/gautier/workspace/WS-JTBDiveLogBook/jtbdivelogbook-client/src/main/resources/resources/xsd/udcf.xsd");
			
			FileInputStream xml = new FileInputStream(
					new File(
							"c:/temp/text.xml"));
			File xsd = new File(
					"C:/Local/workspaces/ws_jtbdivelogbook/jtbdivelogbook-client/src/main/resources/resources/xsd/udcf.xsd");

			System.out.println("Validation is " + validate(xml, xsd));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static boolean validXML(org.jdom.Document jDomDocument,
			InputStream xsdName) {
		String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
		String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
		String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

		try {
			Format format = Format.getRawFormat();
			format.setEncoding("UTF-8");
			XMLOutputter output = new XMLOutputter(format);
			String str = output.outputString(jDomDocument);
			str = new String(str.getBytes(), "UTF-8");
			
			byte currentXMLBytes[] = str.getBytes();
			ByteArrayInputStream _xmlFile = new ByteArrayInputStream(
					currentXMLBytes);

			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			spf.setValidating(true);
			SAXParser sp = spf.newSAXParser();
			sp.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
			sp.setProperty(JAXP_SCHEMA_SOURCE, xsdName);
			sp.parse(_xmlFile, new DefaultHandler() {

				public void fatalError(SAXParseException e)
						throws SAXParseException {
					LOGGER.fatal(e.getMessage());
					throw e;
				}

				public void error(SAXParseException e) throws SAXParseException {
					LOGGER.error(e.getMessage());
					throw e;
				}

				public void warning(SAXParseException e)
						throws SAXParseException {
					LOGGER.warn(e.getMessage());
					throw e;
				}
			});
		} catch (SAXNotRecognizedException e) {
			LOGGER.warn(e.getMessage());
			return false;
		} catch (SAXNotSupportedException e) {
			LOGGER.warn(e.getMessage());
			return false;
		} catch (SAXException e) {
			LOGGER.warn(e.getMessage());
			return false;
		} catch (ParserConfigurationException e) {
			LOGGER.warn(e.getMessage());
			return false;
		} catch (IOException e) {
			LOGGER.warn(e.getMessage());
			return false;
		}

		LOGGER.info("XML file is valid according to the schema");
		return true;
	}

	public static boolean validXML(InputStream is, String xsdFilePath)
			throws IOException, ParserConfigurationException {
		try {
			SchemaFactory factory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			File schemaLocation = new File(xsdFilePath);
			Schema schema = factory.newSchema(schemaLocation);
			Validator validator = schema.newValidator();

			DocumentBuilderFactory domFactory = DocumentBuilderFactory
					.newInstance();
			domFactory.setNamespaceAware(true); // never forget this
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(is);

			DOMSource source = new DOMSource(doc);
			DOMResult result = new DOMResult();

			validator.validate(source, result);
//			Document augmented = (Document) result.getNode();
			// do whatever you need to do with the augmented document...
			return true;
		} catch (SAXException ex) {
			System.out.println(xsdFilePath + " is not valid because ");
			System.out.println(ex.getMessage());
		}
		return false;
	}

}
