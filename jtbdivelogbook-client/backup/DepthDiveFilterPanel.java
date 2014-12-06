package be.vds.jtbdive.client.view.core.search.filterpanels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.core.filters.DepthDiveFilter;
import be.vds.jtbdive.client.core.filters.DiveFilterOperator;
import be.vds.jtbdive.client.core.filters.NumericDiveFilter;
import be.vds.jtbdive.client.core.filters.NumericOperator;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;

public class DepthDiveFilterPanel extends DiveFilterTypePanel {

	private static final long serialVersionUID = 6780863589842705436L;
	private JSpinner searchCriteriaComponent;
	private JComboBox operatorCb;
	private boolean propagateChanges = true;
	private JLabel maxDepthUnitLabel;

	public DepthDiveFilterPanel() {
	}

	@Override
	public Component createFilterComponents() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		int y = 0;
		GridBagLayoutManager.addComponent(p,
				new I18nLabel("criteria.search"), gc, 0, y, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createSearchCriteriaTf(), gc,
				1, y, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, new I18nLabel("matching"), gc,
				0, ++y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createOperatorComponent(), gc,
				1, y, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, Box.createGlue(), gc, 0, y, 0,
				2, 0, 0, GridBagConstraints.BOTH, GridBagConstraints.WEST);

		updateUnits();
		
		return p;
	}

	private Component createOperatorComponent() {
		operatorCb = new JComboBox(NumericOperator.values());
		operatorCb.setRenderer(new KeyedCatalogRenderer());
		operatorCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (propagateChanges) {
					if (null != diveFilter) {
						diveFilter.setOperator((DiveFilterOperator) operatorCb.getSelectedItem());
						notifyChanges(diveFilter);
					}
				}
			}
		});
		return operatorCb;
	}

	private Component createSearchCriteriaTf() {
		double initial = UnitsAgent.getInstance().convertLengthFromModel(0d);
		double min = UnitsAgent.getInstance().convertLengthFromModel(-1000d);
		double max = UnitsAgent.getInstance().convertLengthFromModel(0d);

		SpinnerModel sm = new SpinnerNumberModel(initial, min, max, 0.5d);
		searchCriteriaComponent = new JSpinner(sm);
		searchCriteriaComponent.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (propagateChanges) {
					if (null != diveFilter) {
						Double d = (Double) searchCriteriaComponent.getValue();
						
						((DepthDiveFilter) diveFilter).setCriteriaValue(new Object[]{d, null});
						notifyChanges(diveFilter);
					}
				}
			}
		});

		JPanel p = new JPanel(new BorderLayout(2, 0));
		p.setOpaque(false);
		p.add(searchCriteriaComponent, BorderLayout.CENTER);
		maxDepthUnitLabel = new JLabel();
		p.add(maxDepthUnitLabel, BorderLayout.EAST);

		return p;
	}

	@Override
	public void clearComponents() {
		propagateChanges = false;
		searchCriteriaComponent.setValue(0);
		operatorCb.setSelectedIndex(-1);
		propagateChanges = true;
	}

	@Override
	public void fillComponents() {
		propagateChanges = false;
		searchCriteriaComponent.setValue(((NumericDiveFilter)diveFilter).getFirstCriteria());
		operatorCb.setSelectedItem(diveFilter.getOperator());
		propagateChanges = true;
	}
	
	public void updateUnits() {
		setLengthUnitLabel();
	}
	
	private void setLengthUnitLabel() {
		maxDepthUnitLabel.setText("("
				+ UnitsAgent.getInstance().getLengthUnit().getSymbol() + ")");
	}

}
