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
package be.vds.jtbdive.core.core.catalogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The formats of the documents that are allowed in the application.
 * 
 * @author gautier
 * 
 */
public enum DocumentFormat {

	FORMAT_PNG(0, true, "PNG image", "png", new String[] { "png" }), 
	FORMAT_JPG(1, true, "JPG image", "jpg", new String[] { "jpg", "jpeg" }), 
	FORMAT_GIF(2, true, "PNG image", "gif", new String[] { "gif" }), 
	;

	private int id;
	private String extension;
	private String[] authorizedExtensions;
	private boolean canBeImported;

	private String description;

	private DocumentFormat(int id, boolean canBeImported, String description,
			String extension, String[] authorizedExtensions) {
		this.id = id;
		this.canBeImported = canBeImported;
		this.description = description;
		this.extension = extension;
		this.authorizedExtensions = Arrays.copyOf(authorizedExtensions, authorizedExtensions.length);
	}

	public String getDescription() {
		return description;
	}

	public String getExtension() {
		return extension;
	}

	public int getId() {
		return id;
	}

	public String[] getAuthorizedExtensions() {
		return authorizedExtensions;
	}

	public static DocumentFormat getDocumentFormat(int id) {
		for (DocumentFormat df : values()) {
			if (df.id == id){
				return df;
			}
		}
		return null;
	}

	public static DocumentFormat getAuthorizedDocumentFormatForFileName(String fileName) {
		for (DocumentFormat df : values()) {
			for (String ext : df.authorizedExtensions) {
				if (fileName.toLowerCase().endsWith(ext)) {
					return df;
				}
			}
		}
		return null;
	}

	public static List<DocumentFormat> getImportableExtensions() {
		List<DocumentFormat> l = new ArrayList<DocumentFormat>();
		for (DocumentFormat df : values()) {
			if (df.canBeImported) {
				l.add(df);
			}
		}
		return l;
	}
	
	public boolean isCanBeImported() {
		return canBeImported;
	}
}
