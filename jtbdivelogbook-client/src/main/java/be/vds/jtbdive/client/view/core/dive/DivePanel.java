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
package be.vds.jtbdive.client.view.core.dive;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Window;
import java.util.Collection;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.view.core.dive.profile.DiveProfileGraphicDetailPanel;
import be.vds.jtbdive.client.view.events.DiveSiteSelectionListener;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveEquipment;
import be.vds.jtbdive.core.core.DiveSite;

public class DivePanel extends JPanel implements Observer {

	private static final long serialVersionUID = -4604205753089428698L;
	private static final int INDEX_PARAMETER = 0;
	private static final int INDEX_DIVE_PROFILE = 1;
	private static final int INDEX_EQUIPMENT = 2;
	private static final int INDEX_DOCUMENTS = 3;
	private static final I18nResourceManager i18Mgr = I18nResourceManager
			.sharedInstance();
	private Window parentWindow;
	private DiverManagerFacade diverManagerFacade;
	private Dive currentDive;
	private DiveParametersPanel diveParametersPanel;
	private DiveSiteManagerFacade diveLocationManagerFacade;
	private DiveProfileGraphicDetailPanel profilePanel;
	private EquipmentParameterPanel equipmentParameterPanel;
	private JTabbedPane tabbedPane;
	private LogBookManagerFacade logBookManagerFacade;
	private DocumentsParameterPanel documentsParameterPanel;
	private Set<DiveSiteSelectionListener> diveSiteSelectionListeners = new HashSet<DiveSiteSelectionListener>();

	public DivePanel(Window parentWindow,
			LogBookManagerFacade logBookManagerFacade,
			DiverManagerFacade diverManagerFacade,
			DiveSiteManagerFacade diveLocationManagerFacade) {
		this.parentWindow = parentWindow;
		this.logBookManagerFacade = logBookManagerFacade;
		this.diverManagerFacade = diverManagerFacade;
		this.diveLocationManagerFacade = diveLocationManagerFacade;
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM,
				JTabbedPane.WRAP_TAB_LAYOUT);

		diveParametersPanel = new DiveParametersPanel(parentWindow,
				logBookManagerFacade, diverManagerFacade,
				diveLocationManagerFacade);
		JLabel customTab = createTabLabel("parameters", UIAgent.getInstance()
				.getIcon(UIAgent.ICON_PARAMETER_16), null);
		tabbedPane.addTab(null, diveParametersPanel);
		tabbedPane.setTabComponentAt(INDEX_PARAMETER, customTab);

		profilePanel = new DiveProfileGraphicDetailPanel(logBookManagerFacade);
		customTab = createTabLabel("profile",
				UIAgent.getInstance().getIcon(UIAgent.ICON_GRAPH_16), null);
		tabbedPane.addTab(null, profilePanel);
		tabbedPane.setTabComponentAt(INDEX_DIVE_PROFILE, customTab);

		equipmentParameterPanel = new EquipmentParameterPanel(
				logBookManagerFacade);
		customTab = createTabLabel("equipment",
				UIAgent.getInstance().getIcon(UIAgent.ICON_MASK_16), null);
		tabbedPane.addTab(null, equipmentParameterPanel);
		tabbedPane.setTabComponentAt(INDEX_EQUIPMENT, customTab);

		documentsParameterPanel = new DocumentsParameterPanel(
				logBookManagerFacade);
		customTab = createTabLabel("documents",
				UIAgent.getInstance().getIcon(UIAgent.ICON_ATTACHEMENT_16),
				null);
		tabbedPane.addTab(null, documentsParameterPanel);
		tabbedPane.setTabComponentAt(INDEX_DOCUMENTS, customTab);

		this.add(tabbedPane, BorderLayout.CENTER);
	}

	private JLabel createTabLabel(String text, Icon icon, String tooltip) {
		JLabel label = new JLabel(text);
		label.setHorizontalTextPosition(SwingConstants.RIGHT);
		label.setIcon(icon);
		label.setToolTipText(tooltip);
		return label;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		((JLabel) tabbedPane.getTabComponentAt(INDEX_PARAMETER)).setText(i18Mgr
				.getString("parameters"));
		((JLabel) tabbedPane.getTabComponentAt(INDEX_DIVE_PROFILE))
				.setText(i18Mgr.getString("dive.profile"));
		((JLabel) tabbedPane.getTabComponentAt(INDEX_EQUIPMENT)).setText(i18Mgr
				.getString("equipment"));
		((JLabel) tabbedPane.getTabComponentAt(INDEX_DOCUMENTS)).setText(i18Mgr
				.getString("documents"));
	}

	public void setCurrentDive(Dive dive) {
		this.currentDive = dive;
		if (currentDive == null) {
			clear();
		} else {
			updateData();
		}

		if (currentDive == null || currentDive.getDiveSite() == null) {
			fireDiveSiteSelected(null);
		} else {
			fireDiveSiteSelected(currentDive.getDiveSite());
		}

	}

	private void updateData() {
		if (null == currentDive) {
			clear();
		} else {
			diveParametersPanel.setDive(currentDive);

			profilePanel.setDiveProfile(currentDive.getDiveProfile(),
					currentDive);

			DiveEquipment equipment = currentDive.getDiveEquipment();
			if (equipment == null) {
				equipmentParameterPanel.setDiveEquipment(null, currentDive);
			} else {
				equipmentParameterPanel.setDiveEquipment(
						currentDive.getDiveEquipment(), currentDive);
			}

			documentsParameterPanel.setDocuments(currentDive.getDocuments(),
					currentDive);
		}

	}

	private void clear() {
		diveParametersPanel.clear();
		profilePanel.clear();
		equipmentParameterPanel.clear();
		diveParametersPanel.setDive(null);
		profilePanel.setDiveProfile(null, null);
		equipmentParameterPanel.setDiveEquipment(null, null);
		documentsParameterPanel.setDocuments(null, null);
	}

	public Dive getCurrentDive() {
		return currentDive;
	}

	@Override
	public String toString() {
		if (currentDive == null || currentDive.getId() == -1) {
			return "dive panel for New Dive...";
		} else {
			return "dive panel for " + currentDive;
		}
	}

	public void addDiveSiteSelectionListener(
			DiveSiteSelectionListener diveSiteSelectionListener) {
		diveSiteSelectionListeners.add(diveSiteSelectionListener);
	}

	public void removeDiveSiteSelectionListener(
			DiveSiteSelectionListener diveSiteSelectionListener) {
		diveSiteSelectionListeners.remove(diveSiteSelectionListener);
	}

	private void fireDiveSiteSelected(DiveSite diveSite) {
		for (DiveSiteSelectionListener listener : diveSiteSelectionListeners) {
			listener.diveSiteSelected(diveSite);
		}
	}

	public Collection<DiveSiteSelectionListener> getDiveSiteSelectionListeners() {
		return diveSiteSelectionListeners;
	}

	public void removeAllListeners() {
		diveSiteSelectionListeners.clear();
	}

	public void updateUnitsLabels() {
		diveParametersPanel.updateUnits();
		profilePanel.updateUnits();
		equipmentParameterPanel.updateUnits();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o.equals(UnitsAgent.getInstance())
				&& arg.equals(UnitsAgent.UNITS_CHANGED)) {
			updateUnitsLabels();
		}
	}

	public void refreshProfileGraph() {
		profilePanel.refreshGraph();
		equipmentParameterPanel.refreshForNewDiveTime();
	}
}
