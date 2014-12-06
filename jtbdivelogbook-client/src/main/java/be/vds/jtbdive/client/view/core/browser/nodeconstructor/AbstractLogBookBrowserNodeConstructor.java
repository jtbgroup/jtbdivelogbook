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

import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import be.vds.jtbdive.core.core.Dive;

public abstract class AbstractLogBookBrowserNodeConstructor implements
        LogBookBrowserNodeConstructor {

    private Map<Dive, DefaultMutableTreeNode> nodes = new HashMap<Dive, DefaultMutableTreeNode>();

    protected void registerNode(Dive dive, DefaultMutableTreeNode treenode) {
        nodes.put(dive, treenode);
    }

    public DefaultMutableTreeNode getNodeForDive(Dive dive) {
        return nodes.get(dive);
    }

    @Override
    public DefaultMutableTreeNode replaceDiveInNode(Dive oldValue, Dive newValue) {
        DefaultMutableTreeNode node = getNodeForDive(oldValue);
        if (node != null) {
            node.setUserObject(newValue);
        }

        nodes.remove(oldValue);
        nodes.put(newValue, node);

        return node;
    }

    public void clear() {
        nodes.clear();
    }
}
