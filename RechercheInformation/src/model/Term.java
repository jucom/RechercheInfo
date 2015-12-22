package model;

public class Term {

	private String name;
	private float idf;
	private float tf;
	
	
	public Term(String name) {
		this.name = name;
		// TODO Auto-generated constructor stub
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


	public float getTf() {
		return tf;
	}


	public void setTf(float tf) {
		this.tf = tf;
	}
	
}
