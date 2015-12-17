package matcher;

public class Requete {
	private String name;
	private String req;
	private int nbDocPertinent;
	private int nbDocFinded;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNbDocFinded() {
		return nbDocFinded;
	}

	public void setNbDocFinded(int nbDocFinded) {
		this.nbDocFinded = nbDocFinded;
	}

	private int rappel;
	private int precision;
	
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

	public Requete() {
		super();
		// TODO Auto-generated constructor stub
	}
}
