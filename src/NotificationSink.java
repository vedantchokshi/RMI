import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

/*
 * Defines structure of a NotificationSink
 */
public class NotificationSink extends UnicastRemoteObject implements NotificationSinkInterface {
	private NotificationSinkController controller;
	private Set<String> sourcesConnected;
	private String sinkName;
	
	//Constructor
	protected NotificationSink() throws RemoteException {
		super();
		this.sourcesConnected = new HashSet<String>();
	}
	
	//Sets controller
	public void setController(NotificationSinkController controller) {
		this.controller = controller;
	}
	
	
	//Register sink to a source
	@Override 
	public void registerSink(NotificationSourceInterface source) throws RemoteException {
		sourcesConnected.add(source.getSourceName());
		source.registerSink(this);
	}

	
	//Unregister sink to a source
	@Override
	public void unregisterSink(NotificationSourceInterface source) throws RemoteException {
		sourcesConnected.remove(source.getSourceName());
		source.unregisterSink(this);
	}

	//Method called my NotificationSource to send Notification to NotificationSink
	@Override
	public void retrieveNotification(Notification notification) throws RemoteException {
		controller.addNotification(notification);
	}
	
	
	//Returns sink name
	@Override
	public String getSinkName() {
		return sinkName;
	}

	//Sets sink name
	@Override
	public void setSinkName(String sinkName) {
		this.sinkName = sinkName;
	}
	
	//Returns set of sources the instance of Sink is connected to
	@Override
	public Set<String> getSourcesConnected() {
		return sourcesConnected;
	}
	
}
