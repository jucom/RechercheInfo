package reformulation;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import sparqlclient.SparqlClient;

import model.Request;


public class Reformulation {
	
	public static void main(String[] args){
		SparqlClient sparqlClient = new SparqlClient("localhost:3030/space");
		String query = "ASK WHERE { ?s ?p ?o }";
		boolean serverIsUp = sparqlClient.ask(query);
		if (serverIsUp) {
			System.out.println("server is UP");
			labelLinkToProp(sparqlClient,"lieu naissance", "Omar Sy");

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
			
			if (req.getKeyWords().size() == 2){
				ArrayList<String> resProp, resLab = new ArrayList<String>();
				resProp = labelsOfRessource(sparqlClient,req.getKeyWords().get(0));
				resLab = labelsOfRessource(sparqlClient,req.getKeyWords().get(1));
				req.addListToReformulation(resLab); //utile??
				req.addListToReformulation(resProp); //utile??
				for (String p : resProp){
					for (String l : resProp){
						ArrayList<String> res =  new ArrayList<String>();
						res = labelLinkToProp(sparqlClient,p,l);
						if (res.size() != 0){
							req.addListToReformulation(resProp);
						}
						
					}
				}

			}
			

		} else {
			System.out.println("service is DOWN");
		}
	}   
	
	/**
	 * @param sparqlClient
	 * @param label
	 * @return l'ensemble des labels définis pour la ressource dont le label est label
	 */
	private static ArrayList<String> labelsOfRessource(SparqlClient sparqlClient, String label) {
		String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "SELECT ?label WHERE\n"
				+ "{\n"
				+ "    ?res rdfs:label \""+label +"\" @fr.\n"
				+ "    ?res rdfs:label ?label.\n"	
				+ "}\n";
		Iterable<Map<String, String>> results = sparqlClient.select(query);
		ArrayList<String> res = new ArrayList<String>();
		System.out.println("Ensemble ou label = " + label);
		for (Map<String, String> result : results) {
			System.out.println(result.get("label"));
			res.add(result.get("label"));
		}
		return res;
	}    

	
	/**
	 * @param sparqlClient
	 * @param label
	 * @return l'ensemble des labels définis pour des ressources dont le label contient «prix»
	 */
	private static ArrayList<String> labelsContainingRessource(SparqlClient sparqlClient, String label) {
		String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "SELECT ?label WHERE\n"
				+ "{\n"
				+ "    ?res rdfs:label ?label.\n"
				+ "    filter contains(?label,\""+label+"\").\n"	
				+ "}\n";
		Iterable<Map<String, String>> results = sparqlClient.select(query);
		ArrayList<String> res = new ArrayList<String>();
		System.out.println("Ensemble ou label = " + label);
		for (Map<String, String> result : results) {
			System.out.println(result.get("label"));
			res.add(result.get("label"));
		}
		return res;
	}    
	
	
	/**
	 * @param sparqlClient
	 * @param prop
	 * @param label
	 * @return les labels de la ressource liée par la propriété ayant pour label prop à la ressource ayant pour label label

	 */
	private static ArrayList<String> labelLinkToProp(SparqlClient sparqlClient,String prop, String label) {
		String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "SELECT ?label WHERE\n"
				+ "{\n"
				+ "    ?res rdfs:label ?label.\n"
				+ "    ?prop rdfs:label \""+prop+"\" @fr.\n"
				+ "    ?sujet rdfs:label \""+label+"\".\n"
				+ "    ?sujet ?prop ?res.\n"	
				+ "}\n";
		Iterable<Map<String, String>> results = sparqlClient.select(query);
		ArrayList<String> res = new ArrayList<String>();
		System.out.println("Ensemble ou label = " + label);
		for (Map<String, String> result : results) {
			System.out.println(result.get("label"));
			res.add(result.get("label"));
		}
		return res;
	} 
	
	
}