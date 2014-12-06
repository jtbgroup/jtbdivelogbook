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
package be.vds.jtbdive.client.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Contact;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.GazMix;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.PalanqueeEntry;
import be.vds.jtbdive.core.core.catalogs.ContactType;
import be.vds.jtbdive.core.core.catalogs.Gaz;
import be.vds.jtbdive.core.core.material.DiveTankEquipment;
import be.vds.jtbdive.core.core.material.Equipment;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.core.utils.StringManipulator;
import be.vds.jtbdive.core.utils.UnitsUtilities;

/**
 * 
 * @author gautier
 */
public class LogBookUtilities {

	private static final Syslog LOGGER = Syslog
			.getLogger(LogBookUtilities.class);

	public static Map<Integer, GazMix> getGazMixesUsed(List<Dive> dives) {
		Map<Integer, GazMix> gazmixes = new HashMap<Integer, GazMix>();

		int counter = 1;
		GazMix mix = new GazMix();
		mix.addGaz(Gaz.GAZ_OXYGEN, 21);
		mix.addGaz(Gaz.GAZ_NITROGEN, mix.getPercentRest());
		gazmixes.put(counter++, mix);

		for (Dive dive : dives) {
			counter = checkGazMix(gazmixes, counter, dive);
		}
		return gazmixes;
	}

	private static int checkGazMix(Map<Integer, GazMix> gazmixes, int counter,
			Dive dive) {
		if (dive.getDiveEquipment() != null
				&& dive.getDiveEquipment().getDiveTanks() != null) {
			for (DiveTankEquipment diveTank : dive.getDiveEquipment()
					.getDiveTanks()) {
				GazMix gazMix = diveTank.getGazMix();
				if (gazMix != null) {
					boolean hasSameComposition = false;
					for (GazMix itMix : gazmixes.values()) {
						if (gazMix.hasSameComposition(itMix)) {
							hasSameComposition = true;
							break;
						}
					}

					if (!hasSameComposition) {
						gazmixes.put(counter++, gazMix);
					}
				}
			}
		}
		return counter;
	}

	public static Map<Integer, GazMix> getGazMixesUsed(Dive dive) {
		Map<Integer, GazMix> gazmixes = new HashMap<Integer, GazMix>();

		int counter = 1;
		GazMix mix = new GazMix();
		mix.addGaz(Gaz.GAZ_OXYGEN, 21);
		mix.addGaz(Gaz.GAZ_NITROGEN, mix.getPercentRest());
		gazmixes.put(counter++, mix);

		counter = checkGazMix(gazmixes, counter, dive);
		return gazmixes;
	}

	public static List<Diver> getDivers(List<Dive> dives, Diver owner) {
		Set<Diver> divers = new HashSet<Diver>();

		if (owner != null) {
			divers.add(owner);
		}

		for (Dive dive : dives) {
			if (dive.getPalanquee() != null) {
				for (PalanqueeEntry palanqueeEntry : dive.getPalanquee()
						.getPalanqueeEntries()) {
					if (!divers.contains(palanqueeEntry.getDiver())) {
						divers.add(palanqueeEntry.getDiver());
					}
				}
			}
		}
		return new ArrayList<Diver>(divers);
	}

	public static List<DiveSite> getDiveSites(List<Dive> dives) {
		Set<DiveSite> diveSite = new HashSet<DiveSite>();

		for (Dive dive : dives) {
			if (dive.getDiveSite() != null) {
				diveSite.add(dive.getDiveSite());
			}
		}
		return new ArrayList<DiveSite>(diveSite);
	}

	public static List<DiveSite> getDiveSites(List<Dive> dives,
			DiveSiteManagerFacade diveSiteManagerFacade) {
		return getDiveSites(dives, false, diveSiteManagerFacade);
	}

	/**
	 * Gets the list of dive sites present into the list of dives. A load type
	 * level ensures the dive site is properly loaded (using the dive site
	 * manager facade if needed).
	 * 
	 * @param dives
	 *            the list of dives from which the dive sites must be extracted
	 * @param loadType
	 *            the load level of the dive site
	 * @param diveSiteManagerFacade
	 *            the facade that allows to load the dive site
	 * @return
	 */
	public static List<DiveSite> getDiveSites(List<Dive> dives,
			boolean includeDocumentsContent,
			DiveSiteManagerFacade diveSiteManagerFacade) {
		Map<Long, DiveSite> diveSitesMap = new HashMap<Long, DiveSite>();

		DiveSite goodDs = null;
		for (Dive dive : dives) {
			if (dive.getDiveSite() != null) {
				DiveSite ds = dive.getDiveSite();
				if (!diveSitesMap.containsKey(ds.getId())) {
					if (includeDocumentsContent
							&& !ds.areDocumentsContentLoaded()) {
						try {
							goodDs = diveSiteManagerFacade.findDiveSiteById(
									ds.getId(), true);
						} catch (DataStoreException e) {
							LOGGER.error("Can't load diveSite");
							goodDs = ds;
						}
					}
				}
				diveSitesMap.put(goodDs.getId(), goodDs);
			}
		}
		return new ArrayList<DiveSite>(diveSitesMap.values());
	}

	public static boolean areDiveListEquals(List<Dive> dives1, List<Dive> dives2) {
		if (dives1 == null || dives2 == null) {
			return false;
		}
		if (dives1.size() != dives2.size()) {
			return false;
		}

		for (Dive dive : dives1) {
			if (!dives2.contains(dive)) {
				return false;
			}
		}

		return true;
	}

	public static Map<String, Object> getLogBookData(LogBook logBook) {
		Map<String, Object> data = new HashMap<String, Object>();

		if (logBook.getOwner() != null) {
			data.put("owner", logBook.getOwner().getFullName());
		}

		data.put("dives.size", logBook.getDives().size());

		int maxDiveTime = 0;
		int minDiveTime = 0;
		int cumulDiveTime = 0;
		double maxDepth = 0;
		double minDepth = 0;
		double maxTemp = 0;
		double minTemp = 0;
		Date firstDate = null;
		Date lastDate = null;
		Set<DiveSite> diveSites = new HashSet<DiveSite>();
		Set<Diver> divers = new HashSet<Diver>();

		if (null != logBook.getOwner()) {
			divers.add(logBook.getOwner());
		}

		int counter = 0;
		for (Dive dive : logBook.getDives()) {
			cumulDiveTime += dive.getDiveTime();

			if (maxDiveTime < dive.getDiveTime()) {
				maxDiveTime = dive.getDiveTime();
			}
			if (counter == 0) {
				minDiveTime = dive.getDiveTime();
			} else if (minDiveTime > dive.getDiveTime()) {
				minDiveTime = dive.getDiveTime();
			}

			// Attention Depth is negative!
			if (maxDepth > dive.getMaxDepth()) {
				maxDepth = dive.getMaxDepth();
			}
			if (counter == 0) {
				minDepth = dive.getMaxDepth();
			} else if (minDepth < dive.getMaxDepth()) {
				minDepth = dive.getMaxDepth();
			}

			if (maxTemp < dive.getWaterTemperature()) {
				maxTemp = dive.getWaterTemperature();
			}
			if (counter == 0) {
				minTemp = dive.getWaterTemperature();
			} else if (minTemp > dive.getWaterTemperature()) {
				minTemp = dive.getWaterTemperature();
			}

			if ((dive.getDate() != null)
					&& (firstDate == null || firstDate.after(dive.getDate()))) {
				firstDate = dive.getDate();
			}

			if ((dive.getDate() != null)
					&& (lastDate == null || lastDate.before(dive.getDate()))) {
				lastDate = dive.getDate();
			}

			if (null != dive.getDiveSite()) {
				diveSites.add(dive.getDiveSite());
			}

			if (null != dive.getPalanquee()) {
				for (PalanqueeEntry pe : dive.getPalanquee()
						.getPalanqueeEntries()) {
					if (pe.getDiver() != null) {
						divers.add(pe.getDiver());
					}
				}
			}
			counter++;
		}

		data.put("depth.max", formatNumber(UnitsAgent.getInstance()
				.convertLengthFromModel(maxDepth))
				+ " ("
				+ UnitsAgent.getInstance().getLengthUnit().getSymbol() + ")");
		data.put("depth.min", formatNumber(UnitsAgent.getInstance()
				.convertLengthFromModel(minDepth))
				+ " ("
				+ UnitsAgent.getInstance().getLengthUnit().getSymbol() + ")");
		data.put("dive.time.max",
				StringManipulator.formatTimeInHour(maxDiveTime * 1000));
		data.put("dive.time.min",
				StringManipulator.formatTimeInHour(minDiveTime * 1000));
		data.put("dive.time.cumul",
				StringManipulator.formatTimeInHour(cumulDiveTime * 1000));
		if (firstDate != null) {
			data.put("dive.first.date", UIAgent.getInstance()
					.getFormatDateHoursFull().format(firstDate));
		}
		if (lastDate != null) {
			data.put("dive.last.date", UIAgent.getInstance()
					.getFormatDateHoursFull().format(lastDate));
		}
		data.put("divesites.size", diveSites.size());
		data.put("divers.size", divers.size());
		data.put("temperature.max", formatNumber(UnitsAgent.getInstance()
				.convertTemperatureFromModel(maxTemp))
				+ " ("
				+ UnitsAgent.getInstance().getTemperatureUnit().getSymbol()
				+ ")");
		data.put("temperature.min", formatNumber(UnitsAgent.getInstance()
				.convertTemperatureFromModel(minTemp))
				+ " ("
				+ UnitsAgent.getInstance().getTemperatureUnit().getSymbol()
				+ ")");

		return data;
	}

	private static String formatNumber(double number) {
		return StringManipulator.formatFixedDecimalNumber(number, 3, '.', true);
	}

	public static List<Material> getMaterials(List<Dive> dives) {
		Set<Material> material = new HashSet<Material>();
		for (Dive d : dives) {
			if (d.getDiveEquipment() != null) {
				for (Equipment eq : d.getDiveEquipment().getAllEquipments()) {
					if (eq.getMaterial() != null) {
						material.add(eq.getMaterial());
					}
				}
			}
		}
		return new ArrayList<Material>(material);
	}

	public static DiveSite findBestMatchForDiveSite(DiveSite ds,
			DiveSiteManagerFacade diveSiteManagerFacade) {
		try {
			List<DiveSite> sites = diveSiteManagerFacade.findDiveSitesByName(ds
					.getName());
			if (sites == null || sites.size() == 0) {
				return null;
			}
			return sites.get(0);
		} catch (DataStoreException e) {
			LOGGER.error(e);
		}
		return null;
	}

	public static Diver findBestMatchForDiver(Diver diver,
			DiverManagerFacade diverManagerFacade) {
		try {
			List<Diver> divers = diverManagerFacade.findDiversByName(
					diver.getFirstName(), diver.getLastName());
			if (divers == null || divers.size() == 0) {
				return null;
			}
			return divers.get(0);
		} catch (DataStoreException e) {
			LOGGER.error(e);
		}
		return null;
	}

	/**
	 * 
	 * @param dive
	 *            the dive on which some tests are performed
	 * @param newDiveTime
	 *            the new desired diveTime in <b>seconds</b>
	 * @return
	 */
	public static boolean doesNewTimeInfluenceDiveData(Dive dive,
			int newDiveTime) {
		int diveTime = dive.getDiveTime();
		if (newDiveTime > diveTime) {
			return false;
		}

		if (null != dive.getDiveProfile()) {
			if (newDiveTime < dive.getDiveProfile().getLastTimeEntry()) {
				return true;
			}
		}

		if (null != dive.getDiveEquipment()) {
			if (UnitsUtilities.convertMinutesToSeconds(newDiveTime) < dive
					.getDiveEquipment().getLastDiveTankSwitchTime()) {
				return true;
			}
		}

		return false;
	}

	public static boolean doesNewDepthInfluenceDiveData(Dive dive,
			double newDepth) {
		double depth = dive.getMaxDepth();
		if (newDepth < depth) {
			return false;
		}

		if (null != dive.getDiveProfile()) {
			double d = dive.getDiveProfile().getMaxDepth();
			if (newDepth > d) {
				return true;
			}
		}

		return false;
	}

	public static double getMaxDepth(List<Dive> dives) {
		double d = 0;
		for (Dive dive : dives) {
			double depth = dive.getMaxDepth();
			if (depth < d) {
				d = depth;
			}
		}
		return d;
	}

	public static int getDiversAge(Diver diver) {
		Date bd = diver.getBirthDate();
		Date currentDate = new Date();
		if (null == bd || currentDate.before(bd))
			return -1;

		Calendar cal = new GregorianCalendar();
		cal.setTime(currentDate);
		int curY = cal.get(Calendar.YEAR);
		int curM = cal.get(Calendar.MONTH);
		int curD = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(bd);
		int dY = cal.get(Calendar.YEAR);
		int dM = cal.get(Calendar.MONTH);
		int dD = cal.get(Calendar.DAY_OF_MONTH);

		int age = curY - dY;
		if (dM > curM) {
			age--;
		} else if (dM == curM) {
			if (dD > curD) {
				age--;
			}
		}
		return age;
	}

	public static Map<ContactType, List<Contact>> getContactMap(
			List<Contact> contacts) {
		if (null == contacts)
			return null;

		Map<ContactType, List<Contact>> map = new HashMap<ContactType, List<Contact>>();
		for (Contact contact : contacts) {
			if (!map.containsKey(contact.getContactType())) {
				map.put(contact.getContactType(), new ArrayList<Contact>());
			}
			map.get(contact.getContactType()).add(contact);
		}
		return map;
	}

	public static double getTotalPriceForMaterials(List<Material> materials) {
		double price = 0d;
		for (Material material : materials) {
			price += material.getPurchasePrice();
		}
		return price;
	}
}
