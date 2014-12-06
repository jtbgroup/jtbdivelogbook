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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import be.vds.jtbdive.client.util.XsdValidator;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveEquipment;
import be.vds.jtbdive.core.core.DiveProfile;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.GazMix;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.Version;
import be.vds.jtbdive.core.core.catalogs.Gaz;
import be.vds.jtbdive.core.core.comparator.DiveDateComparator;
import be.vds.jtbdive.core.core.material.DiveTankEquipment;
import be.vds.jtbdive.core.core.material.DiveTankMaterial;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.XMLValidationException;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.core.utils.StringManipulator;

/**
 * This parser is written to deal with UDDF standard V2.2.0. To see which tags
 * are filled, please refer to the write method.s
 * 
 * @author gautier
 */
public class UDCFParser {

	private static final String UDCF_VERSION = "1";
	private static final long HOURS_24 = 24 * 60 * 60;
	private static final Syslog LOGGER = Syslog.getLogger(UDCFParser.class);

	public void write(List<Dive> dives, Diver owner, OutputStream outputStream)
			throws IOException, XMLValidationException {
		write(dives, owner, outputStream, null);
	}

	/**
	 * Writes a UDCF file and validate it according to the xsd. An exception is
	 * thrown in case of validation exception.
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
		Document doc = createUdcf(dives, owner);

		if (null != xsdInputStream) {
			boolean b = XsdValidator.validXML(doc, xsdInputStream);
			if (!b) {
				throw new XMLValidationException(
						"The XML generated doesn't fulfill the XSD udcf.xsd");
			}
		}

		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		outputter.output(doc, outputStream);
		outputStream.close();

		LOGGER.info("Export to UDCF done.");
	}

	private Element createSurfaceTimeElement(long seconds, Dive dive) {
		Element surfIntervalEl = new Element("surfaceinterval");
		if (seconds == 0 || seconds > HOURS_24) {
			surfIntervalEl.setText("infinity");
		} else {
			long surfTime = dive.getSurfaceTime();
			if (surfTime == 0) {
				surfTime = seconds;
			}
			surfIntervalEl.setText(StringManipulator.formatFixedDecimalNumber(
					surfTime, 1, '.'));
		}
		return surfIntervalEl;
	}

	private Document createUdcf(List<Dive> dives, Diver owner) {
		Document doc = new Document();
		doc.setRootElement(createRootElement(dives));

		return doc;
	}

	private Element createRootElement(List<Dive> dives) {
		Element el = new Element("profile");
		el.setAttribute("udcf", UDCF_VERSION);
		el.addContent(createUnitsEl());
		el.addContent(createDeviceEl());

		for (Element regroup : createDiveProfileElements(dives)) {
			el.addContent(regroup);
		}

		return el;
	}

	private Element createDeviceEl() {
		Element el = new Element("device");

		Element sel = new Element("vendor");
		sel.setText("JtB");
		el.addContent(sel);

		sel = new Element("model");
		sel.setText("JtB Dive Logbook");
		el.addContent(sel);

		sel = new Element("version");
		sel.setText(Version.getCurrentVersion().toString());
		el.addContent(sel);

		return el;
	}

	/**
	 * Uses always metric units.
	 * 
	 * @return
	 */
	private Element createUnitsEl() {
		Element el = new Element("units");
		el.setText("metric");
		return el;
	}

	private Element createGasDefinitionEl(Map<Integer, DiveTankEquipment> tanks) {
		Element el = new Element("gases");
		Element mix = null;

		for (Integer id : tanks.keySet()) {
			mix = new Element("mix");

			Element mixname = new Element("mixname");
			mixname.setText(String.valueOf(id));
			mix.addContent(mixname);

			DiveTankEquipment dt = tanks.get(id);

			Element o2 = new Element("o2");
			double val = ((double) dt.getGazMix().getPercentage(Gaz.GAZ_OXYGEN)) / 100d;
			o2.setText(StringManipulator.formatFixedDecimalNumber(val, 3, '.'));
			mix.addContent(o2);

			Element n2 = new Element("n2");
			val = ((double) dt.getGazMix().getPercentage(Gaz.GAZ_NITROGEN)) / 100d;
			n2.setText(StringManipulator.formatFixedDecimalNumber(val, 3, '.'));
			mix.addContent(n2);

			Element he = new Element("he");
			val = ((double) dt.getGazMix().getPercentage(Gaz.GAZ_HELIUM)) / 100d;
			he.setText(StringManipulator.formatFixedDecimalNumber(val, 3, '.'));
			mix.addContent(he);

			Element tankEl = new Element("tank");
			if (dt.getMaterial() != null) {
				Element volEl = new Element("tankvolume");
				volEl.setText(String.valueOf(((DiveTankMaterial) dt
						.getMaterial()).getVolume()));
				tankEl.addContent(volEl);
			}

			Element pstartEl = new Element("pstart");
			pstartEl.setText(String.valueOf(dt.getBeginPressure()));
			tankEl.addContent(pstartEl);

			Element pendEl = new Element("pend");
			pendEl.setText(String.valueOf(dt.getEndPressure()));
			tankEl.addContent(pendEl);

			mix.addContent(tankEl);

			el.addContent(mix);
		}

		return el;

	}

	private List<Element> createDiveProfileElements(List<Dive> dives) {
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

		List<Element> regroups = new ArrayList<Element>();
		for (List<Dive> list : groups) {
			regroups.add(createRepetitionGroupElement(list));
		}
		return regroups;
	}

	private Element createRepetitionGroupElement(List<Dive> dives) {
		Element el = new Element("regroup");

		Dive previousDive = null;
		for (Dive dive : dives) {
			long seconds = 0;
			if (previousDive != null) {
				seconds = secondsBetweenDives(dive, previousDive);
			}

			el.addContent(createDiveEl(dive, seconds));
			previousDive = dive;
		}

		return el;
	}

	private Element createDiveEl(Dive dive, long seconds) {
		Element el = new Element("dive");

		if (dive.getDiveSite() != null)
			el.addContent(createPlaceElement(dive));

		el.addContent(createDiveDateEl(dive.getDate()));
		el.addContent(createTimeEl(dive.getDate()));

		el.addContent(createSurfaceTimeElement(seconds, dive));
		el.addContent(createWaterTemperatureElement(dive));

		el.addContent(createWaterDensity(dive));
		el.addContent(createAltitude(dive));

		Map<Integer, DiveTankEquipment> diveTanks = getDiveTanksMap(dive);
		el.addContent(createGasDefinitionEl(diveTanks));

		el.addContent(new Element("timedepthmode"));

		if (dive.getDiveProfile() != null) {
			el.addContent(createSamplesEl(dive.getDiveProfile(), diveTanks));
		} else {
			el.addContent(createBasicSamplesEl(dive));
		}
		return el;
	}

	private Map<Integer, DiveTankEquipment> getDiveTanksMap(Dive dive) {
		Map<Integer, DiveTankEquipment> map = new HashMap<Integer, DiveTankEquipment>();
		DiveEquipment eq = dive.getDiveEquipment();
		if (eq != null && eq.getDiveTanks() != null
				&& eq.getDiveTanks().size() > 0) {
			int counter = 1;
			for (DiveTankEquipment tank : eq.getDiveTanks()) {
				map.put(counter++, tank);
			}
		} else {
			DiveTankEquipment tank = new DiveTankEquipment();
			GazMix gm = new GazMix();
			gm.addGaz(Gaz.GAZ_NITROGEN, 79);
			gm.addGaz(Gaz.GAZ_OXYGEN, 21);
			tank.setGazMix(gm);
			map.put(1, tank);
		}
		return map;
	}

	private Element createBasicSamplesEl(Dive dive) {
		int timeSec = dive.getDiveTime();

		Element el = new Element("samples");

		Element switchEl = new Element("switch");
		switchEl.setText("1");
		el.addContent(switchEl);

		// Time 0
		Element divetime = new Element("t");
		divetime.setText("0");
		el.addContent(divetime);

		Element depth = new Element("d");
		depth.setText("0");
		el.addContent(depth);

		// 1/2 time of dive : set Max depth
		divetime = new Element("t");
		divetime.setText(String.valueOf(timeSec / 2));
		el.addContent(divetime);

		depth = new Element("d");
		depth.setText(String.valueOf(String.valueOf(Math.abs(dive.getMaxDepth()))));
		el.addContent(depth);

		// End of dive set max time at depth 0
		divetime = new Element("t");
		divetime.setText(String.valueOf(timeSec));
		el.addContent(divetime);

		depth = new Element("d");
		depth.setText("0");
		el.addContent(depth);

		return el;
	}

	private Element createAltitude(Dive dive) {
		Element el = new Element("altitude");
		if (dive.getDiveSite() == null)
			el.setText("0.0");
		else
			el.setText(StringManipulator.formatFixedDecimalNumber(dive
					.getDiveSite().getAltitude(), 1, '.'));
		return el;
	}

	private Element createWaterDensity(Dive dive) {
		Element el = new Element("density");
		el.setText("1000.0");
		return el;
	}

	private Element createWaterTemperatureElement(Dive dive) {
		Element el = new Element("temperature");
		el.setText(StringManipulator.formatFixedDecimalNumber(
				dive.getWaterTemperature(), 1, '.'));
		return el;
	}

	private Element createPlaceElement(Dive dive) {
		Element el = new Element("place");
		el.setText(dive.getDiveSite().getName());
		return el;
	}

	private Element createSamplesEl(DiveProfile diveProfile,
			Map<Integer, DiveTankEquipment> gazId) {
		Element el = new Element("samples");

		for (Integer inte : gazId.keySet()) {
			Element switchEl = new Element("switch");
			switchEl.setText(String.valueOf(inte));
			el.addContent(switchEl);
		}

		List<Double> times = new ArrayList<Double>(diveProfile
				.getDepthEntries().keySet());
		Collections.sort(times);

		for (Double second : times) {
			Element divetime = new Element("t");
			divetime.setText(StringManipulator.formatFixedDecimalNumber(second, 1,
					'.'));
			el.addContent(divetime);

			Element depth = new Element("d");
			depth.setText(StringManipulator.formatFixedDecimalNumber(
					Math.abs(diveProfile.getDepthEntries().get(second)), 1, '.'));
			el.addContent(depth);
		}

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
		month.setText(String.valueOf(cal.get(Calendar.MONTH) + 1));
		el.addContent(month);

		Element day = new Element("day");
		day.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		el.addContent(day);

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
		String units = root.getChild("units").getValue();

		String s = root.getChild("device").getChild("model").getValue();
		LogBook lb = new LogBook();
		lb.setName(s);
		lb.setDives(createDives(root));
		return lb;
	}

	private List<Dive> createDives(Element rootEl) {
		List<Dive> dives = new ArrayList<Dive>();
		Map<String, DiveSite> diveSites = new HashMap<String, DiveSite>();

		for (Iterator itReg = rootEl.getChildren("regroup").iterator(); itReg
				.hasNext();) {
			Element regroupEl = (Element) itReg.next();

			for (Iterator iterator = regroupEl.getChildren("dive").iterator(); iterator
					.hasNext();) {
				Element diveEl = (Element) iterator.next();
				Dive dive = new Dive();

				Map<String, DiveTankEquipment> diveTanks = getDiveMixes(diveEl);

				addDate(dives, diveEl, dive);
				addDiveSite(diveSites, diveEl, dive);
				addDiveDepthAndTime(diveEl, dive);
				addDiveWaterTemperature(diveEl, dive);
				addDiveSurfaceInterval(diveEl, dive);
				addProfileAndTanks(diveEl, dive, diveTanks);
			}
		}
		return dives;
	}

	private void addProfileAndTanks(Element diveEl, Dive dive,
			Map<String, DiveTankEquipment> diveTanks) {

		Map<Double, Double> depths = new HashMap<Double, Double>();

		Element samplesEl = diveEl.getChild("samples");
		if (diveEl.getChild("timedepthmode") != null) {
			for (Iterator iterator = samplesEl.getChildren().iterator(); iterator
					.hasNext();) {
				Element el = (Element) iterator.next();
				if (el.getName().equals("switch")) {
					addDiveTank(dive, el, diveTanks);
				} else if (el.getName().equals("t")) {
					double time = Double.parseDouble(el.getText());
					el = (Element) iterator.next();
					if (el.getName().equals("d")) {
						depths.put(time,
								-(Math.abs(Double.parseDouble(el.getText()))));
					}
				}
			}

		} else if (diveEl.getChild("deltamode") != null) {
			double delta = Double.parseDouble(samplesEl.getChildText("delta"));
			double timeCounter = 0;
			for (Iterator iterator = samplesEl.getChildren().iterator(); iterator
					.hasNext();) {
				Element el = (Element) iterator.next();
				if (el.getName().equals("switch")) {
					addDiveTank(dive, el, diveTanks);
				} else if (el.getName().equals("d")) {
					depths.put(timeCounter,
							-(Math.abs(Double.parseDouble(el.getText()))));
				}
			}
		}

		if (depths.size() > 3) {
			DiveProfile profile = new DiveProfile();
			profile.setDepthEntries(depths);
			dive.setDiveProfile(profile);
		} else {
			LOGGER.info("No profile has been added to the dive "
					+ dive.getDate()
					+ ". At least 4 entries are required to create a dive profile");
		}
	}

	private void addDiveTank(Dive dive, Element el,
			Map<String, DiveTankEquipment> gazmixes) {
		String ref = el.getValue();
		DiveTankEquipment dt = gazmixes.get(ref);

		if (dive.getDiveEquipment() == null) {
			dive.setDiveEquipment(new DiveEquipment());
		}
		dive.getDiveEquipment().addEquipment(dt);
	}

	private Map<String, DiveTankEquipment> getDiveMixes(Element diveEl) {
		Map<String, DiveTankEquipment> mixes = new HashMap<String, DiveTankEquipment>();
		Element gazesEl = diveEl.getChild("gases");
		if (gazesEl != null) {
			for (Iterator iterator = gazesEl.getChildren("mix").iterator(); iterator
					.hasNext();) {
				Element mixEl = (Element) iterator.next();

				GazMix mix = new GazMix();
				mix.addGaz(
						Gaz.GAZ_OXYGEN,
						(int) (Double.parseDouble(mixEl.getChildText("o2")) * 100));
				mix.addGaz(
						Gaz.GAZ_NITROGEN,
						(int) (Double.parseDouble(mixEl.getChildText("n2")) * 100));
				mix.addGaz(
						Gaz.GAZ_HELIUM,
						(int) (Double.parseDouble(mixEl.getChildText("he")) * 100));
				DiveTankEquipment dt = new DiveTankEquipment();
				dt.setGazMix(mix);

				Element tankEl = mixEl.getChild("tank");
				if (tankEl != null) {

					Element pStartEl = tankEl.getChild("pstart");
					if (pStartEl != null) {
						dt.setBeginPressure(Double.valueOf(pStartEl.getText()));
					}

					Element pEndEl = tankEl.getChild("pend");
					if (pEndEl != null) {
						dt.setEndPressure(Double.valueOf(pEndEl.getText()));
					}

					Element volEl = tankEl.getChild("tankvolume");
					if (volEl != null) {
						dt.setComment("Tank volume : " + volEl.getText() + " L");
					}
				}

				mixes.put(mixEl.getChildText("mixname"), dt);
			}
		}
		return mixes;
	}

	private void addDiveWaterTemperature(Element diveEl, Dive dive) {
		Element el = diveEl.getChild("temperature");
		dive.setWaterTemperature(Double.parseDouble(el.getValue()));
	}

	private void addDiveSurfaceInterval(Element diveEl, Dive dive) {
		Element el = diveEl.getChild("surfaceinterval");
		if (!el.getText().equals("infinity")) {
			// surfacetime in udcs is expressed in seconds while it must be in
			// minute in the model.
			int st = (int) (Double.parseDouble(el.getValue()));
			dive.setSurfaceTime(st);
		}
	}

	private void addDiveDepthAndTime(Element diveEl, Dive dive) {
		double maxDepth = 0;
		double maxTime = 0;
		Element samplesEl = diveEl.getChild("samples");
		if (diveEl.getChild("timedepthmode") != null) {
			for (Iterator iterator = samplesEl.getChildren("t").iterator(); iterator
					.hasNext();) {
				Element tEl = (Element) iterator.next();
				double testedTime = Double.parseDouble(tEl.getValue());
				if (testedTime > maxTime) {
					maxTime = testedTime;
				}
			}

			for (Iterator iterator = samplesEl.getChildren("d").iterator(); iterator
					.hasNext();) {
				Element tEl = (Element) iterator.next();
				double testedDepth = Double.parseDouble(tEl.getValue());
				if (testedDepth > maxDepth) {
					maxDepth = testedDepth;
				}
			}

		} else if (diveEl.getChild("deltamode") != null) {
			maxTime = samplesEl.getChildren("d").size()
					* (Double.parseDouble(samplesEl.getChildText("delta")));

			for (Iterator iterator = samplesEl.getChildren("d").iterator(); iterator
					.hasNext();) {
				Element tEl = (Element) iterator.next();
				double testedDepth = Double.parseDouble(tEl.getValue());
				if (testedDepth > maxDepth) {
					maxDepth = testedDepth;
				}
			}
		}

		dive.setDiveTime((int) maxTime);
		dive.setMaxDepth(-maxDepth);
	}

	private void addDiveSite(Map<String, DiveSite> diveSites, Element diveEl,
			Dive dive) {
		// DiveSites
		Element diveSiteEl = diveEl.getChild("place");
		if (diveSiteEl != null) {
			String diveSiteName = diveSiteEl.getText();
			if (diveSites.get(diveSiteName) == null) {
				DiveSite ds = new DiveSite();
				ds.setName(diveSiteName);
				ds.setAltitude(Double.parseDouble(diveEl
						.getChildText("altitude")));
				diveSites.put(diveSiteName, ds);
			}

			dive.setDiveSite(diveSites.get(diveSiteName));
		}
	}

	private void addDate(List<Dive> dives, Element diveEl, Dive dive) {
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

		dive.setDate(cal.getTime());

		dives.add(dive);
	}
}
