package indexation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import model.Cst;


import Parser.FileManager;
import Parser.Parser;
import Parser.ParserWithTags;
import Parser.StringWithScore;

public class FillDatabase {

	private DatabaseMgmt db;
	private String stopListPath ;
	private String docsPath;
	//map : word in db words / id word
	private HashMap<String,Integer> wordsInDB;


	public FillDatabase() {
		this.db = new DatabaseMgmt();
		this.docsPath = Cst.docsPath;
		this.stopListPath = Cst.stopListPath;
		this.setWordsInDB(new HashMap<String,Integer>());
	}

	public void fillDatabaseWithFile(String input, boolean withTags) {
		File inputFile = new File(input);
		// on cree le document dans la table des documents 
		db.insertWordOrDoc("DOCS", inputFile.getName());
		int idDoc = db.getID("DOCS",inputFile.getName());
		IndexationStructure indexOfWords = new IndexationStructure(idDoc);
		HashMap<Integer,Integer> mapFreq = indexOfWords.getMapIdWordFrequency();
		// on parse le document input
		if (withTags) {
			HashMap<Integer,Integer> mapScore = indexOfWords.getMapIdWordScore();
			ArrayList<StringWithScore> result = ParserWithTags.parsingWithTags(inputFile,stopListPath);
			for (StringWithScore sws: result) {
				int score = sws.getScore();
				for (String word : sws.getCleanedContentList()) {
					if (! wordsInDB.containsKey(word)) {
						// on ajoute le mot dans la table word
						db.insertWordOrDoc("WORDS",word);
						int idWord = db.getID("WORDS", word);
						wordsInDB.put(word, idWord);
					}
					int idWord = wordsInDB.get(word);
					if (! mapFreq.containsKey(idWord)) {
						mapFreq.put(idWord, 1);
					}	
					else {
						mapFreq.put(idWord, mapFreq.get(idWord) + 1);
					}
					if (! mapScore.containsKey(idWord)) {
						mapScore.put(idWord, score);
					}	
					else {
						mapScore.put(idWord, mapScore.get(idWord) + score);
					}
				}
			}
			// insertion des mots dans la table indexation
			for(Entry<Integer, Integer> entry : mapFreq.entrySet()) {
				Integer key = entry.getKey();
				Integer value = entry.getValue();
				Integer score = mapScore.get(key);
				//System.out.println("key : "+key+" value : "+value+" , score:"+score);
				db.insertIndexationWithFrequencyAndScore(key, idDoc, value,score);
			}	
		}
		else {
			ArrayList<String> result = Parser.parsing(inputFile, stopListPath);
			// on ajoute tous les mots parses dans la table
			for (String word: result) {
				if (! wordsInDB.containsKey(word)) {
					// on ajoute le mot dans la table word
					db.insertWordOrDoc("WORDS",word);
					int idWord = db.getID("WORDS", word);
					wordsInDB.put(word, idWord);
				}
				int idWord = wordsInDB.get(word);
				if (! mapFreq.containsKey(idWord)) {
					mapFreq.put(idWord, 1);
				}	
				else {
					mapFreq.put(idWord, mapFreq.get(idWord) + 1);
				}
			}
			// insertion des mots dans la table indexation
			for(Entry<Integer, Integer> entry : mapFreq.entrySet()) {
				Integer key = entry.getKey();
				Integer value = entry.getValue();
				db.insertIndexationWithFrequency(key, idDoc, value);
			}	
		}
	}

	public void fillDatabaseWithAllFiles(boolean withTags) {
		ArrayList<String> listRep = new ArrayList<String>();
		listRep = FileManager.listerRepertoire(this.docsPath);
		String input = null;
		db.loadDB();	
		db.deleteTables();
		db.createTable();
		db.setAutoCommit(false);
		for (String file : listRep) {
			input = this.docsPath+file;
			System.out.println("FILE "+input);
			fillDatabaseWithFile(input,withTags);
		}
		db.commit();
		db.closeDB();
		System.out.println("fillDatabaseWithAllFiles ended");
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

	public HashMap<String,Integer> getWordsInDB() {
		return wordsInDB;
	}

	public void setWordsInDB(HashMap<String,Integer> wordsInDB) {
		this.wordsInDB = wordsInDB;
	}

	public static void main( String args[] ){
		FillDatabase fdb = new FillDatabase();
		fdb.fillDatabaseWithAllFiles(true);

	}

}
