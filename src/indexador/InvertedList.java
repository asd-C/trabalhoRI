package indexador;

import java.util.ArrayList;
import java.util.HashMap;

public class InvertedList extends HashMap<String, ArrayList<String>>{
	
	/**
	 * Recebe um index e uma url na qual se encontra este, 
	 * se o index nao existe, cria um campo para ele e adiciona url,
	 * senao so adiciona url nesse index.
	 * 
	 * @param index	O token que serve como index.
	 * @param url	A url que contem esse token (index).
	 * */
	public void addIndex(String index, String url) {
		if (!this.containsKey(index)) {
			this.put(index, new ArrayList<String>());
		}
		if (!this.get(index).contains(url)) {
			this.get(index).add(url);
		}
	}
}
