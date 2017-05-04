package entity.indexador;

import java.util.ArrayList;
import java.util.List;

public class Index {
	String word;
	List<IndexDoc> indexDocs;
	int ni;
	
	public Index() {
		indexDocs = new ArrayList<IndexDoc>();
	}
	
	public Index(Index.Builder builder) {
		this.word = builder.word;
		this.indexDocs = new ArrayList<IndexDoc>();
		this.ni = builder.ni;
	}
	
	public Index(String word, List<IndexDoc> indexDocs, int ni) {
		this.word = word;
		this.indexDocs = indexDocs;
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
	public void addIndexDoc(IndexDoc doc) {
		this.ni++;
		this.indexDocs.add(doc);
	}
	
	public static class Builder {
		String word;
		List<IndexDoc> indexDocs;
		int ni;
		
		public Builder setWord(String word) {
			this.word = word;
			return this;
		}
		public Builder setIndexDocs(List<IndexDoc> indexDocs) {
			this.indexDocs = indexDocs;
			return this;
		}
		public Builder setNi(int ni) {
			this.ni = ni;
			return this;
		}
		public Index build() {
			return new Index(this);
		}
	}
}
