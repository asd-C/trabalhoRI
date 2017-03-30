package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Reader {
	public static final String separator_url_content = "&&&&";
	public static final String separator_document = "@@@@";
	
	private String filename;
	
	public Reader(String filename) {
		this.filename = filename;
	}
	
	public HashMap<String, String> load() {
		return convertToHashMap(getFromFile());
	}
	
	public HashMap<String, String> convertToHashMap(String content) {
		HashMap<String, String> map = new HashMap<String, String>();
		
		if (content == null) return map;
		
		String[] elems;
		String[] docs = content.split(separator_document);
		
		for (String doc: docs) {
			elems = doc.split(separator_url_content);
			
			map.put(elems[0], elems[1]);
		}
		
		return map;
	}
	
	public String getFromFile() {
		String content = null;
		
		try (BufferedReader br = new BufferedReader(new FileReader(filename));) {
			content = "";
			
			while(br.ready()) {
				content += br.readLine();
			}
			
		} catch (IOException e) {
			return null;
		}
		return content;
	}
}
