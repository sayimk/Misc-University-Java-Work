package uk.ac.le.cs.module;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

//Note: you need to implement either XMLDOMParser.java OR XMLSAXParser.java

public class XMLParserSAX extends DefaultHandler{
	
	public void parse(String xml) {
		 
		 try{
			 
			XMLParserSAX SAXHandler = new XMLParserSAX();
	 		SAXParser parser = new SAXParser();
	 		parser.setContentHandler(SAXHandler);
	 		parser.setErrorHandler(SAXHandler);
	 		parser.parse(xml);
	 		//Complete task 3....
		    }
			catch(Exception e){e.printStackTrace(System.err);
			}
		 
	 }
	 
	 public void startElement(String uri, String localName, String rawName, Attributes attributes) { 		 
		}
		 
		public void characters(char characters[], int start, int length){
	 
		}
		 
		public void endElement(String uri, String localName, String rawName){	 
				 
		}
	 

}
