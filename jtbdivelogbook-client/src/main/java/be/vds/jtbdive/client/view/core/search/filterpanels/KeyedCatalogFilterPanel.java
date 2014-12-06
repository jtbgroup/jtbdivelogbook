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
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.comparator.KeyedCatalogComparator;
import be.vds.jtbdive.client.core.comparator.OrderedCatalogComparator;
import be.vds.jtbdive.client.core.filters.KeyedCatalogDiveFilter;
import be.vds.jtbdive.client.core.filters.inspector.KeyedCatalogFilterInspector;
import be.vds.jtbdive.client.core.filters.operator.DiveFilterOperator;
import be.vds.jtbdive.client.core.filters.operator.KeyedCatalogOperator;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;
import be.vds.jtbdive.core.core.catalogs.KeyedCatalog;

public class KeyedCatalogFilterPanel extends FilterNaturePanel {

	private static final long serialVersionUID = 6780863589842705436L;
	private JComboBox searchCriteriaComponent;
	private JComboBox operatorCb;
	private boolean propagateChanges = true;

	public KeyedCatalogFilterPanel() {
	}

	@Override
	public Component createFilterComponents() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		int y = 0;
		
		GridBagLayoutManager.addComponent(p, new I18nLabel("matching"), gc, 0,
				y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
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
		DiveFilterOperator[] values = KeyedCatalogOperator.values();
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
		searchCriteriaComponent = new JComboBox();
		searchCriteriaComponent.setRenderer(new KeyedCatalogRenderer());

		searchCriteriaComponent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (propagateChanges) {
					if (null != diveFilter) {
						KeyedCatalog d = (KeyedCatalog) searchCriteriaComponent
								.getSelectedItem();
						((KeyedCatalogDiveFilter) diveFilter).setCriteria(d);
						notifyChanges(diveFilter);
					}
				}
			}
		});

		return searchCriteriaComponent;
	}

	@Override
	public void clearComponents() {
		propagateChanges = false;
		searchCriteriaComponent.removeAllItems();
		operatorCb.setSelectedIndex(-1);
		propagateChanges = true;
	}

	@Override
	public void fillComponents() {
		propagateChanges = false;
		operatorCb.setSelectedItem(diveFilter.getOperator());

		searchCriteriaComponent.removeAllItems();
		KeyedCatalog[] values = (((KeyedCatalogFilterInspector) diveFilter
				.getDiveFilterType().getInspector())).getCatalogValues();
		Arrays.sort(values, new KeyedCatalogComparator());
		for (KeyedCatalog item : values) {
			searchCriteriaComponent.addItem(item);
		}
		if (null != diveFilter.getCriteria())
			searchCriteriaComponent.setSelectedItem(diveFilter.getCriteria());

		propagateChanges = true;
	}

}
