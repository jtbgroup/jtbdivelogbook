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
import java.util.List;

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.XMLValidationException;

public class ImpExUDCFHandler {

	private File file;

	public ImpExUDCFHandler(File file) {
		this.file = file;
	}

	public LogBook read() throws DataStoreException {
		UDCFParser parser = new UDCFParser();
		try {
			return parser.read(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new DataStoreException(e);
		}
	}

	public void write(List<Dive> dives, Diver owner) throws IOException,
			XMLValidationException {
		// Currently don't use xsd because it gives problems

		UDCFParser parser = new UDCFParser();
		parser.write(dives, owner, new FileOutputStream(file), null);
	}
}
