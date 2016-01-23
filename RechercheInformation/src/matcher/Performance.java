package matcher;

import java.util.ArrayList;

import model.Request;
import model.RequestRelevance;

public class Performance {
	ArrayList<String> corpus;

	/**
	 * Constructor
	 * @param corpus
	 */
	public Performance(ArrayList<String> corpus) {
		this.corpus = corpus;
	}

	/**
	 * calcul le rappel du moteur de recherche en fonction de la reqûete et de notre corpus
	 * @param req
	 * @param indice
	 * @return
	 */
	public float rappel(Request req, float indice){
		float rappel = (float) nbDocFindedPertinent(req, indice)/(float) req.getNbDocPertinent();
		return rappel;
	}
	
	/** 
	 * @param req
	 * @param indice
	 * @return le rappel en fonction de l'indice du moteur de recherche en fonction 
	 * de la reqûete et de notre corpus
	 */
	public String rappelToString(Request req, float indice){
		float rappel = rappel(req,indice);
		return "rappel"+(int)indice+" : "+rappel+"\n";
	}
	
	/**
	 * @param req
	 * @param indice
	 * @return le rappel moyenne en fonction de l'indice de précision
	 */
	public String rappelMoyen(ArrayList<Request> req, float indice){
		float res = 0;
		for (Request r : req){
			res += rappel(r, indice);
		}
		res = (float) (res/req.size());
		return "rappel moyenne "+(int)indice+" : "+res+"\n";
	}
	

	/**
	 * calcul la precision en fonction de l'indice du moteur de recherche en fonction 
	 * de la reqûete et de notre corpus
	 * @param req
	 * @param indice
	 * @return la precision en fonction de l'indice de précision
	 * du moteur de recherche en fonction de la reqûete et de notre corpus
	 */
	public float precision(Request req, float indice){
		float precision = (float) nbDocFindedPertinent(req, indice)/ indice;
		return precision;
	}
	
	/**
	 * @param req
	 * @param indice
	 * @return un String de la precision en fonction de l'indice
	 * du moteur de recherche en fonction de la reqûete et de notre corpus
	 */
	public String precisionToString(Request req, float indice){
		return "precision"+(int)indice+" : "+precision(req,indice)+"\n";
	}

	/**
	 * @param req
	 * @param indice
	 * @return la précision moyenne en fonction de l'indice de précision
	 */
	public String precisionMoyenne(ArrayList<Request> req, float indice){
		float res = 0;
		for (Request r : req){
			res += precision(r, indice);
		}
		res = (float) (res/req.size());
		return "precision moyenne "+(int)indice+" : "+res+"\n";
	}

	
	/**
	 * @param req
	 * @param indice
	 * @return nombre de document pertinent trouves
	 */
	public int nbDocFindedPertinent(Request req, float indice){
		//on calcule le nombre de docs pertinent parmis ceux trouvé
		int nbDocFindedPertinent = 0;	
		ArrayList<String> docs = new ArrayList<String>();
		String doc;
		docs=req.getListDoc();
		for (int i=0;(i<indice && i < docs.size());i++){
			//System.out.println("ok");
			doc = docs.get(i);
			if(isDocPertinant(doc,req)){
				nbDocFindedPertinent++;
			}
		}
		//System.out.println("nbDocFindedPertinent : " + nbDocFindedPertinent);
		return nbDocFindedPertinent;
	}
	
	/**
	 * @param doc
	 * @param r
	 * @return si un document est pertinant ou pas par rapport a une requete ou non
	 */
	public boolean isDocPertinant(String doc, Request r){
		boolean bool = false;
		boolean finded = false;
		int i;
		ArrayList<RequestRelevance> list = r.getListRelevanceDocs(); 
		for (i=0;(i<list.size()&&!finded);i++){
			if (list.get(i).getDoc().equals(doc)){
				finded = true;
				if (list.get(i).getRelevance() != 0){
					bool = true;
				}
			}
		}

		return bool;
	}






}
