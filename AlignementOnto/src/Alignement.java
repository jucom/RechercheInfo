import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.Spliterator;
import java.util.function.Consumer;

import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.AlignmentProcess;
import org.semanticweb.owl.align.AlignmentVisitor;
import org.semanticweb.owl.align.Cell;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import fr.inrialpes.exmo.align.impl.URIAlignment;
import fr.inrialpes.exmo.align.impl.eval.PRecEvaluator;
import fr.inrialpes.exmo.align.impl.method.ClassStructAlignment;
import fr.inrialpes.exmo.align.impl.method.EditDistNameAlignment;
import fr.inrialpes.exmo.align.impl.method.NameAndPropertyAlignment;
import fr.inrialpes.exmo.align.impl.method.NameEqAlignment;
import fr.inrialpes.exmo.align.impl.method.SMOANameAlignment;
import fr.inrialpes.exmo.align.impl.renderer.RDFRendererVisitor;
import fr.inrialpes.exmo.align.parser.AlignmentParser;
import fr.inrialpes.exmo.ontowrap.OntowrapException;


public class Alignement {

	public static void generateAlign(String name) throws URISyntaxException ,
	AlignmentException {
		URI onto1 = new URI ("http://oaei.ontologymatching.org/tests/101/onto.rdf") ;
		URI onto2 = new URI ("http://oaei.ontologymatching.org/tests/304/onto.rdf") ;
		AlignmentProcess alignment = null;
		switch (name) {
		case "NameEqAlignment" : 
			alignment = new NameEqAlignment () ;
			break;
			// 70 corres
		case "EditDistNameAlignment" : 
			alignment = new EditDistNameAlignment () ;
			break;
			// 97 corres
		case "SMOANameAlignment" : 
			alignment = new SMOANameAlignment () ;
			break;
			// 95 corres
		case "NameAndPropertyAlignment" : 
			alignment = new NameAndPropertyAlignment () ;
			break;
			// 87 corres
		case "ClassStructAlignment" : 
			alignment = new ClassStructAlignment () ;
			break;
			// 0 corres
		}

		alignment.init ( onto1 , onto2 ) ;
		alignment.align (null , new Properties () ) ;
		System.out.println ("Num corresp.générées : " + alignment.nbCells () ) ;
		try {
			render(alignment,name);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		evaluate(alignment);
	}
	
	public static void generateAlignWithMatcher() throws URISyntaxException ,
	AlignmentException {
		URI onto1 = new URI ("http://oaei.ontologymatching.org/tests/101/onto.rdf") ;
		URI onto2 = new URI ("http://oaei.ontologymatching.org/tests/304/onto.rdf") ;
		Matcher alignment = new Matcher();
		try {
			alignment.init( onto1 , onto2 ) ;
		} catch (OWLOntologyCreationException | OntowrapException e1) {
			e1.printStackTrace();
		}
		alignment.align (null , new Properties (), true ) ;
		System.out.println ("Num corresp.générées : " + alignment.nbCells () ) ;
		try {
			render(alignment,"matcher");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		evaluate(alignment);
	}

	public static void render ( Alignment alignment, String name ) throws
	FileNotFoundException , UnsupportedEncodingException , AlignmentException
	{
		PrintWriter writer ;
		FileOutputStream f = new FileOutputStream (new File (name+".rdf") ) ;
		writer = new PrintWriter ( new BufferedWriter ( new OutputStreamWriter (f ,
				"UTF-8" )) , true ) ;
		AlignmentVisitor renderer = new RDFRendererVisitor ( writer ) ;
		alignment.render ( renderer ) ;
		writer.flush () ;
		writer.close () ;
	}

	/* precision de 1 : aucune erreur dans les docs retournés */
	public static void evaluate ( Alignment alignment ) throws URISyntaxException ,
	AlignmentException {
		URI reference = new URI ("http://oaei.ontologymatching.org/tests/304/refalign.rdf");
		AlignmentParser aparser = new AlignmentParser (0) ;
		Alignment refalign = aparser.parse ( reference );
		PRecEvaluator evaluator = new PRecEvaluator ( refalign , alignment ) ;
		evaluator.eval (new Properties () ) ;
		System.out.println (" Precision : " + evaluator.getPrecision () ) ;
		System.out.println (" Recall :" + evaluator.getRecall () ) ;
		System.out.println (" FMeasure :" + evaluator.getFmeasure () ) ;
	}
	
	public static void evaluationWithLogMap() {
		try {
			AlignmentParser aparser = new AlignmentParser(0) ;
			Alignment al;
			al = aparser.parse ( new URI ("file:/Users/User/Documents/GitHub/RechercheInfo/AlignementOnto/mappings(1).rdf") );
			System.out.println ( al.nbCells () ) ;
			evaluate(al);
		} catch (AlignmentException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//try {
			/*
			generateAlign("NameEqAlignment");
			generateAlign("EditDistNameAlignment");
			generateAlign("SMOANameAlignment");
			generateAlign("NameAndPropertyAlignment");
			generateAlign("ClassStructAlignment");
			*/
			//generateAlignWithMatcher();
			
			evaluationWithLogMap();
			/*
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlignmentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}


}
