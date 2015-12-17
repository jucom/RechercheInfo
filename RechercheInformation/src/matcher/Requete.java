package matcher;

public class Requete {
	private String req;
	private int nbDocPertinent;
	private int rappel;
	private int pertinence;
	
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

	public int getPertinence() {
		return pertinence;
	}

	public void setPertinence(int pertinence) {
		this.pertinence = pertinence;
	}

	public Requete() {
		super();
		// TODO Auto-generated constructor stub
	}
}
