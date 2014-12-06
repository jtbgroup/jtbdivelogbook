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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.filters.DiveFilterOperator;
import be.vds.jtbdive.client.core.filters.DiverDiveFilter;
import be.vds.jtbdive.client.core.filters.DiverOperator;
import be.vds.jtbdive.client.view.core.diver.DiverChooser;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;

public class DiverDiveFilterPanel extends DiveFilterTypePanel {

	private static final long serialVersionUID = 8777175272246131169L;
	private DiverChooser searchCriteriaTf;
	private JComboBox operatorCb;
	private boolean propagateChanges = true;

	@Override
	public Component createFilterComponents() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		int y = 0;
		GridBagLayoutManager.addComponent(p, new I18nLabel("criteria.search"),
				gc, 0, y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createSearchCriteriaTf(), gc, 1,
				y, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, new I18nLabel("matching"), gc, 0,
				++y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createOperatorComponent(), gc, 1,
				y, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, Box.createGlue(), gc, 0, y, 0, 2,
				0, 0, GridBagConstraints.BOTH, GridBagConstraints.WEST);

		return p;

	}

	public void setDiverManagerFacade(DiverManagerFacade diverManagerFacade) {
		searchCriteriaTf.setDiveManagerFacade(diverManagerFacade);
	}

	private Component createOperatorComponent() {
		operatorCb = new JComboBox(DiverOperator.values());
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
		searchCriteriaTf = new DiverChooser(null);

		searchCriteriaTf
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (DiverChooser.DIVER_CHANGED_PROPERTY.equals(evt
								.getPropertyName()) && propagateChanges) {
							if (null != diveFilter) {
								((DiverDiveFilter) diveFilter)
										.setDiverCriteria(searchCriteriaTf
												.getDiver());
								firePropertyChange(FILTER_CHANGED, null,
										diveFilter);
							}
						}
					}
				});

		return searchCriteriaTf;
	}

	@Override
	public void clearComponents() {
		propagateChanges = false;
		searchCriteriaTf.setDiver(null);
		operatorCb.setSelectedIndex(-1);
		propagateChanges = true;
	}

	@Override
	public void fillComponents() {
		propagateChanges = false;
		searchCriteriaTf.setDiver(((DiverDiveFilter) diveFilter)
				.getDiverCriteria());
		operatorCb.setSelectedItem(diveFilter.getOperator());
		propagateChanges = true;
	}

}
