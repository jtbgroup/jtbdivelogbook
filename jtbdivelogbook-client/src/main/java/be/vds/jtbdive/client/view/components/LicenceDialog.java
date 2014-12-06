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
package be.vds.jtbdive.client.view.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import be.smd.i18n.swing.I18nButton;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class LicenceDialog extends JDialog {

	private static final long serialVersionUID = 190887037351874838L;

	public LicenceDialog(String licenceName, String licenceContent) {
		init(licenceName, licenceContent);
	}

	private void init(String licenceName, String licenceContent) {
		this.getContentPane().add(createContentPane(licenceContent));
		this.setTitle("Licence for " + licenceName);
		this.setSize(550, 550);
	}

	private Component createContentPane(String licenceContent) {
		JTextArea textArea = new JTextArea(licenceContent);
		textArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.getVerticalScrollBar().setUnitIncrement(
				UIAgent.VERTICAL_UNIT_SCROLL);

		I18nButton btn = new I18nButton(new AbstractAction("close") {

			private static final long serialVersionUID = 4595661038480917155L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bp.setOpaque(false);
		bp.add(btn);

		JPanel p = new DetailPanel();
		p.setLayout(new BorderLayout());
		p.add(scroll, BorderLayout.CENTER);
		p.add(bp, BorderLayout.SOUTH);
		return p;
	}
}
