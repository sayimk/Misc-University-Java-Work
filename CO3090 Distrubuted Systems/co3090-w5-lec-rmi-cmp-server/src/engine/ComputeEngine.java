package engine;

import compute.*;
import java.rmi.*;
import java.rmi.server.*;

public class ComputeEngine extends UnicastRemoteObject
                           implements Compute
{
    public ComputeEngine() throws RemoteException {
        super();
    }

    public Object executeTask(Task t) throws RemoteException {
        return t.execute();
    }

    public static void main(String[] args) {
	if (System.getSecurityManager() == null) {
	    System.setSecurityManager(new RMISecurityManager());
	}
	
	/*System.setProperty("java.rmi.server.codebase", "https://campus.cs.le.ac.uk/people/sk619/co3090/shared.jar");
      System.setProperty("java.rmi.server.useCodebaseOnly","false");
      System.setProperty("java.security.policy","file:/s_home/sk619/Documents/policy.permission");
	*/
	
	
	String name = "rmi://localhost/Compute";
	try {
	    Compute engine = new ComputeEngine();
	    Naming.rebind(name, engine);
	    System.out.println("ComputeEngine bound");
	} catch (Exception e) {
	    System.err.println("ComputeEngine exception: " + 
			       e.getMessage());
	    e.printStackTrace();
	}
    }
}
