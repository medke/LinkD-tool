import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdtjena.HDTGraph;

import shared.ParseFile;
import shared.writeFile;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;

public class getCandidateProperties {
	String queryString, keyword, urlsource, film;
	static int nresults = 0;
	static String label;
	static String[] urls;
	static ArrayList urlsList;
	List<String> properties;
	ResultSet results;
	static ResultSetRewindable res, res2;
	QuerySolution so;
	static Pattern pattern = Pattern.compile("[^a-z0-9 ]",
			Pattern.CASE_INSENSITIVE);
	static Matcher matcher;
	static Query query;
	static QueryExecution qexec;
	static ResultSet s;
	static int numberOfCand;
	private static Model tdb;
	private static HDT hdt;
	private static Model model2;

	public static void main(String[] args) {
		numberOfCand = 0;
		String s3 = "";
		// System.out.println("start reading TBD");
		// readTBD();
		List<String> list = new ArrayList<String>();
		System.out.println("start reading HDT");
		try {
			readHDT();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("finish ");
		String s2 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+

				"\n"
				+ "select  ?s where {"
				+ "?s ?p ?o ."
				+ "?s rdf:type <http://dbpedia.org/ontology/Film>." +

				" } limit 100 ";
		runQuery2(s2);
		int loading = res.size();

		while (res.hasNext()) {
			QuerySolution binding = res.nextSolution();
			s3 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
					+

					"\n" + "select  distinct (?p)  where {" + "<"
					+ binding.get("s").toString() + "> ?p ?o ." + "}";
			query = QueryFactory.create(s3);
			qexec = QueryExecutionFactory.create(query, model2);
			s = qexec.execSelect();
			res2 = ResultSetFactory.copyResults(s);
			while (res2.hasNext()) {
				QuerySolution binding2 = res2.nextSolution();
				list.add(binding2.get("p").toString());
			
			}
		}
		Set<String> foo = new HashSet<String>(list);

		System.out.println("size=" + foo.size());
		System.out.println("content =>" + Arrays.toString(foo.toArray()));

		System.out.println("\n \n 2)size=" + list.size());

		/*
		 * List<String> listOfElements = new
		 * ParseFile("LinkedMDB.txt").getElement();
		 * 
		 * 
		 * for (String item : listOfElements) { getCandidates(item); }
		 */
		countP();
	}

	public static void countP() {
		String sparqlQueryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT (count(?l) as ?c) WHERE" + "{" + "?l ?p ?s.}";

		runQuery2(sparqlQueryString);
		while (res.hasNext()) {

			QuerySolution so = res.nextSolution();
			System.out.println(so.get("c"));
		}
	}

	public static void getCandidates(String url) {

		String sparqlQueryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "SELECT distinct ?l WHERE"
				+ "{"
				+ "<"
				+ url
				+ "> rdfs:label ?l."
				+ "filter (lang(?l) = '' || langMatches(lang(?l), 'en'))"
				+ "}order by asc(?id)";

		runQuery(sparqlQueryString);
		while (res.hasNext()) {

			QuerySolution so = res.nextSolution();
			getCandidates2(new ArrayList(Arrays.asList(so.get("l").toString()
					.split("\\s+"))));

		}
	}

	public static void readTBD() {
		String directory = "./tdb";
		Dataset dataset = TDBFactory.createDataset(directory);

		// assume we want the default model, or we could get a named model here
		tdb = dataset.getDefaultModel();

		// read the input file - only needs to be done once
		String source = "E:/_PhD/Implementation3/linkedmdb-latest-dump.nt";
		FileManager.get().readModel(tdb, source, "N-TRIPLES");
	}

	public static void readHDT() throws IOException {
		hdt = HDTManager.mapIndexedHDT("E:/DBpedia/dbpedia3.hdt", null);

		// Create Jena Model on top of HDT.
		HDTGraph graph = new HDTGraph(hdt);
		model2 = ModelFactory.createModelForGraph(graph);
	}

	public static void runQuery(String sparqlQueryString) {

		query = QueryFactory.create(sparqlQueryString);
		qexec = QueryExecutionFactory.create(query, tdb);
		s = qexec.execSelect();
		res = ResultSetFactory.copyResults(s);
	}

	public static void runQuery2(String sparqlQueryString) {
		query = QueryFactory.create(sparqlQueryString);
		qexec = QueryExecutionFactory.create(query, model2);
		s = qexec.execSelect();
		res = ResultSetFactory.copyResults(s);
	}

	/*
	 * try { getSourceLabel(item); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } } //close comment
	 * 
	 * 
	 * } public getCandidateProperties() {
	 * 
	 * properties= new ArrayList<String>(); }
	 */
	public static void getCandidates2(ArrayList str) {
		LogCtl.setCmdLogging();

		String value = "", sparqlQueryString2 = "", sparqlQueryString3 = "";
		String sparqlQueryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "SELECT distinct ?f WHERE"
				+ "{"
				+ "?f a <http://dbpedia.org/ontology/Film>;"
				+ " 		rdfs:label ?s.";

		int sizeOfTokens = str.size();
		if (sizeOfTokens == 1 && str.get(0).toString().length() < 4) {
			sparqlQueryString2 += "FILTER (regex(?s, '"
					+ str.get(0).toString().replace("'", "\\'") + " ','i'))";
		} else {
			for (int i = 0; i < sizeOfTokens; i++)
				sparqlQueryString2 += "FILTER (regex(?s, '"
						+ str.get(i).toString().replace("'", "\\'") + "','i'))";
		}
		sparqlQueryString += sparqlQueryString2;
		sparqlQueryString += "} LIMIT   50";
		runQuery2(sparqlQueryString);

		if (res.size() < 1) {
			if (str.size() == 1) {
				writeFile("no");
				System.out.println(Arrays.toString(str.toArray()));
			} else {
				for (int w = 0; w < str.size(); w++) {
					if (checkSpeciaChar(str.get(w).toString())) {

					} else {
						sparqlQueryString3 += " FILTER (regex(?s, '"
								+ str.get(w).toString().replace("'", "\\'")
								+ "','i'))";
					}

				}

				sparqlQueryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
						+ "SELECT distinct ?f WHERE"
						+ "{"
						+ "?f a <http://dbpedia.org/ontology/Film>;"
						+ " 		rdfs:label ?s.";
				sparqlQueryString += sparqlQueryString3;
				sparqlQueryString += "} LIMIT   50";

				// Arrays.sort(str, new comp());

				sparqlQueryString.replace(sparqlQueryString2,
						sparqlQueryString3);

				runQuery2(sparqlQueryString);
			}

		}
		while (res.hasNext()) {

			QuerySolution so = res.nextSolution();

			writeFile(so.getResource("f").toString() + " ;; ");
			numberOfCand++;
			// getProperties(so.getResource("f").toString());
			// properties.add(so.getResource("f").toString());

		}
		// System.out.println(Arrays.toString(properties.toArray()));
		writeFile(" ++ ");

	}
	/*
	 * public static void getProperties(String film) {
	 * System.out.println("-------------------"); LogCtl.setCmdLogging(); String
	 * sparqlQueryString =
	 * "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
	 * +"SELECT distinct ?p WHERE" +"{" +"<"+ film +"> ?p ?s." +"}"; Query query
	 * = QueryFactory.create(sparqlQueryString); QueryExecution qexec =
	 * QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
	 * ResultSet s = qexec.execSelect(); ResultSetRewindable res =
	 * ResultSetFactory.copyResults(s); while(res.hasNext()) { QuerySolution so
	 * = res.nextSolution(); System.out.println(so.getResource("p").toString());
	 * //properties.add(so.getResource("f").toString());
	 * 
	 * } //System.out.println(Arrays.toString(properties.toArray()));
	 * 
	 * System.out.println("-------------------"); }
	 * 
	 * 
	 * public static void getSourceLabel(String url) throws InterruptedException
	 * {
	 * 
	 * 
	 * String sparqlQueryString =
	 * "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
	 * +"SELECT distinct ?l WHERE" +"{" +""+url+" rdfs:label ?l" +"}";
	 * 
	 * Query query = QueryFactory.create(sparqlQueryString); QueryExecution
	 * qexec =
	 * QueryExecutionFactory.sparqlService("http://linkedmdb.org/sparql",query);
	 * ResultSet s = qexec.execSelect(); ResultSetRewindable res =
	 * ResultSetFactory.copyResults(s); while(res.hasNext()) {/* if(nresults >
	 * 20) { Thread.sleep(2000); nresults=0; } nresults++;
	 */
	;

	// urls = label.split("\\s+");
	// System.out.println(Arrays.toString(urls));
	// return urls;

	public static void writeFile(String newline) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter("E:\\_PhD\\implementation3\\DBpedia2.txt",
							true)));
			out.println(newline);
			out.close();
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}
	}

	public static boolean checkSpeciaChar(String word) {
		matcher = pattern.matcher(word);
		boolean b = matcher.find();
		if (b)
			return true;
		else
			return false;

	}

}

/*
 * class comp implements Comparator<String> { public int compare(String o1,
 * String o2) { if (o1.length() > o2.length()) { return 1; } else if
 * (o1.length() < o2.length()) { return -1; } else { return 0; } } }
 */