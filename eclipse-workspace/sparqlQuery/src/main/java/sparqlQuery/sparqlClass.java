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
	
	private String sparqlQuery;
	private String path;
	public sparqlClass() {
		this.path = "result.txt";
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

	public static void writeFile(String writeMessage, String path) 
			  throws IOException {
		        File file = new File(path);
			    BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			    writer.append(writeMessage);
			    writer.newLine();
			     
			    writer.close();
			}
	
	//Fibonacci function 
	 public static long fibonacci(int n) {
	        if (n <= 1) return n;
	        else return fibonacci(n-1) + fibonacci(n-2);
	    }

	 
	    public static void main(String[] args) {
	    	ExecutorService executor = Executors.newCachedThreadPool(); //meant for Future
	    	ParameterizedSparqlString qs = new ParameterizedSparqlString((new sparqlClass()).sparqlQuery); //SparQL
	    	QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs.asQuery()); //SparQL
 	        final ResultSet results = exec.execSelect(); //SparQL
 	       
	    		
	    	Future<String> future = executor.submit(new Callable<String>() {
	    		
	    		public String call() throws Exception{
	    			
	    	        
	    	        System.out.println("Starting Thread...");
	    	       
	    	        
	    	      //Toggling in between the Fibonacci and the SparQl queryy
	    	        int n = Integer.parseInt(args[0]);
	    	        for (int i = 1; i <= n; i++)
	    	        {
	    	        	writeFile(Long.toString( fibonacci(i)),(new sparqlClass()).path);
	    	        }
	    	        
	    	        while (results.hasNext()) {
					writeFile(results.next().toString(),(new sparqlClass()).path);
					
					}
	    	        System.out.println("Ended Thread...");
	    	        return results.toString();
	    		}
	    		
	    	});
	    	
	    	executor.shutdown();
	        
	        try {
	        	System.out.println(future.get());
				//ResultSetFormatter.out(results);
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}


