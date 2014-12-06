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
package be.vds.jtbdive.client.actions;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.processes.WorkingProcess;
import be.vds.jtbdive.client.core.processes.WorkingProcessManager;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.swing.component.jasper.PrintPreviewFrame;
import be.vds.jtbdive.client.util.ReportHelper;
import be.vds.jtbdive.client.util.ResourceManager;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.components.ReportDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.comparator.DiveDateComparator;
import be.vds.jtbdive.core.logging.Syslog;

public class PrintReportLogBookAction extends AbstractAction {

	private static final long serialVersionUID = 7214186065778257267L;
	private Syslog LOGGER = Syslog.getLogger(PrintReportLogBookAction.class);
    private LogBookManagerFacade logBookManagerFacade;
    private DiveSiteManagerFacade diveSiteManagerFacade;
    private JasperPrint jrPrint;

    public PrintReportLogBookAction(LogBookManagerFacade logBookManagerFacade, DiveSiteManagerFacade diveSiteManagerFacade) {
        super("report");
        this.logBookManagerFacade = logBookManagerFacade;
        this.diveSiteManagerFacade = diveSiteManagerFacade;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R,
                KeyEvent.CTRL_DOWN_MASK));
        putValue(Action.SMALL_ICON, 
				UIAgent.getInstance().getIcon(UIAgent.ICON_REPORT_16));
        this.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        ReportDialog dlg = new ReportDialog(logBookManagerFacade.getCurrentLogBook());
        WindowUtils.centerWindow(dlg);
        int i = dlg.showDialog(500, 500);
        if (i == PromptDialog.OPTION_OK) {
            Map<String, Object> options = dlg.getOptions();
            WorkingProcess wp = new InnerWorkingProcess("Generate report", options);
            WorkingProcessManager.getInstance().loadProcess(wp);
        }
    }

    public void generateReport(Map<String, Object> options) {
        try {
            LogBook lb = logBookManagerFacade.getCurrentLogBook();
            @SuppressWarnings("unchecked")
			List<Dive> dives = (List<Dive>) options.get(ReportHelper.DIVES) ;
            Collections.sort(dives, new DiveDateComparator());

            System.setProperty("org.xml.sax.driver",
                    "org.apache.xerces.parsers.SAXParser");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put(ReportHelper.LOGBOOK, lb);
            parameters.put(ReportHelper.DIVES, dives);
            parameters.put(ReportHelper.REPORT_HELPER, new ReportHelper(logBookManagerFacade, diveSiteManagerFacade));

            for (String param : options.keySet()) {
                parameters.put(param, options.get(param));
            }

            InputStream is = ResourceManager.getInstance().getReportAsInputStream("logbook.jasper");
            JasperReport jr = (JasperReport) JRLoader.loadObject(is);

            jrPrint = JasperFillManager.fillReport(jr, parameters,
                    new JREmptyDataSource());
            is.close();

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    Image image = UIAgent.getInstance().getBufferedImage(UIAgent.ICON_REPORT_16);

                    PrintPreviewFrame previewDialog = new PrintPreviewFrame(
                            "Print report", jrPrint, true, image);

                    double heigth = WindowUtils.getScreenHeight() - 60;
                    WindowUtils.resizeA4Portrait(previewDialog, heigth);
                    WindowUtils.centerWindow(previewDialog);
                    previewDialog.setVisible(true);
                }
            });

        } catch (JRException e) {
            ExceptionDialog.showDialog(e, null, "JRException Error with the report logbook.jasper");
            LOGGER.error(e);
        } catch (IOException e) {
            ExceptionDialog.showDialog(e, null, "JRException Error with the report logbook.jasper");
            LOGGER.error(e);
        } catch (Exception e) {
        	e.printStackTrace();
            ExceptionDialog.showDialog(e, null, "Exception Error with the report logbook.jasper");
            LOGGER.error(e);
        }
    }

    class InnerWorkingProcess extends WorkingProcess {

        private Map<String, Object> options;

        public InnerWorkingProcess(String id, Map<String, Object> options) {
            super(id);
            this.options = options;
        }

        @Override
        protected Object doInBackground() throws Exception {
            LOGGER.info("Generating report");
            fireProcessStarted(100, "Generating report");
            publish("Generating report");
        	try {
				generateReport(options);
			} catch (Exception e) {
				ExceptionDialog.showDialog(e, null);
				LOGGER.error(e.getMessage());
			}
            return null;
        }

        @Override
        protected void process(List<String> arg0) {
            for (String string : arg0) {
                fireProcessProgressed(25, string);
            }
        }

        @Override
        protected void done() {
            fireProcessProgressed(100, "Report generated");
            fireProcessFinished("Report successfully generated");
        }
    }
}
