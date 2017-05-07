package entity.indexador;

import java.util.ArrayList;
import java.util.List;

public class Index {
	private String word;
	private List<IndexDoc> indexDocs;
	private int ni;
	
	public Index() {
		indexDocs = new ArrayList<IndexDoc>();
	}
	public Index(String word, List<IndexDoc> indexDocs, int ni) {
		this.word = word;
		if (indexDocs == null) {
			this.indexDocs = new ArrayList<IndexDoc>();
		} else {
			this.indexDocs = indexDocs;			
		}
		this.ni = ni;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public List<IndexDoc> getIndexDocs() {
		return indexDocs;
	}
	public void setIndexDocs(List<IndexDoc> indexDocs) {
		this.indexDocs = indexDocs;
	}
	public int getNi() {
		return ni;
	}
	public void setNi(int ni) {
		this.ni = ni;
	}
	public void addIndexDoc(IndexDoc indexDoc) {
		this.ni++;
		this.indexDocs.add(indexDoc);
	}
	public void addIndexDocs(List<IndexDoc> indexDocs) {
		for (IndexDoc indexDoc: indexDocs) {
			addIndexDoc(indexDoc);
		}
	}
}
