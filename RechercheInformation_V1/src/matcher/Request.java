package matcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import constante.Cst;

import Parser.Cleaner;
import Parser.FileManager;

public class Request {
	private String name;
	private String req;
	private ArrayList<String> cleanReq;
	private int nbDocPertinent;
	private int nbDocFinded;
	

	private int nbDocsInQrels;
	private ArrayList<String> listDoc;
	private ArrayList<RequestRelevance> listRelevanceDocs;
	
	
	public Request(String name) {
		this.name = name;
		this.nbDocFinded = 0;
		this.nbDocPertinent = 0;
		this.nbDocsInQrels = 0 ;
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
				
				ArrayList<String> l = Cleaner.cleanString(r.getReq());
				ArrayList<String> stopList;
				try {
					stopList = FileManager.readFileContent(Cst.stopListPath,"iso-8859-1");
					r.setCleanReq(Parser.Parser.deleteTokensFromStopList(l, stopList));
					// /!\ nbDocInQrels set in getPertinenceOfReq
					r.setListRelevanceDocs(getPertinenceOfReq(path+"qrels/"+name,r));
					/*
					for (RequestRelevance r2 : r.getListRelevanceDocs()) {
						System.out.println(r2.getDoc()+" , "+r2.getRelevance());
					}
					*/
					r.setNbDocPertinent(r.getListRelevanceDocs().size());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			reqs.add(r);
		}
		return reqs;
	}
	
	public static ArrayList<RequestRelevance> getPertinenceOfReq(String path, Request r) {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<RequestRelevance> listRelevanceDocs = new ArrayList<RequestRelevance>();
		try {
			// list represente le contenu du document 
			list = FileManager.readFileContent(path, "utf-8");
			r.setNbDocsInQrels(list.size());
			// pour chaque ligne du document
			for (String s: list) {
				String[] tokens = Cleaner.tokenize(s);
				/* tokens[0] = Di 
				 * tokens[1] = html
				 * tokens[2] = score
				 * tokens[3] = score decimal (optional)
				 */
				// robustesse : normalement tokens[i] avec i < 3 n'est jamais null
				if (tokens.length >= 3) {
					if (Integer.parseInt(tokens[2]) == 1) {
						RequestRelevance reqRel = new RequestRelevance(tokens[0]+"."+tokens[1], 1);
						listRelevanceDocs.add(reqRel);
					}
					// if we have a decimal part
					else if (tokens.length > 3) {
						if (Integer.parseInt(tokens[3]) == 5) {
							RequestRelevance reqRel = new RequestRelevance(tokens[0]+"."+tokens[1], 0.5);
							listRelevanceDocs.add(reqRel);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listRelevanceDocs;
	}
	
	/*public static void testRequests() {
		ArrayList<Request> list = createListReq("./qrels/");
		for (Request r : list) {
			System.out.println(r.getReq());
			Cleaner.printStringArrayList(r.getCleanReq());
			System.out.println("nb docs pertinents :"+r.getNbDocPertinent());
			System.out.println("nb docs trouves dans qrels :"+r.getNbDocsInQrels());
		}
	}
	
	public static void testPertinence() {
		ArrayList<RequestRelevance> l = getPertinenceOfReq("./qrels/qrels/qrelQ1.txt",new Request("test"));
		for (RequestRelevance r : l) {
			System.out.println(r.getDoc()+" , "+r.getRelevance());
		}
	}
		
	
	public static void main( String args[] ){
		testRequests();
	}*/
	
	

	public ArrayList<String> getListDoc() {
		return listDoc;
	}

	public void setListDoc(ArrayList<String> listDoc) {
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

	public ArrayList<RequestRelevance> getListRelevanceDocs() {
		return listRelevanceDocs;
	}

	public void setListRelevanceDocs(ArrayList<RequestRelevance> listRelevanceDocs) {
		this.listRelevanceDocs = listRelevanceDocs;
	}

	public int getNbDocsInQrels() {
		return nbDocsInQrels;
	}

	public void setNbDocsInQrels(int nbDocsInQrels) {
		this.nbDocsInQrels = nbDocsInQrels;
	}
	
	@Override
	public String toString() {
		return "Request [name=" + name + "\n req=" + req + "\n cleanReq="
				+ cleanReq + "\n nbDocPertinent=" + nbDocPertinent
				+ "\n nbDocFinded=" + nbDocFinded + "\n nbDocsInQrels="
				+ nbDocsInQrels +  "]";
	}
	


}
