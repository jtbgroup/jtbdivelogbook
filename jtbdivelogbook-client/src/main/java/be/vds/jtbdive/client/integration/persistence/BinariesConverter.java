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
package be.vds.jtbdive.client.integration.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import be.vds.jtbdive.core.core.divecomputer.parser.ComputerDataParser;

public class BinariesConverter {

	public void convert(File fIn, File fOut) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fIn));
		Document document = new Document(createRoot(reader));
		FileOutputStream os = new FileOutputStream(fOut);
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		outputter.output(document, os);
		os.close();
	}

	private Element createRoot(BufferedReader reader) throws IOException {
		Element root = new Element("binaries");
		while (reader.ready()) {
			String line1 = reader.readLine();
			String line2 = reader.readLine();
			String[] data = line1.split("-");
			Element entryEl = new Element("entry");
			entryEl.setAttribute("date", data[0]);
			Element parserEl = new Element("parser");
			parserEl.setText(getParserName(data[1]));
			entryEl.addContent(parserEl);
			Element dataEl = new Element("data");
			dataEl.setText(convertBytes(line2));
			entryEl.addContent(dataEl);
			root.addContent(entryEl);
		}
		return root;
	}

	private String convertBytes(String line) {
		String[] bytesString = line.split(";");
		int[] bytes = new int[bytesString.length];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = Integer.parseInt(bytesString[i]);
		}

		reorder_bytes(bytes, bytes.length);

		StringBuilder sb = new StringBuilder();
		for (int i : bytes) {
			sb.append(i).append(";");
		}
		return sb.substring(0, sb.length() - 1);
	}

	private int[] reorder_bytes(int[] buf, int len) {
		int j;

		for (int i = 0; i < len; i++) {
			j = (buf[i] & 0x01) << 7;
			j += (buf[i] & 0x02) << 5;
			j += (buf[i] & 0x04) << 3;
			j += (buf[i] & 0x08) << 1;
			j += (buf[i] & 0x10) >> 1;
			j += (buf[i] & 0x20) >> 3;
			j += (buf[i] & 0x40) >> 5;
			j += (buf[i] & 0x80) >> 7;
			buf[i] = j;
		}
		return buf;
	}

	private String getParserName(String string) {
		if (string.toLowerCase().contains("uwatec"))
			return ComputerDataParser.UWATEC_PARSER.getParserName();
		return "";
	}

}
