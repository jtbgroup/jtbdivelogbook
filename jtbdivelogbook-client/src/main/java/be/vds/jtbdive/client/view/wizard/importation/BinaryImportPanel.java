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
package be.vds.jtbdive.client.view.wizard.importation;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXTable;

import be.vds.jtbdive.client.view.table.BinaryLogBookTableModel;
import be.vds.jtbdive.client.view.wizard.WizardPanel;
import be.vds.jtbdive.core.core.divecomputer.binary.BinaryLogBook;

public class BinaryImportPanel extends WizardPanel {

	private static final long serialVersionUID = -6934344741975378144L;
	private BinaryLogBookTableModel binaryTableModel;
	private JXTable binarySelectionTable;

	public void setBinaryValues(List<BinaryLogBook> binaryLogBooks) {
		binaryTableModel.setData(binaryLogBooks);
	}

	@Override
	public String getMessage() {
		return "Choose a binary dataset.";
	}

	@Override
	public JComponent createContentPanel() {
		binaryTableModel = new BinaryLogBookTableModel();
		binarySelectionTable = new JXTable(binaryTableModel);
		binarySelectionTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		binarySelectionTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()) {
							fireCompletionChanged(isComplete());
						}
					}

				});

		JScrollPane scroll = new JScrollPane(binarySelectionTable);
		return scroll;
	}

	public boolean isComplete() {
		return binarySelectionTable.getSelectedRow() > -1;
	}

	public BinaryLogBook getSelectedBinaryLogBook() {
		int row = (binarySelectionTable.getSelectedRow());
		if (row == -1)
			return null;
		return binaryTableModel.getBinaryLogBookAt(binarySelectionTable
				.convertRowIndexToModel(row));
	}



}
