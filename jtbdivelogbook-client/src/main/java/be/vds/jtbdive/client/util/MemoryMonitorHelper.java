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
package be.vds.jtbdive.client.util;

public class MemoryMonitorHelper extends Thread {
    private static MemoryMonitorHelper _instance;
    private double refreshInSec = 1.0;
    private boolean enabled;
    private long free;
    private long total;
    private long max;

    public static MemoryMonitorHelper getInstance() {
	if (_instance == null) {
	    _instance = new MemoryMonitorHelper();
	}
	return _instance;
    }

    public static void startMonitoring() {
	startMonitoring(1);
    }

    public static void startMonitoring(double refreshInSec) {
	MemoryMonitorHelper i = getInstance();
	i.setRefreshInSec(refreshInSec);
	i.start();
    }

    public static void stopMonitoring() {
	getInstance().finish();
    }

    public void setRefreshInSec(double refreshInSec) {
	this.refreshInSec = refreshInSec;
    }

    private String getSize(long size) {
	String[] suffix = { "B", "K", "M", "G" };
	int si = 0;
	long result = size;
	while (result > 1024) {
	    result /= 1024;
	    si++;
	}
	return result + suffix[si];
    }

    public String getCurrentSize() {
	return getSize(total);
    }

    public String getFreeSize() {
	return getSize(free);
    }

    public String getMaxSize() {
	return getSize(max);
    }

    public void run() {
	enabled = true;
	max = Runtime.getRuntime().maxMemory();
	while (enabled) {
	    total = Runtime.getRuntime().totalMemory();
	    free = Runtime.getRuntime().freeMemory();
	    try {
		sleep((int) (refreshInSec * 1000));
	    } catch (Exception e) {
		e.printStackTrace();
		enabled = false;
	    }
	}
    }

    public synchronized void finish() {
	enabled = false;
    }
}