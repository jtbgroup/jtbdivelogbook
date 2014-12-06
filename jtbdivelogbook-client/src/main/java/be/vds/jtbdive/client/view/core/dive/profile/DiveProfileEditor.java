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
package be.vds.jtbdive.client.view.core.dive.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import be.vds.jtbdive.core.core.DiveProfile;

public class DiveProfileEditor extends Observable {

	private List<ProfileEntry> profileEntries;

	public DiveProfileEditor() {
		profileEntries = new ArrayList<ProfileEntry>();
	}

	public void load(DiveProfile diveProfile) {
		profileEntries.clear();

		ProfileEntry entry = null;
		for (Double time : diveProfile.getDepthEntries().keySet()) {
			entry = new ProfileEntry();
			entry.setTime(time);
			entry.setDepth(diveProfile.getDepthEntries().get(time));
			entry.setDecoEntry(diveProfile.getDecoEntries().contains(time));
			entry.setAscentWarning(diveProfile.getAscentWarnings().contains(
					time));
			entry.setDecoCeilingWarning(diveProfile.getDecoCeilingWarnings()
					.contains(time));
			entry.setRemainingBottomTimeWarning(diveProfile
					.getRemainingBottomTimeWarnings().contains(time));
			profileEntries.add(entry);
		}

		setChanged();
		notifyObservers(new DiveProfileEditionEvent(
				DiveProfileEditionEvent.DIVE_PROFILE_SET, null));
	}

	public DiveProfile getDiveProfile() {
		DiveProfile dp = new DiveProfile();
		Map<Double, Double> depths = new HashMap<Double, Double>();
		Set<Double> decoSet = new HashSet<Double>();
		Set<Double> ascentsW = new HashSet<Double>();
		Set<Double> decoCeilingW = new HashSet<Double>();
		Set<Double> remainingBottomW = new HashSet<Double>();

		for (ProfileEntry entry : profileEntries) {
			depths.put(entry.getTime(), entry.getDepth());

			if (entry.isDecoEntry())
				decoSet.add(entry.getTime());

			if (entry.isAscentWarning())
				ascentsW.add(entry.getTime());

			if (entry.isDecoCeilingWarning())
				decoCeilingW.add(entry.getTime());

			if (entry.isRemainingBottomTimeWarning())
				remainingBottomW.add(entry.getTime());
		}

		dp.setDepthEntries(depths);
		dp.setDecoEntries(decoSet);
		dp.setAscentWarnings(ascentsW);
		dp.setDecoCeilingWarnings(decoCeilingW);
		dp.setRemainingBottomTimeWarnings(remainingBottomW);
		return dp;
	}

	public void addEntry(ProfileEntry entry, Object originator) {
		profileEntries.add(entry);
		setChanged();
		notifyObservers(new DiveProfileEditionEvent(
				DiveProfileEditionEvent.ENTRY_ADDED, originator, null, entry));
	}

	public void addEntry(ProfileEntry entry, int index, Object originator) {
		profileEntries.add(index, entry);
		setChanged();
		notifyObservers(new DiveProfileEditionEvent(
				DiveProfileEditionEvent.ENTRY_ADDED, originator, null, entry));
	}

	public List<ProfileEntry> getProfileEntries() {
		return profileEntries;
	}

	public void entrySelected(ProfileEntry entry, Object originator) {
		setChanged();
		notifyObservers(new DiveProfileEditionEvent(
				DiveProfileEditionEvent.ENTRY_SELECTED, originator, null, entry));
	}

	public void replaceEntry(ProfileEntry oldEntry, ProfileEntry newEntry,
			Object originator) {
		int index = profileEntries.indexOf(oldEntry);
		if (index > -1) {
			profileEntries.remove(oldEntry);
			profileEntries.add(index, newEntry);
		} else {
			profileEntries.add(newEntry);
		}
		setChanged();
		notifyObservers(new DiveProfileEditionEvent(
				DiveProfileEditionEvent.ENTRY_MODIFIED, originator, oldEntry,
				newEntry));
	}

	public double getProfileTimeAfter(ProfileEntry profileEntry) {
		if (profileEntries == null || profileEntries.size() == 0)
			return 0;

		List<ProfileEntry> entries = new ArrayList<ProfileEntry>(profileEntries);

		int maxIndex = -1;
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).getTime() > profileEntry.getTime()) {
				maxIndex = i;
				break;
			}
		}

		int minIndex = maxIndex - 1;

		if (maxIndex == -1)
			return entries.get(entries.size() - 1).getTime() + 1;

		if (maxIndex == 0)
			return 0;

		return entries.get(minIndex).getTime()
				+ ((entries.get(maxIndex).getTime() - entries.get(minIndex)
						.getTime()) / 2);
	}

	public void removeEntry(ProfileEntry profileEntry, Object originator) {
		profileEntries.remove(profileEntry);
		setChanged();
		notifyObservers(new DiveProfileEditionEvent(
				DiveProfileEditionEvent.ENTRY_REMOVED, originator, profileEntry, null));
	}

}
