package uk.ac.le.cs.CO3098.cw3.domain;

public class Main {
	
	public static void main(String[] args) {
		
		
		Folder documents=new Folder("Documents");
		Folder books=new Folder("Books");	
		Folder romance=new Folder("Romance");	
		Folder holidays=new Folder("Holidays");		
		Folder fiction=new Folder("Fiction");				
		Folder nonfiction=new Folder("Fiction");		
		Folder asia=new Folder("Asia");	
		Folder europe=new Folder("Europe");	
		
		ItemLink lp_link=new ItemLink("lonely_Planet","Lonely Planet","http://www.lonelyplanet.com");
		ItemLocation london_location=new ItemLocation("London",51.5074,0.1278);
		ItemTextFile travelplan_file=new ItemTextFile("MyTravelPlan.txt","I will visit Victoria & Albert Museum");
		
		lp_link.setParentFolder(nonfiction);
		london_location.setParentFolder(europe);
		travelplan_file.setParentFolder(europe);
		
		books.setParentFolder(documents);
		romance.setParentFolder(books);
		holidays.setParentFolder(documents);
		fiction.setParentFolder(books);
		nonfiction.setParentFolder(books);
		asia.setParentFolder(holidays);
		europe.setParentFolder(holidays);
		holidays.setParentFolder(documents);	
		
		System.out.println(fiction);
		
		System.out.println(lp_link);
		System.out.println(london_location);
		System.out.println(travelplan_file);	

		
	}

}
