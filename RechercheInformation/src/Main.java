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
		//main(1);
		//main(2);
		//main(3);
		main(5);
		//testDB();
	}

	public static void testDB() {
		db.loadDB();
		db.deleteTables();
		db.createTable();
		db.setAutoCommit(false);
		db.insertWordOrDoc("WORDS", "coucou");
		db.insertWordOrDoc("DOCS", "D2");
		db.insertWordOrDoc("WORDS", "chat");
		db.insertWordOrDoc("DOCS", "D1");
		System.out.println("insertIndexation(1, 1)");
		db.insertIndexation(1, 1);
		System.out.println("insertIndexation(1, 2)");
		db.insertIndexation(1, 2);
		System.out.println("insertIndexation(2, 2)");
		db.insertIndexationWithFrequency(2, 2, 2);
		db.insertIndexationWithFrequencyAndScore(2, 1, 2,100);
		db.commit();
		System.out.println("***");
		System.out.println(db.wordExists("coucou"));
		System.out.println(db.wordExists("pizza"));
		System.out.println(db.getID("WORDS", "coucou"));
		System.out.println(db.getID("WORDS", "pizza"));
		System.out.println(db.getNbDocContainingWord("chat"));
		System.out.println(db.getOccWordDoc("chat", "D2"));
		System.out.println(db.getScore("D2", "chat"));
		System.out.println(db.getScore("D2", "coucou"));
	}

	public static void main(int version){
		System.out.println("Initialisation des tables sql");
		/* comment the following lines if you have already imported the corpus*/
		/*
		if (version == 5) {
			fillDb.fillDatabaseWithAllFiles(true);
		}
		else {
			fillDb.fillDatabaseWithAllFiles(false);
		}
		*/
		db.loadDB();
		//On charge le Corpus
		docs = FileManager.listerRepertoire();
		//On charge les requetes
		reqs = Request.createListReq(Cst.reqsPath);
		//On initialise le matcher avec la liste des requetes et des docs
		matcher = new Matcher(reqs, db);

		Performance p = new Performance(docs);
		//On met en route le matcher
		if (version > 0 && version < 6) {
			matcher.matchAll(docs, version);
		}
		else {
			System.out.println("Error : Main argument must belong to [1,5].");
			System.exit(0);
		}
		for (Request r : reqs){
			System.out.println(r);
			System.out.println(p.rappel(r, 5)+p.rappel(r, 10)+p.rappel(r, 25)+p.precision(r, 5)+p.precision(r, 10)+p.precision(r, 25));
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
