package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.stream.Stream;

import common.Query;
import common.QueryExecutor;


public class QueryServer extends UnicastRemoteObject
							implements QueryExecutor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8156344257225006975L;

	public QueryServer() throws RemoteException {
		super();
		
	}
	
	@Override
	public Object executeQuery(Query query) throws RemoteException {
				
		//setting Data Stream
		File file = new File(query.getFilename());
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			Stream<String> stream = reader.lines();
			query.setDataStream(stream);
			reader.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return query.execute();
	}

	public static void main(String[] args) {
				
	    System.setProperty("java.security.policy","file:"+System.getProperty("user.dir")+System.getProperty("file.separator")+"security.policy");
	    
	    //Not required for Localhost RMI
		//System.setProperty("java.rmi.server.codebase", "file://D:"+System.getProperty("file.separator")+"OneDrive"+System.getProperty("file.separator")+"Java Eclipse Projects"+System.getProperty("file.separator")+"CO3090 Distrubuted Systems"+System.getProperty("file.separator")+"Coursework_2"+System.getProperty("file.separator")+"query-server"+System.getProperty("file.separator")+"bin"+System.getProperty("file.separator"));
	    //System.setProperty("java.rmi.server.useCodebaseOnly","false");
		
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		System.setProperty("java.rmi.server.hostname","localhost");
		
		
		String rmiURL = "rmi://localhost:1099/QueryExecutor";
		
		try {
			QueryServer executor = new QueryServer();
			
			Naming.rebind(rmiURL, executor);
			
			System.out.println("Name binding complete");
			System.out.println("Query Executor Service Started as QueryExecutor");
			
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


}
