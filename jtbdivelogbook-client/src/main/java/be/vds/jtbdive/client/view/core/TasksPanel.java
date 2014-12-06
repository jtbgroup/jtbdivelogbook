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

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.VerticalLayout;

import be.vds.jtbdive.client.core.processes.WorkingProcess;
import be.vds.jtbdive.client.core.processes.WorkingProcessListener;
import be.vds.jtbdive.client.core.processes.WorkingProcessManager;
import be.vds.jtbdive.client.core.processes.WorkingProcessManagerEvent;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class TasksPanel extends JPanel implements Observer {

	private static final long serialVersionUID = 8948697443720246572L;
	private Map<WorkingProcess, WorkingProcessPanel> processes = new HashMap<WorkingProcess, WorkingProcessPanel>();
	private JPanel viewportPanel;

	public TasksPanel() {
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		viewportPanel = new JPanel(new VerticalLayout());
		JScrollPane scroll = new JScrollPane(viewportPanel);
		scroll.getVerticalScrollBar().setUnitIncrement(
				UIAgent.VERTICAL_UNIT_SCROLL);
		this.add(scroll, BorderLayout.CENTER);

		WorkingProcessManager.getInstance().addObserver(this);
		loadProcesses();
	}

	private void loadProcesses() {
		List<WorkingProcess> processes = WorkingProcessManager.getInstance().getRunningProcesses();
		for (WorkingProcess workingProcess : processes) {
			addProcess(workingProcess);
		}
	}

	private void addProcess(WorkingProcess process) {
		WorkingProcessPanel wpp = new WorkingProcessPanel();
		wpp.setBorder(new EmptyBorder(5, 10, 5, 10));
		viewportPanel.add(wpp);
		processes.put(process, wpp);

		process.addWorkingProcessListener(new WorkingProcessListener() {

			private WorkingProcessPanel c = null;

			@Override
			public void processStarted(WorkingProcess process, int maxProgress,
					String message) {
				if (null == c)
					c = processes.get(process);
				c.setMaximumProgress(maxProgress);
				c.setMessage(message);
			}

			@Override
			public void processProgressed(WorkingProcess process,
					int increment, String message) {
				if (null == c)
					c = processes.get(process);

				c.incrementProgress(increment);
				c.setMessage(message);

			}

			@Override
			public void processFinished(WorkingProcess process, String message) {
			}
		});

	}

	public void removeProcess(WorkingProcess process) {
		if (processes != null) {
			viewportPanel.remove(processes.get(process));
			viewportPanel.revalidate();
			viewportPanel.repaint();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof WorkingProcessManagerEvent) {
			WorkingProcessManagerEvent event = (WorkingProcessManagerEvent) arg1;
			if (event.getType().equals(
					WorkingProcessManagerEvent.PROCESS_LOADED))
				addProcess(event.getWorkingProcess());
			else if (event.getType().equals(
					WorkingProcessManagerEvent.PROCESS_FINISED))
				removeProcess(event.getWorkingProcess());
		}
	}

}
