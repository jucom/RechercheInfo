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
		main(2);
	}
	
	public void testDB() {
		db.loadDB();
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
		db.commit();
		System.out.println("***");
		System.out.println(db.wordExists("coucou"));
		System.out.println(db.wordExists("pizza"));
		System.out.println(db.getID("WORDS", "coucou"));
	}
	
	public static void main(int version){
		System.out.println("Initialisation des tables sql");
		/* comment the 2 following lines if you have already imported the corpus*/
		//FillDatabase fdb = new FillDatabase();
		//fdb.fillDatabaseWithAllFiles(false);
		
		db.loadDB();
		//On charge le Corpus
		docs = FileManager.listerRepertoire(Cst.docsPath);
		//On charge les requetes
		reqs = Request.createListReq(Cst.reqsPath);
		//On initialise le matcher avec la liste des requetes et des docs
		matcher = new Matcher(reqs, db);

		Performance p = new Performance(docs);
		//On met en route le matcher
		if (version == 1) {
			matcher.matchAllV1(docs);
		}
		else if (version == 2) {
			matcher.matchAllV2(docs);
		}
		else if (version == 3) {
			matcher.matchAllV3(docs);
		}
		else {
			System.out.println("Error : Main argument must be 1, 2 or 3.");
			System.exit(0);
		}
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
