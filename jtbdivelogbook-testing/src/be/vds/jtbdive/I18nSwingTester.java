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
package be.vds.jtbdive;

import java.awt.Component;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtbdive.client.util.LanguageHelper;

public class I18nSwingTester {
	public static void main(String[] args) {
		new I18nSwingTester().run();
	}

	private void run() {
		I18nResourceManager i18nManager = I18nResourceManager.sharedInstance();
		i18nManager.setDefaultLocale(LanguageHelper.LOCALE_ENGLISH);
		i18nManager.addBundle("resources/bundles/Bundle",
				new Locale[] { LanguageHelper.LOCALE_ENGLISH });

		long start = System.currentTimeMillis();
		launchI18n();
		long stop = System.currentTimeMillis();
//		launchSwing();
		long end = System.currentTimeMillis();

		System.out.println("Time for i18n : " + (stop - start));
		System.out.println("Time for swing : " + (end - stop));

	}

	private void launchSwing() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		for (int i = 0; i < 100; i++) {
			p.add(new JLabel(I18nResourceManager.sharedInstance().getString(
					"cancel")));
		}
		createFrame(p);
	}

	private void launchI18n() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		for (int i = 0; i < 100; i++) {
			p.add(new I18nLabel("cancel"));
		}
		createFrame(p);
	}

	private void createFrame(JComponent content) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new JScrollPane(content));
		f.setSize(500, 500);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

}
