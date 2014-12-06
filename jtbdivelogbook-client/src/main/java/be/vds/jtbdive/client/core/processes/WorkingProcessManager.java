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
package be.vds.jtbdive.client.core.processes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import be.vds.jtbdive.core.logging.Syslog;

public class WorkingProcessManager extends Observable implements
		WorkingProcessListener {

	private static final Syslog LOGGER = Syslog
			.getLogger(WorkingProcessManager.class);
	private static WorkingProcessManager instance;

	private Map<String, WorkingProcess> processes;

	private WorkingProcessManager() {
		processes = new HashMap<String, WorkingProcess>();
	}

	public static WorkingProcessManager getInstance() {
		if (null == instance) {
			instance = new WorkingProcessManager();
		}
		return instance;
	}

	public void loadProcess(WorkingProcess workingProcess) {
		processes.put(workingProcess.getId(), workingProcess);
		LOGGER.info("Process added : " + workingProcess.getId());
		workingProcess.addWorkingProcessListener(this);
		setChanged();
		notifyObservers(WorkingProcessManagerEvent
				.createLoadedEvent(workingProcess));
		workingProcess.execute();
	}

	@Override
	public void processFinished(WorkingProcess workingProcess, String message) {
		processes.get(workingProcess.getId())
				.removeAllWorkingProcessListeners();
		processes.remove(workingProcess.getId());
		LOGGER.info("Process removed : " + workingProcess.getId());
		setChanged();
		notifyObservers(WorkingProcessManagerEvent
				.createFinishEvent(workingProcess));
	}

	public WorkingProcess getHighestWorkingProcess() {
		if (processes.size() == 0)
			return null;
		return processes.get(0);
	}

	@Override
	public void processProgressed(WorkingProcess process, int currentProgress,
			String message) {
		setChanged();
		notifyObservers(WorkingProcessManagerEvent.createProgressEvent(process,
				currentProgress, message));
	}

	@Override
	public void processStarted(WorkingProcess process, int maxProgress,
			String message) {
		setChanged();
		notifyObservers(WorkingProcessManagerEvent.createStartedEvent(process,
				maxProgress, message));
	}

	public List<WorkingProcess> getRunningProcesses() {
		return new ArrayList<WorkingProcess>(processes.values());
	}
}
