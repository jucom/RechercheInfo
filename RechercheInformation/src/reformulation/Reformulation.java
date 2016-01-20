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
		
			ArrayList<String> resProp, resLab0, resLab1 = new ArrayList<String>();
			System.out.println("req.getKeyWords().get(req.getKeyWords().size()-2) : " + req.getKeyWords().get(req.getKeyWords().size()-2));

			
			
			//on chercher les correspondances dans l'ontologie
			resProp = labelsOfRessource(sparqlClient,req.getKeyWords().get(req.getKeyWords().size()-2));
			System.out.println("req.getKeyWords().get(req.getKeyWords().size()-1) : " + req.getKeyWords().get(req.getKeyWords().size()-1));
			req.addListToReformulation(resProp); 
			//on ajoute keyword à la reformulation
			req.addReqTermReformulation(req.getKeyWords().get(req.getKeyWords().size()-2));
			
	
			
			//on chercher les correspondances dans l'ontologie
			resLab1 = labelsOfRessource(sparqlClient,req.getKeyWords().get(req.getKeyWords().size()-1));
			req.addListToReformulation(resLab1);
			//on ajoute keyword à la reformulation
			req.addReqTermReformulation(req.getKeyWords().get(req.getKeyWords().size()-1));
			
			System.out.println("resLab1 : " + req.getReformulation());
			System.out.println("resProp : " + req.getReformulation());

			
			//si trois terme pas oublier le dernier
			if (req.getKeyWords().size() == 3){
				//on chercher les correspondances dans l'ontologie
				resLab0 = labelsOfRessource(sparqlClient,req.getKeyWords().get(0));
				req.addListToReformulation(resLab0);
				//on ajoute keyword à la reformulation
				req.addReqTermReformulation(req.getKeyWords().get(0));
			}
			
			System.out.println("resProp " + req.getReformulation());
			for (String p : resProp){
				for (String l : resLab1){
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
				+"{\n"
				+ "    ?res rdfs:label \""+label +"\" @fr.\n"
				+ "    ?res rdfs:label ?label.\n"
				+"FILTER langMatches(lang(?label),\"fr\").}\n"
				+"UNION {?res rdfs:label \""+label +"\".\n"
				+"?res rdfs:label ?label.}\n"
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
			r.add("prix");
			r.add("Omar Sy");
			r.add("Globes cristal 2012");
			req.setKeyWords(r);
			reformulation(req, sparqlClient);
			System.out.println(req.getReformulation());

		} else {
			System.out.println("service is DOWN");
		}
	}

}