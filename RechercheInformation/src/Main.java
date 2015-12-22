import java.util.ArrayList;

import Parser.FileManager;

import matcher.Matcher;
import matcher.Performance;
import model.Cst;
import model.Request;
import indexation.DatabaseMgmt;
import indexation.FillDatabase;

public class Main {
	static FillDatabase fillDb = new FillDatabase();
	static DatabaseMgmt db = new DatabaseMgmt();
	static Matcher matcher;
	static int idWord = 1 ;
	static int idDoc = 1 ; 
	static ArrayList<Request> reqs  = new ArrayList<Request>();
	static ArrayList<String> docs = new ArrayList<String>();



	public static void main( String args[] ){
		mainV1();
		mainV2();
	}
	
	public static void mainV1(){
		db.loadDB();
		// db.createTable();

		//On charge le Corpus
		docs = FileManager.listerRepertoire(Cst.docsPath);

		//On charge les requetes
		reqs = Request.createListReq(Cst.reqsPath);

		//On initialise le matcher avec la liste des requêtes et des docs
		matcher = new Matcher(reqs, db);

		Performance p = new Performance(docs);
		//On met en route le matcher
		matcher.matchAllV1(docs);
		for (Request r : reqs){
			System.out.println(r);
			System.out.println(p.rappel(r, 5)+p.rappel(r, 15)+p.rappel(r, 25)+p.precision(r, 5)+p.precision(r, 15)+p.precision(r, 25));
		}
		db.closeDB();
	}

	

	public static void mainV2(){
		db.loadDB();
		// db.createTable();

		//On charge le Corpus
		docs = FileManager.listerRepertoire(Cst.docsPath);

		//On charge les requetes
		reqs = Request.createListReq(Cst.reqsPath);

		//On initialise le matcher avec la liste des requêtes et des docs
		matcher = new Matcher(reqs, db);

		Performance p = new Performance(docs);
		//On met en route le matcher
		matcher.matchAllV2(docs);
		for (Request r : reqs){
			System.out.println(r);
			System.out.println(p.rappel(r, 5)+p.rappel(r, 15)+p.rappel(r, 25)+p.precision(r, 5)+p.precision(r, 15)+p.precision(r, 25));
		}
		db.closeDB();
	}
	
	

	public static void mainV3(){
		db.loadDB();
		// db.createTable();

		//On charge le Corpus
		docs = FileManager.listerRepertoire(Cst.docsPath);

		//On charge les requetes
		reqs = Request.createListReq(Cst.reqsPath);

		//On initialise le matcher avec la liste des requêtes et des docs
		matcher = new Matcher(reqs, db);

		Performance p = new Performance(docs);
		//On met en route le matcher
		matcher.matchAllV3(docs);
		for (Request r : reqs){
			System.out.println(r);
			System.out.println(p.rappel(r, 5)+p.rappel(r, 15)+p.rappel(r, 25)+p.precision(r, 5)+p.precision(r, 15)+p.precision(r, 25));
		}
		db.closeDB();
	}
	/*public static void matcherTest(){
		  ArrayList<String> docs = new ArrayList<String>();
		  docs = FileManager.listerRepertoire("/home/compagnon/Documents/5A/RI/RechercheInfos/RechercheInformation/CORPUS/CORPUS");
		  //matcher.CleanRequest("Le compagnon de sa soeur est le coucou de sa femme");
		  ArrayList<String> terms = matcher.CleanRequest("personnes, Intouchables");
		  //matcher.SumTermFrequency( terms, "D15.html");
		  Object[] mapTrier = matcher.MatcherDocWords(terms, docs);
		  for (Object e : mapTrier) {
				System.out.println(((Map.Entry<String, Integer>) e).getKey() + " : "
						+ ((Map.Entry<String, Integer>) e).getValue());
			}
	  }*/

	
}
