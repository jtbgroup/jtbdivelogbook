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
package be.vds.jtbdive.client.view.core.dive;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import be.smd.i18n.swing.I18nButton;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.swing.component.SelectableInnerPanel;
import be.vds.jtbdive.client.swing.component.SelectionListener;
import be.vds.jtbdive.client.util.ExtensionFilter;
import be.vds.jtbdive.client.view.core.document.DocumentModificationListener;
import be.vds.jtbdive.client.view.core.document.DocumentPanel;
import be.vds.jtbdive.client.view.core.document.DocumentViewerPanel;
import be.vds.jtbdive.client.view.core.document.DocumentsSlideWalker;
import be.vds.jtbdive.client.view.core.document.FormatIconDocumentPanel;
import be.vds.jtbdive.client.view.core.slideshow.SlideShowDialog;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.Document;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;

public class DocumentsParameterPanel extends JPanel {

	private static final long serialVersionUID = 7155827251151266062L;
	private Dive currentDive;
	private LogBookManagerFacade logBookManagerFacade;
	private I18nButton removeDocumentButton;
	private SelectableInnerPanel selectablePanel;
	private DocumentViewerPanel documentViewerPanel;
	private Document selectedDocument;
	private I18nButton slideShowButton;

	public DocumentsParameterPanel(LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
		init();
	}

	private void init() {
		this.setBackground(UIAgent.getInstance().getColorBaseBackground());
		this.setLayout(new BorderLayout());
		this.add(createButtonsPanel(), BorderLayout.NORTH);
		this.add(createCentralPanel(), BorderLayout.CENTER);
	}

	private Component createButtonsPanel() {
		removeDocumentButton = new I18nButton(new AbstractAction("remove",
				UIAgent.getInstance().getIcon(UIAgent.ICON_CANCEL_16)) {
			private static final long serialVersionUID = -6708646302655234389L;

			@Override
			public void actionPerformed(ActionEvent e) {
				List<JComponent> compons = selectablePanel
						.getSelectedComponents();
				for (JComponent jComponent : compons) {
					Document d = ((DocumentPanel) jComponent).getDocument();
					currentDive.removeDocument(d);
				}
				selectablePanel.removeComponents(selectablePanel
						.getSelectedComponents());
				logBookManagerFacade.setDiveChanged(currentDive);

				adaptSlidShowButton();
				// TODO : verify wheter needed
				invalidate();
				repaint();
				validate();
			}
		});

		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(createAddPictureButton());
		p.add(removeDocumentButton);
		p.add(createSlideShowButton());
		return p;
	}

	private Component createSlideShowButton() {
		slideShowButton = new I18nButton(new AbstractAction("slideshow",
				UIAgent.getInstance().getIcon(UIAgent.ICON_SLIDESHOW)) {
			private static final long serialVersionUID = 607226067020690957L;

			@Override
			public void actionPerformed(ActionEvent e) {
				SlideShowDialog dlg = new SlideShowDialog(
						new DocumentsSlideWalker(currentDive.getDocuments(),
								logBookManagerFacade));
				dlg.setLocationRelativeTo(null);
				dlg.setVisible(true);
			}
		});
		return slideShowButton;
	}

	private Component createAddPictureButton() {
		JButton addPictureButton = new I18nButton(new AbstractAction("add",
				UIAgent.getInstance().getIcon(UIAgent.ICON_ADD_16)) {

			private static final long serialVersionUID = -503416518635933007L;

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();
				fc.setMultiSelectionEnabled(false);
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

				List<DocumentFormat> df = DocumentFormat
						.getImportableExtensions();
				Set<String> knownExts = new HashSet<String>();

				for (DocumentFormat documentFormat : df) {
					String[] exts = documentFormat.getAuthorizedExtensions();
					String s = exts[0];

					if (!knownExts.contains(s)) {
						ExtensionFilter ef = new ExtensionFilter(s,
								documentFormat.getDescription());
						if (exts.length > 1) {
							for (int i = 1; i < exts.length; i++) {
								ef.addExtension(exts[i]);
							}
						}
						fc.addChoosableFileFilter(ef);
						knownExts.add(s);
					}
				}

				ExtensionFilter allFilters = new ExtensionFilter(
						"All supported formats");
				allFilters.setDetailedDescription(false);
				for (String string : knownExts) {
					allFilters.addExtension(string);
				}

				fc.addChoosableFileFilter(allFilters);

				int i = fc.showOpenDialog(null);
				if (i == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					byte[] b = new byte[(int) f.length()];
					try {
						new FileInputStream(f).read(b);
						Document p = new Document();
						p.setTitle(f.getName());
						p.setContent(b);
						p.setDocumentFormat(DocumentFormat
								.getAuthorizedDocumentFormatForFileName(f
										.getName()));

						addDocumentToModelAndView(p, true);

					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		return addPictureButton;
	}

	private void addDocumentToModelAndView(Document document, boolean repaint) {

		currentDive.addDocument(document);
		logBookManagerFacade.setDiveChanged(currentDive);

		selectablePanel.clearSelection();

		createAndAddDocumentPanel(document, true);

		adaptSlidShowButton();

		if (repaint) {
			// TODO : verify wheter needed
			this.invalidate();
			this.repaint();
			this.validate();
		}
	}

	private void createAndAddDocumentPanel(Document document, boolean autoSelect) {
		DocumentPanel doc = new FormatIconDocumentPanel(document);

		selectablePanel.addSelectableComponent(doc, document.getTitle(),
				autoSelect);

		if (autoSelect) {
			documentViewerPanel.setDocument(document, logBookManagerFacade);
			selectedDocument = document;
		}
	}

	private Component createCentralPanel() {
		JSplitPane centralSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		centralSplit.setOpaque(false);

		centralSplit.setLeftComponent(createSelectablePanel());
		centralSplit.setRightComponent(createDocumentViewerPanel());

		centralSplit.setDividerLocation(100);

		return centralSplit;

	}

	private Component createDocumentViewerPanel() {
		documentViewerPanel = new DocumentViewerPanel();
		documentViewerPanel
				.addDocumentModificationListener(new DocumentModificationListener() {

					@Override
					public void documentTitleModified(String title) {
						selectedDocument.setTitle(title);
						adaptThumbnailTitle(selectedDocument);
						logBookManagerFacade.setDiveChanged(currentDive);
					}

					@Override
					public void documentCommentModified(String comment) {
						selectedDocument.setComment(comment);
						logBookManagerFacade.setDiveChanged(currentDive);
					}
				});
		return documentViewerPanel;
	}

	private void adaptThumbnailTitle(Document document) {
		selectablePanel.getSelectedPanel().updateTitle(document.getTitle());
	}

	private Component createSelectablePanel() {
		selectablePanel = new SelectableInnerPanel();

		selectablePanel.addSelectionListener(new SelectionListener() {

			@Override
			public void selectionChanged(JComponent component) {
				boolean selected = selectablePanel.getSelectedComponents()
						.size() > 0;

				removeDocumentButton.setEnabled(selected);

				if (selected) {
					selectedDocument = ((DocumentPanel) selectablePanel
							.getSelectedComponents().get(0)).getDocument();
					documentViewerPanel.setDocument(selectedDocument,
							logBookManagerFacade);
				} else {
					selectedDocument = null;
					documentViewerPanel.setDocument(null, null);
				}
			}
		});

		JScrollPane scroll = new JScrollPane(selectablePanel);
		SwingComponentHelper.displayJScrollPane(scroll);
		return scroll;
	}

	public void clear() {
		selectablePanel.clear();
		adaptSlidShowButton();
	}

	private void adaptSlidShowButton() {
		slideShowButton.setEnabled(currentDive != null
				&& currentDive.getDocuments() != null
				&& currentDive.getDocuments().size() > 0);
	}

	public void setDocuments(List<Document> documents, Dive currentDive) {
		this.currentDive = currentDive;
		clear();

		if (null != documents) {
			for (Document document : documents) {
				createAndAddDocumentPanel(document, false);
			}
		}
		adaptSlidShowButton();
	}

}
