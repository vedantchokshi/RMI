import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
/*
 * NotificationSinkController acts as the controller between the NotificationSink and the NotificationSinkGUI
 */
public class NotificationSinkController {
	private int registrySize = 0;
	private NotificationSinkInterface sink;
	private NotificationSinkGUI GUI;
	private String sinkName;
	
	//Constructor
	public NotificationSinkController(NotificationSinkInterface sink, NotificationSinkGUI GUI) throws AccessException, RemoteException {
		this.sink = sink;
		this.GUI = GUI;
		
		//Adds action listener to the RegisterSink button
		this.GUI.getRegisterSink().addActionListener(new RegisterUserLisener());
	}
	
	//Populates all the SinkGUI with a list of JCheckBoxes of all the available sources that the sink can recieve alerts from
	public void populateSources() throws AccessException, RemoteException {
		Map<String, JCheckBox> components = new HashMap<String, JCheckBox>();
		for(String sourceName : LocateRegistry.getRegistry("localhost", 9999).list()) {
			NotificationSourceInterface source;
			try {
				source = (NotificationSourceInterface) LocateRegistry.getRegistry("localhost", 9999).lookup(sourceName);
				JCheckBox checkbox = new JCheckBox(sourceName);
				checkbox.setSelected(source.containsSink(sinkName));
				components.put(sourceName, checkbox);
				components.get(sourceName).addItemListener(new SourceLisenter(sourceName));
				GUI.addToSourcePanel(components.get(sourceName));
				GUI.revalidate();
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	//Adds notification to the GUI
	public void addNotification(Notification notification) {
		GUI.addRowToTableModel(new Object[] {notification.getDate(), notification.getSource(), notification.getAlert()});
	}
	
	//ActionListener for Registering a user
	class RegisterUserLisener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			sinkName = GUI.getUsername().getText();
			if(sinkName.equals("")) {
				JOptionPane.showMessageDialog(GUI, "Username cannot be empty", "Error", JOptionPane.INFORMATION_MESSAGE);
			} else {
				GUI.addWindowListener(new GUIWindowListener());
				GUI.notificationsScreen();
				try {
					//Tries to read file for the sink
					File sinksFile = new File("Sinks/" + sinkName + ".txt");
					//If exists, re-register to all the sources the user was previously connected to
					if(sinksFile.exists()) {
						FileInputStream sinks = new FileInputStream(sinksFile);
						ObjectInputStream sinkIn = new ObjectInputStream(sinks);
						@SuppressWarnings("unchecked")
						Set<String> sourcesConnected = (Set<String>) sinkIn.readObject();
						NotificationSourceInterface source;
						for(String sourceName : sourcesConnected) {
							if(Arrays.asList(LocateRegistry.getRegistry("localhost", 9999).list()).contains(sourceName)) {
								GUI.getTextArea().append("Attempting to retreive any missed notifications");
								source = (NotificationSourceInterface) LocateRegistry.getRegistry("localhost", 9999).lookup(sourceName);
								sink.setSinkName(sinkName);
								sink.registerSink(source);
							}							
						}
						sinkIn.close();
					}				
				} catch (IOException | ClassNotFoundException | NotBoundException e1) {
					e1.printStackTrace();
				}
				//Thread to constantly update JCheckBoxes to see the sources available to subscribe to
				new Thread(new UpdateSources()).start();
			}
		}
	}
	
	//Subscribes and unsubscribes from source according to what is ticked in the JCheckBox
	class SourceLisenter implements ItemListener {
		private String sourceName;
		private NotificationSourceInterface source;
		
		public SourceLisenter(String sourceName) {
			this.sourceName = sourceName;
		}
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			try {
				source = (NotificationSourceInterface) LocateRegistry.getRegistry("localhost", 9999).lookup(sourceName);
			} catch (RemoteException | NotBoundException e2) {
				e2.printStackTrace();
			}
			if(e.getStateChange() == ItemEvent.SELECTED) {
					try {
						sink.setSinkName(sinkName);
						sink.registerSink(source);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}					
					GUI.getTextArea().append("Connected to " + sourceName + "\n");
			} else if(e.getStateChange() == ItemEvent.DESELECTED) {
				try {
					sink.unregisterSink(source);
					GUI.getTextArea().append("Disconnected from " + sourceName + "\n");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		}		
	}
	
	//WindowAdapater which saves the set of sources the sink is connected to when closing
	class GUIWindowListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			File dir = new File("Sinks");
			if(!dir.exists()){
				dir.mkdir();
			}
			try {
				FileOutputStream sinks = new FileOutputStream(dir + "/" + GUI.getUsername().getText() + ".txt");
				ObjectOutputStream sinkOut = new ObjectOutputStream(sinks);
				sinkOut.writeObject(sink.getSourcesConnected());
				sinkOut.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//Checks for changes in the registry
	class UpdateSources implements Runnable {
		private int currentSize;
		
		public int updateSize() throws AccessException, RemoteException {
			return LocateRegistry.getRegistry("localhost", 9999).list().length;
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					currentSize = updateSize();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				if(!(registrySize == currentSize)) {
					try {
						GUI.clearSourcePanel();
						populateSources();
						registrySize = currentSize;
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
