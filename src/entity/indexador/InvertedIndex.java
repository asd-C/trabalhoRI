package entity.indexador;

import java.util.HashMap;
import java.util.List;

public class InvertedIndex {
	HashMap<String, Index> invertedIndex;

	public InvertedIndex() {
		invertedIndex = new HashMap<String, Index>();
	}
	public HashMap<String, Index> getInvertedIndex() {
		return invertedIndex;
	}
	public void setInvertedIndex(HashMap<String, Index> invertedIndex) {
		this.invertedIndex = invertedIndex;
	}
	
	public void addIndex(Index index) {
		String key = index.getWord();
		
		// if there is not index yet, put it in hashMap
		if (!invertedIndex.containsKey(key)) {
			invertedIndex.put(key, index);
		} else {
			// put all related documents together
			invertedIndex.get(key).addIndexDocs(index.getIndexDocs());
		}
	}

	public void addIndexs(List<Index> indexs) {
		for (Index index : indexs) {
			addIndex(index);
		}
	}
	
	public void addIndexs(HashMap<String, Index> indexs) {
		InvertedIndex tmp = new InvertedIndex();
		tmp.setInvertedIndex(indexs);
		this.merge(tmp);
	}

	public void merge(InvertedIndex invertedIndex) {
		HashMap<String, Index> tmp = invertedIndex.getInvertedIndex();

		tmp.forEach((word, index) -> {
			addIndex(index);
		});
	}
}
