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
package be.vds.jtbdive.client.view.wizard.stats;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nCheckBox;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.view.wizard.WizardPanel;

public class StatGraphOptionsPanel extends WizardPanel {
	private static final long serialVersionUID = -3160653150799125002L;
	private I18nCheckBox cumulCb;

	@Override
	public String getMessage() {
		return i18n.getString("wizard.stat.message.graph.options");
	}

	@Override
	public JComponent createContentPanel() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		GridBagLayoutManager.addComponent(p, createCumulatedCb(), gc, 1, 1, 1,
				1, 1, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, Box.createGlue(), gc, 0, 2, 2, 1,
				1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);

		return p;

	}

	private Component createCumulatedCb() {
		cumulCb = new I18nCheckBox("cumulated");
		cumulCb.setEnabled(false);
		return cumulCb;
	}

	public void enableCumulCheckBox(boolean b) {
		if (!b)
			cumulCb.setSelected(false);
		cumulCb.setEnabled(b);
	}

	public boolean isCumulEnabled() {
		return cumulCb.isSelected();
	}

}
