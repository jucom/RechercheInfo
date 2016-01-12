package Parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	
	public static ArrayList<String> deleteTokensFromStopList(ArrayList<String> list, ArrayList<String> stoplist) {
		boolean found = false;
		
		Iterator<String> iterator = list.iterator();
		while (iterator.hasNext()) {
			// On recupere l'element courant
			String word = iterator.next(); 
			found = false;
			int i = 0;
			while (!found && i < stoplist.size()) {
				if (word.equals(stoplist.get(i))) {
					found = true;
					// On supprime l'element courant
					iterator.remove();
				}
				else {
					i++;
				}				
			}
		}		
		return list;
	}
	
	public static String initParsing(File filePath) {
		String[] isoFiles = {"D110.html","D125.html","D77.html","D93.html"};
		String[] unknown8bitFiles = {"D117.html","D118.html","D12.html","D44.html","D46.html","D49.html","D75.html","D89.html","D90.html"};
		String s = null;
		if(Arrays.asList(isoFiles).contains(filePath.getName()) || Arrays.asList(unknown8bitFiles).contains(filePath.getName())) {
			s = parseDocument(filePath,"iso-8859-1");
		}
		else {
			s = parseDocument(filePath,"utf-8");
		}
		return s;
	}
	
	public static ArrayList<String> parsing(File filePath, String stopListPath) {
		ArrayList<String> list = new ArrayList<String>();
		String s = initParsing(filePath);
		list = Cleaner.cleanString(s);
		try {
			ArrayList<String> stopList = FileManager.readFileContent(stopListPath,"iso-8859-1");
			list = deleteTokensFromStopList(list,stopList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	/*
	public static void testParser() {
		String input = "./CORPUS/CORPUS/D88.html";
		String stopListPath = "./stopliste.txt";
		File inputFile = new File(input);
		ArrayList<String> result = new ArrayList<String>();
		result = parsing(inputFile, stopListPath);
		Cleaner.printStringArrayList(result);
	}

	public static void main( String args[] ){
		testParser();
	}
	*/
}
