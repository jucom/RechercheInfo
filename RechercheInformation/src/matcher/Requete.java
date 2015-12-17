package matcher;

import java.util.ArrayList;

import Parser.FileManager;

public class Requete {
	private String name;
	private String req;
	private ArrayList<String> cleanReq;
	private int nbDocPertinent;
	private int nbDocFinded;
	private int rappel;
	private int precision;
	private Object[] listDoc;
	
	
	public Requete(String name) {
		this.name = name;
		this.nbDocFinded = 0;
		this.nbDocPertinent = 0;
	}
	
	public static ArrayList<Requete> createListReq(String path) {
		ArrayList<Requete> reqs = new ArrayList<Requete>();
		ArrayList<String> reqDoc = FileManager.listerRepertoire(path);
		for (String name : reqDoc){
			Requete r = new Requete(name);
			//Get Req and clean it
			//nbDOcpertinentReq
			
			reqs.add(r);
		}
		return reqs;
	}
	
	
	public Object[] getListDoc() {
		return listDoc;
	}

	public void setListDoc(Object[] listDoc) {
		this.listDoc = listDoc;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getCleanReq() {
		return cleanReq;
	}

	public void setCleanReq(ArrayList<String> cleanReq) {
		this.cleanReq = cleanReq;
	}

	public int getNbDocFinded() {
		return nbDocFinded;
	}

	public void setNbDocFinded(int nbDocFinded) {
		this.nbDocFinded = nbDocFinded;
	}

	public String getReq() {
		return req;
	}

	public void setReq(String req) {
		this.req = req;
	}

	public int getNbDocPertinent() {
		return nbDocPertinent;
	}

	public void setNbDocPertinent(int nbDocPertinent) {
		this.nbDocPertinent = nbDocPertinent;
	}

	public int getRappel() {
		return rappel;
	}

	public void setRappel(int rappel) {
		this.rappel = rappel;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}


}
