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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import be.vds.jtbdive.core.exceptions.TransferException;
import be.vds.jtbdive.core.interfaces.DataCommInterface;
import be.vds.jtbdive.core.interfaces.DataCommInterfaceType;

public class UsbDataComInterface implements DataCommInterface {

	public boolean close() throws TransferException {
		return false;
	}

	public InputStream getInputStream() throws IOException {
		return null;
	}

	public OutputStream getOutputStream() throws IOException {
		return null;
	}

	public boolean isClosed() {
		return false;
	}

	public boolean open() throws TransferException {
		return false;
	}

	@Override
	public DataCommInterfaceType getDataCommInterfaceType() {
		return null;
	}
}
