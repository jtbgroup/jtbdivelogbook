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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import be.vds.jtbdive.core.core.DiveProfile;

public class DiveProfileParser {

	public static final String TAG_DIVEPROFILE = "diveprofile";
	private static final String TAG_TIME = "time";
	private static final String TAG_ENTRY = "entry";
	private static final String TAG_DEPTH = "depth";
	private static final String TAG_DECO_ENTRY = "deco.entry";
	private static final String TAG_WARNING_DECO_CEILING = "warning.deco.ceiling";
	private static final String TAG_WARNING_ASCENT_TOO_FAST = "warning.ascent.too.fast";
	private static final String TAG_WARNING_REMAINING_BOTTOM_TIME = "warning.remaining.bottom.time";

	public static DiveProfile readDiveProfile(InputStream is) {
		DiveProfile result = null;
		SAXBuilder sb = new SAXBuilder();
		try {
			Document document = sb.build(is);
			Element root = document.getRootElement();
			result = readDiveProfile(root);
			is.close();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	/**
	 * 
	 * <entry depth="3.5">20</entry>
	 * 
	 * @param root
	 * @return
	 */
	public static DiveProfile readDiveProfile(Element root) {

		Map<Double, Double> depthEntries = new HashMap<Double, Double>();

		Set<Double> decoEntries = new HashSet<Double>();

		Set<Double> decoWarns = new HashSet<Double>();
		Set<Double> ascWarn = new HashSet<Double>();
		Set<Double> bottomTimeWarn = new HashSet<Double>();

		Element element = null;
		for (Iterator<Element> iterator = root.getChildren(TAG_ENTRY).iterator(); iterator
				.hasNext();) {
			element = iterator.next();
			Double time = Double.valueOf(element.getAttributeValue(TAG_TIME));

			depthEntries.put(time, Double.valueOf(element
					.getChildText(TAG_DEPTH)));

			if (element.getChild(TAG_DECO_ENTRY) != null
					&& Boolean.valueOf(element.getChildText(TAG_DECO_ENTRY))) {
				decoEntries.add(time);
			}

			if (element.getChild(TAG_WARNING_DECO_CEILING) != null
					&& Boolean.valueOf(element.getChildText(TAG_WARNING_DECO_CEILING))) {
				decoWarns.add(time);
			}

			if (element.getChild(TAG_WARNING_ASCENT_TOO_FAST) != null
					&& Boolean.valueOf(element
							.getChildText(TAG_WARNING_ASCENT_TOO_FAST))) {
				ascWarn.add(time);
			}

			if (element.getChild(TAG_WARNING_REMAINING_BOTTOM_TIME) != null
					&& Boolean.valueOf(element
							.getChildText(TAG_WARNING_REMAINING_BOTTOM_TIME))) {
				bottomTimeWarn.add(time);
			}
		}

		DiveProfile dp = null;
		if (depthEntries.size() > 0) {
			dp = new DiveProfile();
			dp.setDepthEntries(depthEntries);
			dp.setDecoEntries(decoEntries);
			dp.setAscentWarnings(ascWarn);
			dp.setDecoCeilingWarnings(decoWarns);
			dp.setRemainingBottomTimeWarnings(bottomTimeWarn);
		}
		return dp;
	}

	// public static String createFakeprofile() {
	// StringBuilder sb = new StringBuilder();
	// sb.append("<entries>");
	// sb.append("<entry depth=\"3.5\">10</entry>");
	// sb.append("<entry depth=\"5\">20</entry>");
	// sb.append("<entry depth=\"7\">30</entry>");
	// sb.append("<entry depth=\"10\">40</entry>");
	// sb.append("<entry depth=\"15\">50</entry>");
	// sb.append("<entry depth=\"6\">60</entry>");
	// sb.append("<entry depth=\"2.6\">70</entry>");
	// sb.append("<entry depth=\"1.3\">80</entry>");
	// sb.append("</entries>");
	// return sb.toString();
	// }

	public static String convertDiveProfile(DiveProfile diveProfile) {
		String result = null;
		Element root = createDiveProfileElement(diveProfile);
		Document document = new Document(root);

		Writer os = new StringWriter();
		XMLOutputter outputter = new XMLOutputter();
		// outputter.setFormat(Format.getPrettyFormat());
		try {
			outputter.output(document, os);
			result = os.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static Element createDiveProfileElement(
			DiveProfile diveProfile) {
		Element root = new Element(TAG_DIVEPROFILE);
		Map<Double, Double> diveProfileDepthEntries = diveProfile
				.getDepthEntries();
		Set<Double> decoEntries = diveProfile.getDecoEntries();

		Set<Double> decoWarns = diveProfile.getDecoCeilingWarnings();
		Set<Double> ascWarn = diveProfile.getAscentWarnings();
		Set<Double> bottomTimeWarn = diveProfile
				.getRemainingBottomTimeWarnings();
		Element el = null;
		for (Double time : diveProfileDepthEntries.keySet()) {
			el = new Element(TAG_ENTRY);
			el.setAttribute(TAG_TIME, String.valueOf(time));

			Element depthEl = new Element(TAG_DEPTH);
			depthEl.setText(String.valueOf(diveProfileDepthEntries.get(time)));
			el.addContent(depthEl);

			if (decoEntries.contains(time)) {
				Element warnEl = new Element(TAG_DECO_ENTRY);
				warnEl.setText("true");
				el.addContent(warnEl);
			}

			if (decoWarns.contains(time)) {
				Element warnEl = new Element(TAG_WARNING_DECO_CEILING);
				warnEl.setText("true");
				el.addContent(warnEl);
			}

			if (ascWarn.contains(time)) {
				Element warnEl = new Element(TAG_WARNING_ASCENT_TOO_FAST);
				warnEl.setText("true");
				el.addContent(warnEl);
			}

			if (bottomTimeWarn.contains(time)) {
				Element warnEl = new Element(TAG_WARNING_REMAINING_BOTTOM_TIME);
				warnEl.setText("true");
				el.addContent(warnEl);
			}

			root.addContent(el);
		}
		return root;
	}
}
