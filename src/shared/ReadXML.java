package shared;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXML {
	public List<String> listOfElements = new ArrayList<String>() ;
	public ReadXML(String classsource, String n, boolean b) throws SAXException, IOException,
    ParserConfigurationException, TransformerException {
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    	Document document;
    	
    	if(b)
    	 document = docBuilder.parse(new File("E:\\_PhD\\implementation2\\"+classsource+"-"+n+".xml"));
    	else
    	 document = docBuilder.parse(new File("E:\\_PhD\\implementation2\\"+classsource+"-"+n+".xml"));
    	
    	
    	NodeList nodeList = document.getElementsByTagName("*");
    	for (int i = 0; i < nodeList.getLength(); i++) {
    		Node node = nodeList.item(i);
    		if (node.getNodeType() == Node.ELEMENT_NODE) {
    			listOfElements.add(node.getNodeName());
    		}
    	}
    	
   
	}
	public List<String> getElements()
	{
		return this.listOfElements;
	}
}

