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

import org.jdom.Document;
import org.jdom.Element;

import be.vds.jtbdive.core.core.modifications.PersistenceVersion;
import be.vds.jtbdive.core.logging.Syslog;

public class XMLVersionDiversConverter extends XMLVersionConverter {
	private static final Syslog LOGGER = Syslog
			.getLogger(XMLVersionDiversConverter.class);

	@Override
	public void convertToVersion_2_6_0(PersistenceVersion targetVersion,
			Document document) throws IOException {
			LOGGER.debug("Nothing to convert");
	}

	@Override
	public void convertToVersion_2_7_0(PersistenceVersion targetVersion,
			Document document) throws IOException {
		LOGGER.debug("Nothing to convert");
	}

}
