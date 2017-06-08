package buscador;

import java.util.ArrayList;
import java.util.HashMap;

import buscador.model.BM25;
import global.Global;
import global.MainInterface;
import global.entity.DocResponse;
import utils.Reader;

public class Buscador implements MainInterface{
	
	public Buscador() {
		Global.loadDataForSearch();
	}
	
	@Override
	public ArrayList<DocResponse> queryingTopN(String[] query, int n) {
		
		ArrayList<DocResponse> result = new ArrayList<DocResponse>();
		
		HashMap<String, Double> scores = BM25.score(query);
		
		ArrayList<String> topN = BM25.getTopNList(n, scores);
		
		int stop_point = (topN.size() < n) ? topN.size() : n;
		
		for (int i = 0; i < stop_point; i++) {
			result.add(new DocResponse(topN.get(i), Reader.getFileByUrl(topN.get(i))));
		}
		
		return result;
	
	}
	
	@Override
	public ArrayList<DocResponse> queryingTopN(String query, int n) {
		
		String[] terms = query.toLowerCase().split("[\\p{Punct}\\s]+");
		ArrayList<String> filtered = new ArrayList<>();
		
		for (String string : terms) {
			if (string.length() > 2) filtered.add(string);
		}
		
		String[] stockArr = new String[filtered.size()];
		stockArr = filtered.toArray(stockArr);
		
		return queryingTopN(stockArr, n);

	}

	@Override
	public ArrayList<DocResponse> querying(String query) {
		return queryingTopN(query, Integer.MAX_VALUE);
	}

	@Override
	public ArrayList<DocResponse> querying(String[] query) {
		return queryingTopN(query, Integer.MAX_VALUE);
	}
}
