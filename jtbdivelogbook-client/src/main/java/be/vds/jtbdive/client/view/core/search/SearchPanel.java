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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.core.event.LogBookEvent;
import be.vds.jtbdive.client.core.filters.AggregatorDiveFilter;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.table.DiveTableModel;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.comparator.DiveNumberComparator;

public class SearchPanel extends JPanel implements Observer {

	private static final long serialVersionUID = -8199077441769882499L;
	private LogBookManagerFacade logBookManagerFacade;
	private DiveTableModel model;
	private JXTable table;
	private AggregatorDiveFilter filters;
	private I18nLabel filterLabel;
	private JButton searchButton;
	private JButton criteriasButton;
	private I18nLabel numberOfDiveLabel;
	private DiverManagerFacade diverManagerFacade;
	private DiveSiteManagerFacade diveSiteManagerFacade;

	public SearchPanel(LogBookManagerFacade logBookManagerFacade,
			DiverManagerFacade diverManagerFacade,
			DiveSiteManagerFacade diveSiteManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
		this.diverManagerFacade = diverManagerFacade;
		this.diveSiteManagerFacade = diveSiteManagerFacade;
		this.logBookManagerFacade.addObserver(this);
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		this.setBackground(UIAgent.getInstance().getColorBaseBackground());
		this.add(createHeader(), BorderLayout.NORTH);
		this.add(createContent(), BorderLayout.CENTER);

		UnitsAgent.getInstance().addObserver(this);
		UIAgent.getInstance().addObserver(this);
	}

	private Component createRowNumber() {
		numberOfDiveLabel = new I18nLabel("dives.numberof.param");
		numberOfDiveLabel.setTextParameters(new Object[] { "0" });

		return numberOfDiveLabel;
	}

	private Component createHeader() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setOpaque(false);

		GridBagConstraints gc = new GridBagConstraints();

		gc.insets = new Insets(0, 0, 0, 3);
		GridBagLayoutManager.addComponent(p, createChooseCriteriasButton(), gc,
				0, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createSearchButton(), gc, 1, 0, 1,
				1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createFilterLabel(), gc, 2, 0, 1,
				1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.EAST);

		GridBagLayoutManager.addComponent(p, Box.createHorizontalGlue(), gc, 3,
				0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, createRowNumber(), gc, 4, 0, 1, 1,
				0, 0, GridBagConstraints.NONE, GridBagConstraints.EAST);

		enableCriteriaButton(false);

		return p;
	}

	private Component createChooseCriteriasButton() {
		criteriasButton = new JButton(new AbstractAction(null, UIAgent
				.getInstance().getIcon(UIAgent.ICON_FILTER_16)) {

			private static final long serialVersionUID = -512468444010996740L;
			private DiveFiltersDialog filtersDialog;

			@Override
			public void actionPerformed(ActionEvent e) {
				// if (null == searchDialog) {
				buildSearchDialog();
				// }

				int i = filtersDialog.showDialog(500, 450);
				if (i == PromptDialog.OPTION_OK) {
					filters = filtersDialog.getDiveFilterGroup();
					adaptFilterLabel();
				}
			}

			private void buildSearchDialog() {
				filtersDialog = new DiveFiltersDialog(
						WindowUtils.getParentFrame(SearchPanel.this),
						diverManagerFacade, diveSiteManagerFacade);

				boolean filterDefined = (filters == null);
				if (filterDefined) {
					filtersDialog.setDefaultFilters();
				} else {
					filtersDialog.setDiveFilters(filters);
				}
				enableSearchButton(filterDefined);
			}
		});

		return criteriasButton;
	}

	private Component createFilterLabel() {
		filterLabel = new I18nLabel("no.filter");
		return filterLabel;
	}

	private Component createSearchButton() {
		searchButton = new JButton(new AbstractAction(null, UIAgent
				.getInstance().getIcon(UIAgent.ICON_SEARCH_16)) {

			private static final long serialVersionUID = -2384716377613012752L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (filters == null)
					return;

				List<Dive> dives = new ArrayList<Dive>();
				for (Dive dive : logBookManagerFacade.getCurrentLogBook()
						.getDives()) {
					if (filters.isValid(dive)) {
						dives.add(dive);
					}
				}

				Collections.sort(dives, new DiveNumberComparator());
				model.setData(dives);
				adaptRowCountLabel();
			}
		});
		searchButton.setEnabled(false);
		return searchButton;
	}

	private void adaptFilterLabel() {
		if (filters == null) {
			filterLabel.setTextBundleKey("no.filter");
		} else {
			filterLabel.setTextBundleKey("filter.defined");
		}
	}

	private Component createContent() {
		model = new DiveTableModel();
		table = new JXTable(model);
		table.setColumnControlVisible(true);
		model.setRenderer(table);
		table.addHighlighter(HighlighterFactory.createAlternateStriping());

		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() >= 2) {
					openDive();
				}
			}
		});

		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					openDive();
				}
			}
		});

		JScrollPane scroll = new JScrollPane(table);
		SwingComponentHelper.displayJScrollPane(scroll);
		return scroll;
	}

	private void openDive() {
		Dive dive = model.getDiveAt(table.convertRowIndexToModel(table
				.getSelectedRow()));
		if (dive != null) {
			logBookManagerFacade.setCurrentDive(dive);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (UnitsAgent.getInstance().equals(o)
				&& arg.equals(UnitsAgent.UNITS_CHANGED)) {
			repaintTableHeaders();
			model.fireTableDataChanged();
		} else if (arg instanceof LogBookEvent) {
			LogBookEvent event = (LogBookEvent) arg;
			if (event.getType().equals(LogBookEvent.LOGBOOK_LOADED)) {
				enableCriteriaButton(true);
				clear();
			} else if (event.getType().equals(LogBookEvent.LOGBOOK_DELETED)
					|| event.getType().equals(LogBookEvent.LOGBOOK_CLOSED)) {
				clear();
				enableCriteriaButton(false);
			}
		} else if (UIAgent.getInstance().equals(o)
				&& arg.equals(UIAgent.CHANGE_DATE_FORMAT_DAY_HOUR)) {
			model.fireTableDataChanged();
		}
	}

	private void clear() {
		model.setData(null);
		filters = null;
		enableSearchButton(false);
		adaptRowCountLabel();
		adaptFilterLabel();
	}

	public void enableSearchButton(boolean b) {
		searchButton.setEnabled(b);
	}

	public void enableCriteriaButton(boolean b) {
		criteriasButton.setEnabled(b);
		if (!b)
			enableSearchButton(false);
	}

	private void adaptRowCountLabel() {
		numberOfDiveLabel.setTextParameters(model.getRowCount());
		numberOfDiveLabel.revalidate();
		numberOfDiveLabel.repaint();
	}

	@Override
	public void updateUI() {
		super.updateUI();
		if (table != null) {
			repaintTableHeaders();
			model.fireTableDataChanged();
		}
	}

	private void repaintTableHeaders() {
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(i).setHeaderValue(model.getColumnName(i));
		}
	}

}
