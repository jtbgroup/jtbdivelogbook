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
package be.vds.jtbdive.client.view.utils;

import javax.swing.Icon;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.view.core.logbook.material.edit.AbstractMaterialPanel;
import be.vds.jtbdive.client.view.core.logbook.material.edit.DefaultMaterialPanel;
import be.vds.jtbdive.client.view.core.logbook.material.edit.DefaultSizeableMaterialPanel;
import be.vds.jtbdive.client.view.core.logbook.material.edit.DiveComputerMaterialPanel;
import be.vds.jtbdive.client.view.core.logbook.material.edit.DiveTankMaterialPanel;
import be.vds.jtbdive.client.view.core.logbook.material.edit.FinsMaterialPanel;
import be.vds.jtbdive.client.view.core.logbook.material.edit.MaskMaterialPanel;
import be.vds.jtbdive.client.view.core.logbook.material.edit.SuitMaterialPanel;
import be.vds.jtbdive.client.view.core.logbook.material.edit.WeightBeltMaterialPanel;
import be.vds.jtbdive.core.core.GazMix;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.DefaultEquipment;
import be.vds.jtbdive.core.core.material.DefaultMaterial;
import be.vds.jtbdive.core.core.material.DefaultSizeableMaterial;
import be.vds.jtbdive.core.core.material.DiveComputerEquipment;
import be.vds.jtbdive.core.core.material.DiveComputerMaterial;
import be.vds.jtbdive.core.core.material.DiveTankEquipment;
import be.vds.jtbdive.core.core.material.DiveTankMaterial;
import be.vds.jtbdive.core.core.material.Equipment;
import be.vds.jtbdive.core.core.material.FinsEquipment;
import be.vds.jtbdive.core.core.material.FinsMaterial;
import be.vds.jtbdive.core.core.material.MaskEquipment;
import be.vds.jtbdive.core.core.material.MaskMaterial;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.SuitMaterial;
import be.vds.jtbdive.core.core.material.WeightBeltEquipment;
import be.vds.jtbdive.core.core.material.WeightBeltMaterial;

public class MaterialHelper {

	public static final int ICON_SIZE_16 = 16;
	public static final int ICON_SIZE_24 = 24;

	public static Equipment getEquipmentForMaterialType(
			MaterialType materialType) {
		Equipment eq = null;
		switch (materialType) {
		case DIVE_COMPUTER:
			eq = new DiveComputerEquipment();
			break;
		case DIVE_TANK:
			eq = createDiveTank();
			break;
		case WEIGHT_BELT:
			eq = new WeightBeltEquipment();
			break;
		case FINS:
			eq = new FinsEquipment();
			break;
		case MASK:
			eq = new MaskEquipment();
			break;
		default:
			eq = new DefaultEquipment(materialType);
			break;
		}

		return eq;
	}

	private static Equipment createDiveTank() {
		DiveTankEquipment dt = new DiveTankEquipment();
		dt.setGazMix(GazMix.getDefaultGazMix());
		return dt;
	}

	public static Icon getMaterialIcon(MaterialType materialType, int size) {
		if (size == 24) {
			return getMaterialIcon24(materialType);
		}

		return getMaterialIcon16(materialType);
	}

	private static Icon getMaterialIcon16(MaterialType materialType) {
		Icon icon = null;
		switch (materialType) {
		case DIVE_TANK:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_DIVETANK_16);
			break;
		case MASK:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_MASK_16);
			break;
		case FINS:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_FINS_16);
			break;
		case SNORKEL:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_SNORKEL_16);
			break;
		case WEIGHT_BELT:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_WEIGHT_BELT_16);
			break;
		case SCOOTER:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_SCOOTER_16);
			break;
		case WATCH:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_WATCH_16);
			break;
		case COMPASS:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_COMPASS_16);
			break;
		case KNIFE:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_KNIFE_16);
			break;
		case LIGHT:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_LIGHT_16);
			break;
		case DIVE_COMPUTER:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_DIVECOMPUTER_16);
			break;
		case SUIT:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_DRY_SUIT_16);
			break;
		case GLOVES:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_GLOVES_16);
			break;
		case BOOTS:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_BOOTS_16);
			break;
		case BUOYANCY_COMPENSATOR:
			icon = UIAgent.getInstance().getIcon(
					UIAgent.ICON_BUOYANCY_COMPENSATOR_16);
			break;
		case REGULATOR:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_REGULATOR_16);
			break;
		case REBREATHER:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_REBREATHER_16);
			break;
		case MANOMETER:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_MANOMETER_16);
			break;
		case HOOD:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_HOOD_16);
			break;
		}

		return icon;
	}

	private static Icon getMaterialIcon24(MaterialType materialType) {
		Icon icon = null;
		switch (materialType) {
		case DIVE_TANK:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_DIVETANK_24);
			break;
		case MASK:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_MASK_24);
			break;
		case FINS:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_FINS_24);
			break;
		case SNORKEL:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_SNORKEL_24);
			break;
		case WEIGHT_BELT:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_WEIGHT_BELT_24);
			break;
		case SCOOTER:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_SCOOTER_24);
			break;
		case WATCH:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_WATCH_24);
			break;
		case COMPASS:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_COMPASS_24);
			break;
		case KNIFE:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_KNIFE_24);
			break;
		case LIGHT:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_LIGHT_24);
			break;
		case DIVE_COMPUTER:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_DIVECOMPUTER_24);
			break;
		case SUIT:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_DRY_SUIT_24);
			break;
		case GLOVES:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_GLOVES_24);
			break;
		case BOOTS:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_BOOTS_24);
			break;
		case BUOYANCY_COMPENSATOR:
			icon = UIAgent.getInstance().getIcon(
					UIAgent.ICON_BUOYANCY_COMPENSATOR_24);
			break;
		case REGULATOR:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_REGULATOR_24);
			break;
		case REBREATHER:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_REBREATHER_24);
			break;
		case MANOMETER:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_MANOMETER_24);
			break;
		case HOOD:
			icon = UIAgent.getInstance().getIcon(UIAgent.ICON_HOOD_24);
			break;
		}

		return icon;
	}

	public static AbstractMaterialPanel createPanel(MaterialType materialType) {
		AbstractMaterialPanel p = null;
		switch (materialType) {
		case DIVE_TANK:
			p = new DiveTankMaterialPanel();
			break;
		case FINS:
			p = new FinsMaterialPanel();
			break;
		case MASK:
			p = new MaskMaterialPanel();
			break;
		case WEIGHT_BELT:
			p = new WeightBeltMaterialPanel();
			break;
		case DIVE_COMPUTER:
			p = new DiveComputerMaterialPanel();
			break;
		case SUIT:
			p = new SuitMaterialPanel();
			break;

		case GLOVES:
		case BOOTS:
		case BUOYANCY_COMPENSATOR:
		case HOOD:
			p = new DefaultSizeableMaterialPanel(materialType);
			break;
		default:
			p = new DefaultMaterialPanel(materialType);
			break;
		}
		return p;
	}

	public static Material createMaterialForMaterialType(
			MaterialType materialType) {
		switch (materialType) {
		case DIVE_COMPUTER:
			return new DiveComputerMaterial();
		case DIVE_TANK:
			return new DiveTankMaterial();
		case FINS:
			return new FinsMaterial();
		case MASK:
			return new MaskMaterial();
		case WEIGHT_BELT:
			return new WeightBeltMaterial();
		case SUIT:
			return new SuitMaterial();
		case GLOVES:
		case BOOTS:
		case BUOYANCY_COMPENSATOR:
		case HOOD:
			return new DefaultSizeableMaterial(materialType);
		default:
			return new DefaultMaterial(materialType);
		}
	}

	public static String getGeneralFieldsAsHTML(Material material) {
		I18nResourceManager i18n = I18nResourceManager.sharedInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body>");
		sb.append("<table border='0px' width='95%'><tr>");

		// MANUFACTURER
		sb.append("<td width='50%'><b><u>").append(i18n.getString("manufacturer"))
				.append("</u></b>");
		String manufacturer = material.getManufacturer();
		sb.append("&nbsp;<i>").append(manufacturer == null ? "" : manufacturer)
				.append("</i></td>");

		// PURCHASE DATE
		sb.append("<td><b><u>").append(i18n.getString("purchase.date"))
				.append("</u></b>");
		if (null != material.getPurchaseDate()) {
			sb.append(" euros<i>")
					.append(UIAgent.getInstance().getFormatDateHoursFull()
							.format(material.getPurchaseDate()))
					.append("</i></td>");
		}
		sb.append("</tr><tr>");

		// MODEL
		sb.append("<td><b><u>").append(i18n.getString("model"))
				.append("</u></b>");
		String model = material.getModel();
		sb.append("&nbsp;<i>").append(model == null ? "" : model)
				.append("</i></td>");

		// PURCHASE PRICE
		sb.append("<td><b><u>").append(i18n.getString("purchase.price"))
				.append("</u></b>");
		sb.append("&nbsp;<i>").append(material.getPurchasePrice())
				.append("euros</i></td>");
		sb.append("</tr><tr>");

		// VENDOR
		sb.append("<td><b><u>").append(i18n.getString("vendor"))
				.append("</u></b>");
		String vendor = material.getVendor();
		sb.append("&nbsp;<i>").append(vendor == null ? "" : vendor)
				.append("</i></td>");
		sb.append("</tr><tr>");

		// COMMENT
		sb.append("<td colspan='2'><b><u>").append(i18n.getString("comment"))
				.append("</u></b><br/>");
		String comment = material.getComment();
		sb.append("&nbsp;<i>").append(comment == null ? "" : comment)
				.append("</i></td>");

		sb.append("</tr>");
		sb.append("</table></body></html>");
		return sb.toString();
	}

	public static String getSpecificFieldsAsHTML(Material material) {
		switch (material.getMaterialType()) {
		case DIVE_COMPUTER:
			return getSpecificFieldsAsHTMLForDiveComputer((DiveComputerMaterial) material);
		case DIVE_TANK:
			return getSpecificFieldsAsHTMLForDiveTank((DiveTankMaterial) material);
		case FINS:
			return getSpecificFieldsAsHTMLForFins((FinsMaterial) material);
		case MASK:
			return getSpecificFieldsAsHTMLForMask((MaskMaterial) material);
		case SUIT:
			return getSpecificFieldsAsHTMLForSuit((SuitMaterial) material);
		case GLOVES:
		case BOOTS:
		case BUOYANCY_COMPENSATOR:
		case HOOD:
			return getSpecificFieldsAsHTMLForSizeableMaterial((DefaultSizeableMaterial) material);
		case WEIGHT_BELT:
		default:
			return null;
		}
	}

	private static String getSpecificFieldsAsHTMLForDiveTank(
			DiveTankMaterial material) {
		I18nResourceManager i18n = I18nResourceManager.sharedInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body>");
		sb.append("<table border='0px' width='95%'><tr>");
		// VOLUME
		sb.append("<td><b><u>").append(i18n.getString("volume"))
				.append("</u></b>");
		sb.append("&nbsp;<i>").append(material.getVolume())
				.append("L </i></td>");
		sb.append("</tr>");

		// PRESSURE
		sb.append("<tr><td><b><u>").append(i18n.getString("pressure"))
				.append("</u></b>");
		sb.append("&nbsp;<i>")
				.append(UnitsAgent.getInstance().convertPressureFromModel(
						material.getMaxPressure()))
				.append(UnitsAgent.getInstance().getPressureUnit().getSymbol())
				.append("</i></td>");
		sb.append("</tr>");

		// COMPOSITE
		sb.append("<tr><td><b><u>").append(i18n.getString("composite"))
				.append("</u></b>");
		if (null != material.getComposite()) {
			sb.append("&nbsp;<i>")
					.append(i18n.getString(material.getComposite().getKey()))
					.append("</i></td>");
		}
		sb.append("</tr>");

		sb.append("</table></body></html>");
		return sb.toString();
	}

	private static String getSpecificFieldsAsHTMLForFins(FinsMaterial material) {
		I18nResourceManager i18n = I18nResourceManager.sharedInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body>");
		sb.append("<table border='0px' width='95%'><tr>");
		// size
		sb.append("<td><b><u>").append(i18n.getString("size"))
				.append("</u></b>");
		if (null != material.getSize()) {
			sb.append("&nbsp;<i>").append(material.getSize())
					.append("</i></td>");
		}
		sb.append("</tr>");

		sb.append("</table></body></html>");
		return sb.toString();
	}

	private static String getSpecificFieldsAsHTMLForMask(MaskMaterial material) {
		I18nResourceManager i18n = I18nResourceManager.sharedInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body>");
		sb.append("<table border='0px' width='95%'><tr>");
		// size
		sb.append("<td><b><u>").append(i18n.getString("size"))
				.append("</u></b>");
		if (null != material.getSize()) {
			sb.append("&nbsp;<i>").append(material.getSize())
					.append("</i></td>");
		}
		// LEFT DIOPTRY
		sb.append("<td><b><u>").append(i18n.getString("dioptry.left"))
				.append("</u></b>");
		sb.append("&nbsp;<i>").append(material.getLeftDioptry())
				.append("</i></td>");
		// RIGHT DIOPTRY
		sb.append("<td><b><u>").append(i18n.getString("dioptry.right"))
				.append("</u></b>");
		sb.append("&nbsp;<i>").append(material.getRightDioptry())
				.append("</i></td>");
		sb.append("</tr>");

		sb.append("</table></body></html>");
		return sb.toString();
	}

	private static String getSpecificFieldsAsHTMLForSuit(SuitMaterial material) {
		I18nResourceManager i18n = I18nResourceManager.sharedInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body>");
		sb.append("<table border='0px' width='95%'><tr>");
		// size
		sb.append("<td><b><u>").append(i18n.getString("size"))
				.append("</u></b>");
		if (null != material.getSize()) {
			sb.append("&nbsp;<i>").append(material.getSize())
					.append("</i></td>");
		}
		sb.append("</tr>");

		sb.append("</table></body></html>");
		return sb.toString();
	}

	private static String getSpecificFieldsAsHTMLForSizeableMaterial(
			DefaultSizeableMaterial material) {
		I18nResourceManager i18n = I18nResourceManager.sharedInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body>");
		sb.append("<table border='0px' width='95%'><tr>");
		// size
		sb.append("<td><b><u>").append(i18n.getString("size"))
				.append("</u></b>");
		if (null != material.getSize()) {
			sb.append("&nbsp;<i>").append(material.getSize())
					.append("</i></td>");
		}
		sb.append("</tr>");

		sb.append("</table></body></html>");
		return sb.toString();
	}

	private static String getSpecificFieldsAsHTMLForDiveComputer(
			DiveComputerMaterial material) {
		I18nResourceManager i18n = I18nResourceManager.sharedInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body>");
		sb.append("<table border='0px' width='95%'><tr>");

		// SERIAL NUMBER
		sb.append("<td><b><u>").append(i18n.getString("serial.number"))
				.append("</u></b>");
		if (null != material.getSerialNumber()) {
			sb.append("&nbsp;<i>").append(material.getSerialNumber())
					.append("</i></td>");
		}
		sb.append("</tr>");

		sb.append("</table></body></html>");
		return sb.toString();
	}

}
