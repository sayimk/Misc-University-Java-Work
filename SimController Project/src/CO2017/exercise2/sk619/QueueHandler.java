//Written by Sayim Khan
package CO2017.exercise2.sk619;

import java.util.concurrent.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;

public class QueueHandler implements Runnable{
	
	//class variables
	ThreadPoolExecutor e;
	MemManager m;
	String processName;
	
	//run method for 
	@Override
	public void run() {
		
		//variables for reading from the file
	      int delay = 0, size = 0, rt = 0;
	      char pid = 0;
		  String fname = processName;
		  Path fpath = Paths.get(fname);
		    
	      //reading from the file which contains the process data
	      //default format is 10:A:50:200
		    try (Scanner file = new Scanner(fpath)) {

		      //reading data from the file 
		      while (file.hasNextLine()) {
		        Scanner line = new Scanner(file.nextLine());
		        line.useDelimiter(":");
		        delay = line.nextInt();
		        pid   = line.next().charAt(0);
		        size  = line.nextInt();
		        rt    = line.nextInt();
		        
			    //sleeping for the length of the delay
			    try {
					Thread.sleep(delay*100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			    
			    //creating the new process and then adding it to the threadpool for execution
			    Process newProcess = new Process(m,pid,size,rt);
			    e.execute(newProcess);
		        line.close();
		      }  
		      file.close();

		    } catch (NoSuchFileException e) {
		      System.err.println("File not found: "+fname);
		      System.exit(1);
		    } catch (IOException e) {
		      System.err.println(e);
		      System.exit(1);
		    }
		  }

	//QueueHandlers Constructor and assigns values to the variables
	QueueHandler(ThreadPoolExecutor e, MemManager m, String f){
		
		this.e=e;
		this.m=m;
		processName=f;
	}

}
