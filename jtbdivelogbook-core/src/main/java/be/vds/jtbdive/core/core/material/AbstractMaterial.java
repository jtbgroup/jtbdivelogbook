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
package be.vds.jtbdive.core.core.material;

import java.util.Date;

public abstract class AbstractMaterial implements Material {
	private static final long serialVersionUID = -2997288427783339263L;
	private long id = -1;
	private String manufacturer;
	private String model;
	private Date purchaseDate;
	/**
	 * In Euro (euro)
	 */
	private double purchasePrice;
	private String vendor;
	private String comment;
	private boolean active = true;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getMaterialType().getKey());
		sb.append(" : ");
		sb.append(getShortDescription());
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return (int)id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (obj instanceof AbstractMaterial) {
			AbstractMaterial material = (AbstractMaterial) obj;
			if (this.id == -1 && material.getId() == -1) {
				return this == obj;
			}
			return this.id == material.getId();
		}
		return false;
	}
	
	@Override
	public String getShortDescription() {
		StringBuilder sb = new StringBuilder();

		if (manufacturer != null && manufacturer.length() > 0) {
			sb.append(manufacturer);
			if (model != null && model.length() > 0) {
				sb.append(" - ");
			}
		}

		if (model != null && model.length() > 0) {
			sb.append(model);
		}
		return sb.toString();
	}
}
