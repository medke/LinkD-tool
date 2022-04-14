import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import edu.umbc.web.Similarity;
import shared.ParseFile;



public class InstanceMatching2 {
	private static getProperties gets;
	private static getProperties gett;
	private static List<List<String>>  instancesTarget;
	private static List<List<String>>  instancesSource;
	private static long startTime, duration;
	
	public static Similarity sim= new Similarity();
	static int matches=0,nonmatches=0,matches2=0,pmatches=0,pause=0,last;
	
	public static void Matching(List<String> sourceProperties, List<String> targetProperties, String source, String target) throws UnsupportedEncodingException
	{
		int size1 = sourceProperties.size();
		int size2 = targetProperties.size();
		double sc=0,score=0 , average=0; 
		int number=0;
		for(int i=0; i<size1; i++)
		{
			
			for(int j=0; j<size2; j++)
			{
				if ( sim.getSim(sourceProperties.get(i), targetProperties.get(j))>0.75 )
				{
					pmatches++;
					
				    sc= compare(instancesSource.get(i), instancesTarget.get(j));

				    if(sc>0.1)
				    {
				    	score+=sc;
				    	number++;
				    }
				}

			}
		
		}
		average= score/number;
		
		if(average>0.81)
			matches++;

		
	}
	
	public static void FirstStep(int start, List<String> listOfElement) throws UnsupportedEncodingException
	{
		
		
		
		for(int i=start;i<listOfElement.size();i++)
		{
			last=i;
			gett=    new getProperties(listOfElement.get(i));
			List<String> propertiesTarget = gett.getAllProperties();
			instancesTarget = gett.getAllInstances();
			
			
			   if(listOfElements.get(i).size()>1)
			   {
				for(int j=0;j<listOfElements.get(i).size();j++)
				{
					gets= new getProperties(listOfElements.get(i).get(j).replaceAll("\\s",""));
					List<String> propertiesSource= gets.getAllProperties();
					instancesSource=gets.getAllInstances();
					System.out.println("the mapping between "+listOfElement.get(i)+" and      "+ listOfElements.get(i).get(j) );
				    Matching(propertiesSource,propertiesTarget, listOfElement.get(i), listOfElements.get(i).get(j));
				  
				}
			   }
			   else if(listOfElements.get(i).size()==1 ) 
			   {
				   matches++;
			   }
			   
			   if(matches2==matches && i!=0)
				   {
				    	nonmatches++;
				    	
				   }
				   matches2=matches;
				   
				   duration = (System.nanoTime() - startTime)/1000000000 ;
				   System.out.println("\n \n ------------------");
			   System.out.println("instace: " + i +"\n matches: "+matches+"\n non-matches: "+ nonmatches+ "\n properties aligned: " + pmatches+ "\n time spent:" + duration);
				System.out.println(" ------------------");
				
				
			
			
		}	
	}

	public static void main(String[] args) throws IOException, InterruptedException{

	//List<List<String>> listOfElements= new ParseFile("results2nd.txt",true).getElements2();
	List<String> listOfElement= new LinkedMDB().getMovies();
	startTime= System.nanoTime();
	last= 2339;
	int tries=0;
	while(true)
	{
	try{
		FirstStep(last, listOfElement);
		if(last>=85618) break;
	}catch(Exception e){
		//System.out.println(e.getStackTrace()+ "||"+e.getMessage()+" \n PAUSE \n");
		pause++;
		tries++;
		if(tries>3)
			{last++;
			tries=0;}
		Thread.sleep(60000);
	}
	}

		
	
	
	System.out.println("There are "+ matches +" matches \n Pauses: "+pause);
	

	} 	

	public static String getLastBitFromUrl(final String url){
	    // return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
	    return url.replaceFirst(".*/([^/?]+).*", "$1");
	}
	
	public static String prepareLabel(final String url){
	    // return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
	    return getLastBitFromUrl(url).replace("#", "_").replaceAll("(.)([A-Z])", "$1_$2");
	}
	public static double compare(List<String> l1, List<String> l2)
	{
		double dist=0, max=0;
		

		for(int i=0; i<l1.size();i++)
		{
			for(int j=0; j<l2.size();j++)
			{
				dist= syntacticSimilarity(prepareLabel(l1.get(i).toString()),prepareLabel(l2.get(j).toString()));
				if(dist>max)
				{
					if(dist>0.95)
							max=1;
					else
						max=dist;
				}	
			}
		}


		return max;
	}
	
	  public static double syntacticSimilarity(String s1, String s2) {
		  
		    if (s1.equals(s2))
		      return 1.0;

		    if (s1.length() > s2.length()) {
		      String tmp = s2;
		      s2 = s1;
		      s1 = tmp;
		    }

		    int maxdist = s2.length() / 2;
		    int c = 0; // count of common characters
		    int t = 0; // count of transpositions
		    int prevpos = -1;
		    for (int ix = 0; ix < s1.length(); ix++) {
		      char ch = s1.charAt(ix);

		      // now try to find it in s2
		      for (int ix2 = Math.max(0, ix - maxdist);
		           ix2 < Math.min(s2.length(), ix + maxdist);
		           ix2++) {
		        if (ch == s2.charAt(ix2)) {
		          c++; // we found a common character
		          if (prevpos != -1 && ix2 < prevpos)
		            t++; // moved back before earlier 
		          prevpos = ix2;
		          break;
		        }
		      }
		    }

		 
		    if (c == 0)
		      return 0.0;

		    // first compute the score
		    double score = ((c / (double) s1.length()) +
		                    (c / (double) s2.length()) +
		                    ((c - t) / (double) c)) / 3.0;

		    // (2) common prefix modification
		    int p = 0; // length of prefix
		    int last = Math.min(4, s1.length());
		    for (; p < last && s1.charAt(p) == s2.charAt(p); p++)
		      ;

		    score = score + ((p * (1 - score)) / 10);

		    return score;
		  }
}







/*listOfElement = listOfElements.get(i);
System.out.println("\n =>"+ Arrays.toString(listOfElements.get(i).toArray())+"");
System.out.println("==>"+ listOfElement.get(i));
if(listOfElements.get(i).size()==1 && listOfElements.get(i).get(0).replaceAll("\\s","") !="no")
	perfect++;*/
	//new getProperties(listOfElements.get(i).get(0).replaceAll("\\s",""));