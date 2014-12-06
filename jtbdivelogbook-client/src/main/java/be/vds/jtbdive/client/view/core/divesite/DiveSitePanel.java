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
package be.vds.jtbdive.client.view.core.divesite;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.GlossaryManagerFacade;
import be.vds.jtbdive.core.core.DiveSite;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class DiveSitePanel extends JPanel {

	private static final long serialVersionUID = 7337112544630923693L;
	private DiveSite currentDiveLocation;
	private DiveSiteGeneralPanel diveSiteGeneralPanel;
	private DiveSiteLocationPanel diveSiteLocationPanel;
	private DiveSiteDocumentPanel diveSitePicturePanel;
	private DiveSiteManagerFacade diveSiteManagerFacade;
	private GlossaryManagerFacade glossaryManagerFacade;

	public DiveSitePanel(DiveSiteManagerFacade diveSiteManagerFacade, GlossaryManagerFacade glossaryManagerFacade) {
		this.diveSiteManagerFacade = diveSiteManagerFacade;
		this.glossaryManagerFacade = glossaryManagerFacade;
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.add(createDetailPanel(), BorderLayout.CENTER);
		setEditable(false);
	}

	private JComponent createDetailPanel() {
		I18nResourceManager i18n = I18nResourceManager.sharedInstance();
		JTabbedPane diveSiteTabbedPane = new JTabbedPane();
		diveSiteTabbedPane
				.addTab(i18n.getString("general"), createGeneralTab());
		diveSiteTabbedPane.addTab(i18n.getString("localization"),
				createLocationTab());
		diveSiteTabbedPane.addTab(i18n.getString("pictures"),
				createPicturesTab());
		return diveSiteTabbedPane;

	}

	private Component createPicturesTab() {
		diveSitePicturePanel = new DiveSiteDocumentPanel(diveSiteManagerFacade);
		return diveSitePicturePanel;
	}

	private Component createLocationTab() {
		diveSiteLocationPanel = new DiveSiteLocationPanel(glossaryManagerFacade.getCountries());
		return diveSiteLocationPanel;
	}

	private Component createGeneralTab() {
		diveSiteGeneralPanel = new DiveSiteGeneralPanel();
		return diveSiteGeneralPanel;
	}

	public void setValue(DiveSite diveSite) {
		this.currentDiveLocation = diveSite;
		diveSiteGeneralPanel.setValue(diveSite);
		diveSiteLocationPanel.setValue(diveSite);
		diveSitePicturePanel.setValue(diveSite);
	}

	public void reset() {

		diveSiteGeneralPanel.reset();
		diveSiteLocationPanel.reset();
		diveSitePicturePanel.reset();
		
	}

	public DiveSite getValue() {
		DiveSite newDiveSite = null;
		newDiveSite = new DiveSite();
		if (currentDiveLocation != null) {
			newDiveSite.setId(currentDiveLocation.getId());
		}

		newDiveSite.setName(diveSiteGeneralPanel.getDiveSiteName());
		newDiveSite.setDepth(diveSiteGeneralPanel.getDiveSiteDepth());
		newDiveSite.setDiveSiteType(diveSiteGeneralPanel.getDiveSiteType());
		newDiveSite.setInternetSite(diveSiteGeneralPanel.getWebSite()); 

		newDiveSite.setCoordinates(diveSiteLocationPanel
				.getDiveSiteCoordinates());
		newDiveSite.setAltitude(diveSiteLocationPanel.getDiveSiteAltitude());
		
		newDiveSite.setAddress(diveSiteLocationPanel.getDiveSiteAddress());
		
		newDiveSite.setDocuments(diveSitePicturePanel.getDiveSiteDocuments());

		return newDiveSite;
	}

	public void setEditable(boolean b) {
		diveSiteGeneralPanel.setEditable(b);
		diveSiteLocationPanel.setEditable(b);
//		diveSitePicturePanel.setEditable(b);
	}

}
