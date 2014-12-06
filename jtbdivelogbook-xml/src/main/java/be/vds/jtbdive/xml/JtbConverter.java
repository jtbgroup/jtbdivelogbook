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
package be.vds.jtbdive.xml;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import be.vds.jtbdive.core.core.catalogs.Gaz;
import be.vds.jtbdive.core.core.divecomputer.parser.UwatecDiveComputerDataParser;
import be.vds.jtbdive.core.core.divecomputer.uwatec.UwatecConstants;

public class JtbConverter {

	public void convertBinaryData(InputStream is, OutputStream os) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String s = null;
		try {
			while (br.ready()) {
				s = br.readLine();
				if (s.contains("Uwatec")) {
					String newString = new String();
					newString = s.split("-")[0] + "-";
					newString += UwatecDiveComputerDataParser.class.getName();
					os.write(newString.getBytes());
				} else {
					os.write(s.getBytes());
				}
				os.write("\r\n".getBytes());
			}
			is.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void convertLogBook(InputStream is, OutputStream os) {
		try {
			SAXBuilder sb = new SAXBuilder();
			Document documentRead = sb.build(is);
			Element rootRead = documentRead.getRootElement();

			Document documentWrite = new Document();
			Element rootWrite = new Element("logbookapplication");
			documentWrite.setRootElement(rootWrite);

			convertDiveLocations(rootRead, rootWrite);
			convertDivers(rootRead, rootWrite);
			convertLogBooks(rootRead, rootWrite);

			addSurrogates(rootWrite);

			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat());
			outputter.output(documentWrite, os);
			os.close();

		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addSurrogates(Element rootWrite) {
		Element dEl = rootWrite.getChild("divers");
		int surrogateDiver = 0;
		for (Iterator<?> iterator = dEl.getChildren("diver").iterator(); iterator
				.hasNext();) {
			Element el = (Element) iterator.next();
			int id = Integer.parseInt(el.getAttributeValue("id"));
			if (id > surrogateDiver) {
				surrogateDiver = id;
			}
		}
		surrogateDiver++;

		Element dlEl = rootWrite.getChild("divelocations");
		int surrogateDiveLocation = 0;
		for (Iterator<?> iterator = dlEl.getChildren("divelocation").iterator(); iterator
				.hasNext();) {
			Element el = (Element) iterator.next();
			int id = Integer.parseInt(el.getAttributeValue("id"));
			if (id > surrogateDiveLocation) {
				surrogateDiveLocation = id;
			}
		}
		surrogateDiveLocation++;

		Element lbkEl = rootWrite.getChild("logbooks");
		int surrogateLogBook = 0;
		for (Iterator<?> iterator = lbkEl.getChildren("logbook").iterator(); iterator
				.hasNext();) {
			Element el = (Element) iterator.next();
			el.setAttribute("id", String.valueOf(surrogateLogBook));
			surrogateLogBook++;
		}

		Element lbsEl = rootWrite.getChild("logbooks");
		int surrogateDive = 0;
		for (Iterator<?> iterator = lbsEl.getChildren("logbook").iterator(); iterator
				.hasNext();) {
			Element lbEl = (Element) iterator.next();
			for (Iterator<?> iterator2 = lbEl.getChild("dives")
					.getChildren("dive").iterator(); iterator2.hasNext();) {
				Element diveEl = (Element) iterator2.next();
				diveEl.setAttribute("id", String.valueOf(surrogateDive));
				surrogateDive++;
			}
		}

		Element surrogatesEl = new Element("surrogates");
		Element diverEl = new Element("diver");
		diverEl.setText(String.valueOf(surrogateDiver));
		Element diveLocEl = new Element("divelocation");
		diveLocEl.setText(String.valueOf(surrogateDiveLocation));
		Element logBookEl = new Element("logbook");
		logBookEl.setText(String.valueOf(surrogateLogBook));
		Element diveEl = new Element("dive");
		diveEl.setText(String.valueOf(surrogateDive));

		surrogatesEl.addContent(diverEl);
		surrogatesEl.addContent(diveLocEl);
		surrogatesEl.addContent(logBookEl);
		surrogatesEl.addContent(diveEl);
		rootWrite.addContent(0, surrogatesEl);
	}

	private void convertDiveLocations(Element rootRead, Element rootWrite) {
		Element dlEl = new Element("divelocations");
		dlEl.addContent(rootRead.getChild("divelocations").cloneContent());

		for (Iterator<?> iterator = dlEl.getChildren("divelocation").iterator(); iterator
				.hasNext();) {
			Element dlocEl = (Element) iterator.next();
			Element addressEl = new Element("address");

			Element countryCodeEl = new Element("countrycode");
			countryCodeEl.setText(dlocEl.getChildText("countrycode"));
			addressEl.addContent(countryCodeEl);
			dlocEl.removeContent(dlocEl.getChild("countrycode"));

			if (null != dlocEl.getChild("address")) {
				Element streetEl = new Element("street");
				Element toCopy = dlocEl.getChild("address");
				dlocEl.removeContent(toCopy);
				streetEl.setText(toCopy.getText());
				addressEl.addContent(streetEl);
			}
			dlocEl.addContent(addressEl);
		}
		rootWrite.addContent(dlEl);
	}

	private void convertDivers(Element rootRead, Element rootWrite) {
		Element dlEl = new Element("divers");
		dlEl.addContent(rootRead.getChild("divers").cloneContent());
		rootWrite.addContent(dlEl);
	}

	private void convertLogBooks(Element rootRead, Element rootWrite) {
		Element dlEl = new Element("logbooks");
		Element logbookEl = new Element("logbook");
		logbookEl.addContent(rootRead.cloneContent());
		logbookEl.removeChild("divers");
		logbookEl.removeChild("divelocations");
		dlEl.addContent(logbookEl);
		rootWrite.addContent(dlEl);

		for (Iterator<?> iterator = rootWrite.getChild("logbooks")
				.getChildren("logbook").iterator(); iterator.hasNext();) {
			Element logBEl = (Element) iterator.next();

			for (Iterator<?> iterator2 = logBEl.getChildren("dives").iterator(); iterator2
					.hasNext();) {
				Element elDives = (Element) iterator2.next();
				for (Iterator<?> iterator3 = elDives.getChildren("dive")
						.iterator(); iterator3.hasNext();) {
					Element elDive = (Element) iterator3.next();

					convertDivers(elDive);
					convertDiveProfile(elDive);
					convertDiveLocation(elDive);
					convertPhysiologicalStatus(elDive);
					convertAltitude(elDive);
					convertEquipment(elDive);

					elDive.removeChild("divetype");
				}
			}
		}
	}

	private void convertEquipment(Element elDive) {
		Element equipmentEl = new Element("equipment");
		convertMaterial(elDive, equipmentEl);
		convertDiveTanks(elDive, equipmentEl);
	}

	private void convertMaterial(Element elDive, Element equipmentEl) {
		Element computerEl = elDive.getChild("diveComputer");
		if (null != computerEl) {
			Element newComputerEl = new Element("divecomputer");
			newComputerEl.addContent(computerEl.cloneContent());
			equipmentEl.addContent(newComputerEl);
		}
		elDive.removeChild("diveComputer");
	}

	private void convertDiveTanks(Element elDive, Element equipmentEl) {
		Element originalTanksEl = elDive.getChild("divetanks");

		if (null != originalTanksEl) {
			Element newTanksEl = new Element("divetanks");
			for (Iterator<Element> iterator = originalTanksEl.getChildren(
					"divetank").iterator(); iterator.hasNext();) {
				Element tankEl = iterator.next();
				Element newTankEl = new Element("divetank");

				Element minEl = new Element("min");
				minEl.setText(tankEl.getAttributeValue("min"));
				Element maxEl = new Element("max");
				maxEl.setText(tankEl.getAttributeValue("max"));
				Element volumeEl = new Element("volume");
				volumeEl.setText(tankEl.getAttributeValue("capacity"));

				Element gazesEl = new Element("gazes");
				Element oxygenEl = new Element("gaz");
				oxygenEl.setAttribute("type", String.valueOf(Gaz.GAZ_OXYGEN));
				Element azoteEl = new Element("gaz");
				azoteEl.setAttribute("type", String.valueOf(Gaz.GAZ_NITROGEN));
				String perc = UwatecConstants.getNitroxMix(Integer
						.parseInt(tankEl.getAttributeValue("gaz")) - 1);

				oxygenEl.setText(perc);
				azoteEl.setText(String.valueOf(100 - Integer.parseInt(perc)));
				gazesEl.addContent(oxygenEl);
				gazesEl.addContent(azoteEl);

				newTankEl.addContent(minEl);
				newTankEl.addContent(maxEl);
				newTankEl.addContent(volumeEl);
				newTankEl.addContent(gazesEl);

				newTanksEl.addContent(newTankEl);
			}
			equipmentEl.addContent(newTanksEl);
		}
		elDive.addContent(equipmentEl);
		elDive.removeChild("divetanks");
		elDive.removeChild("nitroxmix");
	}

	private void convertAltitude(Element elDive) {
		Element altEl = elDive.getChild("altitude");
		if (null != altEl) {
			int i = Integer.parseInt(altEl.getText());
			switch (i) {
			case 0:
				i = 0;
				break;
			case 1:
				i = 0;
				break;
			case 2:
				i = 900;
				break;
			case 3:
				i = 1750;
				break;
			case 4:
				i = 2700;
				break;
			default:
				i = 0;
				break;
			}
			altEl.setText(String.valueOf(i));
		}
	}

	private void convertPhysiologicalStatus(Element elDive) {
		Element phisioEl = new Element("physiological");

		if (elDive.getChild("arterialmicrobubblelevel") != null) {
			Element el = new Element("arterialmicrobubblelevel");
			el.addContent(elDive.getChild("arterialmicrobubblelevel")
					.cloneContent());
			phisioEl.addContent(el);
			elDive.removeChild("arterialmicrobubblelevel");
		}

		if (elDive.getChild("skincoollevel") != null) {
			Element el = new Element("skincooltemperature");
			int max = Integer.parseInt(elDive.getChildText("skincoollevel"));
			float f = 0;
			switch (max) {
			case 0:
				f = 30.7f;
				break;
			case 1:
				f = 28.0f;
				break;
			case 2:
				f = 26.0f;
				break;
			case 3:
				f = 24.0f;
				break;
			case 4:
				f = 23.0f;
				break;
			case 5:
				f = 22.0f;
				break;
			case 6:
				f = 21.0f;
				break;
			case 7:
				f = 20.0f;
				break;
			}
			el.addContent(String.valueOf(f));
			phisioEl.addContent(el);
			elDive.removeChild("skincoollevel");
		}

		if (elDive.getChild("maxppo2") != null) {
			Element el = new Element("maxppo2");
			String max = elDive.getChildText("maxppo2");
			float f = Float.parseFloat((UwatecConstants.PPO2.get(Integer
					.parseInt(max))).replace(',', '.'));
			el.setText(String.valueOf(f));
			phisioEl.addContent(el);
			elDive.removeChild("maxppo2");
		}

		if (elDive.getChild("saturationindex") != null) {
			Element el = new Element("saturationindex");
			el.setText(elDive.getChildText("saturationindex"));
			phisioEl.addContent(el);
			elDive.removeChild("saturationindex");
		}

		if (elDive.getChild("interpulmonaryshunt") != null) {
			Element el = new Element("interpulmonaryshunt");
			el.setText(elDive.getChildText("interpulmonaryshunt"));
			phisioEl.addContent(el);
			elDive.removeChild("interpulmonaryshunt");
		}

		if (elDive.getChild("cnsbeforedive") != null) {
			Element el = new Element("cnsbeforedive");
			el.setText(elDive.getChildText("cnsbeforedive"));
			phisioEl.addContent(el);
			elDive.removeChild("cnsbeforedive");
		}

		if (phisioEl.getContentSize() > 0) {
			elDive.addContent(phisioEl);
		}
	}

	private void convertDiveLocation(Element elDive) {
		// Change de tag location by the tag divelocation
		Element locationEl = elDive.getChild("location");
		if (null != locationEl) {
			Element diveLocation = new Element("divelocation");
			diveLocation.setContent(locationEl.cloneContent());
			elDive.addContent(diveLocation);
			elDive.removeContent(locationEl);
		}
	}

	private void convertDiveProfile(Element elDive) {
		Element profileEl = new Element("diveprofile");

		if (null != elDive.getChild("depthentries")) {
			Element dE = new Element("depthentries");
			dE.addContent(elDive.getChild("depthentries").cloneContent());
			convertDepthEntries(dE);
			profileEl.addContent(dE);
		}

		if (null != elDive.getChild("warningentries")) {
			Element dE = new Element("decowarnings");
			dE.addContent(elDive.getChild("warningentries").cloneContent());
			convertDecoWarning(dE);
			profileEl.addContent(dE);

			Element aw = new Element("ascentwarnings");
			aw.addContent(elDive.getChild("warningentries").cloneContent());
			convertAscentTooFastWarning(aw);
			profileEl.addContent(aw);
		}

		if (null != elDive.getChild("decoentry")) {
			Element dE = new Element("decoentry");
			dE.addContent(elDive.getChild("decoentry").cloneContent());
			profileEl.addContent(dE);
		}

		elDive.removeChild("depthentries");
		elDive.removeChild("warningentries");
		elDive.removeChild("decoentry");

		if (profileEl.getChildren().size() > 0) {
			elDive.addContent(profileEl);
		}

	}

	private void convertAscentTooFastWarning(Element de) {
		String[] s = de.getTextTrim().split(";");
		de.removeContent();

		StringBuffer sb = new StringBuffer();
		int sec = 20;
		for (int i = 0; i < s.length; i++) {
			int warn = Integer.parseInt(s[i]);
			if ((warn & 4) == 4) {
				sb.append(sec).append(";");
			}
			sec += 20;
		}

		String result = sb.toString();
		if (result.endsWith(";")) {
			result = result.substring(0, result.lastIndexOf(';') - 1);
		}
		de.setText(result);
	}

	private void convertDecoWarning(Element de) {
		String[] s = de.getTextTrim().split(";");
		de.removeContent();

		StringBuffer sb = new StringBuffer();
		int sec = 20;
		for (int i = 0; i < s.length; i++) {
			int warn = Integer.parseInt(s[i]);
			if ((warn & 1) == 1) {
				sb.append(sec).append(";");
			}
			sec += 20;
		}

		String result = sb.toString();
		if (result.endsWith(";")) {
			result = result.substring(0, result.lastIndexOf(';') - 1);
		}
		de.setText(result);
	}

	private void convertDepthEntries(Element de) {
		String[] s = de.getTextTrim().split(";");
		de.removeContent();
		StringBuffer sb = new StringBuffer();
		int sec = 20;
		for (int i = 0; i < s.length; i++) {
			sb.append(sec).append(":").append(s[i]);
			if (i != s.length - 1) {
				sb.append(";");
			}
			sec += 20;
		}
		de.setText(sb.toString());
	}

	private void convertDivers(Element elDive) {
		if (null != elDive.getChild("divers")) {
			Element diversEl = elDive.getChild("divers");
			String divers = diversEl.getText();

			diversEl.removeContent();

			String[] diversArray = divers.split(";");
			if (null != divers) {
				for (int i = 0; i < diversArray.length; i++) {
					Element el = new Element("diver");
					Element id = new Element("id");
					id.setText(diversArray[i]);
					el.addContent(id);
					diversEl.addContent(el);
				}
			}
		}
	}

	public static void main(String[] args) {
		JtbConverter c = new JtbConverter();
		try {
			FileInputStream is = new FileInputStream("tester.jtb");
			FileOutputStream os = new FileOutputStream("result.jtb");
			c.convertLogBook(is, os);

			// FileInputStream is = new FileInputStream("dive-backup.txt");
			// FileOutputStream os = new FileOutputStream("dive-result.txt");
			// c.convertBinaryData(is, os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
