package indexador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.CoreAnnotations.AnswerAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import entity.indexador.Doc;
import entity.indexador.Index;
import entity.indexador.IndexDoc;
import entity.indexador.MetaDoc;
import global.Global;

public class Analyser {

	public Doc analyze(List<List<CoreLabel>> list) {
		HashMap<String, Integer> indexs = new HashMap<String, Integer>();
		String word;
		Doc doc = new Doc();
		int size = 0;

		for (List<CoreLabel> lc : list) {
			for (CoreLabel label : lc) {
				if (label.word().length() > 2 
						&& !label.get(AnswerAnnotation.class).equals("O")
						&& !Pattern.matches("\\p{Punct}", label.word())) {
					word = label.word();

					if (indexs.containsKey(word)) {
						indexs.put(word, indexs.get(word) + 1);
					} else {
						indexs.put(word, 1);
					}
				}
				size++;
			}
		}
		doc.setIndexs(indexs);
		doc.setSize(size);
		return doc;
	}

	public Doc createIndex(String url, String content) {
		// The document with indexes (<index>, number of occurrences)
		// and document length
		Doc doc = analyze(Global.classifier.classify(content));
		doc.setUrl(url);
		return doc;
	}

	public List<Doc> createIndexes(HashMap<String, String> contents) {
		ArrayList<Doc> docs = new ArrayList<Doc>();
		contents.forEach((key, value) -> docs.add(createIndex(key, value)));

		return docs;
	}
	
	public List<MetaDoc> createMetaDoc(List<Doc> docs) {
		ArrayList<MetaDoc> metaDocs = new ArrayList<>();
		docs.forEach((doc) -> {
			metaDocs.add(new MetaDoc(doc.getUrl(), doc.getSize()));
		});
		
		return metaDocs;
	}

	public HashMap<String, Index> createInvertedIndex(List<Doc> docs) {
		HashMap<String, Index> indexs = new HashMap<String, Index>();

		for (Doc doc : docs) {
			doc.getIndexs().forEach((key, value) -> {
				if (!indexs.containsKey(key)) {
					Index index = new Index(key, null, 0);

					indexs.put(key, index);
				}

				indexs.get(key).addIndexDoc(new IndexDoc(doc.getUrl(), value));
			});
		}

		return indexs;
	}
}