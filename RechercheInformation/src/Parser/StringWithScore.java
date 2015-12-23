package Parser;

import java.util.ArrayList;


public class StringWithScore {
	
	// score du string : depend de la balise dans laquelle le string se trouve
	private int score;
	// contenu du string
	private String content;
	// contenu nettoye et tokenise
	private ArrayList<String> cleanedContentList;
	
	public StringWithScore() {
		this.score = 1;
		this.content ="";
	}
	
	public StringWithScore(int score, String content) {
		this.score = score;
		this.content = content;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ArrayList<String> getCleanedContentList() {
		return cleanedContentList;
	}

	public void setCleanedContentList(ArrayList<String> cleanedContentList) {
		this.cleanedContentList = cleanedContentList;
	}

	
	

}
