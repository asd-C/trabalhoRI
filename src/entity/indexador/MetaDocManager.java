package entity.indexador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import global.Global;
import global.Manager;

public class MetaDocManager extends Manager{
	private List<MetaDoc> metaDocs;
	private HashMap<String, Integer> docsSize;

	public MetaDocManager() {
		loadMetaDocs();
	}
	
	public synchronized List<MetaDoc> getMetaDocs() {
		return metaDocs;
	}

	public synchronized void setMetaDocs(List<MetaDoc> metaDocs) {
		this.metaDocs = metaDocs;
		toDocsSize();
	}

	public synchronized void addMetaDoc(MetaDoc metaDoc) {
		metaDocs.add(metaDoc);
		addDocSize(metaDoc);
	}

	public synchronized void addMetaDocs(List<MetaDoc> metaDocs) {
		this.metaDocs.addAll(metaDocs);
		addDocsSize(metaDocs);
	}
	
	public synchronized void addMetaDocsFromDocs(List<Doc> docs) {
		for (Doc doc : docs) {
			metaDocs.add(new MetaDoc(doc.getUrl(), doc.getSize()));
			docsSize.put(doc.getUrl(), doc.getSize());
		}
	}
	
	private void addDocSize(MetaDoc metaDoc) {
		docsSize.put(metaDoc.getUrl(), metaDoc.getSize());
	}
	
	private void addDocsSize(List<MetaDoc> metaDocs) {
		for (MetaDoc metaDoc : metaDocs) {
			docsSize.put(metaDoc.getUrl(), metaDoc.getSize());	
		}
	}
	
	private void toDocsSize() {
		for (MetaDoc metaDoc: metaDocs) {
			docsSize.put(metaDoc.getUrl(), metaDoc.getSize());
		}
	}
	
	private static String path = Global.pathFormat(
			Global.dir_root, 
			Global.dir_metadoc, 
			Global.file_metadoc);

	public synchronized void saveMetaDocs() {
		try {
			Global.log("Saving metadoc to " + path + "...");
			save(Global.objectMapper.writeValueAsString(metaDocs), path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void loadMetaDocs() {
		Global.log("Loading metadoc from " + path + "...");
		
		docsSize = new HashMap<>();
		
		String result = load(path);
		
		if (result == null) {
			Global.log("No metadoc file is found.");
			metaDocs = new ArrayList<MetaDoc>();
		} else {
			try {
				TypeReference<List<MetaDoc>> typeRef = new TypeReference<List<MetaDoc>>() {};

				metaDocs = Global.objectMapper.readValue(result, typeRef);
				Global.log("Metadoc file is found, with " + metaDocs.size() + " items.");
			} catch (IOException e) {
				e.printStackTrace();
				Global.log("Loading metadoc failed.");
			}
		}
		
		toDocsSize();
	}
	
	public synchronized int getSize(String url) {
		return docsSize.get(url);
	}
	
}
