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
package be.vds.jtbdive.client.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ConfigurationParser {

	/**
	 * Reads the configuration and return it but not yet initialized.
	 * 
	 * @param is
	 * @return
	 */
	
	public Configuration read(InputStream is) {
		Configuration c = null;
		try {
			SAXBuilder sb = new SAXBuilder();
			Document document = sb.build(is);
			c = readConfig(document.getRootElement());
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}

	private Configuration readConfig(Element rootElement) {
		Element configEl = rootElement.getChild("config");
		String configType = configEl.getAttributeValue("identifier");
		ConfigurationType ct = ConfigurationType
				.getConfigurationTypeForKey(configType);

		switch (ct) {
		case XML_CONF:
			return readXMLConfiguration(configEl);
		}

		return null;
	}

	private Configuration readXMLConfiguration(Element configEl) {
		Element cmEl = configEl.getChild("basepath");
		XMLConfiguration conf = new XMLConfiguration();
		conf.setBasePath(cmEl.getText());
		return conf;
	}

	//
	// private Configuration initJavaDbConfiguration(Element configEl) {
	// Properties properties = new Properties();
	// Element cmEl = configEl.getChild("connectionmanager");
	// for (Iterator iterator = cmEl.getChildren().iterator(); iterator
	// .hasNext();) {
	// Element propsEl = (Element) iterator.next();
	// properties.put(propsEl.getName(), propsEl.getText());
	// }
	//
	// ConnectionManager cm = new ConnectionManager(properties);
	// JavaDBJdbcConfiguration conf = new JavaDBJdbcConfiguration();
	// conf.setConnectionManager(cm);
	// return conf;
	// }
	//
	// private Configuration initMysqlJdbcConfiguration(Element configEl) {
	// Properties properties = new Properties();
	// Element cmEl = configEl.getChild("connectionmanager");
	// for (Iterator iterator = cmEl.getChildren().iterator(); iterator
	// .hasNext();) {
	// Element propsEl = (Element) iterator.next();
	// properties.put(propsEl.getName(), propsEl.getText());
	// }
	//
	// ConnectionManager cm = new ConnectionManager(properties);
	// MysqlJdbcConfiguration conf = new MysqlJdbcConfiguration();
	// conf.setConnectionManager(cm);
	// return conf;
	// }
	//
	// public void write(OutputStream os, Configuration configuration) {
	// Document document = new Document();
	// Element root = new Element("settings");
	// root.addContent(createConfig(configuration));
	// document.setRootElement(root);
	//
	// XMLOutputter outputter = new XMLOutputter();
	// outputter.setFormat(Format.getPrettyFormat());
	// try {
	// outputter.output(document, os);
	// os.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	//
	private Element createConfig(Configuration configuration) {
		ConfigurationType ct = configuration.getConfigurationType();
		Element el = new Element("config");
		el.setAttribute("identifier", ct.getKey());

		switch (ct) {
		case XML_CONF:
			createXMLConfig((XMLConfiguration) configuration, el);
			break;
		}
		return el;
	}

	private void createXMLConfig(XMLConfiguration configuration, Element el) {
		Element pathEl = new Element("basepath");
		pathEl.setText(configuration.getBasePath());
		el.addContent(pathEl);
	}

	//
	// private void createJdbcConfig(JdbcConfiguration configuration, Element
	// el) {
	// el.setAttribute("persistency", configuration.getConfigType());
	// Element connectionManagerEl = new Element("connectionmanager");
	// el.addContent(connectionManagerEl);
	//
	// Properties props = configuration.getConnectionManager().getProperties();
	//
	// String keyString = null;
	// Element element = null;
	// for (Object key : props.keySet()) {
	// keyString = (String) key;
	// element = new Element(keyString);
	// element.setText(props.getProperty(keyString));
	// connectionManagerEl.addContent(element);
	// }
	// }

	public void write(Configuration configuration, OutputStream os) {
		Document document = new Document();
		Element root = new Element("settings");
		root.addContent(createConfig(configuration));
		document.setRootElement(root);

		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		try {
			outputter.output(document, os);
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
