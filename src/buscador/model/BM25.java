package buscador.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.indexador.Index;
import entity.indexador.IndexDoc;
import entity.indexador.InvertedIndexManager;
import entity.indexador.MetaDocManager;
import global.Global;

public class BM25 {

	public static double K = 0.5;
	public static double B = 0.5;

	// The total number of documents 
	public static int N = 10;
	public static int AVGDL = 1000;
	
	public static HashMap<String, Double> score(String[] query) {
		
		List<Index> indexs 				= getIndexs(query);					// inverted index that only contain the tokens which are in query
		List<String> docs 				= getDocs(indexs);					// all documents that contain indexes
		HashMap<String, Double> scores 	= getScoreMap(docs);		// scores for each document, <Url, Score>
		
		double score;
		int fi;
		int docLength;
		int nQueryI;
		String url;
		
		for (int i = 0; i < docs.size(); i++) {
			
			url = docs.get(i);
			score = scores.get(url);
			
			for (Index index : indexs) {

				fi = findFi(index, url);
				docLength = findDocLength(url);
				nQueryI = index.getNi();

				score += bm25_aux(fi, docLength, AVGDL) * IDF(N, nQueryI);
			}

			scores.put(url, score);
		}

		return scores;
	}
	
	// 
	public static int findDocLength(String url) {
		MetaDocManager manager = Global.metaDocManager;
		
		return manager.getSize(url);
	}
	
	public static int findFi(Index index, String url) {

		List<IndexDoc> docs = index.getIndexDocs();

		for (int j = 0; j < docs.size(); j++) {

			if (docs.get(j).getUrl() == url) {
				return docs.get(j).getFi();
			}

		}

		return 0;
	}
	
	public static double bm25_aux(int fi, int docLength, int avgdl) {
		return (fi * (K + 1.0)) / (fi + K * (1 - B + B * (docLength * 1.0 / avgdl)));
	}
	
	public static double IDF(int N, int nQueryI) {
		double result = (N - nQueryI + 0.5) / (nQueryI + 0.5);
		
		return Math.log(result)/Math.log(2);
	}
	
	// extract urls from indexDocs and put into map with 0 score
	public static HashMap<String, Double> getScoreMap(List<String> docs) {
		
		HashMap<String, Double> scores = new HashMap<String, Double>();
		
		for (String doc : docs) {
			scores.put(doc, 0.0);
		}
		
		return scores;
	}
	
	// extract InvertedIndex using query 
	public static List<Index> getIndexs(String[] query) {
		
		List<Index> indexs = new ArrayList<Index>();
		InvertedIndexManager manager = Global.invertedIndexManager;
		Index index;
		
		for (String token : query) {
			
			index = manager.getIndexByName(token);
			if (index.getNi() != 0) {
				indexs.add(index);
			}

		}
		
		return indexs; 
	}

	// extract Documents which are in the InvertedIndex
	public static List<String> getDocs(List<Index> indexs) {
		Set<String> urls = new HashSet<String>();
		
		for (Index index : indexs) {
			for (IndexDoc doc : index.getIndexDocs()) {
				urls.add(doc.getUrl());
			}
		}
		
		List<String> urlsList = new ArrayList<String>();
		urlsList.addAll(urls);
		
		return urlsList;
	}
	
	public static void main(String[] args) {
		
	}
}
