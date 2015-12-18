package Parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class ParserWithTags extends Parser {
	
	public static ArrayList<StringWithScore> parseDocumentWithTags(File input, String charsetName) {
		Document doc = null;
		ArrayList<StringWithScore> listOfTextWithScore = new ArrayList<StringWithScore>();
		try {
			doc = Jsoup.parse(input,charsetName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    for ( Element element : doc.getAllElements() )
	    {	    		    	
	        for ( TextNode textNode : element.textNodes() )
	        {
	        	StringWithScore textAndScore = new StringWithScore() ;
	        	if (element.tagName() == "title") {
	        		textAndScore.setScore(20);
	        	}
	        	else if (element.tagName() == "h1") {
	        		textAndScore.setScore(10);
	        	}
	        	else if (element.tagName() == "h2") {
	        		textAndScore.setScore(5);
	        	}
	        	// le score est par défaut de 1
	        	
	            final String text = textNode.text();
	            if (!(text.equals(" "))) {
	            	listOfTextWithScore.add(textAndScore);
	            }
	        }
	    }
	    return listOfTextWithScore;		
	}
	
	public static ArrayList<StringWithScore> initParsingWithTags(File filePath) {
		String[] isoFiles = {"D110.html","D125.html","D77.html","D93.html"};
		String[] unknown8bitFiles = {"D117.html","D118.html","D12.html","D44.html","D46.html","D49.html","D75.html","D89.html","D90.html"};
		ArrayList<StringWithScore> s = new ArrayList<StringWithScore>();
		if(Arrays.asList(isoFiles).contains(filePath.getName()) || Arrays.asList(unknown8bitFiles).contains(filePath.getName())) {
			s = parseDocumentWithTags(filePath,"iso-8859-1");
		}
		else {
			s = parseDocumentWithTags(filePath,"utf-8");
		}
		return s;
	}
	
	public static ArrayList<String> parsingWithTags(File filePath, String stopListPath) {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<StringWithScore> s = initParsingWithTags(filePath);
		//list = Cleaner.cleanString(s);
		/*
		try {
			ArrayList<String> stopList = FileManager.readFileContent(stopListPath,"iso-8859-1");
			list = deleteTokensFromStopList(list,stopList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		return list;
	}
	
	public static void testParserWithTags() {
		String input = "./CORPUS/CORPUS/D88.html";
		String stopListPath = "./stopliste.txt";
		File inputFile = new File(input);
		ArrayList<String> result = new ArrayList<String>();
		result = parsingWithTags(inputFile, stopListPath);
		Cleaner.printStringArrayList(result);
	}
	
	public static void main( String args[] ){
		testParserWithTags();
	}

}
