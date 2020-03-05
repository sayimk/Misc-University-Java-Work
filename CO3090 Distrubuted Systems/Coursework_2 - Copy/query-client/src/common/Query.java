package common;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.stream.Stream;

public interface Query extends Serializable  {

	Object execute();
	
	void setDataStream(Stream<String> stream);
	
	String getFilename();
	
}
