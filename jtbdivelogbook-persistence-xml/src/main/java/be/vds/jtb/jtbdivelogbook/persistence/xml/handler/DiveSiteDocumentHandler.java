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
package be.vds.jtb.jtbdivelogbook.persistence.xml.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Document;
import be.vds.jtbdive.core.logging.Syslog;

public class DiveSiteDocumentHandler {
	private static final Syslog LOGGER = Syslog
			.getLogger(DiveSiteDocumentHandler.class);
	private String basePath;

	public DiveSiteDocumentHandler(String basePath) {
		this.basePath = basePath;
	}

	public void deleteDocuments(List<String> documents) {
		deleteDocument(documents);
	}

	private void deleteDocument(List<String> documents) {
		for (String name : documents) {
			deleteDocument(name);
		}
	}

	private void deleteDocument(String name) {
		String path = basePath + "documents" + File.separatorChar
				+ File.separatorChar + "divesites" + File.separatorChar + name;
		new File(path).delete();
	}

	private void deleteDocument(Document document) {
		deleteDocument(document.getId() + "."
				+ document.getDocumentFormat().getExtension());
	}

	public void saveDocuments(List<Document> documents) {
		if (documents == null || documents.size() == 0) {
			return;
		}

		for (Document document : documents) {
			if (document.getContent() != null) {
				persistDocument(document);
				document.setContent(null);
			}
		}
	}

	private void persistDocument(be.vds.jtbdive.core.core.Document document) {
		File f = new File(basePath + "documents" + File.separatorChar
				+ File.separatorChar + "divesites" + File.separatorChar
				+ document.getId() + "."
				+ document.getDocumentFormat().getExtension());
		try {
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(document.getContent());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	public void fillDocumentContent(DiveSite diveSite) {
		if (diveSite.getDocuments() != null) {
			for (Document document : diveSite.getDocuments()) {
				String path = getDocumentsDir() + File.separatorChar
						+ document.getId() + "."
						+ document.getDocumentFormat().getExtension();
				try {
					File file = new File(path);
					byte[] content = new byte[(int) file.length()];
					new FileInputStream(file).read(content);
					document.setContent(content);
				} catch (FileNotFoundException e) {
					LOGGER.error("Problem loading the image " + path + ": "
							+ e.getMessage());
				} catch (IOException e) {
					LOGGER.error("Problem loading the image " + path + ": "
							+ e.getMessage());
				}
			}
		}
	}

	private String getDocumentsDir() {
		return basePath + File.separatorChar + "documents" + File.separatorChar
				+ "divesites";
	}

	public void synchronizeDocuments(List<Document> currentDocuments,
			List<Document> newDocuments) {

		List<Document> toKeep = new ArrayList<Document>();
		List<Document> toDelete = new ArrayList<Document>();
		List<Document> oldDocs = new ArrayList<Document>();
		
		if (newDocuments != null && newDocuments.size() > 0) {
			toKeep.addAll(newDocuments);
		}
		
		if (currentDocuments != null && currentDocuments.size() > 0) {
			oldDocs.addAll(currentDocuments);
		}
		
		
		// 1. compare the id's lists (from XML and from new Divesite) ==>2
		// lists
		// (to delete - to keep). also verify the size is the same.
		for (Document document : oldDocs) {
			if (toKeep.contains(document)) {
				File f = new File(getDocumentsDir() + File.separatorChar
						+ document.getId() + "."
						+ document.getDocumentFormat().getExtension());
				if (f.length() != toKeep.get(toKeep.indexOf(document)).getContent().length) {
					toDelete.add(document);
				}
			} else {
				toDelete.add(document);
			}
		}

		toKeep.removeAll(toDelete);
		oldDocs.removeAll(toDelete);
		toKeep.removeAll(oldDocs);
		

		// 2. delete the files not required anymore
		for (Document document : toDelete) {
			deleteDocument(document);
		}

		// 3. push new files
		saveDocuments(toKeep);

	}
}
