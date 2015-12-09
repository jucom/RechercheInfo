package indexation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

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
		//StringTokenizer multiTokenizer = new StringTokenizer(s, " !#$%&'()*–+,-./:;<=>?@[]^_`{|}\t\n\r~");
		String[] tokens = s.split("[\\s\\p{Punct}]+");
		return tokens;
	}
	
	public static ArrayList<String> troncate(String[] tokens) {
		ArrayList<String> tokenList = new ArrayList<String>();
		String s2 = null;
		for (String token : tokens)	{	
			token = token.replace(" ", "");
			token = token.replace("\\P{Graph}","");
			if (token.length() > 2) {
			    if (token.length() > 7) {
			    	s2 = token.substring(0,7);
			    }
			    else {
			    	s2 = token;
			    }
			    tokenList.add(s2);
			}
		 }
		return tokenList;		
	}
	
	public static void printStringArrayList(ArrayList<String> list) {
		for (String e : list) {
			System.out.println(e);
		}
	}
	

	public static void main( String args[] ){
		File input = new File("/home/jriviere/Bureau/RI/CORPUS/CORPUS/D1.html");
		ArrayList<String> list = new ArrayList<String>();
		String s = parseDocument(input,"UTF-8");
		//String cleaned = clean(s);
		String cleaned = removeNumbers(s);
		String[] tokens = tokenize(cleaned);
		list = troncate(tokens);
		printStringArrayList(list);
	}
	
	

}
