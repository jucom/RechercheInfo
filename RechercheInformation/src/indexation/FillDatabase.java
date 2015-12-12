package indexation;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class FillDatabase {
	
	DatabaseMgmt db;
	String stopListPath ;
	String docsPath;
	
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

	public FillDatabase() {
		this.db = new DatabaseMgmt();
		//this.stopListPath = "/home/jriviere/workspace/RechercheInfo/RechercheInformation/stopliste.txt";
		//this.docsPath = "/home/jriviere/Bureau/RI/CORPUS/CORPUS/";
		this.docsPath = "C:/Users/User/Documents/INSA/5IL/RerchercheInformation/CORPUS/CORPUS/";
		this.stopListPath = "C:/Users/User/Documents/GitHub/RechercheInfo/RechercheInformation/stopliste.txt";
		
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

	public ArrayList<String> listerRepertoire(String rep){
		File repertoire = new File(rep);
		String [] listefichiers = null;
		ArrayList<String> list = null;
		if (repertoire.isDirectory()) {
			listefichiers=repertoire.list();
			list = new ArrayList<String>(Arrays.asList(listefichiers));
			for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
			    String doc = iterator.next();
			    if (doc.endsWith("#")) {
			        iterator.remove();
			    }
			}
			/*
			for (String s : list) {
				System.out.println(s);
			}*/
		}
		return list;
	}
	
	public void fillDatabaseWithAllFiles() {
		ArrayList<String> listRep = new ArrayList<String>();
		listRep = listerRepertoire(this.docsPath);
		String input = null;
		db.initDB();		
		for (String file : listRep) {
			input = this.docsPath+file;
			System.out.println("*********");
			System.out.println("FILE "+input);
			fillDatabaseWithFile(input);
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
	
	public static void main( String args[] ){
		FillDatabase fdb = new FillDatabase();
		fdb.fillDatabaseWithAllFiles();
		
	}

}
