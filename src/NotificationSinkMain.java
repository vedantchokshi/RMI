import java.awt.EventQueue;
import javax.swing.UnsupportedLookAndFeelException;

//Run RegistryRun before running
public class NotificationSinkMain {
	//Runs the Sink
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) { }
				try {
					NotificationSink sink = new NotificationSink();
					NotificationSinkGUI frame = new NotificationSinkGUI();
					NotificationSinkController controller = new NotificationSinkController(sink, frame);
					sink.setController(controller);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
