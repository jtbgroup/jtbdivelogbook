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
package be.vds.jtbdive.client.interfaces;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.vds.jtbdive.core.exceptions.TransferException;
import be.vds.jtbdive.core.interfaces.DataCommInterface;
import be.vds.jtbdive.core.interfaces.DataCommInterfaceType;
import be.vds.jtbdive.core.logging.Syslog;

public class SerialDataCommInterface implements DataCommInterface {
	private static final Syslog LOGGER = Syslog
			.getLogger(SerialDataCommInterface.class);
	private int stopBits;
	private int parity;
	private int dataBits;
	private int baudRate;
	private String port;

	/**
	 * The serialPort is used only when opening the port.
	 */
	private SerialPort serialPort;
	public static final Integer PARITY_ODD = SerialPort.PARITY_ODD;
	public static final Integer PARITY_EVEN = SerialPort.PARITY_EVEN;
	public static final Integer PARITY_MARK = SerialPort.PARITY_MARK;
	public static final Integer PARITY_NONE = SerialPort.PARITY_NONE;
	public static final Integer PARITY_SPACE = SerialPort.PARITY_SPACE;
	public static final Map<Integer, String> PARITY;
	static {
		PARITY = new HashMap<Integer, String>();
		PARITY.put(PARITY_ODD, "Odd");
		PARITY.put(PARITY_EVEN, "Even");
		PARITY.put(PARITY_MARK, "Mark");
		PARITY.put(PARITY_NONE, "None");
		PARITY.put(PARITY_SPACE, "Space");
	}
	public static final Integer STOP_BIT_1 = SerialPort.STOPBITS_1;
	public static final Integer STOP_BIT_1_5 = SerialPort.STOPBITS_1_5;
	public static final Integer STOP_BIT_2 = SerialPort.STOPBITS_2;
	public static final Map<Integer, String> STOP_BITS;
	static {
		STOP_BITS = new HashMap<Integer, String>();
		STOP_BITS.put(STOP_BIT_1, "1");
		STOP_BITS.put(STOP_BIT_1_5, "1,5");
		STOP_BITS.put(STOP_BIT_2, "2");
	}
	public static final Integer DATA_BITS_5 = SerialPort.DATABITS_5;
	public static final Integer DATA_BITS_6 = SerialPort.DATABITS_6;
	public static final Integer DATA_BITS_7 = SerialPort.DATABITS_7;
	public static final Integer DATA_BITS_8 = SerialPort.DATABITS_8;
	public static final Map<Integer, String> DATA_BITS;
	static {
		DATA_BITS = new HashMap<Integer, String>();
		DATA_BITS.put(DATA_BITS_5, "5");
		DATA_BITS.put(DATA_BITS_6, "6");
		DATA_BITS.put(DATA_BITS_7, "7");
		DATA_BITS.put(DATA_BITS_8, "8");
	}

	public static final int BAUD_RATE_2400 = 2400;
	public static final int BAUD_RATE_4800 = 4800;
	public static final int BAUD_RATE_7200 = 7200;
	public static final int BAUD_RATE_9600 = 9600;
	public static final int BAUD_RATE_14400 = 14400;
	public static final int BAUD_RATE_19200 = 19200;
	public static final List<Integer> BAUD_RATE;
	static {
		BAUD_RATE = new ArrayList<Integer>();
		BAUD_RATE.add(BAUD_RATE_2400);
		BAUD_RATE.add(BAUD_RATE_4800);
		BAUD_RATE.add(BAUD_RATE_7200);
		BAUD_RATE.add(BAUD_RATE_9600);
		BAUD_RATE.add(BAUD_RATE_14400);
		BAUD_RATE.add(BAUD_RATE_19200);
	}

	public SerialDataCommInterface() {
	}

	public SerialDataCommInterface(String port, int baudRate, int dataBits,
			int parity, int stopBits) {
		this.port = port;
		this.baudRate = baudRate;
		this.dataBits = dataBits;
		this.parity = parity;
		this.stopBits = stopBits;
	}

	public static String getParity(Integer integer) {
		return PARITY.get(integer);
	}

	public static String getStopBit(Integer integer) {
		return STOP_BITS.get(integer);
	}

	public static String getDataBit(Integer integer) {
		return DATA_BITS.get(integer);
	}

	private boolean isClosed = true;

	public boolean open() throws TransferException {

		boolean bOk = false;
		try {
			// String drivername = "com.sun.comm.Win32Driver";
			// try {
			// CommDriver driver = (CommDriver) Class.forName(drivername)
			// .newInstance();
			// driver.initialize();
			// } catch (Exception e) {
			// logger.warn("Driver " + drivername + " not found.");
			// }

			// Recuperation de l'identifiant du port
			CommPortIdentifier portId = CommPortIdentifier
					.getPortIdentifier(port);

			// Ouverture et configuration
			serialPort = (SerialPort) portId.open("Jtb Dive", 10000);
			LOGGER.debug("port " + serialPort.getName() + " opened");

			serialPort
					.setSerialPortParams(baudRate, dataBits, stopBits, parity);
			LOGGER.debug("port " + serialPort.getName() + " configured");

			bOk = true;

			serialPort.setDTR(true);
			serialPort.setRTS(false);
			isClosed = false;
			LOGGER.debug("Serial Interface opened");
		} catch (NoSuchPortException e) {
			LOGGER.warn("no such port : " + e.getMessage());
			throw new TransferException("no such port : " + port);
		} catch (PortInUseException e) {
			LOGGER.warn("port in use: " + e.getMessage());
			throw new TransferException("port in use");
		} catch (UnsupportedCommOperationException e) {
			LOGGER.warn("unsupported operation : " + e.getMessage());
			throw new TransferException("unsupported operation");
		}
		return bOk;
	}

	public int getBaudRate() {
		return baudRate;
	}

	public int getDataBits() {
		return dataBits;
	}

	public int getParity() {
		return parity;
	}

	public String getPort() {
		return port;
	}

	public int getStopBits() {
		return stopBits;
	}

	public InputStream getInputStream() throws IOException {
		return serialPort.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		return serialPort.getOutputStream();
	}

	public SerialPort getSerialPort() {
		return serialPort;
	}

	public boolean close() throws TransferException {
		try {
			if (serialPort != null) {
				serialPort.setDTR(false);
				serialPort.getInputStream().close();
				serialPort.getOutputStream().close();
				serialPort.close();
				isClosed = true;
			}
			LOGGER.debug("Serial Interface closed");
			return isClosed;
		} catch (IOException e) {
			LOGGER.warn("unsupported operation : " + e.getMessage());
			throw new TransferException("error closing the port");
		}
	}

	public boolean isClosed() {
		return isClosed;
	}

	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}

	public void setDataBits(int dataBits) {
		this.dataBits = dataBits;
	}

	public void setParity(int parity) {
		this.parity = parity;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setStopBits(int stopBits) {
		this.stopBits = stopBits;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("SerialDataComInterface : ").append(port);
		sb.append(" (").append(baudRate).append(" - ");
		sb.append(dataBits).append(" - ");
		sb.append(parity).append(" - ");
		sb.append(stopBits).append(")");
		return sb.toString();
	}

	public static List<String> getAllPorts() {
		Set<String> set = new HashSet<String>();
		Enumeration<?> commPorts = null;

		if (isInstalled()){
			commPorts = CommPortIdentifier.getPortIdentifiers();
		}else{
			LOGGER.error("RXTX library seems not present in the path!");
		}

		if (null != commPorts) {
			while (commPorts.hasMoreElements()) {
				CommPortIdentifier portId = (CommPortIdentifier) commPorts
						.nextElement();
				set.add(portId.getName());
			}
		}

		List<String> result = new ArrayList<String>();
		result.addAll(set);
		Collections.sort(result);
		return result;
	}

	public static boolean isInstalled() {
		// String f = new String(PropertiesManager.getJREUsed() + "/bin");
		// if (PropertiesManager.getOSType().equals(
		// SystemProperties.WINDOWS_OS_TYPE)) {
		// return new File(f + "/rxtxSerial.dll").exists();
		// }
		try {
			Runtime.getRuntime().loadLibrary("rxtxSerial");
		} catch (Error e) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws IOException {
		// SerialDataCommInterface dsdi = new SerialDataCommInterface();
		// dsdi.setPort("/dev/ttyUSB0");
		// dsdi.setBaudRate(BAUD_RATE_9600);
		// dsdi.setDataBits(DATA_BITS_8);
		// dsdi.setParity(PARITY_NONE);
		// dsdi.setStopBits(STOP_BIT_1);
		// try {
		// dsdi.open();
		// } catch (TransferException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// InputStream is = dsdi.getInputStream();
		//
		// int len = 0;
		// try {
		// System.out.println("start reading");
		// while (len > -1) {
		// len = is.read();
		// System.out.print(len);
		// }
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		System.out.println(SerialDataCommInterface.isInstalled());
	}

	@Override
	public DataCommInterfaceType getDataCommInterfaceType() {
		return DataCommInterfaceType.SERIAL;
	}
}
