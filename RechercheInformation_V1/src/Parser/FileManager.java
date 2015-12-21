package Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

public class FileManager {

	
	public static ArrayList<String> listerRepertoire(String path){
		File repertoire = new File(path);
		File [] listefichiers = null;
		ArrayList<String> list = new ArrayList<String>();
		if (repertoire.isDirectory()) {
			listefichiers=repertoire.listFiles();
			for (File f:listefichiers) {
				if (!f.isDirectory()) {
					list.add(f.getName());
				}
			}
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
