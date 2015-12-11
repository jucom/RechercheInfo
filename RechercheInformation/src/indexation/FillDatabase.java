package indexation;

import java.io.File;
import java.util.ArrayList;

public class FillDatabase {
	
	DatabaseMgmt db;
	String stopListPath ;
	
	public String getStopListPath() {
		return stopListPath;
	}

	public void setStopListPath(String stopListPath) {
		this.stopListPath = stopListPath;
	}

	public FillDatabase() {
		this.db = new DatabaseMgmt();
		this.stopListPath = "/home/jriviere/workspace/RechercheInfo/RechercheInformation/stopliste.txt";
	}
	
	public void fillDatabaseWithFile(String input, String charsetName) {
		File inputFile = new File(input);
		// on cree le document dans la table des documents 
		db.insertWordOrDoc("DOCS", inputFile.getName());
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
			int idDoc = db.getID("DOCS",inputFile.getName());
			db.insertIndexation(idWord,idDoc);
			
		}
		
	}
	
	public void fillDatabaseWithAllFiles() {
		db.initDB();
		db.closeDB();
	}

	public DatabaseMgmt getDb() {
		return db;
	}

	public void setDb(DatabaseMgmt db) {
		this.db = db;
	}
	
	public static void main( String args[] ){
		FillDatabase fdb = new FillDatabase();
		fdb.fillDatabaseWithAllFiles();
		
	}

}
