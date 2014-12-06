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
package be.vds.jtbdive.core.core.divecomputer.parser;

import java.util.List;

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.exceptions.TransferException;
import be.vds.jtbdive.core.interfaces.DataCommInterface;

public interface DiveComputerDataParser {

	void setDataComInterface(DataCommInterface dataComInterface);

	List<Dive> read(MatCave matCave) throws TransferException;

	List<Dive> convertBinaries(int[] binaries, MatCave matCave)
			throws TransferException;

	/**
	 * Tells the parser if the data to read are ordered or not
	 * 
	 * @param b
	 *            true if the data are ordered
	 */
	int[] getBinaries();

	void close();
}
