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
import be.vds.jtbdive.core.core.comparator.DiveNumberComparator;

public class DiveNumberNodeConstructor extends
		AbstractLogBookBrowserNodeConstructor {

	public void construct(DefaultMutableTreeNode rootNode, List<Dive> dives) {
		Collections.sort(dives, new DiveNumberComparator());
		Collections.reverse(dives);
		DefaultMutableTreeNode tn = null;
		for (Dive dive : dives) {
			tn = new DefaultMutableTreeNode(dive);
			rootNode.add(tn);
			registerNode(dive, tn);
		}
	}

	@Override
	public DefaultMutableTreeNode addNodeForDive(Dive dive,
			DefaultMutableTreeNode rootNode) {
		int childIndex = getChildIndex(dive, rootNode);

		DefaultMutableTreeNode tn = new DefaultMutableTreeNode(dive);
		rootNode.insert(tn, childIndex);
		registerNode(dive, tn);
		return tn;
	}

	private int getChildIndex(Dive dive, DefaultMutableTreeNode rootNode) {
		int max = rootNode.getChildCount();
		for (int i = 0; i < max; i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootNode
					.getChildAt(i);
			if (((Dive) node.getUserObject()).getNumber() < dive.getNumber()) {
				return i;
			}
		}
		return max;
	}

}
