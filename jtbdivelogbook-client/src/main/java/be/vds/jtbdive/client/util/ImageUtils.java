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
package be.vds.jtbdive.client.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import be.vds.jtb.swing.utils.GraphicsUtilities;
import be.vds.jtbdive.core.logging.Syslog;

public class ImageUtils {

	private static final Syslog LOGGER = Syslog.getLogger(ImageUtils.class);

	public static BufferedImage getBufferedImageFitToHeight(ImageIcon icon,
			double height) {
		double originalHeight = icon.getIconHeight();
		double scale = height / originalHeight;
		return getScaledBufferedImage(icon, scale);
	}

	public static BufferedImage getBufferedImageFitToWidth(ImageIcon icon,
			double width) {
		double originalWidth = icon.getIconWidth();
		double scale = width / originalWidth;
		return getScaledBufferedImage(icon, scale);
	}

	public static BufferedImage getResizedBufferedImage(ImageIcon icon,
			double width) {
		double scale = width / icon.getIconWidth();
		return getScaledBufferedImage(icon, scale);
	}

	public static BufferedImage getScaledBufferedImage(ImageIcon icon,
			double scale) {
		int iw = icon.getIconWidth();
		int w = (int) (iw * scale);
		int h = (int) (icon.getIconHeight() * scale);
		BufferedImage outImage = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		AffineTransform trans = new AffineTransform();
		trans.scale(scale, scale);
		Graphics2D g = outImage.createGraphics();
		g.drawImage(icon.getImage(), trans, null);
		g.dispose();
		return outImage;
	}

	public static BufferedImage convertImageIconToBufferedImage(ImageIcon icon) {

		boolean hasAlpha = hasAlpha(icon);
		int transparency = hasAlpha ? Transparency.BITMASK
				: Transparency.OPAQUE;

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();
		BufferedImage bufferedImage = gc.createCompatibleImage(
				icon.getIconWidth(), icon.getIconHeight(), transparency);

		if (bufferedImage == null) {

			// if that failed then use the default color model

			int type = hasAlpha ? BufferedImage.TYPE_INT_ARGB
					: BufferedImage.TYPE_INT_RGB;
			bufferedImage = new BufferedImage(icon.getIconWidth(),
					icon.getIconHeight(), type);
		}

		// Copy image

		Graphics g = bufferedImage.createGraphics();
		g.drawImage(icon.getImage(), 0, 0, null);
		g.dispose();
		return bufferedImage;
	}

	public static BufferedImage convertImageToBufferedImage(Image image) {
		if (image instanceof BufferedImage)
			return (BufferedImage) image;

		boolean hasAlpha = hasAlpha(image);
		int transparency = hasAlpha ? Transparency.BITMASK
				: Transparency.OPAQUE;

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();
		BufferedImage bufferedImage = gc.createCompatibleImage(
				image.getWidth(null), image.getHeight(null), transparency);

		if (bufferedImage == null) {

			// if that failed then use the default color model

			int type = hasAlpha ? BufferedImage.TYPE_INT_ARGB
					: BufferedImage.TYPE_INT_RGB;
			bufferedImage = new BufferedImage(image.getWidth(null),
					image.getHeight(null), type);
		}

		// Copy image

		Graphics g = bufferedImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bufferedImage;
	}

	private static boolean hasAlpha(Image image) {
		boolean hasAlpha = false;
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
			if (pg.getColorModel() == null)
				return false;

			hasAlpha = pg.getColorModel().hasAlpha();
		} catch (InterruptedException e) {
			LOGGER.error(e);
		}
		return hasAlpha;
	}

	private static boolean hasAlpha(ImageIcon icon) {
		return hasAlpha(icon.getImage());
	}

	public static BufferedImage convertIconToBufferedImage(Icon icon) {
		/** On dessine l'icone dans un bufferedImage **/
		BufferedImage image = new BufferedImage(icon.getIconWidth(),
				icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		icon.paintIcon(null, image.getGraphics(), 0, 0);

		return GraphicsUtilities.createCompatibleImage(image);
	}
	
	public static BufferedImage getResizedBufferedImage(Image icon,
			double width) {
		double scale = width / icon.getWidth(null);
		return getScaledBufferedImage(icon, scale);
	}

	public static BufferedImage getScaledBufferedImage(Image icon,
			double scale) {
		int iw = icon.getWidth(null);
		int w = (int) (iw * scale);
		int h = (int) (icon.getHeight(null) * scale);
		BufferedImage outImage = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		AffineTransform trans = new AffineTransform();
		trans.scale(scale, scale);
		Graphics2D g = outImage.createGraphics();
		g.drawImage(icon, trans, null);
		g.dispose();
		return outImage;
	}

}
