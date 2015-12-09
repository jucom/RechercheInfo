package indexation;

import java.sql.*;

//http://www.tutorialspoint.com/sqlite/sqlite_java.htm 

public class DatabaseMgmt {

	Connection c = null;

	public void loadDB(){	
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:mr.db");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}

	public void createTable(){	
		// todo : erreur si table deja cree
		Statement stmt = null;
		try {	
			stmt = c.createStatement();
			String sql = "CREATE TABLE WORDS" +
					"(ID INTEGER PRIMARY KEY NOT NULL," +
					" NAME         VARCHAR(10)  NOT NULL)"; 
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE DOCS" +
					"(ID INTEGER PRIMARY KEY NOT NULL," +
					" NAME         VARCHAR(10)  NOT NULL)"; 
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE INDEXTABLE" +
					"(IDWORD INTEGER NOT NULL," +
					"IDDOC INTEGER NOT NULL," +
					"OCC INTEGER NOT NULL," +
					"SCORE INTEGER,"+
					"PRIMARY KEY (IDWORD,IDDOC),"+
					"FOREIGN KEY (IDWORD) REFERENCES WORDS(ID),"+
					"FOREIGN KEY (IDDOC) REFERENCES DOCS(ID))";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Table created successfully");
	}

	public void insertWordOrDoc(String tableName, int id, String value){
		Statement stmt = null;
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "";
			//parsage des docs récupération infos
			sql = "INSERT INTO "+tableName+" (ID,NAME) " + "VALUES ("+id+","+'"'+value+'"'+");";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Records created successfully");
	}

	public void insertIndexation(int idWord, int idDoc){
		Statement stmt = null;
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			// si le couple n'existe pas encore dans la table : l'ajouter
			String sql = "";
			ResultSet rs = stmt.executeQuery( "SELECT COUNT(*) FROM INDEXTABLE WHERE IDDOC="+idDoc+" and IDWORD="+idWord+";" );
			if (rs.getString("COUNT(*)").equals("0")) {
				sql = "INSERT INTO INDEXTABLE (IDWORD,IDDOC,OCC) " + "VALUES ("+idWord+","+idWord+","+"1);";
				System.out.println(sql);
				stmt.executeUpdate(sql);
			// sinon, occ++
			} else {
				ResultSet rsOcc = stmt.executeQuery( "SELECT OCC FROM INDEXTABLE WHERE IDDOC="+idDoc+" and IDWORD="+idWord+";" );
				int nbOcc = Integer.parseInt(rs.getString("OCC"));
				nbOcc++;
				sql = "UPDATE INDEXTABLE set OCC = "+nbOcc+" WHERE IDDOC="+idDoc+" and IDWORD="+idWord+";";
			    stmt.executeUpdate(sql);
			}
			stmt.close();
		    rs.close();
			c.commit();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Records created successfully");
	}


	public void closeDB(){	
		try {
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Database closed successfully");
	}


}
