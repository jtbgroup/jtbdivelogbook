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

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.comparator.DiveDateComparator;

public class DiveYearNodeConstructor extends AbstractLogBookBrowserNodeConstructor {

	public void construct(DefaultMutableTreeNode rootNode, List<Dive> dives) {
		Collections.sort(dives, new DiveDateComparator());
		Collections.reverse(dives);
		int year = -1;
		DefaultMutableTreeNode dlTreeNode = null;
		for (Dive dive : dives) {
			if (null == dive.getDate()) {
//				if (null == dlTreeNode) {
//					dlTreeNode = new DefaultMutableTreeNode("");
//					rootNode.add(dlTreeNode);
//					registerNode(dive, dlTreeNode);
//				}
				DefaultMutableTreeNode tn = new DefaultMutableTreeNode(dive);
				rootNode.add(tn);
				registerNode(dive, tn);
			} else {
				Calendar cal = new GregorianCalendar();
				cal.setTime(dive.getDate());
				if (year == -1 || year != cal.get(Calendar.YEAR)) {
					year = cal.get(Calendar.YEAR);
					dlTreeNode = new DefaultMutableTreeNode(year);
					rootNode.add(dlTreeNode);
					registerNode(dive, dlTreeNode);
				}
				DefaultMutableTreeNode tn = new DefaultMutableTreeNode(dive);
				dlTreeNode.add(tn);
				registerNode(dive, dlTreeNode);
			}
		}
	}
	
	@Override
	public DefaultMutableTreeNode addNodeForDive(Dive dive,
			DefaultMutableTreeNode rootNode) {
		DefaultMutableTreeNode tn = new DefaultMutableTreeNode(dive);
		int year = -1;
		if(dive.getDate() != null){
			Calendar cal = new GregorianCalendar();
			cal.setTime(dive.getDate());
			if (year == -1 || year != cal.get(Calendar.YEAR)) {
				year = cal.get(Calendar.YEAR);
			}
		}
		

		if (-1 == year) {
			insertNode(dive, rootNode, tn);
		} else {
			DefaultMutableTreeNode yearNode = null;
			for (int i = 0; i < rootNode.getChildCount(); i++) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootNode
						.getChildAt(i);
				if (new Integer(year).equals(node.getUserObject())) {
					yearNode = node;
					break;
				}
			}
			
			if (yearNode != null) {
				insertNode(dive, yearNode, tn);
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
