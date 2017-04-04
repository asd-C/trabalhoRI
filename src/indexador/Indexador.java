package indexador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Indexador {
	InvertedList invertedList;
	Tokenizer tokenizer;
	
	/**
	 * Construtor
	 * */
	public Indexador() {
		invertedList = new InvertedList();
		tokenizer = new Tokenizer();
	}
	
	/**
	 * Gerar/atualizar a lista invertida.
	 * 
	 * @param map	O par <url, conjunto de tokens>.
	 * */
	public void add(HashMap<String, String> map) {
		map.forEach((key, value) -> add(key, tokenizer.process(value)));
	}
	

	/**
	 * Para cada word (token), cria e insere a url correspondente.
	 * 
	 * @param url	A url na qual se encontra os tokens.
	 * @param words	O conjunto de tokens.
	 * */
	public void add(String url, Set<String> words) {
		words.forEach(s -> invertedList.addIndex(s, url));
	}
	
	/**
	 * Mostrar a lista invertida.
	 * */
	public void showInvertedList() {
		invertedList.forEach((key, value) -> showIndex(key));
	}
	
	/**
	 * Mostra a lista de urls nas quais se encontra o index.
	 * 
	 * @param index	Dado um index, mostra a lista de urls nas quais se encontra este.
	 * */
	public void showIndex(String index) {
		ArrayList<String> tmp = invertedList.get(index);
		
		System.out.print("Key: " + index + ", (");
		for (String url: tmp) {
			System.out.print(url + ", ");
		}
		System.out.println(")");
	}
}
