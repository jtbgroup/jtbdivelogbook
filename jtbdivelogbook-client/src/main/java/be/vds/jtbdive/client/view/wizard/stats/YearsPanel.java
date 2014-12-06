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
package be.vds.jtbdive.client.view.wizard.stats;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import be.smd.i18n.swing.I18nButton;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class YearsPanel extends JPanel {
	private static final int COLUMN_NUMBER = 2;
	private List<JCheckBox> yearsBoxes = new ArrayList<JCheckBox>();
	private JPanel centralPanel;

	public YearsPanel() {
		this(null);
	}

	public YearsPanel(List<Integer> years) {
		init(years);
	}

	private void init(List<Integer> years) {
		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		this.add(createYearsPanel(years), BorderLayout.CENTER);
		this.add(createButtons(), BorderLayout.SOUTH);
	}

	private Component createYearsPanel(List<Integer> years) {
		centralPanel = new JPanel(new GridBagLayout());
		centralPanel.setBackground(UIAgent.getInstance()
				.getColorBaseBackground());
		centralPanel.setOpaque(true);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2);

		if (null != years) {
			JCheckBox yearBox = null;
			int column = 0;
			int row = 0;
			for (Integer year : years) {
				yearBox = new JCheckBox(String.valueOf(year));
				yearBox.setOpaque(false);
				yearBox.setFocusable(false);
				yearBox.setSelected(true);
				yearBox.setActionCommand(String.valueOf(year));
				yearsBoxes.add(yearBox);
				GridBagLayoutManager.addComponent(centralPanel, yearBox, c,
						column, row, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.CENTER);

				column++;
				if (column >= COLUMN_NUMBER) {
					column = 0;
					row++;
				}
			}
			
			GridBagLayoutManager.addComponent(centralPanel, Box.createGlue(), c,
					0, row+1, 1, 1, 0, 1, GridBagConstraints.BOTH,
					GridBagConstraints.CENTER);
		}
		
		JScrollPane scroll = new JScrollPane(centralPanel);
		SwingComponentHelper.displayJScrollPane(scroll);
		return scroll;
	}

	private Component createButtons() {
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setOpaque(false);
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));

		JButton checkAll = new I18nButton(new AbstractAction("select.all") {
			private static final long serialVersionUID = -8030368792732265340L;

			public void actionPerformed(ActionEvent e) {
				checkAll();
			}
		});

		JButton unCheckAll = new I18nButton(new AbstractAction("unselect.all") {

			private static final long serialVersionUID = 444158576827385422L;

			public void actionPerformed(ActionEvent e) {
				unCheckAll();
			}
		});

		buttonsPanel.add(checkAll);
		buttonsPanel.add(unCheckAll);

		return buttonsPanel;
	}

	public void checkAll() {
		for (JCheckBox box : yearsBoxes) {
			box.setSelected(true);
		}
	}

	public void unCheckAll() {
		for (JCheckBox box : yearsBoxes) {
			box.setSelected(false);
		}
	}

	public List<Integer> getSelectedYears() {
		List<Integer> years = new ArrayList<Integer>();
		for (JCheckBox cb : yearsBoxes) {
			if (cb.isSelected()) {
				years.add(new Integer(cb.getActionCommand()));
			}
		}
		return years;
	}

	public void setYears(List<Integer> years) {
		this.remove(centralPanel);
		this.add(createYearsPanel(years), BorderLayout.CENTER);
		this.revalidate();
	}
}
