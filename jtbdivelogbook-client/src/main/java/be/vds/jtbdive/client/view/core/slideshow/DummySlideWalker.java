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

import java.awt.image.BufferedImage;

import be.vds.jtbdive.client.util.ImageUtils;
import be.vds.jtbdive.client.util.ResourceManager;

public class DummySlideWalker implements SlideWalker {

	private int pointer;

	private String[] imgs = new String[4];
	{
		imgs[0] = "about.jpg";
		imgs[1] = "logo/logo_125.png";
		imgs[2] = "trashEmpty24.png";
		imgs[3] = "splash.png";
	}

	@Override
	public BufferedImage getNextSlide() {
		if (pointer == imgs.length - 1) {
			pointer = 0;
		} else {
			pointer++;
		}

		return getCurrentSlide();
	}

	@Override
	public BufferedImage getPreviousSlide() {
		if (pointer == 0) {
			pointer = imgs.length - 1;
		} else {
			pointer--;
		}
		return getCurrentSlide();
	}

	@Override
	public BufferedImage getCurrentSlide() {
		return ImageUtils.convertImageIconToBufferedImage(ResourceManager
				.getInstance().getImageIcon(imgs[pointer]));
	}

}
