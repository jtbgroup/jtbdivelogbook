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

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import net.sf.jasperreports.engine.JasperPrint;

/**
 * This dialog is used to print an inner component normally representing a
 * report. If the panel passed is a {@link PrintViewerPanel}, the page is fitted
 * when the dialog is set visible.
 * 
 * 
 * @author Gautier Vanderslyen
 * 
 * 
 */
public class PrintPreviewDialog extends JDialog {
    private static final long serialVersionUID = 6885867738992163655L;
    private boolean fitPage = true;
   

    public PrintPreviewDialog(Dialog parent, String title, JasperPrint jrPrint,
	    boolean fitPage) {
	super(parent, title, true);
	this.fitPage = fitPage;
	initWithoutViewer(jrPrint);
    }

    public PrintPreviewDialog(Frame parent, String title, JasperPrint jrPrint,
	    boolean fitPage) {
	super(parent, title, true);
	this.fitPage = fitPage;
	initWithoutViewer(jrPrint);
    }

    public PrintPreviewDialog(String title, JasperPrint jrPrint, boolean fitPage) {
	this.setModal(true);
	setTitle(title);
	this.fitPage = fitPage;
	initWithoutViewer(jrPrint);
    }
    
    public PrintPreviewDialog(Dialog parent, String title, PrintViewerPanel reportViewer,
	    boolean fitPage) {
	super(parent, title, true);
	this.fitPage = fitPage;
	initWithViewer(reportViewer);
    }
    
    public PrintPreviewDialog(Frame parent, String title, PrintViewerPanel reportViewer,
	    boolean fitPage) {
	super(parent, title, true);
	this.fitPage = fitPage;
	initWithViewer(reportViewer);
    }
    

    private void initWithViewer(PrintViewerPanel reportViewer) {
	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	this.getContentPane().add(reportViewer, BorderLayout.CENTER);
	if (fitPage) {
	    reportViewer.fitPage();
	}
	pack();
    }

    private void initWithoutViewer(JasperPrint jrPrint) {
	PrintViewerPanel reportViewer = new PrintViewerPanel(jrPrint);
	initWithViewer(reportViewer);
    }
     
}
