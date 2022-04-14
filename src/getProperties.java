

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.jena.atlas.logging.LogCtl;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetRewindable;


public class getProperties {
	
	static ResultSetRewindable res;
	QuerySolution so ;
	static Query query;
	static QueryExecution qexec;
	static ResultSet s;
	List<String> properties= new ArrayList<String>();
	List<String> inst= new ArrayList<String>();
	List<List<String>> instances= new ArrayList<List<String>>();
	
	String p1,p2;
	
	public getProperties(String dataset) throws UnsupportedEncodingException{
		String p1, p2="";
		LogCtl.setCmdLogging();

        String sparqlQueryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
        		+"SELECT  ?p (group_concat(distinct ?o ; separator = '--') AS ?ob) WHERE" 
        		+"{"
        		+"<"+URLDecoder.decode(dataset.replaceAll("\\s",""),"UTF-8").replaceAll(Pattern.quote("+"), "\\+")+"> ?p ?o"
        		+ "} Group by ?p";
        
        runQuery(sparqlQueryString);
        
        while(res.hasNext())
        {
        
        	QuerySolution so = res.nextSolution();
        	//properties.add(so.getResource("p").toString() +" ;; ");
        	//System.out.println((so.getResource("p").toString()));
        	properties.add(prepareLabel(so.getResource("p").toString()));
        	inst= Arrays.asList(so.get("ob").toString().split("--"));
        	instances.add(new ArrayList<String>(inst));
        //	instances= Arrays.asList(so.get("ob").toString().split(","));
        	
        }
		
	}
	
	public List<String> getAllProperties()
	{
		return properties;
	}
	public List<List<String>> getAllInstances()
	{
		return instances;
	}
	public static void runQuery(String sparqlQueryString)
	{
	query = QueryFactory.create(sparqlQueryString);
    qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
    qexec.setTimeout(2000000000);
    s = qexec.execSelect();
    res = ResultSetFactory.copyResults(s);
	}
	public static String getLastBitFromUrl(final String url){
	    // return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
	    return url.replaceFirst(".*/([^/?]+).*", "$1");
	}
	
	public static String prepareLabel(final String url){
	    // return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
	    return getLastBitFromUrl(url).replace("#", "_").replaceAll("(.)([A-Z])", "$1_$2");
	}
}
