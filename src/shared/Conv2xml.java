package shared;


import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Conv2xml {
	private Element[] elements;
	private String element;

	public Conv2xml (Set<String> properties, String classsource, int resultsNumber) {
		

	  try {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("Global");
		doc.appendChild(rootElement);
		elements = new Element[properties.size()];
		int i=0; // index of the element number
        for(String str : properties){
        	element=removeInvalidXMLCharacters(str).replaceAll("#", "_").replaceAll("-", "_").replaceAll(" ", "_").replaceAll("\\d","_");
        	element= element.replaceAll("___", "_");
        	element= element.replaceAll("__", "_");
        	element= element.replaceAll(",", "_");
        	element = element.startsWith("_") ? element.substring(1) : element;
        	try
        	{
    		elements[i]=doc.createElement(element);
        	}catch(DOMException e){
        		System.out.println(element);
        		System.out.println(e.getMessage());
        		
        	}
    	
    		i++;
    			
        }
        System.out.println(Arrays.toString(elements));
        i=0;
        while(i< properties.size())
        {
        	rootElement.appendChild(elements[i]);
        	i++;
        }

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("E:\\_PhD\\implementation\\"+classsource+"-"+resultsNumber+".xml"));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);

		System.out.println("File saved!");

	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
	}
	public Conv2xml (Set<String> properties, String classsource, int resultsNumber, boolean b) {
		

		  try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Global");
			doc.appendChild(rootElement);
			elements = new Element[properties.size()];
			int i=0; // index of the element number
	        for(String str : properties){
	        	element=removeInvalidXMLCharacters(str).replaceAll("#", "_").replaceAll("-", "_").replaceAll(" ", "_").replaceAll("\\d","_");
	        	element= element.replaceAll("___", "_");
	        	element= element.replaceAll("__", "_");
	        	element = element.startsWith("_") ? element.substring(1) : element;
	    		elements[i]=doc.createElement(element);
	    	
	    		i++;
	    			
	        }
	        System.out.println(Arrays.toString(elements));
	        i=0;
	        while(i< properties.size())
	        {
	        	rootElement.appendChild(elements[i]);
	        	i++;
	        }

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("E:\\_PhD\\implementation\\"+classsource+"-"+resultsNumber+"-s.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
		}
	  public static String removeInvalidXMLCharacters(String s)
	    {
	        StringBuilder out = new StringBuilder();

	        int codePoint;
	        int i = 0;

	        while (i < s.length())
	        {
	            // This is the unicode code of the character.
	            codePoint = s.codePointAt(i);
	            if ((codePoint == 0x9) ||
	                    (codePoint == 0xA) ||
	                    (codePoint == 0xD) ||
	                    ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
	                    ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
	                    ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)))
	            {
	                out.append(Character.toChars(codePoint));
	            }
	            i += Character.charCount(codePoint);
	        }
	        return out.toString();
	    }
	  
}