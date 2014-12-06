package be.vds.jtbdive;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtbdive.client.view.core.logbook.LogBookMetaDataPanel;

public class TestFrame extends JFrame {
	private static final long serialVersionUID = 4650930484699554297L;

	public TestFrame() {
		this.setName("test");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(createPanel());
		this.pack();
	}

	private Component createPanel() {
//		DiveParametersPanel panel = new DiveParametersPanel(this, null, null);
		LogBookMetaDataPanel panel = new LogBookMetaDataPanel();
//		return panel;
		
		
			JPanel contentPane = new JPanel();
			contentPane.setLayout(new BorderLayout());

			contentPane.add(createButtonsPanel(), BorderLayout.SOUTH);
			contentPane.add(panel, BorderLayout.CENTER);
			return contentPane;
	}

	private Component createButtonsPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.add(new I18nButton("cancel"));
		buttonsPanel.add(new I18nButton("qsdfsdf"));
		return buttonsPanel;
	}
	
	public static void main(String[] args) {
		
		initLAF();
		
		Locale fr = new Locale("fr", "FR");
		I18nResourceManager.sharedInstance().addBundle("bundles/Bundle",fr);
		I18nResourceManager.sharedInstance().setDefaultLocale(fr);
		
		TestFrame frame = new TestFrame();
		frame.setVisible(true);
	}
	
	private static void initLAF() {
		try {
			// splash.setText("Initializing Look & Feel...");
			// UIManager
			// .setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			// UIManager
			// .setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}
}
