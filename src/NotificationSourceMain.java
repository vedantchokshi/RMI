import java.awt.EventQueue;

import javax.swing.UnsupportedLookAndFeelException;

//Run RegistryRun before running
public class NotificationSourceMain {
	//Runs the source
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("unused")
			public void run() {
				try {
					javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) { }
				try {
					NotificationSourceInterface source = new NotificationSource();
					NotificationSourceGUI frame = new NotificationSourceGUI();
					NotificationSourceController controller = new NotificationSourceController(source, frame);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
