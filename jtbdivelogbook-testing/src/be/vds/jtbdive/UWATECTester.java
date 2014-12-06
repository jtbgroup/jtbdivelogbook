package be.vds.jtbdive;

import java.util.List;

import org.apache.log4j.BasicConfigurator;

import be.vds.jtbdive.client.interfaces.SerialDataCommInterface;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.divecomputer.parser.UwatecDiveComputerDataParser;
import be.vds.jtbdive.core.exceptions.TransferException;

public class UWATECTester {
	public static void main(String[] args) {
		BasicConfigurator.configure();
		SerialDataCommInterface.getAllPorts();
		SerialDataCommInterface dsdi = new SerialDataCommInterface();
		dsdi.setPort("/dev/ttyUSB0");
		dsdi.setBaudRate(SerialDataCommInterface.BAUD_RATE_19200);
		dsdi.setDataBits(SerialDataCommInterface.DATA_BITS_8);
		dsdi.setParity(SerialDataCommInterface.PARITY_NONE);
		dsdi.setStopBits(SerialDataCommInterface.STOP_BIT_1);
		UwatecDiveComputerDataParser p = new UwatecDiveComputerDataParser();
		p.setDataComInterface(dsdi);
		try {
//			System.out.println(p.read2());
			List<Dive> dives = p.read(null);
			System.out.println(dives.size());
		} catch (TransferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
