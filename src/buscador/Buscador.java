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
	
	public ArrayList<DocResponse> querying(String[] query) {
		
		ArrayList<DocResponse> result = new ArrayList<DocResponse>();
		
		HashMap<String, Double> scores = BM25.score(query);
		
		ArrayList<String> topN = BM25.getTopNList(5, scores);
		
		for (int i = 0; i < topN.size(); i++) {
			result.add(new DocResponse(topN.get(i), Reader.getFileByUrl(topN.get(i))));
		}
		
		return result;
	
	}
	
	public ArrayList<DocResponse> querying(String query) {
		
		String[] terms = query.toLowerCase().split("[\\p{Punct}\\s]+");
		ArrayList<String> filtered = new ArrayList<>();
		
		for (String string : terms) {
			if (terms.length > 2) filtered.add(string);
		}
		
		String[] stockArr = new String[filtered.size()];
		stockArr = filtered.toArray(stockArr);
		
		return querying(stockArr);
	}
}
