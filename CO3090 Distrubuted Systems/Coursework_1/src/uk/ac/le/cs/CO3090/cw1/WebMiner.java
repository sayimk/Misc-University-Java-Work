package uk.ac.le.cs.CO3090.cw1;

import java.net.URISyntaxException;
import java.util.Map;

public class WebMiner implements Runnable {
	 
	public static int MAX_MINER_NUM=10;
	
	public MinerManager parent;
	public String minerURL;

	
	
	@Override
	public void run() {

		try {
			
	
			
			mine(minerURL);

			parent.addToSubLinks(Utils.extractHyperlinks(minerURL, Utils.getTextFromAddress(minerURL)));
			
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Miner for "+minerURL+" was interrupted");
		}

		
	}

	/**
	 * void mine(String URL) 
	 * 
	 * this method should record:
	 * 
	 *  (1) All pages it visited  marks a URL as 'visited' by 
	 *      adding it to 'visited' list
	 *  
	 *  (2) total keyword counts  
	 *  
	 *  (3) keyword frequency for every page it visited (case-insensitive) 
	 *      
	 *  (4) keyword frequency for all pages it visited (case-insensitive) 
	 *  
	 *  Note: skip URLs that have been visited by other miners 
	 * 
	 * @param URL
	 * @throws InterruptedException
	 */
	
	public boolean mine(String URL) throws InterruptedException{
		
		return parent.incrementwordCounter(Utils.calculate(parent.keywords, Utils.getPlainText(Utils.getTextFromAddress(URL))), URL);
		
		
	}
	

	public WebMiner() {
		
	}
	
	public WebMiner(MinerManager parentManager, String URL) {
		parent = parentManager;
		minerURL = URL;
	}
	
	

	
	
}
