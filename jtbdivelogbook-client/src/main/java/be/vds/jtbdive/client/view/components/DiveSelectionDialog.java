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
import java.awt.Image;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;

public class DiveSelectionDialog extends PromptDialog {
	private static final long serialVersionUID = 3492982981287977384L;

	private DiveSelectionPanel diveSelectionPanel;

	public DiveSelectionDialog(String title, String message, List<Dive> dives) {
		super(title, message);
		initCustom(dives);
	}

	public DiveSelectionDialog(JFrame parentFrame, String title,
			String message, List<Dive> dives) {
		super(parentFrame, title, message);
		initCustom(dives);
	}

	public DiveSelectionDialog(JFrame parentFrame, String title,
			String message, Image image, List<Dive> dives) {
		super(parentFrame, title, message, image, null);
		initCustom(dives);
	}

	private void initCustom(List<Dive> dives) {
		diveSelectionPanel.setDives(dives);
	}

	@Override
	protected Component createContentPanel() {
		DetailPanel dp = new DetailPanel(new BorderLayout());
		diveSelectionPanel = new DiveSelectionPanel(null);
		dp.add(diveSelectionPanel, BorderLayout.CENTER);

		JPanel p = new JPanel();
		p.setBackground(UIAgent.getInstance().getColorBaseBackground());
		p.setLayout(new BorderLayout());
		p.add(dp, BorderLayout.CENTER);
		return p;
	}

	public List<Dive> getSelectedDives() {
		return diveSelectionPanel.getSelectedDives();
	}

	public void setSelectedDives(List<Dive> selectedDives) {
		diveSelectionPanel.setSelectedDives(selectedDives);
	}
}
