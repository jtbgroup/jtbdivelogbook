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

import java.io.Serializable;
import java.util.Date;

import be.vds.jtbdive.core.core.catalogs.MaterialType;

public interface Material extends MaterialTypable, Serializable {

	MaterialType getMaterialType();

	long getId();

	void setId(long id);

	String getModel();

	void setModel(String model);

	Date getPurchaseDate();

	void setPurchaseDate(Date purchaseDate);

	double getPurchasePrice();

	void setPurchasePrice(double purchasePrice);

	String getVendor();

	void setVendor(String vendor);

	String getComment();

	void setComment(String comment);

	void setManufacturer(String manufacturer);

	String getManufacturer();

	boolean isActive();

	void setActive(boolean active);
}
