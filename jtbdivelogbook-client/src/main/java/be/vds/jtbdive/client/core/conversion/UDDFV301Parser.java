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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.GazMix;
import be.vds.jtbdive.core.core.material.DiveTankEquipment;
import be.vds.jtbdive.core.core.material.DiveTankMaterial;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.units.PressureUnit;
import be.vds.jtbdive.core.core.units.VolumeUnit;
import be.vds.jtbdive.core.exceptions.XMLValidationException;

public class UDDFV301Parser extends UDDFV3xParser {

	@Override
	public String getUDDFVersion() {
		return "3.0.1";
	}

	@Override
	public void write(List<Dive> dives, Diver owner, OutputStream outputStream,
			InputStream xsdInputStream) throws IOException,
			XMLValidationException {
		super.write(dives, owner, outputStream, xsdInputStream);
	}

	protected Element createTagDiveSite(List<DiveSite> diveSites) {
		Element el = new Element("divesite");

		for (DiveSite diveSite : diveSites) {
			Element diveSiteEl = createTagSite(diveSite);
			el.addContent(diveSiteEl);
		}
		return el;
	}

	@Override
	protected Element createTagDiveTank(DiveTankMaterial material, String id) {
		Element el = super.createTagDiveTank(material, id);
		return el;
	}

	@Override
	protected Element createTagDive(Dive dive, long seconds,
			Map<String, GazMix> gazMixes, Map<Material, String> matMap) {
		Element diveEl = new Element("dive");
		diveEl.setAttribute("id", "dive_" + dive.getId());

		// INFORMATION BEFORE DIVE
		diveEl.addContent(createTagInformationBeforeDive(dive, seconds));

		// SAMPLE
		if (dive.getDiveProfile() != null) {
			diveEl.addContent(createTagSamples(dive.getDiveProfile(),
					matchUsedGaz(dive, gazMixes)));
		}

		// TANK DATA
		if (dive.getDiveEquipment() == null) {
			diveEl.addContent(creatTagTankDataDummy());
		} else {
			List<DiveTankEquipment> tanks = dive.getDiveEquipment()
					.getDiveTanks();
			if (tanks == null || tanks.size() == 0) {
				diveEl.addContent(creatTagTankDataDummy());
			} else {
				for (DiveTankEquipment diveTankEquipment : tanks) {
					diveEl.addContent(creatTagTankData(diveTankEquipment,
							matMap, gazMixes));
				}
			}
		}

		// INFORMATION AFTER DIVE
		diveEl.addContent(createTagInformationAfterDive(dive, matMap));

		return diveEl;
	}

	private Element creatTagTankData(DiveTankEquipment tankEquipement,
			Map<Material, String> matMap, Map<String, GazMix> gazMixes) {

		Element tankDataEl = new Element("tankdata");
		if (tankEquipement.getGazMix() != null) {
			Element linkGazEl = new Element("link");
			String ref = pickGazMixRef(tankEquipement.getGazMix(), gazMixes);
			linkGazEl.setAttribute("ref", ref == null ? "" : ref);
			tankDataEl.addContent(linkGazEl);
		}

		if (tankEquipement.getMaterial() != null) {
			DiveTankMaterial diveTankMat = (DiveTankMaterial) tankEquipement
					.getMaterial();
			Element linkMatEl = new Element("link");
			linkMatEl.setAttribute("ref", matMap.get(diveTankMat));
			tankDataEl.addContent(linkMatEl);

			// This tag is nt necessary as there is already a reference to the
			// tank equipment, but by putting this information again, we fit the
			// UDF standard in a better way. This can be necessary if an
			// application doesn't use the "ref" tag.
			Element tankVolEl = new Element("tankvolume");
			tankVolEl.setText(formatDouble(UnitsAgent.getInstance()
					.convertVolumeFromModel(diveTankMat.getVolume(),
							VolumeUnit.CUBIC_METER)));
			tankDataEl.addContent(tankVolEl);
		}

		Element pressureBeginEl = new Element("tankpressurebegin");
		pressureBeginEl.setText(formatDouble(UnitsAgent.getInstance()
				.convertPressureFromModel(tankEquipement.getBeginPressure(),
						PressureUnit.PASCAL)));
		tankDataEl.addContent(pressureBeginEl);

		Element pressureEndEl = new Element("tankpressureend");
		pressureEndEl.setText(formatDouble(UnitsAgent.getInstance()
				.convertPressureFromModel(tankEquipement.getEndPressure(),
						PressureUnit.PASCAL)));
		tankDataEl.addContent(pressureEndEl);

		return tankDataEl;
	}

	private String pickGazMixRef(GazMix gazMix, Map<String, GazMix> gazMixes) {
		for (String key : gazMixes.keySet()) {
			if (gazMixes.get(key).hasSameComposition(gazMix))
				return key;
		}
		return null;
	}

	private Element creatTagTankDataDummy() {
		Element tankDataEl = new Element("tankdata");
		Element pressureBeg = new Element("tankpressurebegin");
		pressureBeg.setText("0");
		tankDataEl.addContent(pressureBeg);
		return tankDataEl;
	}

	protected List<Dive> readDives(Element root, Map<String, Diver> divers,
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
				setDiveTankInformation(dive, diveEl, materials, gazMixes);

				dives.add(dive);
			}
		}
		return dives;
	}

	protected void setDiveTankInformation(Dive dive, Element diveEl,
			Map<String, Material> materials, Map<String, GazMix> gazMixes) {
		List<Element> tankDataEls = diveEl.getChildren("tankdata");

		for (Element tankDataEl : tankDataEls) {
			List<Element> links = diveEl.getChildren("link");

			Material material = null;
			GazMix gazMix = null;

			for (Element linkEl : links) {
				if (material == null)
					material = materials.get(linkEl.getAttributeValue("ref"));

				if (gazMix == null)
					gazMix = gazMixes.get(linkEl.getAttributeValue("ref"));
			}

			DiveTankEquipment eq = null;
			if (null != material) {
				eq = (DiveTankEquipment) dive.getDiveEquipment()
						.getEquipmentForMaterial((Material) material);
			} else {
				eq = new DiveTankEquipment();
				dive.getDiveEquipment().addEquipment(eq);
			}

			String beginPressure = tankDataEl.getChildText("tankpressurebegin");
			if (null != beginPressure) {
				eq.setBeginPressure(UnitsAgent.getInstance()
						.convertVolumeToModel(
								Double.parseDouble(beginPressure),
								VolumeUnit.CUBIC_METER));
			}

			String endPressure = tankDataEl.getChildText("tankpressureend");
			if (null != endPressure) {
				eq.setEndPressure(UnitsAgent.getInstance()
						.convertVolumeToModel(
								Double.parseDouble(endPressure),
								VolumeUnit.CUBIC_METER));
			}
		}
	}

}
