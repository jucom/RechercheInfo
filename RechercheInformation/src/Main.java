import java.util.ArrayList;
import java.util.Map;

import Parser.FileManager;

import matcher.Matcher;
import indexation.DatabaseMgmt;
import indexation.FillDatabase;




public class Main {
	static FillDatabase fillDb = new FillDatabase();
	static DatabaseMgmt db = new DatabaseMgmt();
	//static Matcher matcher = new Matcher();
	static int idWord = 1 ;
	static int idDoc = 1 ; 
	
	  public static void main( String args[] ){
		  db.loadDB();
		  db.createTable();
		  /*db.insertWordOrDoc("WORDS", "coucou");
		  db.insertWordOrDoc("DOCS", "D2");
		  db.insertWordOrDoc("WORDS", "chat");
		  db.insertWordOrDoc("DOCS", "D1");
		  System.out.println("insertIndexation(1, 1)");
		  db.insertIndexation(1, 1);
		  System.out.println("insertIndexation(1, 2)");
		  db.insertIndexation(1, 2);
		  System.out.println("insertIndexation(2, 2)");
		  db.insertIndexation(2, 2);
		  System.out.println("***");
		  System.out.println(db.wordExists("coucou"));
		  System.out.println(db.wordExists("pizza"));
		  System.out.println(db.getID("WORDS", "coucou"));*/
		  //db.insertIndexation(23, 23);
		  //matcher.setDatabaseMgmt(db);
		  matcherTest();
		  
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
