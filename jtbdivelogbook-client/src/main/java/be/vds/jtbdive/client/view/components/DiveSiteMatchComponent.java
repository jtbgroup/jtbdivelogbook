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

import javax.swing.JFrame;

import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.view.core.divesite.DiveSiteChooser;
import be.vds.jtbdive.core.core.DiveSite;

public class DiveSiteMatchComponent extends AbstractMatchComponent {

	private static final long serialVersionUID = 4348693948525639648L;
	private DiveSiteChooser diveSiteChooser;
	private DiveSiteManagerFacade diveSiteManagerFacade;


	public DiveSiteMatchComponent(DiveSiteManagerFacade diveSiteManagerFacade) {
		super(true);
		setDiveSiteManagerFacade(diveSiteManagerFacade);
	}

	public void setDiveSiteManagerFacade(
			DiveSiteManagerFacade diveSiteManagerFacade) {
		this.diveSiteManagerFacade = diveSiteManagerFacade;
		diveSiteChooser.setDiveSiteManagerFacade(this.diveSiteManagerFacade);
	}

	public void setMatchingDiveSite(DiveSite diveSite) {
		setMatchingObject(diveSite);
	}

	public void setDiveSiteToMatch(DiveSite diveSite) {
		setObjectToMatch(diveSite);
	}

	@Override
	protected String getMatchBundleKey() {
		return "divesite.map";
	}

	@Override
	protected void handleMatchComponent(boolean enabled) {
		diveSiteChooser.setEditable(enabled);

		if (!enabled) {
			diveSiteChooser.setDiveSite(null);
		}
	}

	@Override
	protected Component createMatchComponent() {
		diveSiteChooser = new DiveSiteChooser(diveSiteManagerFacade);
		return diveSiteChooser;
	}

	@Override
	protected String getObjectToMatchDescription() {
		if (null == getObjectToMatch())
			return null;
		return ((DiveSite) getObjectToMatch()).getName();
	}

	@Override
	protected Object getMatchingObjectFromComponent() {
		return diveSiteChooser.getDiveSite();
	}

	@Override
	protected void setMatchingObjectToCustomComponent(Object selectedObject) {
		DiveSite diveSite = (DiveSite) selectedObject;
		diveSiteChooser.setDiveSite(diveSite);
		diveSiteChooser.setEditable(diveSite != null);
	}

	public DiveSite getDiveSiteToMatch() {
		return (DiveSite) getObjectToMatch();
	}

	public DiveSite getMatchingDiveSite() {
		return (DiveSite) getSelectedObject();
	}
	
	
	public static void main(String[] args) {
		DiveSiteMatchComponent comp = new DiveSiteMatchComponent(null);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(comp);
		f.setSize(300, 200);
		f.setLocationRelativeTo(null);
		f.setVisible(true);

		DiveSite d1 = new DiveSite();
		d1.setName("Site Original");
		comp.setDiveSiteToMatch(d1);

		DiveSite d2 = new DiveSite();
		d2.setName("Site Selected");
		comp.setMatchingDiveSite(d2);

		comp.setSelected(false);
	}

}
