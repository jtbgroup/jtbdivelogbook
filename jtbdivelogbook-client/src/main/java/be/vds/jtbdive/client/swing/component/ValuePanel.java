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

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class ValuePanel extends DetailPanel {

	private static final long serialVersionUID = 6859629829479059038L;
	private static final Font FONT_LABEL = new Font("Courrier", Font.ITALIC, 12);
	private static final int BORDER_WIDTH = 5;
	private GridBagConstraints gc;

	public ValuePanel(String label) {
		init(label, null);
	}

	public ValuePanel(String label, Icon icon) {
		init(label, icon);
	}
	
	private void init(String label, Icon icon) {
		I18nLabel lab = new I18nLabel(label);
		lab.setFont(FONT_LABEL);
		lab.setForeground(Color.DARK_GRAY);
		
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
		if(icon != null){
			p.add(new JLabel(icon));
		}
		p.add(Box.createHorizontalGlue());
		p.add(lab);
		p.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		
		this.setLayout(new GridBagLayout());
		gc = new GridBagConstraints();

		GridBagLayoutManager.addComponent(this, p, gc, 1, 1, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
		

		GridBagLayoutManager.addComponent(this,
				Box.createHorizontalStrut(BORDER_WIDTH), gc, 0, 0, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.EAST);
		GridBagLayoutManager.addComponent(this,
				Box.createHorizontalStrut(BORDER_WIDTH), gc, 2, 0, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.EAST);

	}

	public void setComponent(JComponent component) {
		GridBagLayoutManager.addComponent(this, component, gc, 1, 0, 1, 1,
				1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	}

}
