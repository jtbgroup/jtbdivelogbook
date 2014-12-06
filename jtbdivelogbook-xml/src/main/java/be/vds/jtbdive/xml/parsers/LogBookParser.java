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
package be.vds.jtbdive.xml.parsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Attribute;
import org.jdom.Element;

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveEquipment;
import be.vds.jtbdive.core.core.DiveProfile;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.Document;
import be.vds.jtbdive.core.core.GazMix;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.core.Palanquee;
import be.vds.jtbdive.core.core.PalanqueeEntry;
import be.vds.jtbdive.core.core.PhysiologicalStatus;
import be.vds.jtbdive.core.core.Rating;
import be.vds.jtbdive.core.core.Version;
import be.vds.jtbdive.core.core.catalogs.DivePlatform;
import be.vds.jtbdive.core.core.catalogs.DivePurpose;
import be.vds.jtbdive.core.core.catalogs.DiveType;
import be.vds.jtbdive.core.core.catalogs.DiverRole;
import be.vds.jtbdive.core.core.catalogs.Gaz;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.DefaultEquipment;
import be.vds.jtbdive.core.core.material.DiveComputerEquipment;
import be.vds.jtbdive.core.core.material.DiveTankEquipment;
import be.vds.jtbdive.core.core.material.Equipment;
import be.vds.jtbdive.core.core.material.FinsEquipment;
import be.vds.jtbdive.core.core.material.MaskEquipment;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;
import be.vds.jtbdive.core.core.material.WeightBeltEquipment;
import be.vds.jtbdive.core.logging.Syslog;

public class LogBookParser extends AbstractXMLParser {

	private static final Syslog LOGGER = Syslog.getLogger(LogBookParser.class);

	public static final String TAG_EQUIPMENT_BALLAST = "ballast";
	public static final String TAG_EQUIPMENT_WEIGHT = "weight";
	public static final String TAG_EQUIPMENT_WEIGHTBELT = "weightbelt";
	public static final String TAG_DIVE_DOCUMENTS = "documents";
	public static final String TAG_MATERIALCAVE = "materialcave";
	public static final String TAG_DIVES = "dives";
	public static final String TAG_DIVE = "dive";
	private static final String TAG_DIVE_SITE = "divesite";
	private static final String TAG_DIVER = "diver";
	private static final String TAG_OWNER = "owner";

	private static final SimpleDateFormat FORMATTER_DATE = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private Element rootElement;

	private LogBookParser(Element rootElement) {
		this.rootElement = rootElement;
	}

	// TODO: make it private
	public LogBook readLogBook(Map<Long, DiveSite> diveSites,
			Map<Long, Diver> divers) {
		LogBook lb = new LogBook();
		lb.setId(Long.parseLong(rootElement.getChildText("id")));
		lb.setName(rootElement.getChildText("name"));

		if (rootElement.getChild("owner") != null) {
			lb.setOwner(divers.get(Long.parseLong(rootElement
					.getChildText("owner"))));
		}

		MatCaveParser materialCaveParser = MatCaveParser
				.createParser(getMaterialCaveRootElement(false));
		lb.getMatCave().setMaterials(materialCaveParser.getMaterials());
		lb.getMatCave().setMaterialSets(materialCaveParser.getMatialSets());

		if (rootElement.getChild(TAG_DIVES) != null) {
			for (Iterator iterator = rootElement.getChild(TAG_DIVES)
					.getChildren(TAG_DIVE).iterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();
				lb.addDive(readDiveElement(element, diveSites, divers,
						lb.getMatCave()));
			}
		}

		return lb;
	}

	private Element getMaterialCaveRootElement(boolean createIfNotPresent) {
		if (createIfNotPresent) {
			createNodeIfNotExist(TAG_MATERIALCAVE);
		}
		return rootElement.getChild(TAG_MATERIALCAVE);
	}

	// TODO: make it private
	public Dive readDiveElement(long diveId, Map<Long, DiveSite> diveSites,
			Map<Long, Diver> divers, MatCave matCave) {
		Dive dive = null;
		Element divesEl = rootElement.getChild(TAG_DIVES);
		for (Iterator it = divesEl.getChildren(TAG_DIVE).iterator(); it
				.hasNext();) {
			Element diveEl = (Element) it.next();
			if (Long.valueOf(diveEl.getAttributeValue(ATTRIBUTE_ID))
					.longValue() == diveId) {
				dive = readDiveElement(diveEl, diveSites, divers, matCave);
			}
		}

		return dive;
	}

	private Dive readDiveElement(Element diveElement,
			Map<Long, DiveSite> diveLocations, Map<Long, Diver> divers,
			MatCave matCave) {
		Dive dive = new Dive();
		dive.setId(Long.parseLong(diveElement.getAttributeValue("id")));
		dive.setNumber(Integer.parseInt(diveElement.getChildText("number")));

		if (diveElement.getChild("date") != null) {
			String dateString = diveElement.getChildText("date");
			Date date = null;
			if (dateString != null) {
				if (dateString
						.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
					try {
						date = FORMATTER_DATE.parse(dateString);
					} catch (ParseException e) {
						LOGGER.error("Error parsing the date for dive id "
								+ dive.getId() + " : " + e.getMessage());
					}
				} else {
					date = new Date(Long.parseLong(dateString));
					LOGGER.warn("Date of dive " + dive.getId()
							+ " is still in old format (date.getTime())");
				}
				dive.setDate(date);
			}
		}

		if (diveElement.getChild("divetime") != null) {
			dive.setDiveTime(Integer.parseInt(diveElement
					.getChildText("divetime")));
		}

		if (diveElement.getChild("maxdepth") != null) {
			dive.setMaxDepth(Double.parseDouble(diveElement
					.getChildText("maxdepth")));
		}

		if (diveElement.getChild("comment") != null) {
			dive.setComment(diveElement.getChildText("comment"));
		}

		if (diveElement.getChild("rating") != null) {
			Rating rating = new Rating();
			rating.setValue(Short.parseShort(diveElement.getChild("rating")
					.getAttributeValue("value")));
			dive.setRating(rating);
		}

		if (diveElement.getChild("platform") != null) {
			dive.setDivePlatform(DivePlatform.getDivePlatform(Short
					.parseShort(diveElement.getChildText("platform"))));
		}

		if (diveElement.getChild("types") != null) {
			List<DiveType> diveTypes = new ArrayList<DiveType>();
			for (Iterator iterator = diveElement.getChild("types")
					.getChildren("type").iterator(); iterator.hasNext();) {
				Element diveTypeEl = (Element) iterator.next();
				diveTypes.add(DiveType.getDiveType(Short.parseShort(diveTypeEl
						.getText())));
			}
			if (diveTypes.size() > 0)
				dive.setDiveType(diveTypes);
		}

		if (diveElement.getChild("purposes") != null) {
			List<DivePurpose> list = new ArrayList<DivePurpose>();
			for (Iterator iterator = diveElement.getChild("purposes")
					.getChildren("purpose").iterator(); iterator.hasNext();) {
				Element diveTypeEl = (Element) iterator.next();
				list.add(DivePurpose.getDivePurpose(Short.parseShort(diveTypeEl
						.getText())));
			}
			if (list.size() > 0)
				dive.setDivePurposes(list);
		}

		if (diveElement.getChild("surfacetime") != null) {
			dive.setSurfaceTime(Integer.parseInt(diveElement
					.getChildText("surfacetime")));
		}

		if (diveElement.getChild("watertemperature") != null) {
			dive.setWaterTemperature(Double.parseDouble(diveElement
					.getChildText("watertemperature")));
		}

		if (diveElement.getChild("divesite") != null) {
			dive.setDiveSite(diveLocations.get(Long.parseLong(diveElement
					.getChildText("divesite"))));
		}

		if (diveElement.getChild("palanquee") != null) {
			dive.setPalanquee(readPalanquee(diveElement.getChild("palanquee"),
					divers));
		}

		if (diveElement.getChild("physiologicalstatus") != null) {
			dive.setPhysiologicalStatus(readPhysiologicalStatus(diveElement
					.getChild("physiologicalstatus")));
		}

		if (diveElement.getChild("diveprofile") != null) {
			dive.setDiveProfile(readDiveProfile(diveElement
					.getChild(DiveProfileParser.TAG_DIVEPROFILE)));
		}

		if (diveElement.getChild("equipments") != null) {
			dive.setDiveEquipment(readDiveEquipments(
					diveElement.getChild("equipments"), matCave));
		}

		if (diveElement.getChild(TAG_DIVE_DOCUMENTS) != null) {
			dive.setDocuments(readDocuments(diveElement
					.getChild(DocumentParser.TAG_DOCUMENTS)));
		}

		return dive;
	}

	private List<Document> readDocuments(Element documentsEl) {
		List<Document> docs = new ArrayList<Document>();
		// DocumentParser parser = new DocumentParser();
		for (Iterator iterator = documentsEl.getChildren().iterator(); iterator
				.hasNext();) {
			Element documentEl = (Element) iterator.next();
			docs.add(DocumentParser.readDocument(documentEl));
		}

		if (docs.size() == 0) {
			return null;
		}
		return docs;
	}

	private DiveEquipment readDiveEquipments(Element equipmentElement,
			MatCave matCave) {
		List<Equipment> equipments = new ArrayList<Equipment>();
		for (Iterator iterator = equipmentElement.getChildren().iterator(); iterator
				.hasNext();) {
			Element el = (Element) iterator.next();
			String name = el.getName();
			Equipment equipment = null;
			if (name.equals("divetank")) {
				equipment = readDiveTankEquipment(el);
			} else if (name.equals("divecomputer")) {
				equipment = readDiveComputerEquipment(el);
			} else if (name.equals(TAG_EQUIPMENT_BALLAST)
					|| el.getName().equals(TAG_EQUIPMENT_WEIGHTBELT)) {
				equipment = readWeightBeltEquipment(el);
			} else if (name.equals("fins")) {
				equipment = readFinsEquipment(el);
			} else if (name.equals("mask")) {
				equipment = readMaskEquipment(el);
			} else {
				equipment = readDefaultEquipment(el);
			}

			readCommonEquipmentAttributes(el, equipment, matCave);

			equipments.add(equipment);
		}

		if (equipments.size() > 0) {
			DiveEquipment de = new DiveEquipment();
			de.setEquipments(equipments);
			return de;
		}

		return null;
	}

	private void readCommonEquipmentAttributes(Element el, Equipment equipment,
			MatCave matCave) {
		String index = el.getAttributeValue("index");
		if (index != null) {
			equipment.setOrderIndex(Integer.parseInt(index));
		}

		String materialId = el.getAttributeValue("materialid");
		if (materialId != null) {
			equipment.setMaterial(findMaterialForId(Long.parseLong(materialId),
					matCave));

		}

		Element commentElement = el.getChild("comment");
		if (null != commentElement) {
			equipment.setComment(commentElement.getText());
		}

		if (null != el.getChild("position")) {
			equipment
					.setPosition(Integer.parseInt(el.getChildText("position")));
		}
	}

	private Material findMaterialForId(long materialId, MatCave matCave) {
		if (null == matCave) {
			MatCaveParser materialParser = MatCaveParser
					.createParser(getMaterialCaveRootElement(false));
			return materialParser.findMaterialForId(materialId);
		}

		return matCave.getMaterialForId(materialId);
	}

	private Equipment readDefaultEquipment(Element el) {
		MaterialType materialType = null;
		String name = el.getName();
		if (name.equals("other"))
			materialType = MaterialType.OTHER;
		else if (name.equals("knife"))
			materialType = MaterialType.KNIFE;
		else if (name.equals("light"))
			materialType = MaterialType.LIGHT;
		else if (name.equals("buoyancycompensator"))
			materialType = MaterialType.BUOYANCY_COMPENSATOR;
		else if (name.equals("compass"))
			materialType = MaterialType.COMPASS;
		else if (name.equals("manometer"))
			materialType = MaterialType.MANOMETER;
		else if (name.equals("regulator"))
			materialType = MaterialType.REGULATOR;
		else if (name.equals("scooter"))
			materialType = MaterialType.SCOOTER;
		else if (name.equals("snorkel"))
			materialType = MaterialType.SNORKEL;
		else if (name.equals("watch"))
			materialType = MaterialType.WATCH;
		else if (name.equals("suit"))
			materialType = MaterialType.SUIT;
		else if (name.equals("gloves"))
			materialType = MaterialType.GLOVES;
		else if (name.equals("hood"))
			materialType = MaterialType.HOOD;
		else if (name.equals("boots"))
			materialType = MaterialType.BOOTS;
		else if (name.equals("rebreather"))
			materialType = MaterialType.REBREATHER;
		else
			LOGGER.warn("The material type couldn't be found for name " + name);
		if (materialType == null)
			return null;

		DefaultEquipment eq = new DefaultEquipment(materialType);
		return eq;
	}

	private Equipment readFinsEquipment(Element el) {
		FinsEquipment fe = new FinsEquipment();
		return fe;
	}

	private Equipment readMaskEquipment(Element el) {
		MaskEquipment mask = new MaskEquipment();
		return mask;
	}

	private Equipment readWeightBeltEquipment(Element el) {
		WeightBeltEquipment b = new WeightBeltEquipment();
		b.setWeight(Double.parseDouble(el.getChildText(TAG_EQUIPMENT_WEIGHT)));
		return b;
	}

	private Equipment readDiveComputerEquipment(Element el) {
		DiveComputerEquipment dc = new DiveComputerEquipment();
		// dc.setDiveComputerType(DiveComputerType.getDiveComputerType(el
		// .getChildText("type")));
		dc.setRemainingBattery(Integer.parseInt(el.getChildText("battery")));
		return dc;
	}

	private Equipment readDiveTankEquipment(Element diveTankElement) {
		DiveTankEquipment dt = new DiveTankEquipment();

		if (null != diveTankElement.getChild("beginpressure")) {
			dt.setBeginPressure(Double.parseDouble(diveTankElement
					.getChildText("beginpressure")));
		}
		if (null != diveTankElement.getChild("endpressure")) {
			dt.setEndPressure(Double.parseDouble(diveTankElement
					.getChildText("endpressure")));
		}

		if (null != diveTankElement.getChild("switchtime")) {
			dt.setSwitchTime(Double.parseDouble(diveTankElement
					.getChildText("switchtime")));
		}

		if (diveTankElement.getChild("gazmix") != null) {
			GazMix gazMix = new GazMix();
			for (Iterator iterator = diveTankElement.getChild("gazmix")
					.getChildren("gaz").iterator(); iterator.hasNext();) {
				Element el = (Element) iterator.next();
				String percent = el.getText();
				gazMix.addGaz(Gaz.getGaz(Integer.parseInt(el
						.getAttributeValue("type"))), Integer.parseInt(percent));
			}
			dt.setGazMix(gazMix);
		}

		return dt;
	}

	private PhysiologicalStatus readPhysiologicalStatus(Element element) {
		PhysiologicalStatus ps = new PhysiologicalStatus();
		ps.setArterialMicrobubbleLevel(Integer.parseInt(element
				.getChildText("arterialmicrobubblelevel")));
		ps.setCnsBeforeDive(Integer.parseInt(element
				.getChildText("cnsbeforedive")));
		ps.setMaxPPO2(Double.parseDouble(element.getChildText("maxppo2")));
		ps.setSkinCoolTemperature(Double.parseDouble(element
				.getChildText("skincooltemperature")));
		ps.setInterPulmonaryShunt(Double.parseDouble(element
				.getChildText("interpulmonaryshunt")));

		if (null != element.getChild("saturationindexafterdive")) {
			ps.setSaturationIndexAfterDive(element.getChildText(
					"saturationindexafterdive").charAt(0));
		}

		if (null != element.getChild("saturationindexbeforedive")) {
			ps.setSaturationIndexBeforeDive(element.getChildText(
					"saturationindexbeforedive").charAt(0));
		}

		return ps;
	}

	private Palanquee readPalanquee(Element palanqueeEl, Map<Long, Diver> divers) {
		Palanquee p = new Palanquee();
		for (Iterator iterator = palanqueeEl.getChildren("entry").iterator(); iterator
				.hasNext();) {
			Element el = (Element) iterator.next();

			Diver diver = divers.get(Long.parseLong(el
					.getAttributeValue("diver")));
			if (null != diver) {
				PalanqueeEntry pe = new PalanqueeEntry();
				pe.setDiver(diver);
				pe.setPalanqueeRole(DiverRole.convertToRoles(Integer
						.parseInt(el.getAttributeValue("role"))));
				p.addPalanqueeEntry(pe);
			}
		}
		return p;
	}

	private DiveProfile readDiveProfile(Element child) {
		return DiveProfileParser.readDiveProfile(child);
	}

	private void buildAndRegisterRootElement(LogBook logBook) {
		List<Material> materials = null;
		Set<MaterialSet> materialSets = null;
		MatCave cave = logBook.getMatCave();
		if (cave != null) {
			materials = cave.getAllMaterials();
			materialSets = cave.getMaterialSets();
		}

		buildAndRegisterRootElement(logBook, logBook.getDives(), materials,
				materialSets);
	}

	/**
	 * Creates an XML root Element containing the meta data of the logbook
	 * (owner, name, ...), the list of dives and the list of material.
	 * <p/>
	 * The material referenced in the equipment of the dives is supposed to be
	 * known in the list of material. If a material is in the list be not used
	 * in any dive, there is no problem.
	 * 
	 * @param logBook
	 *            the logbook to be referenced in the XML Element
	 * @param dives
	 *            the list of dives that must appear in the root Element
	 * @param materials
	 *            the list of material that must appear in the root element
	 * @return An XML Element containing the logbook, dives and material.
	 * 
	 */
	private void buildAndRegisterRootElement(LogBook logBook, List<Dive> dives,
			List<Material> materials, Set<MaterialSet> materialSets) {

		rootElement = new Element("logbook");

		Element version = new Element("version");
		version.setText(Version.getCurrentVersion().toString());
		rootElement.addContent(version);

		Element idEl = new Element("id");
		idEl.setText(String.valueOf(logBook.getId()));
		rootElement.addContent(idEl);

		Element nameEl = new Element("name");
		nameEl.setText(logBook.getName() != null ? logBook.getName() : "");
		rootElement.addContent(nameEl);

		if (logBook.getOwner() != null) {
			Element ownerEl = new Element("owner");
			ownerEl.setText(String.valueOf(logBook.getOwner().getId()));
			rootElement.addContent(ownerEl);
		}

		MatCaveParser materialParser = MatCaveParser
				.createParser(getMaterialCaveRootElement(true));
		if (null != materials) {
			materialParser.setMaterials(materials);
		}

		if (null != materialSets) {
			materialParser.setMaterialSets(materialSets);
		}

		if (dives.size() > 0) {
			Element el = new Element(TAG_DIVES);
			for (Dive dive : dives) {
				el.addContent(createDiveElement(dive));
			}
			rootElement.addContent(el);
		}
	}

	private Element createDiveElement(Dive dive) {
		Element diveElement = new Element("dive");
		diveElement.setAttribute("id", String.valueOf(dive.getId()));

		Element subElement = null;

		if (null != dive.getDate()) {
			subElement = new Element("date");
			subElement.setText(FORMATTER_DATE.format(dive.getDate()));
			diveElement.addContent(subElement);
		}

		subElement = new Element("number");
		subElement.setText(String.valueOf(dive.getNumber()));
		diveElement.addContent(subElement);

		subElement = new Element("watertemperature");
		subElement.setText(String.valueOf(dive.getWaterTemperature()));
		diveElement.addContent(subElement);

		subElement = new Element("divetime");
		subElement.setText(String.valueOf(dive.getDiveTime()));
		diveElement.addContent(subElement);

		subElement = new Element("maxdepth");
		subElement.setText(String.valueOf(dive.getMaxDepth()));
		diveElement.addContent(subElement);

		subElement = new Element("surfacetime");
		subElement.setText(String.valueOf(dive.getSurfaceTime()));
		diveElement.addContent(subElement);

		if (dive.getComment() != null && dive.getComment().length() > 0) {
			subElement = new Element("comment");
			subElement.setText(String.valueOf(dive.getComment()));
			diveElement.addContent(subElement);
		}

		if (dive.getDivePlatform() != null) {
			subElement = new Element("platform");
			subElement.setText(String.valueOf(dive.getDivePlatform().getId()));
			diveElement.addContent(subElement);
		}

		if (dive.getDivePurposes() != null && dive.getDivePurposes().size() > 0) {
			subElement = new Element("purposes");
			for (DivePurpose dp : dive.getDivePurposes()) {
				Element pe = new Element("purpose");
				pe.setText(String.valueOf(dp.getId()));
				subElement.addContent(pe);
			}
			diveElement.addContent(subElement);
		}

		if (dive.getDiveTypes() != null && dive.getDiveTypes().size() > 0) {
			subElement = new Element("types");
			for (DiveType dp : dive.getDiveTypes()) {
				Element pe = new Element("type");
				pe.setText(String.valueOf(dp.getId()));
				subElement.addContent(pe);
			}
			diveElement.addContent(subElement);
		}

		if (dive.getRating() != null) {
			subElement = new Element("rating");
			subElement.setAttribute("value",
					String.valueOf(dive.getRating().getValue()));
			diveElement.addContent(subElement);
		}

		if (dive.getDiveSite() != null) {
			subElement = new Element("divesite");
			subElement.setText(String.valueOf(dive.getDiveSite().getId()));
			diveElement.addContent(subElement);
		}

		if (dive.getPhysiologicalStatus() != null) {
			diveElement.addContent(createPhysiologicalStatusElement(dive
					.getPhysiologicalStatus()));
		}

		if (dive.getPalanquee() != null
				&& dive.getPalanquee().getPalanqueeEntries().size() > 0) {
			diveElement.addContent(createPalanqueeElement(dive.getPalanquee()));
		}

		if (dive.getDiveProfile() != null) {
			diveElement.addContent(createDiveProfileElement(dive
					.getDiveProfile()));
		}

		if (dive.getDiveEquipment() != null) {
			diveElement.addContent(createEquipmentElement(dive
					.getDiveEquipment()));
		}

		if (dive.getDocuments() != null && dive.getDocuments().size() > 0) {
			diveElement.addContent(createDocumentsElement(dive.getDocuments()));
		}

		return diveElement;
	}

	private Element createDocumentsElement(List<Document> documents) {
		Element el = new Element(TAG_DIVE_DOCUMENTS);
		for (Document document : documents) {
			el.addContent(DocumentParser.createDocumentElement(document));
		}
		return el;
	}

	private Element createEquipmentElement(DiveEquipment diveEquipment) {
		List<Equipment> equipments = diveEquipment.getAllEquipments();

		Element el = new Element("equipments");
		for (Equipment equipment : equipments) {
			Element equipmentEl = null;
			switch (equipment.getMaterialType()) {
			case DIVE_TANK:
				equipmentEl = createDiveTankEquipmentElement((DiveTankEquipment) equipment);
				break;
			case DIVE_COMPUTER:
				equipmentEl = createDiveComputerEquipmentElement((DiveComputerEquipment) equipment);
				break;
			case WEIGHT_BELT:
				equipmentEl = createWeigthBeltEquipmentElement((WeightBeltEquipment) equipment);
				break;
			case FINS:
				equipmentEl = createFinsEquipmentElement((FinsEquipment) equipment);
				break;
			case MASK:
				equipmentEl = createMaskEquipmentElement((MaskEquipment) equipment);
				break;
			default:
				equipmentEl = createDefaultEquipmentElement((DefaultEquipment) equipment);
			}

			fillCommonEquipmentAttributes(equipment, equipmentEl);

			el.addContent(equipmentEl);
		}
		return el;
	}

	private void fillCommonEquipmentAttributes(Equipment equipment,
			Element equipmentEl) {
		equipmentEl.setAttribute("index",
				String.valueOf(equipment.getOrderIndex()));

		if (equipment.getMaterial() != null) {
			equipmentEl.setAttribute("materialid",
					String.valueOf(equipment.getMaterial().getId()));
		}

		if (equipment.getPosition() > 0) {
			Element position = new Element("position");
			position.setText(String.valueOf(equipment.getPosition()));
			equipmentEl.addContent(position);
		}

		if (null != equipment.getComment()
				&& equipment.getComment().length() > 0) {
			Element commentEl = new Element("comment").setText(equipment
					.getComment());
			equipmentEl.addContent(commentEl);
		}
	}

	private Element createDefaultEquipmentElement(DefaultEquipment equipment) {
		String name = "other";
		switch (equipment.getMaterialType()) {
		case KNIFE:
			name = "knife";
			break;
		case LIGHT:
			name = "light";
			break;
		case BUOYANCY_COMPENSATOR:
			name = "buoyancycompensator";
			break;
		case COMPASS:
			name = "compass";
			break;
		case MANOMETER:
			name = "manometer";
			break;
		case REGULATOR:
			name = "regulator";
			break;
		case SCOOTER:
			name = "scooter";
			break;
		case SNORKEL:
			name = "snorkel";
			break;
		case WATCH:
			name = "watch";
			break;
		case SUIT:
			name = "suit";
			break;
		case GLOVES:
			name = "gloves";
			break;
		case BOOTS:
			name = "boots";
			break;
		case HOOD:
			name = "hood";
			break;
		case REBREATHER:
			name = "rebreather";
			break;
		case OTHER:
			name = "other";
			break;
		}
		Element el = new Element(name);
		return el;
	}

	private Element createMaskEquipmentElement(MaskEquipment equipment) {
		Element el = new Element("mask");
		return el;
	}

	private Element createFinsEquipmentElement(FinsEquipment fins) {
		Element el = new Element("fins");
		return el;
	}

	private Element createWeigthBeltEquipmentElement(WeightBeltEquipment ballast) {
		Element el = new Element(TAG_EQUIPMENT_WEIGHTBELT);

		Element weightEl = new Element(TAG_EQUIPMENT_WEIGHT);
		weightEl.setText(String.valueOf(ballast.getWeight()));
		el.addContent(weightEl);

		return el;
	}

	private Element createDiveComputerEquipmentElement(
			DiveComputerEquipment diveComputer) {
		Element el = new Element("divecomputer");

		// Element typeEl = new Element("type");
		// typeEl.setText(diveComputer.getDiveComputerType().getKey());
		// el.addContent(typeEl);

		Element batteryEl = new Element("battery");
		batteryEl.setText(String.valueOf(diveComputer.getRemainingBattery()));
		el.addContent(batteryEl);

		return el;
	}

	private Element createDiveTankEquipmentElement(DiveTankEquipment diveTank) {
		Element diveTankEl = new Element("divetank");

		Element beginPressure = new Element("beginpressure");
		beginPressure.setText(String.valueOf(diveTank.getBeginPressure()));
		diveTankEl.addContent(beginPressure);

		Element endPressure = new Element("endpressure");
		endPressure.setText(String.valueOf(diveTank.getEndPressure()));
		diveTankEl.addContent(endPressure);

		Element switchTime = new Element("switchtime");
		switchTime.setText(String.valueOf(diveTank.getSwitchTime()));
		diveTankEl.addContent(switchTime);

		if (diveTank.getGazMix() != null && diveTank.getGazMix().size() > 0) {
			Element gazmix = new Element("gazmix");
			for (Gaz gaz : diveTank.getGazMix().getGazes()) {
				Element el = new Element("gaz");
				el.setAttribute("type", String.valueOf(gaz.getGazType()));
				el.setAttribute("formule", gaz.getFormule());
				el.setText(String.valueOf(diveTank.getGazMix().getPercentage(
						gaz)));
				gazmix.addContent(el);
			}
			diveTankEl.addContent(gazmix);
		}

		return diveTankEl;
	}

	private Element createPalanqueeElement(Palanquee palanquee) {
		Element palEl = new Element("palanquee");
		for (PalanqueeEntry pe : palanquee.getPalanqueeEntries()) {
			if (pe.getDiver() != null) {
				Element entryEl = new Element("entry");
				entryEl.setAttribute("diver",
						String.valueOf(pe.getDiver().getId()));
				entryEl.setAttribute("role",
						String.valueOf(DiverRole.convertToInt(pe.getRoles())));
				palEl.addContent(entryEl);
			}
		}
		return palEl;
	}

	private Element createDiveProfileElement(DiveProfile diveProfile) {
		return DiveProfileParser.createDiveProfileElement(diveProfile);
	}

	private Element createPhysiologicalStatusElement(
			PhysiologicalStatus physiologicalStatus) {
		Element psEl = new Element("physiologicalstatus");
		Element subElement = null;

		if (physiologicalStatus.getSaturationIndexBeforeDive() != (char) 0) {
			subElement = new Element("saturationindexbeforedive");
			subElement.setText(String.valueOf(physiologicalStatus
					.getSaturationIndexBeforeDive()));
			psEl.addContent(subElement);
		}
		;

		if (physiologicalStatus.getSaturationIndexAfterDive() != (char) 0) {
			subElement = new Element("saturationindexafterdive");
			subElement.setText(String.valueOf(physiologicalStatus
					.getSaturationIndexAfterDive()));
			psEl.addContent(subElement);
		}
		;

		subElement = new Element("arterialmicrobubblelevel");
		subElement.setText(String.valueOf(physiologicalStatus
				.getArterialMicrobubbleLevel()));
		psEl.addContent(subElement);

		subElement = new Element("cnsbeforedive");
		subElement.setText(String.valueOf(physiologicalStatus
				.getCnsBeforeDive()));
		psEl.addContent(subElement);

		subElement = new Element("maxppo2");
		subElement.setText(String.valueOf(physiologicalStatus.getMaxPPO2()));
		psEl.addContent(subElement);

		subElement = new Element("skincooltemperature");
		subElement.setText(String.valueOf(physiologicalStatus
				.getSkinCoolTemperature()));
		psEl.addContent(subElement);

		subElement = new Element("interpulmonaryshunt");
		subElement.setText(String.valueOf(physiologicalStatus
				.getInterPulmonaryShunt()));
		psEl.addContent(subElement);

		return psEl;
	}

	// private Element createMaterialElement(Material material) {
	// MaterialParser parser = new MaterialParser();
	// return parser.createMaterialElement(material);
	// }

	private void removeMaterialReference(long materialId) {
		for (Iterator iterator = rootElement.getChild("dives")
				.getChildren("dive").iterator(); iterator.hasNext();) {
			Element diveEl = (Element) iterator.next();
			if (diveEl.getChild("equipments") != null) {
				Element equipementEl = null;
				Attribute materialAttribute = null;
				for (Iterator iterator2 = diveEl.getChild("equipments")
						.getChildren().iterator(); iterator2.hasNext();) {
					equipementEl = (Element) iterator2.next();
					materialAttribute = equipementEl.getAttribute("materialid");
					if (materialAttribute != null
							&& (Long.parseLong(materialAttribute.getValue())) == materialId) {
						equipementEl.removeAttribute("materialid");
					}
				}

			}
		}
	}

	public void replaceMaterialReference(long materialToKeepId,
			long materialToDeleteId) {

		// Element matsEl = rootElement.getChild(TAG_MATERIALCAVE);
		// if (matsEl != null) {
		// detachElWithIdFromParent(matsEl, materialToDeleteId);
		// }

		MatCaveParser materialParser = MatCaveParser
				.createParser(getMaterialCaveRootElement(false));
		materialParser.replaceMaterialReference(materialToKeepId,
				materialToDeleteId);

		for (Iterator iterator = rootElement.getChild(TAG_DIVES)
				.getChildren(TAG_DIVE).iterator(); iterator.hasNext();) {
			Element diveEl = (Element) iterator.next();
			if (diveEl.getChild("equipments") != null) {
				Element equipementEl = null;
				Attribute materialAttribute = null;
				for (Iterator iterator2 = diveEl.getChild("equipments")
						.getChildren().iterator(); iterator2.hasNext();) {
					equipementEl = (Element) iterator2.next();
					materialAttribute = equipementEl.getAttribute("materialid");
					if (materialAttribute != null
							&& (Long.parseLong(materialAttribute.getValue())) == materialToDeleteId) {
						materialAttribute.setValue(String
								.valueOf(materialToKeepId));
					}
				}

			}
		}
	}

	public static LogBookParser createParser(Element rootElement) {
		return new LogBookParser(rootElement);
	}

	/**
	 * Creates a parser and populate it with the logbook
	 * 
	 * @param logBook
	 * @return
	 */
	public static LogBookParser buildParser(LogBook logBook) {
		LogBookParser parser = new LogBookParser(null);
		parser.buildAndRegisterRootElement(logBook);
		return parser;
	}

	/**
	 * Creates a parser and populates it with the given dives, materials and
	 * materialsets
	 * 
	 * @param logBook
	 * @param dives
	 * @param materials
	 * @param materialSets
	 * @return
	 */
	public static LogBookParser buildParser(LogBook logBook, List<Dive> dives,
			List<Material> materials, Set<MaterialSet> materialSets) {
		LogBookParser parser = new LogBookParser(null);
		parser.buildAndRegisterRootElement(logBook, dives, materials,
				materialSets);
		return parser;
	}

	// private void setRootElement(Element root) {
	// rootElement = root;
	// }

	/**
	 * This method is set to public in this class
	 */
	@Override
	public Element getRootElement() {
		return rootElement;
	}

	public long getLogBookId() {
		return Long.parseLong(rootElement.getChildText("id"));
	}

	public String getLogBookName() {
		return rootElement.getChildText("name");
	}

	public void addDive(Dive dive) {
		createNodeIfNotExist(new String[] { LogBookParser.TAG_DIVES });
		rootElement.getChild(TAG_DIVES).addContent(createDiveElement(dive));
	}

	public List<String> findDiveDocumentNames(long diveId) {
		for (Iterator iterator = rootElement.getChild(TAG_DIVES)
				.getChildren(TAG_DIVE).iterator(); iterator.hasNext();) {
			Element diveEl = (Element) iterator.next();
			if (Long.parseLong(diveEl.getAttributeValue(ATTRIBUTE_ID)) == diveId) {
				return getDocumentNames(diveEl);
			}
		}
		return null;
	}

	private List<String> getDocumentNames(Element diveEl) {
		if (diveEl.getChild(DocumentParser.TAG_DOCUMENTS) != null) {
			return DocumentParser.getDocumentFileNames(diveEl
					.getChild(DocumentParser.TAG_DOCUMENTS));
		}
		return null;
	}

	public boolean removeDive(long diveId) {
		boolean b = false;
		Element divesEl = rootElement.getChild(TAG_DIVES);
		if (null != divesEl) {
			Element elToDetach = null;
			Element element = null;
			for (Iterator iterator = divesEl.getChildren(TAG_DIVE).iterator(); iterator
					.hasNext();) {
				element = (Element) iterator.next();
				if (Long.valueOf(element.getAttributeValue(ATTRIBUTE_ID)) == diveId) {
					elToDetach = element;
					break;
				}
			}
			if (elToDetach != null) {
				divesEl.removeContent(elToDetach);
				b = true;
			}
		}
		return b;
	}

	public List<String> findAllDivesDocumentNames() {
		List<String> list = new ArrayList<String>();
		Element divesEl = rootElement.getChild(TAG_DIVES);
		if (divesEl != null) {
			for (Iterator iterator = divesEl.getChildren(TAG_DIVE).iterator(); iterator
					.hasNext();) {
				Element diveEl = (Element) iterator.next();
				if (diveEl != null) {
					List<String> s = getDocumentNames(diveEl);
					if (s != null) {
						list.addAll(s);
					}
				}
			}
		}
		return list;
	}

	public boolean isDiveSiteUsed(long diveSiteId) {
		Element divesEl = rootElement.getChild(TAG_DIVES);
		if (null != divesEl) {
			for (Iterator iterator = divesEl.getChildren(TAG_DIVE).iterator(); iterator
					.hasNext();) {
				Element el = (Element) iterator.next();
				if (el.getChild(TAG_DIVE_SITE) != null
						&& Long.parseLong(el.getChildText(TAG_DIVE_SITE)) == diveSiteId) {
					return true;
				}
			}
		}
		return false;
	}

	public long getLogBookOwnerId() {
		if (rootElement.getChildText("owner") != null) {
			return Long.parseLong(rootElement.getChildText("owner"));
		}
		return -1;
	}

	public boolean isDiverUsed(long diverId) {
		Element divesEl = rootElement.getChild(TAG_DIVES);
		if (null != divesEl) {
			for (Iterator iterator = divesEl.getChildren(TAG_DIVE).iterator(); iterator
					.hasNext();) {
				Element el = (Element) iterator.next();
				if (el.getChild("palanquee") != null) {
					for (Iterator iterator2 = el.getChild("palanquee")
							.getChildren("entry").iterator(); iterator2
							.hasNext();) {
						Element entryEl = (Element) iterator2.next();
						if (Integer.parseInt(entryEl
								.getAttributeValue(TAG_DIVER)) == diverId) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public void replaceDiver(long diverToKeepId, long diverToDeleteId) {
		if (rootElement.getChild("owner") != null
				&& Long.parseLong(rootElement.getChildText("owner")) == diverToDeleteId) {
			rootElement.getChild("owner")
					.setText(String.valueOf(diverToKeepId));
		}

		Element divesEl = rootElement.getChild(TAG_DIVES);
		if (null != divesEl) {
			for (Iterator iterator = divesEl.getChildren(TAG_DIVE).iterator(); iterator
					.hasNext();) {
				Element el = (Element) iterator.next();
				if (el.getChild("palanquee") != null) {
					for (Iterator iterator2 = el.getChild("palanquee")
							.getChildren("entry").iterator(); iterator2
							.hasNext();) {
						Element entryEl = (Element) iterator2.next();
						if (Long.parseLong(entryEl.getAttributeValue(TAG_DIVER)) == diverToDeleteId) {
							entryEl.setAttribute(TAG_DIVER,
									String.valueOf(diverToKeepId));
						}
					}
				}
			}
		}
	}

	public void replaceDiveSite(long diveSiteToDeleteId, long diveSiteToKeepId) {
		if (rootElement.getChild(TAG_DIVES) != null) {
			for (Iterator iterator = rootElement.getChild(TAG_DIVES)
					.getChildren(TAG_DIVE).iterator(); iterator.hasNext();) {
				Element el = (Element) iterator.next();
				if (el.getChild(TAG_DIVE_SITE) != null
						&& Long.parseLong(el.getChildText(TAG_DIVE_SITE)) == diveSiteToDeleteId) {
					el.getChild(TAG_DIVE_SITE).setText(
							String.valueOf(diveSiteToKeepId));
				}
			}
		}
	}

	public void setLogBookName(String name) {
		if (rootElement.getChild("name") == null) {
			rootElement.addContent(new Element("name"));
		}
		rootElement.getChild("name").setText(name);

	}

	public void setOwner(long ownerId) {
		if (ownerId > -1) {
			if (rootElement.getChild(TAG_OWNER) == null) {
				rootElement.addContent(new Element(TAG_OWNER));
			}
			rootElement.getChild(TAG_OWNER).setText(String.valueOf(ownerId));
		} else {
			rootElement.removeChild(TAG_OWNER);
		}
	}

	public void addMaterial(Material material) {
		MatCaveParser materialParser = MatCaveParser
				.createParser(getMaterialCaveRootElement(true));
		materialParser.addMaterial(material);
	}

	public void removeMaterial(long materialId) {
		MatCaveParser materialParser = MatCaveParser
				.createParser(getMaterialCaveRootElement(false));
		materialParser.removeMaterial(materialId);

		removeMaterialReference(materialId);
	}

	public void removeMaterialSet(long materialSetId) {
		MatCaveParser materialParser = MatCaveParser
				.createParser(getMaterialCaveRootElement(false));
		materialParser.removeMaterialSet(materialSetId);
	}

	public void addMaterialSet(MaterialSet materialset) {
		MatCaveParser materialParser = MatCaveParser
				.createParser(getMaterialCaveRootElement(true));
		materialParser.addMaterialSet(materialset);
	}

	public void removeMaterialFromMaterialSet(long materialSetId,
			long materialId) {
		MatCaveParser materialParser = MatCaveParser
				.createParser(getMaterialCaveRootElement(false));
		materialParser.removeMaterialFromMaterialSet(materialSetId, materialId);
	}

	public void addMaterialsIntoMaterialSet(long materialSetId,
			Material[] materials) {
		MatCaveParser materialParser = MatCaveParser
				.createParser(getMaterialCaveRootElement(false));
		materialParser.addMaterialsIntoMaterialSet(materialSetId, materials);
	}

	// TODO: review code for documents
	public long getMaxDocumentId() {
		long id = -1;
		if (rootElement.getChild(TAG_DIVES) != null) {
			for (Iterator iterator = rootElement.getChild(TAG_DIVES)
					.getChildren(TAG_DIVE).iterator(); iterator.hasNext();) {
				Element el = (Element) iterator.next();

				if (el.getChild(TAG_DIVE_DOCUMENTS) != null) {
					for (Iterator iterator2 = rootElement
							.getChild(TAG_DIVE_DOCUMENTS)
							.getChildren("document").iterator(); iterator2
							.hasNext();) {
						Element dEl = (Element) iterator2.next();
						id = Math.max(id, Long.parseLong(dEl
								.getAttributeValue(ATTRIBUTE_ID)));
					}
				}
			}
		}
		return id;
	}
}
