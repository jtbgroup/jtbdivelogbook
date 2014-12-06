/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: Settings.java
 * 
 * @author Andr&eacute; Schenk <andre@melior.s.bawue.de>
 * 
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package be.vds.jtbdive.core.core.divecomputer.uwatec;

/**
 * Description: container for the "settings" section of an Aladin log file
 * 
 * @author Andr&eacute; Schenk
 * @version $Revision: 1.1 $
 */
public class UwatecSettings {
	private static final short TOTAL_BYTES_SEQUENCE_LENGTH = 52;
	private static final short BYTE_INDEX_SETTINGS = 22;
	private static final short BYTE_INDEX_MAX_PRESSURE = 23;
	private static final short BYTE_INDEX_RESERVE_PRESSURE = 34;
	private static final short BYTE_INDEX_SENSITIVITY = 47;
	private static final int BYTE_INDEX_SERIAL_NUMBER_1 = 51;
	private static final int BYTE_INDEX_SERIAL_NUMBER_2 = 50;
	private static final int BYTE_INDEX_SERIAL_NUMBER_3 = 49;
	private final int aladinType;
	private final int settings;
	private final int maximumPressure;
	private final int reservePressure;
	private final int sensitivity;
	private final int serialNumber;

	public UwatecSettings(int[] bytes) {
		if ((bytes == null) || (bytes.length < TOTAL_BYTES_SEQUENCE_LENGTH)) {
			throw new IllegalArgumentException("need 52 bytes for settings");
		}
		aladinType = bytes[0];
		settings = bytes[BYTE_INDEX_SETTINGS];
		maximumPressure = bytes[BYTE_INDEX_MAX_PRESSURE] * 1000 / 20 + 1200;
		reservePressure = bytes[BYTE_INDEX_RESERVE_PRESSURE];
		sensitivity = bytes[BYTE_INDEX_SENSITIVITY];
		serialNumber = bytes[BYTE_INDEX_SERIAL_NUMBER_1] * 65536
				+ bytes[BYTE_INDEX_SERIAL_NUMBER_2] * 256
				+ bytes[BYTE_INDEX_SERIAL_NUMBER_3];
	}

	public int getAladinType() {
		return aladinType;
	}

	private String getAladinTypeName() {
		switch (aladinType) {
		case 0x40:
			return "Mares Genius";
		case 0x34:
			return "Aladin Air Z";
		case 0x44:
			return "Aladin Air Z";
		case 0xa4:
			return "Aladin Air Z O2";
		case 0xf4:
			return "Aladin Air Z Nitrox";
		case 0x48:
			return "Spiro Monitor 3 Air";
		case 0x1c:
			return "Aladin Air Twin";
		case 0x1d:
			return "Spiro Monitor 2 Plus";
		case 0x3d:
			return "Spiro Monitor 2 Plus";
		case 0x1e:
			return "Aladin Sport Plus";
		case 0x3e:
			return "Aladin Sport Plus";
		case 0x1f:
			return "Aladin Pro";
		case 0x3f:
			return "Aladin Pro";
		case 0xff:
			return "Aladin Pro Ultra";
		case 0x1b:
			return "AIRE (Aladin Pro)";
		default:
			return "unknown";
		}
	}

	private int getSensitivity() {
		switch (sensitivity) {
		case 0x19:
			return -12;
		case 0x1b:
			return -11;
		case 0x1d:
			return -10;
		case 0x1f:
			return -9;
		case 0x21:
			return -8;
		case 0x23:
			return -7;
		case 0x25:
			return -6;
		case 0x27:
			return -5;
		case 0x2a:
			return -4;
		case 0x2c:
			return -3;
		case 0x2f:
			return -2;
		case 0x31:
			return -1;
		case 0x34:
			return 0;
		case 0x37:
			return 1;
		case 0x3a:
			return 2;
		case 0x3d:
			return 3;
		case 0x41:
			return 4;
		case 0x44:
			return 5;
		case 0x48:
			return 6;
		case 0x4c:
			return 7;
		case 0x50:
			return 8;
		case 0x54:
			return 9;
		case 0x58:
			return 10;
		case 0x5c:
			return 11;
		case 0x61:
			return 12;
		default:
			return 0;
		}
	}

	public String toString() {
		StringBuffer result = new StringBuffer();

		result.append("Aladin type: " + getAladinTypeName() + "\n");
		result.append("BEEP " + (((settings & 2) != 0) ? "on" : "off"));
		result.append("\n");
		result.append((((settings & 1) != 0) ? "imperial" : "metric"));
		result.append(" unit\n");
		result.append("maximum O2 partial pressure: " + maximumPressure
				+ " mbar\n");
		result.append("reserve pressure: " + reservePressure + " bar\n");
		result.append("sensitivity: " + getSensitivity() + "\n");
		result.append("serial number: #" + serialNumber + "\n");
		return result.toString();
	}

	public int getSerialNumber() {
		return serialNumber;
	}

}
