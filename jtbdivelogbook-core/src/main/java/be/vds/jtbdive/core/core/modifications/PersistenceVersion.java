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
package be.vds.jtbdive.core.core.modifications;

import java.util.Arrays;

import be.vds.jtbdive.core.core.Version;

/**
 * This enum is the list of all the modifications that occurred in the
 * persistence model. When a modification has been performed in a version, it
 * must be registered here.
 * 
 * @author vanderslyen.g
 * 
 */
public enum PersistenceVersion {
	V_1_0_0("1.0.0"), 
	V_2_0_0("2.0.0"), 
	V_2_6_0("2.6.0"), 
	V_2_7_0("2.7.0")
	;

	private String versionId;

	private PersistenceVersion(String versionId) {
		this.versionId = versionId;
	}

	protected static PersistenceVersion getVersionMapper(String versionId) {
		for (PersistenceVersion mapper : values()) {
			if (mapper.getVersionId().equals(versionId)) {
				return mapper;
			}
		}
		return null;
	}

	/**
	 * Returns the ID of the version
	 * 
	 * @return a String representing the Version.
	 */
	public String getVersionId() {
		return versionId;
	}

	/**
	 * This method returns the last persistence version registered.
	 * 
	 * @return the object {@link PersistenceVersion} that represents the last
	 *         modification in the persistence model.
	 */
	public static PersistenceVersion getLastPersistenceVersion() {
		PersistenceVersion[] v = PersistenceVersion.values();
		Arrays.sort(v, new PersistenceVersionComparator());
		return v[v.length - 1];
	}

	public static PersistenceVersion getPersistenceVersionForVersion(
			Version version) {
		PersistenceVersion[] pVersions = values();
		Arrays.sort(pVersions, new PersistenceVersionComparator());
		int index = 0;
		for (int i = 0; i < pVersions.length; i++) {
			PersistenceVersion persistenceVersion = pVersions[i];
			Version v2 = Version.createVersion(persistenceVersion
					.getVersionId());
			if ((version.isHigerOrEqualsThan(v2))) {
				index = i;
			} else {
				break;
			}
		}
		return pVersions[index];
	}
	
	public static PersistenceVersion getNextPersistenceVersion(
			PersistenceVersion version) {
		PersistenceVersion[] pVersions = PersistenceVersion.values();
		Arrays.sort(pVersions, new PersistenceVersionComparator());
		int index = -1;
		for (int i = 0; i < pVersions.length; i++) {
			if(pVersions[i].equals(version)){
				index = i;
				break;
			}
		}

		index = index == pVersions.length ? index : index + 1;
		return pVersions[index];
	}

}
