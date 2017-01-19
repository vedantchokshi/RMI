import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

/*
 * NotificationSinkController acts as the controller between the NotificationSource and the NotificationSourceGUI
 */
public class NotificationSourceController {
	private NotificationSourceInterface source;
	private NotificationSourceGUI GUI;
	
	//Constructor
	public NotificationSourceController(NotificationSourceInterface source, NotificationSourceGUI GUI) {
		this.source = source;
		this.GUI = GUI;
		
		//Adds RegisterListener to the GUI register button
		this.GUI.getRegisterButton().addActionListener(new RegisterListener());
	}

	//Validates input when registering a new area/source
	class RegisterListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String text = GUI.getSourceName().getText();
			if(text.equals("")) {
				JOptionPane.showMessageDialog(GUI, "Please enter a valid area name to brodcast notifications from", "Error", JOptionPane.INFORMATION_MESSAGE);
			} else {
				try {
					//Binds source and updates GUI to sendNotificationScreen.
					if(source.bind(text)) {
						GUI.sendNotificationScreen(GUI.getSourceName().getText());
						GUI.getSendButton().addActionListener(new BroadcastNotificationsListener());
						GUI.addWindowListener(new UnbindSourceOnClose());
					} else {
						JOptionPane.showMessageDialog(GUI, "Source already exists", "Error", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	//Validates input and calls broadcast message in the source to send the message to the sink
	class BroadcastNotificationsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String text = GUI.getNotification().getText();
			if(text.equals("")) {
				JOptionPane.showMessageDialog(GUI, "Cannot send an empty notification", "Error", JOptionPane.INFORMATION_MESSAGE);
			} else {
				try {
					source.broadcastNotification((new Notification(source.getSourceName(), text)));
					GUI.getNotification().setText("");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	//Unbinds source from registry on close
	class UnbindSourceOnClose extends WindowAdapter {
		@Override
	    public void windowClosing(WindowEvent e) {
			try {
				source.unbind();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
	    }
	}
}
