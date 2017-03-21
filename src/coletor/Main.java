package coletor;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;

import entity.Seeds;

public class Main {
	
	public static void main(String... args) throws MalformedURLException, UnknownHostException, URISyntaxException {
		HashMap<String, Document> documents;
		Set<String> seeds;
		ArrayList<String> new_seeds;
		int round;
		String[] urls;
		 
		
		Fetcher.delay = 2000;								// delay to fetch new page
		Seeds.seeds_size = 1;								// number of new seeds are generated.
		round = 0; 											// round counter
		urls = new String[] {"http://www.globo.com/"};		// first input
		
		Fetcher fetcher = new Fetcher();
		Parser parser = new Parser();
		Scheduler scheduler = new Scheduler();
		DNSResolver dns = new DNSResolver();
		
		scheduler.addNewUrls(new HashSet<String>(Arrays.asList(urls)));
		
		while (true) {
			System.out.println("\nRound: " + round);
			
			new_seeds = scheduler.generateNewSeeds();
			System.out.println("Number of seeds: " + new_seeds.size());
			
			dns.addListURL(new_seeds);
			
			documents = fetcher.fetchAll(new_seeds);
			System.out.println("Number of documents: " + documents.size());
			
			seeds = parser.getURLsFromPages(documents);
			System.out.println("Number of new seeds: " + seeds.size());
			
			scheduler.addNewUrls(seeds);
			
			System.out.println("Total of visited seeds: " + Seeds.getVisitedSeeds());
			System.out.println("Total of unvisited seeds: " + Seeds.getUnvisitedSeeds());
			
			round++;
		}
//		parser.getTextsFromPages(fetcher.fetchAll(urls)).forEach((k,v) -> System.out.println(v));
//		parser.getURLsFromPages(fetcher.fetchAll(urls)).forEach(v -> System.out.println(v));
	}
}
