package indexador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Tokenizer {
	
	private Set<String> stopwords;
	
	public Tokenizer() {
		stopwords = new HashSet<String>();
		getStopwordsFromFile("stopwords.txt");
	}
	
	private void getStopwordsFromFile(String filename) {
		try (BufferedReader br = new BufferedReader(new FileReader(filename));) {
			while (br.ready()) {
				stopwords.add(br.readLine());
			}
		} catch (IOException e) {}
	}
	
	public Set<String> process(String content) {
		return removeRepeatedWords(removeStopwords(tokenize(content)));
	}
	
	public String[] tokenize(String content) {
		return content.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
	}
	
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
