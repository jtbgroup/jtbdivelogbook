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
import java.util.List;

import be.vds.jtbdive.core.core.catalogs.DiveSiteType;

/**
 * Represents a location where a dive is possible. The id is -1 when the
 * location is not stored yet. A dive site has coordinates to determine the
 * exact position, but can also have an address. The altitude is also a part of
 * the dive site as it's the location that determines the altitude and not the
 * dive himself.
 * 
 * @author Gautier Vanderslyen
 * 
 */
public class DiveSite implements Serializable, PinableObject {

	private static final long serialVersionUID = -7200903196278137613L;

	private long id = -1;
	private double depth;
	private String name;
	private String internetSite;

	private Address address;
	private Coordinates coordinates;
	private double altitude;
	private List<Document> documents;
	private DiveSiteType diveSiteType;

	public DiveSite() {
	}

	public DiveSiteType getDiveSiteType() {
		return diveSiteType;
	}

	public void setDiveSiteType(DiveSiteType diveSiteType) {
		this.diveSiteType = diveSiteType;
	}


	/**
	 * Sets the depth of this location
	 * 
	 * @param depth
	 */
	public void setDepth(double depth) {
		this.depth = depth;
	}

	/**
	 * Sets the name of the location
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * gets the id
	 * 
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * gets the depth
	 * 
	 * @return
	 */
	public double getDepth() {
		return depth;
	}

	/**
	 * gets the name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the id
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(" (id : ").append(id).append(")");
		return sb.toString();
	}

	/**
	 * sets the address of the location
	 * 
	 * @param address
	 */
	public void setAddress(Address addresses) {
		this.address = addresses;
	}

	/**
	 * gets the address of the location
	 * 
	 * @return
	 */
	public Address getAddress() {
		return address;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (this == obj){
			return true;
		}

		if (obj instanceof DiveSite) {
			DiveSite diveLocation = (DiveSite) obj;

			if (-1 == id && -1 == diveLocation.getId()){
				return this == diveLocation;
			}

			if (id == diveLocation.getId()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(id).hashCode();
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	public boolean hasCoordinates() {
		return coordinates != null;
	}

	public String getInternetSite() {
		return internetSite;
	}

	public void setInternetSite(String internetSite) {
		this.internetSite = internetSite;
	}

	/**
	 * A document is considered to be fully loaded when the content is not null
	 * 
	 * @return true is all documents are loaded or if there is no document
	 */
	public boolean areDocumentsContentLoaded() {
		if (documents == null || documents.size() == 0){
			return true;
		}

		for (Document document : documents) {
			if (document.getContent() == null){
				return false;
			}
		}
		return true;
	}

	@Override
	public String getShortDescription() {
		return getName();
	}
}
