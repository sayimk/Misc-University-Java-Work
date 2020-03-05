package client;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import common.CarFuel;
import common.CarFuelQuery;
import common.Query;
import common.QueryExecutor;

public class QueryClient {

	
	QueryClient(){};
	
	
	public static void main(String[] args) {
		
		String rmiRegistry = "127.0.0.1";
		
		//checking client arguements 
			
			try {
				//checking the registry
				Registry registry = LocateRegistry.getRegistry(rmiRegistry);
				
				System.out.println("Available RMI Services");
				for(int i = 0; i< registry.list().length; i++) {
					System.out.println(registry.list()[i]);
				}

				double val = -1.0;
				String sVal = "";
				
				CarFuel filter = null;
				
				//checking the next client arguements
				if(args[1].contains(".")) {
					val = Double.parseDouble(args[1]);
				}else {
					sVal = args[1];
				}
				
				if(args.length==3) {
					if(args[2].contains(".")) {
						val = Double.parseDouble(args[2]);
					}else {
						sVal = args[2];
					}
				}
				
				if((val!=-1.0)&&(!sVal.equals(""))) {
					filter = new CarFuel(sVal,val);
				}else if (val!= -1.0) {
					filter = new CarFuel(val);
				} else if (!sVal.equals("")) {
					filter = new CarFuel(sVal);
				}
				
				
				//defining and fetching an instance of Query, not creating a new one
				Query query = new CarFuelQuery(args[0], filter);
				QueryExecutor executor = (QueryExecutor) registry.lookup("QueryExecutor");
				CarFuelQuery test =   (CarFuelQuery) executor.executeQuery(query);
				
				System.out.println("");
				System.out.println("Displaying Results recieved form server");
				test.printResults(test.CarFuelList);
				
				//print results
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		

	}

}
