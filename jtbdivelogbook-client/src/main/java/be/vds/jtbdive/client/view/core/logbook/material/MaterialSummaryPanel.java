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
package be.vds.jtbdive.client.view.core.logbook.material;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTable;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.client.view.renderer.MaterialIconTableCellRenderer;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.core.core.material.Material;

public class MaterialSummaryPanel extends JPanel {

	private static final long serialVersionUID = 1203743382200615194L;
	private MaterialTableModel materialTableModel;
	private JLabel rowCountLabel;
	private JLabel totalPriceLabel;
	private JXTable materialTable;

	public MaterialSummaryPanel() {
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.add(createCentralTable(), BorderLayout.CENTER);
		this.add(createInformationsBar(), BorderLayout.SOUTH);
	}

	private Component createInformationsBar() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		GridBagLayoutManager
				.addComponent(p, createRowCounter(), gc, 0, 0, 1, 1, 1, 0,
						GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
		GridBagLayoutManager
				.addComponent(p, createTotalPrice(), gc, 0, 1, 1, 1, 1, 0,
						GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
		p.setOpaque(false);
		return p;
	}

	private Component createTotalPrice() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		p.setOpaque(false);
		p.add(new I18nLabel("price"));
		totalPriceLabel = new JLabel("0");
		p.add(totalPriceLabel);
		p.add(new JLabel("euros"));
		return p;
	}

	private Component createRowCounter() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		p.setOpaque(false);
		p.add(new I18nLabel("rows"));
		rowCountLabel = new JLabel("0");
		p.add(rowCountLabel);
		return p;
	}

	private Component createCentralTable() {
		materialTableModel = new MaterialTableModel();
		materialTable = new JXTable(materialTableModel);
		materialTable.getColumn(0).setCellRenderer(
				new MaterialIconTableCellRenderer());
		materialTable.setAutoResizeMode(JXTable.AUTO_RESIZE_ALL_COLUMNS);

		JScrollPane scroll = new JScrollPane(materialTable);
		SwingComponentHelper.displayJScrollPane(scroll);
		return scroll;
	}

	public void setMaterials(List<Material> materials) {
		materialTableModel.setData(materials);
		if (materials == null) {
			rowCountLabel.setText("0");
			totalPriceLabel.setText("0");
		} else {
			rowCountLabel.setText(String.valueOf(materials.size()));
			totalPriceLabel.setText(String.valueOf(LogBookUtilities
					.getTotalPriceForMaterials(materials)));
		}
	}

	@Override
	public void updateUI() {
		super.updateUI();
		repaintTableHeaders();
		// materialTableModel.fireTableDataChanged();
	}

	private void repaintTableHeaders() {
		if (materialTable != null) {
			for (int i = 0; i < materialTableModel.getColumnCount(); i++) {
				materialTable.getColumn(i).setHeaderValue(
						materialTableModel.getColumnName(i));
			}
		}
	}

}
