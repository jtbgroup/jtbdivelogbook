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
package be.vds.jtbdive.client.view.core.dive.profile;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nCheckBox;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtbdive.client.swing.component.DetailPanel;

public class DiveProfileOptionsControlPanel extends DetailPanel {
	private static final long serialVersionUID = 1162993909940159650L;
	private I18nCheckBox showGazesOptionCb;
	private I18nLabel titleLabel;
	private DiveProfileGraph graph;

	public DiveProfileOptionsControlPanel(DiveProfileGraph graph) {
		this.graph = graph;
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		this.setOpaque(false);

		this.add(createTitle(), BorderLayout.NORTH);
		this.add(createChoicesPanel(), BorderLayout.CENTER);

		activateFocus(false);
	}

	private void activateFocus(boolean focusable) {
		showGazesOptionCb.setFocusable(focusable);
	}
	
	private Component createChoicesPanel() {
		showGazesOptionCb = new I18nCheckBox("gaz.mixes.show");
		showGazesOptionCb.setOpaque(false);
		showGazesOptionCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				graph.showGazMixes(((JCheckBox) e.getSource()).isSelected());
			}
		});

		JPanel choicesPanel = new JPanel();
		choicesPanel.setOpaque(false);
		choicesPanel
				.setLayout(new BoxLayout(choicesPanel, BoxLayout.PAGE_AXIS));
		choicesPanel.add(showGazesOptionCb);


		return choicesPanel;
	}

	private Component createTitle() {
		titleLabel = new I18nLabel("options");
		Font f = titleLabel.getFont();
		titleLabel.setFont(new Font(f.getFontName(), Font.BOLD, 14));
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
		p.setOpaque(false);
		p.add(titleLabel);
		return p;
	}

	public void setActive(boolean active) {
		showGazesOptionCb.setEnabled(active);
		activateFocus(active);
	}

	public boolean isShowGazMixes() {
		return showGazesOptionCb.isSelected();
	}
	
}
