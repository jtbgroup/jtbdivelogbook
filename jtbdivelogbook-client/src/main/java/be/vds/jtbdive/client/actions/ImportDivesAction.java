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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.GlossaryManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.conversion.ImpExFormat;
import be.vds.jtbdive.client.core.conversion.ImpExJTBHandler;
import be.vds.jtbdive.client.core.processes.WorkingProcess;
import be.vds.jtbdive.client.view.wizard.importation.ImportWizard;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveEquipment;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.Document;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.LogBookMeta;
import be.vds.jtbdive.core.core.PalanqueeEntry;
import be.vds.jtbdive.core.core.material.Equipment;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.core.utils.ObjectSerializer;

public class ImportDivesAction extends AbstractAction {

	private static final long serialVersionUID = 6840061822963948488L;
	private static final Syslog LOGGER = Syslog
			.getLogger(ImportDivesAction.class);
	private LogBookManagerFacade logBookManagerFacade;
	private DiveSiteManagerFacade diveSiteManagerFacade;
	private DiverManagerFacade diverManagerFacade;
	private GlossaryManagerFacade glossaryManagerFacade;

	public ImportDivesAction(LogBookManagerFacade logBookManagerFacade,
			DiveSiteManagerFacade diveSiteManagerFacade,
			DiverManagerFacade diverManagerFacade,
			GlossaryManagerFacade glossaryManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
		this.diveSiteManagerFacade = diveSiteManagerFacade;
		this.glossaryManagerFacade = glossaryManagerFacade;
		this.diverManagerFacade = diverManagerFacade;

		putValue(Action.NAME, "import");
		putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK
						+ KeyEvent.ALT_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ImportWizard wizard = new ImportWizard(diverManagerFacade,
				diveSiteManagerFacade, logBookManagerFacade,
				glossaryManagerFacade);
		Map<Object, Object> map = wizard.doImport(logBookManagerFacade
				.getCurrentLogBook() == null);
		doImport(map);

	}

	private void doImport(Map<Object, Object> map) {
		if (null == map) {
			LOGGER.warn("Wizard map is null. Import aborted.");
			return;
		}

		ImpExFormat f = (ImpExFormat) map.get(ImportWizard.IMPORT_FORMAT);
		switch (f) {
		case JTB_FORMAT:
			importForJtb(map);
			break;
		case BINARIES_FORMAT:
			importForBinaries(map);
			break;
		case UDCF:
			importForUDCF(map);
			break;
		case UDDF_V220:
		case UDDF_V300:
		case UDDF_V301:
			importForUDDF(map);
			break;
		}
	}

	private void importForBinaries(Map<Object, Object> map) {
		@SuppressWarnings("unchecked")
		List<Dive> dives = (List<Dive>) map.get(ImportWizard.IMPORT_DIVES);

		LogBook newLogBook = (LogBook) map.get(ImportWizard.IMPORT_NEW_LOGBOOK);

		integrateData(dives, null, null, newLogBook, null, null);
	}

	private void importForUDCF(Map<Object, Object> map) {
		@SuppressWarnings("unchecked")
		List<Dive> dives = (List<Dive>) map.get(ImportWizard.IMPORT_DIVES);
		@SuppressWarnings("unchecked")
		Map<DiveSite, DiveSite> diveSiteMap = (Map<DiveSite, DiveSite>) map
				.get(ImportWizard.IMPORT_DIVESITE_MAP);

		LogBook newLogBook = (LogBook) map.get(ImportWizard.IMPORT_NEW_LOGBOOK);

		integrateData(dives, diveSiteMap, null, newLogBook, null, null);
	}

	private void importForUDDF(Map<Object, Object> map) {
		@SuppressWarnings("unchecked")
		List<Dive> dives = (List<Dive>) map.get(ImportWizard.IMPORT_DIVES);
		@SuppressWarnings("unchecked")
		Map<DiveSite, DiveSite> diveSiteMap = (Map<DiveSite, DiveSite>) map
				.get(ImportWizard.IMPORT_DIVESITE_MAP);
		@SuppressWarnings("unchecked")
		Map<Diver, Diver> diverMap = (Map<Diver, Diver>) map
				.get(ImportWizard.IMPORT_DIVER_MAP);
		@SuppressWarnings("unchecked")
		Map<Material, Material> materialMap = (Map<Material, Material>) map
				.get(ImportWizard.IMPORT_MATERIAL_MAP);

		LogBook newLogBook = (LogBook) map.get(ImportWizard.IMPORT_NEW_LOGBOOK);

		integrateData(dives, diveSiteMap, diverMap, newLogBook, materialMap,
				null);
	}

	private void integrateData(List<Dive> dives,
			Map<DiveSite, DiveSite> diveSiteMap, Map<Diver, Diver> diverMap,
			LogBook newLogBook, Map<Material, Material> materialMap,
			ImpExDocumentHandler handler) {
		saveDiveSites(dives, diveSiteMap, handler);
		saveDivers(dives, diverMap);
		if (null != newLogBook) {
			createNewLogBook(newLogBook);
		}
		saveMaterials(materialMap);
		integrateInCurrentLogBook(dives, handler, materialMap);

	}

	private void importForJtb(Map<Object, Object> map) {
		@SuppressWarnings("unchecked")
		List<Dive> dives = (List<Dive>) map.get(ImportWizard.IMPORT_DIVES);
		@SuppressWarnings("unchecked")
		Map<DiveSite, DiveSite> diveSiteMap = (Map<DiveSite, DiveSite>) map
				.get(ImportWizard.IMPORT_DIVESITE_MAP);
		@SuppressWarnings("unchecked")
		Map<Diver, Diver> diverMap = (Map<Diver, Diver>) map
				.get(ImportWizard.IMPORT_DIVER_MAP);
		@SuppressWarnings("unchecked")
		Map<Material, Material> materialMap = (Map<Material, Material>) map
				.get(ImportWizard.IMPORT_MATERIAL_MAP);

		ImpExJTBHandler parser = (ImpExJTBHandler) map
				.get(ImportWizard.IMPORT_IMPEX_HANDLER);

		LogBook newLogBook = (LogBook) map.get(ImportWizard.IMPORT_NEW_LOGBOOK);

		integrateData(dives, diveSiteMap, diverMap, newLogBook, materialMap,
				parser);
	}

	private void createNewLogBook(LogBook newLogBook) {
		if (newLogBook != null) {
			LogBookMeta meta = newLogBook.getLogbookMeta();
			try {
				meta = logBookManagerFacade.saveLogBookMeta(meta);
				logBookManagerFacade.loadLogBook(meta.getId());
			} catch (DataStoreException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveMaterials(Map<Material, Material> materialMap) {
		if (null == materialMap)
			return;

		List<Material> materialSet = new ArrayList<Material>(
				materialMap.keySet());
		for (Material material : materialSet) {
			if (materialMap.get(material) == null) {
				Material clone = (Material) ObjectSerializer
						.cloneObject(material);
				clone.setId(-1);
				try {
					Material newMat = logBookManagerFacade.saveMaterial(clone);
					materialMap.put(material, newMat);
				} catch (DataStoreException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void saveDiveSites(List<Dive> dives,
			Map<DiveSite, DiveSite> diveSiteMap, ImpExDocumentHandler handler) {
		List<DiveSite> diveSiteSet = new ArrayList<DiveSite>(
				diveSiteMap.keySet());
		for (DiveSite diveSite : diveSiteSet) {
			if (diveSiteMap.get(diveSite) == null) {
				DiveSite clone = (DiveSite) ObjectSerializer
						.cloneObject(diveSite);
				clone.setId(-1);

				if (clone.getDocuments() != null && handler != null) {
					for (Document doc : clone.getDocuments()) {
						byte[] content;
						try {
							content = handler.getDiveSiteDocumentContent(
									doc.getId(), doc.getDocumentFormat());
							doc.setContent(content);
						} catch (DataStoreException e) {
							LOGGER.error(e.getMessage());
						}
						doc.setId(-1);
					}
				}
				try {
					DiveSite newDiveSite = diveSiteManagerFacade
							.saveDiveSite(clone);
					diveSiteMap.put(diveSite, newDiveSite);
				} catch (DataStoreException e) {
					e.printStackTrace();
				}
			}
		}

		for (Dive dive : dives) {
			if (dive.getDiveSite() != null) {
				DiveSite ds = dive.getDiveSite();
				if (diveSiteMap.keySet().contains(ds)) {
					dive.setDiveSite(diveSiteMap.get(ds));
				} else {
					dive.setDiveSite(null);
				}
			}
		}
	}

	private void saveDivers(List<Dive> dives, Map<Diver, Diver> diverMap) {
		List<Diver> diverSet = new ArrayList<Diver>(diverMap.keySet());
		for (Diver ds : diverSet) {
			if (diverMap.get(ds) == null) {
				Diver clone = (Diver) ObjectSerializer.cloneObject(ds);
				clone.setId(-1);
				try {
					Diver newDiver = diverManagerFacade.saveDiver(clone);
					diverMap.put(ds, newDiver);
				} catch (DataStoreException e) {
					e.printStackTrace();
				}
			}
		}

		for (Dive dive : dives) {
			if (dive.getPalanquee() != null) {

				List<PalanqueeEntry> peToDelete = new ArrayList<PalanqueeEntry>();
				for (PalanqueeEntry pe : dive.getPalanquee()
						.getPalanqueeEntries()) {
					Diver ds = pe.getDiver();
					if (diverMap.keySet().contains(ds)) {
						pe.setDiver(diverMap.get(ds));
					} else {
						peToDelete.add(pe);
					}
				}

				for (PalanqueeEntry palanqueeEntry : peToDelete) {
					dive.getPalanquee().removePalanqueeEntry(palanqueeEntry);
				}

			}
		}
	}

	private void integrateInCurrentLogBook(List<Dive> dives,
			ImpExDocumentHandler handler, Map<Material, Material> materialMap) {

		for (Dive dive : dives) {

			if (dive.getDiveEquipment() != null) {
				DiveEquipment de = dive.getDiveEquipment();
				for (Equipment eq : de.getAllEquipments()) {
					if (eq.getMaterial() != null
							&& materialMap.keySet().contains(eq.getMaterial())) {
						eq.setMaterial(materialMap.get(eq.getMaterial()));
					} else {
						eq.setMaterial(null);
					}
				}
			}

			if (dive.getDocuments() != null && handler != null) {
				for (Document doc : dive.getDocuments()) {
					byte[] content;
					try {
						content = handler.getDiveDocumentContent(doc.getId(),
								doc.getDocumentFormat());
						doc.setContent(content);
						doc.setId(-1);
					} catch (DataStoreException e) {
						LOGGER.error(e.getMessage());
						dive.removeDocument(doc);
					}
				}
			}
			dive.setId(-1);
			logBookManagerFacade.addDive(dive);
		}
	}

	class InnerWorkingProcess extends WorkingProcess {

		private Map<Object, Object> map;

		public InnerWorkingProcess(String id, Map<Object, Object> map) {
			super(id);
			this.map = map;
		}

		@Override
		protected Object doInBackground() throws Exception {
			fireProcessStarted(1, "Importing dives...");
			LOGGER.info("importing dives");
			publish("importing dives");
			try {
				doImport(map);
			} catch (Exception e) {
				LOGGER.error(e);
			}
			return null;
		}

		@Override
		protected void process(List<String> arg0) {
			for (String string : arg0) {
				fireProcessProgressed(1, string);
			}
		}

		@Override
		protected void done() {
			fireProcessProgressed(1, "Dives imported");
			fireProcessFinished("Dives imported!");
		}
	}
}
