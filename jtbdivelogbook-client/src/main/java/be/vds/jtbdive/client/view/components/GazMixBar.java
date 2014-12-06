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
package be.vds.jtbdive.client.view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import be.smd.i18n.swing.I18nButton;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.view.core.modifieddives.ModificationListenableJPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.GazMix;
import be.vds.jtbdive.core.core.catalogs.Gaz;
import be.vds.jtbdive.core.core.comparator.GazFormulaComparator;

public class GazMixBar extends ModificationListenableJPanel {

	private static final long serialVersionUID = 1795252661530742476L;
	private GazMix gazMix;
	private static Map<Gaz, Color> gazColors;
	private int gazGap;
	private Color barBackGround = UIAgent.getInstance()
			.getColorGazBackground();
	private Color borderColor = UIAgent.getInstance().getColorGazBorder();
	private Dimension barPreferredSize, barMinimumSize, barMaximumSize;
	private int gazAlpha;
	private JPanel gazPanel;

	public GazMixBar() {
		this(5, 80);
	}

	public GazMixBar(int gazGap, int gazAlpha) {
		this.gazGap = gazGap;
		this.gazAlpha = gazAlpha;
		initGazColors();
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		this.add(createGazPanel(), BorderLayout.CENTER);
		this.add(createButonsPanel(), BorderLayout.SOUTH);
	}

	private Component createGazPanel() {
		gazPanel = new JPanel();
		gazPanel.setOpaque(false);
		gazPanel.setLayout(new BoxLayout(gazPanel, BoxLayout.PAGE_AXIS));
		return gazPanel;
	}

	private Component createButonsPanel() {
		I18nButton b = new I18nButton(new AbstractAction() {

			private static final long serialVersionUID = 3969979558179166587L;

			@Override
			public void actionPerformed(ActionEvent e) {
				GazMixChooserDialog dialog = new GazMixChooserDialog(
						WindowUtils.getParentFrame(GazMixBar.this), "gaz", true);
				dialog.setGazMix(gazMix);
				dialog.setSize(200, 300);
				WindowUtils.centerWindow(dialog);

				int i = dialog.showDialog();
				if (i == GazMixChooserDialog.OPTION_OK) {
					setGazMix(dialog.getGazMix());
					repaint();
					revalidate();
					notifyModificationListeners(true);
				}
			}
		});
		b.setIcon(
				UIAgent.getInstance().getIcon(UIAgent.ICON_GAZ_MASK_24));
		b.setTooltipTextBundleKey("edit");
		b.setPreferredSize(new Dimension(30, 30));
		b.setBorderPainted(false);
		b.setContentAreaFilled(false);

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		buttonsPanel.setOpaque(false);
		buttonsPanel.add(b);
		return buttonsPanel;
	}

	private Component createGazPanel(final Gaz gaz) {
		JPanel p = new JPanel() {

			private static final long serialVersionUID = 1186065143679026480L;

			@Override
			protected void paintComponent(Graphics grphcs) {
				super.paintComponent(grphcs);
				double width = getWidth()
						* ((double) gazMix.getPercentage(gaz) / 100);
				Graphics2D g2 = (Graphics2D) grphcs;

				Color c = gazColors.get(gaz);
				if (c == null) {
					c = Color.GREEN;
				}
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				Color c2 = new Color(red, green, blue, gazAlpha);

				int height = getHeight();

				g2.setPaint(new GradientPaint(0, height / 2, c, (float) width,
						height / 2, c2));
				g2.fillRect(0, 0, (int) width, height);
			}
		};

		if (null != barPreferredSize)
			p.setPreferredSize(barPreferredSize);
		if (null != barMinimumSize)
			p.setMinimumSize(barMinimumSize);
		if (null != barMaximumSize)
			p.setMaximumSize(barMaximumSize);

		String gazString = gaz.getFormule() + " : " + gazMix.getPercentage(gaz)
				+ "%";
		p.add(new JLabel(gazString));

		p.setBackground(barBackGround);
		p.setOpaque(true);
		p.setBorder(new LineBorder(borderColor));
		p.setName("gaz:" + gaz.getFormule());
		return p;
	}

	public void setGazMix(GazMix gazMix) {
		this.gazMix = gazMix;
		gazPanel.removeAll();

		if (gazMix != null) {
			List<Gaz> list = gazMix.getGazes();
			Collections.sort(list, new GazFormulaComparator());

			int size = list.size();
			int i = 0;
			for (final Gaz gaz : list) {
				gazPanel.add(createGazPanel(gaz));
				if (i < size - 1)
					gazPanel.add(Box.createVerticalStrut(gazGap));
				i++;
			}
		}
	}

	public GazMix getGazMix() {
		return gazMix;
	}

	public void setBarPreferedSize(Dimension dimension) {
		this.barPreferredSize = dimension;
	}

	public void setBarMinimumSize(Dimension dimension) {
		this.barMinimumSize = dimension;
	}

	public void setBarMaximumSize(Dimension dimension) {
		this.barMaximumSize = dimension;
	}

	private void initGazColors() {
		gazColors = new HashMap<Gaz, Color>();
		gazColors.put(Gaz.GAZ_OXYGEN, UIAgent.getInstance()
				.getColorGazOxygen());
		gazColors.put(Gaz.GAZ_NITROGEN, UIAgent.getInstance()
				.getColorGazNitrogen());
		gazColors.put(Gaz.GAZ_HELIUM, UIAgent.getInstance()
				.getColorGazHelium());
		gazColors.put(Gaz.GAZ_HYDROGEN, UIAgent.getInstance()
				.getColorGazHydrogen());
		gazColors.put(Gaz.GAZ_NEON, UIAgent.getInstance()
				.getColorGazNeon());
		gazColors.put(Gaz.GAZ_ARGON, UIAgent.getInstance()
				.getColorGazArgon());
	}

	// //////////////////////////////////////////////////////
	public static void main(String[] args) {
		GazMix mix = new GazMix();
		mix.addGaz(Gaz.GAZ_OXYGEN, 20);
		mix.addGaz(Gaz.GAZ_NITROGEN, 75);
		mix.addGaz(Gaz.GAZ_HELIUM, 5);

		GazMixBar gazMixComp = new GazMixBar();
		gazMixComp.setGazMix(mix);

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(gazMixComp);
		f.setSize(200, 200);
		f.setLocationRelativeTo(null);
		f.setVisible(true);

	}
}
