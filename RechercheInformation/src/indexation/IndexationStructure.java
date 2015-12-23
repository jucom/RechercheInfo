package indexation;

import java.util.HashMap;

public class IndexationStructure {
	
	// map with key = idword, value = nb occ of idword 
	private HashMap<Integer,Integer> mapIdWordFrequency ;
	// map with key = idword, value = score
	private HashMap<Integer,Integer> mapIdWordScore ;
	private int idDoc ;
	
	public HashMap<Integer, Integer> getMapIdWordFrequency() {
		return mapIdWordFrequency;
	}

	public void setMapIdWordFrequency(HashMap<Integer, Integer> mapIdWordFrequency) {
		this.mapIdWordFrequency = mapIdWordFrequency;
	}

	public int getIdDoc() {
		return idDoc;
	}

	public void setIdDoc(int idDoc) {
		this.idDoc = idDoc;
	}

	public IndexationStructure(int idDoc) {
		this.idDoc = idDoc;
		this.mapIdWordFrequency = new HashMap<Integer,Integer>();
		this.mapIdWordScore = new HashMap<Integer,Integer>();
	}

	public HashMap<Integer,Integer> getMapIdWordScore() {
		return mapIdWordScore;
	}

	public void setMapIdWordScore(HashMap<Integer,Integer> mapIdWordScore) {
		this.mapIdWordScore = mapIdWordScore;
	}
	
	

}
