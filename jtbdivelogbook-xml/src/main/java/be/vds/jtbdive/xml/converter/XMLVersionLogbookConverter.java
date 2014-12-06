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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import be.vds.jtbdive.core.core.modifications.PersistenceVersion;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.xml.utils.XMLUtils;

public class XMLVersionLogbookConverter extends XMLVersionConverter {
	private static final Syslog LOGGER = Syslog
			.getLogger(XMLVersionLogbookConverter.class);

	@Override
	public void convertToVersion_2_7_0(PersistenceVersion targetVersion,
			Document document) throws IOException {
		Element root = document.getRootElement();
		
		Element matsEl = root.getChild("materials");
		if (matsEl != null) {
			matsEl.detach();
			Element matCaveEl = new Element("materialcave");
			matCaveEl.addContent(matsEl);
			root.addContent(matCaveEl);
		}
		
		Element el = root.getChild("materialcave");
		if(el != null){
			el = el.getChild("materials");
			for (Iterator iterator = el.getChildren("buoyancaycompensator").iterator(); iterator.hasNext();) {
				Element type = (Element) iterator.next();
				type.setName("buoyancycompensator");
				LOGGER.debug("Corrected a tag name (buoyancaycompensator)");
			}
		}
		

		LOGGER.debug("Integrated all materials in the matcave tag");
	}

	@Override
	public void convertToVersion_2_6_0(PersistenceVersion targetVersion,
			Document document) throws IOException {
		Element root = document.getRootElement();
		
		SimpleDateFormat FORMATTER_DATE = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		Element divesEl = root.getChild("dives");
		if (null != divesEl) {
			List elems = divesEl.getChildren("dive");
			for (Iterator iterator = elems.iterator(); iterator.hasNext();) {
				Element diveEl = (Element) iterator.next();

				// Upgrading divetime from minutes to seconds
				Element diveTimeEl = diveEl.getChild("divetime");
				if (null != diveTimeEl) {
					Long l = Long.parseLong(diveTimeEl.getText());
					diveTimeEl.setText(String.valueOf(l * 60));
				}

				// upgrading dive surface time from minutes to seconds
				Element surfaceTiemEl = diveEl.getChild("surfacetime");
				if (null != surfaceTiemEl) {
					Long l = Long.parseLong(surfaceTiemEl.getText());
					surfaceTiemEl.setText(String.valueOf(l * 60));
				}

				Element dateEl = diveEl.getChild("date");
				if (dateEl != null) {
					String dateString = dateEl.getText();
					Date date = null;
					if (dateString != null) {
						if (!dateString
								.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
							date = new Date(Long.parseLong(dateString));
							dateEl.setText(FORMATTER_DATE.format(date));
						}
					}
				}
			}
			LOGGER.debug("Converted all the divetimes and surfacetimes from minutes to seconds. Converted dive dates from milisecond timestamp to date format.");
		}

	}

}
