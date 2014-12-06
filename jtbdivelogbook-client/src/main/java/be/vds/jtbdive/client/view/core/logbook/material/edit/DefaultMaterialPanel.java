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

import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.DefaultMaterial;
import be.vds.jtbdive.core.core.material.Material;

public class DefaultMaterialPanel extends AbstractMaterialPanel {
	private static final long serialVersionUID = -654570503107396796L;
	private MaterialType materialType;

	public DefaultMaterialPanel(MaterialType materialType) {
		super();
		this.materialType = materialType;
	}

	@Override
	protected JComponent createSpecific() {
		return null;
	}

	@Override
	public Material getMaterial() {
		DefaultMaterial mat = new DefaultMaterial(materialType);
		setGeneralFields(mat);
		return mat;
	}


	@Override
	protected void fillSpecificComponents(Material material) {
	}
	
}
