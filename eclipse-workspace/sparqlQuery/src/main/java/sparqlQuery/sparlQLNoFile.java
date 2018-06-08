package sparqlQuery;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import java.io.*;

public class sparlQLNoFile {
	
	private static String sparqlQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
			"PREFIX dbo: <http://dbpedia.org/ontology/>\r\n" + 
			"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\r\n" + 
			"SELECT DISTINCT ?resc WHERE {\r\n" + 
			"  {?resc rdf:type dbo:Place .\r\n" + 
			"  ?resc rdfs:label ?labelDe .\r\n" + 
			"  ?resc foaf:name ?labelEn . \r\n" + 
			"    Filter (lang(?labelDe) = 'de') .}\r\n" + 
			"  UNION\r\n" + 
			"  {?resc rdf:type dbo:Place .\r\n" + 
			"   ?resc rdfs:label ?labelDe .\r\n" + 
			"  ?resc foaf:name ?labelEn .\r\n" + 
			"    Filter (lang(?labelDe) = 'en') .}\r\n" + 
			"} \r\n" + 
			"LIMIT 500";
    private static String path = "result.txt";
	
	public static void writeFile(String writeMessage, String path) 
			  throws IOException {
		        File file = new File(path);
			    BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			    writer.append(writeMessage);
			    writer.newLine();
			     
			    writer.close();
			}
	public sparlQLNoFile() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		 ParameterizedSparqlString qs = new ParameterizedSparqlString(sparqlQuery);

	        QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs.asQuery());

	        ResultSet results = exec.execSelect();

	        while (results.hasNext()) {
	        	
	            //System.out.println(results.next().get("?resc").toString());
	        	
	        	//The output of the query is written to a file
	            try {
					writeFile(results.next().get("?resc").toString(), path);
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }

	        //ResultSetFormatter.out(results);
	        //ResultSetFormatter.out(System.out, results);
	    }

}
