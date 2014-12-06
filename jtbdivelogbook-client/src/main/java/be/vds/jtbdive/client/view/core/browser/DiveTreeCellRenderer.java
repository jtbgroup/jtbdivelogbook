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
package be.vds.jtbdive.client.view.core.browser;

import java.awt.Component;
import java.awt.Font;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.utils.StringManipulator;

public class DiveTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -5807655861439160732L;
//	private static final SimpleDateFormat sdf = UIController.getInstance().getFormatDateHoursFull();
	private static final Font FONT_NORMAL;
	private static final Font FONT_MODIFIED;
	static {
		FONT_NORMAL = new JLabel().getFont();
		FONT_MODIFIED = new Font(FONT_NORMAL.getFamily(), Font.ITALIC
				+ Font.BOLD, FONT_NORMAL.getSize());
	}

	private LogBookManagerFacade logBookManagerFacade;

	public DiveTreeCellRenderer(LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
		setLeafIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_BULLET_16));
		setClosedIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_FOLDER_CLOSED_16));
		setOpenIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_FOLDER_OPEN_16));
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		JLabel l = (JLabel) super.getTreeCellRendererComponent(tree, value,
				selected, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		Object userObject = node.getUserObject();

		if (userObject instanceof Dive) {
			adaptForDive((Dive) userObject);
		} else if (userObject instanceof DiveSite) {
			adaptForDiveSite((DiveSite) userObject, l);
		} else {
			setFont(FONT_NORMAL);
		}

		return this;
	}

	private void adaptForDive(Dive dive) {
		SimpleDateFormat sdf = UIAgent.getInstance().getFormatDateHoursFull();
		StringBuilder sb = new StringBuilder();
		sb.append(StringManipulator.formatNumberWithFixedSize(dive.getNumber(),
				UserPreferences.getInstance().getDiveNumberSize()));
		sb.append(dive.getDate() == null ? "" : " - "
				+ sdf.format(dive.getDate()));
		setText(sb.toString());

		if (logBookManagerFacade.isDiveModified(dive)) {
			setFont(FONT_MODIFIED);
		} else {
			setFont(FONT_NORMAL);
		}
	}

	private void adaptForDiveSite(DiveSite diveSite, JLabel l) {
		l.setText(diveSite.getName());
		setFont(FONT_NORMAL);
	}

}
