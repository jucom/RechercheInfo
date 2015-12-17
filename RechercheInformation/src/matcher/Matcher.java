package matcher;

import indexation.DatabaseMgmt;
import Parser.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author compagnon
 *
 */
public class Matcher {
	DatabaseMgmt db;
	ArrayList<Requete> reqs;
	
	public Matcher(ArrayList<Requete> reqs) {
		this.db = new DatabaseMgmt();
		this.reqs = reqs;
	}

	public void setDatabaseMgmt(DatabaseMgmt db){
		this.db = db;
	}

	//cree un tableau de string avec les mots pertinents
	public ArrayList<String> CleanRequest(String req){
		req = Parser.clean(req);
		req = Parser.removeNumbers(req);
		String[]tabReq = Parser.tokenize(req);
		ArrayList<String> listReq = Parser.troncate(tabReq);
		Parser.printStringArrayList(listReq);
		return listReq;
	}

	public int sumTermFrequency(Requete r, String doc){
		int tf = 0;
		//Pour chaque terme on cherche le nombre d'occurrence dans le Doc
		for (String t : r.getCleanReq())  {
			int rs = 0;
			//On met à jour la somme des tf
			rs = db.getOccWordDoc(t, doc);
			tf += rs;
			if (rs!=0){
				r.setNbDocFinded(r.getNbDocFinded()+1);
			}
		}
		return tf;
	}
	
	


	//créé un tableau contenant la liste des documents qui contiennent un  des termes
	//et son nombre d'occurrence
	//verifier le resultat !!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public Object[] matcherDocWords(Requete req, ArrayList<String> docs){
		//Pour tous les docs on calcules la sum des tfs et on les classes
		Map<String,Integer> map = new HashMap<>();
		System.out.println("declaration Map OK");
		for (String doc :docs){
			int res =  sumTermFrequency(req, doc);
			map.put(doc, res);
		}

		Object[] mapTrier = map.entrySet().toArray();
		Arrays.sort(mapTrier, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Map.Entry<String, Integer>) o2).getValue().compareTo(
						((Map.Entry<String, Integer>) o1).getValue());
			}
		});
		/*for (Object e : mapTrier) {
			System.out.println(((Map.Entry<String, Integer>) e).getKey() + " : "
					+ ((Map.Entry<String, Integer>) e).getValue());
		}*/
		return mapTrier;
	}
	
	

	/**
	 * Permet de calculer le tf pour chaque document d'après la requête donnée
	 * @param req
	 * @param docs
	 * @return
	 */
	public Object[] matcherDocReq(Requete req, ArrayList<String> docs){
		return matcherDocWords(req,docs);
	}
	
	public void matchAll(ArrayList<String> docs){
		Performance p = new Performance(docs);
		for (Requete r : reqs){
			r.setListDoc(matcherDocReq(r,docs));
			p.rappel(r);
			p.precision(r);
		}
	}
	

	public static void printMap(Map<String, Integer> map) {
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.println("Doc : " + entry.getKey() 
					+ " sumtf : " + entry.getValue());
		}
	}


}
