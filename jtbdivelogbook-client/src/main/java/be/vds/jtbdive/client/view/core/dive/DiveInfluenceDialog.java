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
package be.vds.jtbdive.client.view.core.dive;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;

/**
 * 
 * @author vanderslyen.g
 */
public class DiveInfluenceDialog extends PromptDialog {

	private JTextArea ta;

	public DiveInfluenceDialog(JFrame parentFrame, String message) {
		super(parentFrame, i18n.getString("warning"), i18n
				.getString("dive.influence.message.confirm"), UIAgent
				.getInstance().getBufferedImage(UIAgent.ICON_IMPORTANT_48), i18n
				.getString("bypass.nexttime.always.accept"));
		initDlg(message);
	}
	
	

	private void initDlg(String message) {
		ta.setText(message);
	}

	@Override
	protected Component createContentPanel() {
		DetailPanel p = new DetailPanel(new BorderLayout());
		ta = new JTextArea();
		ta.setBorder(null);
		ta.setOpaque(false);
		ta.setEditable(false);
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		
		ta.setBorder(BorderFactory.createEmptyBorder());
		ta.setBackground(new Color(0,0,0,0));
		
		p.add(ta, BorderLayout.CENTER);
		return p;
	}
	
	protected void performSkipAction() {
		setCancelButtonEnabled(!skipSelected());
	}
}
