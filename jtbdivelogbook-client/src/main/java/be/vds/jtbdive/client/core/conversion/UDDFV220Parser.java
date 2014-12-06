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
package be.vds.jtbdive.client.core.conversion;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.client.util.XsdValidator;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveProfile;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.GazMix;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.Palanquee;
import be.vds.jtbdive.core.core.PalanqueeEntry;
import be.vds.jtbdive.core.core.Version;
import be.vds.jtbdive.core.core.catalogs.Gaz;
import be.vds.jtbdive.core.core.comparator.DiveDateComparator;
import be.vds.jtbdive.core.core.comparator.EquipmentIndexComparator;
import be.vds.jtbdive.core.core.material.DiveTankEquipment;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.XMLValidationException;

/**
 * This parser is written to deal with UDDF standard V2.2.0. To see which tags
 * are filled, please refer to the write method.s
 * 
 * @author gautier
 */
public class UDDFV220Parser {

	private static final String UDDF_VERSION = "2.2.0";
	private static final long HOURS_24 = 24 * 60 * 60;

	public void write(List<Dive> dives, Diver owner, OutputStream outputStream)
			throws IOException, XMLValidationException {
		write(dives, owner, outputStream, null);
	}

	/**
	 * Tags that are filled
	 * <ul>
	 * <li>generator : originator software
	 * <li>gasdefinition : list of all the gaz mixes used in the list of dives
	 * <li>divers : list of divers contained in the list of dives. Owner and
	 * Buddies are set. Only a few data are stored for the divers (personal)
	 * <li>profiledata : the data of the dives; they are grouped into
	 * 'repetitiongroup' if the time between dives is less than 24 hours.
	 * </ul>
	 * 
	 * @param dives
	 * @param owner
	 * @param outputStream
	 * @throws IOException
	 * @throws XMLValidationException
	 */
	public void write(List<Dive> dives, Diver owner, OutputStream outputStream,
			InputStream xsdInputStream) throws IOException,
			XMLValidationException {
		Document document = createUddfEl(dives, owner);

		if (xsdInputStream != null) {
			boolean b = XsdValidator.validXML(document, xsdInputStream);
			if (!b) {
				throw new XMLValidationException(
						"The XML generated doesn't fulfill the XSD uddf.xsd");
			}
		}

		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		outputter.output(document, outputStream);
		outputStream.close();
	}

	private Element createSurfaceTimeElement(long seconds, Dive dive) {
		Element surfIntervalEl = new Element("surfaceinterval");
		if (seconds == 0 || seconds > HOURS_24) {
			Element infinityEl = new Element("infinity");
			surfIntervalEl.addContent(infinityEl);
		} else {
			Element passedTimeEl = new Element("passedtime");
			// Surface time is expressed in seconds
			long surfTime = dive.getSurfaceTime();
			if (surfTime == 0) {
				surfTime = seconds;
			}
			passedTimeEl.setText(String.valueOf(surfTime));
			surfIntervalEl.addContent(passedTimeEl);
		}
		return surfIntervalEl;
	}

	private Document createUddfEl(List<Dive> dives, Diver owner) {
		Document doc = new Document();
		Map<Integer, GazMix> gazMixes = LogBookUtilities.getGazMixesUsed(dives);
		List<Diver> divers = LogBookUtilities.getDivers(dives, owner);
		List<DiveSite> diveSites = LogBookUtilities.getDiveSites(dives);

		doc.setRootElement(createRootElement(dives, gazMixes, divers, owner,
				diveSites));
		return doc;
	}

	private Element createRootElement(List<Dive> dives,
			Map<Integer, GazMix> gazMixes, List<Diver> divers, Diver owner,
			List<DiveSite> diveSites) {
		Element uddfEl = new Element("uddf");
		uddfEl.setAttribute("version", UDDF_VERSION);
		uddfEl.addContent(createGeneratorEl());
		uddfEl.addContent(createGasDefinitionEl(gazMixes));
		uddfEl.addContent(createDiversEl(divers, owner));
		uddfEl.addContent(createDiveSitesEl(diveSites));
		uddfEl.addContent(createDiveProfileEl(dives, gazMixes));

		return uddfEl;
	}

	private Element createDiveSitesEl(List<DiveSite> diveSites) {
		Element el = new Element("divesite");
		for (DiveSite diveSite : diveSites) {
			Element diveSiteEl = createDiveSiteElement(diveSite);
			el.addContent(diveSiteEl);
		}
		return el;
	}

	private Element createDiveSiteElement(DiveSite diveSite) {
		Element el = new Element("site");
		el.setAttribute("id", String.valueOf(diveSite.getId()));

		// name
		Element nameEl = new Element("name");
		nameEl.setText(diveSite.getName());
		el.addContent(nameEl);

		// geography
		Element geographyEl = new Element("geography");
		el.addContent(geographyEl);

		Element altitudeEl = new Element("altitude");
		altitudeEl.setText(String.valueOf(diveSite.getAltitude()));
		geographyEl.addContent(altitudeEl);

		if (diveSite.getCoordinates() != null) {
			Element latEl = new Element("latitude");
			latEl.setText(String.valueOf(diveSite.getCoordinates()
					.getLatitude()));
			geographyEl.addContent(latEl);

			Element longEl = new Element("longitude");
			longEl.setText(String.valueOf(diveSite.getCoordinates()
					.getLongitude()));
			geographyEl.addContent(longEl);
		}

		// site data
		Element siteDataEl = new Element("sitedata");
		el.addContent(siteDataEl);

		Element maxDepthEl = new Element("maximumdepth");
		maxDepthEl.setText(String.valueOf(diveSite.getDepth()));
		siteDataEl.addContent(maxDepthEl);

		return el;
	}

	private Element createDiversEl(List<Diver> divers, Diver owner) {
		Element el = new Element("diver");
		for (Diver diver : divers) {
			Element buddyEl = null;
			if (owner != null && diver.equals(owner)) {
				buddyEl = createOwnerElement(diver);

			} else {
				buddyEl = createBuddyElement(diver);
			}
			el.addContent(buddyEl);
		}
		return el;
	}

	private Element createOwnerElement(Diver diver) {

		Element buddyEl = new Element("owner");
		buddyEl.setAttribute("id", String.valueOf(diver.getId()));

		Element personnalEl = new Element("personnal");
		buddyEl.addContent(personnalEl);

		Element fnEl = new Element("firstname");
		fnEl.setText(diver.getFirstName());
		personnalEl.addContent(fnEl);

		Element lnEl = new Element("lastname");
		lnEl.setText(diver.getLastName());
		personnalEl.addContent(lnEl);

		return buddyEl;
	}

	private Element createBuddyElement(Diver diver) {

		Element buddyEl = new Element("buddy");
		buddyEl.setAttribute("id", String.valueOf(diver.getId()));

		Element personnalEl = new Element("personnal");
		buddyEl.addContent(personnalEl);

		Element fnEl = new Element("firstname");
		fnEl.setText(diver.getFirstName());
		personnalEl.addContent(fnEl);

		Element lnEl = new Element("lastname");
		lnEl.setText(diver.getLastName());
		personnalEl.addContent(lnEl);

		return buddyEl;
	}

	private Element createGasDefinitionEl(Map<Integer, GazMix> gazMixes) {
		Element el = new Element("gasdefinition");
		for (Integer id : gazMixes.keySet()) {
			Element mix = new Element("mix");
			mix.setAttribute("id", String.valueOf(id));

			Element o2 = new Element("o2");
			o2.setText(String.valueOf(gazMixes.get(id).getPercentage(
					Gaz.GAZ_OXYGEN)));
			mix.addContent(o2);

			Element n2 = new Element("n2");
			n2.setText(String.valueOf(gazMixes.get(id).getPercentage(
					Gaz.GAZ_NITROGEN)));
			mix.addContent(n2);

			Element he = new Element("he");
			he.setText(String.valueOf(gazMixes.get(id).getPercentage(
					Gaz.GAZ_HELIUM)));
			mix.addContent(he);

			el.addContent(mix);

		}
		return el;

	}

	private Element createDiveProfileEl(List<Dive> dives,
			Map<Integer, GazMix> gazMixes) {
		Collections.sort(dives, new DiveDateComparator());
		List<List<Dive>> groups = new ArrayList<List<Dive>>();
		List<Dive> repetitionGroup = new ArrayList<Dive>();
		Dive previousDive = null;
		for (Dive dive : dives) {
			if (previousDive == null) {
				repetitionGroup.add(dive);
			} else {
				long elapsedTime = secondsBetweenDives(dive, previousDive);
				if (elapsedTime > HOURS_24) {
					groups.add(repetitionGroup);
					repetitionGroup = new ArrayList<Dive>();
					repetitionGroup.add(dive);
				} else if (dive.getSurfaceTime() == 0 && elapsedTime == 0) {
					repetitionGroup.add(dive);
				} else if (elapsedTime <= HOURS_24) {
					repetitionGroup.add(dive);
				} else {
					groups.add(repetitionGroup);
					repetitionGroup = new ArrayList<Dive>();
					repetitionGroup.add(dive);
				}
			}
			previousDive = dive;
		}
		groups.add(repetitionGroup);

		Element profiledataEl = new Element("profiledata");
		for (List<Dive> list : groups) {
			profiledataEl.addContent(createRepetitionGroupEl(list, gazMixes));
		}
		return profiledataEl;
	}

	private Element createRepetitionGroupEl(List<Dive> dives,
			Map<Integer, GazMix> gazMixes) {
		Element el = new Element("repetitiongroup");
		Dive previousDive = null;
		for (Dive dive : dives) {
			long seconds = 0;
			if (previousDive != null) {
				seconds = secondsBetweenDives(dive, previousDive);
			}

			el.addContent(createDiveEl(dive, seconds, gazMixes));
			previousDive = dive;
		}
		return el;
	}

	private Element createDiveEl(Dive dive, long seconds,
			Map<Integer, GazMix> gazMixes) {
		Element el = new Element("dive");
		el.setAttribute("id", String.valueOf(dive.getId()));
		el.addContent(createSurfaceTimeElement(seconds, dive));
		el.addContent(createDiveNumberEl(dive));
		el.addContent(createDiveDateEl(dive.getDate()));
		el.addContent(createTimeEl(dive.getDate()));

		if (dive.getComment() != null && dive.getComment().length() > 0) {
			el.addContent(createObservationsEl(dive.getComment()));
		}

		if (dive.getDiveProfile() != null) {
			el.addContent(createSamplesEl(dive.getDiveProfile(),
					matchUsedGaz(dive, gazMixes)));
		}

		if (dive.getPalanquee() != null) {
			Element pEl = null;
			for (PalanqueeEntry entry : dive.getPalanquee()
					.getPalanqueeEntries()) {
				pEl = new Element("buddyref");
				pEl.setAttribute("ref",
						String.valueOf(entry.getDiver().getId()));
				el.addContent(pEl);
			}
		}

		return el;
	}
	
	private String matchUsedGaz(Dive dive, Map<Integer, GazMix> gazMixes) {
		if (dive.getDiveEquipment() != null
				&& dive.getDiveEquipment().getDiveTanks() != null
				&& dive.getDiveEquipment().getDiveTanks().size() > 0) {
			List<DiveTankEquipment> tanks = dive.getDiveEquipment().getDiveTanks();
			Collections.sort(tanks, new EquipmentIndexComparator());
			GazMix gazMix = tanks.get(0).getGazMix();
			for (Integer id : gazMixes.keySet()) {
				if (gazMix.hasSameComposition(gazMixes.get(id))) {
					return String.valueOf(id);
				}
			}
		}

		return null;
	}

	private Element createSamplesEl(DiveProfile diveProfile, String gazId) {
		Element el = new Element("samples");

		List<Double> times = new ArrayList<Double>(diveProfile
				.getDepthEntries().keySet());
		Collections.sort(times);

		for (Double second : times) {
			Element wp = new Element("waypoint");
			el.addContent(wp);

			Element depth = new Element("depth");
			depth.setText(String.valueOf(Math.abs(diveProfile.getDepthEntries()
					.get(second))));
			wp.addContent(depth);

			Element divetime = new Element("divetime");
			divetime.setText(String.valueOf(second));
			wp.addContent(divetime);

			if (diveProfile.getAscentWarnings().contains(second)) {
				Element al = new Element("alarm");
				al.setText("ascent");
				wp.addContent(al);
			}

			if (diveProfile.getRemainingBottomTimeWarnings().contains(second)) {
				Element al = new Element("alarm");
				al.setText("rbt");
				wp.addContent(al);
			}

			if (diveProfile.getDecoCeilingWarnings().contains(second)) {
				Element al = new Element("alarm");
				al.setText("error");
				wp.addContent(al);
			}

			if (diveProfile.getDecoEntries().contains(second)) {
				Element al = new Element("alarm");
				al.setText("deco");
				wp.addContent(al);
			}

		}

		return el;
	}

	private Element createObservationsEl(String comment) {
		Element el = new Element("observations");
		Element note = new Element("notes");
		note.setText(comment);
		el.addContent(note);
		return el;
	}

	private Element createDiveNumberEl(Dive dive) {
		Element el = new Element("divenumber");
		el.setText(String.valueOf(dive.getNumber()));
		return el;
	}

	private Element createTimeEl(Date diveDate) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(diveDate);
		Element el = new Element("time");
		Element hel = new Element("hour");
		hel.setText(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
		el.addContent(hel);
		Element mel = new Element("minute");
		mel.setText(String.valueOf(cal.get(Calendar.MINUTE)));
		el.addContent(mel);
		return el;

	}

	private Element createDiveDateEl(Date diveDate) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(diveDate);

		Element el = new Element("date");

		Element year = new Element("year");
		year.setText(String.valueOf(cal.get(Calendar.YEAR)));
		el.addContent(year);

		Element month = new Element("month");
		month.setText(String.valueOf(cal.get(Calendar.MONTH)));
		el.addContent(month);

		Element day = new Element("day");
		day.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		el.addContent(day);

		Element dayOfWeek = new Element("dayofweek");
		int count = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (count == 0) {
			count = 7;
		}
		dayOfWeek.setText(String.valueOf(count));
		el.addContent(dayOfWeek);

		return el;
	}

	private Element createGeneratorEl() {
		Element el = new Element("generator");
		el.setText("JtbDiveLogbook-" + Version.getCurrentVersion().toString());
		return el;
	}

	private long secondsBetweenDives(Dive dive, Dive previousDive) {
		return (dive.getDate().getTime() - previousDive.getDate().getTime()) / 1000;
	}

	public LogBook read(InputStream inputStream) throws DataStoreException {
		try {
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(inputStream);
			Element root = doc.getRootElement();
			LogBook lb = createLogBook(root);
			return lb;
		} catch (JDOMException e) {
			throw new DataStoreException(e);
		} catch (IOException e) {
			throw new DataStoreException(e);
		}
	}

	private LogBook createLogBook(Element root) {
		Map<String, Diver> divers = createDiversMap(root);

		LogBook lb = new LogBook();
		lb.setName(root.getChildText("generator"));
		lb.setDives(createDives(root, divers));
		return lb;
	}

	private List<Dive> createDives(Element root, Map<String, Diver> divers) {
		List<Dive> dives = new ArrayList<Dive>();
		Element profileEl = root.getChild("profiledata");
		Dive dive = null;
		for (Iterator iterator = profileEl.getChildren("repetitiongroup")
				.iterator(); iterator.hasNext();) {
			Element repEl = (Element) iterator.next();

			int diveInterval = 0;
			Element surfIntervalEl = repEl.getChild("surfaceinterval");
			if (null != surfIntervalEl) {
				if (surfIntervalEl.getChild("passedtime") != null) {
					diveInterval = Integer.parseInt(surfIntervalEl
							.getChildText("passedtime"));
				}
			}

			for (Iterator iterator2 = repEl.getChildren("dive").iterator(); iterator2
					.hasNext();) {
				Element diveEl = (Element) iterator2.next();
				dive = new Dive();
				dive.setSurfaceTime(diveInterval);
				dive.setDate(createDate(diveEl));
				dive.setNumber(createNumber(diveEl));
				dive.setSurfaceTime(createSurfaceTime(diveEl) / 60);
				dive.setPalanquee(createPalanquee(diveEl, divers));
				dive.setDiveProfile(createDiveProfile(diveEl));
				dive.setComment(createComment(diveEl));

				dives.add(dive);
			}
		}
		return dives;
	}

	private String createComment(Element diveEl) {
		Element el = diveEl.getChild("observations");
		if (null != el) {
			el = el.getChild("notes");
			if (el != null)
				return el.getText();
		}
		return null;
	}

	private DiveProfile createDiveProfile(Element diveEl) {
		Element samplesEl = diveEl.getChild("samples");
		if (samplesEl == null) {
			return null;
		}

		Map<Double, Double> depths = new HashMap<Double, Double>();
		Set<Double> ascent = new HashSet<Double>();
		Set<Double> decoCeilingWarnings = new HashSet<Double>();
		Set<Double> remainingBottomTimeWarnings = new HashSet<Double>();
		Set<Double> decoEntries = new HashSet<Double>();
		for (Iterator iterator = samplesEl.getChildren("waypoint").iterator(); iterator
				.hasNext();) {
			Element waypointEl = (Element) iterator.next();
			double time = Double.parseDouble(waypointEl
					.getChildText("divetime"));
			double depth = -(Math.abs(Double.parseDouble(waypointEl
					.getChildText("depth"))));
			depths.put(time, depth);

			for (Iterator iterator2 = waypointEl.getChildren("alarm")
					.iterator(); iterator2.hasNext();) {
				Element alarmEl = (Element) iterator2.next();
				String alarmVal = alarmEl.getText();
				if (alarmVal.equals("ascent")) {
					ascent.add(time);
				} else if (alarmVal.equals("rbt")) {
					remainingBottomTimeWarnings.add(time);
				} else if (alarmVal.equals("error")) {
					decoCeilingWarnings.add(time);
				} else if (alarmVal.equals("deco")) {
					decoEntries.add(time);
				}
			}

		}

		if (depths.size() == 0)
			return null;

		DiveProfile dp = new DiveProfile();
		dp.setDepthEntries(depths);
		dp.setAscentWarnings(ascent);
		dp.setDecoCeilingWarnings(decoCeilingWarnings);
		dp.setDecoEntries(decoEntries);
		dp.setRemainingBottomTimeWarnings(remainingBottomTimeWarnings);

		return dp;
	}

	private Palanquee createPalanquee(Element diveEl, Map<String, Diver> divers) {
		List<Element> buddyRefs = diveEl.getChildren("buddyref");
		if (buddyRefs.size() == 0)
			return null;

		Palanquee p = new Palanquee();
		for (Iterator iterator = buddyRefs.iterator(); iterator.hasNext();) {
			Element element = (Element) iterator.next();
			Diver d = divers.get(element.getAttributeValue("ref"));
			if (d != null) {
				PalanqueeEntry entry = new PalanqueeEntry();
				entry.setDiver(d);
				entry.setPalanqueeRole(PalanqueeEntry.getBasicRole());
				p.addPalanqueeEntry(entry);
			}
		}
		if (p.size() > 0) {
			return p;
		}
		return null;
	}

	/**
	 * gets the number of seconds of surface time
	 * 
	 * @param diveEl
	 * @return
	 */
	private int createSurfaceTime(Element diveEl) {
		int res = 0;
		Element el = diveEl.getChild("surfaceinterval");
		if (el != null) {
			if ((el = el.getChild("passedtime")) != null) {
				res = Integer.parseInt(el.getText());
			}
		}
		return res;
	}

	private int createNumber(Element diveEl) {
		Element el = diveEl.getChild("divenumber");
		if (el != null)
			return Integer.parseInt(el.getText());
		return 0;
	}

	private Date createDate(Element diveEl) {
		// Date
		GregorianCalendar cal = new GregorianCalendar();

		Element dateEl = diveEl.getChild("date");
		Element timeEl = diveEl.getChild("time");

		cal.set(Calendar.YEAR, Integer.valueOf(dateEl.getChildText("year")));
		cal.set(Calendar.MONTH,
				Integer.valueOf(dateEl.getChildText("month")) - 1);
		cal.set(Calendar.DAY_OF_MONTH,
				Integer.valueOf(dateEl.getChildText("day")));

		cal.set(Calendar.HOUR_OF_DAY,
				Integer.valueOf(timeEl.getChildText("hour")));
		cal.set(Calendar.MINUTE, Integer.valueOf(timeEl.getChildText("minute")));

		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}


	private Map<String, Diver> createDiversMap(Element root) {
		Map<String, Diver> divers = new HashMap<String, Diver>();
		Element diversEl = root.getChild("diver");
		for (Iterator iterator = diversEl.getChildren("buddy").iterator(); iterator
				.hasNext();) {
			Element buddyEl = (Element) iterator.next();
			Diver d = new Diver();
			Element fnEl = buddyEl.getChild("personnal").getChild("firstname");
			if (fnEl != null) {
				d.setFirstName(fnEl.getValue());
			}
			Element lnEl = buddyEl.getChild("personnal").getChild("lastname");
			if (lnEl != null) {
				d.setLastName(lnEl.getValue());
			}
			divers.put(buddyEl.getAttributeValue("id"), d);
		}
		return divers;
	}
}
