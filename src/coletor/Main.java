package coletor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;

import entity.Seeds;
import indexador.Indexador;
import utils.Reader;
import utils.Writer;

public class Main {
	
	public static void coletor() {
		HashMap<String, Document> documents;
		Set<String> seeds;
		ArrayList<String> new_seeds;
		int round;
		String[] urls;
		
		Fetcher.delay = 2000;								// delay to fetch new page
		Seeds.seeds_size = 1;								// number of new seeds are generated.
		round = 0; 											// round counter
		urls = new String[] {"https://en.wikipedia.org/wiki/Barack_obama"};		// first input
		
		Fetcher fetcher = new Fetcher();
		Parser parser = new Parser();
		Scheduler scheduler = new Scheduler();
		Writer writer = new Writer("./output.txt");
		
		scheduler.addNewUrls(new HashSet<String>(Arrays.asList(urls)));
		
		// collecting from web
		while (round < 0) {
			System.out.println("\nRound: " + round);
			
			new_seeds = scheduler.generateNewSeeds();
			System.out.println("Number of seeds: " + new_seeds.size());
			
			documents = fetcher.fetchAll(new_seeds);
			System.out.println("Number of documents: " + documents.size());
			
			seeds = parser.getURLsFromPages(documents);
			System.out.println("Number of new seeds: " + seeds.size());
			
			writer.saveTo(writer.format(parser.getTextsFromPages(documents)));
			
			scheduler.addNewUrls(seeds);
			
			System.out.println("Total of visited seeds: " + Seeds.getVisitedSeeds());
			System.out.println("Total of unvisited seeds: " + Seeds.getUnvisitedSeeds());
			
			round++;
		}
	}
	
	public static void main(String... args) {
		HashMap<String, String> map = new HashMap<String, String>();
		Indexador indexador = new Indexador();
//		Writer writer = new Writer("./output.txt");
//		Reader reader = new Reader("./output.txt");
		
		map.put("1", "sarah is smart.");
		map.put("2", "obama was a president called barack obama.");
		map.put("3", "fritas is smart.");
		map.put("4", "i love smart phone.");
		map.put("5", "dilma is a president.");
		map.put("6", "dehua chen called chen.");
		map.put("7", "sarah is a good student.");
		map.put("8", "sarah love dog.");
		
		indexador.add(map);
		indexador.showInvertedList();
		
//		writer.save(map);
//		
//		map = reader.convertToHashMap(reader.getFromFile());
//		map.forEach((k,v) -> System.out.println("url: " + k + ", content: " + v));
	}
}
