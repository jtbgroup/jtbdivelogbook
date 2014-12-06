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
package be.vds.jtbdive.client.view.core;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import be.vds.jtb.swing.layout.GridBagLayoutManager;


public class WorkingProcessPanel extends JPanel {
	private static final long serialVersionUID = 518524642897528723L;
	private JProgressBar progress;
	private JLabel processMessageLabel;
	private JLabel processIdLabel;

	public WorkingProcessPanel() {
		init();
	}

	private void init() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		GridBagLayoutManager.addComponent(this, createProgressBar(), c, 0, 0, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, createProcessNameLabel(), c, 0, 1, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(this, createProcessMessageLabel(), c, 1, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	}

	private Component createProcessNameLabel() {
		processIdLabel = new JLabel();
		return processIdLabel;
	}

	private Component createProgressBar() {
		progress = new JProgressBar();
		return progress;
	}

	private Component createProcessMessageLabel() {
		processMessageLabel = new JLabel();
		return processMessageLabel;
	}

	public void incrementProgress(int increment) {
		progress.setValue(progress.getValue() + increment);
	}

	public void setMessage(String message) {
		processMessageLabel.setText(message);
	}
	
	public void setProcessId(String processId) {
		processIdLabel.setText(processId + " : ");
	}

	public void setMaximumProgress(int maxProgress) {
		progress.setMaximum(maxProgress);
	}
}
