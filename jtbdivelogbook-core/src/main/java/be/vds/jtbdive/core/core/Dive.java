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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.vds.jtbdive.core.core.catalogs.DivePlatform;
import be.vds.jtbdive.core.core.catalogs.DivePurpose;
import be.vds.jtbdive.core.core.catalogs.DiveType;
import be.vds.jtbdive.core.logging.Syslog;

/**
 * The dive is an object containing all the needed informations in different
 * categories: the physiological status, the buddies or the other admin data. It
 * contains also a dive profile that is the dive curve. A set of documents can
 * be added to give more informations on the dive.
 * 
 * The Dive has an id. If the id is -1, it means the dive is not persisted yet.
 * 
 * @author Gautier Vanderslyen
 * 
 */
public class Dive implements Serializable {

	private static final long serialVersionUID = 4247338743975530599L;
	private static final Syslog LOGGER = Syslog.getLogger(Dive.class);
	private long id = -1;
	private int number;
	private Date date;
	private String comment;
	/**
	 * Expressed in meter. This should be a negative value.
	 */
	private double maxDepth;
	/**
	 * Expressed in seconds (since version 2.6.0)
	 */
	private int diveTime;
	/**
	 * Expressed in Celsuis
	 */
	private double waterTemperature;
	private DiveProfile diveProfile;
	/**
	 * Expressed in seconds (since version 2.6.0)
	 */
	private int surfaceTime;
	private Palanquee palanquee;
	private DiveSite diveSite;
	private PhysiologicalStatus physiologicalStatus;
	private DiveEquipment diveEquipment;
	private List<Document> documents;
	private Rating rating;
	private List<DiveType> diveTypes;
	private List<DivePurpose> divePurposes;
	private DivePlatform divePlatform;

	public Dive() {
	}

	public Dive(int number) {
		this(number, null);
	}

	public Dive(int number, Date date) {
		this.number = number;
		this.date = date;
	}

	public Dive(Date date) {
		this.date = date;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	/**
	 * Gets the dive equipement used during the dive.
	 * 
	 * @return the dive equipment. Can be null.
	 */
	public DiveEquipment getDiveEquipment() {
		return diveEquipment;
	}

	public void setDiveEquipment(DiveEquipment diveEquipment) {
		this.diveEquipment = diveEquipment;
	}

	/**
	 * Gets the date of the dive. This date is the beginning of the dive.
	 * 
	 * @return the date of the dive
	 */
	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Dive (").append(id).append(") ").append(number)
				.append(" on ").append(date);
		return sb.toString();
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Sets the date of the dive.
	 * 
	 * @param date
	 *            the date at which the dive started
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Gets the dive time in seconds
	 */
	public int getDiveTime() {
		return diveTime;
	}

	/**
	 * sets the dive time in seconds
	 */
	public void setDiveTime(int diveTime) {
		this.diveTime = diveTime;
		if (null != this.diveProfile) {
			this.diveProfile.limitMaximumTime(diveTime);
		}
		if (null != this.diveEquipment) {
			this.diveEquipment.setMaximumTime(diveTime);
		}
	}

	/**
	 * Gets the max depth in meter
	 * 
	 * @return
	 */
	public double getMaxDepth() {
		return maxDepth;
	}

	/**
	 * sets the max depth in meter. Even if the depth is positive, it is set as
	 * negative in the model.
	 * 
	 * @param depth
	 *            the depth of the dive. This argument should be negative.
	 */
	public void setMaxDepth(double depth) {
		if (depth > 0d) {
			this.maxDepth = -depth;
		} else {
			this.maxDepth = depth;
		}

		if (null != this.diveProfile) {
			this.diveProfile.setMaximumDepth(depth);
		}
	}

	/**
	 * Gets the water temperature in celsius
	 * 
	 * @return the water temperature in celsius
	 */
	public double getWaterTemperature() {
		return waterTemperature;
	}

	public void setWaterTemperature(double waterTemperature) {
		this.waterTemperature = waterTemperature;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (obj instanceof Dive) {
			Dive dive = (Dive) obj;
			if (this.id == -1 && dive.getId() == -1) {
				return this == obj;
			}
			return this.id == dive.getId();
		}
		return false;
	}

	public void setDiveProfile(DiveProfile diveProfile) {
		this.diveProfile = diveProfile;
	}

	public DiveProfile getDiveProfile() {
		return diveProfile;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setPalanquee(Palanquee palanquee) {
		this.palanquee = palanquee;
	}

	public Palanquee getPalanquee() {
		return palanquee;
	}

	/**
	 * Gets the dive site of the dive
	 * 
	 * @return the dive site where the dive has been performed.
	 */
	public DiveSite getDiveSite() {
		return diveSite;
	}

	public void setDiveSite(DiveSite diveSite) {
		this.diveSite = diveSite;
	}

	/**
	 * Sets the surface time of the dive. This means the time between the last
	 * dive if tissues are still satured. 0 implies tissues are not satured
	 * anymore.
	 * 
	 * @param surfaceTime
	 *            the unit is the second.
	 */
	public void setSurfaceTime(int surfaceTime) {
		this.surfaceTime = surfaceTime;
	}

	/**
	 * Gets the surface time before diving. The value is expressed in seconds.
	 * 
	 * @return
	 */
	public int getSurfaceTime() {
		return surfaceTime;
	}

	public void setPhysiologicalStatus(PhysiologicalStatus physiologicalStatus) {
		this.physiologicalStatus = physiologicalStatus;
	}

	public PhysiologicalStatus getPhysiologicalStatus() {
		return physiologicalStatus;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public void addDocument(Document document) {
		if (documents == null) {
			documents = new ArrayList<Document>();
		}
		documents.add(document);
		LOGGER.debug("document added to dive " + id);
	}

	public void removeDocument(Document document) {
		if (documents != null) {
			documents.remove(document);
		}
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public List<DivePurpose> getDivePurposes() {
		return divePurposes;
	}

	public List<DiveType> getDiveTypes() {
		return diveTypes;
	}

	public void setDiveType(List<DiveType> diveTypes) {
		this.diveTypes = diveTypes;
	}

	public void setDivePurposes(List<DivePurpose> divePurposes) {
		this.divePurposes = divePurposes;
	}

	public DivePlatform getDivePlatform() {
		return this.divePlatform;
	}

	public void setDivePlatform(DivePlatform divePlatform) {
		this.divePlatform = divePlatform;
	}

	public int hashCode() {
		if (-1 == id) {
			return this.hashCode();
		} else {
			return Long.valueOf(id).hashCode();
		}
	};
}
