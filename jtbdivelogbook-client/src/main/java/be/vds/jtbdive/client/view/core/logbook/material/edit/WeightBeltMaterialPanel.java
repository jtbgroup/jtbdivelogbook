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
package be.vds.jtbdive.client.view.core.logbook.material.edit;

import javax.swing.JComponent;

import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.WeightBeltMaterial;

public class WeightBeltMaterialPanel extends AbstractMaterialPanel {

	private static final long serialVersionUID = -4837563432588662843L;

	public WeightBeltMaterialPanel() {
		super();
	}


	@Override
	protected JComponent createSpecific() {
		return null;
	}


	@Override
	public Material getMaterial() {
		WeightBeltMaterial mat = new WeightBeltMaterial();
		setGeneralFields(mat);
		return mat;
	}

	@Override
	protected void fillSpecificComponents(Material material) {
		// Nothing to fill for the moment
	}
	
}
