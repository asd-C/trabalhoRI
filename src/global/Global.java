package global;

import org.codehaus.jackson.map.ObjectMapper;

import coletor.Classifier;
import entity.indexador.InvertedIndexManager;

public class Global {
	public static ObjectMapper objectMapper = new ObjectMapper();
	public static Classifier classifier = new Classifier();
	public static InvertedIndexManager invertedIndexManager = new InvertedIndexManager();
}
