package Parser;

public class StringWithScore {
	
	// score du string : depend de la balise dans laquelle le string se trouve
	public int score;
	// contenu du string
	public String content;
	
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
	
	

}
