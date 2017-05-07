package entity.coletor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import entity.Doc;
import global.Global;
import global.Manager;

public class MetaDocManager extends Manager{
	private List<MetaDoc> metaDocs;

	public MetaDocManager() {
		loadMetaDocs();
	}
	
	public synchronized List<MetaDoc> getMetaDocs() {
		return metaDocs;
	}

	public synchronized void setMetaDocs(List<MetaDoc> metaDocs) {
		this.metaDocs = metaDocs;
	}

	public synchronized void addMetaDoc(MetaDoc metaDoc) {
		metaDocs.add(metaDoc);
	}

	public synchronized void addMetaDocs(List<MetaDoc> metaDocs) {
		this.metaDocs.addAll(metaDocs);
	}
	
	public synchronized void addMetaDocsFromDocs(List<Doc> docs) {
		for (Doc doc : docs) {
			metaDocs.add(new MetaDoc(doc.getUrl(), doc.getSize()));
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
	}
	
}
