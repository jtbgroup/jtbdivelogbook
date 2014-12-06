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
package be.vds.jtbdive.client.view.core.search;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import be.vds.jtbdive.client.core.comparator.KeyedCatalogComparator;
import be.vds.jtbdive.client.core.filters.DiveFilterType;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;

public class FilterTypeSelectionDialog extends PromptDialog {

	private static final long serialVersionUID = -2689628419140978738L;
	private ButtonGroup filterTypeGroup;

	public FilterTypeSelectionDialog(Dialog parentDialog) {
		super(parentDialog, i18n.getString("filter.type"), null);
	}

	@Override
	protected Component createContentPanel() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));

		filterTypeGroup = new ButtonGroup();

		DiveFilterType[] types = DiveFilterType.values();
		Arrays.sort(types, new KeyedCatalogComparator());
		JRadioButton radio = null;
		for (DiveFilterType ft : types) {
			radio = new JRadioButton(i18n.getString(ft.getKey()));
			radio.setActionCommand(ft.getKey());
			radio.setOpaque(false);
			p.add(radio);
			filterTypeGroup.add(radio);
		}

		p.add(Box.createVerticalGlue());
		JScrollPane scroll = new JScrollPane(p);
		SwingComponentHelper.displayJScrollPane(scroll);

		JPanel det = new DetailPanel(new BorderLayout());
		det.add(scroll);
		return det;
	}

	public DiveFilterType getFilterType() {
		ButtonModel sel = filterTypeGroup.getSelection();
		if (sel == null)
			return null;

		String s = sel.getActionCommand();
		return DiveFilterType.getFilterType(s);
	}

}
