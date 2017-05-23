package entity.indexador;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import global.Global;
import global.Manager;

public class InvertedIndexManager extends Manager {
	private InvertedIndex invertedIndex;
	private InvertedIndexInfo invertedIndexInfo;

	public InvertedIndexManager() {
		loadInvertedIndex();
	}

	public synchronized InvertedIndexInfo getInvertedIndexInfo() {
		return invertedIndexInfo;
	}

	public synchronized void setInvertedIndexInfo(InvertedIndexInfo invertedIndexInfo) {
		this.invertedIndexInfo = invertedIndexInfo;
	}

	public synchronized InvertedIndex getInvertedIndex() {
		return invertedIndex;
	}

	public synchronized void setInvertedIndex(InvertedIndex invertedIndex) {
		this.invertedIndex = invertedIndex;
	}

	public synchronized void addIndex(Index index) {
		invertedIndex.addIndex(index);
		invertedIndexInfo.addIndex(index);
	}

	public synchronized void addIndexs(List<Index> indexs) {
		invertedIndex.addIndexs(indexs);
		invertedIndexInfo.addIndexs(indexs);
	}

	public synchronized void addIndexs(HashMap<String, Index> indexs) {
		invertedIndex.addIndexs(indexs);
		invertedIndexInfo.addIndexs(indexs);
	}

	public synchronized void merge(InvertedIndex invertedIndex) {
		invertedIndex.merge(invertedIndex);
		invertedIndexInfo.merge(invertedIndex);
	}

	private static String path_info = Global.pathFormat(Global.dir_root, Global.dir_inverted_index,
			Global.file_inverted_index_info);
	
	private static String path_dir = Global.pathFormat(Global.dir_root, Global.dir_inverted_index,
			Global.dir2_inverted_index);

	public synchronized void saveInvertedIndex() {
		try {
			Global.log("Saving inverted index to " + path_dir + "...");
			
			// saving inverted index info
			Global.objectMapper.writeValue(new File(path_info), invertedIndexInfo);

			// saving inverted index
			invertedIndex.getInvertedIndex().forEach((term, index) -> {
				try {
					// retrieve the old one if exists and add all in the new one 
					Index tmp = getIndexByName(term);

					index.addIndexDocs(tmp.getIndexDocs());
					Global.objectMapper.writeValue(new File(path_dir + "/" + term), index);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
//			save(Global.objectMapper.writeValueAsString(invertedIndexInfo), path); // saving with compression
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Index getIndexByName(String name) {
		Index index = new Index();
		
		try {
			index = Global.objectMapper.readValue(new File(path_dir + "/" + name), Index.class);
		} catch (IOException e) { }
		
		return index;
	}
	
	private HashMap<String, Index> getRelevantIndexs() {
		// TODO
		return new HashMap<String, Index>();
	}

	public synchronized void loadInvertedIndex() {

		Global.log("Loading inverted index urls from " + path_dir + "...");

		invertedIndex = new InvertedIndex();
		invertedIndexInfo = new InvertedIndexInfo();
	
		try {
			
			invertedIndexInfo = Global.objectMapper.readValue(new File(path_info), InvertedIndexInfo.class);
			Global.log("Inverted index file is found, with " + invertedIndexInfo.getIndexsWithDetail().size() + " items.");
			
			//TODO load relevant index;
			
		} catch (IOException e) {
//			e.printStackTrace();
			Global.log("Loading inverted index files failed.");

		}
	}

}
