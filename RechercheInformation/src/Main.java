import indexation.DatabaseMgmt;




public class Main {
	static DatabaseMgmt db = new DatabaseMgmt();
	static int idWord = 1 ;
	static int idDoc = 1 ; 
	
	  public static void main( String args[] ){
		  db.loadDB();
		  //db.createTable();
		  /*
		  db.insertWordOrDoc("WORDS", idWord, "coucou");
		  idWord++;
		  db.insertWordOrDoc("DOCS", idDoc, "D2");
		  idDoc++;
		  */
		  db.insertIndexation(1, 1);
		  db.closeDB();
	  }
}
