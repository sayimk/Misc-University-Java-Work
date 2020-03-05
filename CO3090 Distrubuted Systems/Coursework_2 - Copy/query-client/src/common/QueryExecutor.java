package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface QueryExecutor extends Remote {
	
	Object executeQuery(Query query) throws RemoteException;

}
