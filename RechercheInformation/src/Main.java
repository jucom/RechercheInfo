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
import model.Term;
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
		//main(1, 0);
		//main(2, 0);
		//main(3, 0);
		//main(4, 0);
		main(5, 0);
	}

	/**
	 * Main
	 * @param version
	 * @param reformulation (seulement à partir de la V3)
	 * 		0 si pas de reformulation
	 * 		1 ou 2 Sinon
	 */
	public static void main(int version, int reformulation){
		System.out.println("Initialisation des tables sql");
		/* comment the following lines if you have already imported the corpus*/
		
		if (version == 5) {
			fillDb.fillDatabaseWithAllFiles(true);
		}
		else {
			fillDb.fillDatabaseWithAllFiles(false);
		}

		db.loadDB();
		//On charge le Corpus
		docs = FileManager.listerRepertoire();

		//On charge les requetes depuis les fichiers txt
		reqs = Request.createListReq(Cst.reqsPath);

		if (reformulation != 0){
			//On charge les requetes depuis le fichier html
			File inputFile = new File(Cst.requestFile);
			Map<String, Collection<String>> map = ParserKeyWords.parseKeyWordsDocument(inputFile);	
			for (Map.Entry<String, Collection<String>> result : map.entrySet()) {
				String reqName = result.getKey();
				for (Request r : reqs) {
					if (r.getName().equals(reqName)) {
						System.out.println("Trouve la requete : " + r.getName());
						r.setKeyWords((ArrayList<String>) result.getValue());
						Reformulation.reformulation(r);
						ArrayList<String> strAux = Cleaner.cleanReformulationString(r.getReformulation());
						for (String s : strAux){
							if (r.isInReqTerm(s)){
								Term t  = r.getTermInReqTerm(s);
								if (reformulation == 2){
									t.setIdf((float)100.0);
								} else {
									t.setIdf((float)0.0);
								}
							} else {
								r.addReqTerm(s);
							}
						}

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
			System.out.println(p.rappelToString(r, 5)+p.rappelToString(r, 10)+p.rappelToString(r, 25)+p.precisionToString(r, 5)+p.precisionToString(r, 10)+p.precisionToString(r, 25));
		}
		System.out.println("#############################");
		System.out.println(" Moyenne rappel et précision ");
		System.out.println("#############################");
		System.out.println(p.rappelMoyen(reqs, 5)+p.rappelMoyen(reqs, 10)+p.rappelMoyen(reqs, 25)+p.precisionMoyenne(reqs, 5)+p.precisionMoyenne(reqs, 10)+p.precisionMoyenne(reqs, 25));
		db.closeDB();
	}


}
