package matcher;

import java.io.IOException;
import java.util.ArrayList;

import Parser.Cleaner;
import Parser.FileManager;

public class Request {
	private String name;
	private String req;
	private ArrayList<String> cleanReq;
	private int nbDocPertinent;
	private int nbDocFinded;
	private int rappel;
	private int precision;
	private Object[] listDoc;
	
	
	public Request(String name) {
		this.name = name;
		this.nbDocFinded = 0;
		this.nbDocPertinent = 0;
	}
	
	public static ArrayList<Request> createListReq(String path) {
		ArrayList<Request> reqs = new ArrayList<Request>();
		ArrayList<String> reqDoc = FileManager.listerRepertoire(path);
		ArrayList<String> list = new ArrayList<String>();
		for (String name : reqDoc){
			Request r = new Request(name);
			try {
				list = FileManager.readFileContent(path+name, "utf-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (list != null) {
				r.setReq(list.get(0));
				r.setCleanReq(Cleaner.cleanString(r.getReq()));
				System.out.println(r.getReq());
				Cleaner.printStringArrayList(r.getCleanReq());
			}
			//nbDOcpertinentReq
			
			reqs.add(r);
		}
		return reqs;
	}
	
	public static void testRequests() {
		createListReq("./qrels/");
	}
	
	public static void main( String args[] ){
		testRequests();
	}
	
	
	public Object[] getListDoc() {
		return listDoc;
	}

	public void setListDoc(Object[] listDoc) {
		this.listDoc = listDoc;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getCleanReq() {
		return cleanReq;
	}

	public void setCleanReq(ArrayList<String> cleanReq) {
		this.cleanReq = cleanReq;
	}

	public int getNbDocFinded() {
		return nbDocFinded;
	}

	public void setNbDocFinded(int nbDocFinded) {
		this.nbDocFinded = nbDocFinded;
	}

	public String getReq() {
		return req;
	}

	public void setReq(String req) {
		this.req = req;
	}

	public int getNbDocPertinent() {
		return nbDocPertinent;
	}

	public void setNbDocPertinent(int nbDocPertinent) {
		this.nbDocPertinent = nbDocPertinent;
	}

	public int getRappel() {
		return rappel;
	}

	public void setRappel(int rappel) {
		this.rappel = rappel;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}


}
