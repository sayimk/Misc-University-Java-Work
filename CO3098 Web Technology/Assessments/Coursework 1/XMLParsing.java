package uk.ac.le.cs.module;


public class XMLParsing {
	
	//Usage: java XMLParsing xmlfile 
	//e.g.   java XMLParsing IServiceREST.xml
	 public static void main(String[] args){
		 
		 try {
		//Note: you need to implement either XMLDOMParser.java OR XMLSAXParser.java 
		 XMLParserDOM parser=new XMLParserDOM(); 
		// or XMLSAXParser parser = new XMLSAXParser();	
		 if(args.length == 0) {
			 throw new IllegalArgumentException("Usage: java XMLParsing xmlfile");
		 }
		 parser.parse(args[0]);
		 }catch(Exception ex) {
			 ex.printStackTrace();
		 }
		 
	 }

}




