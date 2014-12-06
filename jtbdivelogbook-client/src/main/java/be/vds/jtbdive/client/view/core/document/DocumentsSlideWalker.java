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
package be.vds.jtbdive.client.view.core.document;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;

import be.vds.jtbdive.client.core.DocumentContentLoader;
import be.vds.jtbdive.client.util.ImageUtils;
import be.vds.jtbdive.client.view.core.slideshow.SlideWalker;
import be.vds.jtbdive.core.core.Document;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;

/**
 * This slideWalker has the possibility to load {@link Document} contents and
 * pre-buffer the images if desired. The pre-buffer is used to avoid waiting
 * time while walking through the slide set as the document loader is depending
 * from the model's speed. There are 2 pre-buffer sizes: one forward and one
 * backward.
 * 
 * @author gautier
 * 
 */
public class DocumentsSlideWalker implements SlideWalker {

	private static final Syslog LOGGER = Syslog
			.getLogger(DocumentsSlideWalker.class);
	private List<Document> documents;
	private int pointer;
	private DocumentContentLoader documentContentLoader;
	private Map<Document, BufferedImage> imageBuffer;
	private int forwardBufferSize = 1;
	private int backwardBufferSize = 0;

	public DocumentsSlideWalker(List<Document> documents,
			DocumentContentLoader documentContentLoader) {
		this.documents = documents;
		this.documentContentLoader = documentContentLoader;

		this.imageBuffer = new HashMap<Document, BufferedImage>();
	}

	@Override
	public BufferedImage getCurrentSlide() {
		BufferedImage img = getImageForPointer(this.pointer);

		if (forwardBufferSize > 0 || backwardBufferSize > 0) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					organizeImageBuffer();
				}
			}).start();
		}

		return img;
	}

	private void organizeImageBuffer() {
		Set<Document> docsToKeep = new HashSet<Document>();
		docsToKeep.add(documents.get(pointer));
		// search next image
		int bufferPointer = this.pointer;
		for (int i = 0; i < forwardBufferSize; i++) {
			if (bufferPointer == documents.size() - 1) {
				bufferPointer = 0;
			} else {
				bufferPointer++;
			}

			Document bufferedDocument = documents.get(bufferPointer);
			docsToKeep.add(bufferedDocument);
			if (!imageBuffer.containsKey(bufferedDocument)) {
				getImageForPointer(bufferPointer);
			}
		}

		bufferPointer = this.pointer;
		for (int i = 0; i < backwardBufferSize; i++) {

			if (bufferPointer == 0) {
				bufferPointer = documents.size() - 1;
			} else {
				bufferPointer--;
			}

			Document bufferedDocument = documents.get(bufferPointer);
			docsToKeep.add(bufferedDocument);
			if (!imageBuffer.containsKey(bufferedDocument)) {
				getImageForPointer(bufferPointer);
			}
		}

		List<Document> docsToRemove = new ArrayList<Document>(documents);
		docsToRemove.removeAll(docsToKeep);
		for (Document document : docsToRemove) {
			if (imageBuffer.containsKey(document)
					&& imageBuffer.get(document) != null) {
				imageBuffer.remove(document);
				LOGGER.debug("Removing image for document "
						+ document.toString() + " from buffer");
			}
		}
	}

	private BufferedImage getImageForPointer(int pointer) {
		if (documents.size() == 0) {
			return null;
		}

		Document doc = documents.get(pointer);
		if (imageBuffer.containsKey(doc) && imageBuffer.get(doc) != null) {
			return imageBuffer.get(doc);
		}

		BufferedImage img = null;

		try {
			byte[] content = doc.getContent();
			if (null == content) {
				content = documentContentLoader.loadDocumentContent(
						doc.getId(), doc.getDocumentFormat());
			}
			img = ImageUtils.convertImageIconToBufferedImage(new ImageIcon(
					content));
			imageBuffer.put(doc, img);
			LOGGER.debug("Adding image for document " + doc.toString()
					+ " to buffer");
		} catch (DataStoreException e) {
			LOGGER.error(e);
		}
		return img;
	}

	@Override
	public BufferedImage getPreviousSlide() {
		if (pointer == 0) {
			pointer = documents.size() - 1;
		} else {
			pointer--;
		}
		return getCurrentSlide();
	}

	@Override
	public BufferedImage getNextSlide() {
		if (pointer == documents.size() - 1) {
			pointer = 0;
		} else {
			pointer++;
		}

		return getCurrentSlide();
	}

	public int getForwardBufferSize() {
		return forwardBufferSize;
	}

	public void setForwardBufferSize(int forwardBufferSize) {
		this.forwardBufferSize = forwardBufferSize;
	}

	public int getBackwardBufferSize() {
		return backwardBufferSize;
	}

	public void setBackwardBufferSize(int backwardBufferSize) {
		this.backwardBufferSize = backwardBufferSize;
	}
}
