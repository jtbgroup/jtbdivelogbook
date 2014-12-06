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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import be.smd.i18n.swing.I18nButton;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.DiveSite;

public class DiveSiteSelectionPanel extends JPanel {

	private static final long serialVersionUID = -4964882610563448314L;
	private Map<JCheckBox, DiveSite> diveSiteCheckBoxes;

	public DiveSiteSelectionPanel(List<DiveSite> diveSites) {
		init(diveSites);
	}

	private void init(List<DiveSite> diveSites) {
		diveSiteCheckBoxes = new HashMap<JCheckBox, DiveSite>();
		this.setLayout(new BorderLayout());
		this.add(createListPanel(diveSites), BorderLayout.CENTER);
		this.setOpaque(false);
	}

	private Component createListPanel(List<DiveSite> diveSites) {
		JPanel diveList = new JPanel();
		diveList.setBackground(UIAgent.getInstance().getColorBaseBackground());
		diveList.setLayout(new BoxLayout(diveList, BoxLayout.PAGE_AXIS));
		JCheckBox cb = null;
		if (null != diveSites) {
			for (DiveSite diveSite : diveSites) {
				cb = new JCheckBox(getDiveSiteDescription(diveSite));
				cb.setOpaque(false);
				cb.setFocusable(false);
				diveList.add(cb);
				diveSiteCheckBoxes.put(cb, diveSite);
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

	private String getDiveSiteDescription(DiveSite diveSites) {
		return diveSites.getName();
	}

	public void setAllSelected(boolean b) {
		for (JCheckBox cb : diveSiteCheckBoxes.keySet()) {
			cb.setSelected(b);
		}
	}

	public List<DiveSite> getSelectedDiveSites() {
		List<DiveSite> r = new ArrayList<DiveSite>();
		for (JCheckBox cb : diveSiteCheckBoxes.keySet()) {
			if (cb.isSelected()) {
				r.add(diveSiteCheckBoxes.get(cb));
			}
		}

		return r;
	}

	public void setSelectedDiveSites(List<DiveSite> diveSites) {
		for (JCheckBox cb : diveSiteCheckBoxes.keySet()) {
			cb.setSelected(diveSites.contains(diveSiteCheckBoxes.get(cb)));
		}
	}

	public void setDiveSites(List<DiveSite> diveSites) {
		this.removeAll();
		init(diveSites);
		repaint();
		revalidate();
	}

	public List<DiveSite> getOriginalDiveSites() {
		return new ArrayList<DiveSite>(diveSiteCheckBoxes.values());
	}

}
