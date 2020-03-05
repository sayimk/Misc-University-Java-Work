package uk.ac.le.cs.CO3090.cw1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MinerManager implements Runnable{
	
	 public static int MAX_PAGES_NUM=50; 
	 public static int TIME_OUT=20;
	 public static int MAX_QUEUE_SIZE=20000;
	 public static int MAX_WORD_COUNT=100000;
	
	 
	 //this is the root website URL for mining
	 public String baseURL = "http://www.bbc.co.uk/news";
	 
	 
	 
	 //Variables for the MinerManager
	 public ArrayList<String> keywords = new ArrayList<String>();
	 
	 public List<String> visitedPages = Collections.synchronizedList(new ArrayList<String>());
		
	 public Map<String,Integer> wordCounter = Collections.synchronizedMap(new HashMap<String,Integer>());
	 
	 public List<String> subLinks = Collections.synchronizedList(new ArrayList<String>());
	 
	 public List<String> pending = Collections.synchronizedList(new ArrayList<String>());

	 
	 ExecutorService ex = Executors.newFixedThreadPool(8);

	 
	@Override
	public void run() {
	
		//Adding keywords
		 keywords.add("The");
		 keywords.add("Apple");
		 keywords.add("World");
		 keywords.add("Video");
		 keywords.add("News");
		 keywords.add("Extradition");
		 keywords.add("S9");
		 keywords.add("Entertainment");
		 keywords.add("cnn");
		 
		 
		 
		 //Intializing WordCounter
		 for (int i=0; i<keywords.size(); i++) {
			 
			 wordCounter.put(keywords.get(i), 0);
			 
		 }
		 
		 
		 //Using executionerService
		 subLinks= Utils.extractHyperlinks(baseURL, Utils.getTextFromAddress(baseURL));
		 WebMiner first = new WebMiner(this, baseURL);
		 ex.execute(first);
		 int added =1;
		 
		 while((subLinks.size()>0)&&(added<MAX_PAGES_NUM)&&(totalUpWords()<MAX_WORD_COUNT)) {
		 
			 System.out.println("Checking URL");
			 Boolean add = false;
			 String temp = null;
			 
			 
		 if (!checkIfPendingOrVisited(subLinks.get(0))) {			 
			 WebMiner sub = new WebMiner(this, subLinks.get(0));
			 ex.execute(sub);
			 added = added+1;
			 pending.add(subLinks.get(0));
			 System.out.println("Added Task: "+subLinks.get(0));
			 temp = subLinks.get(0);
			 subLinks.remove(0);
		 }else {
			 subLinks.remove(0);

		 }
		 
		 
	 }
		 
		 
		 ex.shutdown();
		 
		
		 try {
			
			 ex.awaitTermination(TIME_OUT, TimeUnit.SECONDS);

			 showTotalStatistics();
			 
			 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	
	
//	 * The output should look like:
//	 
//	 ============
//		Total number of keywords:91
//		Pages visited:43
//      Pending pages no visited yet: 67 
//	
//		University = 25
//		Britain = 473
//		Brexit = 130
//		Holidays = 102
//      Sports = 34	
//	 =============
//	    
//	
//	 *The results may vary depending on your 
//	 *search strategy.	 
//	 *               
//	 * @throws InterruptedException
//	 * @return   
//	 */
	public void showTotalStatistics()throws InterruptedException {
		
		totalUpWords();
		
		
		if ((pending.size()>0)&&((pending.size()+subLinks.size()<=MAX_WORD_COUNT)))
			subLinks.addAll(pending);

		System.out.println();
		System.out.println("Results:");
		System.out.println("Total Number of keywords: "+totalUpWords());
		System.out.println("Pages Visited: "+visitedPages.size());
		System.out.println("Pending pages not visited yet: "+subLinks.size());
		System.out.println();
		
		for(int i =0; i<keywords.size();i++) {
			System.out.println(keywords.get(i)+" = "+wordCounter.get(keywords.get(i)));
		}
	}
	
	//Synchonized methods for synchronised data types
	//Synch'ed check and add method
	
	public synchronized void addToVisited(String webURL) {
		visitedPages.add(webURL);
	}
	
	public synchronized boolean checkIfVisited(String webURL) {
		
		for (int i=0; i<visitedPages.size(); i++) {
			
			if (visitedPages.get(i).equals(webURL))
				return true;
		}
		
		return false;
	}
	
	
	public synchronized boolean checkIfPendingOrVisited(String webURL) {
		
		for (int i=0; i<pending.size(); i++) {
			
			if (pending.get(i).equals(webURL))
				return true;
		}
		
		
		if (checkIfVisited(webURL))
			return true;
		
		
		return false;
	}
	
	public synchronized void addToSubLinks(List<String> links){
		
		for (int i=0; i<links.size(); i++) {
		
			if (subLinks.size()<MAX_QUEUE_SIZE)
				subLinks.add(links.get(i));
		}
	}
	
	
	public int totalUpWords() {
		
		 int totalKeys = 0;

		
		for (int i =0; i<keywords.size(); i++) {
			totalKeys= totalKeys+wordCounter.get(keywords.get(i));
		}
		
		return totalKeys;
		
	}
	
	
	//method for incrementing word counter
	public synchronized boolean incrementwordCounter (Map<String, Integer> countMap, String URL) {
		
		for (int i=0; i<keywords.size(); i++) {

			if (totalUpWords()>MAX_WORD_COUNT) {
				ex.shutdownNow();
				subLinks.add(URL);
				return false;
			}else {
				
				for (int j=0; j<countMap.get(keywords.get(i)); j++) {
					
					if (totalUpWords()<MAX_WORD_COUNT)
						wordCounter.put(keywords.get(i), (wordCounter.get(keywords.get(i))+1));


				}
			}
			
			
		}
		
		finishedPending(URL);
		addToVisited(URL);
		
		return true;
	}
	
	
	public synchronized boolean finishedPending(String webURL) {
		
		for (int i=0; i<pending.size(); i++) {
			
			if (pending.get(i).equals(webURL)) {
				pending.remove(i);
				return true;
			}
		}
		
		return false;
	}
	
	
	public static void main(String[] args){

		  MinerManager miner = new MinerManager();
		  
		  Thread mainManagerThread = new Thread(miner);
		  

		  
		  mainManagerThread.start();
		  
		  
		  try {
			mainManagerThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
