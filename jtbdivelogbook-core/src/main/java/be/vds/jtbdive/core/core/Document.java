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
package be.vds.jtbdive.core.core;

import java.io.Serializable;
import java.util.Arrays;

import be.vds.jtbdive.core.core.catalogs.DocumentFormat;

/**
 * A document that can be linked with a specific object THe content of the
 * document is the real payload. If null, it means the payload hasn't been
 * loaded in the object. If the payload is loaded, the variable content MUST be
 * a byte[], even empty.
 * 
 * @author Vanderslyen.G
 * 
 */
public class Document implements Serializable {

	private static final long serialVersionUID = -2534590056808702776L;
	private long id = -1;
	private String comment, title;
	private byte[] content;
	private DocumentFormat documentFormat;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the {@link DocumentFormat} of the document
	 * 
	 * @return the document format
	 */
	public DocumentFormat getDocumentFormat() {
		return documentFormat;
	}

	public void setDocumentFormat(DocumentFormat documentType) {
		this.documentFormat = documentType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the comment linked to the document
	 * 
	 * @return a String containing the comment
	 */
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = Arrays.copyOf(content, content.length);
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (this == obj) {
			return true;
		}

		if (obj instanceof Document) {
			Document doc = (Document) obj;

			if (-1 == id && -1 == doc.getId()) {
				return this == doc;
			}

			if (id == doc.getId()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Document " + id + " (" + title + ")";
	}

	public int hashCode() {
		if (-1 != id) {
			return (int) id;
		} else {
			return (title == null ? "".hashCode() : title.hashCode())
					+ (comment == null ? "".hashCode() : comment.hashCode());
		}
	};

}
