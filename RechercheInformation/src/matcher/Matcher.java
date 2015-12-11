package matcher;

import indexation.DatabaseMgmt;
import indexation.Parser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Matcher {
	DatabaseMgmt db;
	public Matcher() {
		this.db = new DatabaseMgmt();
		// TODO Auto-generated constructor stub
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

	public int SumTermFrequency(ArrayList<String> termes, String doc){
		int tf = 0;
		//Pour chaque termes on cherche le nombre d'occurrence dans le Doc
		for (String t : termes)  {
			int rs = 0;
			//On met à jour la somme des tf
			rs = db.getOccWordDoc(t, doc);
			tf += rs;
		}
		return tf;
	}

	//créé un tableau contenant la liste des documents qui contiennent un  des termes
	//et son nombre d'occurrence
	//verifier le resultat !!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public Map<Integer,String> MatcherDocWords(ArrayList<String> termes, ArrayList<String> docs){
		//Pour tous les docs on calcules la sum des tfs et on les classes
		Map<Integer,String> map = new HashMap<>();
		System.out.println("declaration Map OK");
		for (String doc :docs){
			int res =  SumTermFrequency(termes, doc);
			map.put(res, doc);
		}
		printMap(map);
		 Map<Integer, String> treeMap = new TreeMap<Integer, String>(
				new Comparator<Integer>() {

					@Override
					public int compare(Integer o1, Integer o2) {
						return o2.compareTo(o1);
					}

				});
		treeMap.putAll(treeMap);

		return treeMap; //Verifier le resultat
	}

	public static void printMap(Map<Integer, String> map) {
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			System.out.println("sumtf : " + entry.getKey() 
					+ " Doc : " + entry.getValue());
		}
	}


}
