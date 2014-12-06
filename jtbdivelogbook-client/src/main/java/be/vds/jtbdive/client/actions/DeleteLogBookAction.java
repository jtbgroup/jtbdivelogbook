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
import be.vds.jtbdive.client.core.processes.WorkingProcess;
import be.vds.jtbdive.client.core.processes.WorkingProcessManager;
import be.vds.jtbdive.client.view.core.LogBookApplFrame;
import be.vds.jtbdive.client.view.core.logbook.LogBookChooserDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.logging.Syslog;

public class DeleteLogBookAction extends AbstractAction {

	private static final long serialVersionUID = 2840661519128443241L;
	private LogBookManagerFacade logBookManagerFacade;
	private LogBookApplFrame logBookApplFrame;
	private LogBookChooserDialog chooserDialog;
	private static final Syslog LOGGER = Syslog
			.getLogger(DeleteLogBookAction.class);

	public DeleteLogBookAction(LogBookApplFrame logBookApplFrame,
			LogBookManagerFacade logBookManagerFacade) {
		putValue(Action.NAME, "logbook.delete");
		putValue(Action.SMALL_ICON,
				UIAgent.getInstance().getIcon(UIAgent.ICON_LOGBOOK_DELETE_16));
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));

		this.logBookManagerFacade = logBookManagerFacade;
		this.logBookApplFrame = logBookApplFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		chooserDialog = new LogBookChooserDialog(logBookApplFrame,
				logBookManagerFacade);

		int i = chooserDialog.showDialog(300, 300);
		if (i == LogBookChooserDialog.OPTION_OK) {
			WorkingProcess wp = new InnerWorkingProcess("Delete LogBook "
					+ chooserDialog.getSelectedLogBook().getName());
			WorkingProcessManager.getInstance().loadProcess(wp);
		}
	}

	class InnerWorkingProcess extends WorkingProcess {

		public InnerWorkingProcess(String id) {
			super(id);
		}

		@Override
		protected Object doInBackground() throws Exception {
			LOGGER.info("Deleting logbook");
			fireProcessStarted(100, "process started");
			LogBook lb = chooserDialog.getSelectedLogBook();
			publish("deleting logbook " + lb.getName() + "...");
			try {
				logBookManagerFacade.deleteLogBook(lb.getId());
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error(e);
			}
			return null;
		}

		@Override
		protected void process(List<String> arg0) {
			for (String string : arg0) {
				fireProcessProgressed(25, string);
			}
		}

		@Override
		protected void done() {
			fireProcessProgressed(100, "LogBook deleted");
			fireProcessFinished("LogBook deleted!");
		}
	}
}
