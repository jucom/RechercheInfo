package reformulation;

import java.util.Map;
import java.util.Map.Entry;

import model.Request;

import sparqlclient.SparqlClient;

public class Reformulation {
	
	public static void main(String[] args){
		SparqlClient sparqlClient = new SparqlClient("localhost:3030/space");
		String query = "ASK WHERE { ?s ?p ?o }";
		boolean serverIsUp = sparqlClient.ask(query);
		if (serverIsUp) {
			System.out.println("server is UP");

			ensembleLabelRessource(sparqlClient,"prix");

		} else {
			System.out.println("service is DOWN");
		}
	}

	public void reformulation(Request req){
		
		SparqlClient sparqlClient = new SparqlClient("http://localhost:3030/space");

		String query = "ASK WHERE { ?s ?p ?o }";
		boolean serverIsUp = sparqlClient.ask(query);
		if (serverIsUp) {
			System.out.println("server is UP");

			ensembleLabelRessource(sparqlClient, "prix");

		} else {
			System.out.println("service is DOWN");
		}
	}

	private static void nbPersonnesParPiece(SparqlClient sparqlClient) {
		String query = "PREFIX : <http://www.lamaisondumeurtre.fr#>\n"
				+ "SELECT ?piece (COUNT(?personne) AS ?nbPers) WHERE\n"
				+ "{\n"
				+ "    ?personne :personneDansPiece ?piece.\n"
				+ "}\n"
				+ "GROUP BY ?piece\n";
		Iterable<Map<String, String>> results = sparqlClient.select(query);
		System.out.println("nombre de personnes par pièce:");
		for (Map<String, String> result : results) {
			System.out.println(result.get("piece") + " : " + result.get("nbPers"));
		}
	}    
	
	private static Iterable<Map<String, String>> ensembleLabelRessource(SparqlClient sparqlClient, String label) {
		String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "SELECT ?label WHERE\n"
				+ "{\n"
				+ "    ?res rdfs:label \""+label +"\" @fr.\n"
				+ "    ?res rdfs:label ?label.\n"	
				+ "}\n";
		Iterable<Map<String, String>> results = sparqlClient.select(query);
		System.out.println("Ensemble ou label = " + label);
		for (Map<String, String> result : results) {
			System.out.println(result.get("label"));
		}
		return results;
	}    

}