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

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import be.vds.jtb.swing.component.Thumbnail;
import be.vds.jtbdive.client.core.DocumentContentLoader;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Document;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;

public class ThumbnailDocumentPanel extends DocumentPanel {
	private static final long serialVersionUID = -4515983474451146582L;
	private static final Syslog LOGGER = Syslog
			.getLogger(ThumbnailDocumentPanel.class);
//	private JLabel titleLabel;
	private JLabel iconLabel;

	public ThumbnailDocumentPanel(Document document,
			DocumentContentLoader loader) {
		super(document);
		init(document, loader);
	}

	private void init(Document document, DocumentContentLoader loader) {

		this.setLayout(new BorderLayout(5, 5));
		iconLabel = new JLabel(UIAgent.getInstance().getIcon(UIAgent.ICON_INFO_16));
//		titleLabel = new JLabel(document.getTitle());
		this.add(iconLabel, BorderLayout.CENTER);
//		this.add(titleLabel, BorderLayout.CENTER);
		loadContent(document, loader);

	}

	private void loadContent(final Document document,
			final DocumentContentLoader loader) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				byte[] content = null;
				if (document.getId() == -1) {
					content = document.getContent();
				} else {
					try {
						content = loader.loadDocumentContent(document.getId(),
								document.getDocumentFormat());
					} catch (DataStoreException e) {
						LOGGER.error(e);
					}
				}

				if (content != null) {
					Thumbnail t = new Thumbnail(new ImageIcon(content));
					// t.fitToWidth(70);
					t.fitToHeight(40);
					iconLabel.setIcon(t);
				}
			}
		}).start();
	}


}
