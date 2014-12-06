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
package be.vds.jtbdive.client.view.renderer;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import be.vds.jtbdive.client.core.filters.DiveFilter;
import be.vds.jtbdive.client.core.filters.DiveFilterHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class DiveFilterCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -5807655861439160732L;
//	private static final SimpleDateFormat sdf = UIController.DATE_FORMATTER_DATE_HOURS_FULL;
//	private static final Font FONT_NORMAL;
	// private static final Font FONT_MODIFIED;
//	static {
//		FONT_NORMAL = new JLabel().getFont();
		// FONT_MODIFIED = new Font(FONT_NORMAL.getFamily(), Font.ITALIC
		// + Font.BOLD, FONT_NORMAL.getSize());
//	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
//		JLabel l = (JLabel) super.getTreeCellRendererComponent(tree, value,
//				selected, expanded, leaf, row, hasFocus);
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		Object userObject = node.getUserObject();
		
		
		
		if (userObject == null) {
			setText(node.toString());
		} else {
			if (userObject instanceof DiveFilter) {
				DiveFilter f = (DiveFilter) userObject;
				switch (f.getDiveFilterType()) {
				case AGGREGATOR:
					setIcon(UIAgent.getInstance().getIcon(
							UIAgent.ICON_FOLDER_OPEN_16));
					break;
				case DIVER:
					setIcon(UIAgent.getInstance().getIcon(
							UIAgent.ICON_DIVER_16));
					break;
				case DIVE_TIME:
					setIcon(UIAgent.getInstance().getIcon(
							UIAgent.ICON_WATCH_16));
					break;
				case DEPTH:
					setIcon(UIAgent.getInstance().getIcon(
							UIAgent.ICON_GRAPH_16));
					break;
				case COMMENT:
					setIcon(UIAgent.getInstance().getIcon(
							UIAgent.ICON_DIVE_DOCUMENT_16));
					break;
				case NUMBER:
					setIcon(UIAgent.getInstance().getIcon(
							UIAgent.ICON_SORT_NUMBER_16));
					break;
				case DIVESITE:
					setIcon(UIAgent.getInstance().getIcon(
							UIAgent.ICON_DIVE_SITE_16));
					break;
				case DATE:
					setIcon(UIAgent.getInstance().getIcon(
							UIAgent.ICON_SORT_DAY_16));
					break;
				case WATER_TEMPERATURE:
					setIcon(UIAgent.getInstance().getIcon(
							UIAgent.ICON_THERMOMETER_16));
					break;
				case RATING:
					setIcon(UIAgent.getInstance().getIcon(
							UIAgent.ICON_STAR_16));
					break;
				default:
					setIcon(UIAgent.getInstance().getIcon(
							UIAgent.ICON_BULLET_16));
					break;
				}
				setText(DiveFilterHelper.getTextForFilter(f));
			} else {
				setText(userObject.toString());
			}

		}

		return this;
	}

}
