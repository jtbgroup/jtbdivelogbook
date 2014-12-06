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
package be.vds.jtbdive.xml.parsers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import be.vds.jtbdive.core.core.Document;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;

//TODO: review the code of this parser
public class DocumentParser {

	private static final String TAG_TITLE = "title";
	private static final String TAG_ID = "id";
	private static final String TAG_FORMAT = "format";
	private static final String TAG_COMMENT = "comment";
	public static final String TAG_DOCUMENT = "document";
	public static final String TAG_DOCUMENTS = "documents";

	public static Document readDocument(Element documentEl) {
		Document document = new Document();
		document.setId(Long.parseLong(documentEl.getAttributeValue(TAG_ID)));
		document.setTitle(documentEl.getChildText(TAG_TITLE));
		document.setComment(documentEl.getChildText(TAG_COMMENT));
		document.setDocumentFormat(DocumentFormat.getDocumentFormat(Integer
				.valueOf(documentEl.getChildText(TAG_FORMAT))));

		return document;
	}

	public static Element createDocumentElement(Document document) {
		Element docEl = new Element(TAG_DOCUMENT);
		docEl.setAttribute(TAG_ID, String.valueOf(document.getId()));

		Element elt = new Element(TAG_FORMAT);
		elt.setText(String.valueOf(document.getDocumentFormat().getId()));
		docEl.addContent(elt);

		if (document.getTitle() != null
				&& document.getTitle().trim().length() > 0) {
			elt = new Element(TAG_TITLE);
			elt.setText(document.getTitle());
			docEl.addContent(elt);
		}

		if (document.getComment() != null
				&& document.getComment().trim().length() > 0) {
			elt = new Element(TAG_COMMENT);
			elt.setText(document.getComment());
			docEl.addContent(elt);
		}
		return docEl;
	}

	public static List<String> getDocumentFileNames(Element docsEl) {
		if (docsEl != null) {
			List<String> docsId = new ArrayList<String>();
			for (Iterator<Element> iterator = docsEl.getChildren("document")
					.iterator(); iterator.hasNext();) {
				Element el = iterator.next();
				String s = el.getAttributeValue("id");
				s += ".";
				s += DocumentFormat.getDocumentFormat(
						Integer.parseInt(el.getChildText("format")))
						.getExtension();
				docsId.add(s);
			}
			return docsId;
		}
		return null;
	}
}
