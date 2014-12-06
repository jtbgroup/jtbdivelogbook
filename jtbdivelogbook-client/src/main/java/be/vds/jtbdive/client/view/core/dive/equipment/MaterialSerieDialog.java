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
package be.vds.jtbdive.client.view.core.dive.equipment;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.utils.MaterialHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.comparator.MaterialTypeIdComparator;
import be.vds.jtbdive.core.core.material.Material;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class MaterialSerieDialog extends PromptDialog {

	private static final long serialVersionUID = -1131689189872863790L;
	private MatCave matCave;
	private Map<JCheckBox, Material> matComponentMap = new HashMap<JCheckBox, Material>();
	private List<Material> alreadyPresentMaterial;
	private JPanel listPanel;

	public MaterialSerieDialog(Frame frame) {
		super(frame, i18n.getString("material.serie"), i18n
				.getString("material.serie.selection.message"));
	}

	public MaterialSerieDialog(Dialog dialog) {
		super(dialog, i18n.getString("material.serie"), i18n
				.getString("material.serie.selection.message"));
	}

	public void setMatCave(MatCave matCave) {
		this.matCave = matCave;
		fillComponents();
	}

	@Override
	protected Component createContentPanel() {
		listPanel = new JPanel();
		listPanel.setBackground(UIAgent.getInstance().getColorBaseBackground());
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));

		JScrollPane scroll = new JScrollPane(listPanel);
		scroll.getViewport().setBorder(null);
		scroll.getVerticalScrollBar().setUnitIncrement(
				UIAgent.VERTICAL_UNIT_SCROLL);
		return scroll;
	}

	private void fillComponents() {
		listPanel.removeAll();

		List<MaterialType> types = matCave.getMaterialTypes();
		Collections.sort(types, new MaterialTypeIdComparator());
		for (MaterialType mt : types) {
			List<Material> matToDisplay = getMaterialsInCave(mt);
			if (null != matToDisplay && matToDisplay.size() > 0) {
				listPanel.add(createMaterialTypeLabel(mt));
				for (Material mat : matToDisplay) {
					listPanel.add(createMaterialCheckBox(mat));
				}
				listPanel.add(Box.createVerticalStrut(10));
			}
		}
	}

	private List<Material> getMaterialsInCave(MaterialType mt) {
		return matCave.getMaterials(mt);
	}

	private Component createMaterialCheckBox(Material mat) {
		JCheckBox matCb = new JCheckBox(mat.getShortDescription());
		matComponentMap.put(matCb, mat);
		matCb.setOpaque(false);

		if (alreadyPresentMaterial == null) {
			matCb.setEnabled(true);
		} else if (alreadyPresentMaterial.contains(mat)) {
			matCb.setEnabled(false);
			matCb.setSelected(true);
		}

		matCb.setBorder(BorderFactory.createEmptyBorder(2, 10, 0, 0));

		return matCb;
	}

	private Component createMaterialTypeLabel(MaterialType mt) {
		JLabel lab = new I18nLabel(mt.getKey());
		lab.setFont(UIAgent.getInstance().getFontTitleDetail());
		lab.setIcon(MaterialHelper.getMaterialIcon(mt, 24));

		return lab;
	}

	public List<Material> getSelectedMaterial() {
		return getSelectedMaterial(false);
	}

	public List<Material> getSelectedMaterial(boolean includeAlreadySelected) {
		List<Material> mat = new ArrayList<Material>();
		for (JCheckBox cb : matComponentMap.keySet()) {
			if (cb.isSelected()) {
				if (cb.isEnabled()
						|| (!cb.isEnabled() && includeAlreadySelected)) {
					mat.add(matComponentMap.get(cb));
				}
			}
		}
		return mat;
	}

	public void setAlreadyPresentMaterial(List<Material> alreadyPresentMaterial) {
		this.alreadyPresentMaterial = alreadyPresentMaterial;
		fillComponents();
	}

	public void setMaterials(MatCave matCave,
			List<Material> alreadyPresentMaterial) {
		this.alreadyPresentMaterial = alreadyPresentMaterial;
		this.matCave = matCave;
		fillComponents();
	}
}
