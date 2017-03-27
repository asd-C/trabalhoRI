package coletor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.jsoup.nodes.Document;

import entity.Seeds;
import indexador.Tokenizer;

public class Main {
	
	public static void main(String... args) {
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
		
		String content = "Obama was born in Honolulu, Hawaii, two years after the territory "
				+ "was admitted to the Union as the 50th state. He grew up mostly in Hawaii, "
				+ "but also spent one year of his childhood in Washington State and four "
				+ "years in Indonesia. After graduating from Columbia University in 1983, "
				+ "he worked as a community organizer in Chicago. In 1988 Obama enrolled "
				+ "in Harvard Law School, where he was the first black president of the "
				+ "Harvard Law Review. After graduation, he became a civil rights attorney "
				+ "and professor, teaching constitutional law at the University of Chicago "
				+ "Law School from 1992 to 2004. Obama represented the 13th District for "
				+ "three terms in the Illinois Senate from 1997 to 2004, when he ran for "
				+ "the U.S. Senate. Obama received national attention in 2004, with his "
				+ "unexpected March primary win, his well-received July Democratic National "
				+ "Convention keynote address, and his landslide November election to the Senate. "
				+ "In 2008, Obama was nominated for president, a year after his campaign began, "
				+ "and after a close primary campaign against Hillary Clinton. He was elected "
				+ "over Republican John McCain, and was inaugurated on January 20, 2009. "
				+ "Nine months later, Obama was named the 2009 Nobel Peace Prize laureate.";
		
		Tokenizer tokenizer = new Tokenizer();
		tokenizer.removeStopwords(tokenizer.tokenize(content));
		
//		for (String tmp: tokens) {
//			System.out.println(tmp);
//		}
	}
}
