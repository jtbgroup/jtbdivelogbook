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
package be.vds.jtbdive.core.core.divecomputer.uwatec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveEquipment;
import be.vds.jtbdive.core.core.DiveProfile;
import be.vds.jtbdive.core.core.GazMix;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.core.PhysiologicalStatus;
import be.vds.jtbdive.core.core.catalogs.Gaz;
import be.vds.jtbdive.core.core.comparator.DiveDateComparator;
import be.vds.jtbdive.core.core.material.DiveComputerEquipment;
import be.vds.jtbdive.core.core.material.DiveTankEquipment;
import be.vds.jtbdive.core.core.material.Equipment;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.core.utils.UnitsUtilities;

public class UwatecDiveComputerAdapter {

	private static final Syslog LOGGER = Syslog
			.getLogger(UwatecDiveComputerAdapter.class);

	public Dive adapt(UwatecLogEntry logEntry) {
		Dive d = new Dive();
		setCommonData(logEntry, d);
		return d;
	}

	private void setCommonData(UwatecLogEntry logEntry, Dive d) {
		d.setDate(logEntry.getEntryTime());
		d.setWaterTemperature(logEntry.getWaterTemperature());
		d.setMaxDepth(logEntry.getMaximumDepth());
		d.setDiveTime((int)UnitsUtilities.convertMinutesToSeconds(logEntry.getBottomTime()));
		d.setSurfaceTime(logEntry.getSurfaceTime() * 60);
	}

	public void addUwatecProfileToDives(List<Dive> dives,
			UwatecData aladinData, MatCave matCave) {
		List<UwatecDepthProfile> depthProfiles = aladinData.getDepthProfiles();
		Collections.reverse(depthProfiles);
		Collections.sort(dives, new DiveDateComparator());
		Collections.reverse(dives);
		int i = 0;
		for (UwatecDepthProfile profile : depthProfiles) {
			Dive dive = dives.get(i);

			setDiveProfile(profile, dive);

			setPhysiologicalStatus(profile, dive);

			setEquipments(aladinData, profile, dive, matCave);

			i++;
		}
	}

	private void setEquipments(UwatecData aladinData,
			UwatecDepthProfile profile, Dive dive, MatCave matCave) {
		List<Equipment> equipments = new ArrayList<Equipment>();
		equipments.add(createDiveTank(profile));
		equipments.add(createDiveComputer(aladinData, matCave));

		if (equipments.size() > 0) {
			DiveEquipment eq = new DiveEquipment();
			eq.setEquipments(equipments);
			dive.setDiveEquipment(eq);
		}
	}

	private void setDiveProfile(UwatecDepthProfile profile, Dive dive) {
		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		Set<Double> decoWarnings = new HashSet<Double>();
		Set<Double> ascentTooFasts = new HashSet<Double>();
		Set<Double> remainingBottomTimeWarnings = new HashSet<Double>();
		Set<Double> decoEntries = new HashSet<Double>();

		double timer = 20;
		for (UwatecDepthProfileEntry depthProfileEntry : profile
				.getProfileEntries()) {
			// at 20 sec
			depthEntries.put(timer, (double) -depthProfileEntry.getDepth20());
			if ((depthProfileEntry.getWarnings20() & UwatecConstants.WARN_ASCENT_TOO_FAST) == UwatecConstants.WARN_ASCENT_TOO_FAST) {
				ascentTooFasts.add(timer);
			}
			if ((depthProfileEntry.getWarnings20() & UwatecConstants.WARN_DECO_STOP) == UwatecConstants.WARN_DECO_STOP) {
				decoWarnings.add(timer);
			}
			if ((depthProfileEntry.getWarnings20() & UwatecConstants.WARN_REMAINING_BOTTOM_TIME_TOO_SHORT) == UwatecConstants.WARN_REMAINING_BOTTOM_TIME_TOO_SHORT) {
				remainingBottomTimeWarnings.add(timer);
			}

			// at 40 sec
			timer += 20;
			depthEntries.put(timer, (double) -depthProfileEntry.getDepth40());
			if ((depthProfileEntry.getWarnings40() & UwatecConstants.WARN_ASCENT_TOO_FAST) == UwatecConstants.WARN_ASCENT_TOO_FAST) {
				ascentTooFasts.add(timer);
			}
			if ((depthProfileEntry.getWarnings40() & UwatecConstants.WARN_DECO_STOP) == UwatecConstants.WARN_DECO_STOP) {
				decoWarnings.add(timer);
			}
			if ((depthProfileEntry.getWarnings40() & UwatecConstants.WARN_REMAINING_BOTTOM_TIME_TOO_SHORT) == UwatecConstants.WARN_REMAINING_BOTTOM_TIME_TOO_SHORT) {
				remainingBottomTimeWarnings.add(timer);
			}

			// at 60 sec
			timer += 20;
			depthEntries.put(timer, (double) -depthProfileEntry.getDepth00());
			if ((depthProfileEntry.getWarnings00() & UwatecConstants.WARN_ASCENT_TOO_FAST) == UwatecConstants.WARN_ASCENT_TOO_FAST) {
				ascentTooFasts.add(timer);
			}
			if ((depthProfileEntry.getWarnings00() & UwatecConstants.WARN_DECO_STOP) == UwatecConstants.WARN_DECO_STOP) {
				decoWarnings.add(timer);
			}
			if ((depthProfileEntry.getWarnings00() & UwatecConstants.WARN_REMAINING_BOTTOM_TIME_TOO_SHORT) == UwatecConstants.WARN_REMAINING_BOTTOM_TIME_TOO_SHORT) {
				remainingBottomTimeWarnings.add(timer);
			}

			// DECO entries
			if (depthProfileEntry.getDecompression() != 0) {
				decoEntries.add(timer);
			}
			timer += 20;
		}

		DiveProfile dp = new DiveProfile();
		if (depthEntries.size() > 0) {
			dp.setDepthEntries(depthEntries);
		}
		if (decoWarnings.size() > 0) {
			dp.setDecoCeilingWarnings(decoWarnings);
		}
		if (ascentTooFasts.size() > 0) {
			dp.setAscentWarnings(ascentTooFasts);
		}
		if (remainingBottomTimeWarnings.size() > 0) {
			dp.setRemainingBottomTimeWarnings(remainingBottomTimeWarnings);
		}
		if (decoEntries.size() > 0) {
			dp.setDecoEntries(decoEntries);
		}

		dive.setDiveProfile(dp);
	}

	private Equipment createDiveComputer(UwatecData aladinData, MatCave matCave) {
		DiveComputerEquipment dc = new DiveComputerEquipment();

		// Find appropriate Material
		String serialNumber = String
				.valueOf(aladinData.getSettings().getSerialNumber());
		if (null != serialNumber && serialNumber.length() > 0
				&& matCave != null) {
			LOGGER.info("Found serial number : " + serialNumber);
			dc.setMaterial(matCave.getDiveComputerForSerialNumber(serialNumber));
		}

		dc.setRemainingBattery(aladinData.getCurrentStatus().getRemainingBattery());
		return dc;
	}

	private DiveTankEquipment createDiveTank(UwatecDepthProfile profile) {
		DiveTankEquipment dt = new DiveTankEquipment();
		short oxy = Short.parseShort(UwatecConstants.NITROX_MIX.get(profile
				.getNitroxMix()));
		GazMix gazMix = new GazMix();
		gazMix.addGaz(Gaz.GAZ_OXYGEN, oxy);
		gazMix.addGaz(Gaz.GAZ_NITROGEN, (short) (100 - oxy));
		dt.setGazMix(gazMix);

		return dt;
	}

	private void setPhysiologicalStatus(UwatecDepthProfile profile, Dive dive) {
		PhysiologicalStatus ps = new PhysiologicalStatus();
		ps.setMaxPPO2(Float.parseFloat(UwatecConstants.getPPO2(
				profile.getMaxPPO2()).replace(',', '.')));
		ps.setArterialMicrobubbleLevel(getArterialMicrobubbleLevel(profile));
		ps.setSkinCoolTemperature(getSkinCoolTemperature(profile));
		ps.setCnsBeforeDive(profile.getCNSBeforeDive());
		dive.setPhysiologicalStatus(ps);
	}

	private float getSkinCoolTemperature(UwatecDepthProfile profile) {
		return (float) profile.getEstimatedSkinCoolLevel();
	}

	private int getArterialMicrobubbleLevel(UwatecDepthProfile profile) {
		double arterialLevel = profile.getMicroBubbles()[0];
		if (0x000 <= arterialLevel && arterialLevel <= 0x010) {
			return UwatecConstants.ARTERIAL_MICROBUBBLE_LEVEL0;
		} else if (0x011 <= arterialLevel && arterialLevel <= 0x080) {
			return UwatecConstants.ARTERIAL_MICROBUBBLE_LEVEL1;
		} else if (0x081 <= arterialLevel && arterialLevel <= 0x100) {
			return UwatecConstants.ARTERIAL_MICROBUBBLE_LEVEL2;
		} else if (0x101 <= arterialLevel && arterialLevel <= 0x280) {
			return UwatecConstants.ARTERIAL_MICROBUBBLE_LEVEL3;
		} else if (0x281 <= arterialLevel && arterialLevel <= 0x480) {
			return UwatecConstants.ARTERIAL_MICROBUBBLE_LEVEL4;
		} else if (0x481 <= arterialLevel && arterialLevel <= 0x700) {
			return UwatecConstants.ARTERIAL_MICROBUBBLE_LEVEL5;
		} else if (0x701 <= arterialLevel && arterialLevel <= 0xa00) {
			return UwatecConstants.ARTERIAL_MICROBUBBLE_LEVEL6;
		} else if (0xa01 <= arterialLevel && arterialLevel <= 0xfff) {
			return UwatecConstants.ARTERIAL_MICROBUBBLE_LEVEL7;
		} else {
			return UwatecConstants.ARTERIAL_MICROBUBBLE_LEVEL_UNKNOWN;
		}

	}
}
