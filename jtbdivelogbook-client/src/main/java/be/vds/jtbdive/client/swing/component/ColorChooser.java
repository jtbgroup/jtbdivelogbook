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
package be.vds.jtbdive.client.swing.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ColorChooser extends JPanel {

	private static final long serialVersionUID = 7041949437361573893L;

	private Color color = Color.WHITE;

	private JLabel colorLabel;

	public ColorChooser() {
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());

		colorLabel = new JLabel();
		colorLabel.setOpaque(true);
		colorLabel.setPreferredSize(new Dimension(50, 25));
		colorLabel.setBorder(new LineBorder(Color.BLACK));
		colorLabel.setBackground(color);

		JButton chooseButton = new JButton(createChooseColorAction());
		chooseButton.setText("select");

		this.add(colorLabel, BorderLayout.CENTER);
		this.add(chooseButton, BorderLayout.EAST);
	}

	private Action createChooseColorAction() {
		Action a = new AbstractAction() {

			private static final long serialVersionUID = 3847782769383168025L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Color c = JColorChooser.showDialog(ColorChooser.this, "Choose a color", color);
				setColor(c);
			}

		};
		return a;
	}

	public void setColor(Color color) {
		if(color == null)
			color = Color.WHITE;
		
		this.color = color;
		colorLabel.setBackground(color);
	}

	public Color getColor() {
		return color;
	}

}
