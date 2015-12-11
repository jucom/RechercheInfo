import matcher.Matcher;
import indexation.DatabaseMgmt;




public class Main {
	static DatabaseMgmt db = new DatabaseMgmt();
	static Matcher matcher = new Matcher();
	static int idWord = 1 ;
	static int idDoc = 1 ; 
	
	  public static void main( String args[] ){
		  db.loadDB();
		  db.createTable();
		  db.insertWordOrDoc("WORDS", "coucou");
		  db.insertWordOrDoc("DOCS", "D2");
		  System.out.println("insertIndexation(1, 1)");
		  db.insertIndexation(1, 1);
		  System.out.println("insertIndexation(1, 2)");
		  db.insertIndexation(1, 2);
		  System.out.println("insertIndexation(2, 2)");
		  db.insertIndexation(2, 2);
		  System.out.println("***");
		  System.out.println(db.wordExists("coucou"));
		  System.out.println(db.wordExists("pizza"));
		  System.out.println(db.getID("WORDS", "coucou"));
		  db.closeDB();
		  
		  matcher.setDatabaseMgmt(db);
		  matcherTest();
		  db.closeDB();
	  }
	  
	  public static void matcherTest(){
		  matcher.CleanRequest("Le compagnon de sa soeur est le mari de sa femme");
	  }
}
