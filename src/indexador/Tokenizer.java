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

}
