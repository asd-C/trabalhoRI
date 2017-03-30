package indexador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Indexador {
	InvertedList invertedList;
	Tokenizer tokenizer;
	
	public Indexador() {
		invertedList = new InvertedList();
		tokenizer = new Tokenizer();
	}
	
	public void add(HashMap<String, String> map) {
		map.forEach((key, value) -> add(key, tokenizer.process(value)));
	}
	
	public void add(String url, Set<String> words) {
		words.forEach(s -> invertedList.addIndex(s, url));
	}
	
	public void showInvertedList() {
		invertedList.forEach((key, value) -> showIndex(key));
	}
	
	public void showIndex(String index) {
		ArrayList<String> tmp = invertedList.get(index);
		
		System.out.print("Key: " + index + ", (");
		for (String url: tmp) {
			System.out.print(url + ", ");
		}
		System.out.println(")");
	}
}
