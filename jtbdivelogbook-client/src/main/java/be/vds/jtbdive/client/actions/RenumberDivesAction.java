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
package be.vds.jtbdive.client.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;

public class RenumberDivesAction extends AbstractAction {

  private static final long serialVersionUID = 5697856375375512005L;
  public static final KeyStroke SAVE_DIVE_KEYSTROKE = KeyStroke.getKeyStroke(KeyEvent.VK_S,
          KeyEvent.CTRL_DOWN_MASK);
  private LogBookManagerFacade logBookManagerFacade;

  public RenumberDivesAction(LogBookManagerFacade logBookManagerFacade) {
    super("dive.renumber");
    this.logBookManagerFacade = logBookManagerFacade;
	putValue(Action.SMALL_ICON,
			UIAgent.getInstance().getIcon(UIAgent.ICON_SORT_NUMBER_16));
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    List<Dive> dives = logBookManagerFacade.reunumberDives();
    
	for (Dive dive : dives) {
		logBookManagerFacade.setDiveChanged(dive);
		}
	
//    for (Dive dive : dives) {
//      DiveChangeInspector.getInstance().diveChanged(dive);
//    }
  }
}
