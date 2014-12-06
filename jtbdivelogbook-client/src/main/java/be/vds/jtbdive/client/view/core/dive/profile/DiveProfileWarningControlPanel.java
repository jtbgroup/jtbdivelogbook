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

public class DiveProfileWarningControlPanel extends DetailPanel {
	private static final long serialVersionUID = 1162993909940159650L;
	private I18nCheckBox decoEntriesCb;
	private I18nCheckBox remainBottomTimeCb;
	private I18nCheckBox ascentTooFastCb;
	private I18nCheckBox decoWarningCb;
	private I18nLabel warningsLabel;
	private DiveProfileGraph graph;

	public DiveProfileWarningControlPanel(DiveProfileGraph graph) {
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
		decoEntriesCb.setFocusable(focusable);
		remainBottomTimeCb.setFocusable(focusable);
		ascentTooFastCb.setFocusable(focusable);
		decoWarningCb.setFocusable(focusable);
	}

	private Component createChoicesPanel() {
		decoWarningCb = new I18nCheckBox("deco.warning.ceiling");
		decoWarningCb.setOpaque(false);
		decoWarningCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				graph.showDecoWarning(((JCheckBox) e.getSource()).isSelected());
			}
		});

		ascentTooFastCb = new I18nCheckBox("ascent.too.fast");
		ascentTooFastCb.setOpaque(false);
		ascentTooFastCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				graph.showAscentTooFastWarning(ascentTooFastCb.isSelected());
			}
		});

		remainBottomTimeCb = new I18nCheckBox("remaining.bottom.time.limited");
		remainBottomTimeCb.setOpaque(false);
		remainBottomTimeCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				graph.showRemainBottomTimeWarning(((JCheckBox) e.getSource())
						.isSelected());
			}
		});

		decoEntriesCb = new I18nCheckBox("deco.entries");
		decoEntriesCb.setOpaque(false);
		decoEntriesCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				graph.showDecoEntries(((JCheckBox) e.getSource()).isSelected());
			}
		});

		JPanel choicesPanel = new JPanel();
		choicesPanel.setOpaque(false);
		choicesPanel
				.setLayout(new BoxLayout(choicesPanel, BoxLayout.PAGE_AXIS));
		choicesPanel.add(decoEntriesCb);

		choicesPanel.add(ascentTooFastCb);
		choicesPanel.add(decoWarningCb);
		choicesPanel.add(remainBottomTimeCb);

		return choicesPanel;
	}

	private Component createTitle() {
		warningsLabel = new I18nLabel("alarms");
		Font f = warningsLabel.getFont();
		warningsLabel.setFont(new Font(f.getFontName(), Font.BOLD, 14));
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
		p.setOpaque(false);
		p.add(warningsLabel);
		return p;
	}

	public void setActive(boolean active) {
		decoEntriesCb.setEnabled(active);
		ascentTooFastCb.setEnabled(active);
		remainBottomTimeCb.setEnabled(active);
		decoWarningCb.setEnabled(active);
		activateFocus(active);
	}

	public boolean isAscentTooFastSelected() {
		return ascentTooFastCb.isSelected();
	}

	public boolean isRemainBottomTimeSelected() {
		return remainBottomTimeCb.isSelected();
	}

	public boolean isDecoWarningSelected() {
		return decoWarningCb.isSelected();
	}

	public boolean isDecoEntriesSelected() {
		return decoEntriesCb.isSelected();
	}
}
