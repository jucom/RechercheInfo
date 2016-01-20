import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import Parser.Cleaner;
import Parser.FileManager;
import parserKeyWords.*;
import reformulation.Reformulation;

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
		main(1, true);
		//main(2, false);
		//main(3, false);
		//main(4, false);
		//main(5, false);
	}

	public static void main(int version, boolean reformulation){
		System.out.println("Initialisation des tables sql");
		/* comment the following lines if you have already imported the corpus*/
		
		if (version == 5) {
			fillDb.fillDatabaseWithAllFiles(true);
		}
		else {
			//fillDb.fillDatabaseWithAllFiles(false);
		}
		
		db.loadDB();
		//On charge le Corpus
		docs = FileManager.listerRepertoire();
		
		//On charge les requetes depuis les fichiers txt
		reqs = Request.createListReq(Cst.reqsPath);
		
		if (reformulation){
			Reformulation reformu = new Reformulation();
			//On charge les requetes depuis le fichier html
			File inputFile = new File(Cst.requestFile);
			Map<String, Collection<String>> map = ParserKeyWords.parseKeyWordsDocument(inputFile);	
			for (Map.Entry<String, Collection<String>> result : map.entrySet()) {
				String reqName = result.getKey();
				for (Request r : reqs) {
					if (r.getName().equals(reqName)) {
						r.setKeyWords((ArrayList<String>) result.getValue());
						reformu.reformulation(r);
						r.setCleanReq(Cleaner.cleanReformulationString(r.getReformulation()));
						break;
					}
				}
			}
			
			
		}
		
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


}
