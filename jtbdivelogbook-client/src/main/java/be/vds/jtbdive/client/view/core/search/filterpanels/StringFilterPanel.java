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
package be.vds.jtbdive.client.view.core.search.filterpanels;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.comparator.OrderedCatalogComparator;
import be.vds.jtbdive.client.core.filters.StringDiveFilter;
import be.vds.jtbdive.client.core.filters.operator.DiveFilterOperator;
import be.vds.jtbdive.client.core.filters.operator.StringOperator;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;

public class StringFilterPanel extends FilterNaturePanel {

	private static final long serialVersionUID = 8777175272246131169L;
	private JTextField searchCriteriaTf;
	private JComboBox operatorCb;
	private boolean propagateChanges = true;

	public StringFilterPanel() {
	}

	@Override
	public Component createFilterComponents() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		int y = 0;
		GridBagLayoutManager
				.addComponent(p, new I18nLabel("matching"), gc, 0, y, 1, 1, 0,
						0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createOperatorComponent(), gc, 1,
				y, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, new I18nLabel("criteria.search"),
				gc, 0, ++y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createSearchCriteriaTf(), gc, 1,
				y, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, Box.createGlue(), gc, 0, y, 0, 2,
				0, 0, GridBagConstraints.BOTH, GridBagConstraints.WEST);

		return p;

	}

	private Component createOperatorComponent() {
		DiveFilterOperator[] values = StringOperator.values();
		Arrays.sort(values, new OrderedCatalogComparator());
		operatorCb = new JComboBox(values);
		operatorCb.setRenderer(new KeyedCatalogRenderer());
		operatorCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (propagateChanges) {
					if (null != diveFilter) {
						diveFilter.setOperator((DiveFilterOperator) operatorCb
								.getSelectedItem());
						notifyChanges(diveFilter);
					}
				}
			}
		});
		return operatorCb;
	}

	private Component createSearchCriteriaTf() {
		searchCriteriaTf = new JTextField(30);
		searchCriteriaTf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (propagateChanges) {
					if (null != diveFilter) {
						((StringDiveFilter) diveFilter)
								.setTextCriteria(searchCriteriaTf.getText());
						notifyChanges(diveFilter);
					}
				}
			}
		});
		return searchCriteriaTf;
	}

	@Override
	public void clearComponents() {
		propagateChanges = false;
		searchCriteriaTf.setText(null);
		operatorCb.setSelectedIndex(-1);
		propagateChanges = true;
	}

	@Override
	public void fillComponents() {
		propagateChanges = false;
		searchCriteriaTf.setText(((StringDiveFilter) diveFilter)
				.getTextCriteria());
		operatorCb.setSelectedItem(diveFilter.getOperator());
		propagateChanges = true;
	}

}
