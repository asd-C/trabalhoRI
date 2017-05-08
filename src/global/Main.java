package global;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;

import coletor.DomainUrls;
import coletor.Fetcher;
import coletor.FilterUrls;
import coletor.Parser;
import coletor.Scheduler;
import entity.coletor.Seeds;
import entity.indexador.Doc;
import indexador.Analyser;
import utils.Timer;
import utils.Writer;

public class Main {
	
	public static void coletor() {
		HashMap<String, Document> documents;
		Set<String> seeds;
		ArrayList<String> new_seeds;
		int round;
		String[] urls;
		DomainUrls domainUrls;
		HashMap<String, String> contents;
		
		Fetcher.delay = 500;								// delay to fetch new page
		Seeds.seeds_size = 10;								// number of new seeds are generated.
		round = 0; 											// round counter
		urls = new String[] {"https://en.wikipedia.org/wiki/Category:Living_people"};		// first input
		
		Fetcher fetcher = new Fetcher();
		Parser parser = new Parser();
		Scheduler scheduler = new Scheduler();
		Writer writer = new Writer();
		Timer timer = new Timer();
		Analyser analyser = new Analyser();
		
//		scheduler.addNewUrls(new HashSet<String>(Arrays.asList(urls)));
		
		// collecting from web
		while (round < 2) {
			
			domainUrls = scheduler.generateNewSeeds("en.wikipedia.org");
			
			System.out.println("\n-------------------- Round: " + round + " --------------------\n");
			timer.startTimer(Timer.FILTERURLS);
			new_seeds = FilterUrls.filtrar(domainUrls);
			timer.finishTimer(Timer.FILTERURLS);
			System.out.println("Number of seeds: " + new_seeds.size());
			
			timer.startTimer(Timer.FETCHER);
			documents = fetcher.fetchAll(new_seeds);
			timer.finishTimer(Timer.FETCHER);
			System.out.println("Number of documents: " + documents.size());
			
			timer.startTimer(Timer.PARSER);
			seeds = parser.getURLsFromPages(documents);
			contents = parser.getTextsFromPages(documents);
			timer.finishTimer(Timer.PARSER);
			System.out.println("Number of new seeds: " + seeds.size());
			
			timer.startTimer(Timer.CLASSIFIER);
			Global.classifier.process(contents);
			timer.finishTimer(Timer.CLASSIFIER);
			
			timer.startTimer(Timer.WRITER);
//			parser.getTextsFromPages(documents).forEach((k,v) -> System.out.println(k + v));
			writer.saveWithCompression(parser.getTextsFromPages(documents));
			timer.finishTimer(Timer.WRITER);
			List<Doc> docs = analyser.createIndexes(parser.getTextsFromPages(documents));
			// finished create metadocs
			// finished save documents
			// TODO save urls
			// TODO saving every 5 min
			Global.metaDocManager.addMetaDocsFromDocs(docs);
			Global.invertedIndexManager.addIndexs(analyser.createInvertedIndex(docs));
			
			scheduler.addNewUrls(seeds);
			
			System.out.println("Total of visited seeds: " + Seeds.getVisitedSeeds());
			System.out.println("Total of unvisited seeds: " + Seeds.getUnvisitedSeeds());
			
//			Seeds.showVisitedSeeds();
			
			System.out.println("\n-------------------- End of Round " + round + " --------------------\n");
			round++;
		}
		Global.metaDocManager.saveMetaDocs();
		Global.seedsManager.saveSeeds();
		Global.invertedIndexManager.saveInvertedIndex();
	}
	
	public static void main(String... args) {
		Global.loadData();
		try {
			System.out.println(Global.objectMapper.writeValueAsString(Global.invertedIndexManager.getInvertedIndex().getInvertedIndex().get("Wikipedia")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Main.coletor();
	}
}
