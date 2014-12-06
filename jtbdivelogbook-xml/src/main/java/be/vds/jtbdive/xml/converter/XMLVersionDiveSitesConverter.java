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
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import be.vds.jtbdive.core.core.modifications.PersistenceVersion;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.xml.utils.XMLUtils;

public class XMLVersionDiveSitesConverter extends XMLVersionConverter {
	private static final Syslog LOGGER = Syslog
			.getLogger(XMLVersionDiveSitesConverter.class);

	@Override
	public void convertToVersion_2_6_0(PersistenceVersion targetVersion,
			Document document) throws IOException {
		Element root = document.getRootElement();
		
		// Update tagNames
		if (root.getName().equals("divelocations")) {
			root.setName("divesites");
			LOGGER.debug("updated tags 'divelocations'");
		}
		int counter = 0;
		for (Iterator iterator = root.getChildren("divelocation").iterator(); iterator
				.hasNext();) {
			Element dlEl = (Element) iterator.next();
			dlEl.setName("divesite");
			counter++;
		}

		if (counter > 0) {
			LOGGER.debug("updated tags 'divelocation'");
		}

	}
	
	@Override
	public void convertToVersion_2_7_0(PersistenceVersion targetVersion,
			Document document) throws IOException {
		LOGGER.debug("Nothing to convert");
	}
}
