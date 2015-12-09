import indexation.DatabaseMgmt;




public class Main {
	static DatabaseMgmt db = new DatabaseMgmt();
	static int idWord = 1 ;
	static int idDoc = 1 ; 
	
	  public static void main( String args[] ){
		  db.loadDB();
		  db.createTable();
		  db.insertWordOrDoc("WORDS", "coucou");
		  idWord++;
		  db.insertWordOrDoc("DOCS", "D2");
		  idDoc++;
		  System.out.println("insertIndexation(1, 1)");
		  db.insertIndexation(1, 1);
		  System.out.println("insertIndexation(1, 2)");
		  db.insertIndexation(1, 2);
		  System.out.println("insertIndexation(2, 2)");
		  db.insertIndexation(2, 2);
		  db.closeDB();
	  }
}
