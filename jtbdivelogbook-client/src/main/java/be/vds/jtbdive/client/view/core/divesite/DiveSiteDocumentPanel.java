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
package be.vds.jtbdive.client.view.core.divesite;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.VerticalLayout;

import be.smd.i18n.swing.I18nButton;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.DocumentContentLoader;
import be.vds.jtbdive.client.swing.component.DoubleClickHandler;
import be.vds.jtbdive.client.swing.component.SelectableInnerPanel;
import be.vds.jtbdive.client.swing.component.SelectionListener;
import be.vds.jtbdive.client.view.core.document.DocumentPanel;
import be.vds.jtbdive.client.view.core.document.DocumentViewerDialog;
import be.vds.jtbdive.client.view.core.document.DocumentsSlideWalker;
import be.vds.jtbdive.client.view.core.document.ThumbnailDocumentPanel;
import be.vds.jtbdive.client.view.core.slideshow.SlideShowDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Document;

public class DiveSiteDocumentPanel extends JPanel {
	private static final long serialVersionUID = -2024410783236160455L;
	private SelectableInnerPanel selectablePanel;
	private I18nButton removeDocumentButton;
	private DocumentContentLoader documentContentLoader;
	private I18nButton slideShowButton;
	private I18nButton editDocumentButton;

	public DiveSiteDocumentPanel(DocumentContentLoader documentContentLoader) {
		this.documentContentLoader = documentContentLoader;
		init();
	}

	private void init() {
		setOpaque(false);
		setLayout(new BorderLayout());
		add(createDocumentsButtonsPanel(), BorderLayout.EAST);
		add(createDocumentsCentralPanel(), BorderLayout.CENTER);
	}

	private Component createDocumentsCentralPanel() {
		selectablePanel = new SelectableInnerPanel();
		selectablePanel.setOpaque(false);

		selectablePanel.setDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void handleDoubleClick() {
				openDocumentViewer();
			}

		});
		selectablePanel.addSelectionListener(new SelectionListener() {

			@Override
			public void selectionChanged(JComponent component) {
				boolean selected = selectablePanel.getSelectedComponents()
						.size() > 0;
				// boolean canRemove = removeDocumentButton.isEnabled();
				// if (selected != canRemove){
				removeDocumentButton.setEnabled(selected);
				editDocumentButton.setEnabled(selected);
				// }
			}
		});

		JScrollPane scroll = new JScrollPane(selectablePanel);
		scroll.getVerticalScrollBar().setUnitIncrement(
				UIAgent.VERTICAL_UNIT_SCROLL);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		return scroll;
	}

	private void openDocumentViewer() {
		ThumbnailDocumentPanel p = ((ThumbnailDocumentPanel) (selectablePanel
				.getSelectedComponents().get(0)));
		Document d = p.getDocument();
		DocumentViewerDialog dlg = null;
		Window w = WindowUtils.getTopLevelWindow(DiveSiteDocumentPanel.this);
		if (w instanceof Frame) {
			dlg = new DocumentViewerDialog((Frame) w);
		} else if (w instanceof Dialog) {
			dlg = new DocumentViewerDialog((Dialog) w);
		} else {
			dlg = new DocumentViewerDialog();
		}
		dlg.setDocument(d, documentContentLoader);
		int i = dlg.showDocument();
		if (i == DocumentViewerDialog.OPTION_SELECT) {
			d.setComment(dlg.getDocumentComment());
			d.setTitle(dlg.getDocumentTitle());
			selectablePanel.getSelectedPanel().updateTitle(d.getTitle());
			this.revalidate();
		}
	}

	private Component createDocumentsButtonsPanel() {
		removeDocumentButton = new I18nButton(new AbstractAction("remove") {
			private static final long serialVersionUID = -2286285412007206184L;

			@Override
			public void actionPerformed(ActionEvent e) {
				selectablePanel.removeComponents(selectablePanel
						.getSelectedComponents());
			}
		});
		removeDocumentButton.setEnabled(false);

		JButton addPictureButton = new I18nButton(new AbstractAction("add") {

			private static final long serialVersionUID = 1823233433169478505L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Window window = WindowUtils
						.getTopLevelWindow(DiveSiteDocumentPanel.this);
				DiveSiteDocumentDialog dlg = null;
				if (window instanceof Frame) {
					dlg = new DiveSiteDocumentDialog((Frame) window);
				} else if (window instanceof Dialog) {
					dlg = new DiveSiteDocumentDialog((Dialog) window);
				} else {
					dlg = new DiveSiteDocumentDialog();
				}
				int i = dlg.showDialog();

				if (i == DiveSiteDocumentDialog.OPTION_SELECT) {
					Document doc = dlg.getSelectedDocument();
					addDocumentThumb(doc, true);
				}
			}
		});

		JPanel p = new JPanel(new VerticalLayout());
		p.add(addPictureButton);
		p.add(removeDocumentButton);
		p.add(createEditDocumentButton());
		p.add(createSlideShowButton());
		p.setOpaque(false);
		return p;
	}

	private Component createEditDocumentButton() {
		editDocumentButton = new I18nButton(new AbstractAction("edit") {
			private static final long serialVersionUID = 7749677402366747159L;

			@Override
			public void actionPerformed(ActionEvent e) {
				openDocumentViewer();
			}
		});
		editDocumentButton.setEnabled(false);
		return editDocumentButton;
	}

	private Component createSlideShowButton() {
		slideShowButton = new I18nButton(
				new AbstractAction("slideshow", UIAgent.getInstance()
						.getIcon(UIAgent.ICON_SLIDESHOW)) {
					private static final long serialVersionUID = 6223479051684957173L;

					@Override
					public void actionPerformed(ActionEvent e) {
						SlideShowDialog dlg = new SlideShowDialog(
								new DocumentsSlideWalker(
										getDiveSiteDocuments(),
										documentContentLoader));
						dlg.setLocationRelativeTo(null);
						dlg.setVisible(true);
					}
				});
		return slideShowButton;
	}

	private void addDocumentThumb(Document document, boolean repaint) {
		ThumbnailDocumentPanel doc = new ThumbnailDocumentPanel(document,
				documentContentLoader);
		selectablePanel.addSelectableComponent(doc, document.getTitle());

		if (repaint) {
			// TODO : verify wheter needed
			this.invalidate();
			this.repaint();
			this.validate();
		}
	}

	public void setValue(DiveSite diveSite) {
		if (diveSite.getDocuments() != null) {
			for (Document document : diveSite.getDocuments()) {
				addDocumentThumb(document, false);
			}
			this.invalidate();
			this.repaint();
			this.validate();
		}
	}

	public void reset() {
		selectablePanel.removeAll();
	}

	public List<Document> getDiveSiteDocuments() {
		List<Document> docs = new ArrayList<Document>();
		List<JComponent> comps = selectablePanel.getRegisteredComponents();
		for (JComponent jComponent : comps) {
			if (jComponent instanceof DocumentPanel) {
				docs.add(((DocumentPanel) jComponent).getDocument());
			}
		}

		if (docs.size() == 0)
			return null;
		return docs;
	}

//	public void setEditable(boolean b) {
//	}
}
