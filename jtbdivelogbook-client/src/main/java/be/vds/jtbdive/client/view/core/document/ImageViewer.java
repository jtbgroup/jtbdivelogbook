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
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.jdesktop.swingx.JXImageView;

import be.vds.jtbdive.client.view.utils.UIAgent;

public class ImageViewer extends Viewer {

	private static final long serialVersionUID = 7912205771625142808L;
	private JXImageView viewer;
	private JToggleButton bestFitButton;

	public ImageViewer() {
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		this.add(createViewerPanel(), BorderLayout.CENTER);
		this.add(createButtonsPanel(), BorderLayout.NORTH);

		this.addComponentListener(createComponentListener());
	}

	private ComponentListener createComponentListener() {
		return new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				adaptWhenBestFit();
			}

			@Override
			public void componentResized(ComponentEvent e) {
				adaptWhenBestFit();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}

		};

	}

	public void adaptWhenBestFit() {
		if (isKeepBestFit()) {
			fitBest();
		}
	}

	private Component createButtonsPanel() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEADING));
		p.add(createZoomInButton());
		p.add(createZoomOutButton());
		p.add(createRotateClockwiseButton());
		p.add(createRotateCounterClockwiseButton());
		p.add(createFitWidth());
		p.add(createFitHeight());
		p.add(createFitBest());

		return p;
	}

	private boolean isKeepBestFit() {
		return bestFitButton.isSelected();
	}

	private Component createFitBest() {
		bestFitButton = new JToggleButton(new AbstractAction() {
			private static final long serialVersionUID = -8298912352906413361L;

			public void actionPerformed(ActionEvent actionEvent) {
				fitBest();
			}
		});
		bestFitButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_FIT_BEST_16));
		return bestFitButton;
	}

	private Component createFitWidth() {
		JButton b = new JButton(new AbstractAction() {
			private static final long serialVersionUID = -8298912352906413361L;

			public void actionPerformed(ActionEvent actionEvent) {
				fitWidth();
			}
		});
		b.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_FIT_WIDTH_16));
		return b;
	}

	private Component createFitHeight() {
		JButton b = new JButton(new AbstractAction() {
			private static final long serialVersionUID = -8298912352906413361L;

			public void actionPerformed(ActionEvent actionEvent) {
				fitHeight();
			}
		});
		b.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_FIT_HEIGHT_16));
		return b;
	}

	private Component createZoomInButton() {
		JButton b = new JButton(new AbstractAction() {
			private static final long serialVersionUID = -190104991021244892L;

			public void actionPerformed(ActionEvent actionEvent) {
				viewer.setScale(viewer.getScale() * 2);
			}
		});

		b.setText(null);
		b.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_ZOOM_IN_16));
		return b;
	}

	private Component createZoomOutButton() {
		JButton b = new JButton(new AbstractAction() {
			private static final long serialVersionUID = 1145184475345389315L;

			public void actionPerformed(ActionEvent actionEvent) {
				viewer.setScale(viewer.getScale() * 0.5);
			}
		});
		b.setText(null);
		b.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_ZOOM_OUT_16));
		return b;
	}

	private Component createRotateClockwiseButton() {
		JButton b = new JButton(new AbstractAction() {
			private static final long serialVersionUID = 1025528829442465096L;

			public void actionPerformed(ActionEvent actionEvent) {
				double scale = viewer.getScale();
				Image img = viewer.getImage();
				BufferedImage src = new BufferedImage(img.getWidth(null),
						img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				BufferedImage dst = new BufferedImage(img.getHeight(null),
						img.getWidth(null), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = (Graphics2D) src.getGraphics();
				// smooth scaling
				g.drawImage(img, 0, 0, null);
				g.dispose();
				AffineTransform trans = AffineTransform.getRotateInstance(
						-Math.PI / 2, 0, 0);
				trans.translate(-src.getWidth(), 0);
				BufferedImageOp op = new AffineTransformOp(trans,
						AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				// Rectangle2D rect = op.getBounds2D(src);
				op.filter(src, dst);
				viewer.setImage(dst);
				viewer.setScale(scale);

				adaptWhenBestFit();
			}
		});
		b.setText(null);
		b.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_ROTATE_CLOCKWISE_16));
		return b;
	}

	private Component createRotateCounterClockwiseButton() {
		JButton b = new JButton(new AbstractAction() {

			private static final long serialVersionUID = -3877031247827718678L;

			@Override
			public void actionPerformed(ActionEvent e) {
				double scale = viewer.getScale();
				Image img = viewer.getImage();
				BufferedImage src = new BufferedImage(img.getWidth(null),
						img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				BufferedImage dst = new BufferedImage(img.getHeight(null),
						img.getWidth(null), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = (Graphics2D) src.getGraphics();
				// smooth scaling
				g.drawImage(img, 0, 0, null);
				g.dispose();
				AffineTransform trans = AffineTransform.getRotateInstance(
						Math.PI / 2, 0, 0);
				trans.translate(0, -src.getHeight());
				BufferedImageOp op = new AffineTransformOp(trans,
						AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				// Rectangle2D rect = op.getBounds2D(src);
				op.filter(src, dst);
				viewer.setImage(dst);
				viewer.setScale(scale);

				adaptWhenBestFit();
			}
		});
		b.setText(null);
		b.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_ROTATE_COUNTER_CLOCKWISE_16));
		return b;
	}

	private void fitWidthIfTooLarge() {
		if (viewer.getImage() != null) {

			double viewerW = (double) this.getParent().getWidth();
			double imageW = (double) viewer.getImage().getWidth(null);
			if (imageW > viewerW) {
				double scale = viewerW / imageW;
				if (scale > 0)
					viewer.setScale(scale);
			}
		}
	}

	private void fitBest() {
		if (viewer.getImage() != null) {
			double viewerW = (double) this.getParent().getWidth();
			double viewerH = (double) this.getParent().getHeight();
			double imageW = (double) viewer.getImage().getWidth(null);
			double imageH = (double) viewer.getImage().getHeight(null);

			if ((imageH - viewerH) > 0
					&& (imageH - viewerH) > (imageW - viewerW)) {
				fitHeight();
			} else if ((imageW - viewerW) > 0
					&& (imageW - viewerW) > (imageH - viewerH)) {
				fitWidth();
			}
		}
	}

	private void fitWidth() {
		if (viewer.getImage() != null) {
			double scale = (double) ImageViewer.this.getWidth()
					/ (double) viewer.getImage().getWidth(null);
			viewer.setScale(scale);
		}
	}

	private void fitHeight() {
		if (viewer.getImage() != null) {
			double scale = (double) ImageViewer.this.getHeight()
					/ (double) viewer.getImage().getHeight(null);
			viewer.setScale(scale);
		}
	}

	private Component createViewerPanel() {
		viewer = new JXImageView();
		return viewer;
	}

	public void setImage(byte[] content) {
		Image image = null;
		try {
			image = ImageIO.read(new ByteArrayInputStream(content));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewer.setImage(image);
		// fitWidth();
	}

	@Override
	public void doAfterInstall() {
		fitWidthIfTooLarge();
		adaptWhenBestFit();
	}
}
