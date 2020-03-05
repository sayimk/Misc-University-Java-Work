package common;

import java.io.Serializable;

public class CarFuel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8925233308778444365L;
	public String queryString ="";
	public Double queryDouble = -1.0;
	public int queryInt = -1;
	private String result="";
	
	//saved Results
	
	public CarFuel(){
	}

	public CarFuel(String query){

		boolean isDigit = true;
		
		for(int i =0; i<query.length(); i++) {
			if (!Character.isDigit(query.charAt(i)))
				isDigit = false;
		}
		
		if(isDigit) {
			queryInt = Integer.parseInt(query);
		}
		
		queryString = query;
	}
	
	public CarFuel(Double query){

		queryDouble = query;
	}
	
	public CarFuel(String query1, Double query2){

		queryString = query1;
		queryDouble = query2;
	}
	
	public void parseAndSetResult(String data) { 
		//parse and split the data
		
		//for now just return the result after testing do this----------
		result = data;
	}
	
	public String getStringQuery() {
		return queryString;
	}
	
	public Double getDoubleQuery() {
		return queryDouble;
	}
	
	public String toString() {
		
		return result;
	}
	
}
