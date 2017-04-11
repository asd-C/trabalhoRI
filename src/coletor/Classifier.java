package coletor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations.AnswerAnnotation;
import edu.stanford.nlp.ling.CoreLabel;

public class Classifier {
	
	private static final String MODEL_PATH = "./lib/classifier/english.all.3class.distsim.crf.ser.gz";
	private AbstractSequenceClassifier<CoreLabel> classifier;
	
	public Classifier() {
		this.classifier = prepareClassifier();
	}
	
	private AbstractSequenceClassifier<CoreLabel> prepareClassifier() {
		String model = MODEL_PATH;
		AbstractSequenceClassifier<CoreLabel> classifier = null;
			
			try {
				classifier = CRFClassifier.getClassifier(model);
			} catch (ClassCastException e) { e.printStackTrace(); } 
			catch (ClassNotFoundException e) { e.printStackTrace(); } 
			catch (IOException e) { e.printStackTrace(); }
		
		return classifier;
	}
	
	public HashMap<String, String> process(HashMap<String, String> contents) {
		int start = 0, length = 500, size;
		String content;
		HashMap<String, String> results;
		
		if (classifier != null) {
			results = new HashMap<String, String>();
			
			for (Entry<String, String> ctt: contents.entrySet()) {
				start = 0;
				content = ctt.getValue();
				size = content.length();
				
				while (start + length < size) {
					if (hasEntity(content.substring(start, start+length))) {
						results.put(ctt.getKey(), ctt.getValue());
					}	
					
					start += length;
				}
				
				if (hasEntity(content.substring(start, size))) {
					results.put(ctt.getKey(), ctt.getValue());
				}
			}
			contents = results;
		}
		
		return contents;
	}
	
	public boolean hasEntity(String text) {
		List<List<CoreLabel>> list = classifier.classify(text);
		
		for (List<CoreLabel> lc: list) {
			for (CoreLabel label: lc) {
				if (!label.get(AnswerAnnotation.class).equals("O")) 
					return true;
			}
		}
		
		return false;
	}
}
