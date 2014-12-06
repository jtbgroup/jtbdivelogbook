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
package be.vds.jtbdive.client.actions.download;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.view.core.LogBookApplFrame;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.Palanquee;
import be.vds.jtbdive.core.core.PalanqueeEntry;
import be.vds.jtbdive.core.core.catalogs.DiverRole;
import be.vds.jtbdive.core.logging.Syslog;

public abstract class DownloadDiveAction extends AbstractAction {
	private static final long serialVersionUID = 1786729140098738186L;

	private static final Syslog logger = Syslog
			.getLogger(DownloadDiveAction.class);

	protected LogBookApplFrame logBookApplFrame;

	public DownloadDiveAction(LogBookApplFrame logBookApplFrame) {
		this.logBookApplFrame = logBookApplFrame;
	}

	protected void addDiverToDives(List<Dive> dives, Diver owner) {
		Palanquee p = null;
		PalanqueeEntry pa = null;
		for (Dive dive : dives) {
			p = dive.getPalanquee();
			if (p == null) {
				Set<DiverRole> roles = new HashSet<DiverRole>();
				p = new Palanquee();
				roles.add(DiverRole.ROLE_PALANQUEE_SIMPLE_DIVER);
				pa = new PalanqueeEntry(owner, roles);
				p.addPalanqueeEntry(pa);
			}
			dive.setPalanquee(p);
		}
	}

	protected void renumberDives(List<Dive> dives) {
		int number = logBookApplFrame.getLogBookManagerFacade()
				.getCurrentLogBook().getNextDiveNumber();
		for (Dive dive : dives) {
			dive.setNumber(number++);
		}
	}

	protected void addDives(List<Dive> dives) {
		DiveImportWorker w = new DiveImportWorker(dives);
		w.execute();
	}

	private class DiveImportWorker extends SwingWorker<Dive, String> {

		private List<Dive> dives;
		private ProgressDialog p;

		public DiveImportWorker(List<Dive> dives) {
			this.dives = dives;
			initDialog();
		}

		private void initDialog() {
			p = new ProgressDialog();
			p.setMaximumProgress(dives.size());
			p.setLocationRelativeTo(null);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					p.setVisible(true);
				}
			});
		}

		@Override
		protected Dive doInBackground() throws Exception {
			Dive lastDive = null;
			for (Dive dive : dives) {
//				try {
					logBookApplFrame.getLogBookManagerFacade()
							.addDive(dive);
					publish("Dive saved : " + dive.toString());
					logger.info("Dive saved : " + dive.toString());
					lastDive = dive;
//				} catch (DataStoreException e1) {
//					publish("Error while saving dive " + dive.toString()
//							+ "\r\n" + e1.getMessage());
//					logger.error("Error while saving dive " + dive.toString()
//							+ "\r\n" + e1.getMessage());
//				}
			}
			return lastDive;
		}

		@Override
		protected void process(List<String> arg0) {
			int i = 1;
			for (String message : arg0) {
				p.setProgress(i, message);
				i++;
			}
		}

		@Override
		protected void done() {
//			try {
//				logBookApplFrame.displayDive(get(), false);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} finally {
				p.dispose();
//			}
		}

	}

	private class ProgressDialog extends JDialog {
		private static final long serialVersionUID = 3961028567012348350L;
		private JProgressBar progressBar;
		private JLabel infoLabel;

		public ProgressDialog() {
			super(logBookApplFrame, "Importing...", true);
			init();
		}

		private void init() {
			this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			this.getContentPane().add(createContentPane());
			this.setSize(300, 100);
		}

		private Component createContentPane() {
			progressBar = new JProgressBar();
			infoLabel = new JLabel();
			infoLabel.setBackground(Color.yellow);

			JPanel p = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.insets = new Insets(10, 10, 5, 10);
			GridBagLayoutManager.addComponent(p, progressBar, gc, 0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
			gc.insets = new Insets(0, 10, 10, 10);			
			GridBagLayoutManager.addComponent(p, infoLabel, gc, 0, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

			return p;
		}

		public void setProgress(int percent, String message) {
			progressBar.setValue(percent);
			infoLabel.setText(message);
		}
		
		public void setMaximumProgress (int max){
			progressBar.setMaximum(max);
		}
	}

}
