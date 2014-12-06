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
import java.text.SimpleDateFormat;
import java.util.List;

import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.core.divecomputer.binary.BinaryLogBook;
import be.vds.jtbdive.xml.parsers.BinaryDataFileParser;

/**
 * This parser is written to deal with UDDF standard V2.2.0. To see which tags
 * are filled, please refer to the write method.s
 * 
 * @author gautier
 */
public class ImpExBinaryHandler {

	private File file;
	private static final SimpleDateFormat sdf = new SimpleDateFormat();

	public ImpExBinaryHandler(File file) {
		this.file = file;
	}

	public List<BinaryLogBook> getBinaryLogBooks() {
		List<BinaryLogBook> list = null;
		try {
			list = new BinaryDataFileParser().read(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public LogBook convertToLogBook(BinaryLogBook binaryLogBook, MatCave matCave) {
		if (binaryLogBook == null)
			return null;
		LogBook lb = new LogBook();
		lb.setDives(binaryLogBook.loadDives(matCave));
		lb.setName(binaryLogBook.getParser().getParserName() + " - "
				+ sdf.format(binaryLogBook.getDate()));
		return lb;
	}

}
