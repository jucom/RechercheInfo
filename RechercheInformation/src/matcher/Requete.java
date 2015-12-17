package matcher;

import java.util.ArrayList;

public class Requete {
	private String name;
	private String req;
	private ArrayList<String> cleanReq;
	private int nbDocPertinent;
	private int nbDocFinded;
	private int rappel;
	private int precision;
	
	public Requete() {
		super();
		// TODO Auto-generated constructor stub
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
