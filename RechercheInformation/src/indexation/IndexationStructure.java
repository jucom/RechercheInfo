package indexation;

import java.util.HashMap;

public class IndexationStructure {
	
	// map with key = idword, value = nb occ of idword 
	private HashMap<Integer,Integer> mapIdWordFrequency ;
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
	}
	
	

}
