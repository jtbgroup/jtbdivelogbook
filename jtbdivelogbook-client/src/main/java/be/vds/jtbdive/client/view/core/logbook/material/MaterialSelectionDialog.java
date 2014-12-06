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
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.core.logbook.materialset.MaterialSetTreePanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class MaterialSelectionDialog extends PromptDialog {

	private static final long serialVersionUID = -1131689189872863790L;
	private JTabbedPane materialTabbedPane;
	private MaterialTreePanel materialTreePanel;
	private MaterialSetTreePanel materialSetTreePanel;
	private Map<Integer, MaterialSelectable> selectablePanels;
	private int currentSelectedPane;
	private MaterialTreePanel materialSelectionTreePanel;

	public MaterialSelectionDialog(Frame frame) {
		super(frame, i18n.getString("material.serie"), i18n
				.getString("material.serie.selection.message"), UIAgent.getInstance().getBufferedImage(UIAgent.ICON_MATERIALSET_48));
	}

	public MaterialSelectionDialog(Dialog dialog) {
		super(dialog, i18n.getString("material.serie"), i18n
				.getString("material.serie.selection.message"), UIAgent.getInstance().getBufferedImage(UIAgent.ICON_MATERIALSET_48));
	}

	public MaterialSelectionDialog() {
		super(i18n.getString("material.serie"), i18n
				.getString("material.serie.selection.message"), UIAgent.getInstance().getBufferedImage(UIAgent.ICON_MATERIALSET_48));
	}

	@Override
	protected void doBeforeInit() {
		selectablePanels = new HashMap<Integer, MaterialSelectable>();
		this.setIconImage(UIAgent.getInstance().getBufferedImage(UIAgent.ICON_MATERIALSET_16));
	}

	@Override
	protected Component createContentPanel() {
		GridBagConstraints gc = new GridBagConstraints();
		JPanel centerPanel = new DetailPanel(new GridBagLayout());
		GridBagLayoutManager.addComponent(centerPanel, createLeftPanel(), gc,
				0, 0, 1, 1, 0.5, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(centerPanel, createButtons(), gc, 1,
				0, 1, 1, 0, 1, GridBagConstraints.VERTICAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(centerPanel, createRightPanel(), gc,
				2, 0, 1, 1, 0.5, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);

		return centerPanel;
	}

	private Component createLeftPanel() {
		materialTabbedPane = new JTabbedPane(JTabbedPane.EAST);
		materialTabbedPane.addTab("mat", createMaterialsTree());
		selectablePanels.put(0, materialTreePanel);
		materialTabbedPane.addTab("set", createMaterialSetsTree());
		selectablePanels.put(1, materialSetTreePanel);

		materialTabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				currentSelectedPane = materialTabbedPane.getSelectedIndex();
			}
		});

//		materialTabbedPane.setPreferredSize(panelsDim);

		JPanel p = createMatContainerPanel("available", materialTabbedPane);
		return p;
	}

	private JPanel createMatContainerPanel(String key, JComponent payloadPanel) {
		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(false);
		I18nLabel label = new I18nLabel(key);
		label.setFont(UIAgent.getInstance().getFontTitleDetail());
		p.add(label, BorderLayout.NORTH);
		p.add(payloadPanel);
		return p;
	}

	private Component createMaterialsTree() {
		materialTreePanel = new MaterialTreePanel();
		JScrollPane scroll = new JScrollPane(materialTreePanel);
		return scroll;
	}

	private Component createMaterialSetsTree() {
		materialSetTreePanel = new MaterialSetTreePanel();
		JScrollPane scroll = new JScrollPane(materialSetTreePanel);
		return scroll;
	}

	private Component createButtons() {
		JButton addButton = new JButton(new AbstractAction() {

			private static final long serialVersionUID = 8392834982912274826L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Material> mats = selectablePanels.get(currentSelectedPane)
						.getSelectedMaterials();
				materialSelectionTreePanel.addMaterials(mats);
			}
		});
		addButton.setText(">");

		JButton removeButton = new JButton(new AbstractAction() {
			private static final long serialVersionUID = 6562293197038236842L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Material> mats = materialSelectionTreePanel
						.getSelectedMaterials();
				materialSelectionTreePanel.removeMaterials(mats);
			}
		});
		removeButton.setText("<");

		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.add(Box.createVerticalGlue());
		p.add(addButton);
		p.add(Box.createVerticalStrut(5));
		p.add(removeButton);
		p.add(Box.createVerticalGlue());

		return p;

	}

	private Component createRightPanel() {
		materialSelectionTreePanel = new MaterialTreePanel();
		JScrollPane scroll = new JScrollPane(materialSelectionTreePanel);
		JPanel p = createMatContainerPanel("selected", scroll);
		return p;
	}

	public Collection<Material> getSelectedMaterial() {
		return getSelectedMaterial(false);
	}

	public Collection<Material> getSelectedMaterial(
			boolean includeAlreadySelected) {
		return materialSelectionTreePanel.getMaterials();
	}

	public void setMaterialSets(Collection<MaterialSet> materialSets) {
		materialSetTreePanel.setMaterialSet(materialSets);
	}

	public void setMaterials(Collection<Material> materials) {
		materialTreePanel.setMaterials(materials);
	}

	public void setSelectedMaterials(Collection<Material> materials) {
		materialSelectionTreePanel.setMaterials(materials);
	}

	@Override
	public int showDialog() {
		return super.showDialog(500, 500);
	}

}
