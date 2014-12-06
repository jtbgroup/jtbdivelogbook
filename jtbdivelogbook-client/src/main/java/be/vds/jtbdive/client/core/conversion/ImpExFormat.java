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

import java.util.ArrayList;
import java.util.List;

public enum ImpExFormat {

	JTB_FORMAT("jtb", "JtB Format", "jtb", true, true), //
	UDCF("udcf", "UDCF", "udcf", true, true), //
	UDDF_V220("uddf.v220", "UDDF V2.2.0", "uddf", true, false), //
	UDDF_V300("uddf.v300", "UDDF V3.0.0", "uddf", true, true), //
	UDDF_V301("uddf.v301", "UDDF V3.0.1", "uddf", true, true), //
	BINARIES_FORMAT("jtb.bin", "Binaries", "jtbb", true, false), //
	;
	private String name;
	private String extension;
	private String key;
	private boolean isExport;
	private boolean isImport;

	private ImpExFormat(String key, String name, String extension,
			boolean isImport, boolean isExport) {
		this.key = key;
		this.name = name;
		this.extension = extension;
		this.isExport = isExport;
		this.isImport = isImport;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getExtension() {
		return extension;
	}

	public static ImpExFormat getFormat(String name) {
		for (ImpExFormat format : values()) {
			if (format.getName().equals(name))
				return format;
		}
		return null;
	}

	public static ImpExFormat getFormatForExtension(String extension) {
		for (ImpExFormat format : values()) {
			if (format.getExtension().equals(extension))
				return format;
		}
		return null;
	}

	public static List<ImpExFormat> getExportFormats() {
		List<ImpExFormat> formats = new ArrayList<ImpExFormat>();
		for (ImpExFormat format : values()) {
			if (format.isExport)
				formats.add(format);
		}
		return formats;
	}
	
	public static List<ImpExFormat> getImportFormats() {
		List<ImpExFormat> formats = new ArrayList<ImpExFormat>();
		for (ImpExFormat format : values()) {
			if (format.isImport)
				formats.add(format);
		}
		return formats;
	}

}
