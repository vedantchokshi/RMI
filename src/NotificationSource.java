import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/*
 * NotificationSource to be remotely accessed. Implements NotificationSourceInterface
 */
public class NotificationSource extends UnicastRemoteObject implements NotificationSourceInterface {
	private String sourceName;
	private HashMap<String, NotificationSinkInterface> sinks;
	private HashMap<String, Queue<Notification>> notificationStorage;
	
	//Constructor
	protected NotificationSource() throws RemoteException {
		super();
		sinks = new HashMap<String, NotificationSinkInterface>();
		this.notificationStorage = new HashMap<String, Queue<Notification>>();
	}
	
	//Binds the instance of NotificationSource to the String passed in as the argument. Only binds if the source doesn't already exist.
	@Override
	public boolean bind(String sourceName) throws RemoteException {
		if(Arrays.asList(LocateRegistry.getRegistry("localhost", 9999).list()).contains(sourceName)) {
			return false;
		} else {
			this.sourceName = sourceName;
			LocateRegistry.getRegistry("localhost", 9999).rebind(sourceName, this);
			return true;
		}
	}

	//Unbinds the instance Notification Source from the registry
	@Override
	public void unbind() throws RemoteException {
		try {
			LocateRegistry.getRegistry("localhost", 9999).unbind(sourceName);
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	//Registers a sink to the source
	@Override
	public void registerSink(NotificationSinkInterface sink) throws RemoteException {
		Queue<Notification> notificationStorage = this.notificationStorage.get(sink.getSinkName());
		//If sink already has registered and there are notifications which have not been delivered because the sink was disconnected
		if(sinks.containsKey(sink.getSinkName()) && !notificationStorage.isEmpty()) {
			//Overwrite current instance of NotificationSinkInterface in the HashMap sinks
			sinks.put(sink.getSinkName(), sink);
			//Broadcast all the notifications in the queue
			while(!notificationStorage.isEmpty()) {
				Notification s = notificationStorage.remove();
				broadcastNotification(s);
			}
			//Reset queue
			this.notificationStorage.put(sink.getSinkName(), new LinkedList<>());
		//else register sink and create a notification storage for the sink for when it disconnects
		} else {
			sinks.put(sink.getSinkName(), sink);
			this.notificationStorage.put(sink.getSinkName(), new LinkedList<>());
		}
	}

	//Unregisters sink by removing it from the sinks and notificationStorage HashMaps
	@Override
	public void unregisterSink(NotificationSinkInterface sink) throws RemoteException {
		sinks.remove(sink.getSinkName());	
		this.notificationStorage.remove(sink.getSinkName());
	}

	//Loops over each sink that is registered to the source and sends the notification
	@Override
	public  void broadcastNotification(Notification notification) throws RemoteException {
		for(String sinkName : sinks.keySet()) {
			//If the try statement returns and error, it means that the sink has disconnected
			try {
				sinks.get(sinkName).retrieveNotification(notification);
			} catch (RemoteException e) {
				//so add the notificationStorage of that sink so when connected, the notifications can be redelivered
				Queue<Notification> notificationStorage = this.notificationStorage.get(sinkName);
				notificationStorage.add(notification);
				this.notificationStorage.put(sinkName, notificationStorage);
			}			
		}
	}

	//Returns source name
	@Override
	public String getSourceName() {
		return sourceName;
	}
	
	//Returns true if a sink is connected to the source
	@Override
	public boolean containsSink(String sinkName) {
		return sinks.containsKey(sinkName);
	}
}
