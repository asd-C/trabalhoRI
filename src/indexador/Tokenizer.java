package indexador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Tokenizer {
	
	private Set<String> stopwords;
	
	/**
	 * Construtor
	 * */
	public Tokenizer() {
		stopwords = new HashSet<String>();
		getStopwordsFromFile("stopwords.txt");
	}
	
	/**
	 * Criar base de stopwords para filtro.
	 * 
	 * @param filename	o arquivo no qual se encontra o conjunto de stopwords.
	 * */
	private void getStopwordsFromFile(String filename) {
		try (BufferedReader br = new BufferedReader(new FileReader(filename));) {
			while (br.ready()) {
				stopwords.add(br.readLine());
			}
		} catch (IOException e) {}
	}
	
	/**
	 * Processa um string, gera um conjunto de tokens nao repetidos e sem stopwords.
	 * 
	 * @param content	O conteudo a ser processado.
	 * @return		O conjunto de tokens.
	 * */
	public Set<String> process(String content) {
		return removeRepeatedWords(removeStopwords(tokenize(content)));
	}
	
	/**
	 * Dado um string, gera tokens a partir deste, remove numeros 
	 * e convertendo todos caracteres para minusculos.
	 * 
	 * @param content	O conteudo a ser processado.
	 * @return		O conjunto de tokens.
	 * */
	public String[] tokenize(String content) {
		return content.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
	}
	
	/**
	 * Dado um conjunto de tokens, remove os stopwords.
	 * 
	 * @param tokens	O conjunto de tokens a serem processados.
	 * @return			O conjunto de tokens processados (sem stopwords).
	 * */
	public String[] removeStopwords(String[] tokens) {
		ArrayList<String> results = new ArrayList<String>();
		
		for (String token: tokens) {
			if (!stopwords.contains(token)) {
				results.add(token);
				System.out.print(token + " ");
			}
		}
		System.out.println();
		
		String[] stockArr = new String[results.size()];
		stockArr = results.toArray(stockArr);
		return stockArr;
	}
	
	/**
	 * Dado um conjunto de tokens, remove os tokens repetidos.
	 * 
	 * @param words	O conjunto de tokens a serem processados.
	 * @return		O conjunto de tokens processados (nao repetidos).
	 * */
	public Set<String> removeRepeatedWords(String[] words) {
		HashSet<String> no_repeated_words = new HashSet<String>();
		
		for (String word: words) {
			if (!no_repeated_words.contains(word)) {
				no_repeated_words.add(word);
			}
		}
		
		return no_repeated_words;
	}

}
