import java.rmi.*;
/*
 * Interface for NotificationSourceInterface which contains Remote Methods
 * Methods explained in NotificationSource which implements this interface
 */
public interface NotificationSourceInterface extends Remote {
	public boolean bind(String sourceName) throws RemoteException;
	
	public void unbind() throws RemoteException;
	
	public void registerSink(NotificationSinkInterface sink) throws RemoteException;
	
	public void unregisterSink(NotificationSinkInterface sink) throws RemoteException;
	
	public void broadcastNotification(Notification notification) throws RemoteException;
	
	public String getSourceName() throws RemoteException;
	
	public boolean containsSink(String sinkName) throws RemoteException;
}
