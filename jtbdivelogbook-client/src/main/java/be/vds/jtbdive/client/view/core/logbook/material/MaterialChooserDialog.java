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
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import be.vds.jtbdive.client.core.comparator.OrderedCatalogComparator;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
//TODO: check whether this class can't be replaced by the MaterialSelectionDialog
public class MaterialChooserDialog extends PromptDialog {

	private static final long serialVersionUID = -7379417073493950323L;
	private ButtonGroup matGroup;

	public MaterialChooserDialog(JFrame parentFrame) {
		super(parentFrame, i18n.getString("material"), i18n
				.getString("material.selection.message"));
	}

	@Override
	protected Component createContentPanel() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		matGroup = new ButtonGroup();

		MaterialType[] values = MaterialType.values();
		Arrays.sort(values, new OrderedCatalogComparator());
		JRadioButton mtRb = null;
		for (MaterialType mt : values) {
			mtRb = new JRadioButton(i18n.getString(mt.getKey()));
			mtRb.setActionCommand(String.valueOf(mt.getId()));
			mtRb.setOpaque(false);
//			mtRb.setIcon(MaterialHelper.getMaterialIcon(mt,
//					MaterialHelper.ICON_SIZE_16));
			p.add(mtRb);
			matGroup.add(mtRb);
		}

		JScrollPane scroll = new JScrollPane(p);
		SwingComponentHelper.displayJScrollPane(scroll);

		JPanel det = new DetailPanel(new BorderLayout());
		det.add(scroll, BorderLayout.CENTER);
		return det;
	}

	public MaterialType getMaterialType() {
		if (null == matGroup.getSelection()) {
			return null;
		}
		return MaterialType.getMaterialType(Short.parseShort(matGroup
				.getSelection().getActionCommand()));
	}

}
