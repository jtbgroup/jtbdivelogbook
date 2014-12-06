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
package be.vds.jtbdive.client.view.core.logbook.materialset;

import java.awt.Component;
import java.awt.Frame;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.core.core.material.MaterialSet;

public class MaterialSetDialog extends PromptDialog {

	private static final long serialVersionUID = -4775332198435116520L;
	private MaterialSetEditionPanel materialSetPanel;

	private MaterialSetDialog (String title, String message) {
		super(title, message);
	}

	private MaterialSetDialog(Frame parentFrame, String title, String message) {
		super(parentFrame, title, message);
	}

	public static MaterialSetDialog createNewMaterialSetDialog(Frame parentFrame){
		return new MaterialSetDialog(parentFrame, i18n.getString("materialset.new"), i18n
				.getString("materialset.new.message"));
	}
	
	public static MaterialSetDialog createEditMaterialSetDialog(Frame parentFrame){
		return new MaterialSetDialog(parentFrame, i18n.getString("materialset.edit"), i18n
				.getString("materialset.edit.message"));
	}
	
	public void setLogBookManagerFacade(
			LogBookManagerFacade logBookManagerFacade) {
		materialSetPanel.setLogBookManagerFacade(logBookManagerFacade);
	}

	@Override
	protected Component createContentPanel() {
		materialSetPanel = new MaterialSetEditionPanel();
		return materialSetPanel;
	}

	public void setMaterialSet(MaterialSet materialSet) {
		materialSetPanel.setMaterialSet(materialSet);
	}

	public MaterialSet getDisplayedMaterialSet() {
		return materialSetPanel.getDisplayedMaterialSet();
	}

}
