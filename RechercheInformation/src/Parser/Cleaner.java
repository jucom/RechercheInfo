package Parser;

import java.text.Normalizer;
import java.util.ArrayList;

public class Cleaner {

	// remove &nbsp 
	public static String clean(String s) {
		String cleaned = s.replace("\u00a0","");
		return cleaned;
	}

	// remove numbers
	public static String removeNumbers(String s) {
		String cleaned = s.replaceAll("\\p{Digit}","");
		return cleaned;
	}
	
	public static String normalize(String input) {
		return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
	}
	
	// split words
	public static String[] tokenize(String s) {
		//StringTokenizer multiTokenizer = new StringTokenizer(s, " !#$%&'()*–+,-./:;<=>?@[]^_`{|}\t\n\r~");
		String[] tokens = s.split("[\\s\\p{Punct}]+");
		return tokens;
	}

	// troncate (7 chars) and clean some special chars
	public static ArrayList<String> troncate(String[] tokens) {
		ArrayList<String> tokenList = new ArrayList<String>();
		String s2 = null;
		for (String token : tokens)	{	
			token = token.replace(" ", "");
			token = token.replace("»", "");
			token = token.replace("«", "");
			token = token.replace("­‐", "");
			token = token.replace("’", "");
			token = token.replace("©", "");
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
	
	public static ArrayList<String> cleanString(String s) {
		ArrayList<String> list = new ArrayList<String>();
		s = normalize(s);
		s = removeNumbers(s);
		s = clean(s);
		String[] tokens = tokenize(s);
		list = troncate(tokens);
		return list;
	}

	public static void printStringArrayList(ArrayList<String> list) {
		for (String e : list) {
			System.out.println(e);
		}
	}



}
