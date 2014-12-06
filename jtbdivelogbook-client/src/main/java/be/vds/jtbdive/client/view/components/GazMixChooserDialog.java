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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.core.core.GazMix;
import be.vds.jtbdive.core.core.catalogs.Gaz;
import be.vds.jtbdive.core.core.comparator.GazFormulaComparator;

public class GazMixChooserDialog extends PromptDialog {

	private static final long serialVersionUID = 8687878348643027600L;
	private static Map<Gaz, JComponent> gazComponents;
	static {
		gazComponents = new HashMap<Gaz, JComponent>();
	}
	private JLabel volumeLabel;

	public GazMixChooserDialog(Frame parent, String title, boolean modal) {
		super(parent, title, "Choose your gaz mix");
	}

	public GazMixChooserDialog(Dialog parent, String title, boolean modal) {
		super(parent, title, "Choose your gaz mix");
	}

	@Override
	protected Component createContentPanel() {
		DetailPanel panel = new DetailPanel();

		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);

		List<Gaz> list = new ArrayList<Gaz>();
		for (Gaz gaz : Gaz.values()) {
			list.add(gaz);
		}
		Collections.sort(list, new GazFormulaComparator());

		int y = 0;
		for (Gaz gaz : list) {
			Component comp = createGazComponent(gaz);

			GridBagLayoutManager.addComponent(panel, createGazLabel(gaz), c, 0,
					y, 1, 1, 0, 0, GridBagConstraints.NONE,
					GridBagConstraints.EAST);

			GridBagLayoutManager
					.addComponent(panel, comp, c, 1, y++, 1, 1, 1, 0,
							GridBagConstraints.NONE,
							GridBagConstraints.WEST);
		}

		GridBagLayoutManager.addComponent(panel, Box.createGlue(), c,
				0, y++, 2, 1, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(panel, createVolumeLabel(), c, 0, y++,
				2, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.EAST);

		return panel;
	}

	private Component createVolumeLabel() {
		volumeLabel = new JLabel();
		 volumeLabel.setFont(new Font("Arial", Font.BOLD, 10));
		return volumeLabel;
	}

	private void adaptVolumeLabel(int volume) {
		volumeLabel.setText("Volume : " + volume + "%");
		if (volume == 100)
			volumeLabel.setForeground(Color.GREEN);
		else
			volumeLabel.setForeground(Color.RED);
	}

	private Component createGazLabel(Gaz gaz) {
		return new I18nLabel(gaz.getKey());
	}

	private Component createGazComponent(Gaz gaz) {
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
		JSpinner gazSpinner = new JSpinner(spinnerModel);
		gazSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				controlVolume();
			}

		});
		gazComponents.put(gaz, gazSpinner);

		return gazSpinner;
	}

	private void controlVolume() {
		int volume = 0;
		for (Gaz gaz : gazComponents.keySet()) {
			JSpinner s = (JSpinner) gazComponents.get(gaz);
			volume += (Integer) s.getValue();
		}
		adaptVolumeLabel(volume);
		setOkButtonEnabled(volume == 100);
	}

	public GazMix getGazMix() {
		GazMix mix = new GazMix();

		for (Gaz gaz : gazComponents.keySet()) {
			JSpinner s = (JSpinner) gazComponents.get(gaz);
			int i = (Integer) s.getValue();
			if (i > 0) {
				mix.addGaz(gaz, i);
			}
		}

		return mix;
	}

	public void setGazMix(GazMix gazMix) {
		for (Gaz gaz : gazMix.getGazes()) {
			((JSpinner) gazComponents.get(gaz)).setValue(gazMix
					.getPercentage(gaz));
		}
		controlVolume();
	}
}
