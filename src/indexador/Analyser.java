package indexador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import coletor.Classifier;
import edu.stanford.nlp.ling.CoreAnnotations.AnswerAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import entity.Doc;
import entity.indexador.Index;
import entity.indexador.IndexDoc;

public class Analyser {
	
	public Doc analyze(List<List<CoreLabel>> list) {
		HashMap<String, Integer> indexs = new HashMap<String, Integer>();
		String word;
		Doc doc = new Doc();
		int size = 0;
		
		for (List<CoreLabel> lc: list) {
			for (CoreLabel label: lc) {
				if (!label.get(AnswerAnnotation.class).equals("O")) {
					word = label.word();
					
					if (indexs.containsKey(word)) {
						indexs.put(word, indexs.get(word) + 1);
					} else {
						indexs.put(word, 1);
					}
				}
				size ++;
			}
		}
		doc.setIndexs(indexs);
		doc.setSize(size);
		return doc;
	}
	
	static Classifier classifier = new Classifier();
	public Doc process(String url, String content) {
		Doc doc = analyze(classifier.classify(content));
		doc.setUrl(url);
		return doc;
	}
	
	public List<Doc> process(HashMap<String, String> contents) {
		ArrayList<Doc> docs = new ArrayList<Doc>();
		contents.forEach((key, value) -> docs.add(process(key, value)));
		
		return docs;
	}
	
	public HashMap<String, Index> reverse(List<Doc> docs) {
		HashMap<String, Index> indexs = new HashMap<String, Index>();
		
		for (Doc doc: docs) {
			doc.getIndexs().forEach((key, value) -> {
				if (!indexs.containsKey(key)) {
					Index.Builder builder = new Index.Builder();
					Index index = builder.setWord(key).build();
					
					indexs.put(key, index);
				}
				
				indexs.get(key).addIndexDoc(new IndexDoc(doc.getUrl(), value));
			});
		}
		
		return indexs;
	}
	
	public static void main(String... args) {
		Analyser an = new Analyser();
		String url1 = "google.com", url2 = "uol.com", url3 = "facebook.com";
		String str1  = "Obama was born in Honolulu, Hawaii, two years after the territory "
				+ "was admitted to the Union as the 50th state. He grew up mostly in Hawaii, "
				+ "but also spent one year of his childhood in Washington State and four years "
				+ "in Indonesia. After graduating from Columbia University in 1983, he worked "
				+ "as a community organizer in Chicago. In 1988 Obama enrolled in Harvard Law "
				+ "School, where he was the first black president of the Harvard Law Review. "
				+ "After graduation, he became a civil rights attorney and professor, teaching "
				+ "constitutional law at the University of Chicago Law School from 1992 to 2004.";
		
		String str2 = "Barack Hussein Obama II (US Listeni/bəˈrɑːk huːˈseɪn oʊˈbɑːmə/ bə-rahk hoo-sayn "
				+ "oh-bah-mə;[1][2] born August 4, 1961) is an American politician who served as "
				+ "the 44th President of the United States from 2009 to 2017. He is the first African "
				+ "American to have served as president, as well as the first born outside the contiguous "
				+ "United States. He previously served in the U.S. Senate representing Illinois from 2005 "
				+ "to 2008, and in the Illinois State Senate from 1997 to 2004.";
		
		String str3 = "Obama represented the 13th District for three terms in the Illinois Senate from 1997 "
				+ "to 2004, when he ran for the U.S. Senate. Obama received national attention in 2004, "
				+ "with his unexpected March primary win, his well-received July Democratic National "
				+ "Convention keynote address, and his landslide November election to the Senate. In "
				+ "2008, Obama was nominated for president, a year after his campaign began, and after "
				+ "a close primary campaign against Hillary Clinton. He was elected over Republican John "
				+ "McCain, and was inaugurated on January 20, 2009. Nine months later, Obama was named "
				+ "the 2009 Nobel Peace Prize laureate.";
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(url1, str1);map.put(url2, str2);map.put(url3, str3);
		
		an.reverse(an.process(map)).forEach((key, value) -> {
			System.out.print(key + ": " + value.getNi() + ", \t");
			value.getIndexDocs().forEach(elem -> {
				System.out.print("(" + elem.url + "," + elem.fi + "), ");
			});
			System.out.println();
		});
	}
}