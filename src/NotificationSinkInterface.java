import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

/*
 * Interface for NotificationSinkInterface which contains Remote Methods
 * Methods explained in NotificationSink which implements this interface
 */
public interface NotificationSinkInterface extends Remote {
	public void registerSink(NotificationSourceInterface source) throws RemoteException;
	
	public void unregisterSink(NotificationSourceInterface source) throws RemoteException;
	
	public void retrieveNotification(Notification notification) throws RemoteException;
	
	public String getSinkName() throws RemoteException;
	
	public void setSinkName(String sinkName) throws RemoteException;
	
	public Set<String> getSourcesConnected() throws RemoteException;
}
