package global;

import java.util.ArrayList;
import java.util.HashMap;
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
		DomainUrls domainUrls;
		HashMap<String, String> contents;
		
		Fetcher.delay = 0;								// delay to fetch new page
		Seeds.seeds_size = 50;								// number of new seeds are generated.
		round = 0; 											// round counter
		
		Fetcher fetcher = new Fetcher();
		Parser parser = new Parser();
		Scheduler scheduler = new Scheduler();
		Writer writer = new Writer();
		Timer timer = new Timer();
		Analyser analyser = new Analyser();
		
//		scheduler.addNewUrls(new HashSet<String>(Arrays.asList(urls)));
		
		// collecting from web
		while (round < 40) {
			
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
			contents = Global.classifier.process(contents);
			timer.finishTimer(Timer.CLASSIFIER);
			
			timer.startTimer(timer.INDEXADOR);
			List<Doc> docs = analyser.createIndexes(contents);
			// finished create metadocs
			// finished save documents
			// TODO save urls
			// TODO saving every 5 min
			Global.invertedIndexManager.addIndexs(analyser.createInvertedIndex(docs));
			timer.finishTimer(timer.INDEXADOR);
			
			Global.metaDocManager.addMetaDocsFromDocs(docs);
			scheduler.addNewUrls(seeds);

			timer.startTimer(Timer.WRITER);
			writer.saveWithCompression(contents);
			timer.finishTimer(Timer.WRITER);
			
			System.out.println("Total of visited seeds: " + Seeds.getVisitedSeeds());
			System.out.println("Total of unvisited seeds: " + Seeds.getUnvisitedSeeds());
			
//			Seeds.showVisitedSeeds();
			Global.logSizeOfDocument();
			
			System.out.println("\n-------------------- End of Round " + round + " --------------------\n");
			round++;
		}
//		Global.saveStatus();
	}
	
	public static void timeToRetrieveIndex() {
		Timer timer = new Timer();
		timer.startTimer(timer.ACCESSTIMETOINDEX);
		Global.invertedIndexManager.getIndexByName("Wikipedia");
		timer.finishTimer(timer.ACCESSTIMETOINDEX);
	}
	
	public static void main(String... args) {
		Global.loadData();
//		Main.coletor();
//		
		timeToRetrieveIndex();
	}
}
