package coletor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;

import entity.Seeds;
import utils.Writer;

public class Main {

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
		Writer writer = new Writer("./output.txt");
		
		scheduler.addNewUrls(new HashSet<String>(Arrays.asList(urls)));
		
		// collecting from web
		while (round < 10) {
			
//			Seeds.showUnvisitedSeeds();
			
			System.out.println("\nRound: " + round);
			
			new_seeds = FilterUrls.filtrar(scheduler.generateNewSeeds());
			System.out.println("Number of seeds: " + new_seeds.size());
			
			documents = fetcher.fetchAll(new_seeds);
			System.out.println("Number of documents: " + documents.size());
			
			seeds = parser.getURLsFromPages(documents);
			System.out.println("Number of new seeds: " + seeds.size());
			
			writer.saveTo(writer.format(parser.getTextsFromPages(documents)));
			
			scheduler.addNewUrls(seeds);
			
			System.out.println("Total of visited seeds: " + Seeds.getVisitedSeeds());
			System.out.println("Total of unvisited seeds: " + Seeds.getUnvisitedSeeds());
			
			Seeds.showVisitedSeeds();
			
			round++;
		}
	}
	
	public static void main(String... args) {
		coletor();
//		try {
//			new Parser().removeAnchor("http://en.wikipedia.org/wiki/Barack_obama");
//		} catch (MalformedURLException | URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
