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
package be.vds.jtbdive.client.view.components;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import be.vds.jtbdive.client.view.renderer.MaterialListCellRenderer;
import be.vds.jtbdive.core.core.material.Material;

public class MaterialChooserDialog extends PromptDialog {

	private static final long serialVersionUID = -3447053744383478757L;
	private DefaultListModel listModel;
	private JList list;

	public MaterialChooserDialog(Frame frame) {
		super(frame, i18n.getString("material"), i18n
				.getString("material.selection.message"));
	}

	public MaterialChooserDialog(Dialog dialog) {
		super(dialog, i18n.getString("material"), i18n
				.getString("material.selection.message"));
	}

	public void setMaterial(List<Material> materials) {
		listModel.removeAllElements();
		if (materials != null) {
			for (Material mat : materials) {
				listModel.addElement(mat);
			}
		}
	}

	@Override
	protected Component createContentPanel() {
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCellRenderer(new MaterialListCellRenderer());
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					setOkButtonEnabled(list.getSelectedIndex() > -1);
				}
			}
		});

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && list.getSelectedIndex() > -1) {
					performOkAction();
				}

			}
		});

		JScrollPane scroll = new JScrollPane(list);
		return scroll;

	}

	public Material getSelectedMaterial() {
		return (Material) list.getSelectedValue();
	}

}
