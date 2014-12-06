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
package be.vds.jtbdive.client.swing.component;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import be.vds.jtbdive.client.util.RuntimeMemoryMonitorSource;

public class HeapMonitorShortPanel extends JPanel {

	private static final long serialVersionUID = -6490114612597414648L;
	private int interval;
	private JProgressBar progress;

	public HeapMonitorShortPanel() {
		this(2000);
	}

	public HeapMonitorShortPanel(int interval) {
		this.interval = interval;
		init();
		startHeapRecorder();
	}

	private void init() {

		this.setLayout(new BorderLayout());
		progress = new JProgressBar();
		progress.setStringPainted(true);
		this.add(progress);
	}

	private void redrawHeapValues() {
		int currentHeap = (int) RuntimeMemoryMonitorSource.getTotal();
		int used = (int) RuntimeMemoryMonitorSource.getUsed();
		progress.setMaximum((int) currentHeap);
		progress.setValue((int) used);
		progress.setString(formatSize(used) + " / " + formatSize(currentHeap));
	}

	private String formatSize(float size) {
		String[] suffix = { "B", "K", "M", "G" };
		int si = 0;
		float result = size;
		while (result > 1024) {
			result /= 1024;
			si++;
		}
		return (int) result + suffix[si];
	}

	private void startHeapRecorder() {
		// if (null == MemoryMonitorHelper.getInstance()
		// || !MemoryMonitorHelper.getInstance().isAlive()) {
		// MemoryMonitorHelper.startMonitoring(2);
		// }

		Thread thread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							if (HeapMonitorShortPanel.this.isVisible())
								redrawHeapValues();
						}
					});
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

}
