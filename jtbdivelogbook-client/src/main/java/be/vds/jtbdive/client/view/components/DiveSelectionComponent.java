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
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.comparator.DiveNumberComparator;

public class DiveSelectionComponent extends JPanel {

	private static final long serialVersionUID = 332437490658084723L;
	public static final String SELECTION_CHANGED = "selection.changed";
	private List<Dive> originalDives;
	private List<Dive> selectedDives;
	private PercentageFillPanel fillPanel;

	public DiveSelectionComponent() {
		this(null);
	}

	public DiveSelectionComponent(List<Dive> originalDives) {
		this.originalDives = originalDives;
		this.selectedDives = originalDives;
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setBorder(null);
		this.setLayout(new BorderLayout(0, 0));
		this.add(createTextLabel(), BorderLayout.CENTER);
		this.add(createButton(), BorderLayout.EAST);
	}

	private Component createTextLabel() {
		fillPanel = new PercentageFillPanel(null, UIAgent.getInstance()
				.getColorHomePanel(), 120);
		adaptFillComponent();
		return fillPanel;
	}

	private Component createButton() {
		JButton b = new JButton(new AbstractAction(null, UIAgent.getInstance()
				.getIcon(UIAgent.ICON_SEARCH_16)) {

			private static final long serialVersionUID = 1534855612022140652L;

			@Override
			public void actionPerformed(ActionEvent e) {
				I18nResourceManager i18n = I18nResourceManager.sharedInstance();
				List<Dive> dives = new ArrayList<Dive>(originalDives);
				Collections.sort(dives, new DiveNumberComparator());

				DiveSelectionDialog dlg = new DiveSelectionDialog(i18n
						.getString("dives.selection"), i18n
						.getString("dives.export.select"), dives);
				if (selectedDives != null) {
					dlg.setSelectedDives(selectedDives);
				}

				int i = dlg.showDialog(400, 400);
				if (i == DiveSelectionDialog.OPTION_OK) {
					setSelectedDives(dlg.getSelectedDives());
				}
			}
		});
		b.setBorderPainted(false);
		b.setContentAreaFilled(false);
		return b;
	}

	private void setSelectedDives(List<Dive> dives) {
		List<Dive> oldValue = this.originalDives;
		this.selectedDives = dives;
		adaptFillComponent();
		firePropertyChange(SELECTION_CHANGED, oldValue, this.originalDives);
	}

	private void adaptFillComponent() {
		int selectCount = 0;
		if (null != selectedDives) {
			selectCount = selectedDives.size();
		}

		int originalCount = 0;
		if (null != originalDives) {
			originalCount = originalDives.size();
		}

		String s = "# "
				+ I18nResourceManager.sharedInstance().getString("dives")
				+ " : " + selectCount + " / " + originalCount;
		fillPanel.setText(s);

		if (originalCount == 0) {
			fillPanel.setPercentage(1);
		} else {
			fillPanel.setPercentage((double) selectCount
					/ (double) originalCount);
		}
	}

	public List<Dive> getSelectedDives() {
		return selectedDives;
	}

	public void setDives(List<Dive> dives) {
		this.originalDives = dives;
		this.selectedDives = dives;
		adaptFillComponent();
	}

}
