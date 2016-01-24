package Parser;

import java.io.IOException;
import java.text.Normalizer;
import model.Cst;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Cleaner {

	/**
	 * remove &nbsp 
	 * @param s
	 * @return
	 */
	public static String clean(String s) {
		String cleaned = s.replace("\u00a0","");
		return cleaned;
	}

	/**
	 * remove numbers
	 * @param s
	 * @return
	 */
	public static String removeNumbers(String s) {
		String cleaned = s.replaceAll("\\p{Digit}","");
		return cleaned;
	}
	
	/**
	 * string normalization : removes accents etc
	 * @param input
	 * @return String normalized
	 */
	public static String normalize(String input) {
		return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
	}
	
	/**
	 * split words
	 * @param s
	 * @return
	 */
	public static String[] tokenize(String s) {
		//StringTokenizer multiTokenizer = new StringTokenizer(s, " !#$%&'()*–+,-./:;<=>?@[]^_`{|}\t\n\r~");
		String[] tokens = s.split("[\\s\\p{Punct}]+");
		return tokens;
	}
	

	/**
	 * troncate (7 chars) and clean some special chars
	 * delete final s of plural words
	 * @param tokens
	 * @return
	 */
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
			if (token.length() > 0) {
				// suppression du pluriel
				if (token.length() > 4 && token.charAt(token.length()-1)=='s') {
					token = token.substring(0, token.length()-1);
				}
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
	
	/**
	 * troncate (7 chars) and clean some special chars
	 * delete final s of plural words
	 * @param tokens
	 * @param sws
	 */
	public static void troncateWithScore(String[] tokens, StringWithScore sws) {
		ArrayList<String> tokenList = troncate(tokens);
		sws.setCleanedContentList(tokenList);
	}
	
	/**
	 * deleteDuplicateWords
	 * @param tokens
	 * @return
	 */
	public static ArrayList<String> deleteDuplicateWords(ArrayList<String> tokens) {
		ArrayList<String> listWithoutDuplicates = new ArrayList<>();
		Set<String> hs = new HashSet<>();
		hs.addAll(tokens);
		listWithoutDuplicates.clear();
		listWithoutDuplicates.addAll(hs);
		return listWithoutDuplicates;
	}
	
	/**
	 * clean a string
	 * @param s
	 * @return
	 */
	public static ArrayList<String> cleanString(String s) {
		ArrayList<String> list = new ArrayList<String>();
		s = normalize(s);
		//s = removeNumbers(s);
		s = clean(s);
		String[] tokens = tokenize(s);
		list = troncate(tokens);
		return list;
	}
	
	/**
	 * clean string and delete duplicate words
	 * @param s
	 * @return
	 */
	public static ArrayList<String> cleanReformulationString(String s) {
		ArrayList<String> list = cleanString(s);
		list = deleteDuplicateWords(list);
		ArrayList<String> stopList;
		try {
			stopList = FileManager.readFileContent(Cst.stopListPath,"iso-8859-1");
			list = Parser.deleteTokensFromStopList(list,stopList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * clean string with score (tag version)
	 * @param s
	 */
	public static void cleanStringWithScore(ArrayList<StringWithScore> s) {
		for (StringWithScore sws : s) {
			sws.setContent(normalize(sws.getContent()));
			sws.setContent(removeNumbers(sws.getContent()));
			sws.setContent(clean(sws.getContent()));
			String[] tokens = tokenize(sws.getContent());
			troncateWithScore(tokens, sws);
		}
	}
	
	/**
	 * remove empty items
	 * @param s
	 */
	public static void cleanListOfStringWithScore(ArrayList<StringWithScore> s) {
		Iterator<StringWithScore> iterator = s.iterator();
		while (iterator.hasNext()) {
			StringWithScore sws = iterator.next();
			if (sws.getCleanedContentList().isEmpty()) {
				iterator.remove();
			}
		}				
	}

	/**
	 * print an arraylist of string
	 * @param list
	 */
	public static void printStringArrayList(ArrayList<String> list) {
		for (String e : list) {
			System.out.println(e);
		}
	}
	
	
}
