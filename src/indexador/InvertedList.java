package indexador;

import java.util.ArrayList;
import java.util.HashMap;

public class InvertedList {
	
	private HashMap<String, ArrayList<String>> map;
	
	public InvertedList() {
		map = new HashMap<String, ArrayList<String>>();
	}

	/**
	 * Recebe um index e uma url na qual se encontra este, 
	 * se o index nao existe, cria um campo para ele e adiciona url,
	 * senao so adiciona url nesse index.
	 * 
	 * @param index	O token que serve como index.
	 * @param url	A url que contem esse token (index).
	 * */
	public void addIndex(String index, String url) {
		if (!map.containsKey(index)) {
			map.put(index, new ArrayList<String>());
		}
		if (!map.get(index).contains(url)) {
			map.get(index).add(url);
		}
	}
	
	public ArrayList<String> get(String index) {
		return map.get(index);
	}
	
	public void show() {
		map.forEach((key, value) -> showIndex(key));
	}
	
	
	/**
	 * Mostra a lista de urls nas quais se encontra o index.
	 * 
	 * @param index	Dado um index, mostra a lista de urls nas quais se encontra este.
	 * */
	public void showIndex(String index) {
		ArrayList<String> tmp = get(index);
		
		System.out.print("Key: " + index + ", (");
		for (String url: tmp) {
			System.out.print(url + ", ");
		}
		System.out.println(")");
	}
}
