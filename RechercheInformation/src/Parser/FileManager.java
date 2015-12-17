package Parser;

import indexation.FillDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
	
	public static ArrayList<String> listerRepertoire(){
		return listerRepertoire("./CORPUS/CORPUS/");
	}

	public static ArrayList<String> readFileContent(final String fileName, final String encoding) throws IOException {
		// Recuperation du fichier
		File file = new File(fileName);

		// Creation d'un flux de lecture de fichier
		InputStream fileReader = new FileInputStream(file);

		// Creation d'un lecteur
		Reader utfReader = new InputStreamReader(fileReader, encoding);

		// Creation d'un lecteur plus intelligent. Il lira ligne par ligne au lieu de caractere par caractere
		BufferedReader input =  new BufferedReader(utfReader);

		// Liste qui contiendra le contenu du fichier
		ArrayList<String> list = new ArrayList<String>();

		String line = null;
		while ((line = input.readLine()) != null){
			list.add(line);
		}
		input.close();
		return list;
	}
	
}
