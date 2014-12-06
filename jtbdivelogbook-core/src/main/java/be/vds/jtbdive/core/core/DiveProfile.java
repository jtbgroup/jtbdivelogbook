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
package be.vds.jtbdive.core.core;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.vds.jtbdive.core.logging.Syslog;


/**
 * <p>
 * This is the profile of a Dive, containing the different entries (second,
 * depth) and the different warnings that are referenced with the seconds of the
 * depth entries
 * <p>
 * A depth entry is a combination of a second (the begin of the dive is
 * referenced as second 0) and a depth (this should be negative...). The Second
 * is the basic unit for the profile. It is represented by a double, so if you
 * want a smaller unit than a second, you can still use a subdivision of the
 * second... (who would do that?????)
 * <p>
 * A warning represent the moment a warning occurs which means a certain second
 * of the dive.
 * <p>
 * A deco entry is just as a warning, but just means you are in sursaturation.
 * It's also referenced by a second references in the depth entries.
 * 
 * @author Gautier Vanderslyen
 * 
 */
public class DiveProfile implements Serializable {

	private static final long serialVersionUID = -4101737018530228286L;
	private static final Syslog LOGGER = Syslog.getLogger(DiveProfile.class);
	/**
	 * Those are the references values for the profile. The map is composed by
	 * the values of the seconds at which a depth value has been picked up. The
	 * value of the depth should be negative. The depth unit is the meter.
	 */
	private Map<Double, Double> depthEntries = new HashMap<Double, Double>();
	/**
	 * When ceiling violation of deco stop
	 */
	private Set<Double> decoCeilingWarnings = new HashSet<Double>();
	/**
	 * When ascending to fast
	 */
	private Set<Double> ascentWarnings = new HashSet<Double>();
	/**
	 * When the remaining time is low compared with pressure in tank
	 */
	private Set<Double> remainingBottomTimeWarnings = new HashSet<Double>();
	/**
	 * The seconds where a decompression is running
	 */
	private Set<Double> decoEntries = new HashSet<Double>();

	public Map<Double, Double> getDepthEntries() {
		return depthEntries;
	}

	public void setDepthEntries(Map<Double, Double> depthEntries) {
		for (Double second : depthEntries.keySet()) {
			if(second < 0){
				throw new InvalidParameterException("The depth entries contains a value for a negative time ("+second+" s), which is not allowed.");
			}
		}
		this.depthEntries = depthEntries;
	}

	public void setDecoCeilingWarnings(Set<Double> decoWarnings) {
		for (Double second : decoWarnings) {
			if(second < 0){
				throw new InvalidParameterException("The deco ceiling warnings contains a value for a negative time ("+second+" s), which is not allowed.");
			}
		}
		this.decoCeilingWarnings = decoWarnings;
	}

	public Set<Double> getAscentWarnings() {
		return ascentWarnings;
	}

	public void setAscentWarnings(Set<Double> ascentWarnings) {
		for (Double second : ascentWarnings) {
			if(second < 0){
				throw new InvalidParameterException("The ascent warnings contains a value for a negative time ("+second+" s), which is not allowed.");
			}
		}
		this.ascentWarnings = ascentWarnings;
	}

	public Set<Double> getDecoCeilingWarnings() {
		return decoCeilingWarnings;
	}

	/**
	 * Gets the last <b>second</b> recorded in the depth entries
	 * 
	 * @return
	 */
	public double getLastTimeEntry() {
		double result = 0;
		for (Double second : depthEntries.keySet()) {
			if (second > result) {
				result = second;
			}
		}
		return result;
	}

	public void setRemainingBottomTimeWarnings(
			Set<Double> remainingBottomTimeWarnings) {
		this.remainingBottomTimeWarnings = remainingBottomTimeWarnings;
	}

	public void setDecoEntries(Set<Double> decoEntries) {
		this.decoEntries = decoEntries;
	}

	public Set<Double> getRemainingBottomTimeWarnings() {
		return remainingBottomTimeWarnings;
	}

	public Set<Double> getDecoEntries() {
		return decoEntries;
	}

	/**
	 * This method will remove all the entries > than the provided seconds. The
	 * entries equal to it will remain.
	 * 
	 * @param seconds
	 */
	void limitMaximumTime(double seconds) {
		if (depthEntries != null) {
			List<Double> secs = new ArrayList(depthEntries.keySet());
			for (Double sec : secs) {
				if (sec > seconds) {
					depthEntries.remove(sec);
					LOGGER.debug("Deleted entry " + sec
							+ " due to max dive time");
				}
			}
		}

		removeHigherThan(seconds, decoCeilingWarnings);
		removeHigherThan(seconds, ascentWarnings);
		removeHigherThan(seconds, decoEntries);
		removeHigherThan(seconds, remainingBottomTimeWarnings);
	}

	private void removeHigherThan(double seconds, Set<Double> secondsSet) {
		List<Double> secs = new ArrayList<Double>(secondsSet);
		for (Double sec : secs) {
			if (sec > seconds) {
				secondsSet.remove(sec);
				LOGGER.debug("Deleted warning at " + sec
						+ " due to max dive time");
			}
		}
	}

	public double getMaxDepth() {
		double d = 0;

		for (Double depth : depthEntries.values()) {
			d = Math.min(d, depth);
		}

		return d;
	}

	public void setMaximumDepth(double depth) {
		if (depthEntries != null) {
			List<Double> secs = new ArrayList(depthEntries.keySet());
			for (Double sec : secs) {
				if (depthEntries.get(sec) < depth) {
					depthEntries.put(sec, depth);
					LOGGER.debug("Depth entry adapted to " + depth
							+ " for entry at second " + sec);
				}
			}
		}

	}
}
