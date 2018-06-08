package sparqlQuery;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import java.io.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class sparqlClass {
	
	private static String sparqlQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
			"PREFIX dbo: <http://dbpedia.org/ontology/>\r\n" + 
			"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\r\n" + 
			"SELECT DISTINCT ?labelEn ?labelDe WHERE {\r\n" + 
			"  {?sub rdf:type dbo:Place .\r\n" + 
			"  ?sub rdfs:label ?labelDe .\r\n" + 
			"  ?sub foaf:name ?labelEn . \r\n" + 
			"    Filter (lang(?labelDe) = 'de') .}\r\n" + 
			"  UNION\r\n" + 
			"  {?sub rdf:type dbo:Place .\r\n" + 
			"   ?sub rdfs:label ?labelDe .\r\n" + 
			"  ?sub foaf:name ?labelEn .\r\n" + 
			"    Filter (lang(?labelDe) = 'en') .}\r\n" + 
			"} \r\n" + 
			"LIMIT 500";

	public sparqlClass() {
	}
	
	
	public static void writeFile(String op) 
			  throws IOException {
			    BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\user\\Documents\\CodeStore\results.txt", true));
			    writer.append(op);
			    writer.newLine();
			     
			    writer.close();
			}

	    public static void main(String[] args) {
	    	ExecutorService executor = Executors.newCachedThreadPool();
	    	
	    	ParameterizedSparqlString qs = new ParameterizedSparqlString(sparqlQuery);
	        QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs.asQuery());
	        final ResultSet results = exec.execSelect();
	        
	    		
	    	Future<String> future = executor.submit(new Callable<String>() {
	    		
	    		public String call() throws Exception{
	    	        
	    	        System.out.println("Starting Thread...");
	    	        while (results.hasNext()) {
					writeFile(results.next().toString());

					//System.out.println(results.next().get("Concept"));
					}

	    	        System.out.println("Ended Thread...");
	    	        return results.toString();
	    		}
	    		
	    	});
	    	
	    	executor.shutdown();
	        
	        try {
	        	System.out.println(future.get());
				//ResultSetFormatter.out(future.get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}


