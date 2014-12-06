/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: LogEntry.java
 * 
 * @author Andr&eacute; Schenk <andre@melior.s.bawue.de>
 * 
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package be.vds.jtbdive.core.core.divecomputer.uwatec;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Description: container for one logbook entry of an Aladin log file
 * 
 * @author Andr&eacute; Schenk
 * @version $Revision: 1.3 $
 */
public class UwatecLogEntry {
	private final List<Integer> alarms = new ArrayList<Integer>();

	private int alarmFromData;

	private final int bottomTime;

	private final Float maximumDepth;

	private final int surfaceTime;

	private final int airConsumption;

	private final Date entryTime;

	private final Float waterTemperature;

	public UwatecLogEntry(int[] bytes, UwatecSettings settings) {
		if ((bytes == null) || (bytes.length < 12)) {
			throw new IllegalArgumentException("need 12 bytes for log entry");
		}
		if (settings == null) {
			throw new IllegalArgumentException(
					"need settings to generate log entry");
		}
		alarmFromData =  bytes[0];
		bottomTime = getBottomTime(bytes);
		maximumDepth = getMaximumDepth(bytes);
		surfaceTime = getSurfaceTime(bytes);
		airConsumption = getAirConsumption(bytes, settings);
		entryTime = getEntryTime(bytes);
		waterTemperature = new Float((bytes[11] * 25) / 100.0);
	}

	private int getAirConsumption(int[] bytes, UwatecSettings settings) {
		int result = bytes[6];

		if (settings.getAladinType() == 0x1c) {
			// Aladin Air: unit = x * 20 psi
			result = result * 1378 / 1000;
		}
		return result;
	}

	public String getAlarms() {
		StringBuffer result = new StringBuffer();

		if ((alarmFromData & 1) > 0) {
			alarms.add(1);
		}
		if ((alarmFromData & 2) > 0) {
			alarms.add(2);
		}
		if ((alarmFromData & 8) > 0) {
			alarms.add(3);
		}
		if ((alarmFromData & 16) > 0) {
			alarms.add(4);
		}
		if ((alarmFromData & 32) > 0) {
			alarms.add(5);
		}
		return result.toString();
	}

	private int getBottomTime(int[] bytes) {
		int result = bytes[1] % 256 / 16 * 10 + bytes[1] % 16;
		if ((alarmFromData & 4) > 0) {
			result += 100;
		}
		return result;
	}

	private Date getEntryTime(int[] bytes) {
		int time = (bytes[7] * 16777216 + bytes[8] * 65536 + bytes[9] * 256 + bytes[10]) / 2;
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		calendar.set(Calendar.DATE, 01);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.YEAR, 1994);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.SECOND, time);
		return calendar.getTime();
	}

	public int getHighPlace() {
		switch ((alarmFromData & 0xc0) >> 6) {
		case 0:
			return UwatecConstants.ALTITUDE_0_900;
		case 1:
			return UwatecConstants.ALTITUDE_900_1750;
		case 2:
			return UwatecConstants.ALTITUDE_1750_2700;
		case 3:
			return UwatecConstants.ALTITUDE_2700_4000;
		default:
			return UwatecConstants.ALTITUDE_UNKNOWN;
		}
	}

	private Float getMaximumDepth(int[] bytes) {
		return new Float(((bytes[2] * 256 + bytes[3]) * 1000 / 4096) / 100.0);
	}

	private int getSurfaceTime(int[] bytes) {
		int time = bytes[4] * 256 + bytes[5];
		int hours = 0;
		int minutes = 0;

		if ((alarmFromData & 2) > 0) {
			hours = time / 4096 * 10 + time % 4096 / 256;
			minutes = time % 256 / 16 * 10 + time % 16;
		}
		return hours * 60 + minutes;
	}

	public String toString() {
		return "  high place: " + getHighPlace() + "\n" + "  alarms:"
				+ getAlarms() + "\n" + "  bottom time: " + bottomTime
				+ " min\n" + "  maximum depth: " + maximumDepth + " m\n"
				+ "  surface time: " + surfaceTime + " min\n"
				+ "  air consumption: " + airConsumption + " bar\n"
				+ "  entry time: " + entryTime + "\n" + "  water temperature: "
				+ waterTemperature + " \u00b0C\n";
	}
	
	public int getBottomTime() {
		return bottomTime;
	}
	public Float getMaximumDepth() {
		return maximumDepth;
	}
	
	public int getSurfaceTime() {
		return surfaceTime;
	}
	

	public Date getEntryTime() {
		return entryTime;
	}

	public Float getWaterTemperature() {
		return waterTemperature;
	}
}
