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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom.Element;
import org.omg.IOP.TAG_INTERNET_IOP;

import be.vds.jtbdive.core.core.catalogs.DiveTankCompositeMaterial;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.catalogs.SuitType;
import be.vds.jtbdive.core.core.material.DefaultMaterial;
import be.vds.jtbdive.core.core.material.DefaultSizeableMaterial;
import be.vds.jtbdive.core.core.material.DiveComputerMaterial;
import be.vds.jtbdive.core.core.material.DiveTankMaterial;
import be.vds.jtbdive.core.core.material.FinsMaterial;
import be.vds.jtbdive.core.core.material.MaskMaterial;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;
import be.vds.jtbdive.core.core.material.SuitMaterial;
import be.vds.jtbdive.core.core.material.WeightBeltMaterial;
import be.vds.jtbdive.core.logging.Syslog;

public class MatCaveParser extends AbstractXMLParser {
	private static final SimpleDateFormat FORMATTER_DATE = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final Syslog LOGGER = Syslog.getLogger(MatCaveParser.class);
	private static final String TAG_MATERIALS = "materials";
	private static final String TAG_MATERIAL_SETS = "materialsets";
	private static final String TAG_MATERIAL_SET = "materialset";
	private static final String TAG_MATERIAL = "material";
	private static final String TAG_NAME = "name";
	private static final String TAG_BOYANCY_COMPENSATOR = "buoyancycompensator";
	private static final String TAG_HOOD = "hood";
	private Element rootElement;

	private MatCaveParser(Element rootElement) {
		this.rootElement = rootElement;
	}

	public static MatCaveParser createParser(Element rootElement) {
		return new MatCaveParser(rootElement);
	}

	private Element createMaterialElement(Material material) {
		Element materialElement = null;
		switch (material.getMaterialType()) {
		case DIVE_TANK:
			materialElement = createDiveTankMaterialElement((DiveTankMaterial) material);
			break;
		case FINS:
			materialElement = createFinsMaterialElement((FinsMaterial) material);
			break;
		case MASK:
			materialElement = createMaskMaterialElement((MaskMaterial) material);
			break;
		case WEIGHT_BELT:
			materialElement = createWeightBeltMaterialElement((WeightBeltMaterial) material);
			break;
		case DIVE_COMPUTER:
			materialElement = createDiveComputerMaterialElement((DiveComputerMaterial) material);
			break;
		case SUIT:
			materialElement = createSuitMaterialElement((SuitMaterial) material);
			break;
		case BUOYANCY_COMPENSATOR:
		case GLOVES:
		case BOOTS:
		case HOOD:
			materialElement = createDefaultSizableMaterialElement((DefaultSizeableMaterial) material);
			break;
		default:
			materialElement = createDefaultMaterialElement((DefaultMaterial) material);
			break;
		}

		createCommonMaterialAttributes(materialElement, material);

		return materialElement;
	}

	private Element createDefaultSizableMaterialElement(
			DefaultSizeableMaterial material) {
		String name = null;
		switch (material.getMaterialType()) {
		case BOOTS:
			name = "boots";
			break;
		case GLOVES:
			name = "gloves";
			break;
		case BUOYANCY_COMPENSATOR:
			name = TAG_BOYANCY_COMPENSATOR;
			break;
		case HOOD:
			name = TAG_HOOD;
			break;
		}
		if (null == name)
			return null;

		Element el = new Element(name);
		if (material.getSize() != null && material.getSize().length() > 0) {
			Element sizeEl = new Element("size");
			sizeEl.setText(material.getSize());
			el.addContent(sizeEl);
		}

		return el;
	}

	private Element createSuitMaterialElement(SuitMaterial material) {
		Element el = new Element("suit");

		if (null != material.getSize() && material.getSize().length() > 0)
			el.addContent(new Element("size").setText(material.getSize()));

		el.addContent(new Element("type").setText(String.valueOf(material
				.getSuitType().getId())));

		return el;
	}

	private Element createDiveComputerMaterialElement(
			DiveComputerMaterial material) {
		Element el = new Element("divecomputer");
		if (null != material.getSerialNumber()
				&& material.getSerialNumber().length() > 0)
			el.addContent(new Element("serialnumber").setText(material
					.getSerialNumber()));

		// if (null != material.getDiveComputerType()) {
		// el.addContent(new Element("type").setText(material
		// .getDiveComputerType().getKey()));
		// }

		return el;
	}

	private Element createWeightBeltMaterialElement(
			WeightBeltMaterial weightBeltMaterial) {
		Element el = new Element("weightbelt");
		return el;
	}

	private Element createMaskMaterialElement(MaskMaterial mask) {
		Element el = new Element("mask");
		if (null != mask.getSize() && mask.getSize().length() > 0)
			el.addContent(new Element("size").setText(mask.getSize()));
		return el;
	}

	private Element createFinsMaterialElement(FinsMaterial fins) {
		Element el = new Element("fins");
		if (null != fins.getSize() && fins.getSize().length() > 0)
			el.addContent(new Element("size").setText(fins.getSize()));
		return el;
	}

	private Element createDiveTankMaterialElement(DiveTankMaterial material) {
		Element el = new Element("divetank");
		el.addContent(new Element("volume").setText(String.valueOf(material
				.getVolume())));
		if (material.getComposite() != null) {
			el.addContent(new Element("composite").setText(String
					.valueOf(material.getComposite().getId())));
		}
		el.addContent(new Element("maxpressure").setText(String
				.valueOf(material.getMaxPressure())));
		return el;
	}

	private Element createDefaultMaterialElement(DefaultMaterial material) {
		String name = null;
		switch (material.getMaterialType()) {
		case COMPASS:
			name = "compass";
			break;
		case KNIFE:
			name = "knife";
			break;
		case LIGHT:
			name = "light";
			break;
		case MANOMETER:
			name = "manometer";
			break;
		case SCOOTER:
			name = "scooter";
			break;
		case SNORKEL:
			name = "snorkel";
			break;
		case OTHER:
			name = "other";
			break;
		case REGULATOR:
			name = "regulator";
			break;
		case WATCH:
			name = "watch";
			break;
		case REBREATHER:
			name = "rebreather";
			break;
		}

		if (null == name)
			return null;

		Element el = new Element(name);
		return el;
	}

	private void createCommonMaterialAttributes(Element el, Material material) {
		el.setAttribute("id", String.valueOf(material.getId()));
		el.setAttribute("active", String.valueOf(material.isActive()));

		if (null != material.getManufacturer()
				&& material.getManufacturer().length() > 0)
			el.addContent(new Element("manufacturer").setText(material
					.getManufacturer()));

		if (null != material.getModel() && material.getModel().length() > 0)
			el.addContent(new Element("model").setText(material.getModel()));

		if (null != material.getComment() && material.getComment().length() > 0)
			el.addContent(new Element("comment").setText(material.getComment()));

		if (null != material.getPurchaseDate()) {
			el.addContent(new Element("purchasedate").setText(FORMATTER_DATE
					.format(material.getPurchaseDate())));
		}

		el.addContent(new Element("purchaseprice").setText(String
				.valueOf(material.getPurchasePrice())));

	}

	private List<Material> readMaterialElements() {
		List<Material> materials = new ArrayList<Material>();
		Element materialsElement = rootElement.getChild(TAG_MATERIALS);
		if (materialsElement == null)
			return materials;

		List ms = materialsElement.getChildren();
		if (ms != null) {
			for (Iterator iterator = ms.iterator(); iterator.hasNext();) {
				Element matElement = (Element) iterator.next();
				Material material = readMaterialElement(matElement);
				materials.add(material);
			}
		}
		return materials;
	}

	private Material readMaterialElement(Element matElement) {
		Material material = null;
		if (matElement.getName().equals("divetank")) {
			material = readDiveTankElement(matElement);
		} else if (matElement.getName().equals("fins")) {
			material = readFinsElement(matElement);
		} else if (matElement.getName().equals("mask")) {
			material = readMaskElement(matElement);
		} else if (matElement.getName().equals("weightbelt")) {
			material = readWeightBeltElement(matElement);
		} else if (matElement.getName().equals("divecomputer")) {
			material = readDiveComputerElement(matElement);
		} else if (matElement.getName().equals("suit")) {
			material = readSuitElement(matElement);
		} else {
			material = readGenericMaterialElement(matElement);
		}

		readCommonMaterialAttributes(material, matElement);
		return material;
	}

	private Material readSuitElement(Element matElement) {
		SuitMaterial mat = new SuitMaterial();
		if (null != matElement.getChild("size")) {
			mat.setSize(matElement.getChildText("size"));
		}

		if (null != matElement.getChild("type")) {
			mat.setSuitType(SuitType.getSuitType(Short.valueOf(matElement
					.getChildText("type"))));
		}

		return mat;
	}

	private Material readGenericMaterialElement(Element matElement) {
		DefaultSizeableMaterial mat = null;
		String name = matElement.getName();
		if (name.equals("gloves"))
			mat = new DefaultSizeableMaterial(MaterialType.GLOVES);
		else if (name.equals("boots"))
			mat = new DefaultSizeableMaterial(MaterialType.BOOTS);
		else if (name.equals(TAG_BOYANCY_COMPENSATOR))
			mat = new DefaultSizeableMaterial(MaterialType.BUOYANCY_COMPENSATOR);
		else if (name.equals(TAG_HOOD))
			mat = new DefaultSizeableMaterial(MaterialType.HOOD);

		if (mat != null) {
			mat.setSize(matElement.getChildText("size"));
			return mat;
		}

		// if not Sizable Mat
		if (name.equals("compass"))
			return new DefaultMaterial(MaterialType.COMPASS);
		else if (name.equals("watch"))
			return new DefaultMaterial(MaterialType.WATCH);
		else if (name.equals("knife"))
			return new DefaultMaterial(MaterialType.KNIFE);
		else if (name.equals("light"))
			return new DefaultMaterial(MaterialType.LIGHT);
		else if (name.equals("manometer"))
			return new DefaultMaterial(MaterialType.MANOMETER);
		else if (name.equals("regulator"))
			return new DefaultMaterial(MaterialType.REGULATOR);
		else if (name.equals("scooter"))
			return new DefaultMaterial(MaterialType.SCOOTER);
		else if (name.equals("snorkel"))
			return new DefaultMaterial(MaterialType.SNORKEL);
		else if (name.equals("rebreather"))
			return new DefaultMaterial(MaterialType.REBREATHER);
		else if (name.equals("other"))
			return new DefaultMaterial(MaterialType.OTHER);

		return new DefaultMaterial(MaterialType.OTHER);
	}

	private void readCommonMaterialAttributes(Material material,
			Element matElement) {

		material.setId(Long.valueOf(matElement.getAttributeValue("id")));

		if (null != matElement.getAttribute("active")) {
			material.setActive(Boolean.valueOf(matElement
					.getAttributeValue("active")));
		}

		if (matElement.getChild("vendor") != null) {
			material.setVendor(matElement.getChildText("vendor"));
		}
		if (matElement.getChild("manufacturer") != null) {
			material.setManufacturer(matElement.getChildText("manufacturer"));
		}
		if (matElement.getChild("model") != null) {
			material.setModel(matElement.getChildText("model"));
		}
		if (matElement.getChild("comment") != null) {
			material.setComment(matElement.getChildText("comment"));
		}
		if (matElement.getChild("purchasedate") != null) {
			try {
				material.setPurchaseDate(FORMATTER_DATE.parse(matElement
						.getChildText("purchasedate")));
			} catch (ParseException e) {
				LOGGER.error("Can't read purchase date for material "
						+ material.getId());
			}
		}

		if (matElement.getChild("purchaseprice") != null) {
			material.setPurchasePrice(Double.valueOf(matElement
					.getChildText("purchaseprice")));
		}

	}

	private Material readFinsElement(Element matElement) {
		FinsMaterial fins = new FinsMaterial();
		if (matElement.getChild("size") != null) {
			fins.setSize(matElement.getChildText("size"));
		}
		return fins;
	}

	private Material readMaskElement(Element matElement) {
		MaskMaterial mask = new MaskMaterial();
		if (matElement.getChild("size") != null) {
			mask.setSize(matElement.getChildText("size"));
		}
		return mask;
	}

	private Material readDiveComputerElement(Element matElement) {
		DiveComputerMaterial mat = new DiveComputerMaterial();

		if (matElement.getChild("serialnumber") != null) {
			mat.setSerialNumber(matElement.getChild("serialnumber").getText());
		}

		// if (matElement.getChild("type") != null) {
		// mat.setDiveComputerType(DiveComputerType
		// .getDiveComputerType(matElement.getChild("type").getText()));
		// }

		return mat;
	}

	private Material readWeightBeltElement(Element matElement) {
		WeightBeltMaterial wbm = new WeightBeltMaterial();
		return wbm;
	}

	private Material readDiveTankElement(Element matElement) {
		DiveTankMaterial dtm = new DiveTankMaterial();
		if (null != matElement.getChild("volume")) {
			dtm.setVolume(Double.parseDouble(matElement.getChildText("volume")));
		}

		if (null != matElement.getChild("maxpressure")) {
			dtm.setMaxPressure(Double.parseDouble(matElement
					.getChildText("maxpressure")));
		}

		if (null != matElement.getChild("composite")) {
			dtm.setComposite(DiveTankCompositeMaterial
					.getDiveTankCompositeMaterial(Short.parseShort(matElement
							.getChildText("composite"))));
		}

		return dtm;
	}

	private Set<MaterialSet> readMaterialSetsElements() {
		Set<MaterialSet> materialSets = new HashSet<MaterialSet>();
		if (rootElement == null) {
			return materialSets;
		}

		Element materialsetsElement = rootElement.getChild(TAG_MATERIAL_SETS);
		if (null == materialsetsElement) {
			return materialSets;
		}

		List<Material> materialList = readMaterialElements();
		for (Iterator iterator = materialsetsElement.getChildren(
				TAG_MATERIAL_SET).iterator(); iterator.hasNext();) {
			Element setEl = (Element) iterator.next();
			String name = setEl.getChildText("name");
			MaterialSet materialSet = new MaterialSet(name);
			materialSet.setId(Long.parseLong(setEl.getAttributeValue("id")));

			for (Iterator it = setEl.getChild(TAG_MATERIALS)
					.getChildren(TAG_MATERIAL).iterator(); it.hasNext();) {
				Element matEl = (Element) it.next();
				long id = Long.parseLong(matEl.getAttributeValue("id"));
				for (Material material : materialList) {
					if (material.getId() == id) {
						materialSet.addMaterial(material);
						break;
					}
				}
			}
			materialSets.add(materialSet);
		}
		return materialSets;
	}

	private Element createMaterialSetElement(MaterialSet materialset) {
		Element materialsetEl = new Element(TAG_MATERIAL_SET);
		materialsetEl.setAttribute(ATTRIBUTE_ID,
				String.valueOf(materialset.getId()));

		Element nameEl = new Element(TAG_NAME);
		nameEl.setText(materialset.getName());
		materialsetEl.addContent(nameEl);

		Element matsEl = new Element(TAG_MATERIALS);
		materialsetEl.addContent(matsEl);

		for (Material mat : materialset.getMaterials()) {
			matsEl.addContent(createMaterialForMaterialSetElement(mat));
		}
		return materialsetEl;
	}

	private Element createMaterialForMaterialSetElement(Material material) {
		return createMaterialForMaterialSetElement(material.getId());
	}

	private Element createMaterialForMaterialSetElement(long materialId) {
		Element matEl = new Element(TAG_MATERIAL);
		matEl.setAttribute(ATTRIBUTE_ID, String.valueOf(materialId));
		return matEl;
	}

	public List<Material> getMaterials() {
		if (null == rootElement)
			return null;
		return readMaterialElements();

		// Element matcaveEl = rootElement.getChild("materialcave");
		// if (null != matcaveEl) {
		// Element materialsEl = matcaveEl.getChild("materials");
		// if (materialsEl != null) {
		// lb.getMatCave().setMaterials(
		// p.readMaterialElements(materialsEl));
		//
		// if (matcaveEl.getChild(TAG_MATERIALSETS) != null) {
		// Set<MaterialSet> sets = p.readMaterialSetsElements(
		// matcaveEl.getChild(TAG_MATERIALSETS), lb
		// .getMatCave().getAllMaterials());
		// lb.getMatCave().setMaterialSets(sets);
		// }
		// }
		// }
	}

	public Set<MaterialSet> getMatialSets() {
		return readMaterialSetsElements();
	}

	public void setMaterials(List<Material> materials) {
		Element el = rootElement.getChild(TAG_MATERIALS);
		if (null != el) {
			el.detach();
		}

		if (materials != null && materials.size() > 0) {
			el = new Element(TAG_MATERIALS);
			for (Material material : materials) {
				el.addContent(createMaterialElement(material));
			}
			rootElement.addContent(el);
		}
	}

	public void setMaterialSets(Set<MaterialSet> materialSets) {
		Element el = rootElement.getChild(TAG_MATERIAL_SETS);
		if (el != null) {
			el.detach();
		}

		if (materialSets != null && materialSets.size() > 0) {
			el = new Element(TAG_MATERIAL_SETS);
			for (MaterialSet materialSet : materialSets) {
				el.addContent(createMaterialSetElement(materialSet));
			}
			rootElement.addContent(el);
		}
	}

	@Override
	protected Element getRootElement() {
		return rootElement;
	}

	public void addMaterial(Material material) {
		createNodeIfNotExist(new String[] { TAG_MATERIALS });
		Element materialsElement = rootElement.getChild(TAG_MATERIALS);
		if (material.getId() > -1) {
			detachElWithIdFromParent(rootElement.getChild(TAG_MATERIALS),
					material.getId());
		}
		materialsElement.addContent(createMaterialElement(material));
	}

	public void removeMaterial(long materialId) {
		if (null != rootElement) {
			// Remove the material
			Element matsEl = rootElement.getChild(TAG_MATERIALS);
			detachElWithIdFromParent(matsEl, materialId);

			// remove all materials in the materialsets
			Element matsSetEl = rootElement.getChild(TAG_MATERIAL_SETS);
			if (matsSetEl != null) {
				for (Iterator iterator = matsSetEl
						.getChildren(TAG_MATERIAL_SET).iterator(); iterator
						.hasNext();) {
					Element matSetEl = (Element) iterator.next();
					for (Iterator iterator2 = matSetEl
							.getChildren(TAG_MATERIAL).iterator(); iterator2
							.hasNext();) {
						Element matEl = (Element) iterator.next();
						String s = matEl.getAttributeValue(ATTRIBUTE_ID);
						if (null != s && Long.valueOf(s) == materialId) {
							matEl.detach();
						}
					}
				}
			}
		}
	}

	public void removeMaterialSet(long materialSetId) {
		if (null != rootElement) {
			Element matsEl = rootElement.getChild(TAG_MATERIAL_SETS);
			detachElWithIdFromParent(matsEl, materialSetId);
		}
	}

	public void addMaterialSet(MaterialSet materialset) {
		createNodeIfNotExist(new String[] { TAG_MATERIAL_SETS });
		Element materialSetsElement = rootElement.getChild(TAG_MATERIAL_SETS);
		if (materialset.getId() > -1) {
			detachElWithIdFromParent(materialSetsElement, materialset.getId());
		}
		materialSetsElement.addContent(createMaterialSetElement(materialset));
	}

	public void removeMaterialFromMaterialSet(long materialSetId,
			long materialId) {
		Element matsEl = rootElement.getChild(TAG_MATERIAL_SETS);

		for (Iterator iterator = matsEl.getChildren(TAG_MATERIAL_SET)
				.iterator(); iterator.hasNext();) {
			Element matSetEl = (Element) iterator.next();
			if (Long.valueOf(matSetEl.getAttribute(ATTRIBUTE_ID).getValue()) == materialSetId) {
				detachElWithIdFromParent(matSetEl, materialId);
				break;
			}
		}

	}

	public void addMaterialsIntoMaterialSet(long materialSetId,
			Material[] materials) {
		Element matsEl = rootElement.getChild(TAG_MATERIAL_SETS);
		Element materialsEl = null;

		for (Iterator iterator = matsEl.getChildren(TAG_MATERIAL_SET)
				.iterator(); iterator.hasNext();) {
			Element matSetEl = (Element) iterator.next();
			if (Long.valueOf(matSetEl.getAttribute(ATTRIBUTE_ID).getValue()) == materialSetId) {
				// 1. be sure there is a materials element in the matset tag
				materialsEl = matSetEl.getChild(TAG_MATERIALS);
				if (null == materialsEl) {
					materialsEl = new Element(TAG_MATERIALS);
					matsEl.addContent(materialsEl);
				} else {
					// 2. detach old reference to avoid double entries
					for (Material material : materials) {
						detachElWithIdFromParent(materialsEl, material.getId(),
								true);
					}
				}
			}
		}

		// 3. populate the mat set with the added materials
		for (Material mat : materials) {
			Element materialElement = createMaterialForMaterialSetElement(mat);
			materialsEl.addContent(materialElement);
		}
	}

	public Material findMaterialForId(long materialId) {
		if (null != rootElement) {
			Element materialsEl = rootElement.getChild(TAG_MATERIALS);
			for (Iterator iterator = materialsEl.getChildren().iterator(); iterator
					.hasNext();) {
				Element matEl = (Element) iterator.next();
				if (materialId == Long.parseLong(matEl
						.getAttributeValue(ATTRIBUTE_ID))) {
					return readMaterialElement(matEl);
				}
			}
		}
		return null;
	}

	public void replaceMaterialReference(long materialToKeepId,
			long materialToDeleteId) {
		// delete from the materials
		Element materialsEl = rootElement.getChild(TAG_MATERIALS);
		Element elToDetach = null;
		for (Iterator iterator = materialsEl.getChildren().iterator(); iterator
				.hasNext();) {
			Element matEl = (Element) iterator.next();
			if (materialToDeleteId == Long.parseLong(matEl
					.getAttributeValue(ATTRIBUTE_ID))) {
				elToDetach = matEl;
				break;
			}
		}
		if (null != elToDetach)
			elToDetach.detach();

		// replace in the materialsets
		Element materialSetsEl = rootElement.getChild(TAG_MATERIAL_SETS);
		for (Iterator iterator = materialSetsEl.getChildren(TAG_MATERIAL_SET)
				.iterator(); iterator.hasNext();) {
			boolean containsNewMaterial = false;
			Element matSetEl = (Element) iterator.next();
			elToDetach = null;
			for (Iterator iterator2 = matSetEl.getChild(TAG_MATERIALS)
					.getChildren(TAG_MATERIAL).iterator(); iterator2.hasNext();) {
				Element materialElement = (Element) iterator2.next();
				if (materialToDeleteId == Long.parseLong(materialElement
						.getAttributeValue(ATTRIBUTE_ID))) {
					elToDetach = materialElement;
				} else if (materialToKeepId == Long.parseLong(materialElement
						.getAttributeValue(ATTRIBUTE_ID))) {
					containsNewMaterial = true;
				}
			}

			// detach the oldMaterial
			if (null != elToDetach)
				elToDetach.detach();

			// create new entry if not yet contained
			if (!containsNewMaterial) {
				Element el = createMaterialForMaterialSetElement(materialToKeepId);
				matSetEl.getChild(TAG_MATERIALS).addContent(el);
			}
		}
	}

}
