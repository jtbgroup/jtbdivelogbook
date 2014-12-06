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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.comparator.OrderedCatalogComparator;
import be.vds.jtbdive.client.core.filters.IntegerDiveFilter;
import be.vds.jtbdive.client.core.filters.inspector.IntegerFilterInspector;
import be.vds.jtbdive.client.core.filters.operator.DiveFilterOperator;
import be.vds.jtbdive.client.core.filters.operator.NumericOperator;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;

public class IntegerFilterPanel extends FilterNaturePanel {

	private static final long serialVersionUID = 6780863589842705436L;
	private JSpinner searchCriteriaComponent;
	private JComboBox operatorCb;
	private boolean propagateChanges = true;
	private JLabel unitLabel;
	private SpinnerNumberModel spinnerModel;

	public IntegerFilterPanel() {
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

		updateUnits();

		return p;
	}

	private Component createOperatorComponent() {
		DiveFilterOperator[] values = NumericOperator.values();
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
		spinnerModel = new SpinnerNumberModel();
		searchCriteriaComponent = new JSpinner(spinnerModel);
		searchCriteriaComponent.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (propagateChanges) {
					Integer d = (Integer) searchCriteriaComponent.getValue();
					((IntegerDiveFilter) diveFilter).setCriteriaValue(d);

					notifyChanges(diveFilter);
				}
			}
		});

		JPanel p = new JPanel(new BorderLayout(2, 0));
		p.setOpaque(false);
		p.add(searchCriteriaComponent, BorderLayout.CENTER);
		unitLabel = new JLabel();
		p.add(unitLabel, BorderLayout.EAST);

		return p;
	}

	@Override
	public void clearComponents() {
		propagateChanges = false;

		IntegerFilterInspector inspector = (IntegerFilterInspector) diveFilter
				.getDiveFilterType().getInspector();
		// double initial =
		// UnitsAgent.getInstance().convertLengthFromModel(inspector.getInitialValue());
		spinnerModel.setValue(inspector.getInitialValue());
		unitLabel.setText(null);
		// searchCriteriaComponent.setValue(0);
		operatorCb.setSelectedIndex(-1);
		propagateChanges = true;
	}

	@Override
	public void fillComponents() {
		propagateChanges = false;

		IntegerFilterInspector inspector = (IntegerFilterInspector) diveFilter
				.getDiveFilterType().getInspector();
		adaptSpinnerModel(inspector);

		// searchCriteriaComponent.setValue(((NumericDiveFilter) diveFilter)
		// .getFirstCriteria());
		operatorCb.setSelectedItem(diveFilter.getOperator());

		propagateChanges = true;
	}

	private void adaptSpinnerModel(IntegerFilterInspector inspector) {
		// double minimum = UnitsAgent.getInstance()
		// .convertLengthFromModel(min);
		// double maximum =
		// UnitsAgent.getInstance().convertLengthFromModel(max);
		// double initial =
		// UnitsAgent.getInstance().convertLengthFromModel(value);
		IntegerFilterInspector di = (IntegerFilterInspector) inspector;
		int value = ((IntegerDiveFilter) diveFilter).getFirstCriteria();
		int minimum = di.getMinimumValue();
		int maximum = di.getMaximumValue();
		int step = di.getStepValue();

		spinnerModel = new SpinnerNumberModel(value, minimum, maximum, step);

		searchCriteriaComponent.setModel(spinnerModel);
		setLengthUnitLabel();
	}

	public void updateUnits() {
		setLengthUnitLabel();
	}

	private void setLengthUnitLabel() {
		if (diveFilter != null) {
			IntegerFilterInspector di = (IntegerFilterInspector) diveFilter
					.getDiveFilterType().getInspector();
			if (di.getUnitSymbol() != null) {
				unitLabel.setText("(" + di.getUnitSymbol() + ")");
			}
		}
	}

}
