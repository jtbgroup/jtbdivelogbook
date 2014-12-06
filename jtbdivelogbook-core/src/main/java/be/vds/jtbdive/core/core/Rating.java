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

/**
 * This object represents a rating accorded to any ratable object. A rating
 * value must be between 0 and 10.
 * 
 * @author Vanderslyen.G
 * 
 */
public class Rating implements Serializable {
	private static final long serialVersionUID = -5940882191823826005L;
	private short value = 0;

	public void setValue(short value) {
		if (value < 0) {
			this.value = 0;
		} else if (value > 10) {
			this.value = 10;
		} else {
			this.value = value;
		}
	}

	public short getValue() {
		return value;
	}
}
