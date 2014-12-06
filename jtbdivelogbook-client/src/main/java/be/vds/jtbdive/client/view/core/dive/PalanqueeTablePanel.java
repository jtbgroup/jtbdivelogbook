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
package be.vds.jtbdive.client.view.core.dive;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.events.LogBookUiEventHandler;
import be.vds.jtbdive.client.view.table.PalanqueeTableModel;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.Palanquee;
import be.vds.jtbdive.core.core.PalanqueeEntry;

public class PalanqueeTablePanel extends JPanel {

	private static final long serialVersionUID = 8216213714109385878L;
	private static final Font font = new Font("Courier", Font.PLAIN, 10);
	private PalanqueeTableModel palanqueeTableModel;
	private JXTable palanqueeTable;
	private JXButton removeDiverButton;
	private JXButton editDiverButton;
	private DiverManagerFacade diverManagerFacade;
	private Window parentWindow;
	private Palanquee currentPalanquee;
	private boolean enableChangePropagation;
	private Dive currentDive;
	private LogBookManagerFacade logBookManagerFacade;
	private JScrollPane palanqueeScroll;

	public PalanqueeTablePanel(Window parentWindow,
			LogBookManagerFacade logBookManagerFacade,
			DiverManagerFacade diverManagerFacade) {
		this.parentWindow = parentWindow;
		this.logBookManagerFacade = logBookManagerFacade;
		this.diverManagerFacade = diverManagerFacade;
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2);

		GridBagLayoutManager.addComponent(this, createPalanqueeTable(), c, 1,
				0, 1, 4, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(this, createAddDiverButton(), c, 0,
				0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(this, createEditDiverButton(), c, 0,
				1, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(this, createRemoveDiverButton(), c,
				0, 2, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);
	}

	private Component createPalanqueeTable() {
		palanqueeTableModel = new PalanqueeTableModel();
		palanqueeTable = new JXTable(palanqueeTableModel);
		palanqueeTableModel.setRenderer(palanqueeTable);
		palanqueeTable.addHighlighter(HighlighterFactory
				.createAlternateStriping());
		palanqueeTable.setColumnControlVisible(true);

		palanqueeTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent lse) {
						if (!lse.getValueIsAdjusting()) {
							boolean b = palanqueeTable.getSelectedRow() >= 0;
							editDiverButton.setEnabled(b);
							removeDiverButton.setEnabled(b);

							if (b) {
								int realRow = palanqueeTable
										.convertRowIndexToModel(palanqueeTable
												.getSelectedRow());
								PalanqueeEntry pe = palanqueeTableModel
										.getPalanqueeEntryAt(realRow);
								if (null != pe) {
									LogBookUiEventHandler.getInstance()
											.notifyDiverSelected(
													PalanqueeTablePanel.this,
													pe.getDiver());
								} else {
									LogBookUiEventHandler.getInstance()
											.notifyDiverSelected(
													PalanqueeTablePanel.this,
													null);
								}
							} else {
								LogBookUiEventHandler.getInstance()
										.notifyDiverSelected(
												PalanqueeTablePanel.this, null);
							}
						}
					}
				});

		palanqueeScroll = new JScrollPane(palanqueeTable);
		return palanqueeScroll;
	}

	private JComponent createEditDiverButton() {
		editDiverButton = new JXButton(new AbstractAction() {

			private static final long serialVersionUID = -2978083749323114555L;

			@Override
			public void actionPerformed(ActionEvent e) {
				PalanqueeSelectionPanel dsp = new PalanqueeSelectionPanel(
						diverManagerFacade);
				int realRow = palanqueeTable
						.convertRowIndexToModel(palanqueeTable.getSelectedRow());
				PalanqueeEntry pe = palanqueeTableModel
						.getPalanqueeEntryAt(realRow);
				dsp.setPalanqueeEntry(pe);
				int i = dsp.showDialog(350, 300);

				if (i == PalanqueeSelectionPanel.OPTION_OK) {
					currentPalanquee.removePalanqueeEntry(pe);
					currentPalanquee.addPalanqueeEntry(dsp.getPalanqueeEntry());
					if (enableChangePropagation) {
						logBookManagerFacade.setDiveChanged(currentDive);
					}

					updatePalanqueeTableModel();
				}
			}
		});
		editDiverButton.setText("%");
		editDiverButton.setFont(font);
		editDiverButton.setEnabled(false);
		return editDiverButton;
	}

	private JComponent createRemoveDiverButton() {
		removeDiverButton = new JXButton(new AbstractAction() {

			private static final long serialVersionUID = -7856808005856702035L;

			@Override
			public void actionPerformed(ActionEvent e) {
				int[] diversRows = palanqueeTable.getSelectedRows();
				List<PalanqueeEntry> list = new ArrayList<PalanqueeEntry>();
				for (int i = 0; i < diversRows.length; i++) {
					int realRow = palanqueeTable
							.convertRowIndexToModel(diversRows[i]);
					list.add(palanqueeTableModel.getPalanqueeEntryAt(realRow));
				}

				for (PalanqueeEntry palanqueeEntry : list) {
					currentPalanquee.removePalanqueeEntry(palanqueeEntry);
				}
				if (enableChangePropagation) {
					logBookManagerFacade.setDiveChanged(currentDive);
				}
				updatePalanqueeTableModel();
			}
		});
		removeDiverButton.setText("-");
		removeDiverButton.setFont(font);
		removeDiverButton.setEnabled(false);
		return removeDiverButton;
	}

	private JXButton createAddDiverButton() {
		JXButton addDiverButton = new JXButton(new AbstractAction() {

			private static final long serialVersionUID = -6326694590081693397L;

			@Override
			public void actionPerformed(ActionEvent e) {
				PalanqueeSelectionPanel dsp = new PalanqueeSelectionPanel(
						diverManagerFacade);
				dsp.enableDiverSelection(true);
				int i = dsp.showDialog(350, 300);
				if (i == PalanqueeSelectionPanel.OPTION_OK) {
					currentPalanquee.addPalanqueeEntry(dsp.getPalanqueeEntry());
					if (enableChangePropagation) {
						logBookManagerFacade.setDiveChanged(currentDive);
					}
					updatePalanqueeTableModel();
				}
			}
		});
		addDiverButton.setText("+");
		addDiverButton.setFont(font);
		return addDiverButton;
	}

	public void setPalanquee(Palanquee palanquee, Dive currentDive) {
		enableChangePropagation(false);
		this.currentPalanquee = palanquee;
		this.currentDive = currentDive;
		updatePalanqueeTableModel();
		enableChangePropagation(true);
	}

	private void updatePalanqueeTableModel() {
		palanqueeTableModel.setPalanquee(currentPalanquee);
	}

	public Palanquee getPalanquee() {
		return currentPalanquee;
	}

	private void enableChangePropagation(boolean b) {
		this.enableChangePropagation = b;
	}

	public void setTableOpaque(boolean isOpaque) {
		palanqueeScroll.setOpaque(isOpaque);
		palanqueeScroll.getViewport().setOpaque(isOpaque);
		palanqueeTable.setOpaque(isOpaque);
	}

	public void removeTableBorder() {
		palanqueeScroll.setBorder(null);
		palanqueeScroll.getViewport().setBorder(null);
	}

	@Override
	public void updateUI() {
		super.updateUI();
		if (palanqueeTable != null) {
			repaintTableHeaders();
			palanqueeTableModel.fireTableDataChanged();
		}
	}

	private void repaintTableHeaders() {
		for (int i = 0; i < palanqueeTable.getColumnCount(); i++) {
			palanqueeTable.getColumn(i).setHeaderValue(
					palanqueeTableModel.getColumnName(i));
		}
	}
}
