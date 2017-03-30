package indexador;

import java.util.ArrayList;
import java.util.HashMap;

public class InvertedList extends HashMap<String, ArrayList<String>>{
	
	public void addIndex(String index, String url) {
		if (!this.containsKey(index)) {
			this.put(index, new ArrayList<String>());
		}
		if (!this.get(index).contains(url)) {
			this.get(index).add(url);
		}
	}
}
