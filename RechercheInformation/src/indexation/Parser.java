package indexation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class Parser {
	
	public static String parseDocument(File input, String charsetName) {
		
		Document doc = null;
		try {
			doc = Jsoup.parse(input,charsetName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	    StringBuilder builder = new StringBuilder();

	    for ( Element element : doc.getAllElements() )
	    {
	        for ( TextNode textNode : element.textNodes() )
	        {
	            final String text = textNode.text();
	            //System.out.println(text);
	            if (!(text.equals(" ")))
	            	builder.append( text+" " );
	        }
	    }
	    return builder.toString();		
	}
	
	// remove &nbsp 
	public static String clean(String s) {
		String cleaned = s.replace("\u00a0","");
		return cleaned;
	}
	
	public static String removeNumbers(String s) {
		String cleaned = s.replaceAll("\\p{Digit}","");
		return cleaned;
	}
	
	public static String[] tokenize(String s) {
		//StringTokenizer multiTokenizer = new StringTokenizer(s, " !#$%&'()*â€“+,-./:;<=>?@[]^_`{|}\t\n\r~");
		String[] tokens = s.split("[\\s\\p{Punct}]+");
		return tokens;
	}
	
	public static ArrayList<String> troncate(String[] tokens) {
		ArrayList<String> tokenList = new ArrayList<String>();
		String s2 = null;
		for (String token : tokens)	{	
			token = token.replace(" ", "");
			token = token.replace("«", "");
			token = token.replace("»", "");
			token = token.replace("\\P{Graph}","");
			if (token.length() > 2) {
			    if (token.length() > 7) {
			    	s2 = token.substring(0,7);
			    }
			    else {
			    	s2 = token;
			    }
			    tokenList.add(s2.toLowerCase());
			}
		 }
		return tokenList;		
	}
	
	public static void printStringArrayList(ArrayList<String> list) {
		for (String e : list) {
			System.out.println(e);
		}
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
	
	public static ArrayList<String> deleteTokensFromStopList(ArrayList<String> list, ArrayList<String> stoplist) {
		boolean found = false;
		
		Iterator<String> iterator = list.iterator();
		while (iterator.hasNext()) {
			// On récupère l'élément courant
			String word = iterator.next(); 
			found = false;
			int i = 0;
			while (!found && i < stoplist.size()) {
				if (word.equals(stoplist.get(i))) {
					found = true;
					// On supprime l'élément courant
					iterator.remove();
				}
				else {
					i++;
				}				
			}
		}		
		return list;
	}

	public static void main( String args[] ){
		//File input = new File("/home/jriviere/Bureau/RI/CORPUS/CORPUS/D1.html");
		File input = new File("C:/Users/User/Documents/INSA/5IL/RerchercheInformation/CORPUS/CORPUS/D1.html");
		ArrayList<String> list = new ArrayList<String>();
		String s = parseDocument(input,"UTF-8");
		s = removeNumbers(s);
		s = clean(s);
		String[] tokens = tokenize(s);
		list = troncate(tokens);
		//printStringArrayList(list);
		try {
			ArrayList<String> stopList = readFileContent("C:/Users/User/Documents/GitHub/RechercheInfo/RechercheInformation/stopliste.txt","iso-8859-1");
			//printStringArrayList(stopList);
			list = deleteTokensFromStopList(list,stopList);
			//printStringArrayList(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
