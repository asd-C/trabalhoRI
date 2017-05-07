package entity.indexador;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import global.Global;

public class InvertedIndexManager {
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
	
	// TODO compress and save object in file
	public synchronized void saveInvertedIndex() {
		try {
			Global.objectMapper.writeValueAsString(invertedIndex);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// TODO uncompress and load object in file
	public synchronized void loadInvertedIndex() {
		invertedIndex = new InvertedIndex();
	}
	
	public static void main(String[] args) {
		IndexDoc d1 = new IndexDoc("face1.com", 1);
		IndexDoc d2 = new IndexDoc("face2.com", 1);
		IndexDoc d3 = new IndexDoc("face3.com", 1);
		IndexDoc d4 = new IndexDoc("face4.com", 1);
		
		Index i1 = (new Index("hello1", null, 0));
		Index i2 = (new Index("hello2", null, 0));
		Index i3 = (new Index("hello1", null, 0));
		Index i4 = (new Index("hello2", null, 0));
		
		i1.addIndexDoc(d1);
		i2.addIndexDoc(d1);
		
		InvertedIndex invertedIndex = new InvertedIndex();
		InvertedIndex invertedIndex2 = new InvertedIndex();
		invertedIndex.addIndex(i1);
		invertedIndex.addIndex(i2);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			invertedIndex2 = mapper.readValue(mapper.writeValueAsString(invertedIndex), InvertedIndex.class);
			System.out.println(mapper.writeValueAsString(invertedIndex));
		} catch (Exception e) {
		}
		//invertedIndex.showInvertedIndex();
		invertedIndex.merge(invertedIndex2);
//		i3.addIndexDoc(d2);
//		i4.addIndexDoc(d2);
//		invertedIndex.addIndex(i3);
//		invertedIndex.addIndex(i4);
		try {
			System.out.println(mapper.writeValueAsString(invertedIndex));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
