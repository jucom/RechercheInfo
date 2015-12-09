package matcher;

import indexation.DatabaseMgmt;

import java.sql.*;

public class Matcher {
	DatabaseMgmt db;

	public Matcher() {
		this.db = new DatabaseMgmt();
		// TODO Auto-generated constructor stub
	}
	
	public void setDatabaseMgmt(DatabaseMgmt db){
		this.db = db;
	}

	//cree un tableau de string avec les mots pertinents
	public void CleanRequest(String req){
		
	}
	
	//créé un tableau contenant la liste des documents qui contiennent un  des termes
	//et son nombre d'occurrence
	public void MatcherDocWords(){
		
	}
	

	
}
