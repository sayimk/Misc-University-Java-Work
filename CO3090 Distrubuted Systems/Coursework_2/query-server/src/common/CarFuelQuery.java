package common;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import common.AbstractQuery;

public class CarFuelQuery extends AbstractQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1536784486485188798L;

	public CarFuel filter;
	
	private List<String> outputList = new ArrayList<String>();
	
	public List<CarFuel> CarFuelList;

	public CarFuelQuery() {
		super();
	}
	
	public CarFuelQuery(String query, CarFuel carfuel) {
		super(query);
		filter = carfuel;
	}
	
	public Object execute() {
		
		System.out.println("Executing Clients Query");
		
		
		for(int i =0; i<list.size(); i++) {
			
			String buffer = "";
			boolean addedToResult = false;
			boolean firstConditionMet = false;

			
			for(int j =0; j <list.get(i).length(); j++) {
				
				if(!addedToResult) {
					
					
				
					if((list.get(i).charAt(j)==',')||list.get(i).charAt(j)=='/') {
											
						if ((!filter.getStringQuery().equals(""))&&(filter.getDoubleQuery()!=-1.0)) {
							
							if(buffer.equals(filter.getStringQuery())||(buffer.contains(filter.getStringQuery()))) {
								
								if(filter.queryInt!=-1) {
									if (buffer.equals(filter.queryString)) {

										if(!firstConditionMet) {
									
											firstConditionMet = true;
									
										}else {
											addedToResult = true;
											outputList.add(list.get(i));
										}
									} 
									
								}else {
									if(!firstConditionMet) {
										
										firstConditionMet = true;
								
									}else {
										addedToResult = true;
										outputList.add(list.get(i));
									}
								}
								
									
							
							String temp = Double.toString(filter.getDoubleQuery());
							
							if(buffer.equals(temp)) {
								
								if(!firstConditionMet) {
									firstConditionMet = true;
									
								}else {
									addedToResult = true;
									outputList.add(list.get(i));
								}
							}
							
							}
							
						}else if((!filter.getStringQuery().equals(""))&&(filter.getDoubleQuery()==-1.0)) {
															
							if(((buffer==filter.getStringQuery()))||(buffer.contains(filter.getStringQuery()))) {
								if(filter.queryInt!=-1) {
									if (buffer.equals(filter.queryString)) {
										addedToResult = true;
										outputList.add(list.get(i));
										}
								}else {
									addedToResult = true;
									outputList.add(list.get(i));
								}
							}
						
						}else if ((filter.getStringQuery().equals(""))&&(filter.getDoubleQuery()!=-1.0)) {
							
							String temp = Double.toString(filter.getDoubleQuery());
							
							if(buffer.equals(temp)) {
								addedToResult = true;
								outputList.add(list.get(i));
							}
						
						}
						
						buffer = "";
					
					
					}else {
						if (list.get(i).charAt(j)!='"')
							buffer = buffer+list.get(i).charAt(j);
					}
				}
			}
		}
		
		//list is the file Data filter for the query from car filter
		
		CarFuelList = new ArrayList<CarFuel>();
		
		for(int i = 0; i <outputList.size(); i++) {
		
			CarFuel temp = new CarFuel();
			temp.parseAndSetResult(outputList.get(i));
			CarFuelList.add(temp);
		
		}
		
		System.out.println("Finished Execution");
		System.out.println("Found These Results");
		printResults(CarFuelList);
		System.out.println("Returning results to Client");
			
		dataStream = null;
		
		return this;
	}
	
	public void printResults(List<CarFuel> list) {
		
		for(int i = 0; i <list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
		
	}

}
