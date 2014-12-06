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
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.util.XsdValidator;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveEquipment;
import be.vds.jtbdive.core.core.DiveProfile;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.GazMix;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.Palanquee;
import be.vds.jtbdive.core.core.PalanqueeEntry;
import be.vds.jtbdive.core.core.catalogs.DiveTankCompositeMaterial;
import be.vds.jtbdive.core.core.catalogs.Gaz;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.catalogs.SuitType;
import be.vds.jtbdive.core.core.comparator.DiveDateComparator;
import be.vds.jtbdive.core.core.comparator.EquipmentIndexComparator;
import be.vds.jtbdive.core.core.material.DefaultMaterial;
import be.vds.jtbdive.core.core.material.DefaultSizeableMaterial;
import be.vds.jtbdive.core.core.material.DiveComputerMaterial;
import be.vds.jtbdive.core.core.material.DiveTankEquipment;
import be.vds.jtbdive.core.core.material.DiveTankMaterial;
import be.vds.jtbdive.core.core.material.Equipment;
import be.vds.jtbdive.core.core.material.FinsMaterial;
import be.vds.jtbdive.core.core.material.MaskMaterial;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.SuitMaterial;
import be.vds.jtbdive.core.core.units.VolumeUnit;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.XMLValidationException;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.core.utils.EquipmentUtilities;

/**
 * This parser is written to deal with UDDF standard V3.0.0. To see which tags
 * are filled, please refer to the write method.
 * 
 * @author gautier
 */
public abstract class UDDFV3xParser {

	private static final Syslog LOGGER = Syslog.getLogger(UDDFV3xParser.class);
	private static final long HOURS_24 = 24 * 60 * 60;
	private static final SimpleDateFormat FORMATTER_DATE = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss");
	private static final String NAMESPACE_UDDF = "http://www.streit.cc/uddf/3.0/";

	public abstract String getUDDFVersion();

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
			Element uddfEl = document.getRootElement();
			uddfEl.setNamespace(Namespace.getNamespace(NAMESPACE_UDDF));
			setNameSpaceToChildren(uddfEl, NAMESPACE_UDDF);
			boolean b = XsdValidator.validXML(document, xsdInputStream);
			if (!b) {
				throw new XMLValidationException(
						"The XML generated doesn't fulfill the XSD uddf.xsd");
			}
			uddfEl.setNamespace(null);
			setNameSpaceToChildren(uddfEl, null);
		}

		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		XMLOutputter outputter = new XMLOutputter(format);
		outputter.output(document, outputStream);
		outputStream.close();
	}

	private Document createUddfEl(List<Dive> dives, Diver owner) {
		Document doc = new Document();
		Map<Integer, GazMix> gMs = LogBookUtilities.getGazMixesUsed(dives);
		Map<String, GazMix> gazMixes = new HashMap<String, GazMix>();
		for (Integer id : gMs.keySet()) {
			gazMixes.put("gm_" + id, gMs.get(id));
		}
		List<Diver> divers = LogBookUtilities.getDivers(dives, owner);
		List<DiveSite> diveSites = LogBookUtilities.getDiveSites(dives);

		List<Material> materials = LogBookUtilities.getMaterials(dives);
		Map<Material, String> matMap = new HashMap<Material, String>();
		for (Material material : materials) {
			matMap.put(material, "material_" + material.getId());
		}

		doc.setRootElement(createRootElement(dives, gazMixes, divers, owner,
				diveSites, matMap));
		return doc;
	}

	private Element createRootElement(List<Dive> dives,
			Map<String, GazMix> gazMixes, List<Diver> divers, Diver owner,
			List<DiveSite> diveSites, Map<Material, String> materials) {
		Element uddfEl = new Element("uddf");
		uddfEl.setAttribute("version", getUDDFVersion());
		uddfEl.addContent(createTagGenerator());
		uddfEl.addContent(createTagGasDefinition(gazMixes));
		uddfEl.addContent(createTagDivers(divers, owner, materials));
		uddfEl.addContent(createTagDiveSite(diveSites));
		uddfEl.addContent(createTagDiveData(dives, gazMixes, materials));
		return uddfEl;
	}

	private void setNameSpaceToChildren(Element parentEl, String namespace) {
		for (Iterator iterator = parentEl.getChildren().iterator(); iterator
				.hasNext();) {
			Element childEl = (Element) iterator.next();
			childEl.setNamespace(Namespace.getNamespace(namespace));
			if (childEl.getChildren().size() > 0) {
				setNameSpaceToChildren(childEl, namespace);
			}
		}
	}

	protected Element createTagDiveSite(List<DiveSite> diveSites) {
		Element el = new Element("divesite");

		el.addContent(createTagDiveBases());

		for (DiveSite diveSite : diveSites) {
			Element diveSiteEl = createTagSite(diveSite);
			el.addContent(diveSiteEl);
		}
		return el;
	}

	protected Element createTagDiveBases() {
		Element dbel = new Element("divebase");
		dbel.setAttribute("id", "db_0");

		Element el = new Element("name");
		el.setText("Not implemented!!");
		dbel.addContent(el);

		return dbel;
	}

	protected Element createTagSite(DiveSite diveSite) {
		Element el = new Element("site");
		el.setAttribute("id", "site_" + diveSite.getId());

		// name
		Element nameEl = new Element("name");
		nameEl.setText(diveSite.getName());
		el.addContent(nameEl);

		// geography
		Element geographyEl = new Element("geography");
		el.addContent(geographyEl);

		Element locationEl = new Element("location");
		locationEl.setText(String.valueOf(diveSite.getName()));
		geographyEl.addContent(locationEl);

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

	protected Element createTagDivers(List<Diver> divers, Diver owner,
			Map<Material, String> materials) {
		Element el = new Element("diver");
		Element buddyEl = null;
		if (owner == null) {
			owner = new Diver();
			owner.setFirstName("dummy");
			owner.setLastName("owner");
		}
		buddyEl = createTagOwner(owner, materials);
		el.addContent(buddyEl);

		for (Diver diver : divers) {
			if (owner == null || !diver.equals(owner)) {
				buddyEl = createTagBuddy(diver);
				el.addContent(buddyEl);
			}
		}
		return el;
	}

	protected Element createTagOwner(Diver diver,
			Map<Material, String> materials) {

		Element buddyEl = new Element("owner");
		buddyEl.setAttribute("id", "buddy_" + String.valueOf(diver.getId()));

		Element personalEl = new Element("personal");
		buddyEl.addContent(personalEl);

		Element fnEl = new Element("firstname");
		fnEl.setText(diver.getFirstName());
		personalEl.addContent(fnEl);

		Element lnEl = new Element("lastname");
		lnEl.setText(diver.getLastName());
		personalEl.addContent(lnEl);

		if (materials != null && materials.size() > 0) {
			buddyEl.addContent(createTagEquipment(materials));
		}

		return buddyEl;
	}

	private Element createTagEquipment(Map<Material, String> materials) {
		Element equipmentsEl = new Element("equipment");
		Element equipmentEl = null;

		List<Material> coll = new ArrayList<Material>(materials.keySet());
		Collections.sort(coll, new UDDFMaterialComparator());

		for (Material material : coll) {
			switch (material.getMaterialType()) {
			case BOOTS:
				equipmentEl = createTagBootsElement(material,
						materials.get(material));
				break;
			case BUOYANCY_COMPENSATOR:
				equipmentEl = createTagBuoyancyCompensator(material,
						materials.get(material));
				break;
			case COMPASS:
				equipmentEl = createTagCompass(material,
						materials.get(material));
				break;
			case DIVE_COMPUTER:
				equipmentEl = createTagDiveComputer(
						(DiveComputerMaterial) material,
						materials.get(material));
				break;
			case FINS:
				equipmentEl = createTagFins(material, materials.get(material));
				break;
			case GLOVES:
				equipmentEl = createTagGloves(material, materials.get(material));
				break;
			case KNIFE:
				equipmentEl = createTagknife(material, materials.get(material));
				break;
			case LIGHT:
				equipmentEl = createTagLight(material, materials.get(material));
				break;
			case SUIT:
				equipmentEl = createTagSuit((SuitMaterial) material,
						materials.get(material));
				break;
			case MASK:
				equipmentEl = createTagMask(material, materials.get(material));
				break;
			case REBREATHER:
				equipmentEl = createTagRebreather(material,
						materials.get(material));
				break;
			case REGULATOR:
				equipmentEl = createTagRegulator(material,
						materials.get(material));
				break;
			case SCOOTER:
				equipmentEl = createTagScooter(material,
						materials.get(material));
				break;
			case WATCH:
				equipmentEl = createTagWatch(material, materials.get(material));
				break;
			case CAMERA:
				equipmentEl = createTagCamera(material, materials.get(material));
				break;
			case VIDEOCAMERA:
				equipmentEl = createTagVideoCamera(material,
						materials.get(material));
				break;
			case DIVE_TANK:
				equipmentEl = createTagDiveTank((DiveTankMaterial) material,
						materials.get(material));
				break;
			case LEAD:
				equipmentEl = createTagLead((DiveTankMaterial) material,
						materials.get(material));
				break;
			default:
				equipmentEl = createTagVariousPieces(material,
						materials.get(material));
				break;
			}
			equipmentsEl.addContent(equipmentEl);
		}
		return equipmentsEl;
	}

	protected Element createTagDiveTank(DiveTankMaterial material, String id) {
		Element el = new Element("tank");
		fillCommonEquipmentTag(el, material, id);

		if (null != material.getComposite()) {
			el.addContent(new Element("tankmaterial").setText(material
					.getComposite().getKey()));
		} else {
			el.addContent(new Element("tankmaterial")
					.setText(DiveTankCompositeMaterial.STEEL.getKey()));
		}

		return el;
	}

	private Element createTagVariousPieces(Material material, String id) {
		Element el = new Element("variouspieces");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	private Element createTagScooter(Material material, String id) {
		Element el = new Element("scooter");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	private Element createTagCamera(Material material, String id) {
		Element el = new Element("camera");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	private Element createTagVideoCamera(Material material, String id) {
		Element el = new Element("videocamera");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	private Element createTagWatch(Material material, String id) {
		Element el = new Element("watch");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	private Element createTagLead(Material material, String id) {
		Element el = new Element("lead");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	private Element createTagMask(Material material, String id) {
		Element el = new Element("mask");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	private Element createTagRebreather(Material material, String id) {
		Element el = new Element("rebreather");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	private Element createTagRegulator(Material material, String id) {
		Element el = new Element("regulator");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	private Element createTagknife(Material material, String id) {
		Element el = new Element("knife");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	private Element createTagLight(Material material, String id) {
		Element el = new Element("light");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	private Element createTagFins(Material material, String id) {
		Element el = new Element("fins");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	private Element createTagGloves(Material material, String id) {
		Element el = new Element("gloves");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	private Element createTagSuit(SuitMaterial material, String id) {
		Element el = new Element("suit");
		fillCommonEquipmentTag(el, material, id);
		Element suitType = new Element("suittype");
		SuitType type = material.getSuitType();

		switch (type) {
		case DRY:
			suitType.setText("dry-suit");
			break;
		case HALF_DRY:
		case WET:
			suitType.setText("wet-suit");
			break;
		}

		return el;
	}

	private Element createTagDiveComputer(DiveComputerMaterial material,
			String id) {
		Element el = new Element("divecomputer");
		fillCommonEquipmentTag(el, material, id);

		// serialNumber
		int indexOfModel = el.getChild("model") == null ? -1 : el.indexOf(el
				.getChild("model"));
		if (indexOfModel == -1) {
			indexOfModel = el.getChild("manufacturer") == null ? -1 : el
					.indexOf(el.getChild("manufacturer"));
		}
		el.addContent(indexOfModel + 1,
				new Element("serialnumber").setText(material.getSerialNumber()));
		return el;
	}

	private Element createTagCompass(Material material, String id) {
		Element el = new Element("compass");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	private Element createTagBuoyancyCompensator(Material material, String id) {
		Element el = new Element("buoyancycontroldevice");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	private Element createTagBootsElement(Material material, String id) {
		Element el = new Element("boots");
		fillCommonEquipmentTag(el, material, id);
		return el;
	}

	protected void fillCommonEquipmentTag(Element materialEl,
			Material material, String id) {
		materialEl.setAttribute("id", id);
		materialEl.addContent(new Element("name").setText(material
				.getMaterialType().getKey() + "_" + material.getId()));

		// manufacturer
		if (material.getManufacturer() != null
				&& material.getManufacturer().length() > 0) {
			Element manufacturer = new Element("manufacturer");
			manufacturer.setAttribute("id",
					"manufacturer_" + material.getManufacturer());
			Element nameEl = new Element("name");
			nameEl.setText(material.getManufacturer());
			manufacturer.addContent(nameEl);
			materialEl.addContent(manufacturer);
		}

		// model
		Element modelEl = new Element("model").setText(material.getModel());
		materialEl.addContent(modelEl);

		// purchase
		Element purchase = new Element("purchase");
		Element price = new Element("price");
		price.setAttribute("currency", "EUR");
		// TODO:PROBLEM WITH XSD!!!!!!!!
		purchase.addContent(price);
		if (null != material.getPurchaseDate()) {
			purchase.addContent(createTagDiveDate(material.getPurchaseDate()));
		}
		materialEl.addContent(purchase);

		// comment
		if (material.getComment() != null && material.getComment().length() > 0) {
			Element paraEl = new Element("para");
			paraEl.setText(material.getComment());
			Element notesEl = new Element("notes");
			notesEl.addContent(paraEl);
			materialEl.addContent(notesEl);
		}

	}

	protected Element createTagBuddy(Diver diver) {

		Element buddyEl = new Element("buddy");
		buddyEl.setAttribute("id", "buddy_" + String.valueOf(diver.getId()));

		Element personalEl = new Element("personal");
		buddyEl.addContent(personalEl);

		Element fnEl = new Element("firstname");
		fnEl.setText(diver.getFirstName());
		personalEl.addContent(fnEl);

		Element lnEl = new Element("lastname");
		lnEl.setText(diver.getLastName());
		personalEl.addContent(lnEl);

		return buddyEl;
	}

	protected Element createTagGasDefinition(Map<String, GazMix> gazMixes) {
		Element el = new Element("gasdefinitions");
		for (String id : gazMixes.keySet()) {
			Element mix = new Element("mix");
			mix.setAttribute("id", id);

			Element mixnameEl = new Element("name");
			mixnameEl.setText(id);
			mix.addContent(mixnameEl);

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

	private Element createTagDiveData(List<Dive> dives,
			Map<String, GazMix> gazMixes, Map<Material, String> matMap) {
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
		int i = 1;
		for (List<Dive> list : groups) {
			profiledataEl.addContent(createTagRepetitionGroup(list, gazMixes,
					i++, matMap));
		}
		return profiledataEl;
	}

	private Element createTagRepetitionGroup(List<Dive> dives,
			Map<String, GazMix> gazMixes, int repetitionGroupId,
			Map<Material, String> matMap) {
		Element el = new Element("repetitiongroup");
		el.setAttribute("id", "rg_" + repetitionGroupId);
		Dive previousDive = null;
		for (Dive dive : dives) {
			long seconds = 0;
			if (previousDive != null) {
				seconds = secondsBetweenDives(dive, previousDive);
			}

			el.addContent(createTagDive(dive, seconds, gazMixes, matMap));
			previousDive = dive;
		}
		return el;
	}

	protected Element createTagDive(Dive dive, long seconds,
			Map<String, GazMix> gazMixes, Map<Material, String> matMap) {
		Element el = new Element("dive");
		el.setAttribute("id", "dive_" + dive.getId());

		el.addContent(createTagInformationBeforeDive(dive, seconds));

		if (dive.getDiveProfile() != null) {
			el.addContent(createTagSamples(dive.getDiveProfile(),
					matchUsedGaz(dive, gazMixes)));
		}

		el.addContent(createTagInformationAfterDive(dive, matMap));

		return el;
	}

	protected String matchUsedGaz(Dive dive, Map<String, GazMix> gazMixes) {
		if (dive.getDiveEquipment() != null
				&& dive.getDiveEquipment().getDiveTanks() != null
				&& dive.getDiveEquipment().getDiveTanks().size() > 0) {
			List<DiveTankEquipment> tanks = dive.getDiveEquipment()
					.getDiveTanks();
			Collections.sort(tanks, new EquipmentIndexComparator());
			GazMix gazMix = tanks.get(0).getGazMix();
			if (gazMix != null) {
				for (String id : gazMixes.keySet()) {
					if (gazMix.hasSameComposition(gazMixes.get(id))) {
						return String.valueOf(id);
					}
				}
			}
		}

		return null;
	}

	protected Element createTagInformationBeforeDive(Dive dive, long seconds) {
		Element el = new Element("informationbeforedive");

		if (dive.getDiveSite() != null) {
			el.addContent(createTagDiveSiteLink(dive.getDiveSite()));
		}

		if (dive.getPalanquee() != null) {
			for (PalanqueeEntry entry : dive.getPalanquee()
					.getPalanqueeEntries()) {
				el.addContent(createTagPalanqueeEntryLink(entry));
			}
		}

		el.addContent(createTagDiveNumber(dive));
		el.addContent(createTagDiveDate(dive.getDate()));

		Element surfIntervalEl = new Element("surfaceintervalbeforedive");
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
		el.addContent(surfIntervalEl);

		return el;
	}

	private Element createTagPalanqueeEntryLink(PalanqueeEntry entry) {
		Element el = new Element("link");
		el.setAttribute("ref", "buddy_" + entry.getDiver().getId());
		return el;
	}

	private Element createTagDiveSiteLink(DiveSite diveSite) {
		Element el = new Element("link");
		el.setAttribute("ref", "site_" + diveSite.getId());
		return el;
	}

	protected Element createTagSamples(DiveProfile diveProfile, String gazId) {
		Element el = new Element("samples");

		List<Double> times = new ArrayList<Double>(diveProfile
				.getDepthEntries().keySet());
		Collections.sort(times);

		for (Double second : times) {
			Element wp = new Element("waypoint");
			el.addContent(wp);

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
			Element depth = new Element("depth");
			depth.setText(String.valueOf(Math.abs(diveProfile.getDepthEntries()
					.get(second))));
			wp.addContent(depth);

			Element divetime = new Element("divetime");
			divetime.setText(String.valueOf(second));
			wp.addContent(divetime);

		}

		return el;
	}

	private Element createTagObservations(Dive dive) {
		Element el = new Element("observations");
		boolean isObserveation = false;

		String comment = dive.getComment();
		if (comment != null && comment.length() > 0) {
			Element note = new Element("notes");
			Element paraEl = new Element("para");
			paraEl.setText(comment);
			note.addContent(paraEl);
			el.addContent(note);
			isObserveation = true;
		}

		if (isObserveation)
			return el;
		else
			return null;
	}

	private Element createTagDiveNumber(Dive dive) {
		Element el = new Element("divenumber");
		el.setText(String.valueOf(dive.getNumber()));
		return el;
	}

	protected Element createTagInformationAfterDive(Dive dive,
			Map<Material, String> matMap) {
		Element el = new Element("informationafterdive");

		Element depthEl = new Element("greatestdepth");
		depthEl.setText(String.valueOf(Math.abs(dive.getMaxDepth())));
		el.addContent(depthEl);

		Element divedurationEl = new Element("diveduration");
		divedurationEl.setText(String.valueOf(dive.getDiveTime()));
		el.addContent(divedurationEl);

		Element obsEl = createTagObservations(dive);
		if (obsEl != null) {
			el.addContent(obsEl);
		}

		if (dive.getDiveEquipment() != null) {
			Element equipmentUsedEl = null;
			for (Equipment equipment : dive.getDiveEquipment()
					.getAllEquipments()) {

				if (null != equipment.getMaterial()) {
					if (null == equipmentUsedEl) {
						equipmentUsedEl = new Element("equipmentused");
					}
					String key = matMap.get(equipment.getMaterial());
					if (null != key) {
						Element linkEl = new Element("link");
						linkEl.setAttribute("ref", key);
						equipmentUsedEl.addContent(linkEl);
					}
				}
			}
			if (null != equipmentUsedEl) {
				el.addContent(equipmentUsedEl);
			}
		}

		return el;

	}

	private Element createTagDiveDate(Date diveDate) {
		Element el = new Element("datetime");
		el.setText(FORMATTER_DATE.format(diveDate));
		return el;
	}

	protected Element createTagGenerator() {
		Element el = new Element("generator");
		el.addContent(new Element("name").setText("JtbDiveLogbook-"
				+ getUDDFVersion()));
		el.addContent(new Element("type").setText("logbook"));

		return el;
	}

	private long secondsBetweenDives(Dive dive, Dive previousDive) {
		return (dive.getDate().getTime() - previousDive.getDate().getTime()) / 1000;
	}

	public LogBook read(InputStream inputStream) throws DataStoreException,
			XMLValidationException {
		return read(inputStream, null);
	}

	public LogBook read(InputStream inputStream, InputStream xsdInputStream)
			throws DataStoreException, XMLValidationException {
		try {
			SAXBuilder sb = new SAXBuilder();
			Document document = sb.build(inputStream);

			if (xsdInputStream != null) {
				Element uddfEl = document.getRootElement();
				uddfEl.setNamespace(Namespace.getNamespace(NAMESPACE_UDDF));
				setNameSpaceToChildren(uddfEl, NAMESPACE_UDDF);

				boolean b = XsdValidator.validXML(document, xsdInputStream);
				if (!b) {
					throw new XMLValidationException(
							"The XML generated doesn't fulfill the XSD uddf.xsd");
				}
				uddfEl.setNamespace(null);
				setNameSpaceToChildren(uddfEl, null);
			}

			Element root = document.getRootElement();
			LogBook lb = readLogBook(root);
			return lb;
		} catch (JDOMException e) {
			throw new DataStoreException(e);
		} catch (IOException e) {
			throw new DataStoreException(e);
		}
	}

	private LogBook readLogBook(Element root) {
		Map<String, Diver> divers = readDivers(root);
		Map<String, DiveSite> diveSites = readDiveSites(root);
		Map<String, Material> materials = readMaterials(root);
		Map<String, GazMix> gazMixes = readGazmixes(root);

		LogBook lb = new LogBook();
		lb.setName(root.getChild("generator").getChildText("name"));

		Element diversEl = root.getChild("diver");
		if (diversEl != null) {
			Element ownerEl = diversEl.getChild("owner");
			if (ownerEl != null) {
				lb.setOwner(divers.get(ownerEl.getAttributeValue("id")));
			}
		}

		lb.setDives(readDives(root, divers, diveSites, materials, gazMixes));
		return lb;
	}

	private Map<String, GazMix> readGazmixes(Element root) {
		Map<String, GazMix> mixes = new HashMap<String, GazMix>();
		Element el = root.getChild("gasdefinitions");
		if (el == null)
			return mixes;

		List<Element> mixesEl = el.getChildren("mix");
		for (Element mixEl : mixesEl) {
			GazMix mix = new GazMix();
			addGaz(mixEl, mix, "o2", Gaz.GAZ_OXYGEN);
			addGaz(mixEl, mix, "n2", Gaz.GAZ_NITROGEN);
			addGaz(mixEl, mix, "he", Gaz.GAZ_HELIUM);
			addGaz(mixEl, mix, "ar", Gaz.GAZ_ARGON);
			addGaz(mixEl, mix, "h2", Gaz.GAZ_HYDROGEN);

			mixes.put(mixEl.getAttributeValue("id"), mix);
		}

		return mixes;
	}

	private void addGaz(Element mixEl, GazMix mix, String tagName, Gaz gaz) {
		String val = mixEl.getChildText(tagName);
		if (null != val){
			mix.addGaz(gaz, Integer.parseInt(val));
		}
	}

	private Map<String, Material> readMaterials(Element root) {
		Element el = root.getChild("diver");
		if (el != null) {
			el = el.getChild("owner");
		}
		if (el != null) {
			el = el.getChild("equipment");
		}

		if (el == null) {
			return null;
		}

		Map<String, Material> matMap = new HashMap<String, Material>();
		for (Iterator iterator = el.getChildren().iterator(); iterator
				.hasNext();) {
			Element eqEl = (Element) iterator.next();
			addMaterialToMap(matMap, eqEl);
		}
		return matMap;
	}

	private void addMaterialToMap(Map<String, Material> matMap,
			Element equipmentEl) {
		String tagName = equipmentEl.getName();
		String id = equipmentEl.getAttributeValue("id");
		Material m = null;
		if ("boots".equals(tagName)) {
			m = readBoots();
		} else if ("boyancycompensator".equals(tagName)) {
			m = readBoyancyCompensator();
		} else if ("camera".equals(tagName)) {
			m = readCameraMat();
		} else if ("compass".equals(tagName)) {
			m = readCompassMat();
		} else if ("divecomputer".equals(tagName)) {
			m = readDiveComputer(equipmentEl);
		} else if ("fins".equals(tagName)) {
			m = readFinsMat();
		} else if ("gloves".equals(tagName)) {
			m = readGloves();
		} else if ("knife".equals(tagName)) {
			m = readKnifeMat();
		} else if ("lead".equals(tagName)) {
			m = readLeadMat();
		} else if ("light".equals(tagName)) {
			m = readLightMat();
		} else if ("mask".equals(tagName)) {
			m = readMaskMat();
		} else if ("rebreather".equals(tagName)) {
			m = readRebreatherMat();
		} else if ("regulator".equals(tagName)) {
			m = readRegulatorMat();
		} else if ("scooter".equals(tagName)) {
			m = readScooter();
		} else if ("suit".equals(tagName)) {
			m = readSuitMat();
		} else if ("tank".equals(tagName)) {
			m = readDiveTankMat(equipmentEl);
		} else if ("videocamera".equals(tagName)) {
			m = readVideoCameraMat();
		} else if ("watch".equals(tagName)) {
			m = readWatch();
		}

		if (m == null) {
			LOGGER.warn("Type of material with id " + id
					+ " unknown. Consider it as various piece");
			m = new DefaultMaterial(MaterialType.OTHER);
		}

		fillCommonUDDFElements(m, equipmentEl);
		matMap.put(id, m);
	}

	private Material readCompassMat() {
		return new DefaultMaterial(MaterialType.COMPASS);
	}

	private Material readVideoCameraMat() {
		return new DefaultMaterial(MaterialType.VIDEOCAMERA);
	}

	private Material readRebreatherMat() {
		return new DefaultMaterial(MaterialType.REBREATHER);
	}

	private Material readSuitMat() {
		return new SuitMaterial();
	}

	private Material readRegulatorMat() {
		return new DefaultMaterial(MaterialType.REGULATOR);
	}

	private Material readKnifeMat() {
		return new DefaultMaterial(MaterialType.KNIFE);
	}

	private Material readLeadMat() {
		return new DefaultMaterial(MaterialType.LEAD);
	}

	private Material readDiveTankMat(Element equipmentEl) {
		DiveTankMaterial dt = new DiveTankMaterial();
		String composite = equipmentEl.getChildText("tankmaterial");
		if (null != composite) {
			dt.setComposite(DiveTankCompositeMaterial
					.getDiveTankCompositeMaterialByKey(composite));
		}

		String volume = equipmentEl.getChildText("tankvolume");
		if (null != volume) {
			dt.setVolume(UnitsAgent.getInstance().convertVolumeToModel(
					Double.parseDouble(volume), VolumeUnit.CUBIC_METER));
		}
		return dt;
	}

	private Material readGloves() {
		return new DefaultSizeableMaterial(MaterialType.GLOVES);
	}

	private Material readWatch() {
		return new DefaultMaterial(MaterialType.WATCH);
	}

	private Material readFinsMat() {
		return new FinsMaterial();
	}

	private Material readCameraMat() {
		return new DefaultMaterial(MaterialType.CAMERA);
	}

	private Material readDiveComputer(Element equipmentEl) {
		DiveComputerMaterial dc = new DiveComputerMaterial();

		if (equipmentEl.getChild("serialnumber") != null) {
			dc.setSerialNumber(equipmentEl.getChildText("serialnumber"));
		}

		return dc;
	}

	private Material readBoots() {
		return new DefaultSizeableMaterial(MaterialType.BOOTS);
	}

	private Material readBoyancyCompensator() {
		return new DefaultSizeableMaterial(MaterialType.BUOYANCY_COMPENSATOR);
	}

	private Material readScooter() {
		return new DefaultMaterial(MaterialType.SCOOTER);
	}

	private Material readMaskMat() {
		return new MaskMaterial();
	}

	private Material readLightMat() {
		return new DefaultMaterial(MaterialType.LIGHT);
	}

	private void fillCommonUDDFElements(Material m, Element equipmentEl) {
		Element name = equipmentEl.getChild("manufacturer");
		if (name != null) {
			name = name.getChild("name");
			if (name != null) {
				m.setManufacturer(name.getText());
			}
		}

		if (equipmentEl.getChild("model") != null) {
			m.setModel(equipmentEl.getChildText("model"));
		}

		Element price = equipmentEl.getChild("purchase");
		if (price != null) {
			price = price.getChild("price");
			if (price != null) {
				String priceStr = price.getText();
				if (null != priceStr && priceStr.length() > 0){
					m.setPurchasePrice(Double.parseDouble(priceStr));
				}
			}
		}
	}

	private List<Dive> readDives(Element root, Map<String, Diver> divers,
			Map<String, DiveSite> diveSites, Map<String, Material> materials,
			Map<String, GazMix> gazMixes) {
		List<Dive> dives = new ArrayList<Dive>();
		Element profileEl = root.getChild("profiledata");
		Dive dive = null;
		for (Iterator iterator = profileEl.getChildren("repetitiongroup")
				.iterator(); iterator.hasNext();) {
			Element repEl = (Element) iterator.next();

			for (Iterator iterator2 = repEl.getChildren("dive").iterator(); iterator2
					.hasNext();) {
				Element diveEl = (Element) iterator2.next();

				dive = new Dive();

				readInformationBeforeDive(dive, diveEl, diveSites, divers);
				dive.setDiveProfile(readDiveProfile(diveEl));
				setInformationAfterDive(dive, diveEl, divers, materials);

				dives.add(dive);
			}
		}
		return dives;
	}

	protected void setInformationAfterDive(Dive dive, Element diveEl,
			Map<String, Diver> divers, Map<String, Material> materials) {
		Element informationAferDiveEl = diveEl.getChild("informationafterdive");
		dive.setSurfaceTime(readSurfaceTime(informationAferDiveEl));
		dive.setComment(readComment(informationAferDiveEl));

		if (informationAferDiveEl.getChild("equipmentused") != null) {
			for (Iterator iterator = informationAferDiveEl
					.getChild("equipmentused").getChildren("link").iterator(); iterator
					.hasNext();) {
				Element linkEl = (Element) iterator.next();
				String key = linkEl.getAttributeValue("ref");
				if (null != key) {
					Material m = materials.get(key);
					if (dive.getDiveEquipment() == null) {
						dive.setDiveEquipment(new DiveEquipment());
					}
					dive.getDiveEquipment().addEquipment(
							EquipmentUtilities.createEquipmentForMaterial(m));
				}
			}
		}

	}

	protected void readInformationBeforeDive(Dive dive, Element diveEl,
			Map<String, DiveSite> diveSites, Map<String, Diver> divers) {
		Element el = diveEl.getChild("informationbeforedive");
		if (el == null) {
			return;
		}

		dive.setDate(readDate(el));
		dive.setNumber(readDiveNumber(el));

		int diveInterval = 0;
		Element surfIntervalEl = el.getChild("surfaceintervalbeforedive");
		if (null != surfIntervalEl) {
			if (surfIntervalEl.getChild("passedtime") != null) {
				diveInterval = Integer.parseInt(surfIntervalEl
						.getChildText("passedtime")) / 60;
			}
		}
		dive.setSurfaceTime(diveInterval);

		DiveSite site = null;
		List<Diver> buddies = new ArrayList<Diver>();
		for (Iterator iterator = el.getChildren("link").iterator(); iterator
				.hasNext();) {
			Element linkEl = (Element) iterator.next();

			String val = linkEl.getAttributeValue("ref");

			site = diveSites.get(val);
			if (site == null) {
				Diver d = divers.get(val);
				if (d != null) {
					buddies.add(d);
				}
			} else {
				dive.setDiveSite(site);
			}
		}

		if (buddies.size() > 0) {
			Palanquee p = new Palanquee();
			for (Diver diver : buddies) {
				PalanqueeEntry pe = new PalanqueeEntry(diver,
						PalanqueeEntry.getBasicRole());
				p.addPalanqueeEntry(pe);
			}
			dive.setPalanquee(p);
		}

	}

	private String readComment(Element informationAferDiveEl) {
		Element el = informationAferDiveEl.getChild("observations");
		if (null != el) {
			el = el.getChild("notes");
			if (el != null) {
				el = el.getChild("para");
				if (el != null) {
					return el.getText();
				}
			}
		}
		return null;
	}

	protected DiveProfile readDiveProfile(Element diveEl) {
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

		if (depths.size() == 0){
			return null;
		}

		DiveProfile dp = new DiveProfile();
		dp.setDepthEntries(depths);
		dp.setAscentWarnings(ascent);
		dp.setDecoCeilingWarnings(decoCeilingWarnings);
		dp.setDecoEntries(decoEntries);
		dp.setRemainingBottomTimeWarnings(remainingBottomTimeWarnings);

		return dp;
	}

	/**
	 * gets the number of seconds of surface time
	 * 
	 * @param informationAfterDiveEl
	 * @return
	 */
	private int readSurfaceTime(Element informationAfterDiveEl) {
		int res = 0;
		Element el = informationAfterDiveEl.getChild("surfaceinterval");
		if (el != null) {
			if ((el = el.getChild("passedtime")) != null) {
				res = Integer.parseInt(el.getText());
			}
		}
		return res;
	}

	private int readDiveNumber(Element informationBeforeDiveEl) {
		Element el = informationBeforeDiveEl.getChild("divenumber");
		if (el != null)
			return Integer.parseInt(el.getText());
		return 0;
	}

	private Date readDate(Element element) {
		Element dateEl = element.getChild("datetime");
		Date date = null;
		if (dateEl != null) {
			try {
				date = FORMATTER_DATE.parse(dateEl.getText());
			} catch (ParseException e) {
				LOGGER.error("Can't parse value as date : " + dateEl.getText());
			}
		}
		return date;
	}

	private Map<String, DiveSite> readDiveSites(Element root) {
		Map<String, DiveSite> diveSites = new HashMap<String, DiveSite>();
		Element diveSitesEl = root.getChild("divesite");

		for (Iterator iterator = diveSitesEl.getChildren("site").iterator(); iterator
				.hasNext();) {
			Element siteEl = (Element) iterator.next();
			DiveSite ds = new DiveSite();
			ds.setName(siteEl.getChildText("name"));
			diveSites.put(siteEl.getAttributeValue("id"), ds);
		}

		return diveSites;
	}

	private Map<String, Diver> readDivers(Element root) {
		Map<String, Diver> divers = new HashMap<String, Diver>();
		Element diversEl = root.getChild("diver");
		if (null == diversEl){
			return divers;
		}

		Element ownerEl = diversEl.getChild("owner");
		if (null != ownerEl) {
			Diver d = new Diver();
			Element fnEl = ownerEl.getChild("personal").getChild("firstname");
			if (fnEl != null) {
				d.setFirstName(fnEl.getValue());
			}
			Element lnEl = ownerEl.getChild("personal").getChild("lastname");
			if (lnEl != null) {
				d.setLastName(lnEl.getValue());
			}
			divers.put(ownerEl.getAttributeValue("id"), d);
		}

		for (Iterator iterator = diversEl.getChildren("buddy").iterator(); iterator
				.hasNext();) {
			Element buddyEl = (Element) iterator.next();
			Diver d = new Diver();
			Element fnEl = buddyEl.getChild("personal").getChild("firstname");
			if (fnEl != null) {
				d.setFirstName(fnEl.getValue());
			}
			Element lnEl = buddyEl.getChild("personal").getChild("lastname");
			if (lnEl != null) {
				d.setLastName(lnEl.getValue());
			}
			divers.put(buddyEl.getAttributeValue("id"), d);
		}
		return divers;
	}

	protected String formatDouble(double value) {
		return new BigDecimal(value).toPlainString();
	}

}
