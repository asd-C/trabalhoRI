package entity.indexador;

import java.util.HashMap;
import java.util.List;

public class InvertedIndexInfo {

	private HashMap<String, IndexDetail> indexsWithDetail;

	public InvertedIndexInfo() {
		indexsWithDetail = new HashMap<String, IndexDetail>();
	}

	public InvertedIndexInfo(HashMap<String, IndexDetail> indexsWithDetail) {
		this.indexsWithDetail = indexsWithDetail;
	}

	public HashMap<String, IndexDetail> getIndexsWithDetail() {
		return indexsWithDetail;
	}

	public void setIndexsWithDetail(HashMap<String, IndexDetail> indexsWithDetail) {
		this.indexsWithDetail = indexsWithDetail;
	}

	public void addIndex(Index index) {
		indexsWithDetail.put(index.getWord(), new IndexDetail(0));
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
