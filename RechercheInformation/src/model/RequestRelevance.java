package model;

public class RequestRelevance {
	
	private String doc;
	private double relevance;
	
	public RequestRelevance(String doc, double d) {
		this.doc = doc;
		this.relevance = d;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public double getRelevance() {
		return relevance;
	}

	public void setRelevance(double relevance) {
		this.relevance = relevance;
	}

}
