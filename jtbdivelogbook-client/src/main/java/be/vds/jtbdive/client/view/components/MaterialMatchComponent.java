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

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.core.core.material.DiveTankMaterial;
import be.vds.jtbdive.core.core.material.Material;

public class MaterialMatchComponent extends AbstractMatchComponent {

	private static final long serialVersionUID = -5327071167161653302L;
	private MaterialComponent materialComponent;
	private LogBookManagerFacade logBookManagerFacade;

	public MaterialMatchComponent(LogBookManagerFacade logBookManagerFacade) {
		this(logBookManagerFacade, true);
	}

	public MaterialMatchComponent(LogBookManagerFacade logBookManagerFacade,
			boolean allowMatch) {
		super(allowMatch);
		setLogBookManagerFacade(logBookManagerFacade);
	}

	public void setLogBookManagerFacade(
			LogBookManagerFacade logBookManagerFacade) {
		if (isAllowMatch()) {
			this.logBookManagerFacade = logBookManagerFacade;
			materialComponent
					.setLogBookManagerFacade(this.logBookManagerFacade);
		}
	}

	public static void main(String[] args) {
		MaterialMatchComponent comp = new MaterialMatchComponent(null);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(comp);
		f.setSize(300, 200);
		f.setLocationRelativeTo(null);
		f.setVisible(true);

		DiveTankMaterial mat = new DiveTankMaterial();
		mat.setModel("model");
		comp.setMaterialToMatch(mat);

		DiveTankMaterial mat2 = new DiveTankMaterial();
		mat2.setModel("model sel");
		comp.setMatchingMaterial(mat2);
	}

	@Override
	protected String getMatchBundleKey() {
		return "material.map";
	}

	@Override
	protected void handleMatchComponent(boolean enabled) {
		materialComponent.setEditable(enabled);
		if (!enabled)
			materialComponent.setMaterial(null);
	}

	@Override
	protected Component createMatchComponent() {
		materialComponent = new MaterialComponent(logBookManagerFacade);
		return materialComponent;
	}

	@Override
	protected String getObjectToMatchDescription() {
		if (null == getObjectToMatch())
			return null;

		return ((Material) getObjectToMatch()).getShortDescription();
	}

	@Override
	protected Object getMatchingObjectFromComponent() {
		return materialComponent.getMaterial();
	}

	@Override
	protected void setMatchingObjectToCustomComponent(Object selectedObject) {
		Material material = (Material) selectedObject;
		materialComponent.setMaterial(material);
		materialComponent.setEditable(material != null);
	}

	public void setMatchingMaterial(Material material) {
		setMatchingObject(material);
	}

	public void setMaterialToMatch(Material material) {
		setObjectToMatch(material);
	}

	public Material getMaterialToMatch() {
		return (Material) getObjectToMatch();
	}

	public Material getMatchingMaterial() {
		return (Material) getSelectedObject();
	}

}
