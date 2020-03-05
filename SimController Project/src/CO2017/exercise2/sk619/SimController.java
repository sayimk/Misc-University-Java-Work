//Written by Sayim Khan
package CO2017.exercise2.sk619;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

//method must require 3 parameters during runtime
public class SimController implements Runnable {

	//Static variables accessible to both main and run methods
	 static ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	 static MemManager memManager;
	
	 //this is the watcher method and is used to monitor the state of the queue and generate an output based on if it has changed or not
	 //run method for runnable
	@Override
	public void run() {
		System.out.println(memManager.toString()); //outputs an initial state
		while(!threadPool.isTerminated()){
			if(memManager.isChanged())
				System.out.println(memManager.toString());
			try {
			Thread.sleep(1000); //checks the state of the simulated memory once per second
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
		}
		System.out.println(memManager.toString()); //outputs a final state
	}
	
	//main class
	public static void main(String[] args){
		
		//declaring and initializing variables
		String managementStyle = args[0];
		int memorySize = Integer.valueOf(args[1]);
		String fileName = args[2];
		QueueHandler QH = null;
	
		//switch statement for the input arguements
		//cases for w(worst),b(best),f(first), and default
		switch (managementStyle){
		
		case "f":		{System.out.println("Policy: FIRST fit");
						memManager = new FirstFitMemManager(memorySize);				
						QH = new QueueHandler(threadPool,memManager,fileName);
	
		}break;
	
		case "b":		{System.out.println("Policy: BEST fit");
						memManager = new BestFitMemManager(memorySize);
						QH = new QueueHandler(threadPool,memManager,fileName);
					
		}break;
	
		case "w":		{System.out.println("Policy: WORST fit");
						memManager = new WorstFitMemManager(memorySize);
						QH = new QueueHandler(threadPool,memManager,fileName);
		}break;
	
		default:		{
						System.out.println("Invalid management Style");
						System.exit(0);
						}		
	}
		//how to use start method
	
		Thread watcher = new Thread(new SimController());
		watcher.start();//starts the watcher thread
	
		Thread queue =new Thread(QH);
		queue.start();//starts the queue thread
	
		try {
			queue.join(); //waits for the queue thread to join
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
		threadPool.shutdown();
		try {
			watcher.join(); //waits for the watcher thread to finish and join
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
		System.out.println("All threads have terminated"); //prints the final message
		}
}
