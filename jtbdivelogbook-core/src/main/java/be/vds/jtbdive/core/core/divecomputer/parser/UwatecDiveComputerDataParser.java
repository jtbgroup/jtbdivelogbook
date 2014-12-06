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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.core.comparator.DiveDateComparator;
import be.vds.jtbdive.core.core.divecomputer.uwatec.UwatecData;
import be.vds.jtbdive.core.core.divecomputer.uwatec.UwatecDiveComputerAdapter;
import be.vds.jtbdive.core.core.divecomputer.uwatec.UwatecLogEntry;
import be.vds.jtbdive.core.exceptions.TransferException;
import be.vds.jtbdive.core.interfaces.DataCommInterface;

/**
 * serial parameters are 19200 baud, 8 bits, No parity and 1 stopbit
 */
public class UwatecDiveComputerDataParser implements DiveComputerDataParser {

	private static final Logger LOGGER = Logger
			.getLogger(UwatecDiveComputerDataParser.class);
	protected static final long TIME_OUT_DURATION = 15000;
	private DataCommInterface dataComInterface;
	private int[] binaries;
	private boolean timeOutReached;


	/**
	 * A new version of the read method based on a thread for calculating the
	 * timeout.
	 * 
	 * @return
	 * @throws TransferException
	 */
	public List<Dive> read(MatCave matCave) throws TransferException {
		List<Dive> result = null;
		try {
			timeOutReached = false;
			LOGGER.debug("opening port");
			dataComInterface.open();
			LOGGER.debug("port opened");

			int[] readData = new int[2046];
			binaries = new int[2046];
			InputStream serialInputStream = dataComInterface.getInputStream();
			launchTimer();
			int byteRead = 0;
			int i = 0;
			boolean started = false;
			while (!started && !timeOutReached) {
				try {
					byteRead = serialInputStream.read();
				} catch (IOException e) {
					timeOutReached = true;
				}

				if (byteRead != 0) {
					LOGGER.debug("no begining found " + byteRead);
				}
				if (i < 3) {
					if (byteRead == 85) {
						i++;
					} else {
						i = 0;
					}
				} else if (byteRead == 0) {
					i++;
					started = true;
				} else {
					i = 0;
				}
			}
			LOGGER.info("end of loop");

			if (timeOutReached) {
				LOGGER.error("timeout reached... close all");
				throw new TransferException("Time out reached");
			} else {
				for (int j = 0; j < readData.length; j++) {
					if (timeOutReached) {
						LOGGER.error("timeout reached... close all");
						throw new TransferException("Time out reached");
					}
					byteRead = serialInputStream.read();
					readData[j] = byteRead;
					binaries[j] = byteRead;
					LOGGER.debug("index " + j + " - " + byteRead);
				}

				result = convertBinaries(readData, matCave);
				LOGGER.info("reading finished");
			}
		} catch (IOException e) {
			LOGGER.error("IO exception while reading the stream", e);
			throw new TransferException(
					"IO exception while reading the stream", e);
		} finally {
			try {
				dataComInterface.close();
			} catch (Exception e) {
				LOGGER.error("Error cloding the data com interface");
			}
		}
		return result;

	}


	private void launchTimer() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				LOGGER.debug("timer started...");
				try {
					Thread.sleep(TIME_OUT_DURATION);
				} catch (InterruptedException e) {
					LOGGER.error(e);
				} finally {
					timeOutReached = true;
					LOGGER.debug("timer reached end");
				}
			}
		}).start();
	}

	private void checkSum(int[] readData) {
		int checksum = readData[2045] * 256 + readData[2044];
		int sum = 0x1fe;
		for (int i = 0; i < readData.length - 2; i++) {
			sum += readData[i];
		}

		sum = sum % 65536;
		if (sum != checksum) {
			LOGGER.error("Checksum is not good !!! Be careful");
		} else {
			LOGGER.info("Checksum is correct");
		}
	}

	private int[] reorderBytes(int[] buf, int len) {
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

	@Override
	public void setDataComInterface(DataCommInterface dataComInterface) {
		this.dataComInterface = dataComInterface;
	}

	@Override
	public int[] getBinaries() {
		return binaries;
	}

	@Override
	public List<Dive> convertBinaries(int[] binaries, MatCave matCave) throws TransferException {
		int[] originalBinaries = Arrays.copyOf(binaries, binaries.length);
		int[] reorderedBinaries = reorderBytes(originalBinaries, originalBinaries.length);
		checkSum(reorderedBinaries);

		UwatecData aladinData = new UwatecData(reorderedBinaries);
		UwatecDiveComputerAdapter adapter = new UwatecDiveComputerAdapter();
		List<Dive> dives = new ArrayList<Dive>();

		// adding all the log book from aladin
		for (UwatecLogEntry logEntry : aladinData.getLogbook()) {
			dives.add(adapter.adapt(logEntry));
		}

		Collections.sort(dives, new DiveDateComparator());

		adapter.addUwatecProfileToDives(dives, aladinData, matCave);

		return dives;
	}

	@Override
	public void close() {
		try {
			dataComInterface.close();
		} catch (TransferException e) {
			LOGGER.error(e);
		}
	}
}
