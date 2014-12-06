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
package be.vds.jtbdive.client.view.wizard.stats.axisy;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.view.wizard.WizardPanel;

public class StatYAxisDiveDepthsPanel extends WizardPanel {

	// private JSpinner pitchSpinner;
	private static final long serialVersionUID = -2393918336118176087L;


	@Override
	public String getMessage() {
		return i18n.getString("wizard.stat.message.dive.depth.options");
	}

	@Override
	public JComponent createContentPanel() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setOpaque(false);
		GridBagConstraints gc = new GridBagConstraints();

		GridBagLayoutManager.addComponent(p, new I18nLabel("no.option"), gc, 0,
				0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);
		// GridBagLayoutManager.addComponent(p, createPitchComponent(), gc, 1,
		// 0,
		// 1, 1, 1, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, Box.createGlue(), gc, 0, 1, 1, 1,
				1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

		return p;
	}

	// private Component createPitchComponent() {
	// SpinnerModel pitchSpinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
	// pitchSpinner = new JSpinner(pitchSpinnerModel);
	// return pitchSpinner;
	// }
	//
	// public int getSelectedPitch() {
	// int pitch = (Integer) pitchSpinner.getValue();
	// return pitch;
	// }

}
