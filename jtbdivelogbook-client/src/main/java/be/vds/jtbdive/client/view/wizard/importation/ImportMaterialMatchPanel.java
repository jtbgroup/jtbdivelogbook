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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.VerticalLayout;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.components.MaterialMatchComponent;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.client.view.wizard.WizardPanel;
import be.vds.jtbdive.core.core.material.Material;

public class ImportMaterialMatchPanel extends WizardPanel {
	private static final long serialVersionUID = 8480667502093367554L;
	private static final String CARD_CONTENT = "content";
	private static final String CARD_DEFAULT = "default";
	private JPanel innerPanel;
	private LogBookManagerFacade logBookManagerFacade;
	private List<MaterialMatchComponent> materialsComponentList = new ArrayList<MaterialMatchComponent>();
	private CardLayout contentLayout;
	private JPanel contentPanel;
	private String currentCard;
	private boolean allowMatch;

	public ImportMaterialMatchPanel(LogBookManagerFacade logBookManagerFacade) {
		super();
		this.logBookManagerFacade = logBookManagerFacade;
	}


	@Override
	public JComponent createContentPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(false);
		p.add(createChoicePanel(), BorderLayout.CENTER);
		p.add(createButtonsPanel(), BorderLayout.SOUTH);

		JPanel defaultPanel = new JPanel();
		JLabel label = new JLabel(I18nResourceManager.sharedInstance()
				.getString("logbook.no.material.found"));
		defaultPanel.add(label);

		contentLayout = new CardLayout();
		contentPanel = new JPanel(contentLayout);
		contentPanel.add(p, CARD_CONTENT);
		contentPanel.add(defaultPanel, CARD_DEFAULT);

		return contentPanel;
	}

	private Component createButtonsPanel() {
		JButton selectAll = new I18nButton(new AbstractAction("select.all") {

			private static final long serialVersionUID = -5849935558594020746L;

			@Override
			public void actionPerformed(ActionEvent e) {
				for (MaterialMatchComponent dmc : materialsComponentList) {
					if (!dmc.isSelected())
						dmc.setSelected(true);
				}
			}
		});

		JButton unselectAll = new I18nButton(
				new AbstractAction("unselect.all") {

					private static final long serialVersionUID = 4860567986660109363L;

					@Override
					public void actionPerformed(ActionEvent e) {
						for (MaterialMatchComponent dmc : materialsComponentList) {
							if (dmc.isSelected())
								dmc.setSelected(false);
						}
					}
				});

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p.setOpaque(false);
		p.add(selectAll);
		p.add(unselectAll);
		return p;
	}


	private Component createChoicePanel() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new BorderLayout());
		p.add(createMaterialsComponents(), BorderLayout.CENTER);
		return p;

	}

	private JComponent createMaterialsComponents() {
		innerPanel = new JPanel(new VerticalLayout(5));
		innerPanel.setOpaque(false);

		JScrollPane scroll = new JScrollPane(innerPanel);
		scroll.getVerticalScrollBar().setUnitIncrement(
				UIAgent.VERTICAL_UNIT_SCROLL);
		return scroll;
	}

	public void setMaterials(List<Material> materials) {
		if (materials == null || materials.size() == 0) {
			contentLayout.show(contentPanel, CARD_DEFAULT);
			currentCard = CARD_DEFAULT;
		} else {
			contentLayout.show(contentPanel, CARD_CONTENT);
			currentCard = CARD_CONTENT;

			displayAllMaterial(materials);
		}
	}

	private void displayAllMaterial(List<Material> materials) {
		innerPanel.removeAll();
		if (materials != null) {
			for (Material material : materials) {
				MaterialMatchComponent comp = createMaterialMatchComponent(material);
				innerPanel.add(comp);
				materialsComponentList.add(comp);
			}
		}
	}

	public void setAllowMatch(boolean allowMatch) {
		boolean hasChanged = this.allowMatch != allowMatch;
		this.allowMatch = allowMatch;
		if (hasChanged) {
			List<MaterialMatchComponent> matList = new ArrayList<MaterialMatchComponent>(
					materialsComponentList);
			innerPanel.removeAll();
			materialsComponentList.clear();

			for (MaterialMatchComponent materialComp : matList) {
				MaterialMatchComponent comp = createMaterialMatchComponent(materialComp
						.getMaterialToMatch());
				comp.setSelected(materialComp.isSelected());
				if (allowMatch) {
					comp.setMatchingMaterial(materialComp.getMatchingMaterial());
				}
				innerPanel.add(comp);
				materialsComponentList.add(comp);
			}
		}
	}

	private MaterialMatchComponent createMaterialMatchComponent(
			Material material) {
		MaterialMatchComponent dmc = new MaterialMatchComponent(
				logBookManagerFacade, this.allowMatch);
		dmc.setMaterialToMatch(material);
		return dmc;
	}

	/**
	 * A map of matching is returned using the following rules:<br/>
	 * <ul>
	 * <li>Every {@link Material} in used as key is supposed to be taken in
	 * count</li>
	 * <li>Every value set as null means the key is to be the new
	 * {@link Material} to be used</li>
	 * <li>Every time a value is not null, the value is the match of the key
	 * {@link Material}</li>
	 * </ul>
	 * 
	 * @return
	 */
	public Map<Material, Material> getMaterialMatching() {
		if (currentCard == CARD_DEFAULT) {
			return null;
		}

		Map<Material, Material> matchMap = new HashMap<Material, Material>();
		for (MaterialMatchComponent component : materialsComponentList) {
			if (component.isSelected())
				matchMap.put(component.getMaterialToMatch(),
						component.getMatchingMaterial());
		}

		return matchMap;
	}

	public void setMaterialMatching(Map<Material, Material> materials) {
		if (materials.size() == 0) {
			contentLayout.show(contentPanel, CARD_DEFAULT);
			currentCard = CARD_DEFAULT;
		} else {
			contentLayout.show(contentPanel, CARD_CONTENT);
			currentCard = CARD_CONTENT;

			Set<Material> originals = materials.keySet();
			for (MaterialMatchComponent dc : materialsComponentList) {
				Material original = dc.getMaterialToMatch();
				if (originals.contains(original)) {
					dc.setSelected(true);

					Material match = materials.get(original);
					if (match == null) {
						dc.setMatchingMaterial(match);
					}
				}
			}
		}
	}

	public List<Material> getOriginalMaterials() {
		if (currentCard == CARD_DEFAULT) {
			return null;
		}
		List<Material> diveSites = new ArrayList<Material>();
		for (MaterialMatchComponent dmc : materialsComponentList) {
			diveSites.add(dmc.getMaterialToMatch());
		}
		return diveSites;
	}

	public boolean allowMatch() {
		return allowMatch;
	}


	@Override
	public String getMessage() {
		return "Match material.";
	}
}
