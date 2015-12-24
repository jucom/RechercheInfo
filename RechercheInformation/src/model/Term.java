package model;

import java.util.HashMap;

public class Term {

	private String name;
	// idf = log(N/ni)
	// N : taille de la collection
	// ni : nb de documents contenant le terme ti
	private float idf;
	private HashMap<Integer,Float> mapIdDocTF;
	
	
	public Term(String name) {
		this.name = name;
		// TODO Auto-generated constructor stub
	}
	
	public void addIntoMapDocTF(int idDoc, float tf) {
		this.mapIdDocTF.put(idDoc, tf);
	}
	

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public float getIdf() {
		return idf;
	}


	public void setIdf(float idf) {
		this.idf = idf;
	}


	public HashMap<Integer,Float> getMapIdDocTF() {
		return mapIdDocTF;
	}


	public void setMapIdDocTF(HashMap<Integer,Float> mapIdDocTF) {
		this.mapIdDocTF = mapIdDocTF;
	}
}
