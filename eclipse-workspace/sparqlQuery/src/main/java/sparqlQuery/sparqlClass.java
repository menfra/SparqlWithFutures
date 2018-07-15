package sparqlQuery;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.resultset.ResultsFormat;
import org.apache.jena.util.FileManager;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.impl.StatementImpl;
import org.apache.jena.vocabulary.RDFS;

import sparqlQuery.sparqlClass;

import java.io.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;


public class sparqlClass {

	private String sparqlQuery;
	private String path;
	public sparqlClass() {
		this.path = System.getProperty("user.dir") + "\\results\\results.ttl";
		this.sparqlQuery =  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \r\n" + 
				"PREFIX dbo: <http://dbpedia.org/ontology/> \r\n" + 
				"construct { ?city rdfs:label ?labelEn. } \r\n" + 
				"where { ?city a dbo:City. \r\n" + 
				"        ?city rdfs:label ?labelEn. \r\n" + 
				"        filter(lang(?labelEn) = 'en') .}";
				}

	public static OutputStream outStream(String path) 
			  throws IOException {
		        File file = new File(path);
		        OutputStream outputStream = new FileOutputStream(file);
			   
		        return outputStream;
			}

	 
	    public static void main(String[] args) throws IOException {
	    	//ExecutorService executor = Executors.newCachedThreadPool(); //meant for Future
	    	
	    	ParameterizedSparqlString qs = new ParameterizedSparqlString((new sparqlClass()).sparqlQuery); //SparQL
	    	final QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs.asQuery()); //SparQL
 	        final OutputStream  outStream = outStream(new sparqlClass().path); //Created an outputstream to handle the file writing. a paths is parsed to it
 	       
	    	
 	        
	    	        
	    	        System.out.println("Start Threading...");
	    	        
	    	        ArrayList<Resource> resourceList = new ArrayList<Resource>();
	    	        ArrayList<String> literalList = new ArrayList<String>();
	    	        
	    	        final ResultSet results = exec.execSelect();//SparQL
	    	        final Model model = exec.execConstruct();
	    	      
	    	        while (results.hasNext()) {
		    	    	   
		    	    	   resourceList.add(results.next().getResource("resc")); //add the resource to the List
		    	    	   literalList.add(results.next().getLiteral("labelDe").toString()); //add the literals also to the list 
						
						}
		    	       
		    	       for (int x=0; x<resourceList.size(); x++) {
		    	    	  model.createResource(resourceList.get(x).addProperty(RDFS.label, literalList.get(x)));
		    	       }
	    	       model.write(outStream, "TURTLE");
	    	        System.out.println("End Threading...");
	    	      //ResultSetFormatter.output(outStream, results, ResultsFormat.FMT_RDF_TURTLE );
	    	
	    	
	    	
	    }
	}


