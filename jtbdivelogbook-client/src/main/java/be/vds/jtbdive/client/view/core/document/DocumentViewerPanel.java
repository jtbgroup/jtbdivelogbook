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
package be.vds.jtbdive.client.view.core.document;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXBusyLabel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.DocumentContentLoader;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Document;
import be.vds.jtbdive.core.core.catalogs.DocumentFormat;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;

public class DocumentViewerPanel extends JPanel {
	private static final long serialVersionUID = 841284194719369047L;
	protected static final Syslog LOGGER = Syslog
			.getLogger(DocumentViewerPanel.class);
	private JTextField titleJtextField;
	private JTextArea commentTextArea;
	private Set<DocumentModificationListener> documentModificationListeners = new HashSet<DocumentModificationListener>();
	private Viewer viewer;
	private WaitingComponent loadingComponent;
	private Viewer noViewerComponent;
	private JPanel viewerPanel;
	private JLabel formatLabel;
	private I18nLabel commentLabel;
	private JScrollPane commentScroll;
	private JButton collapseCommentButton;

	private Map<String, Viewer> viewersMap = new HashMap<String, Viewer>();
	private Map<DocumentFormat, String> viewerFormat = new HashMap<DocumentFormat, String>();
	{
		viewerFormat.put(DocumentFormat.FORMAT_GIF, "image");
		viewerFormat.put(DocumentFormat.FORMAT_JPG, "image");
		viewerFormat.put(DocumentFormat.FORMAT_PNG, "image");

	}

	public DocumentViewerPanel() {
		init();
	}

	private void init() {
		createLoadingComponent();
		createNoViewerComponent();
		this.setLayout(new BorderLayout());
		this.add(createDetailPanel(), BorderLayout.NORTH);
		this.add(createViewerPanel(), BorderLayout.CENTER);

	}

	private void createLoadingComponent() {
		loadingComponent = new WaitingComponent();
	}

	private void createNoViewerComponent() {
		noViewerComponent = new DefaultViewer();
	}

	private Component createDetailPanel() {
		DetailPanel detailPanel = new DetailPanel();
		detailPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);

		GridBagLayoutManager.addComponent(detailPanel, createTitleLabel(), c,
				0, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.NORTHEAST);
		GridBagLayoutManager.addComponent(detailPanel, createTitleComponent(),
				c, 1, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(detailPanel, createFormatComponent(),
				c, 2, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(detailPanel,
				createButtonCollapseComponent(), c, 3, 0, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(detailPanel, createCommentLabel(), c,
				0, 1, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.NORTHEAST);
		GridBagLayoutManager.addComponent(detailPanel,
				createCommentComponent(), c, 1, 1, 3, 1, 1, 1,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER);

		return detailPanel;
	}

	private Component createButtonCollapseComponent() {
		Action collapseCommentAction = new AbstractAction() {
			private static final long serialVersionUID = 8422977849559621057L;

			@Override
			public void actionPerformed(ActionEvent e) {
				setCommentVisible(!isCommentVisible());
			}
		};

		collapseCommentButton = new JButton(collapseCommentAction);
		collapseCommentButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_BTN_COLLAPSE_ALL_16));
		return collapseCommentButton;
	}

	private void setCommentVisible(boolean b) {
		commentLabel.setVisible(b);
		commentScroll.setVisible(b);
		commentTextArea.setVisible(b);

		collapseCommentButton.setIcon(b ? UIAgent.getInstance().getIcon(
				UIAgent.ICON_BTN_COLLAPSE_ALL_16) : UIAgent
				.getInstance().getIcon(UIAgent.ICON_BTN_EXPAND_ALL_16));
	}

	private boolean isCommentVisible() {
		return commentScroll.isVisible();
	}

	private Component createFormatComponent() {
		formatLabel = new JLabel();
		formatLabel.setPreferredSize(new Dimension(20, 20));
		// formatLabel.setBorder(new LineBorder(UIController.getInstance()
		// .getColorFormatLabelBorder()));
		// formatLabel.setBackground(UIController.getInstance()
		// .getColorBaseBackground());
		// formatLabel.setOpaque(false);
		return formatLabel;
	}

	private Component createTitleLabel() {
		return new I18nLabel("title");
	}

	private Component createTitleComponent() {
		titleJtextField = new JTextField();
		titleJtextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				notifyTitleChanged(titleJtextField.getText());
			}
		});
		return titleJtextField;
	}

	private Component createCommentLabel() {
		commentLabel = new I18nLabel("comment");
		return commentLabel;
	}

	private Component createCommentComponent() {
		commentTextArea = new JTextArea();
		commentTextArea.setRows(3);
		commentTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				notifyCommentChanged(commentTextArea.getText());
			}
		});
		commentScroll = new JScrollPane(commentTextArea);
		return commentScroll;
	}

	private Component createViewerPanel() {
		viewerPanel = new DetailPanel();
		viewerPanel.setLayout(new BorderLayout());

		return viewerPanel;
	}

	public void setDocument(Document document, DocumentContentLoader loader) {
		if (document == null) {
			clear();
		} else {
			titleJtextField.setText(document.getTitle());
			commentTextArea.setText(document.getComment());
			// formatLabel.setText(document.getDocumentFormat().getDescription());
			formatLabel.setIcon(DocumentIcon.getIcon24ForDocument(document
					.getDocumentFormat()));

			loadingComponent.setWaiting(true);
			setViewer(loadingComponent);
			loadDocument(document, loader);
		}
	}

	private void setViewer(Viewer viewer) {
		removeViewer();
		this.viewer = viewer;
		viewerPanel.add(this.viewer, BorderLayout.CENTER);

		this.repaint();
		this.revalidate();
	}

	private void removeViewer() {
		if (null != viewer)
			viewerPanel.remove(viewer);
	}

	private void loadDocument(final Document document,
			final DocumentContentLoader loader) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					byte[] content = null;
					if (document.getId() == -1) {
						content = document.getContent();
					} else {
						content = loader.loadDocumentContent(document.getId(),
								document.getDocumentFormat());
					}
					Viewer v = getViewer(document.getDocumentFormat(), content);
					setViewer(v);
					v.doAfterInstall();
				} catch (DataStoreException e) {
					LOGGER.error(e.getMessage());
					removeViewer();
				} finally {
					loadingComponent.setWaiting(false);
				}
			}

			private Viewer getViewer(DocumentFormat format, byte[] content) {
				Viewer v = viewersMap.get(viewerFormat.get(format));

				switch (format) {
				case FORMAT_PNG:
				case FORMAT_GIF:
				case FORMAT_JPG:
					if (v == null) {
						v = new ImageViewer();
						viewersMap.put(viewerFormat.get(format), v);
					}
					((ImageViewer) v).setImage(content);
					break;
				default:
					v = noViewerComponent;
				}

				return v;

			}
		});

	}

	private void clear() {
		titleJtextField.setText(null);
		commentTextArea.setText(null);
		// formatLabel.setText(null);
		formatLabel.setIcon(null);

		removeViewer();
		this.repaint();
		this.revalidate();
	}

	public void addDocumentModificationListener(
			DocumentModificationListener documentModificationListener) {
		this.documentModificationListeners.add(documentModificationListener);
	}

	public void removeDocumentModificationListener(
			DocumentModificationListener documentModificationListener) {
		this.documentModificationListeners.remove(documentModificationListener);
	}

	private void notifyTitleChanged(String title) {
		for (DocumentModificationListener listener : documentModificationListeners) {
			listener.documentTitleModified(title);
		}
	}

	private void notifyCommentChanged(String comment) {
		for (DocumentModificationListener listener : documentModificationListeners) {
			listener.documentCommentModified(comment);
		}
	}

	private class WaitingComponent extends Viewer {
		private static final long serialVersionUID = -1125283273656541342L;
		private JXBusyLabel busy;

		public WaitingComponent() {
			initWaiting();
		}

		private void initWaiting() {
			busy = new JXBusyLabel();
			busy.setPreferredSize(new Dimension(120, 120));
			busy.getBusyPainter().setBaseColor(Color.DARK_GRAY);
			this.setLayout(new FlowLayout(FlowLayout.CENTER));
			this.setOpaque(false);
			this.add(busy);
		}

		public void setWaiting(boolean waiting) {
			busy.setBusy(waiting);
		}
	}

	public String getDocumentTitle() {
		return titleJtextField.getText();
	}

	public String getDocumentComment() {
		return commentTextArea.getText();
	}
}
