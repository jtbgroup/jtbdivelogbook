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
package be.vds.jtbdive.core.core.divecomputer.binary;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.core.divecomputer.parser.ComputerDataParser;
import be.vds.jtbdive.core.core.divecomputer.parser.DiveComputerDataParser;
import be.vds.jtbdive.core.exceptions.TransferException;
import be.vds.jtbdive.core.logging.Syslog;

public class BinaryLogBook implements Comparable<BinaryLogBook> {

	private static final Syslog LOGGER = Syslog.getLogger(BinaryLogBook.class);
	private int[] binaries;
	private ComputerDataParser parser;
	private Date date;

	public ComputerDataParser getParser() {
		return parser;
	}

	public void setParser(ComputerDataParser parser) {
		this.parser = parser;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;

	}

	public void setBinaries(int[] binaries) {
		this.binaries = Arrays.copyOf(binaries, binaries.length);
	}

	public int[] getBinaries() {
		return binaries;
	}

	public List<Dive> loadDives(MatCave matCave) {
		try {
			int[] copy = Arrays.copyOf(binaries, binaries.length);
			return ((DiveComputerDataParser) (parser.getParserClass()
					.newInstance())).convertBinaries(copy, matCave);
		} catch (TransferException e) {
			LOGGER.error(e);
		} catch (InstantiationException e) {
			LOGGER.error(e);
		} catch (IllegalAccessException e) {
			LOGGER.error(e);
		}
		return null;
	}

	@Override
	public int compareTo(BinaryLogBook o) {
		if (o == null || o.getDate() == null){
			return 1;
		}
		return o.getDate().compareTo(getDate());
	}

}
