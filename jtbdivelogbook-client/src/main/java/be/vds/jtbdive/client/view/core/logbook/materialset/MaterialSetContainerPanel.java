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
package be.vds.jtbdive.client.view.core.logbook.materialset;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.swing.component.TitlePanel;
import be.vds.jtbdive.client.view.core.logbook.material.DropComponent;
import be.vds.jtbdive.client.view.utils.MaterialHelper;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;

public class MaterialSetContainerPanel extends JPanel implements DropComponent {
	private static final long serialVersionUID = -3014702325968145233L;
	private MaterialSet materialSet;
	private Map<Material, DetailPanel> materialMap = new HashMap<Material, DetailPanel>();
	private JPanel materialContainerPanel;
	private TitlePanel titlePanel;
	private LogBookManagerFacade logBookManagerFacade;

	public MaterialSetContainerPanel(LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		this.setOpaque(false);

		this.add(createTitlePanel(), BorderLayout.NORTH);
		this.add(createCentralPanel(), BorderLayout.CENTER);

		this.setTransferHandler(new MaterialSetSimpleTransferHandler(
				logBookManagerFacade, this));
	}

	private Component createTitlePanel() {
		titlePanel = new TitlePanel();
		return titlePanel;
	}

	private Component createCentralPanel() {
		materialContainerPanel = new JPanel(new GridLayout(0, 2));
		materialContainerPanel.setOpaque(false);

		JScrollPane scroll = new JScrollPane(materialContainerPanel);
		SwingComponentHelper.displayJScrollPane(scroll);
		return scroll;
	}

	public void setMaterialSet(MaterialSet materialSet) {
		this.materialSet = materialSet;
		displayMaterialSet();
	}

	private void displayMaterialSet() {
		materialContainerPanel.removeAll();
		if (null != materialSet) {
			titlePanel.setTitle(materialSet.toString());
			for (Material material : materialSet.getMaterials()) {
				DetailPanel matComp = createMaterialComponent(material);
				materialMap.put(material, matComp);
				materialContainerPanel.add(matComp);
			}
		} else {
			titlePanel.setTitle(null);
		}
		materialContainerPanel.invalidate();
		materialContainerPanel.repaint();
		materialContainerPanel.validate();
	}

	public void clear() {
		this.materialSet = null;
		titlePanel.setTitle(null);
		materialContainerPanel.removeAll();
		materialContainerPanel.revalidate();
		materialContainerPanel.repaint();
	}

	private DetailPanel createMaterialComponent(Material material) {
		DetailPanel dp = new DetailPanel();
		JLabel l = new JLabel(material.getShortDescription());
		MaterialType materialType = material.getMaterialType();
		if (materialType != MaterialType.OTHER)
			l.setIcon(MaterialHelper.getMaterialIcon(materialType,
					MaterialHelper.ICON_SIZE_16));
		dp.add(l);
		return dp;
	}

	@Override
	public boolean isMaterialSetDefined() {
		return this.materialSet != null;
	}

	@Override
	public MaterialSet getMaterialSet() {
		return this.materialSet;
	}
}
