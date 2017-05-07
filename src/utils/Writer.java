package utils;

import java.util.HashMap;

import entity.coletor.Document;
import utils.dataManager.TextCompressor;

public class Writer {
	
	public boolean save(HashMap<String, String> contents) {
		
		contents.forEach((url, text) -> {
			TextCompressor.compress(new Document(url, text, 0), TextCompressor.dir_unproc);
		});
		return true;
	}
}
