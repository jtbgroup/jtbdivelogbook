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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.filters.DiveFilter;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;

public abstract class FilterNaturePanel extends JPanel {

	private static final long serialVersionUID = -7460869112008047966L;

	protected DiveFilter diveFilter;

	public static final String FILTER_CHANGED = "filter.changed";
	private I18nLabel titleLabel;

	private I18nLabel filterTypeLabel;

	public FilterNaturePanel() {
		initComponents();
	}

	public void setDiveFilter(DiveFilter diveFilter) {
		this.diveFilter = diveFilter;
		clearComponents();
		fillComponents();
		setTitle();
	}

	private void setTitle() {
		if (diveFilter == null || diveFilter.getDiveFilterType() == null) {
			titleLabel.setText(null);
			filterTypeLabel.setText(null);
		} else {
			titleLabel
					.setTextBundleKey(diveFilter.getDiveFilterType().getKey());
			filterTypeLabel.setTextBundleKey("filter.type");
		}
		this.revalidate();
	}

	protected void initAttributes(Object[] objs) {

	}

	private void initComponents() {
		// this.setLayout(new BorderLayout());
		// this.add(createTitlePanel(), BorderLayout.NORTH);
		// this.add(createFilterComponents(), BorderLayout.CENTER);

		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		GridBagLayoutManager.addComponent(this, createTitlePanel(), gc, 0, 0,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, createFilterScrollComponents(),
				gc, 0, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, Box.createGlue(), gc, 0, 2, 1,
				1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

	}

	private Component createFilterScrollComponents() {
		JScrollPane scroll = new JScrollPane(createFilterComponents());
		SwingComponentHelper.displayJScrollPane(scroll);
		return scroll;
	}

	private Component createTitlePanel() {
		titleLabel = new I18nLabel();
		titleLabel.setFont(UIAgent.getInstance().getFontTitleDetail());

		filterTypeLabel = new I18nLabel();
		filterTypeLabel.setFont(UIAgent.getInstance().getFontNormalItalic());

		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		headerPanel.setOpaque(false);
		headerPanel.add(filterTypeLabel);
		headerPanel.add(titleLabel);

		return headerPanel;
	}

	public abstract Component createFilterComponents();

	public abstract void clearComponents();

	public abstract void fillComponents();

	public void notifyChanges(Object modifiedObject) {
		firePropertyChange(FILTER_CHANGED, null, diveFilter);
	}
}
