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
package be.vds.jtbdive.client.view.wizard.export;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.conversion.ImpExFormat;
import be.vds.jtbdive.client.core.conversion.ImpExJTBHandler;
import be.vds.jtbdive.client.core.conversion.ImpExUDCFHandler;
import be.vds.jtbdive.client.core.conversion.ImpExUDDFHandler;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.XMLValidationException;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.wizard.Wizard;
import be.vds.wizard.WizardPanelDescriptor;

public class ExportWizard {

	private static final Syslog LOGGER = Syslog.getLogger(ExportWizard.class);
	public static final String EXPORT_FORMAT = "export.format";
	public static final Object EXPORT_FILE = "export.file";
	public static final Object EXPORT_DIVES = "export.dives";
	private final Component parentComponent;
	private final LogBookManagerFacade logBookManagerFacade;
	private final DiveSiteManagerFacade diveSiteManagerFacade;

	public ExportWizard(Component parentComponent,
			DiveSiteManagerFacade diveSiteManagerFacade,
			LogBookManagerFacade logBookManagerFacade) {
		this.parentComponent = parentComponent;
		this.diveSiteManagerFacade = diveSiteManagerFacade;
		this.logBookManagerFacade = logBookManagerFacade;
	}

	public void export(LogBook logBook) {
		Map<Object, Object> map = createWizard(logBook);
		LOGGER.debug("Wizard ended, configuration must be returned");

		if (map == null) {
			LOGGER.info("The map is null and no export has been done");
		} else {
			export(map, logBook);
			LOGGER.info("Export done.");
		}
	}

	private void export(Map<Object, Object> map, LogBook logBook) {
		ImpExFormat f = (ImpExFormat) map.get(ExportWizard.EXPORT_FORMAT);
		switch (f) {
		case JTB_FORMAT:
			exportJtb(map, logBook, parentComponent, diveSiteManagerFacade,
					logBookManagerFacade);
			break;
		case UDCF:
			exportUdcf(map, logBook, parentComponent);
			break;
		case UDDF_V300:
			exportUddf(map, logBook, ImpExUDDFHandler.V_3_0_0, parentComponent);
			break;
		case UDDF_V301:
			exportUddf(map, logBook, ImpExUDDFHandler.V_3_0_1, parentComponent);
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void exportJtb(Map<Object, Object> map, LogBook logBook,
			Component parentComponent,
			DiveSiteManagerFacade diveSiteManagerFacade,
			LogBookManagerFacade logBookManagerFacade) {
		File file = (File) map.get(ExportWizard.EXPORT_FILE);
		try {
			ImpExJTBHandler parser = new ImpExJTBHandler(file);
			parser.write((List<Dive>) map.get(ExportWizard.EXPORT_DIVES),
					logBook, file, null, diveSiteManagerFacade,
					logBookManagerFacade);

		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
			ExceptionDialog.showDialog(ex, parentComponent);
		} catch (XMLValidationException ex) {
			LOGGER.error(ex.getMessage());
			ExceptionDialog.showDialog(ex, parentComponent);
		} catch (DataStoreException e) {
			LOGGER.error(e.getMessage());
			ExceptionDialog.showDialog(e, parentComponent);
		}
	}

	// private static void writeExport(Document document, File file)
	// throws FileNotFoundException, IOException {
	// OutputStream os = new FileOutputStream(file);
	// XMLOutputter outputter = new XMLOutputter();
	// outputter.setFormat(Format.getPrettyFormat());
	// outputter.output(document, os);
	// os.close();
	// }

	@SuppressWarnings("unchecked")
	private void exportUdcf(Map<Object, Object> map, LogBook logBook,
			Component parentComponent) {
		try {
			File file = (File) map.get(ExportWizard.EXPORT_FILE);
			ImpExUDCFHandler handler = new ImpExUDCFHandler(file);
			handler.write((List<Dive>) map.get(ExportWizard.EXPORT_DIVES),
					logBook.getOwner());
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
			ExceptionDialog.showDialog(ex, parentComponent);
		} catch (XMLValidationException ex) {
			LOGGER.error(ex.getMessage());
			ExceptionDialog.showDialog(ex, parentComponent);
		}

	}

	private void exportUddf(Map<Object, Object> map, LogBook logBook,
			String impexHandlerVersion, Component parentComponent) {
		File file = (File) map.get(ExportWizard.EXPORT_FILE);
		try {
			ImpExUDDFHandler handler = new ImpExUDDFHandler(file);
			handler.write((List<Dive>) map.get(ExportWizard.EXPORT_DIVES),
					logBook.getOwner(), impexHandlerVersion);

		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
			ExceptionDialog.showDialog(ex, parentComponent);
		} catch (XMLValidationException ex) {
			LOGGER.error(ex.getMessage());
			ExceptionDialog.showDialog(ex, parentComponent);
		}

	}

	private static Map<Object, Object> createWizard(LogBook logBook) {
		Wizard wizard = new Wizard();
		wizard.setWizardResizable(true);
		wizard.setWizardSize(500, 400);
		wizard.setWizardCentered(true);
		wizard.setTitle("Export Wizard");
		wizard.setDefaultIcons();

		WizardPanelDescriptor exportChoiceDescriptor = new ExportChoiceDescriptor();
		wizard.registerWizardPanel(ExportChoiceDescriptor.IDENTIFIER,
				exportChoiceDescriptor);

		WizardPanelDescriptor exportOptionConfigDescriptor = new ExportOptionDescriptor(
				logBook.getDives());
		wizard.registerWizardPanel(ExportOptionDescriptor.IDENTIFIER,
				exportOptionConfigDescriptor);

		WizardPanelDescriptor finishDescriptor = new ExportFinishDescriptor();
		wizard.registerWizardPanel(ExportFinishDescriptor.IDENTIFIER,
				finishDescriptor);

		wizard.setCurrentPanel(ExportChoiceDescriptor.IDENTIFIER);

		int ret = wizard.showModalDialog();

		if (ret == 0) {
			return wizard.getModel().getDataMap();
		} else if (ret == 1) {
			LOGGER.warn("Wizard canceled");
		} else if (ret == 2) {
			LOGGER.error("Wizard in error!!!");
		}

		return null;
	}
}
