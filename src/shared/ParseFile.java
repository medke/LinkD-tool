package shared;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class ParseFile {
	private static String theurl;
	public List<String> listOfElements = new ArrayList<String>() ;
	public List<String> listOfElement = new ArrayList<String>() ;
	public List<List<String>> listOfElements2 = new ArrayList<List<String>>(); 
	


	   public ParseFile(String filename) {
	      String oldFileName = "E:/_PhD/Implementation3/"+filename+"";

	      BufferedReader br = null;
	      try {
	         br = new BufferedReader(new FileReader(oldFileName));
	         String line;
	         
	        /*
	        for(int i = 0; i < 6215; ++i)
	        	  br.readLine();
	        	  */
	         while ((line = br.readLine()) != null) {

	            
	            listOfElement.add(line);
	         }
	      } catch (Exception e) {
	         return;
	      }
	      
	      

	   }
	   public ParseFile(String filename, boolean doubleDimension) {
		   
		   int n_res=0;
		   
		      String oldFileName = "E:/_PhD/Implementation2/"+filename+"";

		      BufferedReader br = null;
		      
		      try {
		         br = new BufferedReader(new FileReader(oldFileName));
		         String line;
		        
		        /*
		        for(int i = 0; i < 6215; ++i)
		        	  br.readLine();
		        	  */
		         while ((line = br.readLine()) != null) {
		        	 
		        	 if(line.contains("++")){
		        		 
		        		
		        		 listOfElements2.add(new ArrayList<String>(listOfElements));
		        		 listOfElements.clear();
		        		
		        	 }
		        	 else{
		        		 
		        		 listOfElements.add(line.replace(";;", ""));
		        		
		        	 }
		         }
		      } catch (Exception e) {
		         return;
		      }
		      
		      

		   }
	   public List<List<String>> getElements2()
	   {
		return listOfElements2;
		   
	   }
	   public List<String> getElements()
	   {
		return listOfElements;
		   
	   }
	   public List<String> getElement()
	   {
		return listOfElement;
		   
	   }
	   
		public static String getLastBitFromUrl(final String url) throws UnsupportedEncodingException{
		    // return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
			theurl = url.replaceFirst(".*/([^/?]+).*", "$1");
		    return java.net.URLDecoder.decode(theurl,"UTF-8");
		}
	

}
