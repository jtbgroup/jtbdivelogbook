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
package be.vds.jtbdive.client.swing.component.jasper;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

public class PrintViewerPanel extends JRViewer {
    private static final long serialVersionUID = -2410928242403482011L;

    public PrintViewerPanel(JasperPrint jrPrint) {
	super(jrPrint);
    }

    public void fitPage() {
	super.btnFitPage.doClick();
    }
}
