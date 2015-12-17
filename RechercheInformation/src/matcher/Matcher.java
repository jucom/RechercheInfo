package matcher;

import indexation.DatabaseMgmt;
import Parser.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Matcher {
	DatabaseMgmt db;
	
	public Matcher() {
		this.db = new DatabaseMgmt();
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
		//Pour chaque terme on cherche le nombre d'occurrence dans le Doc
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
	public Object[] MatcherDocWords(ArrayList<String> termes, ArrayList<String> docs){
		//Pour tous les docs on calcules la sum des tfs et on les classes
		Map<String,Integer> map = new HashMap<>();
		System.out.println("declaration Map OK");
		for (String doc :docs){
			int res =  SumTermFrequency(termes, doc);
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

	public static void printMap(Map<String, Integer> map) {
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.println("Doc : " + entry.getKey() 
					+ " sumtf : " + entry.getValue());
		}
	}


}
