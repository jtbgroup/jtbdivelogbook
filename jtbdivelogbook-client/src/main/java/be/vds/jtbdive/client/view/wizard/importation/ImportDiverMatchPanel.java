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
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.view.components.DiverMatchComponent;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.client.view.wizard.WizardPanel;
import be.vds.jtbdive.core.core.Diver;

public class ImportDiverMatchPanel extends WizardPanel {

	private static final long serialVersionUID = 4048905808345148528L;
	private static final String CARD_CONTENT = "content";
	private static final String CARD_DEFAULT = "default";
	private JPanel innerPanel;
	private DiverManagerFacade diverManagerFacade;
	private List<DiverMatchComponent> diversComponentList = new ArrayList<DiverMatchComponent>();
	private CardLayout contentLayout;
	private JPanel contentPanel;
	private String currentCard;

	public ImportDiverMatchPanel(DiverManagerFacade diverManagerFacade) {
		super();
		loadDiverManagerFacade(diverManagerFacade);
	}

	private void loadDiverManagerFacade(DiverManagerFacade diverManagerFacade) {
		this.diverManagerFacade = diverManagerFacade;
	}

	@Override
	public String getMessage() {
		return "Match Divers.";
	}

	@Override
	public JComponent createContentPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(false);
		p.add(createChoicePanel(), BorderLayout.CENTER);
		p.add(createButtonsPanel(), BorderLayout.SOUTH);

		JPanel defaultPanel = new JPanel();
		JLabel label = new JLabel(I18nResourceManager.sharedInstance()
				.getString("logbook.no.diver.found"));
		defaultPanel.add(label);

		contentLayout = new CardLayout();
		contentPanel = new JPanel(contentLayout);
		contentPanel.add(p, CARD_CONTENT);
		contentPanel.add(defaultPanel, CARD_DEFAULT);

		return contentPanel;
	}

	private Component createButtonsPanel() {
		JButton selectAll = new I18nButton(new AbstractAction("select.all") {

			private static final long serialVersionUID = 6909109584103484404L;

			@Override
			public void actionPerformed(ActionEvent e) {
				for (DiverMatchComponent dmc : diversComponentList) {
					if (!dmc.isSelected())
						dmc.setSelected(true);
				}
			}
		});

		JButton unselectAll = new I18nButton(
				new AbstractAction("unselect.all") {

					private static final long serialVersionUID = -1847603054107921127L;

					@Override
					public void actionPerformed(ActionEvent e) {
						for (DiverMatchComponent dmc : diversComponentList) {
							if (dmc.isSelected())
								dmc.setSelected(false);
						}
					}
				});

		I18nButton fillBtn = new I18nButton(new AbstractAction(null, UIAgent
				.getInstance().getIcon(UIAgent.ICON_WIZARD_16)) {

			private static final long serialVersionUID = 677581597678361848L;

			@Override
			public void actionPerformed(ActionEvent e) {
				List<Diver> erroredDivers = new ArrayList<Diver>();
				for (DiverMatchComponent dmc : diversComponentList) {
					if (dmc.isSelected()) {
						Diver ds = dmc.getDiverToMatch();
						Diver fittingDiver = LogBookUtilities
								.findBestMatchForDiver(ds, diverManagerFacade);
						if (null == fittingDiver) {
							erroredDivers.add(ds);
						} else {
							dmc.setMatchingDiver(fittingDiver);
						}
					}
				}
				if (erroredDivers.size() > 0) {
					StringBuilder b = new StringBuilder();
					for (Diver diver : erroredDivers) {
						b.append(diver.getFullName()).append(", ");
					}
					String message = I18nResourceManager.sharedInstance()
							.getMessage(
									"diver.fit.error.message.params",
									b.toString().substring(0,
											b.toString().lastIndexOf(",")));
					ExceptionDialog.showDialog(message,
							ImportDiverMatchPanel.this);
				}
			}
		});

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p.setOpaque(false);
		p.add(fillBtn);
		p.add(selectAll);
		p.add(unselectAll);
		return p;
	}

	private Component createChoicePanel() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new BorderLayout());
		p.add(createDiversComponents(), BorderLayout.CENTER);
		return p;

	}

	private JComponent createDiversComponents() {
		innerPanel = new JPanel(new VerticalLayout(5));
		innerPanel.setOpaque(false);

		JScrollPane scroll = new JScrollPane(innerPanel);
		scroll.getVerticalScrollBar().setUnitIncrement(
				UIAgent.VERTICAL_UNIT_SCROLL);
		return scroll;
	}

	public void setDivers(List<Diver> divers) {
		if (divers.size() == 0) {
			contentLayout.show(contentPanel, CARD_DEFAULT);
			currentCard = CARD_DEFAULT;
		} else {
			contentLayout.show(contentPanel, CARD_CONTENT);
			currentCard = CARD_CONTENT;

			innerPanel.removeAll();
			if (divers != null) {
				for (Diver diver : divers) {
					DiverMatchComponent comp = createDiverMatchComponent(diver);
					innerPanel.add(comp);
					diversComponentList.add(comp);
				}
			}
		}
	}

	private DiverMatchComponent createDiverMatchComponent(Diver diver) {
		DiverMatchComponent dmc = new DiverMatchComponent(diverManagerFacade);
		dmc.setDiverToMatch(diver);
		return dmc;
	}

	public Map<Diver, Diver> getDiversMatching() {
		if (currentCard == CARD_DEFAULT) {
			return null;
		}

		Map<Diver, Diver> divers = new HashMap<Diver, Diver>();
		for (DiverMatchComponent dc : diversComponentList) {
			if (dc.isSelected())
				divers.put(dc.getDiverToMatch(), dc.getMatchingDiver());
		}

		return divers;
	}

	public void setDiversMatching(Map<Diver, Diver> divers) {
		if (divers.size() == 0) {
			contentLayout.show(contentPanel, CARD_DEFAULT);
			currentCard = CARD_DEFAULT;
		} else {
			contentLayout.show(contentPanel, CARD_CONTENT);
			currentCard = CARD_CONTENT;

			Set<Diver> originals = divers.keySet();
			for (DiverMatchComponent dc : diversComponentList) {
				Diver original = dc.getDiverToMatch();
				if (originals.contains(original)) {
					dc.setSelected(true);

					Diver match = divers.get(original);
					if (match == null) {
						dc.setMatchingDiver(match);
					}
				}
			}
		}
	}

	public List<Diver> getOriginalDivers() {
		if (currentCard == CARD_DEFAULT) {
			return null;
		}

		List<Diver> divers = new ArrayList<Diver>();
		for (DiverMatchComponent dmc : diversComponentList) {
			divers.add(dmc.getDiverToMatch());
		}
		return divers;
	}

}
