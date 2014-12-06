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

public class WorkingProcessManagerEvent {

	public static final String PROCESS_FINISED = "process.finished";
	public static final String PROCESS_LOADED = "process.loaded";
	public static final String PROCESS_PROGRESSED = "process.progressed";
	public static final String PROCESS_STARTED = "process.started";
	private WorkingProcess workingProcess;

	private String type;
	private int maxProgress, currentProgress;
	private String message;

	public WorkingProcessManagerEvent(String type, WorkingProcess workingProcess) {
		this.type = type;
		this.workingProcess = workingProcess;
	}

	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}

	public int getCurrentProgress() {
		return currentProgress;
	}

	public int getMaxProgress() {
		return maxProgress;
	}

	private void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}
	
	private void setCurrentProgress(int currentProgress) {
		this.currentProgress = currentProgress;
	}

	public WorkingProcess getWorkingProcess() {
		return workingProcess;
	}

	public String getType() {
		return type;
	}

	

	public static WorkingProcessManagerEvent createFinishEvent(
			WorkingProcess process) {
		WorkingProcessManagerEvent event = new WorkingProcessManagerEvent(
				PROCESS_FINISED, process);
		return event;
	}

	public static WorkingProcessManagerEvent createLoadedEvent(
			WorkingProcess process) {
		WorkingProcessManagerEvent event = new WorkingProcessManagerEvent(
				PROCESS_LOADED, process);
		return event;
	}

	public static Object createStartedEvent(WorkingProcess process,
			int maxProgress, String message) {
		WorkingProcessManagerEvent event = new WorkingProcessManagerEvent(
				PROCESS_STARTED, process);
		event.setMaxProgress(maxProgress);
		event.setMessage(message);
		return event;
	}
	public static WorkingProcessManagerEvent createProgressEvent(
			WorkingProcess process, int currentProgress, String message) {
		WorkingProcessManagerEvent event = new WorkingProcessManagerEvent(
				PROCESS_PROGRESSED, process);
		event.setCurrentProgress(currentProgress);
		event.setMessage(message);
		return event;
	}

}
