package be.vds.jtbdive;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class RoundedComponent extends JComponent {

	private static final long serialVersionUID = 1147503263818825415L;

	@Override
	public void paintComponent(Graphics g) {
		int w = getWidth();
		int h = getHeight();
//		setComponentBounds(w, h);

		BufferedImage coloredImage = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2Square = (Graphics2D) coloredImage.getGraphics();
		g2Square.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2Square.setPaint(Color.BLACK);
		g2Square.fillRect(0, 0, w, h);
		g2Square.dispose();

		BufferedImage canevasImage = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2Image = (Graphics2D) canevasImage.getGraphics();
		g2Image.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
//		g2Image.fillArc(0, 0, h, h, 90, 180);
//		g2Image.fillRect(h/2, 0, w-h, h);
//		g2Image.fillArc(w - h - 1, 0, h, h, 270, 180);
		g2Image.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
		g2Image.dispose();

		BufferedImage mixImage = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2Mix = (Graphics2D) mixImage.getGraphics();
		g2Mix.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Composite oldComposite = g2Mix.getComposite();
		g2Mix.drawImage(canevasImage, 0, 0, null);
		g2Mix.setComposite(AlphaComposite.SrcIn);
		g2Mix.drawImage(coloredImage, 0, 0, null);
		g2Mix.setComposite(oldComposite);

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(mixImage, 0, 0, null);
	}

//	private void setComponentBounds(int width, int height) {
//		p.setBounds(height / 2, 0, width - height, height);
//	}

	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new RoundedComponent());
		f.setSize(400, 400);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}
