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
package be.vds.jtbdive.client.util;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtb.swing.component.Thumbnail;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DocumentContentLoader;
import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.client.core.comparator.KeyedCatalogComparator;
import be.vds.jtbdive.client.view.core.dive.profile.DiveProfileChartFactory;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveProfile;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Document;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.core.catalogs.KeyedCatalog;
import be.vds.jtbdive.core.core.units.LengthUnit;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;

/**
 * 
 * @author gautier
 */
public class ReportHelper {

	public static final String DIVES = "dives";
	public static final String LOCALE = "locale";
	public static final String INCLUDE_DIVE_DETAILS = "includeDiveDetails";
	public static final String INCLUDE_DIVE_DOCUMENT = "includeDiveDocuments";
	public static final String INCLUDE_DIVE_PROFILE = "includeDiveProfile";
	public static final String INCLUDE_DIVE_SITE = "includeDiveSites";
	public static final String INCLUDE_DIVE_SITE_DOCUMENT = "includeDiveSitesDocuments";
	public static final String INCLUDE_DIVE_PHYSIOLOGICAL_STATUS = "includePhysiologicalStatus";
	public static final String UNIT_LENGTH = "lengthUnit";
	public static final String UNIT_TEMPERATURE = "temperatureUnit";
	public static final String LOGBOOK = "logbook";
	public static final String REPORT_HELPER = "reportHelper";
	private static final Syslog LOGGER = Syslog.getLogger(ReportHelper.class);

	private final DocumentContentLoader diveDocumentContentLoader;
	private final DiveSiteManagerFacade diveSiteManagerFacade;

	public ReportHelper(DocumentContentLoader diveDocumentContentLoader,
			DiveSiteManagerFacade diveSiteManagerFacade) {
		this.diveDocumentContentLoader = diveDocumentContentLoader;
		this.diveSiteManagerFacade = diveSiteManagerFacade;
	}

	public Image getImageForDiveDocument(Document document, double height) {
		return getImageForDocument(document.getId(),
				document.getDocumentFormat(), height, diveDocumentContentLoader);
	}

	public Image getImageForDiveDocument(long documentId,
			DocumentFormat documentFormat, double height) {
		return getImageForDocument(documentId, documentFormat, height,
				diveDocumentContentLoader);
	}

	public Image getImageForDiveSiteDocument(long documentId,
			DocumentFormat documentFormat, double height) {
		return getImageForDocument(documentId, documentFormat, height,
				diveSiteManagerFacade);
	}

	private Image getImageForDocument(long documentId,
			DocumentFormat documentFormat, double height,
			DocumentContentLoader documentContentLoader) {
		try {
			ImageIcon icon = new ImageIcon(
					documentContentLoader.loadDocumentContent(documentId,
							documentFormat));
			Thumbnail t = new Thumbnail(icon);
			t.fitToHeight(height);
			return t.getImage();
		} catch (DataStoreException ex) {
			LOGGER.error(ex);
		}
		return null;
	}

	public Image getDiveProfileImage(DiveProfile diveProfile, int width,
			int height, Locale locale, LengthUnit lengthUnit) {
		Image picture = DiveProfileChartFactory.createDiveProfilePicture(
				diveProfile, width, height, locale, lengthUnit);
		return picture;
	}

	public List<DiveSite> getDiveSitesForDives(List<Dive> dives) {
		List<DiveSite> ds = LogBookUtilities.getDiveSites(dives);
		if (ds != null) {
			List<DiveSite> res = new ArrayList<DiveSite>();
			for (DiveSite diveSite : ds) {
				try {
					res.add(diveSiteManagerFacade.findDiveSiteById(
							diveSite.getId(), true));
				} catch (DataStoreException e) {
					LOGGER.error(e);
				}
			}
			return res;
		}
		return null;
	}

	/**
	 * returns a list of KeyedCatalogs ordered alphabetically according to the locale passed. 
	 * @param catalogEntries
	 * @param locale
	 * @return a String of values separated with comma (",") or an empty string.
	 */
	public String getFormattedKeyedCatalogs(List<KeyedCatalog> catalogEntries,
			Locale locale) {
		StringBuilder sb = new StringBuilder();
		if (catalogEntries == null) {
			return "";
		}

		List<KeyedCatalog> newList = new ArrayList<KeyedCatalog>(catalogEntries);
		Collections.sort(newList, new KeyedCatalogComparator(locale));
		for (KeyedCatalog catalogEntry : newList) {
			sb.append(
					I18nResourceManager.sharedInstance().getString(
							catalogEntry.getKey())).append(", ");
		}

		return sb.substring(0, sb.lastIndexOf(", "));

	}
}
