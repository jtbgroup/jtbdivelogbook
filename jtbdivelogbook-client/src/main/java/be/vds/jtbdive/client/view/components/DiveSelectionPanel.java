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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveSite;

public class DiveSelectionPanel extends JPanel {

	private static final long serialVersionUID = 4134944698270936176L;
	private static I18nResourceManager i18n = I18nResourceManager
			.sharedInstance();
	private Map<JCheckBox, Dive> diveCheckBoxes;

	public DiveSelectionPanel(List<Dive> dives) {
		init(dives);
	}

	private void init(List<Dive> dives) {
		diveCheckBoxes = new HashMap<JCheckBox, Dive>();
		this.setLayout(new BorderLayout());
		this.add(createListPanel(dives), BorderLayout.CENTER);
		this.setOpaque(false);
	}

	private Component createListPanel(List<Dive> dives) {
		JPanel diveList = new JPanel();
		diveList.setBackground(UIAgent.getInstance()
				.getColorBaseBackground());
		diveList.setLayout(new BoxLayout(diveList, BoxLayout.PAGE_AXIS));
		JCheckBox cb = null;
		if (null != dives) {
			for (Dive dive : dives) {
				cb = new JCheckBox(getDiveDescription(dive));
				cb.setOpaque(false);
				cb.setFocusable(false);
				diveList.add(cb);
				diveCheckBoxes.put(cb, dive);
			}
		}

		JScrollPane scroll = new JScrollPane(diveList);
		scroll.setPreferredSize(new Dimension(450, 300));
		SwingComponentHelper.displayJScrollPane(scroll);

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setOpaque(false);
		p.add(scroll, BorderLayout.CENTER);
		p.add(createSelectionButtons(), BorderLayout.SOUTH);
		return p;
	}

	private Component createSelectionButtons() {
		I18nButton unSelectAllBtn = new I18nButton(new AbstractAction(
				"unselect.all") {

			private static final long serialVersionUID = -6493754074762191349L;

			@Override
			public void actionPerformed(ActionEvent e) {
				setAllSelected(false);
			}

		});

		I18nButton selectAllBtn = new I18nButton(new AbstractAction(
				"select.all") {

			private static final long serialVersionUID = -2411920121023301755L;

			@Override
			public void actionPerformed(ActionEvent e) {
				setAllSelected(true);
			}
		});

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p.setOpaque(false);
		p.add(selectAllBtn);
		p.add(unSelectAllBtn);
		return p;
	}

	private String getDiveDescription(Dive dive) {
		StringBuilder sb = new StringBuilder();
		sb.append(i18n.getString("dive")).append(" ").append(dive.getNumber());

		Date diveDate = dive.getDate();
		if (diveDate != null)
			sb.append(" (")
					.append(UIAgent.getInstance().getFormatDateHoursFull()
							.format(diveDate)).append(")");

		DiveSite ds = dive.getDiveSite();
		if (ds != null)
			sb.append(" : ").append(ds.getName());

		return sb.toString();
	}

	public void setAllSelected(boolean b) {
		for (JCheckBox cb : diveCheckBoxes.keySet()) {
			cb.setSelected(b);
		}
	}

	public List<Dive> getSelectedDives() {
		List<Dive> r = new ArrayList<Dive>();
		for (JCheckBox cb : diveCheckBoxes.keySet()) {
			if (cb.isSelected()) {
				r.add(diveCheckBoxes.get(cb));
			}
		}

		return r;
	}

	public void setSelectedDives(List<Dive> selectedDives) {
		for (JCheckBox cb : diveCheckBoxes.keySet()) {
			cb.setSelected(selectedDives.contains(diveCheckBoxes.get(cb)));
		}
	}

	public void setDives(List<Dive> dives) {
		this.removeAll();
		init(dives);
		repaint();
		revalidate();
	}

	public List<Dive> getOriginalDives() {
		return new ArrayList<Dive>(diveCheckBoxes.values());
	}

}
