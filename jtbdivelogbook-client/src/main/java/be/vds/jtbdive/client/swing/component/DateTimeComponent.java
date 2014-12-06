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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXPanel;

import be.vds.jtb.swing.layout.GridBagLayoutManager;

public class DateTimeComponent extends JXPanel {

	private static final long serialVersionUID = 2491387735712962151L;
	public static final String DATE_MODIFIED = "date.modified";
	public static final int MODE_DATE_ONLY = 0;
	public static final int MODE_HOUR = 1;
	public static final int MODE_MINUTES = 2;
	public static final int MODE_SECONDS = 3;
	private static final Dimension DIM_SPIN = new Dimension(50, 20);
	private static final Dimension DIM_DATE = new Dimension(100, 20);

	private JXDatePicker datePicker;
	private JSpinner hourSpinner;
	private JSpinner minuteSpinner;
	private boolean activateNotification = true;
	private int mode;
	private JSpinner secondSpinner;

	public DateTimeComponent() {
		this(MODE_SECONDS);
	}

	public DateTimeComponent(int mode) {
		this.mode = mode;
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		int x = 0;

		GridBagLayoutManager.addComponent(this, createDatePicker(), c, x, 0, 1,
				1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		if (mode >= MODE_HOUR) {
			GridBagLayoutManager.addComponent(this, createHourSpinner(), c,
					++x, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
					GridBagConstraints.CENTER);
		}

		if (mode >= MODE_MINUTES) {
			GridBagLayoutManager.addComponent(this, createMinuteSpinner(), c,
					++x, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
					GridBagConstraints.CENTER);
		}

		if (mode >= MODE_SECONDS) {
			GridBagLayoutManager.addComponent(this, createSecondSpinner(), c,
					++x, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
					GridBagConstraints.CENTER);
		}
	}

	private JComponent createHourSpinner() {
		SpinnerNumberModel sm = new SpinnerNumberModel();
		sm.setMinimum(0);
		sm.setMaximum(24);
		sm.setStepSize(1);
		hourSpinner = new JSpinner(sm);
		hourSpinner.setPreferredSize(DIM_SPIN);
		((JTextField) hourSpinner.getEditor().getComponent(0)).setColumns(3);
		hourSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				notifyPropertyChangeListener();
			}
		});

		return hourSpinner;
	}

	private JComponent createMinuteSpinner() {
		SpinnerNumberModel sm = new SpinnerNumberModel();
		sm.setMinimum(0);
		sm.setMaximum(59);
		sm.setStepSize(1);
		minuteSpinner = new JSpinner(sm);
		minuteSpinner.setPreferredSize(DIM_SPIN);
		((JTextField) minuteSpinner.getEditor().getComponent(0)).setColumns(3);
		minuteSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				notifyPropertyChangeListener();
			}
		});
		return minuteSpinner;
	}

	private JComponent createSecondSpinner() {
		SpinnerNumberModel sm = new SpinnerNumberModel();
		sm.setMinimum(0);
		sm.setMaximum(59);
		sm.setStepSize(1);
		secondSpinner = new JSpinner(sm);
		secondSpinner.setPreferredSize(DIM_SPIN);
		((JTextField) secondSpinner.getEditor().getComponent(0)).setColumns(3);
		secondSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				notifyPropertyChangeListener();
			}
		});
		return secondSpinner;
	}

	private JComponent createDatePicker() {
		datePicker = new JXDatePicker();
		datePicker.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("date".equals(evt.getPropertyName())) {
					notifyPropertyChangeListener();
				}
			}
		});

		datePicker.setPreferredSize(DIM_DATE);
		return datePicker;
	}

	public void setDate(Date date) {
		if (null == date) {
			reset();

		} else {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			Calendar calendar2 = new GregorianCalendar(
					calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			datePicker.setDate(calendar2.getTime());
			if (mode >= MODE_HOUR) {
				hourSpinner.setValue(calendar.get(Calendar.HOUR_OF_DAY));
			}

			if (mode >= MODE_MINUTES) {
				minuteSpinner.setValue(calendar.get(Calendar.MINUTE));
			}

			if (mode >= MODE_SECONDS) {
				secondSpinner.setValue(calendar.get(Calendar.SECOND));
			}
		}
	}

	private void reset() {
		datePicker.setDate(null);
		if (mode >= MODE_HOUR) {
			hourSpinner.setValue(0);
		}
		if (mode >= MODE_MINUTES) {
			minuteSpinner.setValue(0);
		}
		if (mode >= MODE_SECONDS) {
			secondSpinner.setValue(0);
		}
	}

	public Date getDate() {
		Calendar calendar = new GregorianCalendar();
		Date date = datePicker.getDate();
		if (date == null) {
			return null;
		}
		calendar.setTime(date);

		if (mode >= MODE_HOUR) {
			calendar.set(Calendar.HOUR_OF_DAY, (Integer) hourSpinner.getValue());
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
		}

		if (mode >= MODE_MINUTES) {
			calendar.set(Calendar.MINUTE, (Integer) minuteSpinner.getValue());
		} else {
			calendar.set(Calendar.MINUTE, 0);
		}

		if (mode >= MODE_SECONDS) {
			calendar.set(Calendar.SECOND, (Integer) secondSpinner.getValue());
		} else {
			calendar.set(Calendar.SECOND, 0);
		}

		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public void setFormat(String format) {
		datePicker.setFormats(format);
	}

	private void notifyPropertyChangeListener() {
		if (activateNotification) {
			firePropertyChange(DATE_MODIFIED, null, getDate());
		}
	}

	public void activateNotification(boolean b) {
		activateNotification = b;
	}
}
