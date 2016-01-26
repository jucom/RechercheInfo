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
				sumTermFrequencyV4(req, doc);
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
		else if (version != 4) {
			listDocFinded = createListDocFinded(mapF);
		} else {
			listDocFinded = null;
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
		if (version == 4){
			getCosineSimilarity(); //calculates cosine similarity  
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
			tf = tf*(t.getIdf() + t.getPoids());
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
	private ArrayList<float[]> tfidfDocsVector = new ArrayList<float[]>();
	private ArrayList<float[]> tfidfReqVector = new ArrayList<float[]>();
	private ArrayList<String> docList = new ArrayList<String>();
	private ArrayList<Request> reqList = new ArrayList<Request>();


	/**
	 * Method to calculate cosine similarity between two documents.
	 * @param docVector1 : document vector 1 (a)
	 * @param docVector2 : document vector 2 (b)
	 * @return 
	 */
	public float cosineSimilarity(float[] docVector1, float[] docVector2) {
		float dotProduct = (float) 0.0;
		float magnitude1 = (float) 0.0;
		float magnitude2 = (float) 0.0;
		float cosineSimilarity = (float) 0.0;

		if (docVector1 != null && docVector2 != null){

			for (int i = 0; i < docVector1.length; i++) //docVector1 and docVector2 must be of same length
			{
				//System.out.println( "taille vector 1 = " +  docVector1.length);
				//System.out.println( "taille vector 2 = " +  docVector2.length);


				dotProduct += docVector1[i] * docVector2[i];  //a.b
				//System.out.println(" dotProduct = " + docVector2[i] );

				magnitude1 += Math.pow(docVector1[i], 2);  //(a^2)
				magnitude2 += Math.pow(docVector2[i], 2); //(b^2)

				//System.out.println("magnitude2 = " + magnitude2 +  " dotProduct = " + dotProduct );

			}
			magnitude1 =(float)  Math.sqrt(magnitude1);//sqrt(a^2)
			magnitude2 = (float) Math.sqrt(magnitude2);//sqrt(b^2)

			//System.out.println("magnitude1 = " + magnitude1 +  "magnitude2 = " + magnitude2 );
			if (magnitude1 != 0 && magnitude2 != 0 && dotProduct != 0) {
				//System.out.println("passe par là  + magnitude1 = " + magnitude1 +  "magnitude2 = " + magnitude2 +  "dotProduct = " + dotProduct );
				cosineSimilarity = (float) (dotProduct / (magnitude1 * magnitude2));
				//System.out.println("passe par là  + cos = " + cosineSimilarity );
			} else {
				return (float)0;
			}
		}
		return cosineSimilarity;
	}




	//Pour la v3 il faut garder en memoire le nombre de fois qu'apparait chaque mot dans le doc
	public float sumTermFrequencyV4(Request r, String doc){
		float tf = (float) 0.0;
		int count = 0;
		float tfidf = (float) 0.0;
		int nbWords;
		setIdf(r, doc);
		float[] tfidfvectors = new float[r.getReqTerm().size()];

		float[] termVector = new float[r.getReqTerm().size()];
		//System.out.println("Size : " + r.getReqTerm().size());
		for (int i=0; i<r.getReqTerm().size();i++){
		}

		for (Term t : r.getReqTerm())  {
			//On met a jour la somme des tf
			//System.out.println("t.getName() = "+ t.getName());
			tf =  db.getOccWordDoc(t.getName(), doc);
			//System.out.println("1. sumTermFrequencyV4 : tf " + tf + " term : " + t.getName() + " doc : " + doc);
			nbWords = FileManager.nbWordsInDoc(Cst.docsPath+"/"+doc);
			//System.out.println("nbWords = "+ nbWords);
			if (nbWords != 0 || tf !=0){
				tf = tf/(float) nbWords;
				//System.out.println("tf = "+ tf);
				tf = tf*(t.getIdf() + t.getPoids());
				//System.out.println("tf = "+ tf);
			}	
			tfidfvectors[count] = tf;
			//System.out.println("idf = " + t.getIdf()/3);
			termVector[count] = (float) (10);

			//System.out.println("tfidfvectors[count] = "+ tfidfvectors[count]);
			count++;
			if (tf!=0.0){
				r.setNbDocFinded(r.getNbDocFinded()+1);
			}
			tfidf += tf;
		}
		tfidfDocsVector.add(tfidfvectors);  //storing document vectors;  
		tfidfReqVector.add(termVector);
		docList.add(doc);
		reqList.add(r);
		//System.out.println(r.getName() + " Size tfidfvect = " + tfidfvectors.length);

		return tfidf;
	}

	/**
	 * Method to calculate cosine similarity between all the documents.
	 */
	public void getCosineSimilarity() {

		Map<String,Float> mapF = new HashMap<>();
		Request curReq = reqList.get(0);
		for (int j = 0; j < tfidfDocsVector.size(); j++) {
			float cos = cosineSimilarity((float[])tfidfReqVector.get(j), (float[])tfidfDocsVector.get(j));
			if (cos != 0) {
				//System.out.println(r.getCleanReq());
				//System.out.println("between " + reqList.get(j).getName() + " and " + docList.get(j) + "  =  "
				//	+ cos);
				if (curReq == reqList.get(j)) {
					mapF.put(docList.get(j), cos);
					ArrayList<String> listDocFinded = new ArrayList<String>();
					listDocFinded = createListDocFinded(mapF);
					curReq.setListDoc(listDocFinded);
				} else {
					curReq = reqList.get(j);
					mapF = new HashMap<>();
				}
			}

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
