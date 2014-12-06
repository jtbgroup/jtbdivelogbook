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
package be.vds.jtbdive.client.view.wizard.stats;

import java.awt.Font;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JTextArea;

import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.core.stats.DiveDepthsStatQueryObject;
import be.vds.jtbdive.client.core.stats.DiveSiteStatQueryObject;
import be.vds.jtbdive.client.core.stats.DiverStatQueryObject;
import be.vds.jtbdive.client.core.stats.StatQueryObject;
import be.vds.jtbdive.client.core.stats.YearsStatQueryObject;
import be.vds.jtbdive.client.core.stats.axisy.StatYAxisParams;
import be.vds.jtbdive.client.view.wizard.WizardPanel;

public class StatFinishPanel extends WizardPanel {

	private static final long serialVersionUID = -3391269373786199058L;
	private JTextArea textArea;

	@Override
	public JComponent createContentPanel() {
		String text = i18n.getString("stat.query.options") + ":";

		textArea = new JTextArea();
		textArea.setOpaque(false);
		textArea.setEnabled(true);
		textArea.setEditable(false);

		textArea.setWrapStyleWord(true);
		textArea.setText(text);
		textArea.setFont(new Font("Arial", Font.ITALIC, 12));
		return textArea;
	}

	@Override
	public String getMessage() {
		return null;
	}

	public void adaptText(Map<Object, Object> dataMap) {

		StatQueryObject sq = (StatQueryObject) dataMap.get("stat.query");
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n\r\n");
		sb.append(i18n.getString("axis.x")).append(" : ");
		sb.append(i18n.getString(sq.getStatXAxis().getKey())).append("\r\n");
		sb.append("  ").append(generateXOptionsText(sq)).append("\r\n\r\n");
		sb.append(i18n.getString("axis.y")).append(" : ");
		sb.append(
				i18n.getString(sq.getStatYAxisParams().getStatYAxis().getKey()))
				.append("\r\n");
		sb.append("  ").append(generateYOptionsText(sq.getStatYAxisParams()))
				.append("\r\n\r\n");

		sb.append(i18n.getString("dives.numberof")).append(" : ")
				.append(sq.getDives().size()).append("\r\n\r\n");
		sb.append(i18n.getString("cumulated")).append(" : ")
				.append(i18n.getString(sq.isCumulated() ? "yes" : "no"));
		textArea.append(sb.toString());
	}

	private Object generateXOptionsText(StatQueryObject statQueryObject) {
		if (statQueryObject instanceof DiveDepthsStatQueryObject) {
			DiveDepthsStatQueryObject obj = (DiveDepthsStatQueryObject) statQueryObject;
			return i18n.getString("pitch")
					+ " : "
					+ UnitsAgent.getInstance().convertLengthFromModel(
							obj.getPitch()) + " ("
					+ UnitsAgent.getInstance().getLengthUnit().getSymbol()
					+ ")";
		} else if (statQueryObject instanceof DiverStatQueryObject) {
			DiverStatQueryObject obj = (DiverStatQueryObject) statQueryObject;
			return i18n.getString("divers.numberof") + " : "
					+ obj.getDivers().size();

		} else if (statQueryObject instanceof DiveSiteStatQueryObject) {
			DiveSiteStatQueryObject obj = (DiveSiteStatQueryObject) statQueryObject;
			return i18n.getString("divesites.numberof") + " : "
					+ obj.getDiveSites().size();
		} else if (statQueryObject instanceof YearsStatQueryObject) {
			YearsStatQueryObject obj = (YearsStatQueryObject) statQueryObject;
			return i18n.getString("years.numberof") + " : "
					+ obj.getYears().size();
		}
		return i18n.getString("no.option");
	}

	private String generateYOptionsText(StatYAxisParams statYAxisParams) {
		switch (statYAxisParams.getStatYAxis()) {

		default:
			return i18n.getString("no.option");
		}
	}
}