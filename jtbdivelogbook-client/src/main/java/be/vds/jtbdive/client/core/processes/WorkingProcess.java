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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingWorker;


public abstract class WorkingProcess extends SwingWorker<Object, String> {

	private Set<WorkingProcessListener> listeners = new HashSet<WorkingProcessListener>();
	private String id;
	
	public WorkingProcess(String id) {
		this.id = id;
	}
	
	public void addWorkingProcessListener(WorkingProcessListener workingProcessListener){
		listeners.add(workingProcessListener);
	}
	
	public void removeWorkingProcessListener(WorkingProcessListener workingProcessListener){
		listeners.remove(workingProcessListener);
	}
	
	public void fireProcessStarted(int maxProgress, String message) {
		for (WorkingProcessListener listener : listeners) {
			listener.processStarted(this, maxProgress, message);
		}
	}
	
	public void fireProcessProgressed(int currentProgress, String message) {
		for (WorkingProcessListener listener : listeners) {
			listener.processProgressed(this, currentProgress, message);
		}
	}
	
	public void fireProcessFinished(String message) {
		List<WorkingProcessListener> copy = new ArrayList<WorkingProcessListener>(listeners);
		for (WorkingProcessListener listener : copy) {
			listener.processFinished(this, message);
		}
	}

	public String getId() {
		return id;
	}

	public void removeAllWorkingProcessListeners() {
		listeners.removeAll(listeners);
	}
}
