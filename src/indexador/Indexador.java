package indexador;

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
		invertedList.show();
	}

}
