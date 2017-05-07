package entity.indexador;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import global.Global;
import global.Manager;

public class InvertedIndexManager extends Manager{
	private InvertedIndex invertedIndex;
	
	public InvertedIndexManager() {
		loadInvertedIndex();
	}
	
	public synchronized InvertedIndex getInvertedIndex() {
		return invertedIndex;
	}
	
	public synchronized void setInvertedIndex(InvertedIndex invertedIndex) {
		this.invertedIndex = invertedIndex;
	}
	
	public synchronized void addIndex(Index index) {
		invertedIndex.addIndex(index);
	}
	
	public synchronized void addIndexs(List<Index> indexs) {
		invertedIndex.addIndexs(indexs);
	}
	
	public synchronized void addIndexs(HashMap<String, Index> indexs) {
		invertedIndex.addIndexs(indexs);
	}
	
	public synchronized void merge(InvertedIndex invertedIndex) {
		invertedIndex.merge(invertedIndex);
	}
	
	private static String path = Global.pathFormat(
			Global.dir_root, 
			Global.dir_inverted_index, 
			Global.file_inverted_index);

	public synchronized void saveInvertedIndex() {
		try {
			save(Global.objectMapper.writeValueAsString(invertedIndex), path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void loadInvertedIndex() {
		String result = load(path);
		if (result == null) {
			invertedIndex = new InvertedIndex();
		} else {
			try {
				invertedIndex = Global.objectMapper.readValue(result, InvertedIndex.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
