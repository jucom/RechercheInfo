package matcher;

import indexation.DatabaseMgmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import Parser.FileManager;

import model.Cst;
import model.Request;
import model.Term;

/**
 * @author compagnon
 *
 */
public class Matcher {
	DatabaseMgmt db;
	ArrayList<Request> reqs;
	final int NB_DOC_MAX = 25;

	public Matcher(ArrayList<Request> reqs, DatabaseMgmt db) {
		this.db = db;
		this.reqs = reqs;
	}

	public void setDatabaseMgmt(DatabaseMgmt db){
		this.db = db;
	}

	//Pour la v3 il faut garder en memoire le nombre de fois qu'apparaît chaque mot dans le doc
	public void termFrequency(Request r, String doc){
		float tf = 0;
		int nbWords;
		//Pour chaque terme on cherche le nombre d'occurrence dans le Doc
		for (Term t : r.getReqTerm())  {
			int rs = 0;
			//On met à jour la somme des tf
			tf = db.getOccWordDoc(t.getName(), doc);
			nbWords = FileManager.nbWordsInDoc(Cst.docsPath+"/"+doc);
			t.setTf(tf/nbWords);
			if (rs!=0){
				r.setNbDocFinded(r.getNbDocFinded()+1);
			}
		}
		//System.out.println("tf/nbWords = " + (float)tf/nbWords);
	}
	
	
	
	
	
	//créé un tableau contenant la liste des documents qui contiennent un  des termes
	//et son nombre d'occurrence
	public ArrayList<String> matcherDocWords(Request req, ArrayList<String> docs){
		//Pour tous les docs on calcules la sum des tfs et on les classes
		Map<String,Float> map = new HashMap<>();
		//System.out.println("declaration Map OK");
		for (String doc :docs){
			//float res =  termFrequency(req, doc);
			//map.put(doc, res);
		}

		Object[] mapTrier = map.entrySet().toArray();
		Arrays.sort(mapTrier, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Map.Entry<String, Float>) o2).getValue().compareTo(
						((Map.Entry<String, Float>) o1).getValue());
			}
		});
		ArrayList<String> listDocFinded = new ArrayList<String>();
		int i = 0;
		for (Object e : mapTrier) {
			listDocFinded.add(((Map.Entry<String, Float>) e).getKey());
			i ++;
		}
		
		/*for (String s : listDocFinded) {
			System.out.println(s);
		}
		System.out.println("");*/
		
		return listDocFinded;
	}
	
	

	/**
	 * Permet de calculer le tf pour chaque document d'après la requête donnée
	 * @param req
	 * @param docs
	 * @return
	 */
	public ArrayList<String> matcherDocReq(Request req, ArrayList<String> docs){
		return matcherDocWords(req,docs);
	}
	
	public void matchAll(ArrayList<String> docs){
		Performance p = new Performance(docs);
		for (Request r : reqs){
			r.setListDoc(matcherDocReq(r, docs));
			p.rappel(r,10);
			p.precision(r,10);
		}
	}
	

	public static void printMap(Map<String, Integer> map) {
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.println("Doc : " + entry.getKey() 
					+ " sumtf : " + entry.getValue());
		}
	}


}
