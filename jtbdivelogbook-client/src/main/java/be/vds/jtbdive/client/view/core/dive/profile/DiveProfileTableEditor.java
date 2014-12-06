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
package be.vds.jtbdive.client.view.core.dive.profile;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class DiveProfileTableEditor extends JPanel implements Observer {

	private static final long serialVersionUID = 8315529920577347924L;
	private DiveProfileEditor diveProfileEditor;
	private DiveProfileTableModel diveProfileTableModel;
	private JXTable table;

	public DiveProfileTableEditor(DiveProfileEditor diveProfileEditor) {
		registerDiveProfileEditor(diveProfileEditor);
		init();
	}

	public void registerDiveProfileEditor(DiveProfileEditor diveProfileEditor) {
		if (this.diveProfileEditor != null)
			this.diveProfileEditor.deleteObserver(this);

		this.diveProfileEditor = diveProfileEditor;

		if (this.diveProfileEditor != null)
			this.diveProfileEditor.addObserver(this);

		if (null != diveProfileTableModel)
			diveProfileTableModel.setDiveProfileEditor(this.diveProfileEditor);
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.add(createTable(), BorderLayout.CENTER);
	}

	private Component createTable() {
		diveProfileTableModel = new DiveProfileTableModel(diveProfileEditor);
		table = new JXTable(diveProfileTableModel);
		table.addHighlighter(HighlighterFactory.createAlternateStriping());

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						int row = table.getSelectedRow();
						if (row > -1)
							diveProfileEditor.entrySelected(
									diveProfileTableModel.getProfileEntry(table
											.convertRowIndexToModel(row)),
									diveProfileTableModel);
					}
				});

		KeyListener kl = createInsertKeyAdapter();
		table.addKeyListener(kl);

		JScrollPane scroll = new JScrollPane(table);
		scroll.getViewport().addKeyListener(kl);
		scroll.addKeyListener(kl);
		return scroll;
	}

	private KeyListener createInsertKeyAdapter() {
		return new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_INSERT) {
					int row = table.getSelectedRow();
					if (row > -1) {
						row = table.convertRowIndexToModel(row);
					}
					ProfileEntry pe = diveProfileTableModel
							.getProfileEntry(row);
					diveProfileTableModel.addEntryAfter(pe);
				} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					int originalRow = table.getSelectedRow();
					int row = originalRow;
					if (row > -1) {
						row = table.convertRowIndexToModel(row);
					}
					ProfileEntry pe = diveProfileTableModel
							.getProfileEntry(row);
					diveProfileTableModel.removeEntry(pe);

					if (originalRow >= table.getRowCount()) {
						originalRow = table.getRowCount() - 1;
					}
					table.getSelectionModel().setSelectionInterval(originalRow,
							originalRow);
				}
			}
		};
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof DiveProfileEditionEvent) {
			DiveProfileEditionEvent event = (DiveProfileEditionEvent) arg;
			if (event.getType() == DiveProfileEditionEvent.ENTRY_SELECTED) {
				selectEntryInTable((ProfileEntry) event.getNewValue());
			} else if (event.getType() == DiveProfileEditionEvent.ENTRY_REMOVED
					|| event.getType() == DiveProfileEditionEvent.DIVE_PROFILE_SET) {
				reloadTable();
			}
		}
	}

	private void reloadTable() {
		diveProfileTableModel.reloadDiveProfile();
		table.setSortOrder(0, SortOrder.ASCENDING);
	}

	private void selectEntryInTable(ProfileEntry entry) {
		int i = diveProfileTableModel.getRowForEntry(entry);
		if (i > -1) {
			i = table.convertRowIndexToView(i);
			table.getSelectionModel().setSelectionInterval(i, i);
		} else {
			table.getSelectionModel().setSelectionInterval(-1, -1);
		}
		table.scrollRowToVisible(i);
	}

}
