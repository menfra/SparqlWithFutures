package menfra.osd.sparql;

import java.io.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import menfra.osd.sparql.sparqlClass;

public class sparqlClass {

	private String sparqlQueryDe;
	private String sparqlQueryEn;
	private String outputEn_ttl;
	private String outputDe_ttl;
	
	public sparqlClass() {
		this.outputEn_ttl= System.getProperty("user.dir") + "\\results\\resultsEn.ttl";
		this.outputDe_ttl = System.getProperty("user.dir") + "\\results\\resultsDe.ttl";
		
		this.sparqlQueryDe = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \r\n" + 
				"PREFIX dbo: <http://dbpedia.org/ontology/> \r\n" + 
				"construct { ?city rdfs:label ?labelDe. } \r\n" + 
				"where { ?city a dbo:City. \r\n" + 
				"        ?city rdfs:label ?labelDe. \r\n" + 
				"        filter(lang(?labelDe) = 'de') } \r\n" + 
				"LIMIT 50";
		
		this.sparqlQueryEn = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \r\n" + 
				"PREFIX dbo: <http://dbpedia.org/ontology/> \r\n" + 
				"construct { ?city rdfs:label ?labelEn. } \r\n" + 
				"where { ?city a dbo:City. \r\n" + 
				"        ?city rdfs:label ?labelEn. \r\n" + 
				"        filter(lang(?labelEn) = 'en') }";
	}

	public static OutputStream outStream(String path) 
			  throws IOException {
		        File file = new File(path);
		        OutputStream outputStream = new FileOutputStream(file);
			   
		        return outputStream;
			}

	 
	    public static void main(String[] args) throws IOException {
	    	ExecutorService executor = Executors.newCachedThreadPool(); //meant for Future
	    	
	    	ParameterizedSparqlString qs1 = new ParameterizedSparqlString((new sparqlClass()).sparqlQueryDe); //SparQL German Resource
	    	ParameterizedSparqlString qs2 = new ParameterizedSparqlString((new sparqlClass()).sparqlQueryEn); //SparQL English Resource
	    	
	    	final QueryExecution exec1 = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs1.asQuery()); //SparQL German Exec assignment
	    	final QueryExecution exec2 = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs2.asQuery()); //SparQL English Exec assignment
	    	
 	        final OutputStream  outStreamttlDe = outStream(new sparqlClass().outputDe_ttl);
 	        final OutputStream  outStreamttlEn = outStream(new sparqlClass().outputEn_ttl);
 	       
	    		
	    	Future<String> future = executor.submit(new Callable<String>() {
	    		
	    		public String call() throws Exception{
	    			
	    	        
	    	        System.out.println("Start Threading...");
	    	        
	    	        final Model model_de = exec1.execConstruct(); //model_de handles the German result  
					RDFDataMgr.write(outStreamttlDe, model_de, Lang.TTL);
					
					final Model model_en = exec2.execConstruct(); //model_de handles the German result
					RDFDataMgr.write(outStreamttlEn, model_en, Lang.TTL);
					
	    	        System.out.println("End Threading...");
	    	        return "Execution Completed... ";

	    		}
	    		
	    	});
	    	
	    	executor.shutdown();
	        
	        try {
	        	System.out.println(future.get());
				
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

}
