package matcher;

import java.util.ArrayList;

public class Performance {
	ArrayList<String> corpus;

	public Performance(ArrayList<String> corpus) {
		this.corpus = corpus;
	}

	//calcul le rappel du moteur de recherche en fonction de la reqûete et de notre corpus
	public String rappel(Request req, float indice){
		float rappel = (float) nbDocFindedPertinent(req, indice)/(float) req.getNbDocPertinent();
		return "rappel"+(int)indice+" : "+rappel+"\n";
	}


	//calcul la precision du moteur de recherche en fonction de la reqûete et de notre corpus
	public String precision(Request req, float indice){
		float precision = (float) nbDocFindedPertinent(req, indice)/ indice;
		return "precision"+(int)indice+" : "+precision+"\n";
	}

	
	public int nbDocFindedPertinent(Request req, float indice){
		//on calcule le nombre de docs pertinent parmis ceux trouvé
		int nbDocFindedPertinent = 0;	
		String doc;
		for (int i=0;(i<indice);i++){
			doc=req.getListDoc().get(i);
			if(isDocPertinant(doc,req)){
				nbDocFindedPertinent++;
			}
		}
		//System.out.println("nbDocFindedPertinent : " + nbDocFindedPertinent);
		return nbDocFindedPertinent;
	}
	
	//renvoit dit si un document est pertinant ou pas par rapport à une requête
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
