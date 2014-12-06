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

import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.view.core.diver.DiverChooser;
import be.vds.jtbdive.core.core.Diver;

public class DiverMatchComponent extends AbstractMatchComponent {

	private static final long serialVersionUID = -5645815717519540041L;
	private DiverChooser diverChooser;
	private DiverManagerFacade diverManagerFacade;

	public DiverMatchComponent(DiverManagerFacade diverManagerFacade) {
		super(true);
		setDiverManagerFacade(diverManagerFacade);
	}

	public void setDiverManagerFacade(DiverManagerFacade diverManagerFacade) {
		this.diverManagerFacade = diverManagerFacade;
		diverChooser.setDiveManagerFacade(diverManagerFacade);
	}

	@Override
	protected void handleMatchComponent(boolean enabled) {
		diverChooser.setEditable(enabled);

		if (!enabled)
			diverChooser.setDiver(null);
	}

	@Override
	protected Component createMatchComponent() {
		diverChooser = new DiverChooser(diverManagerFacade);
		return diverChooser;
	}

	@Override
	protected String getObjectToMatchDescription() {
		if (null == getObjectToMatch())
			return null;

		return ((Diver) getObjectToMatch()).getFullName();
	}

	@Override
	protected Object getMatchingObjectFromComponent() {
		return diverChooser.getDiver();
	}

	@Override
	protected void setMatchingObjectToCustomComponent(Object selectedObject) {
		Diver diver = (Diver) selectedObject;
		diverChooser.setDiver(diver);
		diverChooser.setEditable(diver != null);
	}

	public static void main(String[] args) {
		DiverMatchComponent comp = new DiverMatchComponent(null);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(comp);
		f.setSize(300, 200);
		f.setLocationRelativeTo(null);
		f.setVisible(true);

		Diver d1 = new Diver();
		d1.setFirstName("Diver 1");
		comp.setObjectToMatch(d1);

		Diver d2 = new Diver();
		d2.setFirstName("Diver 2");
		comp.setObjectToMatch(d2);

		comp.setSelected(false);
	}

	public void setDiverToMatch(Diver diver) {
		setObjectToMatch(diver);
	}

	public void setMatchingDiver(Diver diver) {
		setMatchingObject(diver);
	}

	public Diver getMatchingDiver() {
		return (Diver) getSelectedObject();
	}

	public Diver getDiverToMatch() {
		return (Diver) getObjectToMatch();
	}

	@Override
	protected String getMatchBundleKey() {
		return "diver.map";
	}

}
