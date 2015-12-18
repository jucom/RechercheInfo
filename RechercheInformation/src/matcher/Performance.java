package matcher;

import java.util.ArrayList;

public class Performance {
	ArrayList<String> corpus;
	
	public Performance(ArrayList<String> corpus) {
		this.corpus = corpus;
	}

	//calcul le rappel du moteur de recherche en fonction de la reqûete et de notre corpus
	public void rappel(Request req){
		int rappel = req.getNbDocPertinent()/corpus.size();
		req.setRappel(rappel);
	}
	
	//calcul la precision du moteur de recherche en fonction de la reqûete et de notre corpus
	public void precision(Request req){
		int precision = req.getNbDocPertinent()/req.getNbDocFinded();
		req.setPrecision(precision);
	}

	
}
