import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

//Creates the registry on port 9999 which objects and binded to and remotely accessed
public class RegistryRun {
	public static void main(String[] args) throws RemoteException {
		@SuppressWarnings("unused")
		Registry registry = LocateRegistry.createRegistry(9999);
		while(true) { }
	}	
}
