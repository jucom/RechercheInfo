package reformulation;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import sparqlclient.SparqlClient;

import model.Request;


public class Reformulation {
	
	SparqlClient sparqlClient;

	/**
	 * @return SparqlClient
	 */
	public SparqlClient getSparqlClient() {
		return sparqlClient;
	}

	public static void main(String[] args){
		SparqlClient sparqlClient = new SparqlClient("localhost:3030/space");
		String query = "ASK WHERE { ?s ?p ?o }";
		boolean serverIsUp = sparqlClient.ask(query);
		if (serverIsUp) {
			//String rs = "";
			System.out.println("server is UP");
			//labelLinkToProp(sparqlClient,"lieu naissance", "Omar Sy");
			Request req = new Request("Test");
			ArrayList<String> r = new ArrayList<String>();
			r.add("lieu naissance");
			r.add("Omar Sy");
			req.setKeyWords(r);
			reformulation(req, sparqlClient);
			System.out.println(req.getReformulation());

		} else {
			System.out.println("service is DOWN");
		}
	}
	
	public Reformulation(){
		SparqlClient sparqlClient = new SparqlClient("localhost:3030/space");
		String query = "ASK WHERE { ?s ?p ?o }";
		boolean serverIsUp = sparqlClient.ask(query);
		if (serverIsUp) {
			//String rs = "";
			System.out.println("server is UP");
		} else {
			System.out.println("service is DOWN");
		}
	}

	/**
	 * Reformule une requête
	 * @param req
	 * @param sparqlClient
	 */
	public static void reformulation(Request req, SparqlClient sparqlClient){
		
		if (req.getKeyWords().size() == 2){
			ArrayList<String> resProp, resLab = new ArrayList<String>();
			//on chercher les correspondances dans l'ontologie
			resProp = labelsOfRessource(sparqlClient,req.getKeyWords().get(0));
			//on ajoute aux resultat la propriete initiale
			resProp.add(req.getKeyWords().get(0));
			//on chercher les correspondances dans l'ontologie
			resLab = labelsOfRessource(sparqlClient,req.getKeyWords().get(1));
			//on ajoute aux resultat la propriete initiale
			resLab.add(req.getKeyWords().get(1));
			req.addListToReformulation(resLab);
			req.addListToReformulation(resProp); 
			System.out.println("resProp " + req.getReformulation());
			for (String p : resProp){
				for (String l : resLab){
					ArrayList<String> res1 =  new ArrayList<String>();
					ArrayList<String> res2 =  new ArrayList<String>();

					res2 = labelLinkToProp(sparqlClient,l,p);
					res1 = labelLinkToProp(sparqlClient,p,l);
					if (res1.size() != 0){
						req.addListToReformulation(res1);
					}
					if (res2.size() != 0){
						req.addListToReformulation(res2);
					}

				}
			}

		} else if (req.getKeyWords().size() == 3){

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
		for (Map<String, String> result : results) {
			//System.out.println(result.get("label"));
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
		for (Map<String, String> result : results) {
			//System.out.println(result.get("label"));
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
		for (Map<String, String> result : results) {
			//System.out.println(result.get("label"));
			res.add(result.get("label"));
		}
		return res;
	} 


}