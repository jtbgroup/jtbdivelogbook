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
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.filters.DiveFilterOperator;
import be.vds.jtbdive.client.core.filters.DiveSiteDiveFilter;
import be.vds.jtbdive.client.core.filters.DiveSiteOperator;
import be.vds.jtbdive.client.core.filters.DiverDiveFilter;
import be.vds.jtbdive.client.view.core.diver.DiverChooser;
import be.vds.jtbdive.client.view.core.divesite.DiveSiteChooser;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;

public class DiveSiteDiveFilterPanel extends DiveFilterTypePanel {

	private static final long serialVersionUID = 8777175272246131169L;
	private DiveSiteChooser searchCriteriaTf;
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

	public void setDiveSiteManagerFacade(DiveSiteManagerFacade diveSiteManagerFacade) {
		searchCriteriaTf.setDiveSiteManagerFacade(diveSiteManagerFacade);
	}

	private Component createOperatorComponent() {
		operatorCb = new JComboBox(DiveSiteOperator.values());
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
//		Window parent = WindowUtils.getTopLevelWindow(this);
		searchCriteriaTf = new DiveSiteChooser(null);

		searchCriteriaTf
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (DiverChooser.DIVER_CHANGED_PROPERTY.equals(evt
								.getPropertyName()) && propagateChanges) {
							if (null != diveFilter) {
								((DiveSiteDiveFilter) diveFilter)
										.setDiveSiteCriteria(searchCriteriaTf
												.getDiveSite());
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
		searchCriteriaTf.setDiveSite(null);
		operatorCb.setSelectedIndex(-1);
		propagateChanges = true;
	}

	@Override
	public void fillComponents() {
		propagateChanges = false;
		searchCriteriaTf.setDiveSite(((DiveSiteDiveFilter) diveFilter)
				.getDiveSiteCriteria());
		operatorCb.setSelectedItem(diveFilter.getOperator());
		propagateChanges = true;
	}

}
