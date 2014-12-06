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

import be.vds.jtbdive.core.core.catalogs.MaterialType;

/**
 * An interface for the possible equipment. An equipment piece is used during
 * the dive and is made of a Material most of the time.
 * 
 * @author Gautier Vanderslyen
 * 
 */
public interface Equipment extends Serializable {

	void setPosition(int position);

	/**
	 * Gets the location of the equipment on the diver
	 * 
	 * @return
	 */
	int getPosition();

	/**
	 * Gets the index of the equipment in the list of the same type of
	 * equipment.
	 * 
	 * @return
	 */
	int getOrderIndex();

	/**
	 * sets the index of the equipment in the list of the same type of
	 * equipment.
	 * 
	 * @param orderIndex
	 */
	void setOrderIndex(int orderIndex);

	MaterialType getMaterialType();

	void setMaterial(Material material);

	Material getMaterial();

	String getComment();

	void setComment(String comment);

	String getShortDescription();
}
