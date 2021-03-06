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
package be.vds.jtbdive.client.view.core.browser.nodeconstructor;

import java.util.Collections;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.comparator.DiveDateComparator;
import be.vds.jtbdive.core.core.comparator.DiveDiveSiteComparator;

public class DiveSiteNodeConstructor extends
		AbstractLogBookBrowserNodeConstructor {

	public void construct(DefaultMutableTreeNode rootNode, List<Dive> dives) {
		Collections.sort(dives, new DiveDateComparator());
		Collections.reverse(dives);
		Collections.sort(dives, new DiveDiveSiteComparator());
		DiveSite currentDl = null;
		DefaultMutableTreeNode dlTreeNode = null;
		for (Dive dive : dives) {
			if (null == dive.getDiveSite()) {
				DefaultMutableTreeNode tn = new DefaultMutableTreeNode(dive);
				rootNode.add(tn);
				registerNode(dive, tn);
			} else {
				if (null == currentDl || !currentDl.equals(dive.getDiveSite())) {
					currentDl = dive.getDiveSite();
					dlTreeNode = new DefaultMutableTreeNode(currentDl);
					rootNode.add(dlTreeNode);
					registerNode(dive, dlTreeNode);
				}
				DefaultMutableTreeNode tn = new DefaultMutableTreeNode(dive);
				dlTreeNode.add(tn);
				registerNode(dive, tn);
			}
		}
	}

	@Override
	public DefaultMutableTreeNode addNodeForDive(Dive dive,
			DefaultMutableTreeNode rootNode) {
		DefaultMutableTreeNode tn = new DefaultMutableTreeNode(dive);
		DiveSite ds = dive.getDiveSite();
		if (null == ds) {
			insertNode(dive, rootNode, tn);
		} else {
			DefaultMutableTreeNode diveSiteNode = null;
			for (int i = 0; i < rootNode.getChildCount(); i++) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootNode
						.getChildAt(i);
				if (ds.equals(node.getUserObject())) {
					diveSiteNode = node;
					break;
				}
			}
			
			if (diveSiteNode != null) {
				insertNode(dive, diveSiteNode, tn);
			}
		}
		return tn;
	}

	private void insertNode(Dive dive, DefaultMutableTreeNode rootNode,
			DefaultMutableTreeNode tn) {
		int childIndex = getChildIndex(dive, rootNode);
		rootNode.insert(tn, childIndex);
		registerNode(dive, tn);
	}

	private int getChildIndex(Dive dive, DefaultMutableTreeNode parentNode) {
		int max = parentNode.getChildCount();
		if(dive.getDate() == null){
			return max;
		}
		
		for (int i = 0; i < max; i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) parentNode
					.getChildAt(i);
			if (((Dive) node.getUserObject()).getDate().before(dive.getDate())) {
				return i;
			}
		}
		return max;
	}

}
