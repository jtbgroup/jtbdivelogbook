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
package be.vds.jtbdive.client.view.core.slideshow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import be.vds.jtb.swing.component.reflection.ReflectionPane;
import be.vds.jtb.swing.component.reflection.ReflectionRenderer;
import be.vds.jtb.swing.utils.GraphicsUtilities;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.logging.Syslog;

public class SlideShowDialog extends JDialog {

	private static final long serialVersionUID = -2386426142937640257L;
	private static final int CTRL_PANEL_HEIGHT = 50;
	private static final int EAST_BORDER = 20;
	private static final int WEST_BORDER = 20;
	private static final int NORTH_BORDER = 20;
	private static final int SLIDE_PANEL_HEIGHT = 500;

	protected static final Syslog LOGGER = Syslog
			.getLogger(SlideShowDialog.class);

	// Configurable properties
	private boolean disableButtonsDuringSlideShow = true;
	private int slideShowTimer = 3;
	private boolean useReflection = false;

	private SlideWalker slideWalker;
	private JComponent currentSlide;
	private JPanel sliderPanel;
	private boolean isSlideActive;
	private JButton slideCtrlButton;
	private JButton nextButton;
	private JButton previousButton;
	private Thread slideThread;

	public SlideShowDialog(SlideWalker slideWalker) {
		this.slideWalker = slideWalker;
		init();
	}

	public boolean isUseReflection() {
		return useReflection;
	}

	public void setUseReflection(boolean useReflection) {
		this.useReflection = useReflection;
	}

	public int getSlideShowTimer() {
		return slideShowTimer;
	}

	public void setSlideShowTimer(int slideShowTimer) {
		this.slideShowTimer = slideShowTimer;
	}

	public boolean isDisableButtonsDuringSlideSHow() {
		return disableButtonsDuringSlideShow;
	}

	public void setDisableButtonsDuringSlideSHow(
			boolean disableButtonsDuringSlideSHow) {
		this.disableButtonsDuringSlideShow = disableButtonsDuringSlideSHow;
	}

	private void init() {
		this.setSize(SLIDE_PANEL_HEIGHT + EAST_BORDER + WEST_BORDER,
				SLIDE_PANEL_HEIGHT + CTRL_PANEL_HEIGHT);
		this.getContentPane().add(createContentPane());

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				currentSlide = createImagePanel(slideWalker.getCurrentSlide());
				showSlide();
			}
		});
	}

	private Component createContentPane() {
		JPanel p = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = 244317775408028747L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				Graphics2D g2d = (Graphics2D) g;

				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setPaint(new GradientPaint(getWidth() / 2, 0, UIAgent
						.getInstance().getColorSlideShowBkgTop(),
						getWidth() / 2, getHeight(), UIAgent.getInstance()
								.getColorSlideShowBkgBottom()));
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};

		p.add(Box.createVerticalStrut(NORTH_BORDER), BorderLayout.NORTH);
		p.add(Box.createHorizontalStrut(EAST_BORDER), BorderLayout.EAST);
		p.add(Box.createHorizontalStrut(WEST_BORDER), BorderLayout.WEST);
		p.add(createSliderPanel(), BorderLayout.CENTER);
		p.add(createControlButtonsPanel(), BorderLayout.SOUTH);
		return p;
	}

	private Component createSliderPanel() {
		sliderPanel = new JPanel();
		sliderPanel.setLayout(new BorderLayout());
		sliderPanel.setOpaque(false);

		BufferedImage bi = slideWalker.getCurrentSlide();
		currentSlide = createImagePanel(bi);

		showSlide();

		return sliderPanel;
	}

	private void showSlide() {
		sliderPanel.removeAll();

		sliderPanel.add(currentSlide, BorderLayout.CENTER);

		sliderPanel.revalidate();
		sliderPanel.repaint();
	}

	private JComponent createImagePanel(BufferedImage bufferedImage) {
		if (useReflection) {
			return createReflectionPanel(bufferedImage);
		} else {
			return createSimpleImagePanel(bufferedImage);
		}

	}

	private JComponent createSimpleImagePanel(BufferedImage bufferedImage) {
		JComponent r = null;

		int referenceWidth = getWidth();
		int referenceHeight = getHeight();
		int bufferedWidth = bufferedImage.getWidth();
		int bufferedHeight = bufferedImage.getHeight();

		if (bufferedWidth < referenceWidth && bufferedHeight < referenceHeight) {
			r = new JLabel(new ImageIcon(bufferedImage));
			r.setPreferredSize(new Dimension(bufferedWidth, bufferedHeight));
		} else {
			double whratio1 = ((double)referenceHeight/(double)referenceWidth);
			double whratio2 = ((double)bufferedHeight/(double)bufferedWidth);
			if (whratio1<whratio2) {
				// we base the new sizes on heigth.
				int newHeight = getHeight()-NORTH_BORDER-CTRL_PANEL_HEIGHT;
				double ratio = (double) bufferedHeight / (double) newHeight;
				int newWidth = (int) (bufferedWidth / ratio);
				r = new JLabel(new ImageIcon(GraphicsUtilities.createThumbnail(
						bufferedImage, newWidth, newHeight)));
				r.setPreferredSize(new Dimension(newWidth, (int) (newHeight)));
			} else {
				int newWidth = getWidth()-EAST_BORDER-WEST_BORDER;
				double ratio = (double) bufferedWidth
						/ (double) newWidth;
				int newHeigth = (int) (bufferedHeight / ratio);

				r = new JLabel(new ImageIcon(GraphicsUtilities.createThumbnail(
						bufferedImage, newWidth, newHeigth)));
				r.setPreferredSize(new Dimension(newWidth, newHeigth));
			}
		}

		return r;
	}

	private JComponent createReflectionPanel(BufferedImage bufferedImage) {
		JComponent r = null;
		float reflectedImageFactor = 0.1f;
		float opacity = 0.5f;
		boolean blurEffet = false;
		ReflectionRenderer renderer = new ReflectionRenderer(opacity,
				reflectedImageFactor, blurEffet);

		int referenceWidth = getWidth();
		int referenceHeight = getHeight() - CTRL_PANEL_HEIGHT - NORTH_BORDER;
		int bufferedWidth = bufferedImage.getWidth();
		int bufferedHeight = bufferedImage.getHeight();
		int totalImageHeight = (int) (bufferedHeight + (bufferedHeight * reflectedImageFactor));

		if (bufferedWidth < referenceWidth
				&& totalImageHeight < referenceHeight) {
			r = new ReflectionPane(bufferedImage, renderer);
			r.setPreferredSize(new Dimension(bufferedWidth, totalImageHeight));
		} else {
			double whratio1 = ((double)referenceHeight/(double)referenceWidth);
			double whratio2 = ((double)totalImageHeight/(double)bufferedWidth);
			if (whratio1<whratio2) {
				int newHeight = getHeight()-NORTH_BORDER-CTRL_PANEL_HEIGHT;
				double ratio = (double) totalImageHeight / (double) newHeight;
				int newWidth = (int) (bufferedWidth / ratio);
				
				int thumbHeight = (int) (bufferedHeight / ratio);
				
				r = new ReflectionPane(GraphicsUtilities.createThumbnail(
						bufferedImage, newWidth, thumbHeight), renderer);
				r.setPreferredSize(new Dimension(newWidth, newHeight));
			} else {
				int newWidth = getWidth()-EAST_BORDER-WEST_BORDER;
				double ratio = (double) bufferedWidth
						/ (double) newWidth;
				int newHeigth = (int) (totalImageHeight / ratio);

				int thumbWidth = (int) (bufferedWidth / ratio);
				
				r = new ReflectionPane(GraphicsUtilities.createThumbnail(
						bufferedImage, thumbWidth, newHeigth), renderer);
				r.setPreferredSize(new Dimension(newWidth, newHeigth));
			}
		}

		return r;
	}

	private void displaySlide(BufferedImage bufferedImage) {
		currentSlide = createImagePanel(bufferedImage);
		showSlide();
	}

	private Component createControlButtonsPanel() {
		previousButton = new JButton(new AbstractAction() {

			private static final long serialVersionUID = -3924466471859450211L;

			@Override
			public void actionPerformed(ActionEvent e) {
				displaySlide(slideWalker.getPreviousSlide());
			}
		});
		previousButton.setIcon(
				UIAgent.getInstance().getIcon(UIAgent.ICON_BTN_PREVIOUS));

		nextButton = new JButton(new AbstractAction() {

			private static final long serialVersionUID = -7094823824119930170L;

			@Override
			public void actionPerformed(ActionEvent e) {
				displaySlide(slideWalker.getNextSlide());
			}
		});
		nextButton
				.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_BTN_NEXT));

		slideCtrlButton = new JButton(new AbstractAction() {

			private static final long serialVersionUID = -8349302301459249107L;

			@Override
			public void actionPerformed(ActionEvent e) {
				toggleSlideSHow();
			}

		});
		slideCtrlButton.setIcon(
				UIAgent.getInstance().getIcon(UIAgent.ICON_BTN_PLAY));

		JPanel ctrlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		ctrlPanel.setOpaque(false);

		formatButtons();

		ctrlPanel.add(previousButton);
		ctrlPanel.add(slideCtrlButton);
		ctrlPanel.add(nextButton);

		ctrlPanel.setPreferredSize(new Dimension(300, CTRL_PANEL_HEIGHT));
		ctrlPanel.setMinimumSize(new Dimension(50, CTRL_PANEL_HEIGHT));
		ctrlPanel.setMaximumSize(new Dimension(500, CTRL_PANEL_HEIGHT));
		return ctrlPanel;
	}

	private void formatButtons() {
		previousButton.setBorderPainted(false);
		nextButton.setBorderPainted(false);
		slideCtrlButton.setBorderPainted(false);

		previousButton.setContentAreaFilled(false);
		nextButton.setContentAreaFilled(false);
		slideCtrlButton.setContentAreaFilled(false);

		previousButton.setFocusable(false);
		nextButton.setFocusable(false);
		slideCtrlButton.setFocusable(false);
	}

	private void toggleSlideSHow() {
		if (disableButtonsDuringSlideShow) {
			previousButton.setEnabled(isSlideActive);
			nextButton.setEnabled(isSlideActive);
		}

		if (isSlideActive) {
			slideThread.interrupt();
			slideThread = null;
			isSlideActive = false;
			slideCtrlButton.setIcon(
					UIAgent.getInstance().getIcon(UIAgent.ICON_BTN_PLAY));
		} else {
			slideThread = new Thread(new Runnable() {
				@Override
				public void run() {
					isSlideActive = true;
					slideCtrlButton.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_BTN_PAUSE));
					while (isSlideActive) {
						try {
							Thread.sleep(slideShowTimer * 1000);
						} catch (InterruptedException e) {
							LOGGER.debug("Thread sleep has been interupted");
						}
						displaySlide(slideWalker.getNextSlide());
					}
				}
			});
			slideThread.start();
		}
	}

	public static void main(String[] args) {
		SlideShowDialog d = new SlideShowDialog(new DummySlideWalker());
		d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		d.setLocationRelativeTo(null);
		d.setVisible(true);
	}
}
