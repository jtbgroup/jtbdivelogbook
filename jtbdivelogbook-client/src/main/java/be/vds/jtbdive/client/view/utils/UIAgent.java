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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;

import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.util.ResourceManager;

public class UIAgent extends Observable implements Observer {
	// COLORS - Constants
	public static final String CHANGE_DATE_FORMAT_DAY_HOUR = "date.format.day.hour";
	public static final Object CHANGE_DATE_FORMAT_DAY = "date.format.day";

	private static final Color MEDIUM_BLUE = new Color(153, 149, 255);
	private static final Color MEDIUM_BLUE_ALPHA_125 = new Color(153, 149, 255,
			125);
	private static final Color DARK_GREY = new Color(5, 5, 5);
	private static final Color GREY = Color.GRAY;
	private static final Color MEDIUM_GREY = new Color(230, 230, 230);
	private static final Color LIGHT_GREY = new Color(250, 250, 250);
	private static final Color WHITE = Color.WHITE;
	private static final Color RED = Color.RED;
	private static final Color DARK_BLUE = new Color(1, 5, 75);
	private static final Color BLACK = Color.BLACK;

	// DATE FORMATTER
	private static final SimpleDateFormat DATE_HOURS_FORMATTER_EXPORT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat DATE_FORMATTER_SHORT = new SimpleDateFormat(
			"yyyy-MM-dd");
	private SimpleDateFormat dateFormatterDateHour = DATE_HOURS_FORMATTER_EXPORT;
	private SimpleDateFormat dateFormatterDate = DATE_FORMATTER_SHORT;
	// PRECISION FOR NUMBER VALUES
	public static final int PRECISION_DEPTH = 3;
	public static final int PRECISION_DIVE_TIME = 5;
	public static final int PRECISION_WATER_TEMP = 3;
	public static final char NUMBER_DECIMAL_CHAR = ',';
	// Scroll properties
	public static final int VERTICAL_UNIT_SCROLL = 30;
	// COLORS
	private static Map<String, Color> colors = new HashMap<String, Color>();
	private static final String COLOR_WATER_BOTTOM = "color.water.bottom";
	private static final String COLOR_WATER_SURFACE = "color.water.surface";
	private static final String COLOR_WATER_CROSSHAIR = "color.water.crosshair";
	private static final String COLOR_TITLE_PANEL = "color.title.panel";
	private static final String COLOR_TITLE_PANEL_FG = "color.title.panel.fg";
	private static final String COLOR_TITLE_PANEL_ALPHA = "color.title.panel.alpha";
	private static final String COLOR_CONSOLE_BKG = "color.console.bkg";
	private static final String COLOR_GAZ_OXYGEN = "color.gaz.oxygen";
	private static final String COLOR_GAZ_NITROGEN = "color.gaz.nitrogen";
	private static final String COLOR_GAZ_HELIUM = "color.gaz.helium";
	private static final String COLOR_GAZ_HYDROGEN = "color.gaz.hydrogen";
	private static final String COLOR_GAZ_ARGON = "color.gaz.argon";
	private static final String COLOR_GAZ_NEON = "color.gaz.neon";
	private static final String COLOR_GAZ_BACKGROUND = "color.gaz.background";
	private static final String COLOR_GAZ_BORDER = "color.gaz.border";
	private static final String COLOR_BKG_BASE = "color.background.base";
	private static final String COLOR_FG_BASE = "color.foreground.base";
	private static final String COLOR_WELCOME = "color.welcome";
	private static final String COLOR_WELCOME_ALPHA = "color.welcome.alpha";
	private static final String COLOR_FORMAT_LABEL_BORDER = "color.format.label.border";
	private static final String COLOR_DETAIL_TOP = "color.detail.top";
	private static final String COLOR_DETAIL_BOTTOM = "color.detail.bottom";
	private static final String COLOR_PANEL_UNSELECTED = "color.panel.unselected";
	private static final String COLOR_PANEL_SELECTED = "color.panel.selected";
	private static final String COLOR_PANEL_SELECTED_BACKGROUND = "color.panel.selected.background";
	private static final String COLOR_PANEL_HOME = "color.panel.home";
	private static final String COLOR_PANEL_HOME_ALPHA = "color.panel.home";
	private static final String COLOR_PROFILE_EDITION = "color.profile.edition";
	private static final String COLOR_WARN_REMAINING_BOTTOM_TIME = "color.warn.remain.bottom.time";
	private static final String COLOR_WARN_DECO_CEILING = "color.warn.deco.ceiling";
	private static final String COLOR_WARN_ASCENT_TOO_FAST = "color.warn.ascent.too.fast";
	private static final String COLOR_DECO_ENTRIES = "color.deco.entries";
	private static final String COLOR_SLIDESHOW_TOP = "color.slideshow.top";
	private static final String COLOR_SLIDESHOW_BOTTOM = "color.slideshow.bottom";
	private static final String COLOR_MAP_PANEL_DESCRIPTION = "color.map.panel.description";
	private static final String COLOR_MAP_PANEL_DESCRIPTION_BORDER = "color.map.panel.description.border";
	private static final String COLOR_MAP_PANEL_DESCRIPTION_FG = "color.map.panel.description.fg";
	private static final String COLOR_MATERIAL_ACTIVE_FG = "color.material.active.fg";
	private static final String COLOR_MATERIAL_INACTIVE_FG = "color.material.inactive.fg";

	private static final Font FONT_TITLE_DETAIL = new Font("Arial", Font.BOLD,
			12);
	private static final Font FONT_NORMAL_ITALIC = new Font("Arial",
			Font.ITALIC, 10);
	private static final Font FONT_MATERIAL_ACTIVE = new Font("Arial",
			Font.PLAIN, 12);
	private static final Font FONT_MATERIAL_INACTIVE = new Font("Arial",
			Font.ITALIC, 12);

	private static final Font FONT_NORMAL_BOLD = new Font("Arial", Font.BOLD,
			10);
	private static final Font FONT_MESSAGE_ITALIC = new Font("Arial",
			Font.ITALIC, 12);
	private static final Font FONT_MAP_ITEM_DETAIL = new Font("Arial",
			Font.BOLD, 16);
	private static final Font FONT_PROMPT_MESSAGE = new Font("Arial",
			Font.ITALIC, 16);

	public static final Dimension DIMENSION_20_20 = new Dimension(20, 20);

	private static UIAgent instance;

	private static Map<String, String> icons = new HashMap<String, String>();
	public static final String ICON_BULLET_ARROW_DOWN = "bullet.arrow.down";
	public static final String ICON_SERIAL_16 = "serial.16";
	public static final String ICON_USB_16 = "usb.16";
	public static final String ICON_ADD_16 = "add.16";
	public static final String ICON_ARROW_UP_16 = "arrow.up.16";
	public static final String ICON_ARROW_DOWN_16 = "down.16";
	public static final String ICON_ARROW_NEXT_16 = "arrow.next.16";
	public static final String ICON_ARROW_BACK_16 = "arrow.back.16";
	public static final String ICON_ATTACHEMENT_16 = "attachement.16";
	public static final String ICON_BTN_PLAY = "btn.play";
	public static final String ICON_BTN_PAUSE = "btn.pause";
	public static final String ICON_BTN_PREVIOUS = "btn.previous";
	public static final String ICON_BTN_NEXT = "btn.next";
	public static final String ICON_BTN_COLLAPSE_9 = "btn.collapse.9";
	public static final String ICON_BTN_COLLAPSE_ALL_16 = "btn.collapse.all.16";
	public static final String ICON_BTN_EXPAND_9 = "btn.expand.9";
	public static final String ICON_BTN_EXPAND_ALL_16 = "btn.expand.all.16";
	public static final String ICON_CANCEL_16 = "cancel.16";
	public static final String ICON_CANCEL_CHANGES_16 = "changes.cancel.16";
	public static final String ICON_DIVER_16 = "diver.16";
	public static final String ICON_DIVER_BLACK_16 = "diver.black.16";
	public static final String ICON_DIVER_ROLE_FIRST_16 = "diver.role.first.16";
	public static final String ICON_DIVER_ROLE_SECOND_16 = "diver.role.second.16";
	public static final String ICON_DIVE_SITE_16 = "divesite.16";
	public static final String ICON_DIVETANK_16 = "divetank.16";
	public static final String ICON_DIVETANK_24 = "divetank.24";
	public static final String ICON_DOCUMENT_16 = "document.16";
	public static final String ICON_EXPORT_16 = "export.16";
	public static final String ICON_EXPORT_24 = "export.24";
	public static final String ICON_FINS_16 = "fins.16";
	public static final String ICON_FINS_24 = "fins.24";
	public static final String ICON_SCOOTER_16 = "scooter.16";
	public static final String ICON_SCOOTER_24 = "scooter.24";
	public static final String ICON_WEIGHT_BELT_16 = "weight.belt.16";
	public static final String ICON_WEIGHT_BELT_24 = "weight.belt.24";
	public static final String ICON_SNORKEL_16 = "snorkel.16";
	public static final String ICON_SNORKEL_24 = "snorkel.24";
	public static final String ICON_FIT_HEIGHT_16 = "fit.height.16";
	public static final String ICON_FIT_WIDTH_16 = "fit.width.16";
	public static final String ICON_FIT_BEST_16 = "fit.best.16";
	public static final String ICON_FOLDER_OPEN_16 = "folder.open.16";
	public static final String ICON_FOLDER_CLOSED_16 = "folder.closed.16";
	public static final String ICON_FOLDER_OPEN_24 = "folder.open.24";
	public static final String ICON_FOLDER_CLOSED_24 = "folder.closed.24";
	public static final String ICON_GRAPH_16 = "graph.16";
	public static final String ICON_IMPORT_16 = "import.16";
	public static final String ICON_IMPORT_24 = "import.24";
	public static final String ICON_INFO_16 = "info.16";
	public static final String ICON_LOGBOOK_16 = "logbook.16";
	public static final String ICON_MASK_16 = "mask.16";
	public static final String ICON_MASK_24 = "mask.24";
	public static final String ICON_PARAMETER_16 = "parameters.16";
	public static final String ICON_SAVE_16 = "save.16";
	public static final String ICON_SEARCH_16 = "search.16";
	public static final String ICON_SHOP_16 = "shop.16";
	public static final String ICON_SLIDESHOW = "slideshow";
	public static final String ICON_TRASH_FULL_32 = "trash.64";
	public static final String ICON_TRIANGLE_DOWN_16 = "triangle.down.16";
	public static final String ICON_TRIANGLE_UP_16 = "triangle.up.16";
	public static final String ICON_WEB_BROWSER_16 = "web.browser.16";
	public static final String ICON_MATCAVE_16 = "batman.16";
	public static final String ICON_BULLET_16 = "bullet.orangr.16";
	public static final String ICON_LIGHT_16 = "light.16";
	public static final String ICON_LIGHT_24 = "light.24";
	public static final String ICON_COMPASS_16 = "compass.16";
	public static final String ICON_COMPASS_24 = "compass.24";
	public static final String ICON_WATCH_16 = "watch.16";
	public static final String ICON_WATCH_24 = "watch.24";
	public static final String ICON_DIVECOMPUTER_16 = "dive.computer.16";
	public static final String ICON_DIVECOMPUTER_24 = "dive.computer.24";
	public static final String ICON_KNIFE_16 = "knife.16";
	public static final String ICON_KNIFE_24 = "knife.24";
	public static final String ICON_BOOTS_16 = "boots.16";
	public static final String ICON_BOOTS_24 = "boots.24";
	public static final String ICON_GLOVES_16 = "gloves.16";
	public static final String ICON_GLOVES_24 = "gloves.24";
	public static final String ICON_DRY_SUIT_16 = "suit.dry.16";
	public static final String ICON_DRY_SUIT_24 = "suit.dry.24";
	public static final String ICON_REGULATOR_16 = "regulator.16";
	public static final String ICON_REGULATOR_24 = "regulator.24";
	public static final String ICON_REBREATHER_16 = "recycler.16";
	public static final String ICON_REBREATHER_24 = "recycler.24";
	public static final String ICON_MANOMETER_16 = "manometer.16";
	public static final String ICON_MANOMETER_24 = "manometer.24";
	public static final String ICON_BUOYANCY_COMPENSATOR_16 = "boyancy.compensator.16";
	public static final String ICON_BUOYANCY_COMPENSATOR_24 = "boyancy.compensator.24";
	public static final String ICON_HOOD_16 = "hood.16";
	public static final String ICON_HOOD_24 = "hood.24";
	public static final String ICON_UPDATE_16 = "update.16";
	public static final String ICON_EXIT_16 = "exit.16";
	public static final String ICON_USB_32 = "usb.32";
	public static final String ICON_SERIAL_32 = "serial.32";
	public static final String ICON_TOOLS_32 = "tools.32";
	public static final String ICON_TOOLS_24 = "tools.24";
	public static final String ICON_TOOLS_16 = "tools.16";
	public static final String ICON_PREFERENCE_GENERAL_24 = "preference.general.24";
	public static final String ICON_PREFERENCE_UNIT_24 = "preference.unit.24";
	public static final String ICON_CONSOLE_24 = "console.24";
	public static final String ICON_MERGE_16 = "merge.16";
	public static final String ICON_MERGE_24 = "merge.24";
	public static final String ICON_TRASH_FULL_16 = "trash.full.16";
	public static final String ICON_TRASH_FULL_24 = "trash.full.24";
	public static final String ICON_TRASH_EMPTY_16 = "trash.empty.16";
	public static final String ICON_TRASH_EMPTY_24 = "trash.EMPTY.24";
	public static final String ICON_HIERARCHY_16 = "hierarchy.12";
	public static final String ICON_CONNECT_16 = "connect.16";
	public static final String ICON_LOGBOOK_CLOSE_16 = "logbook.close.16";
	public static final String ICON_CONSOLE_16 = "console.16";
	public static final String ICON_GAZ_MASK_24 = "gaz.mask.24";
	public static final String ICON_SORT_16 = "sort.16";
	public static final String ICON_ABOUT_16 = "about.16";
	public static final String ICON_HOME_16 = "home.16";
	public static final String ICON_MOUTH_16 = "mouth.16";
	public static final String ICON_REPORT_16 = "report.16";
	public static final String ICON_SAVE_ALL_16 = "save.all.16";
	public static final String ICON_ATTENTION_16 = "attention.16";
	public static final String ICON_ATTENTION_48 = "attention.48";
	public static final String ICON_PREFERENCES_16 = "preferences.16";
	public static final String ICON_MAP_16 = "map.16";
	public static final String ICON_DIVE_SITE_EDIT_16 = "divesite.edit.16";
	public static final String ICON_DIVE_SITE_ADD_16 = "divesite.add.16";
	public static final String ICON_DIVE_SITE_DELETE_16 = "divesite.delete.16";
	public static final String ICON_DIVE_SITE_MERGE_16 = "divesite.merge.16";
	public static final String ICON_DIVE_SITE_MERGE_48 = "divesite.merge.48";
	public static final String ICON_CHECKMARK_RED_32 = "checkmark.red.32";
	public static final String ICON_LOGBOOK_24 = "logbook.24";
	public static final String ICON_DIVER_BLACK_EDIT_16 = "diver.black.edit.16";
	public static final String ICON_DIVER_BLACK_ADD_16 = "diver.black.add.16";
	public static final String ICON_DIVER_BLACK_DELETE_16 = "diver.black.delete.16";
	public static final String ICON_DIVER_BLACK_MERGE_16 = "diver.black.merge.16";
	public static final String ICON_DIVER_BLACK_MERGE_48 = "diver.black.merge.48";
	public static final String ICON_CLIPBOARD_16 = "clipboard.16";
	public static final String ICON_LOGBOOK_EDIT_16 = "logbook.edit.16";
	public static final String ICON_DIVE_DOCUMENT_16 = "dive.document.16";
	public static final String ICON_BULLET_ARROW_UP = "bullet.arrow.up";
	public static final String ICON_TASK_16 = "task.16";
	public static final String ICON_DIVE_EDITOR_16 = "dive.editor.16";
	public static final String ICON_STATISTICS_16 = "statistics.16";
	public static final String ICON_NETWORK_16 = "network.16";
	public static final String ICON_ROTATE_CLOCKWISE_16 = "rotate.clockwise.16";
	public static final String ICON_ROTATE_COUNTER_CLOCKWISE_16 = "rotate.counter.clockwise.16";
	public static final String ICON_MAGNIFYING_GLASS_16 = "magnifying.glass.16";
	public static final String ICON_DIVE_DOCUMENT_DELETE_16 = "dive.delete.16";
	public static final String ICON_DIVE_DOCUMENT_ADD_16 = "dive.add.16";
	public static final String ICON_LOGBOOK_ADD_16 = "logbook.add.16";
	public static final String ICON_SORT_DAY_16 = "sort.day.16";
	public static final String ICON_SORT_NUMBER_16 = "sort.number.16";
	public static final String ICON_SORT_YEAR_16 = "sort.year.16";
	public static final String ICON_SORT_DIVE_SITE_16 = "sort.divesite.16";
	public static final String ICON_ZOOM_IN_16 = "zoom.in.16";
	public static final String ICON_ZOOM_OUT_16 = "zoom.out.16";
	public static final String ICON_WIZARD_16 = "wizard.16";
	public static final String ICON_FILTER_16 = "filter.16";
	public static final String ICON_EDIT_16 = "edit.16";
	public static final String ICON_EDIT_24 = "edit.24";
	public static final String ICON_INACTIVE_16 = "active.16";
	public static final String ICON_ACTIVE_16 = "inactive.16";
	public static final String ICON_INACTIVE_24 = "active.24";
	public static final String ICON_ACTIVE_24 = "inactive.24";
	public static final String ICON_THERMOMETER_16 = "thermometer.16";
	public static final String ICON_STAR_16 = "star.16";
	public static final String ICON_BOX_16 = "box.16";
	public static final String ICON_WATER_TEMPERATURE_16 = "water.temperature.16";
	public static final String ICON_DIVE_DATE_16 = "calendar.16";
	public static final String ICON_DIVE_NUMBER_16 = "dive.number.16";
	public static final String ICON_DIVE_DEPTH_16 = "dive.depth.16";
	public static final String ICON_BUG_16 = "bug.16";
	public static final String ICON_DIVE_TIME_16 = "dive.time.16";
	public static final String ICON_DIVE_SURFACE_TIME_16 = "dive.surface.time.16";
	public static final String ICON_DIVE_PURPOSE_16 = "dive.purpose.16";
	public static final String ICON_IMPORTANT_48 = "important.48";
	public static final String ICON_CONVERT_48 = "convert.48";
	public static final String ICON_PIN_BLACK_16 = "pin.black.16";
	public static final String ICON_TARGET_16 = "target.16";
	public static final String ICON_DIVER_ROLE_MEDIC_16 = "diver.role.medic.16";
	public static final String ICON_DIVER_ROLE_CAMERA_16 = "diver.role.camera.16";
	public static final String ICON_DIVER_BLACK_EDIT_24 = "diver.black.edit.24";
	public static final String ICON_DIVER_BLACK_ADD_24 = "diver.black.add.24";
	public static final String ICON_DIVER_BLACK_DETAIL_16 = "diver.black.detail.16";
	public static final String ICON_DIVE_SITE_EDIT_24 = "dive.site.edit.24";
	public static final String ICON_DIVE_SITE_DETAIL_16 = "dive.site.detail.16";
	public static final String ICON_SIMPLE_DIVER_16 = "diver.simple.16";
	public static final String ICON_DIVE_SITE_DETAIL_48 = "dive.site.detail.48";
	public static final String ICON_DIVER_BLACK_DETAIL_48 = "diver.detail.48";
	public static final String ICON_REPORT_48 = "report.48";
	public static final String ICON_FILTER_48 = "filter.48";
	public static final String ICON_EMAIL_16 = "email.16";
	public static final String ICON_PHONE_16 = "phone.16";
	public static final String ICON_MOBILE_16 = "mobile.16";
	public static final String ICON_MATERIALSET_16 = "materialset.16";
	public static final String ICON_MATERIALSET_48 = "materialset.48";
	public static final String ICON_LOGBOOK_DELETE_16 = "logbook.delete.16";
	public static final String ICON_INTERPULMONARY_SHUNT_16 = "interpulmonary.shunt.16";
	public static final String ICON_ARTERIAL_MICROBUBBLES_16 = "arterial.microbubbles.16";

	private Dimension preferedComponentDim;

	private UIAgent() {
		UserPreferences.getInstance().addObserver(this);
		loadDefault();
	}

	private void loadDefault() {
		loadDefaultColors();
		loadDefaultIcons();
	}

	private void loadDefaultIcons() {
		icons.put(ICON_ARTERIAL_MICROBUBBLES_16, "bubbles_16.png");
		icons.put(ICON_INTERPULMONARY_SHUNT_16, "lungs_16.png");
		icons.put(ICON_LOGBOOK_DELETE_16, "trashEmpty16.png");
		icons.put(ICON_MATERIALSET_16, "wood_box_closed_16.png");
		icons.put(ICON_MATERIALSET_48, "wood_box_closed_48.png");
		icons.put(ICON_PHONE_16, "phone_16.png");
		icons.put(ICON_MOBILE_16, "mobile_16.png");
		icons.put(ICON_EMAIL_16, "arobas_16.png");
		icons.put(ICON_REPORT_48, "report_48.png");
		icons.put(ICON_DIVER_BLACK_DETAIL_48, "diver_black_detail_48.png");
		icons.put(ICON_DIVE_SITE_DETAIL_16, "dive_site_detail_16.png");
		icons.put(ICON_DIVE_SITE_DETAIL_48, "dive_site_detail_48.png");
		icons.put(ICON_DIVE_SITE_EDIT_24, "dive_site_edit_24.png");
		icons.put(ICON_DIVER_BLACK_DETAIL_16, "diver_black_detail_16.png");
		icons.put(ICON_DIVER_BLACK_EDIT_24, "diver_black_edit_24.png");
		icons.put(ICON_DIVER_BLACK_ADD_24, "diver_black_add_24.png");
		icons.put(ICON_DIVER_ROLE_CAMERA_16, "camera16.png");
		icons.put(ICON_DIVER_ROLE_MEDIC_16, "mediccase16.png");
		icons.put(ICON_TARGET_16, "crosshair_16.png");
		icons.put(ICON_PIN_BLACK_16, "location_pin_16.png");
		icons.put(ICON_CONVERT_48, "convert_48.png");
		icons.put(ICON_IMPORTANT_48, "important_48.png");
		icons.put(ICON_DIVE_PURPOSE_16, "crosshair_16.png");
		icons.put(ICON_DIVE_SURFACE_TIME_16, "clock16.png");
		icons.put(ICON_DIVE_TIME_16, "stopwatch_16.png");
		icons.put(ICON_BUG_16, "bug_16.png");
		icons.put(ICON_BOX_16, "wood_box_closed_16.png");
		icons.put(ICON_STAR_16, "star16.png");
		icons.put(ICON_WATER_TEMPERATURE_16, "thermometer_snowflake_16.png");
		icons.put(ICON_THERMOMETER_16, "thermometer_snowflake_16.png");
		icons.put(ICON_ACTIVE_16, "bulb_active16.png");
		icons.put(ICON_ACTIVE_24, "bulb_active24.png");
		icons.put(ICON_INACTIVE_16, "bulb_inactive16.png");
		icons.put(ICON_INACTIVE_24, "bulb_inactive24.png");
		icons.put(ICON_EDIT_24, "edit24.png");
		icons.put(ICON_EDIT_16, "edit16.png");
		icons.put(ICON_FILTER_16, "filter_16.png");
		icons.put(ICON_FILTER_48, "filter_48.png");
		icons.put(ICON_WIZARD_16, "wizard16.png");
		icons.put(ICON_ZOOM_IN_16, "zoomin_16.png");
		icons.put(ICON_ZOOM_OUT_16, "zoomout_16.png");
		icons.put(ICON_SORT_DIVE_SITE_16, "dive_site_16.png");
		icons.put(ICON_SORT_YEAR_16, "calendar16.png");
		icons.put(ICON_SORT_DAY_16, "calendar16.png");
		icons.put(ICON_DIVE_DATE_16, "calendar16.png");
		icons.put(ICON_SORT_NUMBER_16, "four16.png");
		icons.put(ICON_DIVE_NUMBER_16, "four16.png");
		icons.put(ICON_LOGBOOK_ADD_16, "logbookadd16.png");
		icons.put(ICON_DIVE_DOCUMENT_ADD_16, "divedocumentadd16.png");
		icons.put(ICON_DIVE_DOCUMENT_DELETE_16, "divedocumentdelete16.png");
		icons.put(ICON_MAGNIFYING_GLASS_16, "magnifying_glass_16.png");
		icons.put(ICON_ROTATE_COUNTER_CLOCKWISE_16,
				"rotate_counter_clockwise_16.png");
		icons.put(ICON_ROTATE_CLOCKWISE_16, "rotate_clockwise_16.png");
		icons.put(ICON_NETWORK_16, "network16.png");
		icons.put(ICON_STATISTICS_16, "statistics16.png");
		icons.put(ICON_DIVE_EDITOR_16, "editor16.png");
		icons.put(ICON_TASK_16, "sun16.png");
		icons.put(ICON_BULLET_ARROW_DOWN, "bulletArrowDown.png");
		icons.put(ICON_BULLET_ARROW_UP, "bulletArrowUp.png");
		icons.put(ICON_DIVE_DOCUMENT_16, "divedocument16.png");
		icons.put(ICON_LOGBOOK_EDIT_16, "logbookedit16.png");
		icons.put(ICON_CLIPBOARD_16, "clipboard_16.png");
		icons.put(ICON_DIVER_BLACK_ADD_16, "diver_black_add_16.png");
		icons.put(ICON_DIVER_BLACK_EDIT_16, "diver_black_edit_16.png");
		icons.put(ICON_DIVER_BLACK_MERGE_16, "diver_black_merge_16.png");
		icons.put(ICON_DIVER_BLACK_MERGE_48, "diver_black_merge_48.png");
		icons.put(ICON_DIVER_BLACK_DELETE_16, "diver_black_delete_16.png");
		icons.put(ICON_LOGBOOK_24, "logbook24.png");
		icons.put(ICON_CHECKMARK_RED_32, "checkmarkRed32.png");
		icons.put(ICON_DIVE_SITE_ADD_16, "dive_site_add_16.png");
		icons.put(ICON_DIVE_SITE_EDIT_16, "dive_site_edit_16.png");
		icons.put(ICON_DIVE_SITE_MERGE_16, "dive_site_merge_16.png");
		icons.put(ICON_DIVE_SITE_MERGE_48, "dive_site_merge_48.png");
		icons.put(ICON_DIVE_SITE_DELETE_16, "dive_site_delete_16.png");
		icons.put(ICON_MAP_16, "map_16.png");
		icons.put(ICON_PREFERENCES_16, "preferences16.png");
		icons.put(ICON_ATTENTION_16, "attention16_2.png");
		icons.put(ICON_ATTENTION_48, "attention48.png");
		icons.put(ICON_SAVE_ALL_16, "saveAll16.png");
		icons.put(ICON_REPORT_16, "report_16.png");
		icons.put(ICON_MOUTH_16, "mouth16.png");
		icons.put(ICON_HOME_16, "home16.png");
		icons.put(ICON_ABOUT_16, "about16.png");
		icons.put(ICON_SORT_16, "sort16.png");
		icons.put(ICON_GAZ_MASK_24, "gaz_mask_24.png");
		icons.put(ICON_CONSOLE_16, "console16.png");
		icons.put(ICON_LOGBOOK_CLOSE_16, "logbookclose16.png");
		icons.put(ICON_CONNECT_16, "connect16.png");
		icons.put(ICON_ARROW_UP_16, "arrow_up16.png");
		icons.put(ICON_ARROW_DOWN_16, "arrow_down16.png");
		icons.put(ICON_ARROW_NEXT_16, "arrow_next16.png");
		icons.put(ICON_ARROW_BACK_16, "arrow_back16.png");
		icons.put(ICON_TRIANGLE_DOWN_16, "triangle_down_16.png");
		icons.put(ICON_TRIANGLE_UP_16, "triangle_up_16.png");
		icons.put(ICON_CANCEL_CHANGES_16, "eraser_16.png");
		icons.put(ICON_CANCEL_16, "cancel16.png");
		icons.put(ICON_INFO_16, "info_16.png");
		icons.put(ICON_FOLDER_CLOSED_16, "folderClosed16.png");
		icons.put(ICON_FOLDER_OPEN_16, "folderOpen16.png");
		icons.put(ICON_FOLDER_CLOSED_24, "folder_closed24.png");
		icons.put(ICON_FOLDER_OPEN_24, "folder_open24.png");
		icons.put(ICON_TRASH_FULL_32, "trashFull32.png");
		icons.put(ICON_BTN_PLAY, "button_play_32.png");
		icons.put(ICON_BTN_PAUSE, "button_pause_32.png");
		icons.put(ICON_BTN_PREVIOUS, "button_fbw_32.png");
		icons.put(ICON_BTN_NEXT, "button_ffw_32.png");
		icons.put(ICON_SLIDESHOW, "slideshow_16.png");
		icons.put(ICON_ADD_16, "add16.png");
		icons.put(ICON_ATTACHEMENT_16, "attachment_16.png");
		icons.put(ICON_GRAPH_16, "graph16.png");
		icons.put(ICON_DIVE_DEPTH_16, "graph16.png");
		icons.put(ICON_WEIGHT_BELT_16, "weight_belt16.png");
		icons.put(ICON_WEIGHT_BELT_24, "weight_belt24.png");
		icons.put(ICON_SCOOTER_16, "scooter16.png");
		icons.put(ICON_SCOOTER_24, "scooter24.png");
		icons.put(ICON_SNORKEL_16, "snorkel16.png");
		icons.put(ICON_SNORKEL_24, "snorkel24.png");
		icons.put(ICON_MASK_16, "mask16.png");
		icons.put(ICON_MASK_24, "mask24.png");
		icons.put(ICON_FINS_16, "fins16.png");
		icons.put(ICON_FINS_24, "fins24.png");
		icons.put(ICON_DIVETANK_16, "dive_tank16.png");
		icons.put(ICON_DIVETANK_24, "dive_tank24.png");
		icons.put(ICON_PARAMETER_16, "parameters16.png");
		icons.put(ICON_FIT_HEIGHT_16, "fit_height_16.png");
		icons.put(ICON_FIT_WIDTH_16, "fit_width_16.png");
		icons.put(ICON_FIT_BEST_16, "fit_best_16.png");
		icons.put(ICON_DIVER_16, "diver_black_16.png");
		icons.put(ICON_DIVER_BLACK_16, "diver_black_16.png");
		icons.put(ICON_DIVER_ROLE_FIRST_16, "diver_red_16.png");
		icons.put(ICON_DIVER_ROLE_SECOND_16, "diver_blue_16.png");
		icons.put(ICON_DIVE_SITE_16, "dive_site_16.png");
		icons.put(ICON_EXPORT_16, "export_16.png");
		icons.put(ICON_IMPORT_16, "import_16.png");
		icons.put(ICON_EXPORT_24, "export_24.png");
		icons.put(ICON_IMPORT_24, "import_24.png");
		icons.put(ICON_DOCUMENT_16, "document16.png");
		icons.put(ICON_LOGBOOK_16, "logbook16.png");
		icons.put(ICON_WEB_BROWSER_16, "globe_16.png");
		icons.put(ICON_SEARCH_16, "magnifying_glass_16.png");
		icons.put(ICON_SHOP_16, "shop_16.png");
		icons.put(ICON_SAVE_16, "save16.png");
		icons.put(ICON_MATCAVE_16, "batman16.png");
		icons.put(ICON_BULLET_16, "bullet_orange16.png");
		icons.put(ICON_BTN_COLLAPSE_9, "btn_collapse_9.gif");
		icons.put(ICON_BTN_EXPAND_9, "btn_expand_9.gif");
		icons.put(ICON_BTN_EXPAND_ALL_16, "expandAll16.png");
		icons.put(ICON_BTN_COLLAPSE_ALL_16, "collapseAll16.png");
		icons.put(ICON_LIGHT_16, "light16.png");
		icons.put(ICON_LIGHT_24, "light24.png");
		icons.put(ICON_KNIFE_16, "knife16.png");
		icons.put(ICON_KNIFE_24, "knife24.png");
		icons.put(ICON_COMPASS_16, "compass16.png");
		icons.put(ICON_COMPASS_24, "compass24.png");
		icons.put(ICON_WATCH_16, "watch16.png");
		icons.put(ICON_WATCH_24, "watch24.png");
		icons.put(ICON_DIVECOMPUTER_16, "divecomputer16.png");
		icons.put(ICON_DIVECOMPUTER_24, "divecomputer24.png");
		icons.put(ICON_DRY_SUIT_16, "drysuit16.png");
		icons.put(ICON_DRY_SUIT_24, "drysuit24.png");
		icons.put(ICON_BOOTS_16, "boots16.png");
		icons.put(ICON_BOOTS_24, "boots24.png");
		icons.put(ICON_GLOVES_16, "gloves16.png");
		icons.put(ICON_GLOVES_24, "gloves24.png");
		icons.put(ICON_REBREATHER_16, "recycler16.png");
		icons.put(ICON_REBREATHER_24, "recycler24.png");
		icons.put(ICON_REGULATOR_16, "regulator16.png");
		icons.put(ICON_REGULATOR_24, "regulator24.png");
		icons.put(ICON_BUOYANCY_COMPENSATOR_16, "boyancycompensator16.png");
		icons.put(ICON_BUOYANCY_COMPENSATOR_24, "boyancycompensator24.png");
		icons.put(ICON_MANOMETER_16, "manometer16.png");
		icons.put(ICON_MANOMETER_24, "manometer24.png");
		icons.put(ICON_HOOD_16, "hood16.png");
		icons.put(ICON_HOOD_24, "hood24.png");
		icons.put(ICON_UPDATE_16, "refresh16.png");
		icons.put(ICON_EXIT_16, "exit16.png");
		icons.put(ICON_SERIAL_16, "serial16.png");
		icons.put(ICON_SERIAL_32, "serial32.png");
		icons.put(ICON_USB_16, "usb16.png");
		icons.put(ICON_USB_32, "usb32.png");
		icons.put(ICON_TOOLS_16, "tools16.png");
		icons.put(ICON_TOOLS_24, "tools24.png");
		icons.put(ICON_TOOLS_32, "tools32.png");
		icons.put(ICON_PREFERENCE_GENERAL_24, "preferenceGeneral24.png");
		icons.put(ICON_PREFERENCE_UNIT_24, "preferenceUnit24.png");
		icons.put(ICON_CONSOLE_24, "console24.png");
		icons.put(ICON_MERGE_24, "merge24.png");
		icons.put(ICON_MERGE_16, "merge16.png");
		icons.put(ICON_TRASH_EMPTY_16, "trashEmpty16.png");
		icons.put(ICON_TRASH_EMPTY_24, "trashEmpty24.png");
		icons.put(ICON_TRASH_FULL_16, "trashFull16.png");
		icons.put(ICON_TRASH_FULL_24, "trashFull24.png");
		icons.put(ICON_HIERARCHY_16, "hierarchical16.gif");
	}

	private void loadDefaultColors() {
		colors.put(COLOR_WATER_BOTTOM, new Color(1, 5, 75));
		colors.put(COLOR_WATER_SURFACE, MEDIUM_BLUE);
		colors.put(COLOR_WATER_CROSSHAIR, RED);
		colors.put(COLOR_TITLE_PANEL, new Color(51, 98, 140));
		// colors.put(COLOR_TITLE_PANEL, MEDIUM_BLUE);
		colors.put(COLOR_TITLE_PANEL_ALPHA, MEDIUM_BLUE_ALPHA_125);
		colors.put(COLOR_TITLE_PANEL_FG, Color.WHITE);
		colors.put(COLOR_CONSOLE_BKG, Color.BLACK);
		colors.put(COLOR_BKG_BASE, Color.WHITE);
		colors.put(COLOR_FG_BASE, Color.BLACK);
		colors.put(COLOR_GAZ_OXYGEN, new Color(0, 6, 159));
		colors.put(COLOR_GAZ_NITROGEN, new Color(147, 17, 67));
		colors.put(COLOR_GAZ_HELIUM, GREY);
		colors.put(COLOR_GAZ_HYDROGEN, new Color(0, 115, 111));
		colors.put(COLOR_GAZ_ARGON, new Color(40, 115, 11));
		colors.put(COLOR_GAZ_NEON, new Color(200, 15, 11));
		colors.put(COLOR_GAZ_BACKGROUND, WHITE);
		colors.put(COLOR_WELCOME, DARK_BLUE);
		colors.put(COLOR_WELCOME_ALPHA, MEDIUM_BLUE);
		colors.put(COLOR_FORMAT_LABEL_BORDER, DARK_GREY);
		colors.put(COLOR_DETAIL_BOTTOM, LIGHT_GREY);
		colors.put(COLOR_DETAIL_TOP, MEDIUM_GREY);
		colors.put(COLOR_PANEL_SELECTED, RED);
		colors.put(COLOR_PANEL_UNSELECTED, DARK_GREY);
		colors.put(COLOR_PANEL_SELECTED_BACKGROUND, new Color(255, 0, 0, 100));
		colors.put(COLOR_PANEL_HOME, new Color(1, 5, 75));
		colors.put(COLOR_PANEL_HOME_ALPHA, MEDIUM_BLUE);
		colors.put(COLOR_PROFILE_EDITION, RED);
		colors.put(COLOR_WARN_ASCENT_TOO_FAST, RED);
		colors.put(COLOR_WARN_DECO_CEILING, new Color(208, 207, 216));
		colors.put(COLOR_WARN_REMAINING_BOTTOM_TIME, new Color(250, 200, 0));
		colors.put(COLOR_DECO_ENTRIES, Color.GREEN);
		colors.put(COLOR_SLIDESHOW_BOTTOM, Color.GRAY);
		colors.put(COLOR_SLIDESHOW_TOP, Color.BLACK);
		colors.put(COLOR_MAP_PANEL_DESCRIPTION, MEDIUM_BLUE);
		colors.put(COLOR_MAP_PANEL_DESCRIPTION_BORDER, DARK_BLUE);
		colors.put(COLOR_MAP_PANEL_DESCRIPTION_FG, WHITE);
		colors.put(COLOR_MATERIAL_INACTIVE_FG, GREY);
		colors.put(COLOR_MATERIAL_ACTIVE_FG, BLACK);
	}

	public static UIAgent getInstance() {
		if (instance == null) {
			instance = new UIAgent();
		}
		return instance;
	}

	private Color getColor(String colorName) {
		return colors.get(colorName);
	}

	public static void setColorTitlePanel(Color color) {
		colors.put(COLOR_TITLE_PANEL, color);
	}

	public static void setColorForegroundTitlePanel(Color color) {
		colors.put(COLOR_TITLE_PANEL_FG, color);
	}

	public Color getColorTitlePanelForeground() {
		return getColor(COLOR_TITLE_PANEL_FG);
	}

	public static void setColorTitlePanelAlpha(Color color) {
		colors.put(COLOR_TITLE_PANEL_ALPHA, color);
	}

	public Color getColorTitlePanel() {
		return getColor(COLOR_TITLE_PANEL);
	}

	public Color getColorTitlePanelAlpha() {
		return getColor(COLOR_TITLE_PANEL_ALPHA);
	}

	public Color getColorWaterSurface() {
		return getColor(COLOR_WATER_SURFACE);
	}

	public Paint getColorWaterGrid() {
		return getColor(COLOR_WATER_SURFACE);
	}

	public Paint getColorWaterCrossHair() {
		return getColor(COLOR_WATER_CROSSHAIR);
	}

	public Color getColorWaterBottom() {
		return getColor(COLOR_WATER_BOTTOM);
	}

	public Color getColorConsoleBkg() {
		return getColor(COLOR_CONSOLE_BKG);
	}

	public Color getColorGazHelium() {
		return getColor(COLOR_GAZ_HELIUM);
	}

	public Color getColorGazHydrogen() {
		return getColor(COLOR_GAZ_HYDROGEN);
	}

	public Color getColorGazArgon() {
		return getColor(COLOR_GAZ_ARGON);
	}

	public Color getColorGazNeon() {
		return getColor(COLOR_GAZ_NEON);
	}

	public Color getColorGazNitrogen() {
		return getColor(COLOR_GAZ_NITROGEN);
	}

	public Color getColorGazOxygen() {
		return getColor(COLOR_GAZ_OXYGEN);
	}

	public int getLineThickness() {
		return 2;
	}

	public Font getFontTitleDetail() {
		return FONT_TITLE_DETAIL;
	}

	public Color getColorBaseBackground() {
		return getColor(COLOR_BKG_BASE);
	}

	public Color getColorBaseForeground() {
		return getColor(COLOR_FG_BASE);
	}

	public Font getFontNormalItalic() {
		return FONT_NORMAL_ITALIC;
	}

	public Font getFontNormalBold() {
		return FONT_NORMAL_BOLD;
	}

	public Font getFontMessageItalic() {
		return FONT_MESSAGE_ITALIC;
	}

	public Color getColorWelcome() {
		return getColor(COLOR_WELCOME);
	}

	public Color getColorWelcomeAlpha() {
		return getColor(COLOR_WELCOME_ALPHA);
	}

	public Color getColorFormatLabelBorder() {
		return getColor(COLOR_FORMAT_LABEL_BORDER);
	}

	public Color getColorGazBorder() {
		return getColor(COLOR_GAZ_BORDER);
	}

	public Color getColorGazBackground() {
		return getColor(COLOR_GAZ_BACKGROUND);
	}

	public Color getColorDetailPanelBottom() {
		return getColor(COLOR_DETAIL_BOTTOM);
	}

	public Color getColorDetailPanelTop() {
		return getColor(COLOR_DETAIL_TOP);
	}

	public Color getColorPanelSelected() {
		return getColor(COLOR_PANEL_SELECTED);
	}

	public Color getColorPanelUnselected() {
		return getColor(COLOR_PANEL_UNSELECTED);
	}

	public Color getColorPanelSelectedBackground() {
		return getColor(COLOR_PANEL_SELECTED_BACKGROUND);
	}

	public Color getColorHomePanel() {
		return getColor(COLOR_PANEL_HOME);
	}

	public Color getColorHomePanelAlpha() {
		return getColor(COLOR_PANEL_HOME_ALPHA);
	}

	public Color getColorProfileEdition() {
		return getColor(COLOR_PROFILE_EDITION);
	}

	public Color getColorWarningRemainingBottomTime() {
		return getColor(COLOR_WARN_REMAINING_BOTTOM_TIME);
	}

	public Color getColorWarningDecoCeiling() {
		return getColor(COLOR_WARN_DECO_CEILING);
	}

	public Color getColorWarningAscentTooFast() {
		return getColor(COLOR_WARN_ASCENT_TOO_FAST);
	}

	public Color getColorDecoEntries() {
		return getColor(COLOR_DECO_ENTRIES);
	}

	public Color getColorSlideShowBkgTop() {
		return getColor(COLOR_SLIDESHOW_TOP);
	}

	public Color getColorSlideShowBkgBottom() {
		return getColor(COLOR_SLIDESHOW_BOTTOM);
	}

	public Icon getIcon(String imageName) {
		String img = icons.get(imageName);
		if (img == null)
			return null;
		return ResourceManager.getInstance().getImageIcon(img);
	}

	public Dimension getPreferredComponentDimension() {
		if (preferedComponentDim == null) {
			preferedComponentDim = new Dimension(60, 20);

		}
		return preferedComponentDim;
	}

	public SimpleDateFormat getFormatDateHoursFull() {
		return dateFormatterDateHour;
	}

	public SimpleDateFormat getFormatDateShort() {
		return dateFormatterDate;
	}

	public void setFormatDateHoursFull(SimpleDateFormat simpleDateFormat) {
		if (!simpleDateFormat.toPattern().equals(
				getFormatDateHoursFull().toPattern())) {
			dateFormatterDateHour = simpleDateFormat;
			setChanged();
			notifyObservers(CHANGE_DATE_FORMAT_DAY_HOUR);
		}
	}

	public void setFormatDateShort(SimpleDateFormat simpleDateFormat) {
		if (!simpleDateFormat.toPattern().equals(
				getFormatDateShort().toPattern())) {
			dateFormatterDate = simpleDateFormat;
			setChanged();
			notifyObservers(CHANGE_DATE_FORMAT_DAY);
		}
	}

	public SimpleDateFormat getExportFormatDateHoursFull() {
		return DATE_HOURS_FORMATTER_EXPORT;
	}

	public SimpleDateFormat getExportFormatDateShort() {
		return DATE_FORMATTER_SHORT;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o.equals(UserPreferences.getInstance())) {
			if (arg.equals(UserPreferences.PREFERENCES_CHANGED)) {
				updatePreferences();
			}
		}
	}

	private void updatePreferences() {
		setFormatDateHoursFull(UserPreferences.getInstance()
				.getPreferredDateHoursFormat());
		setFormatDateShort(UserPreferences.getInstance()
				.getPreferredDateFormat());
	}

	public BufferedImage getBufferedImage(String imageName) {
		String img = icons.get(imageName);
		if (img == null)
			return null;
		return ResourceManager.getInstance().getImage(img);
	}

	public Color getColorMapPanelDescription() {
		return getColor(COLOR_MAP_PANEL_DESCRIPTION);
	}

	public Color getColorMapPanelDescriptionFg() {
		return getColor(COLOR_MAP_PANEL_DESCRIPTION_FG);
	}

	public Paint getColorMapPanelDescriptionBorder() {
		return getColor(COLOR_MAP_PANEL_DESCRIPTION_BORDER);
	}

	public Font getFontMapItemDetail() {
		return FONT_MAP_ITEM_DETAIL;
	}

	public Color getColorMaterialInActive() {
		return getColor(COLOR_MATERIAL_INACTIVE_FG);
	}

	public Color getColorMaterialActive() {
		return getColor(COLOR_MATERIAL_ACTIVE_FG);
	}

	public Font getFontMaterialInActive() {
		return FONT_MATERIAL_INACTIVE;
	}

	public Font getFontMaterialActive() {
		return FONT_MATERIAL_ACTIVE;
	}

	public Font getFontPromptMessage() {
		return FONT_PROMPT_MESSAGE;
	}

	public Font getFontMapPin() {
		return FONT_NORMAL_ITALIC;
	}

}
