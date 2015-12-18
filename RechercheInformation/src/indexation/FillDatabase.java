package indexation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import Parser.FileManager;
import Parser.Parser;

public class FillDatabase {
	
	private DatabaseMgmt db;
	private String stopListPath ;
	private String docsPath;
	//map : word in db words / id word
	private HashMap<String,Integer> wordsInDB;
	

	public FillDatabase() {
		this.db = new DatabaseMgmt();
		this.docsPath = "./CORPUS/CORPUS/";
		this.stopListPath = "./stopliste.txt";
		this.setWordsInDB(new HashMap<String,Integer>());
	}
	
	public void fillDatabaseWithFile(String input) {
		File inputFile = new File(input);
		// on cree le document dans la table des documents 
		db.insertWordOrDoc("DOCS", inputFile.getName());
		int idDoc = db.getID("DOCS",inputFile.getName());
		// on parse le document input
		ArrayList<String> result = new ArrayList<String>();
		result = Parser.parsing(inputFile, stopListPath);
		// on ajoute tous les mots parsés dans la table
		for (String word: result) {
			if (! db.wordExists(word)) {
				// on ajoute le mot dans la table word
				db.insertWordOrDoc("WORDS",word);
			}
			int idWord = db.getID("WORDS", word);
			db.insertIndexation(idWord,idDoc);			
		}		
	}
	
	public void fillDatabaseWithFileOptimized(String input) {
		File inputFile = new File(input);
		// on cree le document dans la table des documents 
		db.insertWordOrDoc("DOCS", inputFile.getName());
		int idDoc = db.getID("DOCS",inputFile.getName());
		// on parse le document input
		ArrayList<String> result = new ArrayList<String>();
		result = Parser.parsing(inputFile, stopListPath);
		IndexationStructure indexOfWords = new IndexationStructure(idDoc);
		HashMap<Integer,Integer> map = indexOfWords.getMapIdWordFrequency();
		// on ajoute tous les mots parsés dans la table
		for (String word: result) {
			if (! wordsInDB.containsKey(word)) {
				// on ajoute le mot dans la table word
				db.insertWordOrDoc("WORDS",word);
				int idWord = db.getID("WORDS", word);
				wordsInDB.put(word, idWord);
			}
			int idWord = wordsInDB.get(word);
			if (! map.containsKey(idWord)) {
				map.put(idWord, 1);
			}	
			map.put(idWord, map.get(idWord) + 1);
		}
		// insertion des mots dans la table indexation
		for(Entry<Integer, Integer> entry : map.entrySet()) {
			Integer key = entry.getKey();
			Integer value = entry.getValue();
			db.insertIndexationWithFrequency(key, idDoc, value);
		}	
	}
	
	public void fillDatabaseWithAllFiles() {
		ArrayList<String> listRep = new ArrayList<String>();
		listRep = FileManager.listerRepertoire(this.docsPath);
		String input = null;
		db.initDB();		
		for (String file : listRep) {
			input = this.docsPath+file;
			System.out.println("*********");
			System.out.println("FILE "+input);
			fillDatabaseWithFileOptimized(input);
			System.out.println("*********");
		}
		db.closeDB();
	}

	public DatabaseMgmt getDb() {
		return db;
	}

	public void setDb(DatabaseMgmt db) {
		this.db = db;
	}
	
	public String getDocsPath() {
		return docsPath;
	}

	public void setDocsPath(String docsPath) {
		this.docsPath = docsPath;
	}

	public String getStopListPath() {
		return stopListPath;
	}

	public void setStopListPath(String stopListPath) {
		this.stopListPath = stopListPath;
	}
	
	public static void main( String args[] ){
		FillDatabase fdb = new FillDatabase();
		fdb.fillDatabaseWithAllFiles();
		
	}

	public HashMap<String,Integer> getWordsInDB() {
		return wordsInDB;
	}

	public void setWordsInDB(HashMap<String,Integer> wordsInDB) {
		this.wordsInDB = wordsInDB;
	}

}
