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
package be.vds.jtbdive.client.view.wizard.stats.axisy;

import be.vds.jtbdive.client.core.stats.axisy.StatYAxisDiveTemperatureParams;
import be.vds.jtbdive.client.core.stats.axisy.StatYAxisParams;

public class StatYAxisDiveTemperatureDescriptor extends
		StatYAxisWizardPanelDescriptor {

	public static final String IDENTIFIER = "STAT_Y_DIVE_TEMPERATURE_PANEL";
	private StatYAxisDiveTemperaturePanel panel;

	public StatYAxisDiveTemperatureDescriptor() {
		panel = new StatYAxisDiveTemperaturePanel();
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(panel);
	}

	@Override
	public StatYAxisParams getYAxisParams() {
		StatYAxisDiveTemperatureParams p = new StatYAxisDiveTemperatureParams();
		return p;
	}

}
