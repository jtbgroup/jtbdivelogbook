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
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import net.sf.jasperreports.engine.JasperPrint;

/**
 * This dialog is used to print an inner component normally representing a
 * report. If the panel passed is a {@link PrintViewerPanel}, the page is fitted
 * when the dialog is set visible.
 * 
 * @author Gautier Vanderslyen
 * 
 */
public class PrintPreviewFrame extends JFrame {

    private static final long serialVersionUID = 6885867738992163655L;
    private boolean fitPage = true;

    public PrintPreviewFrame(Dialog parent, String title, JasperPrint jrPrint,
            boolean fitPage) {
        this.fitPage = fitPage;
        initWithoutViewer(jrPrint);
    }

    public PrintPreviewFrame(Frame parent, String title, JasperPrint jrPrint,
            boolean fitPage) {
        this.fitPage = fitPage;
        initWithoutViewer(jrPrint);
    }

    public PrintPreviewFrame(String title, JasperPrint jrPrint, boolean fitPage) {
        setTitle(title);
        this.fitPage = fitPage;
        initWithoutViewer(jrPrint);
    }

    public PrintPreviewFrame(String title, JasperPrint jrPrint, boolean fitPage, Image image) {
        setTitle(title);
        this.setIconImage(image);
        this.fitPage = fitPage;
        initWithoutViewer(jrPrint);
    }

    public PrintPreviewFrame(String title, PrintViewerPanel reportViewer,
            boolean fitPage) {
        this.fitPage = fitPage;
        initWithViewer(reportViewer);
    }

    private void initWithoutViewer(JasperPrint jrPrint) {
        PrintViewerPanel reportViewer = new PrintViewerPanel(jrPrint);
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
}
