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
package be.vds.jtbdive.client.view.core.logbook.material;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.components.MaterialComponent;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.material.Material;

public class MergeMaterialDialog extends PromptDialog {

	private static final long serialVersionUID = -8646138458735071288L;
	private MaterialComponent materialToDelete;
	private MaterialComponent materialToKeep;

	public MergeMaterialDialog(Frame parentFrame) {
		super(parentFrame, I18nResourceManager.sharedInstance().getString(
				"material.merge"), I18nResourceManager.sharedInstance()
				.getString("material.merge.message"));
	}

	public void setLogBookManagerFacade(
			LogBookManagerFacade logBookManagerFacade) {
		materialToKeep.setLogBookManagerFacade(logBookManagerFacade);
		materialToDelete.setLogBookManagerFacade(logBookManagerFacade);
	}

	@Override
	protected Component createContentPanel() {
		materialToKeep = new MaterialComponent(null);
		materialToDelete = new MaterialComponent(null);

		Border border = new LineBorder(UIAgent.getInstance()
				.getColorPanelUnselected());
		materialToDelete.setBorder(border);
		materialToKeep.setBorder(border);

		JPanel p = new DetailPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(5, 5, 5, 5);

		GridBagLayoutManager.addComponent(p, new I18nLabel("material.to.keep"),
				gc, 0, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, materialToKeep, gc, 1, 0, 1, 1, 1,
				0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, new I18nLabel(
				"material.to.delete"), gc, 0, 1, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, materialToDelete, gc, 1, 1, 1, 1,
				1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
		return p;
	}

	public Material getMaterialToKeep() {
		return materialToKeep.getMaterial();
	}

	public Material getMaterialToDelete() {
		return materialToDelete.getMaterial();
	}
}
