import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;


public class LinkedMDB {
	private static Model model;
	static ResultSetRewindable res;
	QuerySolution so ;
	static Query query;
	static QueryExecution qexec;
	static ResultSet s;
	static String label;
	private static Model tdb;
	private List<String> listOfMovies;
	public LinkedMDB()
	{
		System.out.println("start reading TBD");
		readTBD();
		System.out.println("finish reading TBD");

		 String sparqlQueryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			   	    +"PREFIX movie: <http://data.linkedmdb.org/resource/movie/>"
				    +"SELECT distinct ?l WHERE" 
	        		+"{"
	        		+"?l rdf:type movie:film."
	        		+"?l movie:filmid ?id"
	        		+"}order by asc(?id)";
	        	

		 
	            runQuery(sparqlQueryString);
	            while(res.hasNext())
	            {
	            	

	            	QuerySolution so = res.nextSolution();
	            	writeFile(so.get("l").toString()+"  ;;");
	            	
	            	
	            	
	            }

	}
	public List<String> getMovies()
	{
		return listOfMovies;
	}
	public static void readTBD()
	{
		String directory = "./tdb";
		Dataset dataset = TDBFactory.createDataset(directory);
		

		// assume we want the default model, or we could get a named model here
		tdb = dataset.getDefaultModel();

		// read the input file - only needs to be done once
		String source = "E:/_PhD/Implementation3/linkedmdb-latest-dump.nt";
		FileManager.get().readModel( tdb, source, "N-TRIPLES" );
	}
	public static void runQuery(String sparqlQueryString)
	{
		
		
		query  = QueryFactory.create(sparqlQueryString);
		qexec= QueryExecutionFactory.create(query, tdb);
		s = qexec.execSelect();
		res = ResultSetFactory.copyResults(s);
	}
	public void writeFile(String newline){
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("E:\\_PhD\\implementation3\\LinkedMDB.txt", true)));
		    out.println(newline);
		    out.close();
		} catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
	}
}
