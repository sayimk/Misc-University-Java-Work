package uk.ac.le.cs.module;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//Note: you need to implement either XMLDOMParser.java OR XMLSAXParser.java

public class XMLParserDOM {
	
	public void parse(String xml) {
	  try{
	  		DOMParser parser = new DOMParser();
	  		parser.parse(xml);
	  		Document doc = parser.getDocument();
	  		traverse_tree(doc);
	       }
	       catch(Exception e){
	          e.printStackTrace(System.err);
	       }
    }
	  
	public static void traverse_tree(Node node){
		//Complete task 3 
		if (node==null) {
			return;
		}
		
		int nodeType = node.getNodeType();
		switch (nodeType) {
		
			case Node.DOCUMENT_NODE : {
				traverse_tree(((Document)node).getDocumentElement());
				break;
			}
		
			case Node.ELEMENT_NODE : {
				ElementHandler(node);
				break;
			}
		}
	}
	
	//Handling all Elements
	public static void ElementHandler(Node node) {
		if (node.getNodeName()=="abstract_method"){
				AbstractMethodHander(node);
		}

		NodeList list = node.getChildNodes();		
		int listLength = list.getLength();
		
		for (int i=0; i<listLength; i++) {
			traverse_tree(list.item(i));
		}
	}
	
	//used to handle Abstract Method Children
	public static void AbstractMethodHander(Node node) {
		Element abstractNode = (Element) node;
		String abstractMethodPrint ="";
		NodeList abstractChildList = node.getChildNodes();
		Node parametersNode = null;
		Node exceptionsNode = null;
		int abstractChildLength = abstractChildList.getLength();
		
		//looping Through Method Children for visibility modifier, a return type, Parameters list, Exception List
		for (int i=0; i<abstractChildLength; i++) {
			
			switch (abstractChildList.item(i).getNodeName()) {
				
					case "visibility" :{
						abstractMethodPrint= abstractChildList.item(i).getTextContent() + abstractMethodPrint;
						break;
					}
					
					case "return" :{
						abstractMethodPrint= abstractMethodPrint + " " +abstractChildList.item(i).getTextContent();
						break;
					}
					
					case "parameters" :{
						parametersNode = abstractChildList.item(i);
						break;
					}
					
					case "throw" :{
						exceptionsNode = abstractChildList.item(i);
						break;
					}
				}
		}
		
		//adding Method Name to Method String
		abstractMethodPrint = abstractMethodPrint + " " + abstractNode.getAttributes().item(0).getNodeValue();
		
		//Searching Through Parameters for Arguments after Null Check
		if (parametersNode != null) {
			NodeList argumentsList = parametersNode.getChildNodes();
			String argumentPrintString = "(";
			
			for (int i=0; i < argumentsList.getLength(); i++) {
				if (argumentsList.item(i).getNodeName()=="argument") {
					argumentPrintString = argumentPrintString + argumentsList.item(i).getAttributes().item(0).getNodeValue() + " ";
					argumentPrintString = argumentPrintString + argumentsList.item(i).getTextContent() + ", ";
				}
			}
			argumentPrintString = argumentPrintString.substring(0, (argumentPrintString.length()-2));
			argumentPrintString = argumentPrintString + ")";
			abstractMethodPrint = abstractMethodPrint + argumentPrintString;
		}
		
		//now searching for Abstract Method exceptions
		if (exceptionsNode != null) {
			NodeList exceptionsList = exceptionsNode.getChildNodes();
			String exceptionPrintString = "throws ";
			
			for (int i=0; i < exceptionsList.getLength(); i++) {
				if (exceptionsList.item(i).getNodeName()=="exception") {
					exceptionPrintString = exceptionPrintString + exceptionsList.item(i).getTextContent() + ", ";
				}
			}
			exceptionPrintString = exceptionPrintString.substring(0, (exceptionPrintString.length()-2));
			abstractMethodPrint = abstractMethodPrint + '\n' + '\t' + exceptionPrintString;
		}
		
		abstractMethodPrint = abstractMethodPrint + ";";
		System.out.println(abstractMethodPrint);
	}

		
}


