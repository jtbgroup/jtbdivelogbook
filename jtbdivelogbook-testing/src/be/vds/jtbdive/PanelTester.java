package be.vds.jtbdive;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class PanelTester {

	public static void dropOnFrame(JComponent component) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(component);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}
