package compute;


public interface Compute extends java.rmi.Remote {

    public Object executeTask(Task t) throws java.rmi.RemoteException;

}
