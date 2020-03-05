package co3090.rmi.server;
import java.lang.SecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import co3090.rmi.share.Hello;

public class Server implements Hello {

    public String sayHello() throws RemoteException {
        return "Hello, world!";
    }
    public static void main(String args[]) {

        try { 	
        	
        	//these allow the required RMI setting to be set via System.setProperty instead of 
        	/*
        	 * java -cp ./bin/ -Djava.rmi.server.codebase="https://127.0.0.1/CO3090/shared.jar" -Djava.rmi.server.useCodebaseOnly=false -Djava.security.policy=file:$HOME/Documents/policy.permission co3090.rmi.server.Server

        	 * 
        	 */
        	System.setProperty("java.rmi.server.codebase", "https://campus.cs.le.ac.uk/people/sk619/co3090/shared.jar");
        	System.setProperty("java.rmi.server.useCodebaseOnly","false");
        	System.setProperty("java.security.policy","file:/s_home/sk619/Documents/policy.permission");
        	
        	
        	
        	
        	
        	
        	if (System.getSecurityManager() == null) {
        	    System.setSecurityManager(new SecurityManager());
        	}
        	String name = "rmi://localhost/Hello";
            Server obj = new Server();
            Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);
            Registry registry = LocateRegistry.getRegistry("localhost");
            // Bind the remote object's stub in the registry
            registry.rebind(name, stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

