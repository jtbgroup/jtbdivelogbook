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

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;

import be.vds.jtbdive.core.core.Document;

public class FormatIconDocumentPanel extends DocumentPanel {

	private static final long serialVersionUID = -3414812206432699907L;
	private JLabel titleLabel;

	public FormatIconDocumentPanel(Document document) {
		super(document);
		init(document);
	}

	private void init(Document document) {
		titleLabel = new JLabel();
		titleLabel.setPreferredSize(new Dimension(30, 30));
		titleLabel.setToolTipText(document.getTitle());
		titleLabel.setIcon(DocumentIcon.getIcon24ForDocument(document
				.getDocumentFormat()));

		this.setLayout(new FlowLayout());
		this.add(titleLabel);
		this.setOpaque(false);
	}

}
