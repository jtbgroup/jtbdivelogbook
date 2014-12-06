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
package be.vds.jtbdive.client.view.wizard.stats.axisx;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.view.wizard.WizardPanel;

public class StatXAxisDiveDepthPanel extends WizardPanel {

	private static final long serialVersionUID = -8049989491159920311L;
	private JSpinner pitchSpinner;

	public StatXAxisDiveDepthPanel() {
		super();
	}

	@Override
	public String getMessage() {
		return i18n.getString("wizard.stat.message.dive.depth.options");
	}

	@Override
	public JComponent createContentPanel() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setOpaque(false);
		GridBagConstraints gc = new GridBagConstraints();

		GridBagLayoutManager.addComponent(p, new I18nLabel("pitch"), gc, 0, 0,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(p, createPitchComponent(), gc, 1, 0,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(p, createUnitLabel(), gc, 2, 0, 1, 1,
				0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, Box.createGlue(), gc, 0, 1, 1, 1,
				1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

		return p;
	}

	private Component createUnitLabel() {
		return new JLabel("("
				+ UnitsAgent.getInstance().getLengthUnit().getSymbol() + ")");
	}

	private Component createPitchComponent() {
		double value = UnitsAgent.getInstance().convertLengthFromModel(5);
		double min = UnitsAgent.getInstance().convertLengthFromModel(1);
		double max = UnitsAgent.getInstance().convertLengthFromModel(50);
		double step = UnitsAgent.getInstance().convertLengthFromModel(1);
		
		SpinnerModel pitchSpinnerModel = new SpinnerNumberModel(value, min, max, step);
		pitchSpinner = new JSpinner(pitchSpinnerModel);
		return pitchSpinner;
	}

	/**
	 * Returns the selected pitch in the model unit (meter)
	 * @return the double value of the pitch
	 */
	public double getSelectedPitch() {
		double pitch = (Double) pitchSpinner.getValue();
		return UnitsAgent.getInstance().convertLengthToModel(pitch);
	}

}
