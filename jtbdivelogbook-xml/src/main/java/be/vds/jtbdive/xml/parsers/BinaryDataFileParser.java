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
package be.vds.jtbdive.xml.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import be.vds.jtbdive.core.core.divecomputer.binary.BinaryLogBook;
import be.vds.jtbdive.core.core.divecomputer.parser.ComputerDataParser;
import be.vds.jtbdive.core.logging.Syslog;

public class BinaryDataFileParser {

	private static final Syslog LOGGER = Syslog
			.getLogger(BinaryDataFileParser.class);

	@SuppressWarnings("unchecked")
	public List<BinaryLogBook> read(InputStream stream) {
		List<BinaryLogBook> result = new ArrayList<BinaryLogBook>();
		SAXBuilder sb = new SAXBuilder();
		try {
			Document document = sb.build(stream);
			Element root = document.getRootElement();
			for (Iterator<Element> iterator = root.getChildren("entry")
					.iterator(); iterator.hasNext();) {
				Element el = iterator.next();
				BinaryLogBook bl = new BinaryLogBook();
				long l = Long.parseLong(el.getAttributeValue("date"));
				bl.setDate(new Date(l));
				String[] numbers = el.getChildText("data").trim().split(";");
				int[] bytes = new int[numbers.length];
				for (int i = 0; i < bytes.length; i++) {
					bytes[i] = Integer.parseInt(numbers[i]);
				}
				bl.setBinaries(bytes);
				bl.setParser(ComputerDataParser.getParser(el
						.getChildText("parser")));
				result.add(bl);
			}
			stream.close();
		} catch (JDOMException e) {
			LOGGER.error(e);
		} catch (IOException e) {
			LOGGER.error(e);
		}
		Collections.sort(result);
		return result;
	}

	public void write(OutputStream os, List<BinaryLogBook> binaryLogBooks)
			throws IOException {
		Element root = new Element("binaries");

		for (BinaryLogBook binaryLogBook : binaryLogBooks) {
			Element entry = new Element("entry");
			entry.setAttribute("date", String.valueOf(new Date().getTime()));

			Element data = new Element("data");
			StringBuilder sb = new StringBuilder();
			for (int bit : binaryLogBook.getBinaries()) {
				sb.append(bit).append(";");
			}
			data.setText(sb.substring(0, sb.length() - 1));
			Element parser = new Element("parser");
			parser.setText(binaryLogBook.getParser().getParserName());

			entry.addContent(parser);
			entry.addContent(data);
			root.addContent(entry);
		}

		Document document = new Document(root);
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		try {
			outputter.output(document, os);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
