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
package be.vds.jtbdive.client.view.core.divesite;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Coordinates;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.PinableObject;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.core.utils.StringManipulator;

public class DiveSiteChooserDialog extends PromptDialog {
	private static final long serialVersionUID = 518664082888436561L;
	private DiveSiteManagerFacade diveSiteManagerFacade;
	private JTextField nameTf;
	private DiveSiteTableModel diveSiteTableModel;
	private JXTable diveSiteTable;
	private int selectionPolicy = ListSelectionModel.SINGLE_SELECTION;
	private static final Syslog LOGGER = Syslog
			.getLogger(DiveSiteChooserDialog.class);

	public DiveSiteChooserDialog(DiveSiteManagerFacade diveLocationManagerFacade) {
		super(i18n.getString("divesite.selection"), i18n
				.getString("divesite.selection.message"), UIAgent.getInstance()
				.getBufferedImage(UIAgent.ICON_DIVE_SITE_DETAIL_48),
				PromptDialog.MODE_OK_CANCEL, null);
		initValues(diveLocationManagerFacade);
	}

	public DiveSiteChooserDialog(Frame parentFrame,
			DiveSiteManagerFacade diveLocationManagerFacade) {
		super(parentFrame, i18n.getString("divesite.selection"), i18n
				.getString("divesite.selection.message"), UIAgent.getInstance()
				.getBufferedImage(UIAgent.ICON_DIVE_SITE_DETAIL_48),
				PromptDialog.MODE_OK_CANCEL, null);
		initValues(diveLocationManagerFacade);
	}

	public DiveSiteChooserDialog(Dialog parentDialog,
			DiveSiteManagerFacade diveLocationManagerFacade) {
		super(parentDialog, i18n.getString("divesite.selection"), i18n
				.getString("divesite.selection.message"), UIAgent.getInstance()
				.getBufferedImage(UIAgent.ICON_DIVE_SITE_DETAIL_48),
				PromptDialog.MODE_OK_CANCEL, null);
		initValues(diveLocationManagerFacade);
	}

	private void initValues(DiveSiteManagerFacade diveSiteManagerFacade) {
		this.diveSiteManagerFacade = diveSiteManagerFacade;
		setOkButtonEnabled(false);
		this.setIconImage(UIAgent.getInstance().getBufferedImage(
				UIAgent.ICON_DIVE_SITE_DETAIL_16));
	}

	@Override
	protected Component createContentPanel() {
		JPanel p = new DetailPanel(new BorderLayout());
		// p.setOpaque(false);
		p.add(createSearch(), BorderLayout.NORTH);
		p.add(createTable(), BorderLayout.CENTER);
		return p;
	}

	private Component createTable() {
		diveSiteTableModel = new DiveSiteTableModel() {
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			};
		};
		diveSiteTable = new JXTable(diveSiteTableModel);
		diveSiteTable.addHighlighter(HighlighterFactory
				.createAlternateStriping());
		diveSiteTable.getColumn(DiveSiteTableModel.INDEX_PINABLE)
				.setCellRenderer(new DefaultTableCellRenderer() {
					@Override
					public Component getTableCellRendererComponent(
							JTable table, Object value, boolean isSelected,
							boolean hasFocus, int row, int column) {
						JLabel l = (JLabel) super
								.getTableCellRendererComponent(table, value,
										isSelected, hasFocus, row, column);
						PinableObject po = (PinableObject) value;
						if (null == po)
							l.setText(null);
						else {
							Coordinates c = po.getCoordinates();
							l.setText(StringManipulator.formatCoordinates(
									c.getLatitude(), c.getLongitude(), 5));
						}
						return this;
					}
				});
		diveSiteTable.setSelectionMode(selectionPolicy);
		diveSiteTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()) {
							setOkButtonEnabled(diveSiteTable.getSelectedRow() > -1);
						}
					}
				});
		diveSiteTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (diveSiteTable.getSelectedRow() > -1
						&& e.getClickCount() == 2) {
					setReturnOption(OPTION_OK);
					dispose();
				}
			}
		});

		JScrollPane scroll = new JScrollPane(diveSiteTable);
		SwingComponentHelper.displayJScrollPane(scroll);

		JPanel p = new DetailPanel(new BorderLayout());
		p.add(scroll, BorderLayout.CENTER);
		return p;
	}

	private Component createSearch() {
		JPanel selectionPanel = new JPanel(new GridBagLayout());
		selectionPanel.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();

		GridBagLayoutManager.addComponent(selectionPanel, createNameTf(), c, 1,
				0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(selectionPanel, createSearchButton(),
				c, 2, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.FIRST_LINE_END);

		return selectionPanel;
	}

	private Component createSearchButton() {
		Action action = new AbstractAction() {

			private static final long serialVersionUID = 5853883483464296326L;

			@Override
			public void actionPerformed(ActionEvent e) {
				lookup();
			}
		};
		action.putValue(Action.LARGE_ICON_KEY,
				UIAgent.getInstance().getIcon(UIAgent.ICON_SEARCH_16));
		JButton searchButton = new JButton(action);
		searchButton.setContentAreaFilled(false);
		return searchButton;
	}

	private Component createNameTf() {
		nameTf = new JTextField(15);
		nameTf.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					lookup();
				}
			}
		});
		return nameTf;
	}

	private void lookup() {
		List<DiveSite> list;
		try {
			list = diveSiteManagerFacade.findDiveSitesByName(nameTf.getText());
			diveSiteTableModel.setData(list);
		} catch (DataStoreException e) {
			LOGGER.error(e.getMessage());
			ExceptionDialog.showDialog(e, this);
		}
	}

	public DiveSite getSelectedDiveSite() {
		int selectedRow = diveSiteTable.getSelectedRow();
		int realRow = diveSiteTable.convertRowIndexToModel(selectedRow);
		return diveSiteTableModel.getDiveSiteAt(realRow);
	}

	public void setSelectionPolicy(int selectionPolicy) {
		this.selectionPolicy = selectionPolicy;
	}

	public void setDiveSites(List<DiveSite> l) {
		diveSiteTableModel.setData(l);
	}

	public void setSearchCriteria(String search) {
		nameTf.setText(search);
	}
}
