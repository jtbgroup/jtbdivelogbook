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

import java.security.InvalidParameterException;

import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.logging.Syslog;

public abstract class AbstractEquipment implements Equipment {

	private static final long serialVersionUID = -6636009807996479330L;
	private static final Syslog LOGGER = Syslog
			.getLogger(AbstractEquipment.class);
	/**
	 * The position of the equipment on the body. This ain't used yet...
	 */
	private int position;

	/**
	 * The index of the equipment in the list of equipment of the same kind. The
	 * first index is 0.
	 */
	private int orderIndex;

	private Material material;

	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		if (material != null
				&& !material.getMaterialType().equals(getMaterialType())) {
			if (getMaterialType() != MaterialType.OTHER) {
				throw new InvalidParameterException(
						"Couldn't add this material due to his type ("
								+ material.getMaterialType() + " instead of "
								+ getMaterialType() + ")");
			} else {
				LOGGER.warn("The material type of the added material is defined as "
						+ material.getMaterialType()
						+ " while the equipment ("
						+ toString() + ") is defined as OTHER");
			}
		}
		this.material = material;
	}

	public int getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(int orderIndex) {
		if (orderIndex < 0) {
			throw new IllegalArgumentException(
					"Your order index can't be negative (passed values is "
							+ orderIndex + ")");
		}
		this.orderIndex = orderIndex;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public String getShortDescription() {
		if (null != getMaterial()){
			return "# " + orderIndex + " - "
					+ getMaterial().getShortDescription();
		}

		return "# " + orderIndex;
	}
}
