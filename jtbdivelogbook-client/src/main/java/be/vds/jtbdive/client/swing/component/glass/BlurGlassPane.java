package be.vds.jtbdive.client.swing.component.glass;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

public class BlurGlassPane extends JPanel {
    private BufferedImage image;
    private BufferedImage imageA;
    private int radius = 0;
    private StackBlurFilter blurFilter;
    private int iterations = 1;
    private boolean repaint;
    private JFrame frame;

    public BlurGlassPane(JFrame frame, int radius, int iteration) {
	this.radius = radius;
	this.iterations = iteration;
	this.frame = frame;
	blurFilter = new StackBlurFilter(radius, iterations);
    }

    public void activate(final boolean b) {
	if (b)
	    createImages();
	repaint = true;
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		setVisible(b);
	    }
	});
    }

    private void createImages() {
	JRootPane root = frame.getRootPane();
	imageA = GraphicsUtilities.createCompatibleImage(root.getWidth(), root.getHeight());
	Graphics2D g2 = imageA.createGraphics();
	root.paint(g2);
	g2.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
	paintForBlur(g);
    }

    private void paintForBlur(Graphics g) {
	if (imageA == null)
	    createImages();

	if (image == null) {
	    image = new BufferedImage(imageA.getWidth(), imageA.getHeight(),
		    BufferedImage.TYPE_INT_ARGB);
	    repaint = true;
	}

	if (repaint) {
	    Graphics2D g2 = image.createGraphics();
	    g2.setComposite(AlphaComposite.Clear);
	    g2.fillRect(0, 0, image.getWidth(), image.getHeight());

	    g2.setComposite(AlphaComposite.Src);

	    if (radius > 0) {
//		long start = System.nanoTime();

		g2.drawImage(imageA, blurFilter, 0, 0);

//		long delay = System.nanoTime() - start;
//		System.out.println("time = " + (delay / 1000.0f / 1000.0f) + "ms");
	    } else {
		g2.drawImage(imageA, null, 0, 0);
	    }
	    g2.dispose();

	    repaint = false;
	}

	int x = (getWidth() - image.getWidth()) / 2;
	int y = (getHeight() - image.getHeight()) / 2;
	g.drawImage(image, x, y, null);
    }


    public void setRadius(int radius) {
	this.radius = radius;
	this.blurFilter = new StackBlurFilter(radius, iterations);
	repaint = true;
	repaint();
    }

    public void setIterations(int iterations) {
	this.iterations = iterations;
	this.blurFilter = new StackBlurFilter(radius, iterations);
	repaint = true;
	repaint();
    }
}
