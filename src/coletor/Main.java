package coletor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations.AnswerAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import entity.Seeds;
import utils.Timer;
import utils.Writer;

public class Main {
	
	private static final String MODEL_PATH = "./lib/classifier/english.all.3class.distsim.crf.ser.gz";

	public static AbstractSequenceClassifier<CoreLabel> prepareClassifier() {
		String model = MODEL_PATH;
		AbstractSequenceClassifier<CoreLabel> classifier = null;
			
			try {
				classifier = CRFClassifier.getClassifier(model);
			} catch (ClassCastException e) { e.printStackTrace(); } 
			catch (ClassNotFoundException e) { e.printStackTrace(); } 
			catch (IOException e) { e.printStackTrace(); }
		
		return classifier;
	}
	
	public static void coletor() {
		HashMap<String, Document> documents;
		Set<String> seeds;
		ArrayList<String> new_seeds;
		int round;
		String[] urls;
		
		Fetcher.delay = 1000;								// delay to fetch new page
		Seeds.seeds_size = 5;								// number of new seeds are generated.
		round = 0; 											// round counter
		urls = new String[] {"https://en.wikipedia.org/wiki/Barack_obama"};		// first input
		
		Fetcher fetcher = new Fetcher();
		Parser parser = new Parser();
		Scheduler scheduler = new Scheduler();
		Timer timer = new Timer();
//		Writer writer = new Writer("./output.txt");
		
		scheduler.addNewUrls(new HashSet<String>(Arrays.asList(urls)));
		
		// collecting from web
		while (round < 10) {
			
//			Seeds.showUnvisitedSeeds();
			
			System.out.println("\n-------------------- Round: " + round + " --------------------\n");
			timer.startTimer(Timer.FILTERURLS);
			new_seeds = FilterUrls.filtrar(scheduler.generateNewSeeds());
			timer.finishTimer(Timer.FILTERURLS);
			System.out.println("Number of seeds: " + new_seeds.size());
			
			timer.startTimer(Timer.FETCHER);
			documents = fetcher.fetchAll(new_seeds);
			timer.finishTimer(Timer.FETCHER);
			System.out.println("Number of documents: " + documents.size());
			
			timer.startTimer(Timer.PARSER);
			seeds = parser.getURLsFromPages(documents);
			timer.finishTimer(Timer.PARSER);
			System.out.println("Number of new seeds: " + seeds.size());
			
//			parser.getTextsFromPages(documents);
			
//			writer.saveTo(writer.format(parser.getTextsFromPages(documents)));
			
			scheduler.addNewUrls(seeds);
			
			System.out.println("Total of visited seeds: " + Seeds.getVisitedSeeds());
			System.out.println("Total of unvisited seeds: " + Seeds.getUnvisitedSeeds());
			
			Seeds.showVisitedSeeds();
			
			System.out.println("\n-------------------- End of Round " + round + " --------------------\n");
			round++;
		}
	}
	
	public static void main(String... args) {
//		prepareClassifier();
		coletor();
//		try {
//			new Parser().removeAnchor("http://en.wikipedia.org/wiki/Barack_obama");
//		} catch (MalformedURLException | URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
