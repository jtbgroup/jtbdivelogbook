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

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.exceptions.DataStoreException;

public class DeleteDiveAction extends AbstractAction {

	private static final long serialVersionUID = -1278018973360024361L;
	private LogBookManagerFacade logBookManagerFacade;

	public DeleteDiveAction(LogBookManagerFacade logBookManagerFacade) {
		super("dive.delete", UIAgent.getInstance().getIcon(
				UIAgent.ICON_CANCEL_16));
		this.logBookManagerFacade = logBookManagerFacade;
		setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Dive dive = logBookManagerFacade.getCurrentDive();

		String dateString = dive.getDate() == null ? " no date "
				: UIAgent.getInstance().getFormatDateHoursFull().format(dive
						.getDate());

		String message = I18nResourceManager.sharedInstance().getMessage(
				"dive.delete.confirm",
				new Object[] { String.valueOf(dive.getNumber()), dateString });

		String title = I18nResourceManager.sharedInstance().getString(
				"dive.delete");
		int i = JOptionPane.showConfirmDialog(null, message, title,
				JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (i == JOptionPane.YES_OPTION) {
			try {
				logBookManagerFacade.deleteDive(dive);
			} catch (DataStoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
