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

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;

import be.vds.jtbdive.core.core.catalogs.DiverRole;

public abstract class DiverRoleImageMapper {

	private static final String ROLE_SIMPLE = UIAgent.ICON_SIMPLE_DIVER_16;
	private static final String ROLE_FIRST = UIAgent.ICON_DIVER_ROLE_FIRST_16;
	private static final String ROLE_SECOND = UIAgent.ICON_DIVER_ROLE_SECOND_16;
	private static final String ROLE_MEDICAL = UIAgent.ICON_DIVER_ROLE_MEDIC_16;
	private static final String ROLE_CAMERA = UIAgent.ICON_DIVER_ROLE_CAMERA_16;

	public static final Map<DiverRole, Icon> ICON_ROLES = new HashMap<DiverRole, Icon>();
	static {
		ICON_ROLES.put(DiverRole.ROLE_PALANQUEE_SIMPLE_DIVER, UIAgent
				.getInstance().getIcon(ROLE_SIMPLE));
		ICON_ROLES.put(DiverRole.ROLE_PALANQUEE_FIRST, UIAgent.getInstance()
				.getIcon(ROLE_FIRST));
		ICON_ROLES.put(DiverRole.ROLE_PALANQUEE_SECOND, UIAgent.getInstance()
				.getIcon(ROLE_SECOND));
		ICON_ROLES.put(DiverRole.ROLE_PALANQUEE_MEDICAL_SUPPORT, UIAgent
				.getInstance().getIcon(ROLE_MEDICAL));
		ICON_ROLES.put(DiverRole.ROLE_PALANQUEE_CAMERA, UIAgent.getInstance()
				.getIcon(ROLE_CAMERA));
	}

	public static final Map<DiverRole, Image> IMAGE_ROLES = new HashMap<DiverRole, Image>();
	static {
		IMAGE_ROLES.put(DiverRole.ROLE_PALANQUEE_SIMPLE_DIVER, UIAgent
				.getInstance().getBufferedImage(ROLE_SIMPLE));
		IMAGE_ROLES.put(DiverRole.ROLE_PALANQUEE_FIRST, UIAgent.getInstance()
				.getBufferedImage(ROLE_FIRST));
		IMAGE_ROLES.put(DiverRole.ROLE_PALANQUEE_SECOND, UIAgent.getInstance()
				.getBufferedImage(ROLE_SECOND));
		IMAGE_ROLES.put(DiverRole.ROLE_PALANQUEE_MEDICAL_SUPPORT, UIAgent
				.getInstance().getBufferedImage(ROLE_MEDICAL));
		IMAGE_ROLES.put(DiverRole.ROLE_PALANQUEE_CAMERA, UIAgent.getInstance()
				.getBufferedImage(ROLE_CAMERA));
	}

}
