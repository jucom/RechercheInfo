package Parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import matcher.Requete;

public class FileManager {
	
	
	
	public static ArrayList<String> listerRepertoire(String path){
		File repertoire = new File(path);
		String [] listefichiers = null;
		ArrayList<String> list = null;
		if (repertoire.isDirectory()) {
			listefichiers=repertoire.list();
			list = new ArrayList<String>(Arrays.asList(listefichiers));
			for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
			    String doc = iterator.next();
			    if (doc.endsWith("#")) {
			        iterator.remove();
			    }
			}
			/*
			for (String s : list) {
				System.out.println(s);
			}*/
		}
		return list;
	}
	
	
}
