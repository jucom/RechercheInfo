package matcher;

import indexation.DatabaseMgmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<String> createListDocFindedV1(Map<String,Integer> map) {
		Object[] mapTrier = map.entrySet().toArray();
		Arrays.sort(mapTrier, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Map.Entry<String, Integer>) o2).getValue().compareTo(
						((Map.Entry<String, Integer>) o1).getValue());
			}
		});
		ArrayList<String> listDocFinded = new ArrayList<String>();
		for (Object e : mapTrier) {
			listDocFinded.add(((Map.Entry<String, Integer>) e).getKey());
		}
		return listDocFinded;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<String> createListDocFinded(Map<String,Float> map) {
		Object[] mapTrier = map.entrySet().toArray();
		Arrays.sort(mapTrier, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Map.Entry<String, Float>) o2).getValue().compareTo(
						((Map.Entry<String, Float>) o1).getValue());
			}
		});
		ArrayList<String> listDocFinded = new ArrayList<String>();
		for (Object e : mapTrier) {
			listDocFinded.add(((Map.Entry<String, Float>) e).getKey());
		}
		return listDocFinded;
	}

	public ArrayList<String> matcherDocWords(Request req, ArrayList<String> docs, int version){
		Map<String,Integer> mapI = new HashMap<>();;
		Map<String,Float> mapF = new HashMap<>();;
		for (String doc :docs){
			if (version == 1) {
				int res =  sumTermFrequencyV1(req, doc);
				mapI.put(doc, res);
			}
			else if (version == 2) {
				float res =  sumTermFrequencyV2(req, doc);
				mapF.put(doc, res);
			}
			else if (version == 3) {
				float res =  sumTermFrequencyV3(req, doc);
				mapF.put(doc, res);
			}
			else if (version == 4) {
				//float res =  sumTermFrequencyV4(req, doc);
				//mapF.put(doc, res);
			}
			else if (version == 5) {
				float res =  sumTermFrequencyV5(req, doc);
				mapF.put(doc, res);
			}
		}

		ArrayList<String> listDocFinded = new ArrayList<String>();
		if (version == 1) {
			listDocFinded = createListDocFindedV1(mapI);
		}
		else {
			listDocFinded = createListDocFinded(mapF);
		}
		return listDocFinded;
	}

	public ArrayList<String> matcherDocReq(Request req, ArrayList<String> docs, int version){	
		return matcherDocWords(req,docs, version);
	}

	public void matchAll(ArrayList<String> docs, int version){
		for (Request r : reqs){
			r.setListDoc(matcherDocReq(r, docs, version));
		}
	}


	//###################################################
	//                       V1
	//###################################################

	public int sumTermFrequencyV1(Request r, String doc){
		int tf = 0;
		//Pour chaque terme on cherche le nombre d'occurrence dans le Doc
		for (String t : r.getCleanReq())  {
			int rs = 0;
			//On met a jour la somme des tf
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

	//###################################################
	//                       V2
	//###################################################

	public float sumTermFrequencyV2(Request r, String doc){
		int tf = 0;
		int nbWords;
		//Pour chaque terme on cherche le nombre d'occurrence dans le Doc
		for (String t : r.getCleanReq())  {
			int rs = 0;
			//On met a jour la somme des tf
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

	//###################################################
	//                       V3
	//###################################################

	// idf = log(N/ni)
	// N : taille de la collection
	// ni : nb de documents contenant le terme ti
	public float calculateIDF(float N, float ni) {
		return (float) Math.log(N/ni);
	}

	public void setIdf (Request r, String doc) {
		float idf = 0;
		float ni;
		for (Term t : r.getReqTerm())  {
			ni = db.getNbDocContainingWord(t.getName());
			idf = calculateIDF(FileManager.NbDocInCorpus(), ni);
			t.setIdf(idf);
		}
	}


	//Pour la v3 il faut garder en memoire le nombre de fois qu'apparait chaque mot dans le doc
	public float sumTermFrequencyV3(Request r, String doc){
		float tf = 0;
		float tfidf = 0;
		int nbWords;
		setIdf(r, doc);
		for (Term t : r.getReqTerm())  {
			//On met a jour la somme des tf
			tf = db.getOccWordDoc(t.getName(), doc);
			//System.out.println("1. sumTermFrequencyV3 : tf " + tf + " term : " + t.getName());
			nbWords = FileManager.nbWordsInDoc(Cst.docsPath+"/"+doc);
			//System.out.println("2. sumTermFrequencyV3 : nbWords " + nbWords);
			tf = tf/nbWords;
			//System.out.println("3. sumTermFrequencyV3 : tf " + tf + " term : " + t.getName());
			//System.out.println("3.2 sumTermFrequencyV3 : idf " + (float)t.getIdf() + " term : " + t.getName());
			tf = tf*t.getIdf();
			//System.out.println("4. sumTermFrequencyV3 : tf " + tf + " term : " + t.getName());
			if (tf!=0.0){
				//System.out.println("5. sumTermFrequencyV3 : terme :  " + t.getName());
				r.setNbDocFinded(r.getNbDocFinded()+1);
			}
			tfidf += tf;
		}
		return tfidf;
	}

	public static void printMap(Map<String, Integer> map) {
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.println("Doc : " + entry.getKey() 
					+ " sumtf : " + entry.getValue());
		}
	}

	//###################################################
	//                       V4
	//###################################################

	//This variable will hold all terms of each document in an array.
	private List tfidfDocsVector = new ArrayList<>();
	private List tfidfReqVector = new ArrayList<>();
	private List docList = new ArrayList<>();
	

	/**
	 * Method to calculate cosine similarity between two documents.
	 * @param docVector1 : document vector 1 (a)
	 * @param docVector2 : document vector 2 (b)
	 * @return 
	 */
	public double cosineSimilarity(double[] docVector1, double[] docVector2) {
		double dotProduct = 0.0;
		double magnitude1 = 0.0;
		double magnitude2 = 0.0;
		double cosineSimilarity = 0.0;

		if (docVector1 != null && docVector1 != null){

			for (int i = 0; i < docVector1.length; i++) //docVector1 and docVector2 must be of same length
			{
				dotProduct += docVector1[i] * docVector2[i];  //a.b
				magnitude1 += Math.pow(docVector1[i], 2);  //(a^2)
				magnitude2 += Math.pow(docVector2[i], 2); //(b^2)
			}

			magnitude1 = Math.sqrt(magnitude1);//sqrt(a^2)
			magnitude2 = Math.sqrt(magnitude2);//sqrt(b^2)

			if (magnitude1 != 0.0 | magnitude2 != 0.0) {
				cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
			} else {
				return 0.0;
			}
		}
		return cosineSimilarity;
	}




	//Pour la v3 il faut garder en memoire le nombre de fois qu'apparait chaque mot dans le doc
	public float sumTermFrequencyV4(Request r, String doc){
		float tf = 0;
		int count = 0;
		float tfidf = 0;
		int nbWords;
		setIdf(r, doc);
		double[] tfidfvectors = new double[r.getReqTerm().size()];
		for (Term t : r.getReqTerm())  {
			//On met a jour la somme des tf
			tf = db.getOccWordDoc(t.getName(), doc);
			nbWords = FileManager.nbWordsInDoc(Cst.docsPath+"/"+doc);
			tf = tf/nbWords;
			tf = tf*t.getIdf();
			tfidfvectors[count] = tf;
			count++;
			if (tf!=0.0){
				r.setNbDocFinded(r.getNbDocFinded()+1);
			}
			tfidf += tf;
		}
		tfidfDocsVector.add(tfidfvectors);  //storing document vectors;  
		docList.add(doc);
		return tfidf;
	}

	/**
	 * Method to calculate cosine similarity between all the documents.
	 */
	public void getCosineSimilarity(Request r) {
;
			double[] termVector = new double[r.getReqTerm().size()];
			System.out.println(r.getReqTerm().size());
			for (int i=0; i<r.getReqTerm().size();i++){
				termVector[i] = (double) (1.0 / r.getReqTerm().size());
			}
			tfidfReqVector.add(termVector);
			
			for (int j = 0; j < tfidfDocsVector.size(); j++) {
				double cos = cosineSimilarity((double[])tfidfReqVector.get(0), (double[]) tfidfDocsVector.get(j));
				if (cos != 0) {
					System.out.println("between " + r.getName() + " and " + docList.get(j) + "  =  "
						+ cos);
				}
			}
	}


	//cree un tableau contenant la liste des documents qui contiennent un  des termes
	// de la requete et le tf-idf de chaque document
	public ArrayList<String> matcherDocWordsV4(Request req, ArrayList<String> docs){
		//Pour tous les docs on calcule tfidf et on les classe
		Map<String,Float> map = new HashMap<>();
		//System.out.println("declaration Map OK");
		for (String doc :docs){
			float res =  sumTermFrequencyV4(req, doc);
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



	/**
	 * Permet de calculer le tfidf pour chaque document d'apres la requete donnee
	 * @param req
	 * @param docs
	 * @return
	 */
	public ArrayList<String> matcherDocReqV4(Request req, ArrayList<String> docs){
		return matcherDocWordsV4(req,docs);
	}

	public void matchAllV4(ArrayList<String> docs){
		for (Request r : reqs){
			r.setListDoc(matcherDocReqV4(r, docs));
			getCosineSimilarity(r); //calculates cosine similarity  
		}
	}


	//###################################################
	// V5 : prise en compte des scores (title, h1 et h2)
	//###################################################
	
	public float sumTermFrequencyV5(Request r, String doc){
		int tf = 0;
		int scoreDoc;
		//Pour chaque terme on cherche le score dans le Doc
		for (String t : r.getCleanReq())  {
			int rs = 0;
			//System.out.println("term : "+t);
			rs = db.getScore(doc, t);
			//System.out.println("score : "+rs);
			tf += rs;
			if (rs!=0){
				r.setNbDocFinded(r.getNbDocFinded()+1);
			}
		}
		//nbWords = FileManager.nbWordsInDoc(Cst.docsPath+"/"+doc);
		scoreDoc = db.getScoreOfDdoc(doc);
		return  (float)tf/scoreDoc;
	}

}
