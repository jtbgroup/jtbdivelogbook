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

public class Coordinates implements Serializable {

	public static final double MAX_LATITUDE = 90;
	public static final double MAX_LONGITUDE = 180;

	private static final long serialVersionUID = 2360656174995703111L;
	/**
	 * Negative Latitude is south <br>
	 * Negative Longitude is west
	 */
	private double latitude, longitude;

	public Coordinates(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Get the latitude value of the coordinate
	 * 
	 * @return the latitude. . Negative value means South.
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Get the longitude value of the coordinate
	 * 
	 * @return the longitude. Negative value means West.
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the latitude of the coordinate. The method throws an
	 * {@link InvalidParameterException} if the value is higher than the
	 * MAX_LATITUDE or lower than the negative of the MAX_LATITUDE
	 * 
	 * @param latitude
	 *            the latitude of the coordinate
	 * @throws InvalidParameterException
	 */
	public void setLatitude(double latitude) {
		if (Math.abs(latitude) > MAX_LATITUDE) {
			throw new InvalidParameterException(
					"the provided latitude can't be higher than "
							+ MAX_LATITUDE + " or lower than -" + MAX_LATITUDE);
		}
		this.latitude = latitude;
	}

	/**
	 * Sets the longitude of the coordinate. The method throws an
	 * {@link InvalidParameterException} if the value is higher than the
	 * MAX_LONGITUDE or lower than the negative of the MAX_LATITUDE
	 * 
	 * @param latitude
	 *            the longitude of the coordinate
	 * @throws InvalidParameterException
	 */
	public void setLongitude(double longitude) {
		if (Math.abs(longitude) > MAX_LONGITUDE) {
			throw new InvalidParameterException(
					"the provided latitude can't be higher than "
							+ MAX_LONGITUDE + " or lower than -"
							+ MAX_LONGITUDE);
		}
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(latitude);
		sb.append(" / ");
		sb.append(longitude);
		return sb.toString();
	}
}
