package global;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;

import buscador.model.BM25;
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
	
	private static void coletor() {
		HashMap<String, Document> documents;
		Set<String> seeds;
		ArrayList<String> new_seeds;
		int round;
		DomainUrls domainUrls;
		HashMap<String, String> contents;
		
		Fetcher.delay = 100;								// delay to fetch new page
		Seeds.seeds_size = 100;								// number of new seeds are generated.
		round = 0; 											// round counter
		int MAX_ROUND = 2000;
		
		Fetcher fetcher = new Fetcher();
		Parser parser = new Parser();
		Scheduler scheduler = new Scheduler();
		Writer writer = new Writer();
		Timer timer = new Timer();
		Analyser analyser = new Analyser();
		
//		scheduler.addNewUrls(new HashSet<String>(Arrays.asList(urls)));
		
		// collecting from web
		while (round < MAX_ROUND) {
			
			domainUrls = scheduler.generateNewSeeds("en.wikipedia.org");
			
			System.out.println("\n-------------------- Round: " + round + " --------------------\n");

			// get set of urls to fetch
			new_seeds = FilterUrls.filtrar(domainUrls);
			System.out.println("Number of seeds: " + new_seeds.size());

			// fetch pages
			documents = fetcher.fetchAll(new_seeds);
			System.out.println("Number of documents: " + documents.size());

			// get urls from pages
			seeds = parser.getURLsFromPages(documents);
			scheduler.addNewUrls(seeds);
			System.out.println("Number of new seeds: " + seeds.size());

			// get texts from pages
			contents = parser.getTextsFromPages(documents);

			// create indexes and add to inverted index
			List<Doc> docs = analyser.createIndexes(contents);
			Global.invertedIndexManager.addIndexs(analyser.createInvertedIndex(docs));
			Global.metaDocManager.addMetaDocsFromDocs(docs);

			// save texts of pages
			writer.saveWithCompression(contents);


			System.out.println("Total of visited seeds: " + Seeds.getVisitedSeeds());
			System.out.println("Total of unvisited seeds: " + Seeds.getUnvisitedSeeds());
			
//			Seeds.showVisitedSeeds();
//			Global.logSizeOfDocument();

			// save inverted index and clear to not overload memory
			Global.invertedIndexManager.saveInvertedIndex();
			Global.invertedIndexManager.clearInvertedIndex();
			
			Global.saveStatus();

			System.out.println("\n-------------------- End of Round " + round + " --------------------\n");
			round++;
		}

	}
	
	public static void timeToRetrieveIndex() {
		Timer timer = new Timer();
		timer.startTimer(timer.ACCESSTIMETOINDEX);
		try {
			Global.invertedIndexManager.getIndexByName("Wikipedia");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.finishTimer(timer.ACCESSTIMETOINDEX);
	}
	
	public static void collecting() {

		Global.loadData();
		Main.coletor();

	}
	
	public static void querying(String[] query) {
		
		Global.loadDataForSearch();
		
		HashMap<String, Double> scores = BM25.score(query);
		
//		scores = BM25.getTopN(5, scores);
		ArrayList<String> topN = BM25.getTopNList(5, scores);
		
		
		for (int i = 0; i < topN.size(); i++) {
			System.out.println(topN.get(i) + " : " + scores.get(topN.get(i)));
		}
		
	}
	
	public static void main(String... args) {

		collecting();
		
//		String[] query = new String[]{"obama", "barack"};
//		querying(query);
		
	}
}
