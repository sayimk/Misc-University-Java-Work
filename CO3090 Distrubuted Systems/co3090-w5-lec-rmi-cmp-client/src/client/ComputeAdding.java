package client;
import java.rmi.*;
import compute.*;

public class ComputeAdding {
    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        try {
            String name = "rmi://" + args[0] + "/Compute";
            Compute comp = (Compute) Naming.lookup(name);
            Adding task =
            	
		new Adding(
			      Double.parseDouble(args[1]),
			      Double.parseDouble(args[2])
			      );
            
            double sum =
		((Double)(comp.executeTask(task))).doubleValue();
            
            
            System.out.println("the sum is: " + sum);
        } catch (Exception e) {
            System.err.println("ComputePitagoras exception: " + 
                               e.getMessage());
            e.printStackTrace();
        }
    }
}
