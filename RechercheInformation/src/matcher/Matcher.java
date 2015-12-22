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

	//###################################################
	//                       V1
	//###################################################
	public int sumTermFrequencyV1(Request r, String doc){
		int tf = 0;
		//Pour chaque terme on cherche le nombre d'occurrence dans le Doc
		for (String t : r.getCleanReq())  {
			int rs = 0;
			//On met à jour la somme des tf
			//System.out.println("rs : Ok");
			rs = db.getOccWordDoc(t, doc);
			//System.out.println("rs : " + rs);
			tf += rs;
			if (rs!=0){
				r.setNbDocFinded(r.getNbDocFinded()+1);
			}
		}
		return tf;
	}

	//créé un tableau contenant la liste des documents qui contiennent un  des termes
	//et son nombre d'occurrence
	public ArrayList<String> matcherDocWordsV1(Request req, ArrayList<String> docs){
		//Pour tous les docs on calcules la sum des tfs et on les classes
		Map<String,Integer> map = new HashMap<>();
		//System.out.println("declaration Map OK");
		for (String doc :docs){
			int res =  sumTermFrequencyV1(req, doc);
			map.put(doc, res);
		}

		Object[] mapTrier = map.entrySet().toArray();
		Arrays.sort(mapTrier, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Map.Entry<String, Integer>) o2).getValue().compareTo(
						((Map.Entry<String, Integer>) o1).getValue());
			}
		});
		ArrayList<String> listDocFinded = new ArrayList<String>();
		int i = 0;
		for (Object e : mapTrier) {
			listDocFinded.add(((Map.Entry<String, Integer>) e).getKey());
			i ++;
		}

		/*for (String s : listDocFinded) {
					System.out.println(s);
				}
				System.out.println("");*/

		return listDocFinded;

	}

	public ArrayList<String> matcherDocReqV1(Request req, ArrayList<String> docs){
		return matcherDocWordsV1(req,docs);
	}
	
	public void matchAllV1(ArrayList<String> docs){
		Performance p = new Performance(docs);
		for (Request r : reqs){
			r.setListDoc(matcherDocReqV1(r, docs));
			p.rappel(r,10);
			p.precision(r,10);
		}
	}


	//###################################################
	//                       V2
	//###################################################
	public float sumTermFrequencyV2(Request r, String doc){
		int tf = 0;
		int nbWords;
		//Pour chaque terme on cherche le nombre d'occurrence dans le Doc
		for (String t : r.getCleanReq())  {
			int rs = 0;
			//On met à jour la somme des tf
			//System.out.println("rs : Ok");
			rs = db.getOccWordDoc(t, doc);
			//System.out.println("rs : " + rs);
			tf += rs;
			if (rs!=0){
				r.setNbDocFinded(r.getNbDocFinded()+1);
			}
		}
		nbWords = FileManager.nbWordsInDoc(Cst.docsPath+"/"+doc);
		//System.out.println("tf/nbWords = " + (float)tf/nbWords);
		return  (float)tf/nbWords;
	}

	//créé un tableau contenant la liste des documents qui contiennent un  des termes
	//et son nombre d'occurrence
	public ArrayList<String> matcherDocWordsV2(Request req, ArrayList<String> docs){

		Map<String,Float> map = new HashMap<>();
		//System.out.println("declaration Map OK");
		for (String doc :docs){
			float res =  sumTermFrequencyV2(req, doc);
			map.put(doc, res);
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

	public ArrayList<String> matcherDocReqV2(Request req, ArrayList<String> docs){
		return matcherDocWordsV2(req,docs);
	}
	
	public void matchAllV2(ArrayList<String> docs){
		Performance p = new Performance(docs);
		for (Request r : reqs){
			r.setListDoc(matcherDocReqV2(r, docs));
			p.rappel(r,10);
			p.precision(r,10);
		}
	}


	//###################################################
	//                       V3
	//###################################################


	//Pour la v3 il faut garder en memoire le nombre de fois qu'apparaît chaque mot dans le doc
	public void termFrequencyV3(Request r, String doc){
		float tf = 0;
		int nbWords;
		//Pour chaque terme on cherche le nombre d'occurrence dans le Doc
		for (Term t : r.getReqTerm())  {
			//On met à jour la somme des tf
			tf = db.getOccWordDoc(t.getName(), doc);
			nbWords = FileManager.nbWordsInDoc(Cst.docsPath+"/"+doc);
			t.setTf(tf/nbWords);
			if (tf!=0.0){
				r.setNbDocFinded(r.getNbDocFinded()+1);
			}
		}
		//System.out.println("tf/nbWords = " + (float)tf/nbWords);
	}





	//créé un tableau contenant la liste des documents qui contiennent un  des termes
	//et son nombre d'occurrence
	public ArrayList<String> matcherDocWordsV3(Request req, ArrayList<String> docs){
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
	public ArrayList<String> matcherDocReqV3(Request req, ArrayList<String> docs){
		return matcherDocWordsV3(req,docs);
	}

	public void matchAllV3(ArrayList<String> docs){
		Performance p = new Performance(docs);
		for (Request r : reqs){
			r.setListDoc(matcherDocReqV3(r, docs));
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
