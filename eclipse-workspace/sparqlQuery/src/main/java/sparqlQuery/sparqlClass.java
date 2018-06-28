package sparqlQuery;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

import sparqlQuery.sparqlClass;

import java.io.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class sparqlClass {

	private String sparqlQuery;
	private String path;
	public sparqlClass() {
		this.path = System.getProperty("user.dir") + "\\results\\results.tsv";
		this.sparqlQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX dbo: <http://dbpedia.org/ontology/>\r\n" + 
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\r\n" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
				"SELECT DISTINCT * WHERE {\r\n" + 
				"  {?resc rdf:type dbo:Place .\r\n" + 
				"  ?resc rdfs:label ?labelDe .\r\n" + 
				"  ?resc foaf:name ?labelEn . \r\n" + 
				"    Filter (lang(?labelDe) = 'de') .}\r\n" + 
				" UNION\r\n" + 
				"  {?resc rdf:type dbo:Place .\r\n" + 
				"   ?resc rdfs:label ?labelDe.\r\n" + 
				"  ?resc foaf:name ?labelEn .\r\n" + 
				"    Filter (lang(?labelEn) = 'en') .}\r\n" + 
				" \r\n" + 
				"} \r\n" + 
				"LIMIT 500";
	}

	public static OutputStream outStream(String path) 
			  throws IOException {
		        File file = new File(path);
		        OutputStream outputStream = new FileOutputStream(file);
			   
		        return outputStream;
			}

	 
	    public static void main(String[] args) throws IOException {
	    	ExecutorService executor = Executors.newCachedThreadPool(); //meant for Future
	    	ParameterizedSparqlString qs = new ParameterizedSparqlString((new sparqlClass()).sparqlQuery); //SparQL
	    	final QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs.asQuery()); //SparQL
 	        final OutputStream  outStream = outStream(new sparqlClass().path); //Created an outputstream to handle the file writing
 	       
	    	
 	        //the implementation of Future starts from here
	    	Future<String> future = executor.submit(new Callable<String>() {
	    		
	    		public String call() throws Exception{
	    			
	    	        
	    	        System.out.println("Start Threading...");
	    	        
	    	        final ResultSet results = exec.execSelect(); //SparQL
	    	        
	    	        while (results.hasNext()) {
					ResultSetFormatter.outputAsTSV(outStream, results);
					
					}
	    	        System.out.println("End Threading...");
	    	        return results.toString();
	    		}
	    		
	    	});
	    	
	    	executor.shutdown();
	        
	       
	    }
	}


