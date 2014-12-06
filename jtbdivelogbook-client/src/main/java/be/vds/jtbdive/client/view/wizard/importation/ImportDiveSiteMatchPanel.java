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
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.view.components.DiveSiteMatchComponent;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.client.view.wizard.WizardPanel;
import be.vds.jtbdive.core.core.DiveSite;

public class ImportDiveSiteMatchPanel extends WizardPanel {
	private static final long serialVersionUID = 8480667502093367554L;
	private static final String CARD_CONTENT = "content";
	private static final String CARD_DEFAULT = "default";
	private JPanel innerPanel;
	private DiveSiteManagerFacade diveSiteManagerFacade;
	private List<DiveSiteMatchComponent> diveSitesComponentList = new ArrayList<DiveSiteMatchComponent>();
	private CardLayout contentLayout;
	private JPanel contentPanel;
	private String currentCard;

	public ImportDiveSiteMatchPanel(DiveSiteManagerFacade diveSiteManagerFacade) {
		super();
		loadDiveSiteManagerFacade(diveSiteManagerFacade);
	}

	private void loadDiveSiteManagerFacade(
			DiveSiteManagerFacade diveSiteManagerFacade) {
		this.diveSiteManagerFacade = diveSiteManagerFacade;
	}

	@Override
	public JComponent createContentPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(false);
		p.add(createChoicePanel(), BorderLayout.CENTER);
		p.add(createButtonsPanel(), BorderLayout.SOUTH);

		JPanel defaultPanel = new JPanel();
		JLabel label = new JLabel(I18nResourceManager.sharedInstance()
				.getString("logbook.no.divesite.found"));
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
				for (DiveSiteMatchComponent dmc : diveSitesComponentList) {
					if (!dmc.isSelected())
						dmc.setSelected(true);
				}
			}
		});

		I18nButton unselectAll = new I18nButton(new AbstractAction(
				"unselect.all") {

			private static final long serialVersionUID = 4860567986660109363L;

			@Override
			public void actionPerformed(ActionEvent e) {
				for (DiveSiteMatchComponent dmc : diveSitesComponentList) {
					if (dmc.isSelected())
						dmc.setSelected(false);
				}
			}
		});

		I18nButton fillBtn = new I18nButton(new AbstractAction(null, UIAgent
				.getInstance().getIcon(UIAgent.ICON_WIZARD_16)) {

			private static final long serialVersionUID = 3843592526496796497L;

			@Override
			public void actionPerformed(ActionEvent e) {
				List<DiveSite> erroredDiveSites = new ArrayList<DiveSite>();
				for (DiveSiteMatchComponent dmc : diveSitesComponentList) {
					if (dmc.isSelected()) {
						DiveSite ds = dmc.getDiveSiteToMatch();
						DiveSite fittingDS = LogBookUtilities
								.findBestMatchForDiveSite(ds,
										diveSiteManagerFacade);
						if (null == fittingDS) {
							erroredDiveSites.add(ds);
						} else {
							dmc.setMatchingDiveSite(fittingDS);
						}
					}
				}
				if (erroredDiveSites.size() > 0) {
					StringBuilder b = new StringBuilder();
					for (DiveSite diveSite : erroredDiveSites) {
						b.append(diveSite.getName()).append(", ");
					}
					String message = I18nResourceManager.sharedInstance()
							.getMessage(
									"divesite.fit.error.message.params",
									b.toString().substring(0,
											b.toString().lastIndexOf(",")));
					ExceptionDialog.showDialog(message,
							ImportDiveSiteMatchPanel.this);
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
		p.add(createDiveSitesComponents(), BorderLayout.CENTER);
		return p;

	}

	private JComponent createDiveSitesComponents() {
		innerPanel = new JPanel(new VerticalLayout(5));
		innerPanel.setOpaque(false);

		JScrollPane scroll = new JScrollPane(innerPanel);
		scroll.getVerticalScrollBar().setUnitIncrement(
				UIAgent.VERTICAL_UNIT_SCROLL);
		return scroll;
	}

	public void setDiveSites(List<DiveSite> diveSites) {
		if (diveSites == null || diveSites.size() == 0) {
			contentLayout.show(contentPanel, CARD_DEFAULT);
			currentCard = CARD_DEFAULT;
		} else {
			contentLayout.show(contentPanel, CARD_CONTENT);
			currentCard = CARD_CONTENT;

			innerPanel.removeAll();
			if (diveSites != null) {
				for (DiveSite DiveSite : diveSites) {
					DiveSiteMatchComponent comp = createDiveSiteMatchComponent(DiveSite);
					innerPanel.add(comp);
					diveSitesComponentList.add(comp);
				}
			}
		}
	}

	private DiveSiteMatchComponent createDiveSiteMatchComponent(
			DiveSite diveSite) {
		DiveSiteMatchComponent dmc = new DiveSiteMatchComponent(diveSiteManagerFacade);
		dmc.setDiveSiteToMatch(diveSite);
		return dmc;
	}

	public Map<DiveSite, DiveSite> getDiveSiteMatching() {
		if (currentCard == CARD_DEFAULT) {
			return null;
		}

		Map<DiveSite, DiveSite> divers = new HashMap<DiveSite, DiveSite>();
		for (DiveSiteMatchComponent dc : diveSitesComponentList) {
			if (dc.isSelected())
				divers.put(dc.getDiveSiteToMatch(), dc.getMatchingDiveSite());
		}

		return divers;
	}

	public void setDiveSitesMatching(Map<DiveSite, DiveSite> diveSites) {
		if (diveSites.size() == 0) {
			contentLayout.show(contentPanel, CARD_DEFAULT);
			currentCard = CARD_DEFAULT;
		} else {
			contentLayout.show(contentPanel, CARD_CONTENT);
			currentCard = CARD_CONTENT;

			Set<DiveSite> originals = diveSites.keySet();
			for (DiveSiteMatchComponent dc : diveSitesComponentList) {
				DiveSite original = dc.getDiveSiteToMatch();
				if (originals.contains(original)) {
					dc.setSelected(true);

					DiveSite match = diveSites.get(original);
					if (match == null) {
						dc.setMatchingDiveSite(match);
					}
				}
			}
		}
	}

	public List<DiveSite> getOriginalDiveSites() {
		if (currentCard == CARD_DEFAULT) {
			return null;
		}
		List<DiveSite> diveSites = new ArrayList<DiveSite>();
		for (DiveSiteMatchComponent dmc : diveSitesComponentList) {
			diveSites.add(dmc.getDiveSiteToMatch());
		}
		return diveSites;
	}

	@Override
	public String getMessage() {
		return "Match Dive sites.";
	}

}
