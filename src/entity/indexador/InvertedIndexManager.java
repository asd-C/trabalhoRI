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
	private InvertedIndexCache cache;

	public InvertedIndexManager() {
		loadInvertedIndex();
		cache = new InvertedIndexCache(this);
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

//	public synchronized void addIndex(Index index) {
//		invertedIndex.addIndex(index);
//		invertedIndexInfo.addIndex(index);
//	}

//	public synchronized void addIndexs(List<Index> indexs) {
//		invertedIndex.addIndexs(indexs);
//		invertedIndexInfo.addIndexs(indexs);
//	}

	public synchronized void addIndexs(HashMap<String, Index> indexs) {
		invertedIndex.addIndexs(indexs);
		invertedIndexInfo.addIndexs(indexs);
	}

//	public synchronized void merge(InvertedIndex invertedIndex) {
//		invertedIndex.merge(invertedIndex);
//		invertedIndexInfo.merge(invertedIndex);
//	}
	
	public Index getIndexFromCache(String name) {
		return cache.getIndexByName(name);
	}
	
	public Index getIndexByName(String name) throws IOException{
		
		Index index = null;
		index = Global.objectMapper.readValue(new File(path_dir + "/" + name), Index.class);

		return index;
	}

	public static String path_info = Global.pathFormat(Global.dir_root, Global.dir_inverted_index,
			Global.file_inverted_index_info);
	
	public static String path_dir = Global.pathFormat(Global.dir_root, Global.dir_inverted_index,
			Global.dir2_inverted_index);

	public synchronized void saveInvertedIndex() {

		cache.save();
		
		try {
			Global.log("Saving inverted index to " + path_dir + "...");
			
			// saving inverted index info
			Global.objectMapper.writeValue(new File(path_info), invertedIndexInfo);

			// saving inverted index
			invertedIndex.getInvertedIndex().forEach((term, index) -> {
				try {
					// retrieve the old one if exists and add all in the new one 
					Index tmp = null; 
					
					try {
						tmp = getIndexByName(term);
					} catch (IOException e) {}
					
					if (tmp != null) {
						index.addIndexDocs(tmp.getIndexDocs());
					}
					
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

	public synchronized void loadInvertedIndex() {

		Global.log("Loading inverted index urls from " + path_dir + "...");

		invertedIndex = new InvertedIndex();
		invertedIndexInfo = new InvertedIndexInfo();
	
		try {
			
			invertedIndexInfo = Global.objectMapper.readValue(new File(path_info), InvertedIndexInfo.class);
			Global.log("Inverted index file is found, with " + invertedIndexInfo.getIndexsWithDetail().size() + " items.");

		} catch (IOException e) {
			Global.log("Loading inverted index files failed.");
		}
	}
}

// ranking mechanism, save and load
class InvertedIndexCache {
	
	public static int MAX_CACHE = 3000;
	private int size = 0;
	private HashMap<String, Index> invertedIndex;
	private InvertedIndexManager parent;
	private HashMap<String, Integer> rates;
	
	public InvertedIndexCache(InvertedIndexManager parent) {
		invertedIndex = new HashMap<String, Index>();
		rates = new HashMap<String, Integer>();
		this.parent = parent;
	}

	// update cache, if the requested index is retrieved successfully:
	// 1. if cache is full, remove the less popular and put new one, 
	// 2. if cache is not full, put new one.
	private void update(String name) {
		
		Index newIndex = null;
		
		try {
			newIndex = getIndex(name);
		} catch (IOException e) {}
		
		if (newIndex != null) {
			
			addNewIndex(name, newIndex);
			
			if (size == MAX_CACHE) { // if cache is full, remove less popular from cache.
				removeIndex(getLessPopular());
			} else {
				size++;
			}
		}
	}
	
	private void addNewIndex(String name, Index newIndex) {
		
		invertedIndex.put(name, newIndex);
		rates.put(name, 1);
		
	}
	
	private void removeIndex(String name) {
		
		invertedIndex.remove(name);
		rates.remove(name);
		
	}
	
	// get index from disk, given name
	private Index getIndex(String name) throws IOException{
		
		Index index = null;
		index = Global.objectMapper.readValue(new File(InvertedIndexManager.path_dir + "/" + name), Index.class);
		
		return index;
	}
	
	private void incrementRate(String name) {
		rates.put(name, rates.get(name)+1);
	}
	
	private boolean existsIndex(String name) {
		return (parent.getInvertedIndexInfo().getIndexsWithDetail().get(name) != null);
	}
	
	// get less popular index, remove and save it to disk
	private String getLessPopular() {
		
		int min = Integer.MAX_VALUE;
		String minIdx = null;
		int rate;
		
		for (String key: rates.keySet()) {
			rate = rates.get(key);
			
			if (rate < min) {
				min = rate;
				minIdx = key;
			}
			
		}
		return minIdx;
	}
	
	// get index by name, check it in cache, if it is not in cache, get from disk and add to cache.
	public Index getIndexByName(String name) {
		
		if (existsIndex(name) == false) return new Index(); // if index does not exist, return empty Index.
		
		Index requested = invertedIndex.get(name);
		if (requested != null) {
			incrementRate(name);
			return requested; // if index exists in cache, return from cache.
		}
		
		update(name); // index is not in cache, try to update.
		
		requested = invertedIndex.get(name); // if it failed to update, return null.
		
		return (requested == null ? new Index() : requested); // return empty index if it failed to update.
	}
	
	public void save() {
		
		invertedIndex.forEach((term, index) -> {
			try {
				// retrieve the old one if exists and add all in the new one 
				Index tmp = getIndexByName(term);

				if (tmp != null) {
					index.addIndexDocs(tmp.getIndexDocs());
				}

				Global.objectMapper.writeValue(new File(InvertedIndexManager.path_dir + "/" + term), index);

			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
